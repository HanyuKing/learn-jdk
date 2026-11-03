# 优惠券 Code Review 1：核对结果与修复计划

本文对应 `code_review_1.md` 的 12 条意见。修复已于 2026-10-22 实施，首次验收通过 60 个服务测试及 24 个 API/dev 测试，client/service/api/job 构建通过。本轮 Lua 简化及作废事务调整后，66 个相关服务测试和 8 个 dev 集成测试通过，client/service/api 构建通过。实施与验收边界见第七、八节。

## 一、已确认的业务口径

- 历史券只包含 `LOCKED / USED / EXPIRED`。商品下架只影响现有提示和跳转，不新增历史券状态。
- 历史券先按 `LOCKED → USED → EXPIRED` 分组，组内按有效期结束时间倒序，时间相同按 couponId 倒序。继续在内存筛选、排序后分页，不新增 lock_time 字段。
- 模板 `name` 只供运营使用；`description` 供 C 端展示，改为必填、最多 6 个字符，并增加代码校验。
- 业务时间继续使用 `Long / BIGINT` 毫秒时间戳。`createTime / modifiedTime` 继续继承 `BaseEntity`，使用字符串，数据库保留 DATETIME 审计字段。
- 已有接口日期字符串继续按 `Asia/Shanghai` 解释和展示。
- 新业务不提供数据库迁移脚本，直接修改建表 SQL。
- 创建模板保持当前代码顺序：先保存模板、范围和操作日志，再写 Redis，两步在同一个 MySQL 事务回调内执行；Redis 写入失败抛出异常，使 MySQL 事务回滚。不增加缓存补偿回滚或数据库回滚后的缓存清理。
- 缓存写入和删除各使用一段 Lua，分别保证模板缓存与人群索引操作原子执行；MySQL 与 Redis 之间不引入分布式事务。
- 模板和发券任务分别提供内部 search 接口，直接数据库分页、返回对应表全部字段，默认均按 `id DESC` 排序。

产品 PDF 第 3 页要求历史券按事件时间倒序，第 4 页还要求指定商品全部下架的券进入历史；当前技术文档则统一按有效期结束时间倒序，且下架不改变归属。此次以用户最新确认的上述口径为准，并同步修正文档冲突。

## 二、逐条核对结果

下表“当前事实”记录实施前核对结果；修复后状态见第七节。

| Review 条目 | 当前事实 | 处理结论 |
| --- | --- | --- |
| 1. 模板写操作审计、作废字段归属 | 创建和作废均未调用 `OperationLogWriteService`；`voidTime` 已用于作废接口幂等响应 | 补同事务操作日志；建议保留三个作废字段作为当前状态快照，理由见下文 |
| 2. 范围类型常量写在 Entity | `CouponScopeEntity.REF_TYPE_PRODUCT` 确实直接被组装器和 Repository 使用 | 移到独立业务枚举，Entity 仅保留持久化字段 |
| 3、5. 增加 search 接口 | 两条意见重复；内部只读 Client 目前只有任务列表和详情，任务列表不是表全部字段响应 | 补模板数据库分页 search；按用户追加要求，发券任务同样新增全字段 search |
| 4. name / description 语义 | 模板缓存缺少 description，发券时把 name 写入用户券 display_name | 修正完整展示来源链路，并落实必填、最多 6 字校验 |
| 6. 错误码与中文原因 | 明细响应 failReason 当前返回枚举英文名 | 拆为 failReasonCode 与中文 failReason |
| 7. TTL 时间戳计算 | 当前已使用毫秒时间戳相减，未经过默认时区转换 | 此项已满足；随 Lua 改造使用绝对毫秒过期时间即可 |
| 8. TTL 边界 | 缓存恰好在最后可能的用户券有效期结束时过期；业务判断包含结束时刻 | 增加 24 小时缓存缓冲，券的业务有效期不延长 |
| 9. 魔法值 | 渠道 `<= 5`、默认叠加层级、资方、状态等仍有硬编码 | 检查本分支相对 main 的变更，按业务语义复用或补充枚举/常量 |
| 10. 缓存操作原子性 | 当前创建在同一个 MySQL 事务回调内先保存数据库、再写缓存；模板 SET、人群 SADD、索引过期分多次调用，删除同样分两次 | 保留当前创建顺序与事务边界；写入与索引移除各合并成一段 Lua，分别原子执行，不增加缓存补偿回滚 |
| 11. 历史券范围与排序 | 空 status 会返回仍可用的 UNUSED；排序只看 validEndTime | 限定三类历史券，先按状态分组，再按有效期结束时间倒序，不新增字段 |
| 12. 分布式锁复用 | 到账弹窗自行 SETNX + UUID + 解锁 Lua | 改用已有 RedissonDistributedLocker 低层 API，保留原业务语义 |

## 三、具体改动方案

### 1. 模板操作日志与事务（Review 1、10）

复用现有 `OperationLogWriteService.addLog`，使用 `BizSystem.COUPON`：创建记 `CREATE`，作废记 `CANCEL`，详情中明确动作是模板作废。

- `bizId` 使用模板 ID，`operator` 使用当前传入操作人。
- `operationDetails` 使用结构化 JSON，包含 tenantId、templateId、业务动作、动作毫秒时间，以及必要的创建快照或作废前后状态、作废原因。
- 操作日志与模板、范围数据的数据库变更放在同一个 Spring 事务；日志保存失败应回滚本次数据库变更。
- 作废增加明确事务边界；只有状态真正从正常变为作废时记一次日志。重复作废仍在事务内移除人群索引，返回首次作废时间，不重复记作废日志。
- 作废处理顺序为：行锁读取模板、更新作废状态、写审计、从永久人群 SET 移除模板 ID，最后提交 MySQL。模板规则 JSON 保留；索引移除失败使数据库回滚；基础设施异常记录租户、模板 ID 和堆栈，发送“模板作废失败”告警并转换为 `COUPON_SYSTEM_BUSY`，已有业务异常原样透传。

**作废字段建议：本次保留 `void_time / void_by / void_reason`，同时将完整操作写入日志。** 三个字段描述当前作废结果，日志描述操作经过；尤其 `void_time` 已参与接口幂等响应。全部移除虽然可行，但需要新增日志读取、从日志提取首次作废信息，并让列表查询依赖日志。现有 `OperationLogReadService` 还是空接口。暂时保留能减少本次查询和接口改动；`void_by / void_reason` 从技术上可仅存日志，但没有必要为消除这点冗余增加两套作废结果读取方式。

创建模板采用以下顺序，不新增缓存回滚逻辑：

1. 保存模板、范围和操作日志。
2. 执行写入 Lua，一次写入模板缓存和人群索引。
3. 第 1、2 步均在现有 `TransactionTemplate.executeWithoutResult` 的同一个 MySQL 事务回调内执行。第 2 步成功后结束回调、提交 MySQL 事务，提交成功后才返回创建成功；任一步抛出异常或提交失败都返回创建失败，记录 tenantId、templateId、异常并告警。

当前 `persist` 已按上述事务边界先写数据库、再调用 `templateCache.put`，沿用该实现，只补入同事务审计并改造缓存脚本。同步修正现有“rollback cache”“调用方据此不写 MySQL”“MySQL 回滚时调用 evict”等不符合实际行为的日志和注释，明确 Redis 写入失败时通过异常回滚的是本地 MySQL 事务。

MySQL 事务不能回滚 Redis。若 Lua 已执行成功但数据库提交失败，或 Redis 超时但实际已执行，创建可能保留缓存，作废可能只移除人群索引；按用户要求不增加反向删除、缓存恢复、数据库回滚后的缓存清理、自动重试、补偿任务或分布式事务。两段 Lua 分别保证各自 Redis 操作的原子执行，不代表 MySQL 与 Redis 跨存储原子提交。

### 2. 枚举和常量整理（Review 2、9）

`REF_TYPE_PRODUCT` 表达业务类型，放在 Entity 中没有必要。建议新增 `CouponScopeRefType.PRODUCT`，由组装器和范围查询共用。

这里不能直接套用现有 `CouponScopeType`：模板的 scope_type 表示“指定商品 / 全店”，关系表的 scope_type 表示“商品 / SKU / 类目等引用对象”。两者当前都有数值 1，但语义不同。

本分支相对 main 的 Java 变更按以下顺序检查：

- 渠道：移除 `channel.getCode() <= 5`，由 `CouponIssueChannel` 明确表达普通自动领取渠道；新人渠道仍要求新人身份，灌券渠道只走任务。
- 状态：缓存 READY/VOID、用户券状态、模板状态、任务和明细状态统一引用对应枚举或命名常量；Mapper 条件更新使用枚举码绑定参数，保留原有条件更新与影响行数判断。
- 范围、人群、资方、叠加层级：移除默认值中的裸业务码；优先复用现有枚举，仅为缺失语义增加枚举。特别区分模板 stack_layer 编码与 `DiscountLayer.order` 执行顺序，不能直接互换数值。
- 参数默认值与上限：统一已有分页、限领、缓存缓冲等常量；普通计数、索引、金额精度等数字按其实际含义保留，避免机械增加枚举。
- 检查优惠券接入的价格、购物车、结算和下单代码；不扩展成历史系统的全量重构。

### 3. 内部模板 search（Review 3、5）

建议新增：

`GET /aigc/trade/internal/coupon/v1/templates/search`

Controller 对应路径为 `/internal/coupon/v1/templates/search`，与现有网关前缀习惯一致。

- `CouponV1InternalReadClient` 与 `CouponV1InternalController` 同步增加模板 `search`，方法名与任务 search 明确区分。
- 新增模板查询 DTO、完整字段响应 DTO、读取服务与 Repository 分页方法，沿用现有 `PageResult` 和 MyBatis-Plus `Page` 约定。
- 默认 page=1、pageSize=20，单页最多 100；直接数据库分页和计数，不查 Redis，也不先全表拉到内存再分页。
- 无业务条件时，分页返回当前租户全部模板；tenantId 从现有请求上下文取得，不允许查询条件覆盖租户。closed 不传时不额外隐藏记录，传值时按其值筛选。
- 第一版查询条件覆盖：id/templateId 精确查询、name/description 模糊查询，issueChannel/status/validType/discountType/crowdType/scopeType/stackLayer/stackable/funder/closed 精确查询，appId/createBy/updateBy 精确查询，以及领取、有效期、创建时间范围。
- status 使用表中持久化状态码；与按领取窗口推导出的“未开始/生效中/已过期”展示状态区分，不能在一页结果上再过滤展示状态。
- 默认仅按 `id DESC`，其中 id 为模板表自增主键，不是业务 templateId；固定排序字段，查询参数全部绑定。
- 响应逐一映射模板表所有列，包括 id、tenantId、appId、作废信息、操作人和继承的两个审计时间；不直接暴露 Service Entity。ID 用十进制字符串，金额和日期沿用现有接口字符串约定，业务日期用 `CouponTime` 格式化，审计字符串保持现有处理。
- 非法 ID、状态、日期区间、分页参数返回明确参数错误。对默认列表执行 EXPLAIN，再按实际结果决定是否在建表 SQL 中补充 `(tenant_id, id)` 联合索引。

### 4. 内部发券任务 search（用户追加）

新增：

`GET /aigc/trade/internal/coupon/v1/issue-tasks/search`

Controller 对应路径为 `/internal/coupon/v1/issue-tasks/search`，与模板 search 使用相同协议规则。

- `CouponV1InternalReadClient` 与 `CouponV1InternalController` 同步增加任务 search；新增任务查询 DTO、全字段响应 DTO，以及读取服务和 Repository 分页方法，复用现有任务服务与 Repository，不另建一套查询框架。
- 沿用 `PageResult` 和 MyBatis-Plus `Page`：默认 page=1、pageSize=20，单页最多 100，数据库直接分页并返回 total。无业务条件时返回当前租户全部任务，不默认筛选状态。
- tenantId 从现有请求上下文取得，不能被查询条件覆盖；默认仅按任务表自增主键 `id DESC`，不是 taskId，也不追加 create_time 排序。
- 查询条件按任务表字段提供：id/taskId/templateId 精确查询，name/sourceFile 模糊查询，status/createBy 及 totalCount/successCount/failCount 精确查询，以及 startTime/finishTime/createTime/modifiedTime 范围。所有条件在 SQL 中过滤，参数绑定；ID、状态、计数、日期区间和分页参数做合法性校验。
- status 查询和响应使用任务表持久化状态码 `PENDING=0 / PROCESSING=1 / FINISHED=2 / CANCELLED=3`。与已有任务列表按统计派生的 SUCCESS/PARTIAL_FAILED/FAILED 展示状态区分，不在分页后做展示状态过滤。
- 响应逐一映射 `trade_coupon_issue_task` 全部 15 列：id、taskId、templateId、name、sourceFile、totalCount、successCount、failCount、status、startTime、finishTime、tenantId、createBy、createTime、modifiedTime；不直接暴露 Entity，不查询逐用户任务明细。
- ID 使用十进制字符串；startTime/finishTime 仍按 `Asia/Shanghai` 格式化为现有日期字符串，createTime/modifiedTime 保留 `BaseEntity` 审计字符串处理。空业务时间返回空值。
- 已有 `/issue-tasks` 摘要列表及 `/issue-tasks/{taskId}` 详情继续保持现有协议与排序，新 search 独立提供按表查询的能力；检查静态 `/issue-tasks/search` 路由优先匹配，避免把 search 当作 taskId 解析。
- 对默认查询执行 EXPLAIN，再按实际结果决定是否在建表 SQL 中补充 `(tenant_id, id)` 联合索引。

### 5. C 端展示名和六字校验（Review 4）

- `CouponTemplateCreateRequest.description` 加 `@NotBlank`、`@Size(max = 6)`；注释明确它是 C 端券名称。name 保持运营名称含义。
- `CouponTemplateValidator` 同步校验 description 非空白、长度不超过 6，避免直调 Service 绕过 MVC Bean Validation；超长直接拒绝，不截断。
- `CouponTemplateSnapshot` 增加 description；从创建 Draft 和数据库 Entity 组装快照的两条路径都赋值。
- 发券时改为 `userCoupon.displayName = template.description`，仍使用用户券快照承载展示值。
- 列表、历史、到账弹窗、benefitOptions、购物车、价格、结算及订单优惠明细统一使用该 C 端快照；运营任务中的模板 name 继续使用运营名称。
- C 端现有 `displayName / couponName` 字段名保留，修正其数据来源，不要求客户端更换字段。
- 建表 SQL 中 description 改为 `VARCHAR(6) NOT NULL`；display_name 现有 `VARCHAR(64)` 容量已足够，无须扩列，修正其快照来源注释。
- 模板与人群索引缓存统一升级版本，防止继续读取缺少 description 的旧模板快照；只调整这两类缓存命名空间。

### 6. 发券失败原因（Review 6）

明细响应改为两个字段：

| 字段 | 含义 | 示例 |
| --- | --- | --- |
| failReasonCode | 原有稳定错误码 | NEW_USER_COUPON_EXISTS |
| failReason | 运营可读中文原因 | 已领取过新人优惠券 |

- 在 `IssueFailReason` 补充中文描述与安全查找方法，明细转换时集中映射。
- 成功、待发放时两个字段均为空；未知错误码保留 code，并返回可读的兜底文案，不能导致整个任务详情失败。
- 数据库 fail_reason 继续存错误码；任务 `failedReasonSummary` 的码到数量映射保持稳定。
- 同步更新客户端 DTO、注释、详情测试和接口文档，明确原 failReason 的语义已改变，供运营调用方适配。

### 7. 缓存时间与 Redis Lua（Review 7、8、10）

当前 `expireAt - CouponTime.nowMillis()` 已经是时间戳运算，不受 JVM 默认时区影响。上海时区仍用于接口日期解析、格式化和“领取后 N 个日历天”的计算，这些业务含义不能删掉。

缓存过期时间调整为：

```text
latestBusinessEnd = max(领取结束时间, 最晚可能发出用户券的有效期结束时间)
cacheExpireAt = latestBusinessEnd + 24 小时
```

缓冲使用命名常量，模板创建响应的 cacheExpireTime 返回实际缓存过期时间。领券、算价、锁券仍分别检查原始业务时间，不能因缓存保留而多领、多用一天。

缓存共使用两段 Lua，内联在 `CouponTemplateCache.PUT_SCRIPT / EVICT_SCRIPT` Java 字符串常量中，使用 `DefaultRedisScript`，不再使用独立 Lua 文件或资源加载代码。一次 put 或 evict 只执行对应的一段脚本，各自涉及的模板键与人群索引命令在同一次脚本执行中完成。

Java 校验 snapshot、tenantId、templateId 和写入到期时间，并组装渠道、人群及键名。Lua 不做参数数量、正则、数字范围、键名或 TYPE 防御检查，也不增加 Redis 预查询；缓存键依赖专用命名空间维护。

Redis 写入脚本：

1. 自动领券模板先操作人群索引，执行 SADD；任务灌券模板没有索引键，跳过此分支。
2. 执行 `PERSIST`，确保 all/new 人群 SET 永久保存；已有旧 TTL 的索引在写入时清除过期时间，模板仍使用绝对到期时间。
3. 再写模板 JSON 并设置 `PEXPIREAT(cacheExpireAt)`。
4. 索引与模板操作在同一次脚本执行中完成，中间不插入其他客户端操作。
5. 返回明确结果，由 Java 检查；失败向上抛出异常，创建流程据此回滚当前 MySQL 事务，记录异常并告警，不增加缓存补偿回滚。

Redis 作废脚本仅对 all/new 人群 SET 执行 SREM，不删除模板规则 JSON；渠道 7 没有人群索引时跳过 Redis。重复移除保持幂等，索引类型错误直接暴露。Lua 运行时错误不会自动撤销已执行命令，不宣称脚本失败会回滚 Redis。

实施时核对实际 Redis 部署方式；若使用 Cluster，模板键与对应人群索引键必须共享租户 hash tag，确保多键脚本落在同一 slot。同步验证新版缓存键布局。

### 8. 历史券范围与分组排序（Review 11）

接口行为统一为：

| status 参数 | 返回范围 |
| --- | --- |
| null、空白、ALL | LOCKED、USED、EXPIRED 的并集 |
| LOCKED / USED / EXPIRED | 指定的一类历史券 |
| UNUSED、其他非法值 | 参数错误，说明只支持历史状态 |

保留当前状态推导优先级：先看持久化 LOCKED/USED，只有 UNUSED 且 `now > validEndTime` 才显示 EXPIRED；等于结束时刻不视为过期。锁定后跨过有效期仍显示 LOCKED，取消/超时解锁后，再按原有效期决定是否进入历史。

按用户最新方案实现，复用现有状态和有效期字段：

- 固定分组顺序为 `LOCKED → USED → EXPIRED`，用明确的业务排序定义表达顺序。
- 每组按 `validEndTime DESC, couponId DESC` 排列；空到期时间排组内最后。
- 不新增 lock_time，不改锁券、解锁、核销、返券 SQL，不为排序增加业务时间字段。
- 继续先完成状态筛选与排序，再分页；仅对本页需要跳转的券批量补充范围和商品信息，不增加逐券查询订单的调用。
- `validEndTime` 继续表示用户券有效期结束时间，与组内排序依据一致。
- 商品下架沿用现有提示和跳转处理，不改变状态、排序或分页总数。

### 9. 到账弹窗分布式锁（Review 12）

- 注入 `RedissonDistributedLocker`，移除本类的 UUID、SETNX 和释放锁脚本。
- 使用低层 `tryLock(lockKey, 0, NEW_USER_LOCK_SECONDS, TimeUnit.SECONDS)`，保留立即尝试、当前 5 秒租约、租户加用户维度。
- false 表示竞争失败，本次跳过新人券；连接等基础设施异常与竞争失败分开处理。
- 捕获 InterruptedException 时恢复线程中断标记，停止本次新人发放。
- 成功拿锁后继续在锁内回查全局新人券，保留原幂等业务键及数据库约束。
- finally 调用 `unlockQuietly`；释放失败不能把已经提交的发券结果变成发放失败。
- 不使用会包装业务异常的 Supplier 重载；不引入异步任务或子线程 AuthContext 访问。

## 四、实施顺序

1. 先补充协议和基础定义：description 校验、查询/响应 DTO、缺失枚举、历史状态分组顺序、缓存缓冲和脚本契约。
2. 修改建表 SQL 的 description 约束、Assembler 和发券展示快照，贯通 C 端展示来源。
3. 修复历史券范围和排序、失败原因响应，并实现模板和发券任务 search，均默认 `id DESC`。
4. 完成缓存写入、删除两段 Lua 和绝对过期；保留创建流程在同一 MySQL 事务回调内先写数据库、再写 Redis 的顺序，接入模板操作日志与作废事务。
5. 替换到账弹窗锁，检查分支剩余业务魔法值和优惠券接入点。
6. 更新测试、示例与技术文档，执行下述验收。

DDL 同时维护 `docs/coupon/coupon-tables-ddl.sql` 与当前 MVP 执行脚本 `trade-order-service/src/main/resources/sql/trade_coupon_v1.sql`，避免一份已改、另一份仍保留旧约束和注释。不新增迁移脚本。

## 五、验收计划

| 方向 | 关键验证 |
| --- | --- |
| 展示与校验 | description 为 null、空串、纯空白、7 字时拒绝；1 字、6 字通过；name 与 description 不同时，所有 C 端链路展示后者，运营仍展示前者 |
| 历史券 | null/ALL 排除当前 UNUSED；分别筛选三种状态；按 LOCKED、USED、EXPIRED 分组，组内按到期时间和 couponId 倒序；跨组分页稳定；过期边界、支付核销、取消解锁、退款返券后的归属正确 |
| 模板 search | 真实数据库分页、total、默认仅 id DESC、组合条件、租户隔离、无条件全量范围及全部表字段映射；用创建时间和 ID 顺序不同的数据确认只按 ID 排序；非法参数返回清楚错误 |
| 发券任务 search | 同样验证真实数据库分页、total、仅 id DESC、组合条件、租户隔离、无条件全量范围及全部 15 列映射；区分 id/taskId 与持久化/展示状态；静态 search 路由正常匹配，旧摘要列表和详情协议兼容 |
| 操作审计 | 创建/作废各产生一次准确日志；日志写失败回滚业务数据；重复作废不重复记录；首次作废时间稳定 |
| Redis 脚本 | 验证 Java 参数校验；使用实际 Redis 验证模板与索引一起写入、永久索引、无索引渠道、重复移除、绝对毫秒过期；作废后索引成员消失但模板 JSON 和到期时间保留；索引类型错误直接报错且不改动模板，不增加 TYPE 预检查 |
| 缓存失败与边界 | 同一真实 MySQL 事务内验证数据库写入、审计、Redis 调用、提交的顺序；数据库写入/审计失败时不调用缓存，Redis 写入/索引移除失败时数据库回滚；拆开首次与重复作废的顺序验证；数据库提交失败或 Redis 超时后不反向删除/恢复缓存；业务异常透传，系统异常告警并转换优惠券错误码；缓冲不延长业务有效期，已有时区测试继续通过 |
| 发券原因 | 六种原因码映射中文、未知码兜底、成功/待发为空、汇总仍按码统计 |
| 新人锁 | 竞争失败跳过、中断恢复、异常传播、锁内复查、释放失败不遮蔽成功、并发和重复请求保持幂等 |

执行已有优惠券单测并补充上述必要边界用例，构建 client、service、api 及受影响 job 模块；最后运行目标 Spring 集成测试，模板和发券任务两个新增 search 各覆盖一个真实数据 case，并覆盖已有优惠券、价格、购物车列表、结算、提交链路。真实写入测试只针对既定 dev 环境，使用运行时创建的 ID，不伪造固定模板、任务或用户券 ID。

文档验收同步检查：历史 status 允许值、排序说明、name/description 语义、缓存 TTL、失败原因字段、创建事务顺序及两个 search 协议一致。当前优惠券服务包未发现子线程直接读取 AuthContext；本次保持由入口获取上下文后显式传参的方式。

## 六、核对依据

- Review：[code_review_1.md](/Users/rogerswang/my/work/jjewelry/order-java/docs/coupon/code_review_1.md)
- 历史接口：[UserCouponQueryServiceImpl.java](/Users/rogerswang/my/work/jjewelry/order-java/trade-order-service/src/main/java/cn/mathmagic/jjewelry/business/couponv1/service/query/UserCouponQueryServiceImpl.java:77)
- 锁券 SQL 当前实现：[UserCouponMapper.java](/Users/rogerswang/my/work/jjewelry/order-java/trade-order-service/src/main/java/cn/mathmagic/jjewelry/business/couponv1/repository/mapper/UserCouponMapper.java:17)
- C 端名称来源：[CouponIssueServiceImpl.java](/Users/rogerswang/my/work/jjewelry/order-java/trade-order-service/src/main/java/cn/mathmagic/jjewelry/business/couponv1/service/issue/CouponIssueServiceImpl.java:159)
- 模板缓存及 Lua 入口：[CouponTemplateCache.java](/Users/rogerswang/my/work/jjewelry/order-java/trade-order-service/src/main/java/cn/mathmagic/jjewelry/business/couponv1/cache/CouponTemplateCache.java:53)
- 模板事务与审计：[CouponTemplateServiceImpl.java](/Users/rogerswang/my/work/jjewelry/order-java/trade-order-service/src/main/java/cn/mathmagic/jjewelry/business/couponv1/service/template/CouponTemplateServiceImpl.java:65)
- 技术文档历史定义：[coupon-technical-design-mvp.md](/Users/rogerswang/my/work/jjewelry/order-java/docs/coupon/coupon-technical-design-mvp.md:780)
- 原始产品文档：[造好物优惠券MVP.pdf，第 3、4 页](/Users/rogerswang/Downloads/造好物优惠券MVP.pdf)
- 原始技术文档：[优惠券-技术设计-V3.pdf](/Users/rogerswang/Downloads/优惠券-技术设计-V3.pdf)

## 七、实施与验收结果（2026-10-22）

| 方向 | 已实施与验证 |
| --- | --- |
| 模板创建与作废 | 模板、范围、CREATE 审计在同一真实 MySQL 事务写入后调用缓存，成功提交才返回；作废状态及 CANCEL 审计写入后，在事务内移除人群索引，最后提交。数据库/审计/Redis 故障回滚本地事务，重复作废只记一次审计且保持首次时间；提交故障不反向删除或恢复索引 |
| 模板与任务 search | 两个 Client/Controller/Service/Repository 查询贯通；真实 MySQL 验证分页、total、组合条件、租户隔离、删除标记不过滤、ID 排序与创建时间无关，以及响应字段映射；MockMvc 验证静态任务 search 路由 |
| 展示与参数校验 | description 必填且最多 6 字，MVC 和 Service 双层校验；模板快照、发券 display_name、候选及优惠展示沿用 C 端快照；运营仍使用 name |
| 失败原因 | failReasonCode 保留数据库错误码，failReason 返回中文；六类码和未知码兜底验证通过，成功/待发不带失败原因，汇总仍使用码 |
| 缓存 | put/evict 各为一段 Java 内联 Lua；Java 校验参数，脚本先索引后模板，无 TYPE 预检查；真实 Redis 验证模板/索引写入、灌券无索引、永久索引、绝对毫秒到期、作废保留模板 JSON、索引类型错误不改模板和缓存缓冲不延长领取有效期；模板/人群索引共享租户 hash tag |
| 历史券 | ALL/null 排除当前 UNUSED，三类状态分组后按到期时间、券 ID 倒序再分页；空时间最后，跨组分页及非法状态验证通过；不增加 lock_time |
| 新人锁及常量 | 改用 RedissonDistributedLocker，竞争失败、中断恢复、基础设施异常、锁内复查和释放异常边界测试通过；范围、渠道、模板/任务/用户券状态、资方及层级引用枚举或常量 |
| 文档与 DDL | 技术文档同步两个 search 的参数和完整响应、名称来源、历史规则、TTL、事务顺序及失败原因；两份建表 DDL 同步 description 约束与 `(tenant_id,id)` 索引，没有新增迁移脚本 |

首次验收命令与记录（使用本地 Azul JDK 8，先设置 JAVA_HOME/PATH；本轮结果见第八节）：

```bash
mvn -q -pl trade-order-api -am '-Dtest=CouponReviewIntegrationTest,CouponV1InternalControllerTest,CouponV1ControllerTest' -Dsurefire.failIfNoSpecifiedTests=false test
mvn -q -pl trade-order-api,trade-order-job -am '-Dtest=cn.mathmagic.jjewelry.business.couponv1.*Test,cn.mathmagic.jjewelry.business.trade.support.TradeCouponSupportTest' -Dsurefire.failIfNoSpecifiedTests=false package
git diff --check
```

- API 测试共 24 个：7 个 MySQL/Redis 验收、10 个内部接口用例、7 个 C 端和价格/购物车/checkout/submit 用例，0 失败、0 错误。
- 优惠券服务包及交易优惠券相关测试共 60 个，0 失败、0 错误；client、service、api、job 及其依赖模块打包通过。
- dev 集成测试仅替换优惠券告警发送器，避免测试异常触发外部通知；其余验收依赖使用真实 dev MySQL/Redis。事务故障测试在真实数据库写入后注入故障，Redis 脚本单独使用真实 Redis 执行。
- 数据库查询夹具随测试事务回滚，专用事务验收数据及 Redis 唯一租户键在 finally 清理；已有接口冒烟测试会按约定写入 dev 业务数据。
- EXPLAIN 确认现有 dev 表默认 search 存在 `Using filesort`，因此两份建表 SQL 新增 `(tenant_id,id)` 联合索引。本次没有修改 dev 表结构，新索引效果待按新版 DDL 建表后验收。
- 不引入缓存反向补偿；Lua 成功但 MySQL 提交失败等跨存储边界仍按已确认方案保留。服务包未新增 AuthContext 读取或异步线程。

## 八、Lua 简化与作废事务调整验收（2026-10-22）

- `CouponTemplateCache` 内联两段 `DefaultRedisScript`，移除独立脚本文件和资源加载；Java 校验参数、组装键并计算绝对到期毫秒数，Lua 不再调用 TIME，先索引后模板，all/new 索引通过 PERSIST 永久保存。
- 保留作废事务内更新状态、记录审计、移除人群索引、最后提交的顺序；系统异常记录日志并发送“模板作废失败”告警，返回 `COUPON_SYSTEM_BUSY`，业务异常原样透传。
- 首次和重复作废的顺序分别验证，避免后一次调用掩盖前一次的顺序错误；覆盖索引移除失败回滚、提交失败不恢复索引、首次作废时间和单次审计。

Lua 内联与作废事务调整阶段执行命令（Azul JDK 8，以下通过记录早于 Java TTL 传参调整）：

```bash
mvn -q -pl trade-order-service -am -Dtest=CouponTemplateServiceTest,CouponTemplateCacheTest -Dsurefire.failIfNoSpecifiedTests=false test
mvn -q -pl trade-order-api -am '-Dtest=cn.mathmagic.jjewelry.business.couponv1.*Test,cn.mathmagic.jjewelry.business.trade.support.TradeCouponSupportTest' -Dsurefire.failIfNoSpecifiedTests=false package
mvn -q -pl trade-order-api -am -Dtest=CouponReviewIntegrationTest -Dsurefire.failIfNoSpecifiedTests=false test
git diff --check
```

- 定向单测 10 个通过；包含这些用例的优惠券及交易优惠券回归共 66 个，0 失败、0 错误、0 跳过；client/service/api 打包通过。
- dev Spring 集成验收 8 个通过，0 失败、0 错误、0 跳过。沙箱最初阻止 dev 网络访问，获准后在沙箱外重跑通过；未重新执行已有 17 个会写入业务数据的接口冒烟用例。
- 使用真实 Redis 验证毫秒绝对到期、永久索引、无索引渠道、重复移除及索引类型异常不改模板；使用真实 MySQL 验证作废索引移除失败时状态和审计回滚。告警发送器替换为 mock，夹具在事务回滚或 finally 中清理。
- 未新增线程或子线程 AuthContext 读取。索引移除成功但 MySQL 提交失败时，仍接受索引成员缺失，不增加恢复或补偿。

Java 绝对到期参数调整后再次验收（Azul JDK 8）：

```bash
mvn -q -pl trade-order-api -am '-Dtest=CouponTemplateCacheTest,CouponReviewIntegrationTest#luaWritesAndEvictsAtomicallyAndKeepsAudiencePermanent+audienceTypeErrorsDoNotWriteOrDeleteTemplate' -Dsurefire.failIfNoSpecifiedTests=false test
git diff --check
```

- Java 计算绝对到期毫秒数并通过 ARGV 传入；生产 Lua 不再调用 TIME，写入时 PERSIST 人群索引，模板仍使用 PEXPIREAT。
- `CouponTemplateCacheTest` 的 2 个参数校验单测通过；新增重复写入保持索引永久、作废保留模板规则 JSON 的真实 Redis 断言。
- 选中的 2 个集成测试重试后仍因 dev Nacos `Client not connected,current status:STARTING` 导致 Spring 上下文启动失败，尚未执行 Redis 断言。本次调整后的真实 Redis 验证未完成，不能沿用前一阶段的通过结果。
- `git diff --check` 通过。

本次实现后的完整 `CouponReviewIntegrationTest` 重跑同样被 dev Nacos 网络连接阻塞（`172.31.16.122:9848` 返回 `Operation not permitted`），8 个 Spring 用例均在加载 ApplicationContext 阶段失败，未产生本次验收数据；服务定向单测和 API 打包不受影响。
