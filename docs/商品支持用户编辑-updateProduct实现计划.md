# 商品支持用户编辑 updateProduct 实现计划

## 1. 目标

实现前端保存商品编辑接口：

- Controller：`cn.mathmagic.jjewelry.business.controller.product.ProductController#updateProduct`
- Path：`POST /aigc/trade/product/update`
- 入参：`ProductUpdateCmd`
- 业务目标：PGC 创作者白名单用户编辑自己创建的商品后，进入商品审核流程；审核通过前不直接覆盖线上商品正式信息。

本计划先描述实现方式，不写业务代码。

## 2. 已确认现状

1. `ProductController#updateProduct` 当前只调用 `publishProductProxy.updateSelection(productUpdateRequest)`，而 `PublishProductProxy#updateSelection` 为空实现。
2. `ProductUpdateCmd` 已包含本需求字段：`productId`、`productName`、`description`、`detail`、`mainImageInfoDtoList`。
3. 商品详情接口已返回 `editStatus`，取值与技术方案一致：`0=不展示`、`1=待审核`、`2=可编辑`。
4. `ProductReadServiceImpl#getEditStatus` 已按“当前登录用户必须是商品作者，且作者在 `NacosConfig.canEditProductUserList` 白名单中”校验；非作者或非白名单用户会返回 `DEFAULT(0)`。
5. 已有审核状态枚举 `ProductAuditStatusEnum`：`0=初始化`、`1=审核中`、`2=审核通过`、`3=审核拒绝`。
6. 已有审核历史表实体/仓储 `ProductAuditHistory`、`ProductAuditHistoryRepository`，但代码实体还没有 `afterProductSnapshot` 字段。
7. PDF 技术方案要求 `trade_pd_product_audit_history` 增加 `after_product_snapshot`，用于保存修改后的商品快照。
8. 主图 URL 正式字段在 `trade_pd_product.product_images`，主图宽高信息在 `trade_pd_product_ext.main_image_info`，商品详情在 `trade_pd_product_ext.detail`。

## 3. 总体方案

采用“提交审核保存待审快照，审核通过再落正式商品表”的方案：

1. 用户调用 `/product/update` 保存编辑内容。
2. 后端校验登录态、作者身份、白名单、商品状态、审核状态、参数合法性。
3. 查询当前商品正式数据，组装 `beforeSnapshot`。
4. 根据 `ProductUpdateCmd` 与当前商品数据组装 `afterSnapshot`，仅包含本次允许用户编辑的字段变更。
5. 在一个事务内：
   - 将 `trade_pd_product.audit_status` 更新为 `UNDER_REVIEW(1)`。
   - 写入 `trade_pd_product_audit_history`，`operation_type=1`，`product_snapshot=beforeSnapshot`，`after_product_snapshot=afterSnapshot`。
6. 草稿商品审核通过时，由 `DraftProductAuditService` 从最新一条待审历史的 `after_product_snapshot` 读取修改内容，再写入正式表：
   - `trade_pd_product.product_name`
   - `trade_pd_product.description`
   - `trade_pd_product.product_images`
   - `trade_pd_product_ext.detail`
   - `trade_pd_product_ext.main_image_info`
   - `trade_pd_product.audit_status=APPROVED(2)`
   - 是否立即上架由草稿审核接口入参控制，需要上架时再调用 `productWriteService.upOrDown(..., PUBLISHED)`
7. 草稿商品审核拒绝时只更新 `audit_status=REJECTED(3)`，并更新提交审核时创建的同一条审核记录，不覆盖正式商品内容。
8. 普通商品审核继续保留 `ProductAuditService` 原有实现：审核通过/驳回时新增一条审核历史，不把草稿快照逻辑混入普通审核链路。

这样可以保证审核期间线上商品仍展示旧内容，符合审核链路预期，也能让运营侧通过快照查看编辑前后差异。

## 4. 具体改动清单

### 4.1 DTO 和实体

1. `ProductUpdateCmd`
   - 将 `mainImageInfoDtoList` 上的 `@Min/@Max` 调整为 `@Size(min = 2, max = 5)`。
   - 保留 `@NotEmpty`，继续要求至少上传主图。
   - 必要时补充字段注释，说明 `mainImageInfoDtoList.imageUrl` 会作为正式主图顺序。

2. `ProductAuditHistory`
   - 新增字段 `afterProductSnapshot`，映射表字段 `after_product_snapshot`。

3. `ProductAuditHistoryDto`
   - 新增 `afterProductSnapshot`，方便运营接口返回修改后快照。

4. 新增或复用一个快照 DTO
   - 建议新增 `ProductAuditSnapshotDto`，字段只包含审核需要的商品展示信息：
     - `productId`
     - `productName`
     - `description`
     - `detail`
     - `productImages`
     - `mainImageInfoDtoList`
   - 不建议直接把完整 `ProductDto/Product` 作为待审修改快照，避免把价格、SKU、类目、库存等不在本需求范围内的字段误带入审核通过写表逻辑。

### 4.2 Repository/Service

1. `ProductAuditHistoryRepository`
   - 新增保存提交审核历史的方法，例如 `saveSubmitHistory(productId, beforeSnapshot, afterSnapshot, operator)`。
   - 新增查询最新待审提交记录的方法，例如 `getLatestSubmitHistory(productId)`，用于审核通过读取待审快照。
   - 新增按 `productIdList` 批量查询最新待审修改快照的方法，用于运营平台批量展示 `after_product_snapshot`。
   - 继续保留现有 `saveHistory`，避免影响已有审核通过/拒绝调用。

2. `ProductAuditService`
   - 保持普通商品审核原有实现。
   - `approve(productId)` 继续在审核通过时新增审核历史并直接上架。
   - `reject(productId, remark)` 继续在审核拒绝时新增审核历史。
   - 不承载草稿提交、草稿快照落表、草稿审核记录状态更新等逻辑。

3. 新增 `DraftProductAuditService`
   - `submitAudit(productId, beforeSnapshot, afterSnapshot, operator)`：
     - 只做草稿审核相关逻辑和快照存储。
     - 校验商品 ID、快照非空且快照商品 ID 与入参一致。
     - 使用条件更新将 `audit_status` 置为 `UNDER_REVIEW(1)`，避免重复提交覆盖待审内容。
     - 插入一条草稿提交审核历史，`operation_type=UNDER_REVIEW(1)`，保存 `product_snapshot` 和 `after_product_snapshot`。
   - `approve(productId, publishImmediately)`：
     - 读取最新草稿提交记录的 `after_product_snapshot`。
     - 将快照内容落正式商品表，再将商品审核状态改为 `APPROVED(2)`。
     - 更新提交审核时创建的同一条审核历史为 `APPROVED(2)`，不新增审核历史。
     - `publishImmediately=true` 时调用 `productWriteService.upOrDown(..., PUBLISHED)`；为 false 时不改变商品发布状态。
   - `reject(productId, remark)`：
     - 不落正式表。
     - 将商品审核状态改为 `REJECTED(3)`。
     - 更新提交审核时创建的同一条审核历史为 `REJECTED(3)`，不新增审核历史。

4. 运营平台快照查询接口
   - 在内部读接口中新增批量查询商品修改快照方法，入参为 `productIdList`。
   - 返回每个商品最新待审 `after_product_snapshot`，没有待审修改快照的商品返回空结果或不返回该商品，具体按现有接口返回风格实现。
   - 该接口只用于运营平台审核展示，不影响 C 端商品详情、列表、搜索等对外展示。

5. `PublishProductProxy#commitDraft`
   - 实现 C 端保存入口的编排：
     - 获取 `AuthContext.getCurrentUserId()`，仅在当前线程使用。
     - 调用商品编辑参数校验工具，覆盖必填、主图数量/宽高、XSS、SQL 注入风险、富文本白名单、图片 URL 格式等。
     - 查询当前商品及扩展信息。
     - 校验当前登录用户是商品作者。
     - 复用 `ProductReadService#getEditStatus(auditStatusEnum, currentUserId, authorUserId)` 校验作者和白名单，要求返回 `EDITABLE(2)`。
     - 构建 before/after 快照。
     - 调用 `DraftProductAuditService` 提交草稿审核。
   - 不开子线程，避免 `AuthContext` 的 `InheritableThreadLocal` 污染风险。

6. 正式落表方法
   - 可在 `ProductWriteService`/`ProductExtWriteService` 增加小范围方法，例如：
     - `updateUserEditableFields(productId, productName, description, productImages)`
     - `updateUserEditableExt(productId, detail, mainImageInfoDtoList)`
   - 只更新本需求允许字段，不复用大而全的 `ProductCreateCmd` 更新链路，避免误触发 SPU、类目、SKU、属性等无关逻辑。

### 4.3 Controller

`ProductController#updateProduct` 保持接口形态不变：

```java
@PostMapping("update")
public CommonJsonObject<Void> updateProduct(@RequestBody @Valid ProductUpdateCmd productUpdateRequest) {
    publishProductProxy.commitDraft(productUpdateRequest);
    return ResponseDataUtil.buildSuccess();
}
```

实现重点放在 Service 层，Controller 不承载业务规则。

### 4.4 商品详情编辑状态

`ProductReadServiceImpl#getEditStatus` 已完成前置展示态校验，当前逻辑为：

1. `auditStatusEnum` 为空：返回 `DEFAULT(0)`。
2. 当前登录用户不是作者：返回 `DEFAULT(0)`。
3. 作者不在 `NacosConfig.canEditProductUserList`：返回 `DEFAULT(0)`。
4. 作者在白名单：
   - `UNDER_REVIEW` 返回 `UNDER_REVIEW(1)`。
   - `INIT/APPROVED/REJECTED` 返回 `EDITABLE(2)`。

`updateProduct` 服务端仍需重复做作者和白名单校验，避免绕过前端按钮态直接调用接口；实现上复用 `getEditStatus`，不在审核服务里再单独读取白名单。

## 5. 校验规则

1. 登录校验
   - `AuthContext.getCurrentUserId()` 为空时返回 token 错误或参数错误，按仓库现有风格处理。

2. 商品存在校验
   - `productId` 必须能查询到未删除商品。

3. 作者校验
   - 通过 `ProductCreatorReadService#getByProductIdWithDelete` 或现有读取链路获取作者。
   - 只有 `ProductCreatorDto.uniqueUserId == currentUserId` 才允许提交。

4. 白名单校验
   - 复用 `ProductReadService#getEditStatus`，当前登录用户必须是作者且作者必须在 `NacosConfig.canEditProductUserList` 中。

5. 审核状态校验
   - `UNDER_REVIEW(1)`：拒绝重复提交，提示“商品审核中，请勿重复提交”。
   - `INIT/APPROVED/REJECTED`：允许提交。
   - 其他未知状态：拒绝并打印 warn 日志。

6. 字段校验
   - `productName/description/detail` 非空，建议额外 trim 后校验。
   - `mainImageInfoDtoList` 数量 2 到 5。
   - 每张主图 `imageUrl` 非空。
   - `width/height` 大于 0；`whRate` 大于 0。
   - 主图顺序以请求数组顺序为准。
   - `productId` 仅允许字母、数字、下划线、短横线，长度不超过 64。
   - `productName/description` 不允许 HTML 标签、脚本协议、事件属性等 XSS 风险内容。
   - `detail` 按商品详情富文本处理，使用 HTML 白名单校验；非法标签、脚本协议、事件属性等内容直接拒绝。
   - 文本、富文本、图片 URL 均做 SQL 注入风险片段校验，例如 `union select`、`' or 1=1`、`drop table`、`insert into`、`delete from` 等。
   - 主图 URL 只允许 `http/https`，且必须包含 host，不允许换行。

## 6. 事务与并发

1. 提交审核使用事务，确保 `audit_status=UNDER_REVIEW` 和审核历史同时成功或同时回滚。
2. 更新审核状态建议带条件：
   - `where product_id = ? and audit_status <> UNDER_REVIEW`
   - 或在事务中先读后更新，并检查影响行数。
3. 不需要在循环里调用 RPC/DB；本接口单商品更新，查询当前商品、扩展信息、作者信息各一次即可。
4. 如担心同一商品并发提交，可增加商品维度分布式锁，key 类似 `product:update:{productId}`；第一版也可先用 DB 条件更新保证不重复进入审核中。

## 7. 日志

建议记录少量关键日志：

1. 提交成功：`productId/currentUserId/auditHistoryId`。
2. 权限拒绝：`productId/currentUserId/authorUserId`，warn 级别。
3. 审核通过应用快照失败：error 级别，带 `productId/historyId`。

日志不要打印完整商品详情和图片大 JSON，避免日志过大。

## 8. 测试计划

### 8.1 单元测试/集成测试建议

优先增加 `ProductController` 或 `ProductAuditService` 维度测试：

1. 白名单作者提交成功
   - 断言商品 `audit_status=UNDER_REVIEW`。
   - 断言审核历史写入 `product_snapshot` 和 `after_product_snapshot`。
   - 断言正式商品表字段未立即变化。

2. 非作者提交失败
   - 断言不更新 `audit_status`，不写审核历史。

3. 作者不在白名单提交失败
   - 断言不更新 `audit_status`，不写审核历史。

4. 审核中重复提交失败
   - 断言原待审快照不被覆盖。

5. 主图数量小于 2 或大于 5 校验失败
   - 覆盖 `@Size` 生效。

6. 审核通过应用待审快照
   - 断言正式表 `product_name/description/product_images/detail/main_image_info` 被更新。
   - 断言 `audit_status=APPROVED`。
   - 覆盖 `approve` 立即上架参数：参数为 true 时上架，参数为 false 时不改变发布状态。

7. 审核拒绝不应用待审快照
   - 断言正式商品字段不变，`audit_status=REJECTED`。

### 8.2 手工验证

1. 商品详情作者白名单用户看到 `editStatus=2`。
2. 保存后再次查询商品详情，`editStatus=1`。
3. 运营待审核列表能查到该商品。
4. 运营审核通过后，C 端商品详情展示新标题、新描述、新主图、新详情。
5. 运营审核拒绝后，C 端商品详情仍展示旧内容，作者可再次编辑。

## 9. 待确认点

1. 普通审核接口保持原有行为；运营审核草稿商品时通过新增入参 `draftProductAudit=true` 走 `DraftProductAuditService`。
2. 给运营平台提供批量查询商品修改快照的方法，按 `productIdList` 批量返回最新待审 `after_product_snapshot`，用于列表或详情页展示修改内容。
3. 草稿对外不可见：提交编辑后的待审快照只作为审核草稿保存，不影响 C 端商品详情、列表、搜索等对外展示，审核通过后才落正式表。
