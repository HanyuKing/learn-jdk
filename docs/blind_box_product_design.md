# 盲盒商品模型兼容设计方案

## 1. 文档目的

本文档重点展开盲盒玩法中的商品域设计，解决以下问题：

1. 普通商品模型如何平滑兼容盲盒商品。
2. 盲盒商品、盒内商品、奖池商品之间如何建模。
3. 商品中心、库存、订单、履约之间如何识别“卖的是盲盒，发的是盒内商品”。
4. 如何避免盲盒玩法污染普通商品主链路。
5. 如何支持后续扩展，例如隐藏款、保底、套装、不重复、虚拟权益和未拆盒发货。

本文档默认已有基础电商商品体系：

```text
SPU -> SKU -> Listing -> 库存 -> 购物车 -> 订单 -> 履约
```

普通商品场景下，一个订单明细中的 SKU 通常同时承担三种身份：

```text
售卖 SKU = 计价 SKU = 履约 SKU
```

盲盒场景会打破这个假设：

```text
售卖 SKU = 盲盒 SKU
计价 SKU = 盲盒 SKU
履约 SKU = 开盒后抽中的盒内 SKU
```

所以兼容设计的核心不是新建一套完全独立商品系统，而是让商品中心支持“商品类型扩展”和“售卖对象与履约对象分离”。

---

## 2. 总体设计结论

建议采用：

```text
普通商品主模型复用
    +
商品类型扩展
    +
盲盒商品扩展表
    +
奖池/盒内商品关系表
    +
玩法规则版本
```

不要把盲盒概率、奖池、保底、开盒规则直接写入普通 SKU 表；也不要为盲盒完全复制一套商品中心。

推荐模型：

```text
SPU
 ├─ 普通 SPU
 └─ 盲盒 SPU

SKU
 ├─ 普通 SKU
 └─ 盲盒 SKU

BlindBoxProductExt
 └─ 盲盒商品扩展信息

BlindBoxPool
 └─ 盲盒奖池

BlindBoxPoolItem
 └─ 盒内商品 SKU 关系
```

核心思想：

1. 盲盒本身是一个标准商品，可以上架、定价、参加促销、加入购物车、生成订单。
2. 盒内商品也是标准商品，可以维护成本、库存、图片、规格、仓配、售后。
3. 盲盒商品与盒内商品通过奖池关系绑定。
4. 商品中心只负责定义“是什么”和“可以卖什么”，不负责执行抽取。
5. 抽取服务基于商品中心提供的奖池快照生成开盒结果。
6. 订单记录购买的盲盒 SKU，履约记录最终抽中的盒内 SKU。

---

## 3. 普通商品模型回顾

### 3.1 普通商品核心模型

```text
product_spu
 ├─ spu_id
 ├─ product_type
 ├─ title
 ├─ category_id
 ├─ brand_id
 ├─ status
 ├─ sale_mode
 ├─ audit_status
 └─ created_at

product_sku
 ├─ sku_id
 ├─ spu_id
 ├─ sku_code
 ├─ attrs
 ├─ sale_price
 ├─ market_price
 ├─ cost_price
 ├─ weight
 ├─ volume
 ├─ barcode
 ├─ status
 └─ created_at
```

普通商品一般满足：

```text
商品详情展示 product_spu/product_sku
购物车加入 product_sku
下单购买 product_sku
库存扣减 product_sku
仓库发货 product_sku
售后处理 product_sku
```

### 3.2 普通商品链路中的隐含假设

现有系统常见隐含假设：

1. SKU 是用户明确选择的实物。
2. SKU 的价格就是订单明细价格。
3. SKU 的库存就是可售库存。
4. SKU 的图片、标题就是订单和履约展示内容。
5. SKU 可以直接传给 WMS 出库。
6. 一个订单明细只对应一个确定履约商品。

盲盒会破坏其中的第 1、3、5、6 条。用户购买时不知道最终实物，库存既有盲盒可售库存，也有盒内商品库存，履约商品需要开盒后才能确定。

---

## 4. 盲盒商品的业务定义

### 4.1 三类商品身份

盲盒商品域要显式区分三类身份。

#### 4.1.1 售卖商品

用户实际购买的商品，通常是盲盒 SKU。

```text
box_sku_id
box_spu_id
title = "星座系列盲盒单盒"
price = 69.00
```

它用于：

- 商品详情页展示。
- 购物车。
- 订单计价。
- 支付。
- 营销促销。
- 用户订单列表展示。

#### 4.1.2 盒内商品

开盒后实际可能发货的商品，通常是普通 SKU。

```text
target_sku_id
title = "星座系列-白羊座手办"
cost_price = 28.00
weight = 300g
warehouse = 华东仓
```

它用于：

- 奖池配置。
- 开盒结果展示。
- 盒内库存扣减。
- 履约发货。
- 成本核算。
- 售后换货。

#### 4.1.3 奖池商品

奖池商品不是新的商品实体，而是盲盒 SKU 与盒内 SKU 的关系。

```text
box_sku_id -> pool_id -> target_sku_id
```

它用于表达：

- 某个盒内 SKU 是否属于这个盲盒。
- 概率是多少。
- 该款属于普通款、稀有款还是隐藏款。
- 奖池内库存是多少。
- 是否展示给用户。
- 是否参与保底。

### 4.2 为什么不把盒内商品做成 SKU 属性

不建议在盲盒 SKU 的属性中维护盒内款式列表，原因：

1. SKU 属性通常用于确定规格，例如颜色、尺码、容量，不适合承载动态奖池。
2. 奖池有版本、概率、库存、展示、审核和审计需求。
3. 盒内商品可能跨 SPU、跨类目、跨仓、跨实物/虚拟权益。
4. 抽取结果需要回溯当时奖池快照，普通 SKU 属性难以做到。
5. 运营经常调整奖池，上架审核和玩法审核需要独立流程。

---

## 5. 兼容设计原则

### 5.1 保持商品中心主模型稳定

普通商品表只增加必要的类型字段和能力字段，不把盲盒专属字段全部塞进去。

推荐：

```text
product_type: NORMAL / BLIND_BOX / VIRTUAL / BUNDLE
sale_mode: NORMAL_SALE / BLIND_BOX_SALE / PRE_SALE
fulfill_mode: DIRECT / DRAW_RESULT / VIRTUAL_GRANT / NO_SHIP
```

说明：

- `product_type` 表示商品是什么。
- `sale_mode` 表示怎么卖。
- `fulfill_mode` 表示怎么履约。

普通商品：

```text
product_type = NORMAL
sale_mode = NORMAL_SALE
fulfill_mode = DIRECT
```

盲盒商品：

```text
product_type = BLIND_BOX
sale_mode = BLIND_BOX_SALE
fulfill_mode = DRAW_RESULT
```

### 5.2 用扩展表承载盲盒专属信息

盲盒扩展信息独立建表：

```text
blind_box_product_ext
```

这样普通商品查询、搜索、导购、订单链路不需要加载大量盲盒字段。

### 5.3 售卖 SKU 与履约 SKU 分离

订单明细中保留盲盒 SKU，开盒结果中保留盒内 SKU。

```text
order_item.box_sku_id
blind_box_draw_record.target_sku_id
fulfillment_order.fulfill_sku_id
```

不要在开盒后把订单明细的 `sku_id` 从盲盒 SKU 改成盒内 SKU。这样会导致支付、退款、促销、财务、用户订单展示全部混乱。

### 5.4 奖池必须版本化

盲盒奖池发布后不能直接覆盖修改。每次影响用户权益的变更都生成新版本。

```text
pool_id + pool_version
```

用户下单时记录版本：

```text
order_item.rule_version
order_item.pool_version
```

用户开盒时按照订单绑定的版本执行，避免活动中途调整概率导致争议。

### 5.5 商品中心不执行抽奖

商品中心负责提供：

- 盲盒商品基础信息。
- 盒内商品清单。
- 奖池配置。
- 商品状态。
- 商品可售性校验。
- 商品快照。

开盒服务负责：

- 校验订单。
- 校验规则。
- 计算保底。
- 随机抽取。
- 扣减盒内库存。
- 生成开盒结果。

---

## 6. 商品模型详细设计

### 6.1 product_spu 扩展

```sql
CREATE TABLE product_spu (
    spu_id              BIGINT PRIMARY KEY,
    title               VARCHAR(255) NOT NULL,
    category_id         BIGINT NOT NULL,
    brand_id            BIGINT,
    product_type        VARCHAR(32) NOT NULL DEFAULT 'NORMAL',
    sale_mode           VARCHAR(32) NOT NULL DEFAULT 'NORMAL_SALE',
    fulfill_mode        VARCHAR(32) NOT NULL DEFAULT 'DIRECT',
    status              VARCHAR(32) NOT NULL,
    audit_status        VARCHAR(32) NOT NULL,
    created_at          DATETIME NOT NULL,
    updated_at          DATETIME NOT NULL
);
```

字段说明：

| 字段 | 说明 |
| --- | --- |
| product_type | 商品类型，普通商品为 `NORMAL`，盲盒为 `BLIND_BOX` |
| sale_mode | 售卖模式，盲盒为 `BLIND_BOX_SALE` |
| fulfill_mode | 履约模式，盲盒一般为 `DRAW_RESULT` |

兼容点：

- 普通商品不受影响，默认值保持原链路。
- 搜索、类目、品牌、图片、详情页继续复用 SPU 能力。
- 订单可以根据 `sale_mode` 选择普通链路或盲盒链路。

### 6.2 product_sku 扩展

```sql
CREATE TABLE product_sku (
    sku_id              BIGINT PRIMARY KEY,
    spu_id              BIGINT NOT NULL,
    sku_code            VARCHAR(64) NOT NULL,
    sku_attrs           JSON,
    sale_price          DECIMAL(18, 2) NOT NULL,
    market_price        DECIMAL(18, 2),
    cost_price          DECIMAL(18, 2),
    weight              DECIMAL(18, 3),
    volume              DECIMAL(18, 3),
    barcode             VARCHAR(64),
    status              VARCHAR(32) NOT NULL,
    created_at          DATETIME NOT NULL,
    updated_at          DATETIME NOT NULL,
    UNIQUE KEY uk_sku_code (sku_code),
    KEY idx_spu_id (spu_id)
);
```

盲盒 SKU 仍然是 SKU，但它代表的是“购买一次盲盒机会”或“一盒未拆封商品”，不代表最终款式。

示例：

```text
SPU: 星座系列盲盒
SKU1: 单盒
SKU2: 端盒 12 盒
SKU3: 未拆封整箱
```

兼容点：

- 价格仍挂在盲盒 SKU 上。
- 购物车和订单仍使用 `sku_id`。
- 普通价格中心无需理解盒内商品概率。
- 盲盒 SKU 可以参与优惠券、会员价、限时折扣。

注意：

- 盲盒 SKU 的 `weight` 可以表示未拆盒的包装重量。
- 如果业务只支持开盒后发货，履约不应直接使用盲盒 SKU 的重量。
- 如果支持“未拆盒发货”，盲盒 SKU 自身也必须配置可履约资料。

### 6.3 blind_box_product_ext

```sql
CREATE TABLE blind_box_product_ext (
    id                      BIGINT PRIMARY KEY,
    box_spu_id              BIGINT NOT NULL,
    box_sku_id              BIGINT,
    open_mode               VARCHAR(32) NOT NULL,
    pool_bind_mode          VARCHAR(32) NOT NULL,
    result_display_mode     VARCHAR(32) NOT NULL,
    support_unopened_ship   TINYINT NOT NULL DEFAULT 0,
    support_auto_open       TINYINT NOT NULL DEFAULT 0,
    support_manual_open     TINYINT NOT NULL DEFAULT 1,
    support_batch_open      TINYINT NOT NULL DEFAULT 0,
    max_open_quantity       INT,
    detail_rule_text        TEXT,
    probability_text        TEXT,
    status                  VARCHAR(32) NOT NULL,
    created_at              DATETIME NOT NULL,
    updated_at              DATETIME NOT NULL,
    KEY idx_box_spu_id (box_spu_id),
    KEY idx_box_sku_id (box_sku_id)
);
```

字段说明：

| 字段 | 说明 |
| --- | --- |
| box_spu_id | 盲盒 SPU |
| box_sku_id | 盲盒 SKU，可为空；为空表示扩展配置作用于整个 SPU |
| open_mode | 开盒模式：支付后自动开盒、用户手动开盒、未拆盒发货 |
| pool_bind_mode | 奖池绑定方式：SPU 级、SKU 级、活动级 |
| result_display_mode | 结果展示方式：立即展示、发货后展示、隐藏部分信息 |
| support_unopened_ship | 是否支持未拆盒发货 |
| probability_text | 面向用户展示的概率说明 |

建议枚举：

```text
open_mode:
  PAY_AUTO_OPEN      支付成功后自动开盒
  MANUAL_OPEN        用户手动开盒
  SHIP_UNOPENED      未拆盒发货

pool_bind_mode:
  SPU_LEVEL          同一个 SPU 下所有 SKU 共用奖池
  SKU_LEVEL          每个盲盒 SKU 独立奖池
  ACTIVITY_LEVEL     活动维度绑定奖池

result_display_mode:
  IMMEDIATE          开盒后立即展示
  PARTIAL            只展示等级或部分信息
  AFTER_SHIP         发货后展示
```

### 6.4 blind_box_pool

```sql
CREATE TABLE blind_box_pool (
    pool_id             BIGINT PRIMARY KEY,
    pool_code           VARCHAR(64) NOT NULL,
    pool_name           VARCHAR(255) NOT NULL,
    box_spu_id          BIGINT NOT NULL,
    box_sku_id          BIGINT,
    pool_version        INT NOT NULL,
    version_status      VARCHAR(32) NOT NULL,
    effective_time      DATETIME,
    expire_time         DATETIME,
    total_pool_stock    INT,
    operator_id         BIGINT,
    audit_status        VARCHAR(32) NOT NULL,
    created_at          DATETIME NOT NULL,
    updated_at          DATETIME NOT NULL,
    UNIQUE KEY uk_pool_version (pool_id, pool_version),
    KEY idx_box_spu_id (box_spu_id),
    KEY idx_box_sku_id (box_sku_id)
);
```

设计说明：

- `pool_id` 表示奖池逻辑身份。
- `pool_version` 表示奖池版本。
- 同一个盲盒商品可以随着运营调整发布多个奖池版本。
- 已被订单引用的版本不能物理删除。

状态建议：

```text
DRAFT      草稿
REVIEWING  审核中
APPROVED   审核通过
ONLINE     生效中
OFFLINE    已下线
ARCHIVED   已归档
```

### 6.5 blind_box_pool_item

```sql
CREATE TABLE blind_box_pool_item (
    id                      BIGINT PRIMARY KEY,
    pool_id                 BIGINT NOT NULL,
    pool_version            INT NOT NULL,
    target_spu_id           BIGINT NOT NULL,
    target_sku_id           BIGINT NOT NULL,
    item_level              VARCHAR(32) NOT NULL,
    probability             DECIMAL(12, 8) NOT NULL,
    weight_value            INT NOT NULL,
    pool_stock              INT NOT NULL,
    display_stock           INT,
    max_draw_quantity       INT,
    is_hidden               TINYINT NOT NULL DEFAULT 0,
    is_displayed            TINYINT NOT NULL DEFAULT 1,
    is_guarantee_item       TINYINT NOT NULL DEFAULT 0,
    sort_no                 INT NOT NULL DEFAULT 0,
    status                  VARCHAR(32) NOT NULL,
    created_at              DATETIME NOT NULL,
    updated_at              DATETIME NOT NULL,
    UNIQUE KEY uk_pool_target (pool_id, pool_version, target_sku_id),
    KEY idx_target_sku_id (target_sku_id)
);
```

字段说明：

| 字段 | 说明 |
| --- | --- |
| target_sku_id | 盒内商品 SKU |
| item_level | 款式等级，例如普通款、稀有款、隐藏款 |
| probability | 展示和审计使用的概率 |
| weight_value | 抽取算法使用的权重 |
| pool_stock | 当前奖池版本可抽库存 |
| display_stock | 前台展示库存，可选 |
| max_draw_quantity | 该款最大可抽数量 |
| is_hidden | 是否隐藏款 |
| is_displayed | 是否在详情页展示 |
| is_guarantee_item | 是否可作为保底商品 |

概率与权重关系：

```text
probability = weight_value / sum(weight_value)
```

也可以运营直接配置概率，系统发布时换算成权重。为了避免浮点误差，抽取服务建议使用整数权重。

### 6.6 商品快照

用户下单和开盒时需要保留商品快照。

```sql
CREATE TABLE blind_box_product_snapshot (
    snapshot_id             BIGINT PRIMARY KEY,
    activity_id             BIGINT,
    box_spu_id              BIGINT NOT NULL,
    box_sku_id              BIGINT NOT NULL,
    pool_id                 BIGINT NOT NULL,
    pool_version            INT NOT NULL,
    box_product_snapshot    JSON NOT NULL,
    pool_snapshot           JSON NOT NULL,
    pool_item_snapshot      JSON NOT NULL,
    created_at              DATETIME NOT NULL
);
```

快照中至少包含：

- 盲盒标题。
- 盲盒主图。
- 盲盒价格。
- 玩法规则。
- 概率说明。
- 奖池商品列表。
- 盒内商品标题、图片、等级、概率。

用途：

- 订单展示。
- 用户争议处理。
- 售后审核。
- 概率审计。
- 活动复盘。

---

## 7. 商品类型兼容方案

### 7.1 方案一：只加商品类型字段

做法：

```text
product_spu.product_type = BLIND_BOX
```

优点：

- 改造成本最低。
- 普通商品链路基本不变。

缺点：

- 盲盒的奖池、概率、保底、展示规则无处承载。
- 容易把复杂 JSON 塞进商品扩展属性。
- 后续运营、审核、审计困难。

结论：

不建议单独使用，只适合非常轻量的营销展示，不适合真实抽取履约。

### 7.2 方案二：完全独立盲盒商品系统

做法：

```text
blind_box_spu
blind_box_sku
blind_box_pool
```

优点：

- 盲盒玩法自由度高。
- 与普通商品隔离彻底。

缺点：

- 搜索、详情、价格、促销、购物车、订单、库存、履约全部要适配两套商品身份。
- 盒内商品还要与普通 SKU 打通。
- 重复建设商品基础能力。

结论：

不建议作为主方案，除非盲盒是完全独立业务线，且不复用原电商交易体系。

### 7.3 方案三：商品主模型复用 + 盲盒扩展表

做法：

```text
product_spu/product_sku 继续作为商品主数据
product_type 区分普通商品和盲盒商品
blind_box_product_ext 承载盲盒扩展
blind_box_pool/blind_box_pool_item 承载奖池
```

优点：

- 最大化复用普通商品能力。
- 盲盒专属复杂度独立管理。
- 订单和履约只需要在关键节点识别商品类型。
- 后续可以支持更多玩法商品，例如福袋、随机款、组合包。

缺点：

- 商品中心需要支持类型化校验。
- 订单和履约需要支持售卖 SKU 与履约 SKU 分离。

结论：

推荐采用。

---

## 8. 普通商品到盲盒商品的兼容路径

### 8.1 新建盲盒商品

新建盲盒商品时，仍走普通商品创建流程：

```text
创建 SPU
  -> product_type = BLIND_BOX
  -> sale_mode = BLIND_BOX_SALE
  -> fulfill_mode = DRAW_RESULT

创建 SKU
  -> 单盒 SKU
  -> 端盒 SKU
  -> 整箱 SKU

配置盲盒扩展
  -> 开盒模式
  -> 结果展示方式
  -> 是否支持未拆盒发货

配置奖池
  -> 选择盒内商品 SKU
  -> 配置概率/权重
  -> 配置奖池库存
  -> 配置隐藏款/保底标识

审核发布
  -> 商品审核
  -> 奖池审核
  -> 概率展示审核
```

### 8.2 将普通商品作为盒内商品

普通商品成为盒内商品时，不需要改变自身 `product_type`。

例如：

```text
target_sku_id = 普通手办 SKU
product_type = NORMAL
```

它只是被 `blind_box_pool_item` 引用。

被引用前需要校验：

1. SKU 状态为可售或可履约。
2. SKU 有完整主图、标题、规格。
3. SKU 有成本价。
4. SKU 有重量、体积、仓配资料。
5. SKU 不是另一个不可嵌套的盲盒 SKU。
6. SKU 满足类目和合规要求。

是否允许盲盒嵌套盲盒要谨慎。默认不允许：

```text
blind_box_pool_item.target_sku.product_type != BLIND_BOX
```

除非业务明确支持“套娃盲盒”，否则会让订单、概率、售后和履约复杂度大幅上升。

### 8.3 普通商品升级为盲盒商品

不建议把一个已经正常售卖的普通 SPU 直接修改成盲盒 SPU。

原因：

- 历史订单语义会改变。
- 搜索和推荐标签会混乱。
- 价格和库存意义会变化。
- 用户收藏、评价、售后可能受到影响。

推荐做法：

```text
保留原普通 SPU
新建盲盒 SPU
将原普通 SKU 作为盒内商品加入奖池
```

示例：

```text
原商品：白羊座手办 SKU
新商品：星座系列盲盒单盒 SKU
奖池：白羊座、金牛座、双子座等 SKU
```

只有在商品未上架、无历史订单、无外部引用时，才允许修改类型。

### 8.4 盲盒商品降级为普通商品

原则上不支持直接降级。

如果业务要停止盲盒玩法，应：

```text
盲盒商品下架
奖池归档
保留历史订单和开盒记录
新建普通商品继续售卖具体款式
```

---

## 9. 商品中心对外能力改造

### 9.1 商品详情接口

普通商品详情：

```text
GET /products/{spuId}
```

盲盒商品详情可以复用同一接口，但返回结构需要包含盲盒扩展。

```json
{
  "spuId": 1001,
  "productType": "BLIND_BOX",
  "saleMode": "BLIND_BOX_SALE",
  "fulfillMode": "DRAW_RESULT",
  "title": "星座系列盲盒",
  "skus": [
    {
      "skuId": 2001,
      "skuName": "单盒",
      "salePrice": "69.00"
    }
  ],
  "blindBox": {
    "openMode": "MANUAL_OPEN",
    "supportUnopenedShip": false,
    "probabilityText": "普通款 90%，稀有款 9%，隐藏款 1%",
    "poolVersion": 3,
    "poolItems": [
      {
        "targetSkuId": 3001,
        "title": "白羊座手办",
        "level": "NORMAL",
        "probability": "10.0000%",
        "displayed": true
      }
    ]
  }
}
```

兼容策略：

- 普通商品不返回 `blindBox` 字段。
- 前端根据 `productType` 决定渲染普通详情页还是盲盒详情组件。
- 搜索列表只展示基础商品信息，不强依赖盲盒奖池详情。

### 9.2 商品可售性校验

新增统一接口：

```text
POST /product/sale-check
```

请求：

```json
{
  "skuId": 2001,
  "quantity": 1,
  "scene": "ORDER_CREATE",
  "userId": 9001
}
```

普通商品校验：

```text
SKU 状态
SPU 状态
库存
价格
区域可售
```

盲盒商品额外校验：

```text
盲盒扩展是否有效
奖池是否已发布
奖池版本是否在线
奖池是否有可抽库存
概率展示是否审核通过
是否满足限购
是否处于活动时间
```

返回：

```json
{
  "saleable": true,
  "productType": "BLIND_BOX",
  "saleMode": "BLIND_BOX_SALE",
  "poolId": 5001,
  "poolVersion": 3,
  "reasonCode": null
}
```

### 9.3 商品快照接口

订单创建时调用：

```text
POST /product/snapshot
```

盲盒商品快照需要包含两部分：

```text
盲盒售卖商品快照
奖池与盒内商品快照
```

订单中保存：

```text
box_product_snapshot_id
pool_snapshot_id
```

开盒服务按照订单快照和规则版本执行，避免读取到新版本奖池。

---

## 10. 与价格系统的兼容

### 10.1 价格归属

盲盒订单价格取盲盒 SKU：

```text
order_item.sku_id = box_sku_id
order_item.sale_price = box_sku.sale_price
```

盒内商品价格不参与订单计价。

```text
target_sku_id.cost_price -> 成本核算
target_sku_id.market_price -> 结果展示参考
```

### 10.2 促销兼容

盲盒 SKU 可以参加普通促销：

- 满减。
- 优惠券。
- 会员价。
- 积分抵扣。
- 限时折扣。

但需要增加促销准入：

```text
promotion.allow_product_type 包含 BLIND_BOX
```

不建议默认所有促销都支持盲盒，尤其是：

- 买赠。
- 换购。
- 第二件半价。
- 组合套装。
- 跨店满减。

这些玩法可能与概率、限购、保底叠加，导致用户权益和财务口径复杂。

---

## 11. 与库存系统的兼容

### 11.1 两类库存

盲盒商品涉及两类库存：

```text
盲盒可售库存
盒内奖池库存
```

盲盒可售库存：

```text
用户最多能买多少盒
```

盒内奖池库存：

```text
每个款式最多能被抽中多少次
```

### 11.2 库存模型

普通库存表仍保留：

```text
sku_inventory
 ├─ sku_id
 ├─ warehouse_id
 ├─ available_qty
 ├─ locked_qty
 └─ sold_qty
```

盲盒增加奖池库存：

```text
blind_box_pool_inventory
 ├─ pool_id
 ├─ pool_version
 ├─ target_sku_id
 ├─ total_qty
 ├─ available_qty
 ├─ locked_qty
 ├─ drawn_qty
 └─ shipped_qty
```

### 11.3 库存扣减链路

下单时：

```text
扣/锁盲盒 SKU 可售库存
```

开盒时：

```text
扣/锁盒内 target_sku_id 奖池库存
```

履约时：

```text
按 target_sku_id 扣仓库实物库存或占用履约库存
```

是否需要同时维护奖池库存和仓库实物库存，取决于业务模式。

推荐：

```text
奖池库存 <= 可用于盲盒的仓库实物库存
```

奖池库存是玩法库存，仓库库存是实物库存。二者需要定期对账，避免奖池抽中了但仓库不可发。

---

## 12. 与订单系统的兼容

### 12.1 订单明细保持盲盒 SKU

订单明细：

```text
order_item
 ├─ order_item_id
 ├─ order_id
 ├─ sku_id = box_sku_id
 ├─ product_type = BLIND_BOX
 ├─ sale_mode = BLIND_BOX_SALE
 ├─ fulfill_mode = DRAW_RESULT
 ├─ quantity
 ├─ sale_price
 ├─ pool_id
 ├─ pool_version
 ├─ open_status
 └─ product_snapshot_id
```

开盒结果：

```text
blind_box_draw_record
 ├─ draw_id
 ├─ order_item_id
 ├─ box_sku_id
 ├─ target_sku_id
 ├─ pool_id
 ├─ pool_version
 ├─ draw_status
 └─ draw_time
```

订单展示：

```text
未开盒：展示盲盒商品
已开盒：展示盲盒商品 + 开盒结果
已发货：展示盒内商品和物流信息
```

### 12.2 不要覆盖订单 SKU

错误做法：

```text
开盒后 order_item.sku_id = target_sku_id
```

问题：

- 退款金额无法解释。
- 优惠分摊会错乱。
- 用户购买记录从盲盒变成具体商品。
- 财务收入和成本混淆。
- 售后无法判断是否是盲盒订单。

正确做法：

```text
order_item.sku_id 始终是 box_sku_id
draw_record.target_sku_id 记录开盒结果
fulfillment_order.sku_id 使用 target_sku_id
```

---

## 13. 与履约系统的兼容

### 13.1 履约 SKU 来源

普通商品：

```text
fulfill_sku_id = order_item.sku_id
```

盲盒商品：

```text
fulfill_sku_id = draw_record.target_sku_id
```

履约系统可以通过 `fulfill_mode` 决定取数方式：

```text
DIRECT       从订单明细取 SKU
DRAW_RESULT  从开盒结果取 SKU
VIRTUAL      调用权益发放
NO_SHIP      不生成实物履约
```

### 13.2 未开盒是否允许履约

根据 `open_mode` 决定：

```text
MANUAL_OPEN:
  未开盒不生成履约单

PAY_AUTO_OPEN:
  支付成功自动开盒，成功后生成履约单

SHIP_UNOPENED:
  可以直接按盲盒 SKU 发货
```

如果支持未拆盒发货，盲盒 SKU 本身必须配置：

- 重量。
- 体积。
- 条码。
- 包装资料。
- 仓库库存。
- 发货品名。

---

## 14. 商品审核与发布流程

### 14.1 普通商品审核

普通商品审核原流程不变：

```text
商品资料提交
 -> 类目属性校验
 -> 图片/文案审核
 -> 价格审核
 -> 上架
```

### 14.2 盲盒商品审核

盲盒需要增加玩法审核：

```text
商品资料提交
 -> 类目属性校验
 -> 图片/文案审核
 -> 盲盒扩展配置校验
 -> 奖池商品校验
 -> 概率与库存校验
 -> 规则文案审核
 -> 合规审核
 -> 上架
```

校验项：

1. 奖池至少包含一个有效盒内 SKU。
2. 奖池概率总和等于 100% 或权重总和大于 0。
3. 每个盒内 SKU 有可履约资料。
4. 每个盒内 SKU 有奖池库存。
5. 前台概率文案与后台配置一致。
6. 隐藏款是否符合展示策略。
7. 盲盒售价与盒内价值区间是否通过业务审核。
8. 是否配置售后规则。
9. 是否配置未成年人或高风险用户限制，按业务要求启用。

### 14.3 发布版本

发布时生成不可变版本：

```text
商品版本
盲盒扩展版本
奖池版本
概率版本
文案版本
```

订单引用版本，不引用“当前最新配置”。

---

## 15. 前台展示兼容

### 15.1 搜索和推荐

搜索索引增加字段：

```text
product_type
sale_mode
blind_box_flag
blind_box_level_summary
probability_summary
```

普通商品展示：

```text
商品图 + 标题 + 价格
```

盲盒商品展示：

```text
盲盒图 + 标题 + 价格 + 盲盒标识 + 款式数量/概率入口
```

### 15.2 商品详情

盲盒详情页建议分区：

1. 盲盒主商品信息。
2. 可抽款式列表。
3. 概率说明。
4. 玩法规则。
5. 售后规则。
6. 开盒方式。
7. 发货说明。

展示注意事项：

- 概率入口要明显。
- 隐藏款可展示剪影或不展示，取决于业务规则。
- 不要把盒内商品价格当成用户购买价展示。
- 不要承诺一定抽中特定款，除非有保底规则。

---

## 16. 后台运营兼容

### 16.1 创建流程

后台创建盲盒商品建议分为 5 步：

```text
1. 创建基础商品
2. 创建盲盒 SKU
3. 配置盲盒玩法
4. 配置奖池和概率
5. 提交审核并发布
```

### 16.2 奖池商品选择器

运营选择盒内商品时，选择器应支持：

- 按 SPU/SKU 搜索。
- 按类目筛选。
- 按品牌筛选。
- 按仓库库存筛选。
- 排除不可履约商品。
- 排除下架商品。
- 排除不合规商品。
- 排除盲盒商品，除非显式允许嵌套。

### 16.3 批量导入

奖池通常需要批量导入：

```text
target_sku_code
item_level
probability
pool_stock
is_hidden
is_displayed
sort_no
```

导入后必须做校验：

- SKU 是否存在。
- SKU 是否重复。
- 概率是否合法。
- 库存是否合法。
- 权重是否合法。
- 合计概率是否合法。

---

## 17. 数据一致性设计

### 17.1 商品状态一致性

盒内商品状态变化时，需要影响盲盒奖池。

例如：

```text
target_sku 下架
target_sku 禁售
target_sku 仓配资料失效
target_sku 库存不足
```

处理策略：

1. 已发布奖池不直接删除该 SKU。
2. 新订单可根据规则暂停购买或切换新奖池版本。
3. 已支付未开盒订单按原版本执行，若无法履约则进入补偿。
4. 发出告警给运营。

### 17.2 奖池版本一致性

活动在线期间修改奖池：

```text
不能直接 update 老版本
必须 create new version
新订单绑定新版本
老订单继续使用老版本
```

### 17.3 商品快照一致性

订单创建时保存：

```text
box_sku_snapshot
pool_snapshot
pool_item_snapshot
```

开盒时优先使用订单绑定版本，不使用商品当前版本。

---

## 18. 关键校验规则

### 18.1 盲盒商品保存校验

```text
product_type = BLIND_BOX 时：
  sale_mode 必须是 BLIND_BOX_SALE
  fulfill_mode 必须是 DRAW_RESULT 或 DIRECT
  必须存在 blind_box_product_ext
  必须配置 open_mode
```

### 18.2 奖池发布校验

```text
奖池商品数量 > 0
所有 target_sku 有效
所有 target_sku 可履约
所有 target_sku 未重复
权重总和 > 0
概率总和 = 100%
pool_stock 总和 >= 可售盲盒库存，或配置允许售罄重算
隐藏款概率符合业务阈值
前台概率文案与配置一致
```

### 18.3 下单校验

```text
盲盒 SKU 可售
盲盒活动有效
奖池版本在线
用户满足限购
盲盒可售库存充足
奖池可抽库存充足
商品快照生成成功
```

### 18.4 开盒校验

```text
订单已支付
订单未退款
订单明细未开盒
奖池版本与订单一致
可抽商品池非空
目标 SKU 奖池库存充足
目标 SKU 可履约或有补偿方案
```

---

## 19. 与现有系统的改造清单

### 19.1 商品中心

需要改造：

- SPU 增加商品类型、售卖模式、履约模式。
- 增加盲盒扩展表。
- 增加奖池表和奖池商品表。
- 增加奖池版本和快照能力。
- 商品详情接口支持盲盒扩展返回。
- 商品可售性校验支持盲盒规则。

### 19.2 搜索系统

需要改造：

- 索引增加商品类型。
- 支持盲盒标签。
- 支持盲盒商品排序和召回。

### 19.3 价格系统

需要改造：

- 支持盲盒 SKU 计价。
- 促销准入增加商品类型限制。
- 财务成本可以关联开盒结果。

### 19.4 库存系统

需要改造：

- 支持盲盒可售库存。
- 支持奖池库存。
- 支持开盒扣减盒内库存。
- 支持奖池库存与实物库存对账。

### 19.5 订单系统

需要改造：

- 订单明细记录商品类型。
- 订单明细记录奖池版本。
- 增加开盒状态。
- 不覆盖原订单 SKU。

### 19.6 履约系统

需要改造：

- 根据履约模式选择履约 SKU。
- 支持未开盒不履约。
- 支持开盒后按 target_sku 履约。
- 支持虚拟权益结果分流。

---

## 20. 示例：星座手办盲盒

### 20.1 普通商品

```text
SPU 101: 白羊座手办
SKU 10001: 白羊座手办标准款

SPU 102: 金牛座手办
SKU 10002: 金牛座手办标准款

SPU 103: 双子座手办
SKU 10003: 双子座手办标准款
```

这些商品可以独立售卖，也可以作为盒内商品。

### 20.2 盲盒商品

```text
SPU 200: 星座系列盲盒
product_type = BLIND_BOX
sale_mode = BLIND_BOX_SALE
fulfill_mode = DRAW_RESULT

SKU 20001: 单盒
sale_price = 69.00

SKU 20002: 端盒 12 盒
sale_price = 799.00
```

### 20.3 奖池

```text
pool_id = 300
pool_version = 1

SKU 10001 白羊座 普通款 8.3333%
SKU 10002 金牛座 普通款 8.3333%
SKU 10003 双子座 普通款 8.3333%
SKU 10099 星光隐藏款 隐藏款 1.0000%
```

### 20.4 订单

用户购买单盒：

```text
order_item.sku_id = 20001
order_item.sale_price = 69.00
order_item.product_type = BLIND_BOX
order_item.pool_id = 300
order_item.pool_version = 1
order_item.open_status = NOT_OPEN
```

开盒后：

```text
draw_record.box_sku_id = 20001
draw_record.target_sku_id = 10002
draw_record.item_level = NORMAL
```

履约：

```text
fulfillment_order.sku_id = 10002
```

退款：

```text
refund_amount 基于 order_item.sale_price = 69.00
不是基于 target_sku_id 的市场价
```

---

## 21. 推荐落地顺序

### 21.1 第一阶段：基础兼容

目标：能卖、能开、能发。

范围：

- SPU/SKU 增加商品类型。
- 新增盲盒扩展表。
- 新增奖池和奖池商品表。
- 商品详情支持盲盒信息。
- 订单明细记录盲盒商品类型和奖池版本。
- 开盒后生成 target_sku。
- 履约按 target_sku 发货。

### 21.2 第二阶段：版本和审计

目标：可追溯、可审核、可处理客诉。

范围：

- 奖池版本。
- 商品快照。
- 概率快照。
- 操作日志。
- 审核流程。
- 概率偏差监控。

### 21.3 第三阶段：高级玩法

目标：支持运营玩法扩展。

范围：

- 保底。
- 不重复。
- 端盒规则。
- 套装收集。
- 隐藏款。
- 自动开盒。
- 未拆盒发货。
- 虚拟权益。

---

## 22. 设计边界

商品中心负责：

```text
商品身份
商品类型
盲盒扩展
奖池配置
盒内商品关系
商品快照
商品可售性
```

商品中心不负责：

```text
随机抽取
保底计算
库存实际扣减
支付
订单状态流转
仓库出库
退款决策
```

这些能力应该分别放在开盒服务、库存服务、订单服务、履约服务和售后服务中。

最终推荐架构：

```text
商品中心定义盲盒
营销/活动中心定义玩法活动
订单中心销售盲盒
开盒服务决定结果
库存服务保障不超卖
履约服务发送盒内商品
售后服务按盲盒订单和盒内商品共同处理
```
