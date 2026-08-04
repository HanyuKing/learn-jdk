# order-java 盲盒玩法商品扩展技术设计

## 1. 文档目的

本文档基于现有项目 `/Users/rogerswang/my/work/jjewelry/order-java` 的非 `ikj` 商品体系，设计盲盒玩法的商品域扩展方案。

本阶段只展开商品侧设计，目标是解决：

1. 现有 `productdomain` 商品模型如何兼容盲盒商品。
2. 普通商品、盲盒售卖商品、盒内结果商品之间如何建模。
3. 商品详情、SKU、库存、搜索、审核、发布链路如何最小改造。
4. 如何为后续订单、抽盒、履约扩展留下清晰接口。

明确边界：

- 不使用 `business/ikj` 包内已有抽奖、奖池、订单、支付实现。
- 不复用 `IkjProduct`、`IkjAward`、`IkjAwardPool`、`IkjOrder` 等模型。
- 不修改现有普通商品语义。
- 本文档只设计商品扩展，不实现抽盒算法、支付、订单和履约。

---

## 2. 现有项目商品模型梳理

### 2.1 现有核心模块

非 `ikj` 商品主链路主要分布在以下包：

```text
trade-order-api
 └─ business/controller/product
    ├─ ProductController
    ├─ inner/ProductInnerController
    ├─ inner/SkuInnerController
    └─ dto/ProductUpdateRequest

trade-order-service
 └─ business/productdomain
    ├─ entity/Product
    ├─ entity/ProductExt
    ├─ entity/Sku
    ├─ entity/SkuAttribute
    ├─ service/aggregation/PublishProductProxy
    ├─ service/read/ProductReadService
    ├─ service/write/ProductWriteService
    ├─ service/TradeSkuService
    └─ repository/*

trade-order-service
 └─ business/stock
    ├─ entity/Stock
    ├─ entity/StockLog
    ├─ service/StockReadService
    └─ service/StockWriteService

trade-order-service
 └─ business/product/es
    ├─ entity/ProductIndex
    └─ service/ProductIndexService
```

### 2.2 现有主表

当前商品主表模型：

```text
trade_pd_product
 ├─ product_id
 ├─ spu_id
 ├─ category_id
 ├─ product_type
 ├─ product_name
 ├─ description
 ├─ price
 ├─ product_images
 ├─ audit_status
 ├─ freight_setting
 ├─ shipping_mode
 ├─ delivery_time_type
 ├─ business_mode
 ├─ create_method
 ├─ status
 ├─ discount_channel
 ├─ channel
 ├─ freight_template_id
 ├─ custom_type
 └─ buy_limit

trade_pd_product_ext
 ├─ product_id
 ├─ detail
 ├─ extra
 ├─ main_image_info
 ├─ publish_time
 ├─ last_publish_time
 ├─ is_featured
 └─ featured_sort_order

trade_pd_sku
 ├─ sku_id
 ├─ product_id
 ├─ sku_code
 ├─ price
 ├─ want_price
 ├─ stock
 ├─ img_url
 ├─ erp_status
 └─ closed

trade_pd_sku_attribute
 ├─ sku_id
 ├─ attribute_id
 ├─ attribute_name
 └─ attribute_value

trade_pd_stock
 ├─ product_id
 ├─ sku_id
 ├─ channel
 ├─ sku_name
 ├─ sq
 ├─ lq
 └─ oq
```

### 2.3 现有枚举语义

`TradeProductTypeEnum` 当前定义：

```text
DEFAULT = 0
WANT    = 1 众筹商品
TEMPLATE= 2 模板商品
SHOP    = 3 商城商品
```

这个字段已经表达“商品来源/业务类型”，不是售卖玩法类型。

因此不建议这样扩展：

```text
TradeProductTypeEnum.BLIND_BOX = 4
```

原因：

1. `productType` 当前被 WANT/TEMPLATE/SHOP、创建流程、搜索、列表、活动、创作者逻辑广泛使用。
2. 盲盒可以是商城商品，也可以是模板商品的售卖玩法，和现有 `productType` 不是同一个维度。
3. 直接加 `BLIND_BOX` 会让现有 `TradeProductTypeEnum.isProductWant`、商品 ID 前缀、创建校验、列表过滤语义混乱。

推荐拆分：

```text
product_type: 继续表示商品来源/业务类型
sale_mode: 新增，表示售卖玩法
fulfill_mode: 新增，表示履约商品来源
```

---

## 3. 盲盒商品在现有模型中的映射

### 3.1 三种商品身份

盲盒链路必须显式区分三种身份。

#### 3.1.1 盲盒售卖商品

用户看到并购买的商品，使用现有 `trade_pd_product` 和 `trade_pd_sku` 承载。

```text
product_id = 盲盒商品ID
sku_id     = 盲盒售卖SKU，例如单抽、三抽、十抽
price      = 用户支付价格
```

用途：

- 商品详情。
- 搜索列表。
- 预下单。
- 价格计算。
- 促销。
- 支付。
- 用户订单展示。

#### 3.1.2 盒内结果商品

开盒后实际得到的商品，复用现有普通商品 SKU。

```text
target_product_id = 普通商品ID
target_sku_id     = 普通SKU ID
```

用途：

- 奖池配置。
- 中奖结果展示。
- 后续履约。
- 售后。
- 成本核算。

#### 3.1.3 奖池关系

奖池关系不是普通 SKU 属性，也不是新商品。它是盲盒 SKU 和盒内 SKU 的可抽关系。

```text
box_product_id + box_sku_id + pool_version
        |
        +-- target_product_id + target_sku_id + probability + stock
```

---

## 4. 设计原则

### 4.1 不污染 productType

`trade_pd_product.product_type` 保持现有含义。

盲盒商品可以是：

```text
product_type = SHOP
sale_mode = BLIND_BOX
fulfill_mode = DRAW_RESULT
```

普通商品是：

```text
product_type = SHOP / TEMPLATE / WANT
sale_mode = NORMAL
fulfill_mode = DIRECT
```

### 4.2 盲盒本身仍然是标准商品

盲盒售卖商品必须复用现有商品能力：

- `ProductController#getProductDetail`
- `ProductReadService#getProduct`
- `ProductWriteService#save/update/upOrDown`
- `TradeSkuService#createOrUpdate`
- `trade_pd_stock`
- `ProductIndex`

这样可以最小化购物车、预下单、价格、搜索、上下架链路改造。

### 4.3 盒内商品必须是可履约 SKU

盒内结果商品必须来自现有 `trade_pd_product + trade_pd_sku`。

配置奖池时要校验：

- `target_product_id` 存在且未删除。
- `target_sku_id` 存在且未删除。
- `target_sku_id` 属于 `target_product_id`。
- 商品状态允许履约。
- SKU 有价格、图片、SKU 编码、库存或可售库存来源。
- 不允许把盲盒商品作为盒内商品，第一阶段禁止套娃盲盒。

### 4.4 商品中心只定义奖池，不执行抽取

商品域负责：

- 盲盒商品识别。
- 开盒模式配置。
- 奖池版本。
- 盒内商品列表。
- 概率和展示文案。
- 商品详情返回。
- 商品可售性校验。
- 商品快照。

商品域不负责：

- 随机数。
- 保底。
- 扣奖池库存。
- 生成抽盒记录。
- 创建订单。
- 创建履约单。

后续应由独立的 `blindbox` 抽盒服务承接。

---

## 5. 推荐总体方案

采用：

```text
现有 Product/Sku 主模型复用
    +
新增 sale_mode / fulfill_mode 轻量识别字段
    +
新增盲盒商品扩展表
    +
新增盲盒 SKU 扩展表
    +
新增奖池版本表
    +
新增奖池明细表
    +
新增商品快照表
```

总体关系：

```text
trade_pd_product
  product_id = P_BOX_001
  product_type = SHOP
  sale_mode = BLIND_BOX
  fulfill_mode = DRAW_RESULT

trade_pd_sku
  sku_id = S_BOX_001_SINGLE
  product_id = P_BOX_001
  price = 69.00

trade_pd_blind_box_product_ext
  product_id = P_BOX_001
  open_mode = MANUAL_OPEN
  probability_text = ...

trade_pd_blind_box_sku_ext
  product_id = P_BOX_001
  box_sku_id = S_BOX_001_SINGLE
  draw_count = 1
  current_pool_id = POOL_001
  current_pool_version = 3

trade_pd_blind_box_pool
  pool_id = POOL_001
  pool_version = 3

trade_pd_blind_box_pool_item
  target_product_id = P_NORMAL_001
  target_sku_id = S_NORMAL_001
  probability = 0.10000000
```

---

## 6. 主表扩展设计

### 6.1 trade_pd_product 增加售卖模式

建议增加两个字段：

```sql
ALTER TABLE trade_pd_product
    ADD COLUMN sale_mode TINYINT NOT NULL DEFAULT 1 COMMENT '售卖模式:1=普通售卖,2=盲盒售卖',
    ADD COLUMN fulfill_mode TINYINT NOT NULL DEFAULT 1 COMMENT '履约模式:1=订单SKU直接履约,2=开盒结果履约,3=虚拟发放,4=无需履约';
```

枚举建议：

```java
public enum ProductSaleModeEnum {
    NORMAL(1, "普通售卖"),
    BLIND_BOX(2, "盲盒售卖");
}

public enum ProductFulfillModeEnum {
    DIRECT(1, "订单SKU直接履约"),
    DRAW_RESULT(2, "开盒结果履约"),
    VIRTUAL_GRANT(3, "虚拟权益发放"),
    NO_SHIP(4, "无需履约");
}
```

字段说明：

| 字段 | 普通商品 | 盲盒商品 |
| --- | --- | --- |
| product_type | WANT/TEMPLATE/SHOP | WANT/TEMPLATE/SHOP |
| sale_mode | NORMAL | BLIND_BOX |
| fulfill_mode | DIRECT | DRAW_RESULT |

兼容性：

- 历史数据默认 `NORMAL + DIRECT`。
- 现有查询不带该字段也不影响旧逻辑。
- 订单、预下单、履约后续可以通过该字段做分流。
- 搜索索引可以直接过滤盲盒商品。

### 6.2 Product 实体和 DTO 扩展

需要扩展：

```text
Product
ProductDto
ProductBasicDto
ProductDetailResp
ProductIndex
ProductConverter
```

新增字段：

```text
saleMode
fulfillMode
```

兼容策略：

- Converter 中如果 DB 为空，默认填充 `NORMAL + DIRECT`。
- API 出参对老端不产生破坏。
- C 端详情页根据 `saleMode` 判断是否渲染盲盒信息。

---

## 7. 盲盒商品扩展表

### 7.1 trade_pd_blind_box_product_ext

该表承载商品级盲盒配置。

```sql
CREATE TABLE trade_pd_blind_box_product_ext (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id VARCHAR(64) NOT NULL COMMENT '盲盒商品ID，对应trade_pd_product.product_id',
    open_mode TINYINT NOT NULL COMMENT '开盒模式:1=支付后自动开盒,2=用户手动开盒,3=未拆盒发货',
    result_display_mode TINYINT NOT NULL DEFAULT 1 COMMENT '结果展示模式:1=立即展示,2=部分展示,3=发货后展示',
    support_unopened_ship TINYINT NOT NULL DEFAULT 0 COMMENT '是否支持未拆盒发货',
    support_auto_open TINYINT NOT NULL DEFAULT 0 COMMENT '是否支持自动开盒',
    support_manual_open TINYINT NOT NULL DEFAULT 1 COMMENT '是否支持手动开盒',
    support_batch_open TINYINT NOT NULL DEFAULT 0 COMMENT '是否支持批量开盒',
    probability_text TEXT COMMENT '前台概率说明',
    rule_text TEXT COMMENT '玩法规则说明',
    after_sale_text TEXT COMMENT '售后规则说明',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '配置状态:0=草稿,1=审核中,2=已通过,3=已驳回,4=已停用',
    closed TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除:0=正常,1=删除',
    create_by VARCHAR(64) DEFAULT NULL,
    update_by VARCHAR(64) DEFAULT NULL,
    create_time VARCHAR(32) DEFAULT NULL,
    modified_time VARCHAR(32) DEFAULT NULL,
    UNIQUE KEY uk_product_id (product_id),
    KEY idx_status (status)
) COMMENT='盲盒商品扩展表';
```

字段解释：

| 字段 | 说明 |
| --- | --- |
| product_id | 盲盒售卖商品 ID |
| open_mode | 支付后自动开盒、手动开盒、未拆盒发货 |
| result_display_mode | 结果立即展示或延迟展示 |
| probability_text | 面向用户的概率说明，不能只依赖后台概率 |
| rule_text | 玩法规则 |
| after_sale_text | 盲盒售后说明 |
| status | 盲盒配置审核状态 |

### 7.2 为什么不直接放到 product_ext.extra

`trade_pd_product_ext.extra` 当前已经承载模板、3D、精选、新人商品等多类弱结构扩展。盲盒配置不建议继续塞入 `extra`，原因：

1. 奖池、概率、版本、审核是强约束数据。
2. 需要按 `product_id`、`pool_id`、`target_sku_id` 查询。
3. 需要唯一索引和状态约束。
4. 需要给订单和抽盒服务稳定读取。
5. 运营误改 JSON 风险高，审计困难。

`extra` 可以保留展示类弱字段，但盲盒核心配置必须结构化。

---

## 8. 盲盒 SKU 扩展表

### 8.1 背景

现有 `trade_pd_sku` 表承载商品规格：

```text
sku_id
product_id
price
stock
img_url
sku_attribute
```

盲盒商品下的 SKU 可能代表不同购买单位：

```text
单抽 SKU: draw_count = 1
三连抽 SKU: draw_count = 3
十连抽 SKU: draw_count = 10
整盒 SKU: draw_count = 12
```

这些不是普通销售属性能完整表达的业务含义，因此新增盲盒 SKU 扩展表。

### 8.2 trade_pd_blind_box_sku_ext

```sql
CREATE TABLE trade_pd_blind_box_sku_ext (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id VARCHAR(64) NOT NULL COMMENT '盲盒商品ID',
    box_sku_id VARCHAR(64) NOT NULL COMMENT '盲盒售卖SKU ID',
    draw_count INT NOT NULL DEFAULT 1 COMMENT '购买该SKU对应的开盒次数',
    pool_bind_mode TINYINT NOT NULL DEFAULT 1 COMMENT '奖池绑定模式:1=商品级共用,2=SKU级独立',
    current_pool_id VARCHAR(64) DEFAULT NULL COMMENT '当前生效奖池ID',
    current_pool_version INT DEFAULT NULL COMMENT '当前生效奖池版本',
    allow_duplicate_result TINYINT NOT NULL DEFAULT 1 COMMENT '一次购买多抽是否允许重复款',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:0=停用,1=启用',
    closed TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除:0=正常,1=删除',
    create_by VARCHAR(64) DEFAULT NULL,
    update_by VARCHAR(64) DEFAULT NULL,
    create_time VARCHAR(32) DEFAULT NULL,
    modified_time VARCHAR(32) DEFAULT NULL,
    UNIQUE KEY uk_box_sku (box_sku_id),
    KEY idx_product_id (product_id),
    KEY idx_pool (current_pool_id, current_pool_version)
) COMMENT='盲盒SKU扩展表';
```

字段解释：

| 字段 | 说明 |
| --- | --- |
| box_sku_id | 用户购买的盲盒 SKU |
| draw_count | 该 SKU 对应几次开盒 |
| pool_bind_mode | 商品级奖池或 SKU 级奖池 |
| current_pool_id/current_pool_version | 当前在线奖池版本 |
| allow_duplicate_result | 多抽是否允许重复结果 |

兼容性：

- 普通 SKU 没有该表记录。
- 现有 `TradeSkuService` 的价格、SKU 编码、库存、属性校验不需要改。
- 盲盒详情和预下单只在 `sale_mode=BLIND_BOX` 时额外查询该表。

---

## 9. 奖池版本表设计

### 9.1 trade_pd_blind_box_pool

```sql
CREATE TABLE trade_pd_blind_box_pool (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pool_id VARCHAR(64) NOT NULL COMMENT '奖池业务ID',
    pool_version INT NOT NULL COMMENT '奖池版本',
    product_id VARCHAR(64) NOT NULL COMMENT '盲盒商品ID',
    box_sku_id VARCHAR(64) DEFAULT NULL COMMENT '绑定SKU；为空表示商品级奖池',
    pool_name VARCHAR(128) NOT NULL COMMENT '奖池名称',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态:0=草稿,1=审核中,2=审核通过,3=在线,4=下线,5=归档',
    effective_time VARCHAR(32) DEFAULT NULL COMMENT '生效时间',
    expire_time VARCHAR(32) DEFAULT NULL COMMENT '失效时间',
    total_pool_stock INT DEFAULT NULL COMMENT '奖池总库存配置',
    remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
    closed TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除:0=正常,1=删除',
    create_by VARCHAR(64) DEFAULT NULL,
    update_by VARCHAR(64) DEFAULT NULL,
    create_time VARCHAR(32) DEFAULT NULL,
    modified_time VARCHAR(32) DEFAULT NULL,
    UNIQUE KEY uk_pool_version (pool_id, pool_version),
    KEY idx_product_id (product_id),
    KEY idx_box_sku_id (box_sku_id),
    KEY idx_status (status)
) COMMENT='盲盒奖池版本表';
```

版本规则：

1. `pool_id` 表示同一个逻辑奖池。
2. `pool_version` 表示具体配置版本。
3. 在线版本不可原地修改。
4. 修改概率、盒内商品、库存、展示状态时必须创建新版本。
5. 订单后续只绑定具体 `pool_id + pool_version`。

### 9.2 奖池状态流转

```text
DRAFT
  -> REVIEWING
  -> APPROVED
  -> ONLINE
  -> OFFLINE
  -> ARCHIVED
```

状态说明：

| 状态 | 说明 |
| --- | --- |
| DRAFT | 运营编辑中 |
| REVIEWING | 概率和规则审核中 |
| APPROVED | 审核通过但未生效 |
| ONLINE | 新订单可使用 |
| OFFLINE | 不再给新订单使用 |
| ARCHIVED | 只用于历史订单追溯 |

---

## 10. 奖池明细表设计

### 10.1 trade_pd_blind_box_pool_item

```sql
CREATE TABLE trade_pd_blind_box_pool_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pool_id VARCHAR(64) NOT NULL COMMENT '奖池业务ID',
    pool_version INT NOT NULL COMMENT '奖池版本',
    target_product_id VARCHAR(64) NOT NULL COMMENT '盒内商品ID',
    target_sku_id VARCHAR(64) NOT NULL COMMENT '盒内SKU ID',
    item_level TINYINT NOT NULL DEFAULT 1 COMMENT '款式等级:1=普通,2=稀有,3=隐藏,4=保底',
    probability DECIMAL(12, 8) NOT NULL COMMENT '展示和审计概率',
    weight_value INT NOT NULL COMMENT '抽取权重，抽取服务使用整数权重',
    planned_stock INT NOT NULL DEFAULT 0 COMMENT '该版本计划可抽数量',
    display_stock INT DEFAULT NULL COMMENT '前台展示库存',
    max_draw_quantity INT DEFAULT NULL COMMENT '该款最大可抽数量',
    is_hidden TINYINT NOT NULL DEFAULT 0 COMMENT '是否隐藏款',
    is_displayed TINYINT NOT NULL DEFAULT 1 COMMENT '是否在详情页展示',
    is_guarantee_item TINYINT NOT NULL DEFAULT 0 COMMENT '是否可作为保底结果',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '排序',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态:0=停用,1=启用',
    closed TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除:0=正常,1=删除',
    create_by VARCHAR(64) DEFAULT NULL,
    update_by VARCHAR(64) DEFAULT NULL,
    create_time VARCHAR(32) DEFAULT NULL,
    modified_time VARCHAR(32) DEFAULT NULL,
    UNIQUE KEY uk_pool_target (pool_id, pool_version, target_sku_id),
    KEY idx_target_sku_id (target_sku_id),
    KEY idx_target_product_id (target_product_id)
) COMMENT='盲盒奖池明细表';
```

字段说明：

| 字段 | 说明 |
| --- | --- |
| target_product_id | 盒内商品 ID |
| target_sku_id | 盒内 SKU ID |
| probability | 概率展示、审计、售后举证使用 |
| weight_value | 抽盒服务计算使用 |
| planned_stock | 配置计划库存，不直接作为高并发扣减字段 |
| item_level | 普通、稀有、隐藏、保底 |
| is_displayed | 是否在商详展示 |

### 10.2 概率和权重

配置上允许运营维护概率，发布时系统计算权重。

示例：

```text
普通款 A: probability = 0.10000000, weight_value = 10000000
普通款 B: probability = 0.10000000, weight_value = 10000000
隐藏款 H: probability = 0.01000000, weight_value = 1000000
```

校验规则：

```text
sum(probability) = 1.00000000
sum(weight_value) > 0
probability > 0
weight_value > 0
planned_stock >= 0
```

第一阶段建议固定概率，不做“库存动态调概率”。如果奖池某款库存耗尽，抽盒服务需要从可抽集合里排除该款，并记录概率调整依据。这个属于抽盒服务设计，商品域只提供配置。

---

## 11. 奖池运行库存建议

虽然本阶段是商品扩展，但奖池配置里包含库存含义，必须提前定义边界。

不建议直接在 `trade_pd_blind_box_pool_item.planned_stock` 上做高频扣减。推荐后续库存域新增运行库存表：

```sql
CREATE TABLE trade_pd_blind_box_pool_stock (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pool_id VARCHAR(64) NOT NULL,
    pool_version INT NOT NULL,
    target_sku_id VARCHAR(64) NOT NULL,
    total_qty INT NOT NULL DEFAULT 0,
    available_qty INT NOT NULL DEFAULT 0,
    locked_qty INT NOT NULL DEFAULT 0,
    drawn_qty INT NOT NULL DEFAULT 0,
    shipped_qty INT NOT NULL DEFAULT 0,
    closed TINYINT NOT NULL DEFAULT 0,
    create_time VARCHAR(32) DEFAULT NULL,
    modified_time VARCHAR(32) DEFAULT NULL,
    UNIQUE KEY uk_pool_stock (pool_id, pool_version, target_sku_id)
) COMMENT='盲盒奖池运行库存表';
```

边界：

- 商品域维护 `planned_stock`。
- 奖池发布时初始化 `trade_pd_blind_box_pool_stock`。
- 抽盒服务扣减 `pool_stock`。
- 实物发货仍然基于 `trade_pd_stock` 或后续履约库存。

---

## 12. 商品快照表设计

### 12.1 为什么需要快照

盲盒商品存在概率、奖池、规则文案变更。订单创建和开盒必须能回溯用户购买时看到的配置。

需要快照：

- 订单展示。
- 售后争议。
- 概率审计。
- 活动复盘。
- 后续抽盒服务按订单绑定版本执行。

### 12.2 trade_pd_blind_box_product_snapshot

```sql
CREATE TABLE trade_pd_blind_box_product_snapshot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    snapshot_id VARCHAR(64) NOT NULL COMMENT '快照业务ID',
    product_id VARCHAR(64) NOT NULL COMMENT '盲盒商品ID',
    box_sku_id VARCHAR(64) NOT NULL COMMENT '盲盒售卖SKU',
    pool_id VARCHAR(64) NOT NULL COMMENT '奖池ID',
    pool_version INT NOT NULL COMMENT '奖池版本',
    product_snapshot MEDIUMTEXT NOT NULL COMMENT '盲盒商品快照JSON',
    sku_snapshot MEDIUMTEXT NOT NULL COMMENT '盲盒SKU快照JSON',
    blind_box_snapshot MEDIUMTEXT NOT NULL COMMENT '盲盒扩展快照JSON',
    pool_snapshot MEDIUMTEXT NOT NULL COMMENT '奖池快照JSON',
    pool_item_snapshot MEDIUMTEXT NOT NULL COMMENT '奖池明细快照JSON',
    closed TINYINT NOT NULL DEFAULT 0,
    create_time VARCHAR(32) DEFAULT NULL,
    UNIQUE KEY uk_snapshot_id (snapshot_id),
    KEY idx_product_sku_pool (product_id, box_sku_id, pool_id, pool_version)
) COMMENT='盲盒商品快照表';
```

生成时机：

```text
预下单/下单前:
  根据 product_id + box_sku_id 查询当前在线奖池
  生成商品快照
  返回 snapshot_id 给订单
```

也可以先不落独立快照表，由订单保存完整商品快照。但为了商品域可独立追溯，建议商品中心提供快照生成能力。

---

## 13. DTO 设计

### 13.1 ProductDto 扩展

```java
public class ProductDto {
    private Integer saleMode;
    private Integer fulfillMode;
    private BlindBoxProductDto blindBoxProductDto;
}
```

说明：

- 普通商品 `blindBoxProductDto = null`。
- 盲盒商品详情、后台编辑、内部查询可按需填充。

### 13.2 ProductDetailResp 扩展

```java
public class ProductDetailResp {
    private Integer saleMode;
    private Integer fulfillMode;
    private BlindBoxDetailResp blindBoxDetail;
}
```

`BlindBoxDetailResp`：

```java
public class BlindBoxDetailResp {
    private Integer openMode;
    private Integer resultDisplayMode;
    private Boolean supportUnopenedShip;
    private String probabilityText;
    private String ruleText;
    private String afterSaleText;
    private List<BlindBoxSkuResp> boxSkus;
    private List<BlindBoxPoolItemResp> displayedPoolItems;
}
```

`BlindBoxSkuResp`：

```java
public class BlindBoxSkuResp {
    private String boxSkuId;
    private Integer drawCount;
    private Boolean allowDuplicateResult;
    private String currentPoolId;
    private Integer currentPoolVersion;
}
```

`BlindBoxPoolItemResp`：

```java
public class BlindBoxPoolItemResp {
    private String targetProductId;
    private String targetSkuId;
    private String productName;
    private String skuName;
    private String imageUrl;
    private Integer itemLevel;
    private BigDecimal probability;
    private Boolean hidden;
    private Boolean displayed;
    private Integer sortNo;
}
```

### 13.3 管理端保存请求

新增后台保存请求，不走现有 C 端 `/product/update`：

```java
public class BlindBoxProductSaveCmd {
    private ProductCreateCmd productCreateCmd;
    private BlindBoxProductConfigCmd blindBoxProductConfig;
    private List<BlindBoxSkuConfigCmd> skuConfigs;
    private BlindBoxPoolSaveCmd poolSaveCmd;
}
```

说明：

- `ProductCreateCmd` 继续复用现有商品创建能力。
- 盲盒扩展和奖池配置独立保存。
- 用户侧商品编辑接口只允许编辑标题、描述、主图、详情，不允许编辑奖池概率。

---

## 14. Service 设计

### 14.1 新增包结构

建议新增非 `ikj` 包：

```text
cn.mathmagic.jjewelry.business.productdomain.blindbox
 ├─ entity
 │  ├─ BlindBoxProductExt
 │  ├─ BlindBoxSkuExt
 │  ├─ BlindBoxPool
 │  ├─ BlindBoxPoolItem
 │  └─ BlindBoxProductSnapshot
 ├─ repository
 │  ├─ BlindBoxProductExtRepository
 │  ├─ BlindBoxSkuExtRepository
 │  ├─ BlindBoxPoolRepository
 │  ├─ BlindBoxPoolItemRepository
 │  └─ BlindBoxProductSnapshotRepository
 ├─ service
 │  ├─ BlindBoxProductReadService
 │  ├─ BlindBoxProductWriteService
 │  ├─ BlindBoxPoolReadService
 │  ├─ BlindBoxPoolWriteService
 │  ├─ BlindBoxProductSnapshotService
 │  └─ BlindBoxProductValidateService
 ├─ dto
 └─ enums
```

不建议放在：

```text
cn.mathmagic.jjewelry.business.ikj
```

### 14.2 BlindBoxProductReadService

职责：

```text
getByProductId(productId)
batchGetByProductIdList(productIdList)
getDetail(productId, boxSkuId)
getSkuExt(boxSkuId)
getOnlinePool(productId, boxSkuId)
getDisplayedPoolItems(poolId, poolVersion)
```

典型查询：

```text
ProductReadService#getProductDetailResp
  -> 查询 Product / ProductExt / Sku
  -> saleMode != BLIND_BOX: 返回普通详情
  -> saleMode == BLIND_BOX:
       BlindBoxProductReadService#getDetail
       patch blindBoxDetail
```

### 14.3 BlindBoxProductWriteService

职责：

```text
saveBlindBoxConfig(productId, config)
updateBlindBoxConfig(productId, config)
saveSkuConfig(productId, boxSkuId, config)
updateSkuConfig(productId, boxSkuId, config)
enable/disable
```

注意：

- 不能在 C 端用户编辑接口里允许改概率。
- 盲盒扩展配置变更需要审核或至少操作日志。
- `product_id` 必须已存在，且 `sale_mode=BLIND_BOX`。

### 14.4 BlindBoxPoolWriteService

职责：

```text
createPoolDraft(productId, boxSkuId)
savePoolItems(poolId, poolVersion, itemList)
submitReview(poolId, poolVersion)
approve(poolId, poolVersion)
publish(poolId, poolVersion)
offline(poolId, poolVersion)
archive(poolId, poolVersion)
```

发布逻辑：

```text
校验商品存在
校验盲盒扩展存在
校验 boxSkuId 属于 productId
校验所有 target_sku 有效
校验概率合计
校验库存配置
将老 ONLINE 版本置 OFFLINE
将新版本置 ONLINE
更新 trade_pd_blind_box_sku_ext.current_pool_id/current_pool_version
初始化奖池运行库存
```

### 14.5 BlindBoxProductValidateService

职责：

```text
validateCreateBlindBoxProduct
validateBlindBoxConfig
validatePoolPublish
validateTargetSku
validateSaleable
validateSnapshot
```

关键校验：

```text
Product.saleMode = BLIND_BOX
Product.fulfillMode = DRAW_RESULT 或 DIRECT
Product.status 可用
Sku 属于 Product
Sku price > 0
盲盒 SKU 存在 stock
奖池至少 1 个 item
概率合计 = 100%
每个 target_sku 不重复
target_sku 不是盲盒 SKU
target_sku 未删除
target_product 未删除
target_sku 可履约
```

---

## 15. 与现有创建发布链路的兼容

### 15.1 普通商品创建不变

普通商品继续走：

```text
PublishProductProxy#addProduct
  -> validateAddParams
  -> productWriteService.save
  -> productRelations.add
  -> TradeSkuService#createOrUpdate
```

默认：

```text
sale_mode = NORMAL
fulfill_mode = DIRECT
```

### 15.2 盲盒商品创建

建议新增后台聚合服务：

```text
BlindBoxPublishProductProxy#createBlindBoxProduct
```

流程：

```text
1. 调用 PublishProductProxy.validateAddParams
2. 强制 productDto.saleMode = BLIND_BOX
3. 强制 productDto.fulfillMode = DRAW_RESULT
4. 调用 PublishProductProxy.addProduct 创建标准商品与SKU
5. 保存 trade_pd_blind_box_product_ext
6. 保存 trade_pd_blind_box_sku_ext
7. 创建奖池 DRAFT 版本
8. 保存奖池明细
9. 提交盲盒配置审核
```

为什么不直接改 `PublishProductProxy`：

- 现有类已经承载普通商品、模板商品、创作者商品发布聚合。
- 盲盒创建还涉及奖池版本和概率审核，复杂度应隔离。
- 只在必要处复用现有商品创建能力。

### 15.3 上下架逻辑

现有 `ProductWriteService#upOrDown` 仍然负责商品上下架。

盲盒上架前增加校验：

```text
if saleMode == BLIND_BOX:
  盲盒扩展配置审核通过
  至少一个 box_sku_ext 启用
  每个可售 box_sku 有 ONLINE 奖池
  ONLINE 奖池明细有效
  概率文案不为空
  盲盒SKU价格 > 0
  盲盒SKU库存或销售库存配置有效
```

实现方式：

```text
ProductWriteService#upOrDown
  -> 发布前调用 BlindBoxProductValidateService.validateBeforePublish(productIdList)
```

普通商品不进入该校验。

---

## 16. 商品详情扩展

### 16.1 现有详情链路

当前详情接口：

```text
GET /product/query?productId=xxx
ProductController#getProductDetail
  -> ProductReadService#getProductDetailResp
```

内部主要读取：

```text
Product
ProductExt
ProductCreator
Sku
Stock
Price
Promotion
```

### 16.2 盲盒详情补充

在 `ProductReadServiceImpl#getProductDetailResp` 中补充分支：

```text
if productDto.saleMode == BLIND_BOX:
    BlindBoxDetailResp blindBoxDetail = blindBoxProductReadService.getDetail(productId)
    productDetailResp.setBlindBoxDetail(blindBoxDetail)
```

返回内容：

```json
{
  "productId": "P_BOX_001",
  "productName": "星座系列盲盒",
  "productType": 3,
  "saleMode": 2,
  "fulfillMode": 2,
  "price": 69.00,
  "skuDtoList": [
    {
      "skuId": "S_BOX_SINGLE",
      "price": 69.00,
      "stock": 100
    }
  ],
  "blindBoxDetail": {
    "openMode": 2,
    "probabilityText": "普通款90%，稀有款9%，隐藏款1%",
    "ruleText": "购买后可手动开盒",
    "boxSkus": [
      {
        "boxSkuId": "S_BOX_SINGLE",
        "drawCount": 1,
        "currentPoolId": "POOL_001",
        "currentPoolVersion": 3
      }
    ],
    "displayedPoolItems": [
      {
        "targetProductId": "P_NORMAL_001",
        "targetSkuId": "S_NORMAL_001",
        "productName": "白羊座手办",
        "itemLevel": 1,
        "probability": 0.10000000,
        "hidden": false
      }
    ]
  }
}
```

### 16.3 查询性能

详情页不要循环逐个查盒内商品。

推荐：

```text
1. 查询盲盒配置
2. 查询 boxSkuExt 列表
3. 查询当前 pool
4. 查询 poolItem 列表
5. 批量查询 targetProduct
6. 批量查询 targetSku
7. 组装返回
```

---

## 17. 商品可售性设计

商品域提供可售性校验，不执行下单。

### 17.1 普通商品可售性

现有普通商品校验大体依赖：

```text
Product.status
Product.auditStatus
Sku
Stock
Price
ShippingMode
Promotion
```

### 17.2 盲盒商品额外校验

新增：

```text
saleMode = BLIND_BOX
fulfillMode = DRAW_RESULT
BlindBoxProductExt.status = APPROVED
boxSkuExt.status = ENABLED
boxSkuExt.currentPoolId/currentPoolVersion 不为空
pool.status = ONLINE
poolItem 可抽列表不为空
概率配置合法
奖池运行库存有可用数量
```

建议新增内部接口：

```text
POST /inner/blind-box/product/sale-check
```

请求：

```json
{
  "productId": "P_BOX_001",
  "boxSkuId": "S_BOX_SINGLE",
  "quantity": 1,
  "scene": "PRE_ORDER"
}
```

返回：

```json
{
  "saleable": true,
  "saleMode": 2,
  "fulfillMode": 2,
  "poolId": "POOL_001",
  "poolVersion": 3,
  "drawCount": 1,
  "reasonCode": null,
  "reasonMessage": null
}
```

---

## 18. 商品快照接口设计

后续订单创建时需要调用商品快照能力。

### 18.1 快照生成接口

```text
POST /inner/blind-box/product/snapshot
```

请求：

```json
{
  "productId": "P_BOX_001",
  "boxSkuId": "S_BOX_SINGLE",
  "quantity": 1
}
```

返回：

```json
{
  "snapshotId": "BBS202606300001",
  "productId": "P_BOX_001",
  "boxSkuId": "S_BOX_SINGLE",
  "poolId": "POOL_001",
  "poolVersion": 3,
  "drawCount": 1
}
```

生成内容：

```text
ProductBasicDto
SkuDto
BlindBoxProductExt
BlindBoxSkuExt
BlindBoxPool
BlindBoxPoolItem
target ProductBasicDto
target SkuDto
```

订单后续只保存 `snapshotId + poolId + poolVersion`，不用反查“当前在线配置”。

---

## 19. 与库存的兼容

### 19.1 盲盒售卖库存

盲盒售卖库存仍使用现有：

```text
trade_pd_stock.product_id = box_product_id
trade_pd_stock.sku_id = box_sku_id
```

含义：

```text
用户最多可以买多少个盲盒SKU
```

例如：

```text
S_BOX_SINGLE 库存 1000
S_BOX_TEN    库存 200
```

### 19.2 奖池库存

奖池库存不使用 `trade_pd_stock`，使用独立 `trade_pd_blind_box_pool_stock`。

原因：

1. 同一个盒内 SKU 可能同时普通售卖，也可能被多个盲盒奖池引用。
2. 奖池库存是玩法库存，不等于仓库实物库存。
3. 抽盒扣减必须按 `pool_id + pool_version + target_sku_id` 维度。
4. 不能因为盒内 SKU 普通商品售罄就直接破坏已下单盲盒结果，需要补偿策略。

### 19.3 库存一致性

配置奖池时：

```text
sum(poolItem.plannedStock) <= 可分配给盲盒的目标 SKU 库存
```

第一阶段可以由运营人工保证，并在发布时做弱校验。后续可以增加库存分配表，明确某个普通 SKU 分配给盲盒奖池的数量。

---

## 20. 与搜索索引的兼容

### 20.1 ProductIndex 扩展

新增字段：

```java
@Field(type = FieldType.Integer)
private Integer saleMode;

@Field(type = FieldType.Integer)
private Integer fulfillMode;

@Field(type = FieldType.Boolean)
private Boolean blindBoxFlag;

@Field(type = FieldType.Keyword)
private String blindBoxPoolId;

@Field(type = FieldType.Integer)
private Integer blindBoxPoolVersion;
```

### 20.2 列表展示

搜索列表不需要返回完整奖池明细，只需要：

```text
blindBoxFlag
probabilitySummary
poolItemCount
minItemLevel/maxItemLevel
```

其中 `probabilitySummary` 可以从 `trade_pd_blind_box_product_ext.probability_text` 或 ES 冗余字段获取。

详情页再查完整奖池。

---

## 21. 审核与发布

### 21.1 商品审核

现有商品审核继续负责：

```text
商品名称
描述
主图
详情
类目
SKU
价格
```

### 21.2 盲盒配置审核

新增盲盒配置审核：

```text
盲盒规则文案
概率文案
奖池明细
概率配置
隐藏款展示
盒内商品状态
奖池库存
```

盲盒商品上架条件：

```text
Product.auditStatus = APPROVED
BlindBoxProductExt.status = APPROVED
BlindBoxPool.status = ONLINE
```

### 21.3 为什么需要单独审核

普通商品审核只看商品内容是否可展示。盲盒审核还要看：

- 概率是否合计 100%。
- 前台概率文案是否和后台一致。
- 隐藏款是否符合运营要求。
- 盒内商品是否真实可履约。
- 奖池库存是否合理。
- 售后规则是否明确。

---

## 22. 后台管理接口设计

### 22.1 创建盲盒商品

```text
POST /admin/blind-box/product/create
```

入参：

```text
ProductCreateCmd
BlindBoxProductConfigCmd
List<BlindBoxSkuConfigCmd>
BlindBoxPoolSaveCmd
```

流程：

```text
创建标准商品
创建标准SKU
保存盲盒商品扩展
保存盲盒SKU扩展
创建奖池草稿
保存奖池明细
提交审核
```

### 22.2 编辑奖池

```text
POST /admin/blind-box/pool/save
```

规则：

- 只能编辑 `DRAFT` 或 `REJECTED` 版本。
- `ONLINE` 版本不可编辑。
- 对在线奖池的修改必须创建新版本。

### 22.3 发布奖池版本

```text
POST /admin/blind-box/pool/publish
```

流程：

```text
校验 APPROVED
下线当前 ONLINE 版本
发布新版本为 ONLINE
更新 boxSkuExt.currentPoolId/currentPoolVersion
初始化或刷新运行库存
记录操作日志
```

### 22.4 查询盲盒详情

```text
GET /admin/blind-box/product/detail?productId=xxx
```

返回：

```text
商品基础信息
SKU列表
盲盒配置
当前在线奖池
历史奖池版本
奖池明细
审核状态
```

---

## 23. C 端接口兼容

### 23.1 商品详情

现有：

```text
GET /product/query?productId=xxx
```

兼容方式：

- 普通商品返回结构基本不变。
- 盲盒商品额外返回 `saleMode/fulfillMode/blindBoxDetail`。
- 老端忽略新增字段不受影响。

### 23.2 预下单

现有：

```text
POST /product/detail
```

当前 Controller 进入：

```text
preOrderService.getPreOrder(contentId, productId, skuId, quantity, source, promotionId)
```

后续订单阶段需要在 `PreOrderService` 中增加：

```text
Product.saleMode = BLIND_BOX 时:
  调用 BlindBoxProductValidateService.validateSaleable
  返回盲盒快照信息
  不返回具体 targetSku
```

本阶段商品域先提供 sale-check 和 snapshot 能力。

---

## 24. 普通商品作为盒内商品的规则

### 24.1 引用规则

普通商品不需要改造即可作为盒内商品。

奖池项记录：

```text
target_product_id
target_sku_id
```

### 24.2 校验规则

加入奖池时校验：

```text
target_product exists
target_product.closed = false
target_sku exists
target_sku.closed = false
target_sku.product_id = target_product_id
target_product.sale_mode != BLIND_BOX
target_sku.price is not null
target_sku.img_url is not blank 或 target_product.product_images 不为空
```

发布奖池时再次校验，避免配置后目标商品被下架或删除。

### 24.3 商品状态变化

如果盒内商品下架：

```text
已在线奖池不自动删除该 item
新订单可根据风控配置暂停盲盒售卖
已下单订单仍按绑定版本处理
运营收到告警并创建新奖池版本
```

不要直接修改在线奖池，否则历史订单无法回溯。

---

## 25. 普通商品升级为盲盒商品

### 25.1 不建议直接升级

不建议将已有普通商品直接改成盲盒商品：

```text
sale_mode NORMAL -> BLIND_BOX
fulfill_mode DIRECT -> DRAW_RESULT
```

原因：

- 历史订单语义会变化。
- 搜索、推荐和评价会混乱。
- 原 SKU 从实物 SKU 变成抽盒 SKU。
- 履约从直接发货变成开盒后发货。

### 25.2 推荐方式

```text
保留原普通商品
新建盲盒商品
将原普通商品SKU作为盒内商品加入奖池
```

只有在商品未上架、无订单、无库存流水、无第三方平台同步关系时，才允许后台工具执行类型转换。

---

## 26. 关键校验清单

### 26.1 创建盲盒商品

```text
ProductCreateCmd 合法
productDto.saleMode = BLIND_BOX
productDto.fulfillMode = DRAW_RESULT
skuDtoList 非空
每个盲盒 SKU price > 0
每个盲盒 SKU 有库存配置
BlindBoxProductConfig 非空
BlindBoxSkuConfig 覆盖所有可售 SKU
```

### 26.2 奖池保存

```text
poolItem 非空
target_sku 不重复
target_sku 属于 target_product
target_sku 不是盲盒 SKU
probability > 0
weight_value > 0
planned_stock >= 0
item_level 合法
```

### 26.3 奖池发布

```text
sum(probability) = 1.00000000
sum(weight_value) > 0
至少一个 displayed item
概率文案不为空
所有 target_sku 有效
盲盒商品未删除
盲盒SKU未删除
奖池版本不是 ONLINE
```

### 26.4 商品上架

```text
普通商品: 保持现有校验
盲盒商品:
  Product.auditStatus = APPROVED
  BlindBoxProductExt.status = APPROVED
  每个启用 boxSkuExt 有 ONLINE pool
  ONLINE pool item 有效
  概率文案和规则文案完整
```

---

## 27. 对现有代码的改造清单

### 27.1 Entity/DTO

需要改造：

```text
Product
ProductDto
ProductBasicDto
ProductDetailResp
ProductIndex
ProductConverter
```

新增：

```text
BlindBoxProductExt
BlindBoxSkuExt
BlindBoxPool
BlindBoxPoolItem
BlindBoxProductSnapshot
```

### 27.2 Repository/Mapper

新增：

```text
BlindBoxProductExtRepository / Mapper
BlindBoxSkuExtRepository / Mapper
BlindBoxPoolRepository / Mapper
BlindBoxPoolItemRepository / Mapper
BlindBoxProductSnapshotRepository / Mapper
```

### 27.3 Service

新增：

```text
BlindBoxProductReadService
BlindBoxProductWriteService
BlindBoxPoolReadService
BlindBoxPoolWriteService
BlindBoxProductSnapshotService
BlindBoxProductValidateService
BlindBoxPublishProductProxy
```

改造：

```text
ProductReadServiceImpl#getProductDetailResp
ProductWriteServiceImpl#upOrDown
ProductConverter
ProductIndexService
PreOrderService 后续订单阶段改造
```

### 27.4 Controller

新增后台接口：

```text
BlindBoxProductAdminController
BlindBoxPoolAdminController
```

新增内部接口：

```text
BlindBoxProductInnerController
```

不建议改造：

```text
ProductController#updateProduct
```

该接口是用户编辑展示信息用的，不应该承载盲盒奖池配置。

---

## 28. 示例

### 28.1 盲盒商品

```text
trade_pd_product
 product_id = P_BOX_001
 product_type = SHOP
 sale_mode = BLIND_BOX
 fulfill_mode = DRAW_RESULT
 product_name = 星座系列盲盒
 price = 69.00
 status = PUBLISHED

trade_pd_sku
 sku_id = S_BOX_SINGLE
 product_id = P_BOX_001
 price = 69.00
 stock = 1000

trade_pd_blind_box_sku_ext
 box_sku_id = S_BOX_SINGLE
 draw_count = 1
 current_pool_id = POOL_ZODIAC
 current_pool_version = 3
```

### 28.2 盒内商品

```text
trade_pd_product
 product_id = P_ARIES
 sale_mode = NORMAL
 fulfill_mode = DIRECT
 product_name = 白羊座手办

trade_pd_sku
 sku_id = S_ARIES_STD
 product_id = P_ARIES
 price = 99.00
```

### 28.3 奖池

```text
trade_pd_blind_box_pool
 pool_id = POOL_ZODIAC
 pool_version = 3
 status = ONLINE

trade_pd_blind_box_pool_item
 target_product_id = P_ARIES
 target_sku_id = S_ARIES_STD
 item_level = NORMAL
 probability = 0.08333333
 weight_value = 8333333
 planned_stock = 100
```

---

## 29. 分阶段落地

### 29.1 第一阶段：商品识别和详情

目标：

```text
盲盒商品能创建
盲盒扩展能保存
奖池能配置
商品详情能展示
普通商品不受影响
```

范围：

- 增加 `sale_mode/fulfill_mode`。
- 新增盲盒扩展表。
- 新增奖池表和奖池明细表。
- 新增读写 Service。
- 商品详情返回 `blindBoxDetail`。
- 商品上架增加盲盒配置校验。

### 29.2 第二阶段：版本、快照和可售性

目标：

```text
订单前能绑定稳定奖池版本
概率和规则可追溯
```

范围：

- 奖池版本发布。
- 商品快照。
- sale-check 内部接口。
- snapshot 内部接口。
- 搜索索引增加盲盒标识。

### 29.3 第三阶段：订单和抽盒

目标：

```text
用户能买盲盒
支付后能开盒
开盒结果能给履约
```

范围：

- PreOrderService 分流。
- 订单明细记录 saleMode、poolId、poolVersion、snapshotId。
- 抽盒服务。
- 奖池库存扣减。
- 履约按 targetSku 发货。

---

## 30. 风险与注意事项

### 30.1 productType 误用风险

不要把盲盒塞进 `TradeProductTypeEnum`。盲盒是售卖玩法，不是现有的商品来源类型。

### 30.2 extra JSON 滥用风险

不要把奖池和概率塞进 `trade_pd_product_ext.extra`。该字段可以补充展示扩展，但不能承载核心规则。

### 30.3 在线奖池被修改风险

上线后的奖池必须不可变。任何影响用户权益的修改都创建新版本。

### 30.4 盒内商品失效风险

盒内商品下架、删除、库存不足时，不能直接删除在线奖池项。需要告警、暂停新售卖或发布新版本。

### 30.5 订单 SKU 覆盖风险

后续订单设计中，不能在开盒后把订单明细 SKU 从盲盒 SKU 改成盒内 SKU。

正确关系：

```text
订单明细 SKU = box_sku_id
开盒结果 SKU = target_sku_id
履约 SKU = target_sku_id
```

---

## 31. 本阶段结论

基于现有 `order-java` 非 `ikj` 商品体系，推荐方案是：

```text
保留 trade_pd_product / trade_pd_sku 作为盲盒售卖商品
保留普通商品 SKU 作为盒内结果商品
新增 sale_mode / fulfill_mode 区分售卖与履约模式
新增盲盒扩展表承载玩法配置
新增奖池版本表和奖池明细表承载概率与盒内商品关系
新增快照能力为订单和抽盒服务提供稳定版本
```

该方案对普通商品侵入小，对现有 `productType` 语义影响最小，也能支持后续订单、开盒、履约继续扩展。

