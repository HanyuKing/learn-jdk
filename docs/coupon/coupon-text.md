# 优惠券话术仅回填 `productDetail`

## Summary

优惠券逻辑不再设置 `promotionChannelInfo.productDetails`。优惠券文案只回填 `productDetail`，继续复用现有券标签格式和历史促销话术。

## Implementation Changes

- 删除 `ProductDisplayPricingService.appendCouponProductDetails` 对 `productDetails` 的写入，保留已有历史 `productDetails` 配置，不主动清空或覆盖。
- 优惠券渠道 `promotionChannel=6`：
    - 有已达门槛券时，`productDetail` 使用实际已达门槛券的格式化文案；
    - 没有已达门槛券时，使用 `promotionLabelList` 首条券标签文案；
    - 文案格式继续沿用当前拼接逻辑，例如“新人券 满100减20”。
- 单品直降或其他历史促销渠道：
    - 继续使用老渠道、老 `productDetailType` 和老 `productDetail`；
    - 优惠券不覆盖原促销话术；
    - 仅按现有兼容逻辑补充渠道 6 所需的历史图片字段。
- 用户只有券但未达到门槛时：
    - `promotionChannelInfo` 保留默认空值结构；
    - 不填渠道、图片、类型和 `productDetail`；
    - 价格不扣券；
    - `promotionLabelList` 仍可保留未达门槛券标签。
- 无优惠时维持现有返回行为。
- 商品详情、商城列表、搜索、推荐、购物车、价格、checkout、submit 及订单商品响应统一遵循上述规则。

## Tests

- 纯券且已达门槛：
    - 返回渠道 6；
    - `productDetail` 为已达门槛券文案；
    - `productDetails` 不由优惠券逻辑设置。
- 纯券且未达门槛：
    - 返回默认空值对象；
    - `productDetail` 为空；
    - 价格不扣券；
    - 标签仍可展示券文案。
- 多张可用券：
    - `productDetail` 使用当前实际择优选中的券文案。
- 无可用券但存在未达门槛券：
    - `productDetail` 使用 `promotionLabelList` 首条券文案。
- 直降叠券：
    - 保留渠道 9 的历史 `productDetail`、`productDetailType` 和促销话术；
    - 不写入优惠券 `productDetails`。
- 历史渠道本身带有 `productDetails` 时，确认原值保持不变。
- 回归商品详情、购物车、价格计算及订单链路测试，并执行 API 构建和 `git diff --check`。

## Assumptions

- `productDetails` 仍作为历史促销字段保留，只有优惠券逻辑停止写入。
- “已达门槛券文案”取算价引擎最终选中的优惠券，而不是所有可用券。
- 未达门槛场景沿用现有标签生成逻辑，不新增“还差多少钱”等文案。
