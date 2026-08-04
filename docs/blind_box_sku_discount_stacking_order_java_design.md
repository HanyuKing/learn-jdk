# order-java 盲盒 SKU 折扣叠加技术设计

## 1. 文档目的

本文档基于 `/Users/rogerswang/my/work/jjewelry/order-java` 当前非 `business/ikj` 的商品、算价、订单链路，设计盲盒售卖 SKU 的折扣叠加方案。

核心目标：

1. 说明现有 `order-java` 算价链路如何工作。
2. 保持普通商品仍然使用当前“单一最优优惠”口径。
3. 让 `sale_mode = BLIND_BOX` 的盲盒售卖 SKU 支持多组优惠叠加。
4. 设计价格结果、预下单、订单优惠冻结、支付核销、取消解冻的兼容扩展。
5. 为后续盲盒抽盒、履约、售后留出稳定的价格快照和优惠明细。

本阶段只设计技术方案，不直接修改代码。

---

## 2. 设计前提

沿用已有盲盒商品设计文档中的结论：

```text
不新增 TradeProductTypeEnum.BLIND_BOX
不把盲盒做成新的 product_type
通过 sale_mode 区分普通售卖和盲盒售卖
通过 fulfill_mode 区分订单 SKU 直接履约和抽盒结果履约
```

推荐商品身份：

| 商品身份 | 模型 | 说明 |
| --- | --- | --- |
| 普通商品 | `trade_pd_product + trade_pd_sku` | 现有商品，直接履约 |
| 盲盒售卖商品 | `trade_pd_product + trade_pd_sku` | 用户购买的盲盒 SKU，比如单抽、三抽、十抽 |
| 盒内结果商品 | `trade_pd_product + trade_pd_sku` | 开盒后实际履约的普通 SKU |

盲盒售卖 SKU 仍然是标准 `trade_pd_sku`，可以参与现有商品详情、预下单、支付、订单、促销链路。叠加优惠只在算价阶段根据 `Product.saleMode == BLIND_BOX` 开启。

---

## 3. 现有算价链路分析

### 3.1 预下单入口

当前预下单主入口：

```text
PreOrderService#getPreOrderHasAddressId
```

核心逻辑：

1. 根据 `productId` 查询 `Product`。
2. 根据 `skuId` 查询 `SkuDto`。
3. 根据 `product.productType` 分流：
   - `WANT` -> `getWantPreOrder`
   - `TEMPLATE` -> `getTemplatePreOrder`
   - `SHOP` -> `getShopPreOrder`
4. 分支内部构造 `ProductContextDto`。
5. 调用 `PriceService#getPrice(ProductContextDto)`。
6. 将 `PriceDto` 转为 `OrderProductResponse`。
7. 生成 `preOrderId`，当前 Redis 只保存：

```text
preOrderId -> skuId
TTL = 12h
```

现有风险：

- 预下单展示价和创建订单时会重新算价。
- 当前 token 只保存 `skuId`，没有保存价格快照、优惠明细、盲盒奖池版本。
- 盲盒叠加优惠需要把展示时命中的优惠组、优惠顺序、优惠金额、奖池版本一起快照，否则订单冻结时可能和用户看到的不一致。

### 3.2 价格策略入口

当前价格入口：

```text
PriceService#getPrice(ProductContextDto)
  -> PriceStrategyFactory#getPriceStrategy(productType)
  -> WantPriceStrategy / TemplatePriceStrategy / ShopPriceStrategy
```

三个策略当前模式基本一致：

```text
构造 DiscountContext
调用 MarketingService#getDiscountV2
设置 SKU 单价
计算运费
调用 PriceStrategy#buildPriceDto
```

当前 `PriceDto` 价格公式：

```text
productTotalPrice = sku.price * quantity
totalPrice        = productTotalPrice + shippingFee
payPrice          = totalPrice - discount
```

其中 `discount` 来自 `DiscountDto.discountAmount`。

现状特点：

- `PriceDto` 只有一套优惠字段：`channel/activityId/couponId/discountId/discountValue/discountType/discount`。
- 当前结构天然表达“一个最优优惠”，不能表达多层叠加优惠。
- 普通商品可以继续保持该结构，盲盒 SKU 需要扩展叠加明细，同时回填旧字段。

### 3.3 MarketingService 到 PricingService

当前优惠计算入口：

```text
MarketingService#getDiscountV2
```

逻辑：

```text
DiscountContext
  -> PricingSkuRequest(currentUserIdentityDto, productId, skuDtoList, quantity)
  -> PricingService#calculateSkuPricing
  -> SkuPricingDiscountResult
  -> new DiscountDto(result)
```

这里已经走统一 `pricing` 体系，而不是早期注释掉的多渠道顺序判断。

### 3.4 SKU 算价流程

当前 SKU 算价主链路：

```text
PricingService#calculateSkuPricing
  -> getProductForPricing
  -> getSkusForPricing
  -> PricingContext.forSkuPricing
  -> PricingFlowManager#executeSkuPricingFlow
  -> ActivityMatchingService#matchSkusWithActivities
  -> BestDiscountSelector#selectBest
  -> PricingResultBuilder#buildPricingSKUResponse
```

重要事实：

- `PricingSkuRequest.SkuRequest` 有 `quantity`。
- `PricingContext.forSkuPricing` 会把请求数量写入 `Sku.purchaseQuantity`。
- `ActivityMatchingService#createSkuWrapper` 使用 `sku.getPurchaseQuantity()`，默认值为 1。

因此新增叠加算价流程必须继续走 `PricingContext.forSkuPricing`，不能绕开上下文直接拿数据库 SKU 做计算。

### 3.5 活动匹配

当前 `ActivityMatchingService#matchSkusWithActivities` 会为每个 SKU 匹配多类活动：

1. 所有商品活动。
2. 类目活动。
3. 商品活动。
4. SKU 活动。

每个命中的活动会构造 `PromotionActivitySkuWrapper`，并调用对应折扣策略计算 `wrapper.discountInfo`。

当前候选活动是完整列表，但后续会被压成一个最优活动。

### 3.6 最优优惠选择

当前选择器：

```text
BestDiscountSelector#selectBest
```

排序规则：

1. `PromotionActivity.precedence` 升序。
2. `PricingDiscountResult.discountedPrice` 升序。
3. `PromotionActivity.activityGroup` 升序。

当前 `PromotionActivity.activityGroup` 已有分组字段：

```text
1 = 用户身份组
2 = 平台活动组
3 = 单品级组
```

但它现在只是最终 tie-breaker，不是叠加分组。

### 3.7 折扣计算能力

当前 `DiscountCalculator` 支持：

| discountType | 语义 |
| --- | --- |
| 1 | 立减 |
| 2 | 百分比折扣 |
| 3 | 促销价 |
| 4 | 阶梯百分比折扣 |
| 5 | 阶梯固定金额折扣 |
| 6 | 阶梯促销价折扣 |

当前最小价保护：

```text
MIN_PRICE = 0.01
```

当前金额精度主要使用：

```text
scale = 2
rounding = HALF_UP
```

叠加方案应复用现有折扣类型和最小价保护，但要明确“每一层折扣的计算基数”。

### 3.8 订单优惠冻结链路

创建订单时：

```text
OrderService#buildDiscount
  -> 从 OrderProductResponse 组装一个 discountInfo JSON
  -> PriceService#freeze(discountInfo, order, userId)
  -> MarketingService#freeze
  -> PromotionStrategy#freeze
  -> order.discountInfo = discountInfo.toJSONString()
```

支付成功时：

```text
OrderService#finishPay
  -> use(dbOrder)
  -> parse order.discountInfo
  -> PriceService#use
```

取消或退款解冻时：

```text
OrderService#unFreeze
  -> parse order.discountInfo
  -> PriceService#unFreeze / forceUnFreeze
```

现状限制：

- `order.discountInfo` 是一个优惠 JSON，不是列表。
- `PriceService#freeze/use/unFreeze` 每次只构造一个 `DiscountContext`。
- `MarketingService#freeze/use/unFreeze` 每次只处理一个 `PromotionChannel`。
- 盲盒 SKU 如果命中“单品直降 + 身份优惠 + 平台券”，当前订单侧只能冻结其中一个。

---

## 4. 盲盒 SKU 折扣叠加目标

### 4.1 业务目标

盲盒售卖 SKU 需要支持如下叠加：

```text
盲盒 SKU 基础价 / 单品活动
  + 盲盒玩法专项优惠
  + 用户身份优惠
  + 平台券 / 活动券
```

示例：

```text
盲盒十连抽 SKU 原价: 299.00
单品活动: 279.00
用户身份折扣: 9 折
平台券: 立减 20.00

最终:
299.00 -> 279.00 -> 251.10 -> 231.10
```

### 4.2 兼容目标

普通商品保持现状：

```text
sale_mode = NORMAL
PricingFlowManager#executeSkuPricingFlow
BestDiscountSelector#selectBest
返回单一 SkuPricingDiscountResult
```

盲盒商品启用叠加：

```text
sale_mode = BLIND_BOX
PricingFlowManager#executeSkuStackPricingFlow
StackDiscountSelector#selectStack
返回带 discountStackItems 的 SkuPricingDiscountResult
```

旧字段继续回填：

```text
discountAmount = 所有叠加优惠总金额
discountedPrice = 最终商品应付金额，不含运费
activityId/channel/couponId/discountId = primaryDiscountItem 的字段
```

---

## 5. 总体方案

### 5.1 一句话方案

在现有 SKU 算价候选活动匹配之后，针对盲盒 SKU 不再直接选一个最优活动，而是按 `activity_group` 和叠加规则分组，每组内选最优，组间按配置顺序逐层计算，最后返回总优惠和优惠明细。

### 5.2 新增核心组件

建议新增以下非 `ikj` 组件：

```text
business/pricing/stack
  ├─ PricingStackPolicyService
  ├─ StackDiscountSelector
  ├─ DiscountStackCalculator
  ├─ DiscountStackResultBuilder
  └─ dto
      ├─ PricingStackPolicy
      ├─ PricingStackRule
      ├─ DiscountStackItemDto
      └─ DiscountStackResultDto
```

职责：

| 组件 | 职责 |
| --- | --- |
| `PricingStackPolicyService` | 查询盲盒 SKU 的叠加规则 |
| `StackDiscountSelector` | 候选活动分组、组内选最优、组间排序 |
| `DiscountStackCalculator` | 按当前金额逐层计算折扣 |
| `DiscountStackResultBuilder` | 构造 `SkuPricingDiscountResult` 兼容结果 |

### 5.3 新旧链路分流

推荐由 `PricingService#calculateSkuPricing` 在查到 `Product` 后根据 `saleMode` 分流：

```java
if (ProductSaleModeEnum.BLIND_BOX.is(product.getSaleMode())) {
    Map<String, DiscountStackResultDto> stackResultMap =
            pricingFlowManager.executeSkuStackPricingFlow(context, request);
    return pricingResultBuilder.buildPricingSKUStackResponse(skus, stackResultMap);
}

Map<String, PromotionActivitySkuWrapper> skuActivityMap =
        pricingFlowManager.executeSkuPricingFlow(context, request);
return pricingResultBuilder.buildPricingSKUResponse(skus, skuActivityMap);
```

这里不建议通过 `productType` 分流，因为盲盒是售卖玩法，不是商品业务类型。

---

## 6. 叠加规则设计

### 6.1 活动分组语义

当前 `PromotionActivity.activityGroup` 已有：

| group | 当前语义 | 叠加语义 |
| --- | --- | --- |
| 1 | 用户身份组 | 用户身份层 |
| 2 | 平台活动组 | 券/平台活动层 |
| 3 | 单品级组 | 商品/SKU 基础活动层 |

建议补充：

| group | 建议语义 | 说明 |
| --- | --- | --- |
| 4 | 盲盒玩法组 | 只对 `sale_mode=BLIND_BOX` 生效的专项优惠 |

第一期如果不想新增 group 值，也可以把盲盒 SKU 专项优惠放到 `activity_group=3`，通过 `activity_scope=5` 精准绑定 `box_sku_id`。但这样它会和普通单品活动互斥，不能表达“普通单品价 + 盲盒玩法券”两层叠加。长期建议引入 group 4。

### 6.2 默认叠加顺序

推荐默认顺序：

| stackOrder | group | 说明 |
| --- | --- | --- |
| 10 | 3 | 单品级活动，包含 SKU 促销价、单品直降 |
| 20 | 4 | 盲盒玩法专项优惠 |
| 30 | 1 | 用户身份优惠 |
| 40 | 2 | 平台活动、优惠券 |

顺序解释：

1. 先确定商品或 SKU 的基础活动价。
2. 再应用盲盒玩法优惠。
3. 再应用用户身份权益。
4. 最后应用券类或平台活动。

### 6.3 组内选择规则

同一个 `activity_group` 内仍然互斥，只选一个。

组内复用现有 `BestDiscountSelector#selectBest`：

```text
precedence 升序
discountedPrice 升序
activityGroup 升序
```

这样可以保持现有活动优先级规则，不重新定义“组内最优”。

### 6.4 组间叠加规则

组间按 `stackOrder` 升序逐层执行：

```text
currentAmount = sku.price * quantity

for each group in stackPolicy:
    candidate = selectBest(groupCandidates)
    item = apply(candidate, currentAmount)
    currentAmount = item.afterAmount

discountAmount = originalAmount - currentAmount
```

### 6.5 互斥规则

叠加规则要支持三种互斥：

| 互斥类型 | 说明 |
| --- | --- |
| 组内互斥 | 同一 group 只选一个活动 |
| 活动独占 | 命中独占活动后，后续 group 不再叠加 |
| 渠道互斥 | 指定 `PromotionChannel` 不能和某些 group 叠加 |

示例：

```text
新人 0.01 元活动 = exclusive
命中后不再叠加平台券
```

---

## 7. 叠加策略表设计

建议新增规则表：

```sql
CREATE TABLE trade_pricing_stack_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sale_mode TINYINT NOT NULL COMMENT '售卖模式:1=普通,2=盲盒',
    product_id VARCHAR(64) DEFAULT NULL COMMENT '指定商品，空表示通用',
    sku_id VARCHAR(64) DEFAULT NULL COMMENT '指定SKU，空表示通用',
    activity_group INT NOT NULL COMMENT '活动分组',
    promotion_channel INT DEFAULT NULL COMMENT '指定渠道，空表示该组全部渠道',
    activity_type VARCHAR(64) DEFAULT NULL COMMENT '活动类型，空表示不限',
    target_user_type VARCHAR(64) DEFAULT NULL COMMENT '目标用户类型，空表示不限',
    stack_order INT NOT NULL COMMENT '叠加顺序，越小越先算',
    group_select_mode VARCHAR(32) NOT NULL DEFAULT 'BEST' COMMENT '组内选择方式:BEST',
    stackable TINYINT NOT NULL DEFAULT 1 COMMENT '是否可叠加',
    exclusive TINYINT NOT NULL DEFAULT 0 COMMENT '命中后是否停止后续叠加',
    amount_scope VARCHAR(32) NOT NULL DEFAULT 'AUTO' COMMENT '金额语义:AUTO,UNIT,LINE',
    max_discount_amount DECIMAL(12,2) DEFAULT NULL COMMENT '该层最大优惠金额',
    min_pay_amount DECIMAL(12,2) NOT NULL DEFAULT 0.01 COMMENT '商品最低应付，不含运费',
    enabled TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    closed TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    create_by VARCHAR(64) DEFAULT NULL,
    update_by VARCHAR(64) DEFAULT NULL,
    create_time VARCHAR(32) DEFAULT NULL,
    modified_time VARCHAR(32) DEFAULT NULL,
    KEY idx_sale_mode (sale_mode, enabled, closed),
    KEY idx_product_sku (product_id, sku_id),
    KEY idx_group_order (activity_group, stack_order)
) COMMENT='算价折扣叠加规则表';
```

规则匹配优先级：

```text
sku_id 精确规则
  > product_id 精确规则
  > sale_mode 通用规则
```

第一期可先不做商品和 SKU 维度配置，只初始化盲盒通用规则：

```text
sale_mode=BLIND_BOX, group=3, stack_order=10
sale_mode=BLIND_BOX, group=4, stack_order=20
sale_mode=BLIND_BOX, group=1, stack_order=30
sale_mode=BLIND_BOX, group=2, stack_order=40
```

---

## 8. 金额计算规则

### 8.1 价格口径

叠加只作用在商品金额，不直接作用在运费：

```text
originalProductAmount = sku.price * quantity
stackedProductAmount  = originalProductAmount - stackDiscountAmount
shippingFee           = ShippingFeeService 计算
payPrice              = stackedProductAmount + shippingFee
```

对应旧字段：

```text
PriceDto.productTotalPrice = originalProductAmount
PriceDto.discount          = stackDiscountAmount
PriceDto.totalPrice        = originalProductAmount + shippingFee
PriceDto.payPrice          = totalPrice - stackDiscountAmount
```

### 8.2 每层计算基数

每一层都基于上一层之后的商品应付金额：

```text
beforeAmount = currentAmount
afterAmount  = calculate(beforeAmount, selectedActivity)
discount     = beforeAmount - afterAmount
```

示例：

```text
原价: 299.00
第一层促销价: 279.00
第二层 9 折: 279.00 * 0.9 = 251.10
第三层立减 20.00: 231.10
```

不能这样算：

```text
299.00 的 9 折优惠 = 29.90
再叠加到 279.00 上
```

百分比优惠必须基于当前金额。

### 8.3 discountType 语义

| discountType | 叠加语义 |
| --- | --- |
| 1 立减 | 从当前金额扣减 |
| 2 百分比 | 当前金额乘以折扣率 |
| 3 促销价 | 只允许作为基础价层，原则上不允许在后续层使用 |
| 4 阶梯百分比 | 基于当前金额和购买数量解析折扣率 |
| 5 阶梯固定金额 | 基于当前金额扣减 |
| 6 阶梯促销价 | 只允许作为基础价层 |

发布校验建议：

```text
如果 stack_order > 10，则不允许配置 PROMOTION_PRICE / TIERED_PROMOTION_PRICE
除非该活动配置 exclusive = 1
```

原因：

- 促销价是“价格替换”语义。
- 后续层如果再出现促销价，会覆盖前面层，用户和财务都难解释。

### 8.4 固定金额的 UNIT / LINE 语义

现有 `DiscountCalculator` 对非百分比折扣默认按“每件”处理：

```text
discountValue * quantity
```

叠加时需要区分：

| amountScope | 语义 | 示例 |
| --- | --- | --- |
| UNIT | 每件减免 | SKU 每件立减 10，买 3 件减 30 |
| LINE | 当前订单行减免 | 平台券满减 20，订单行只减 20 |
| AUTO | 根据 group/channel 默认推断 | 单品组按 UNIT，券类按 LINE |

默认推断：

```text
activity_group=3 单品级组 -> UNIT
activity_group=4 盲盒玩法组 -> LINE
activity_group=1 用户身份组 -> 百分比按 LINE，固定金额按 LINE
activity_group=2 平台活动组 -> LINE
```

### 8.5 最低支付金额

叠加后商品金额不能低于规则中的 `min_pay_amount`：

```text
afterAmount = max(calculatedAmount, minPayAmount)
```

默认：

```text
minPayAmount = 0.01
```

如果有运费：

```text
payPrice = max(stackedProductAmount, 0.01) + shippingFee
```

### 8.6 精度

统一使用：

```text
BigDecimal scale = 2
RoundingMode.HALF_UP
```

每层计算后落两位小数，并把 `beforeAmount/discountAmount/afterAmount` 都记录到明细，避免前后端按不同方式重算。

---

## 9. DTO 扩展设计

### 9.1 DiscountStackItemDto

新增叠加明细 DTO：

```java
public class DiscountStackItemDto {
    private String discountLineId;
    private Integer sequence;
    private Integer activityGroup;
    private Integer stackOrder;

    private Long activityId;
    private String activityName;
    private Integer channel;
    private String channelName;
    private Long couponId;
    private String couponCode;
    private String discountId;

    private Integer discountType;
    private BigDecimal discountValue;
    private BigDecimal beforeAmount;
    private BigDecimal discountAmount;
    private BigDecimal afterAmount;

    private Boolean stackable;
    private Boolean exclusive;
    private String amountScope;

    private String bizType;
    private Integer discountOrigin;
    private String uniqueTopicId;
    private Integer enableActivityAuthorQuota;
    private Boolean expectFree;
    private String accountLevel;
}
```

字段说明：

| 字段 | 说明 |
| --- | --- |
| `discountLineId` | 订单冻结、核销、解冻的幂等行 ID |
| `sequence` | 实际执行顺序，从 1 开始 |
| `activityGroup` | 活动分组 |
| `stackOrder` | 规则配置的叠加顺序 |
| `beforeAmount` | 本层优惠前商品金额 |
| `discountAmount` | 本层优惠金额 |
| `afterAmount` | 本层优惠后商品金额 |
| `amountScope` | 固定金额按件还是按订单行 |

### 9.2 DiscountStackResultDto

新增汇总 DTO：

```java
public class DiscountStackResultDto {
    private String productId;
    private String skuId;
    private Integer quantity;
    private BigDecimal originalAmount;
    private BigDecimal finalAmount;
    private BigDecimal totalDiscountAmount;
    private Boolean stacked;
    private Long stackPolicyId;
    private DiscountStackItemDto primaryDiscountItem;
    private List<DiscountStackItemDto> items;
}
```

### 9.3 PricingDiscountResult 扩展

在现有 `PricingDiscountResult` 增加：

```java
private Boolean stacked;
private BigDecimal originalAmount;
private BigDecimal finalAmount;
private BigDecimal totalDiscountAmount;
private Long stackPolicyId;
private List<DiscountStackItemDto> discountStackItems;
private DiscountStackItemDto primaryDiscountItem;
```

兼容回填：

```text
discountAmount = totalDiscountAmount
discountedPrice = finalAmount
activityId = primaryDiscountItem.activityId
promotionChannel = primaryDiscountItem.channel
discountValue = primaryDiscountItem.discountValue
discountType = primaryDiscountItem.discountType
```

### 9.4 DiscountDto 扩展

在 `DiscountDto` 增加：

```java
private Boolean stacked;
private BigDecimal totalDiscountAmount;
private List<DiscountStackItemDto> discountStackItems;
private DiscountStackItemDto primaryDiscountItem;
```

构造函数 `DiscountDto(SkuPricingDiscountResult result)` 调整：

```text
如果 result.stacked = true:
    discountAmount = result.totalDiscountAmount
    discount = result.totalDiscountAmount
    channel/activityId/couponId/discountId = primaryDiscountItem
    discountStackItems = result.discountStackItems
否则:
    保持现有逻辑
```

### 9.5 PriceDto 扩展

在 `PriceDto` 增加：

```java
private Boolean stacked;
private List<DiscountStackItemDto> discountStackItems;
private DiscountStackItemDto primaryDiscountItem;
```

`PriceStrategy#buildPriceDto` 调整：

```text
旧字段仍按 DiscountDto 回填
新增字段从 DiscountDto 复制
discount = sum(discountStackItems.discountAmount)
payPrice = productTotalPrice + shippingFee - discount
```

### 9.6 OrderProductResponse 扩展

在 `OrderProductResponse` 增加：

```java
private Boolean stacked;
private List<DiscountStackItemDto> discountStackItems;
private DiscountStackItemDto primaryDiscountItem;
```

`PreOrderService#getOrderProductResponse` 从 `PriceDto` 复制新字段。

前端展示建议：

- 老端继续读 `discount/channel/promotionChannelInfo`。
- 新端如果 `stacked=true`，展示 `discountStackItems`。

---

## 10. 算法流程设计

### 10.1 普通商品流程

普通商品保持现状：

```text
PricingService#calculateSkuPricing
  -> PricingFlowManager#executeSkuPricingFlow
  -> ActivityMatchingService#matchSkusWithActivities
  -> BestDiscountSelector#selectBest
  -> PricingResultBuilder#buildPricingSKUResponse
```

不改变：

- 商品详情价。
- 预下单价。
- 订单冻结。
- 优惠缓存语义。
- 旧字段含义。

### 10.2 盲盒 SKU 叠加流程

新增流程：

```text
PricingService#calculateSkuPricing
  -> Product.saleMode == BLIND_BOX
  -> PricingFlowManager#executeSkuStackPricingFlow
  -> ActivityMatchingService#matchSkusWithActivities
  -> PricingStackPolicyService#getPolicy(productId, skuId)
  -> StackDiscountSelector#selectStack
  -> DiscountStackCalculator#calculate
  -> DiscountStackResultBuilder#build
  -> PricingResultBuilder#buildPricingSKUStackResponse
```

### 10.3 executeSkuStackPricingFlow

建议在 `PricingFlowManager` 新增方法：

```java
public Map<String, DiscountStackResultDto> executeSkuStackPricingFlow(
        PricingContext context,
        PricingSkuRequest request) {
    prepareActivityTagsContext(context);
    prepareActivityScopeContext(context);

    Map<String, List<PromotionActivitySkuWrapper>> matched =
            activityMatchingService.matchSkusWithActivities(context);

    return stackDiscountSelector.selectStack(context, matched);
}
```

注意：

- 不在子线程中读取 `AuthContext`。
- 继续使用 `CurrentUserIdentityDto` 透传用户信息。
- 如果 SKU 很多，可复用现有分批逻辑，但子上下文必须携带同一份 `PricingStackPolicy` 或按 SKU 批量预加载规则。

### 10.4 StackDiscountSelector 伪代码

```java
public DiscountStackResultDto selectOneSku(
        Product product,
        Sku sku,
        List<PromotionActivitySkuWrapper> candidates,
        PricingStackPolicy policy) {

    BigDecimal originalAmount = sku.getPrice().multiply(BigDecimal.valueOf(sku.getPurchaseQuantity()));
    BigDecimal currentAmount = originalAmount;
    List<DiscountStackItemDto> items = new ArrayList<>();

    Map<Integer, List<PromotionActivitySkuWrapper>> byGroup =
            groupByActivityGroup(candidates);

    for (PricingStackRule rule : policy.getRulesInOrder()) {
        List<PromotionActivitySkuWrapper> groupCandidates =
                filter(byGroup.get(rule.getActivityGroup()), rule);
        if (groupCandidates.isEmpty()) {
            continue;
        }

        PromotionActivitySkuWrapper selected =
                bestDiscountSelector.selectBest(groupCandidates);
        if (selected == null || selected.getDiscountInfo() == null) {
            continue;
        }

        DiscountStackItemDto item =
                discountStackCalculator.apply(selected, rule, currentAmount, sku.getPurchaseQuantity());
        if (item.getDiscountAmount().compareTo(BigDecimal.ZERO) <= 0) {
            continue;
        }

        items.add(item);
        currentAmount = item.getAfterAmount();

        if (Boolean.TRUE.equals(item.getExclusive())) {
            break;
        }
    }

    return buildResult(originalAmount, currentAmount, items);
}
```

### 10.5 无活动命中

无活动命中时返回：

```text
stacked = false
discountStackItems = []
discountAmount = 0
discountedPrice = sku.price * quantity
channel = NONE
```

---

## 11. 订单优惠存储设计

### 11.1 一期兼容方案：versioned discountInfo JSON

当前 `Order.discountInfo` 是字符串字段，可先兼容两种格式。

旧格式：

```json
{
  "discountId": "xxx",
  "productId": "P001",
  "skuId": "S001",
  "chanel": 6,
  "couponId": 123,
  "couponCode": "C001",
  "activityId": 10001
}
```

新格式：

```json
{
  "version": 2,
  "stacked": true,
  "productId": "P_BOX_001",
  "skuId": "S_BOX_10",
  "summary": {
    "originalAmount": 299.00,
    "finalAmount": 231.10,
    "totalDiscountAmount": 67.90,
    "primaryDiscountLineId": "DL_001"
  },
  "items": [
    {
      "discountLineId": "DL_001",
      "sequence": 1,
      "activityGroup": 3,
      "stackOrder": 10,
      "chanel": 9,
      "activityId": 10001,
      "discountType": 3,
      "discountValue": 279.00,
      "beforeAmount": 299.00,
      "discountAmount": 20.00,
      "afterAmount": 279.00
    },
    {
      "discountLineId": "DL_002",
      "sequence": 2,
      "activityGroup": 1,
      "stackOrder": 30,
      "chanel": 12,
      "activityId": 10002,
      "discountType": 2,
      "discountValue": 0.90,
      "beforeAmount": 279.00,
      "discountAmount": 27.90,
      "afterAmount": 251.10
    }
  ]
}
```

兼容判断：

```java
JSONObject discountInfo = JSONObject.parseObject(order.getDiscountInfo());
if (Integer.valueOf(2).equals(discountInfo.getInteger("version"))
        && Boolean.TRUE.equals(discountInfo.getBoolean("stacked"))) {
    priceService.freezeStack(discountInfo, order, userId);
} else {
    priceService.freeze(discountInfo, order, userId);
}
```

### 11.2 长期推荐：订单优惠明细表

长期建议新增：

```sql
CREATE TABLE trade_order_discount_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id VARCHAR(64) NOT NULL,
    order_item_id VARCHAR(64) DEFAULT NULL,
    discount_line_id VARCHAR(64) NOT NULL,
    sequence_no INT NOT NULL,
    product_id VARCHAR(64) NOT NULL,
    sku_id VARCHAR(64) NOT NULL,
    activity_group INT DEFAULT NULL,
    stack_order INT DEFAULT NULL,
    channel INT DEFAULT NULL,
    activity_id BIGINT DEFAULT NULL,
    coupon_id BIGINT DEFAULT NULL,
    coupon_code VARCHAR(128) DEFAULT NULL,
    discount_id VARCHAR(128) DEFAULT NULL,
    discount_type INT DEFAULT NULL,
    discount_value DECIMAL(12,2) DEFAULT NULL,
    before_amount DECIMAL(12,2) NOT NULL,
    discount_amount DECIMAL(12,2) NOT NULL,
    after_amount DECIMAL(12,2) NOT NULL,
    freeze_status TINYINT NOT NULL DEFAULT 0 COMMENT '0=未冻结,1=已冻结,2=冻结失败,3=无需冻结',
    use_status TINYINT NOT NULL DEFAULT 0 COMMENT '0=未核销,1=已核销,2=核销失败,3=无需核销',
    unfreeze_status TINYINT NOT NULL DEFAULT 0 COMMENT '0=未解冻,1=已解冻,2=解冻失败,3=无需解冻',
    ext JSON DEFAULT NULL,
    closed TINYINT NOT NULL DEFAULT 0,
    create_time VARCHAR(32) DEFAULT NULL,
    modified_time VARCHAR(32) DEFAULT NULL,
    UNIQUE KEY uk_order_line (order_id, discount_line_id),
    KEY idx_order_id (order_id),
    KEY idx_discount_id (discount_id),
    KEY idx_activity_id (activity_id),
    KEY idx_coupon_code (coupon_code)
) COMMENT='订单优惠明细表';
```

一期可以先落 `discountInfo` V2 JSON，等盲盒订单量上来后再切明细表。但如果要做稳定的失败重试、运营对账、售后追溯，明细表应尽早落地。

---

## 12. 冻结、核销、解冻设计

### 12.1 PriceService 新增 stack 方法

建议新增：

```java
public List<DiscountContext> freezeStack(JSONObject discountInfo, Order order, String userId)
public void useStack(JSONObject discountInfo, Order order, String userId)
public void unFreezeStack(JSONObject discountInfo, Order order, String userId)
public void forceUnFreezeStack(JSONObject discountInfo, Order order, String userId)
```

旧方法保留：

```java
freeze(JSONObject, Order, String)
use(JSONObject, Order, String)
unFreeze(JSONObject, Order, String)
forceUnFreeze(JSONObject, Order, String)
```

### 12.2 freezeStack

冻结顺序：

```text
按 discountStackItems.sequence 升序冻结
```

失败补偿：

```text
如果第 N 层冻结失败:
    对 1..N-1 已冻结层按 reverse sequence 调用 unFreeze
    订单创建失败
```

伪代码：

```java
List<DiscountContext> frozen = new ArrayList<>();
for (DiscountStackItemDto item : items) {
    DiscountContext ctx = buildDiscountContext(item, order, userId);
    if (ctx.channel == null || ctx.channel == NONE) {
        continue;
    }
    try {
        marketingService.freeze(ctx);
        frozen.add(ctx);
    } catch (Exception e) {
        reverseUnfreeze(frozen, order, userId);
        throw e;
    }
}
```

### 12.3 useStack

支付成功后核销：

```text
按 sequence 升序核销
```

要求：

- 单个优惠行幂等。
- 已核销再次核销应返回成功。
- 某一层核销失败要报警并进入重试，不应影响支付成功事实。

### 12.4 unFreezeStack

订单关闭或售后需要解冻：

```text
按 sequence 倒序解冻
```

原因：

- 后算的券类通常依赖前面的价格结果。
- 反向释放更符合资源栈语义。

对 `PromotionChannel.needRefundAfterRefund` 的兼容：

- 每个 item 单独判断是否需要退权益。
- 不需要退权益的 item 写入 `CouponsRefundRecord` 或优惠明细状态为 `NOT_REFERENCE`。
- 需要退权益的 item 调用对应 `MarketingService#unFreeze`。

### 12.5 DiscountContext 构造

新增从 `DiscountStackItemDto` 构造：

```java
private DiscountContext buildDiscountContext(DiscountStackItemDto item, Order order, String userId) {
    DiscountContext ctx = new DiscountContext();
    ctx.setUserId(userId);
    ctx.setDiscountId(item.getDiscountId());
    ctx.setChannel(item.getChannel());
    ctx.setProductId(item.getProductId());
    ctx.setSkuId(item.getSkuId());
    ctx.setOrder(order);
    ctx.setCouponId(item.getCouponId());
    ctx.setCouponCode(item.getCouponCode());
    ctx.setActivityId(item.getActivityId());
    ctx.setUniqueTopicId(item.getUniqueTopicId());
    ctx.setEnableActivityAuthorQuota(item.getEnableActivityAuthorQuota());
    ctx.setExpectFree(item.getExpectFree());
    ctx.setAccountLevel(item.getAccountLevel());
    return ctx;
}
```

兼容当前 JSON 里字段名拼写：

```text
旧字段使用 "chanel"
新字段建议使用 "channel"
解析时两个都兼容
```

---

## 13. 预下单价格快照设计

### 13.1 当前问题

当前 `PreOrderService#getOrderProductResponse`：

```text
redisService.setStr(preOrderId, skuDto.getSkuId(), 12, TimeUnit.HOURS)
```

只缓存 `skuId` 不够。

盲盒 SKU 需要快照：

- `productId`
- `skuId`
- `quantity`
- `saleMode`
- `poolId`
- `poolVersion`
- `productTotalPrice`
- `shippingFee`
- `discountAmount`
- `payPrice`
- `discountStackItems`
- `primaryDiscountItem`
- 算价时间和过期时间

### 13.2 新增 PreOrderPricingSnapshotDto

```java
public class PreOrderPricingSnapshotDto {
    private String preOrderId;
    private String userId;
    private String productId;
    private String skuId;
    private Integer quantity;
    private Integer saleMode;

    private String poolId;
    private Integer poolVersion;
    private String blindBoxSnapshotId;

    private BigDecimal productTotalPrice;
    private BigDecimal shippingFee;
    private BigDecimal discountAmount;
    private BigDecimal payPrice;

    private Boolean stacked;
    private List<DiscountStackItemDto> discountStackItems;
    private DiscountStackItemDto primaryDiscountItem;

    private String pricingVersion;
    private Long expireAt;
}
```

Redis key：

```text
PRE_ORDER_PRICING:{preOrderId}
```

TTL 建议：

```text
30 分钟到 2 小时
```

盲盒价格和奖池敏感，不建议继续使用 12 小时。

### 13.3 创建订单校验

创建订单时：

1. 根据 token 读取 `PreOrderPricingSnapshotDto`。
2. 校验 `userId/productId/skuId/quantity` 一致。
3. 重新算价。
4. 对比以下字段必须完全一致：
   - `productTotalPrice`
   - `shippingFee`
   - `discountAmount`
   - `payPrice`
   - `discountStackItems.activityId/channel/couponId/discountAmount`
5. 如果不一致，提示用户刷新订单。
6. 订单写入 V2 `discountInfo` 或优惠明细。

说明：

- 不能只信任快照，因为优惠券状态、身份、库存可能变化。
- 不能只重新算价而丢弃快照，因为用户看到的是快照价。
- 推荐“重新算价 + 严格对比 + 不一致阻断”。

---

## 14. 接口兼容

### 14.1 PricingSkuRequest

可选增加：

```java
private String pricingMode; // BEST_ONLY, GROUP_STACK
```

默认：

```text
null -> 服务端根据 product.saleMode 自动判断
```

不建议让外部调用方强行传 `GROUP_STACK` 绕过商品配置。

### 14.2 PricingSkuResponse

`SkuPricingDiscountResult` 继承 `PricingDiscountResult`，扩展字段后响应自然带出：

```json
{
  "skuId": "S_BOX_10",
  "stacked": true,
  "discountAmount": 67.90,
  "discountedPrice": 231.10,
  "discountStackItems": []
}
```

### 14.3 OrderProductResponse

旧端：

```json
{
  "discount": 67.90,
  "channel": 6,
  "payPrice": 231.10
}
```

新端：

```json
{
  "stacked": true,
  "discount": 67.90,
  "payPrice": 231.10,
  "discountStackItems": [
    {
      "sequence": 1,
      "activityName": "盲盒十连抽活动价",
      "discountAmount": 20.00
    },
    {
      "sequence": 2,
      "activityName": "社区作者 9 折",
      "discountAmount": 27.90
    },
    {
      "sequence": 3,
      "activityName": "平台券",
      "discountAmount": 20.00
    }
  ]
}
```

---

## 15. 缓存设计

### 15.1 商品列表算价缓存

当前 `PricingService#calculatePricing` 会缓存商品维度结果：

```text
RedisKey.Pricing.PRODUCT_PRICING_CACHE
```

盲盒 SKU 叠加主要发生在 SKU 维度详情页和预下单。第一期建议：

- 列表页仍展示商品最低价或主 SKU 价格。
- 不在列表页展示完整叠加明细。
- 盲盒商品列表缓存只缓存“展示价”，不参与订单价格冻结。

### 15.2 SKU 叠加结果缓存

第一期不建议缓存 SKU 叠加结果。

原因：

- 用户身份和券状态变化快。
- 盲盒奖池版本可能变化。
- 订单创建前必须严格校验。

如果后续需要缓存，key 必须包含：

```text
userId
productId
skuId
quantity
saleMode
poolId
poolVersion
promotionId
couponCode
pricingRuleVersion
```

---

## 16. 发布校验

### 16.1 活动配置校验

盲盒叠加活动发布时校验：

1. `activity_group` 必填。
2. `sale_mode=BLIND_BOX` 的专项优惠必须绑定商品或 SKU。
3. `PROMOTION_PRICE` 只能处于基础价层，或设置 `exclusive=1`。
4. 同一 SKU 同一 group 可有多个候选，但必须通过 `precedence` 明确优先级。
5. 券类活动如果是固定金额，默认 `amount_scope=LINE`。
6. 叠加后金额不能低于 `min_pay_amount`。

### 16.2 盲盒 SKU 校验

参与叠加的 SKU 必须满足：

1. `Product.saleMode = BLIND_BOX`。
2. `Sku` 是盲盒售卖 SKU，不是盒内结果 SKU。
3. 盲盒 SKU 扩展表存在有效记录。
4. 当前奖池版本在线。
5. `drawCount > 0`。

### 16.3 订单校验

订单创建校验：

1. 预下单快照未过期。
2. 快照中的 `productId/skuId/quantity` 与请求一致。
3. 重新算价结果与快照一致。
4. 冻结所有叠加优惠成功。
5. 写入 `discountInfo` V2 或优惠明细成功。

---

## 17. 异常和降级

### 17.1 叠加规则缺失

如果 `sale_mode=BLIND_BOX` 但找不到叠加规则：

第一期建议直接降级为单一最优优惠，并报警：

```text
fallback to BEST_ONLY
alarm: blind box stack policy missing
```

不建议直接报错阻断，因为运营漏配规则会影响交易。

### 17.2 某层计算失败

如果某个候选活动计算失败：

```text
跳过该候选
继续计算同组其他候选
同组全部失败则跳过该组
记录 warn 日志和监控
```

### 17.3 冻结失败

冻结失败不能降级。

原因：

- 用户支付价已经包含该优惠。
- 如果优惠没有冻结成功，后续核销和权益状态会不一致。

处理：

```text
冻结失败 -> 补偿解冻已冻结优惠 -> 订单创建失败 -> 提示刷新重试
```

---

## 18. 监控指标

建议新增：

| 指标 | 说明 |
| --- | --- |
| `pricing.blind_box.stack.hit` | 盲盒叠加算价次数 |
| `pricing.blind_box.stack.fallback` | 降级单一最优次数 |
| `pricing.blind_box.stack.item_count` | 每次叠加层数 |
| `pricing.blind_box.stack.amount_mismatch` | 预下单和下单重算不一致次数 |
| `order.discount_stack.freeze_fail` | 叠加优惠冻结失败 |
| `order.discount_stack.use_fail` | 叠加优惠核销失败 |
| `order.discount_stack.unfreeze_fail` | 叠加优惠解冻失败 |

关键报警：

```text
payPrice < 0.01
discountAmount > productTotalPrice
discountInfo V2 parse failed
freezeStack partial failure
preOrder pricing snapshot mismatch
```

---

## 19. 测试用例

### 19.1 普通商品兼容

| 用例 | 期望 |
| --- | --- |
| 普通 SHOP 商品命中多个活动 | 仍只返回一个最优活动 |
| 普通 TEMPLATE 商品预下单 | `OrderProductResponse` 旧字段不变 |
| 普通 WANT 商品作者优惠 | 原冻结、核销、解冻链路不变 |

### 19.2 盲盒 SKU 叠加

| 用例 | 期望 |
| --- | --- |
| 盲盒 SKU 命中 group 3 + group 1 + group 2 | 返回 3 条 `discountStackItems` |
| 同组命中多个活动 | 组内只选最优 |
| group 3 促销价 + group 1 百分比 | 百分比基于促销价后金额计算 |
| group 2 固定券 | 按 LINE 只减一次 |
| 叠加后低于 0.01 | 最终商品金额等于 0.01 |
| 命中 exclusive 活动 | 后续 group 不再叠加 |

### 19.3 订单生命周期

| 用例 | 期望 |
| --- | --- |
| 叠加优惠全部冻结成功 | 订单创建成功，`discountInfo.version=2` |
| 第 2 层冻结失败 | 第 1 层反向解冻，订单创建失败 |
| 支付成功 | 所有已冻结优惠逐条核销 |
| 用户取消 | 所有已冻结优惠逐条解冻 |
| 某层不需要退权益 | 跳过策略解冻并记录状态 |

### 19.4 预下单快照

| 用例 | 期望 |
| --- | --- |
| 快照未过期且重算一致 | 允许创建订单 |
| 优惠券被使用导致重算不一致 | 阻断创建，提示刷新 |
| 奖池版本变化 | 阻断创建，提示刷新 |
| quantity 被篡改 | 阻断创建 |

---

## 20. 落地步骤

### 20.1 第一期

1. `Product` 增加 `saleMode/fulfillMode` 字段，历史默认普通售卖。
2. 新增 `DiscountStackItemDto/DiscountStackResultDto`。
3. 扩展 `PricingDiscountResult/SkuPricingDiscountResult/DiscountDto/PriceDto/OrderProductResponse`。
4. 新增 `trade_pricing_stack_rule`。
5. 新增 `StackDiscountSelector/DiscountStackCalculator/PricingStackPolicyService`。
6. `PricingService#calculateSkuPricing` 按 `saleMode` 分流。
7. `Order.discountInfo` 支持 V2 JSON。
8. `PriceService/OrderService` 增加 stack 冻结、核销、解冻。
9. 预下单 Redis 从 `preOrderId -> skuId` 升级为价格快照。

### 20.2 第二期

1. 新增 `trade_order_discount_detail`。
2. 优惠冻结、核销、解冻按明细表做幂等状态机。
3. 接入运营对账和售后查询。
4. 支持按 SKU、商品维度定制叠加规则。
5. 支持前端完整展示叠加优惠明细。

### 20.3 灰度策略

建议增加 Nacos 开关：

```text
pricing.blindBoxStack.enabled = false
pricing.blindBoxStack.productWhitelist = []
pricing.blindBoxStack.dryRun = true
```

灰度流程：

1. `dryRun=true` 时同时计算单一最优和叠加结果，只记录差异不返回叠加价。
2. 白名单商品开启真实叠加。
3. 观察冻结失败率、下单价格不一致率。
4. 扩大到全量盲盒商品。

---

## 21. 关键结论

1. 普通商品不改语义，继续“单一最优优惠”。
2. 盲盒 SKU 通过 `saleMode=BLIND_BOX` 开启“分组叠加”。
3. `activity_group` 从 tie-breaker 升级为叠加分组依据。
4. 每组内部仍复用 `BestDiscountSelector`，降低对现有促销优先级的影响。
5. 每组之间按 `trade_pricing_stack_rule.stack_order` 逐层计算。
6. 订单侧必须支持多条优惠冻结、核销、解冻，否则价格和权益状态会不一致。
7. 预下单必须保存价格快照，盲盒还要绑定奖池版本和叠加优惠明细。
8. 一期可用 V2 `discountInfo` JSON 快速兼容，长期应落 `trade_order_discount_detail`。
