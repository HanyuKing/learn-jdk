# Stripe 支付实战示例

用 Python Flask、SQLite 和 Stripe 沙箱测试支付。商品小计为 **12.50 美元**或 **1,500 日元**。带税订单在本地收银台内嵌 Stripe 的收货地址和付款组件。 [Stripe 沙箱与测试卡](https://docs.stripe.com/testing)

## 启动收银台

需要 Python 3.10 或更高版本，以及同一个 Stripe 沙箱的 `pk_test_...` 和 `sk_test_...`。可在 Stripe Dashboard 的沙箱中获取。Stripe CLI 创建的临时沙箱也可用，其服务端密钥为 `rkcs_test_...`。 [Stripe API 密钥](https://docs.stripe.com/keys)

```bash
cd stripe-payment
python3 -m venv .venv
.venv/bin/python -m pip install -r requirements.txt
test -f .env || cp .env.example .env
```

把公开密钥写入 `.env` 的 `STRIPE_PUBLISHABLE_KEY`，把服务端密钥写入 `STRIPE_SECRET_KEY`。`.env` 已被 Git 忽略。若已有初始化好的 `.env`，可以直接启动。 [本地配置模板](.env.example)

```bash
.venv/bin/python app.py
```

打开 [本地收银台](http://127.0.0.1:4242)。选择币种和税费场景，再创建订单。带税订单先填写收货地址和联系邮箱，然后在同一页核对税额并付款。测试卡用 `4242 4242 4242 4242`、任意未来有效期和三位 CVC。付款后页面显示订单状态、Stripe 支付记录和确认来源。需要银行验证或站外授权的支付方式会按 Stripe 流程完成验证并返回收银台。 [Stripe 测试卡](https://docs.stripe.com/testing#cards) · [Checkout Elements](https://docs.stripe.com/payments/quickstart-checkout-sessions)

## Stripe 手动税率

1. 在与 `.env` 密钥对应的 **Stripe 沙箱**中打开税率页面，新建两个**价外税率（Exclusive）**。美国例子填加州基础销售税 **7.25%**，日本例子填消费税 **10%**。也可用 Tax Rates API 创建。 [Stripe 沙箱税率](https://dashboard.stripe.com/test/tax-rates) · [Stripe 创建手动税率](https://docs.stripe.com/tax/tax-rates) · [加州基础税率](https://cdtfa.ca.gov/taxes-and-fees/sut-rates-description.htm) · [日本标准消费税率](https://www.nta.go.jp/taxes/shiraberu/taxanswer/shohi/6303.htm)
2. 把美国的 `txr_...` 填入 `STRIPE_US_TAX_RATE_ID`，日本的填入 `STRIPE_JP_TAX_RATE_ID`，再重启服务。后端按币种选择对应的 ID，并将它传给 Checkout 商品行的 `tax_rates`。 [本地配置模板](.env.example) · [Checkout 手动税率](https://docs.stripe.com/payments/checkout/use-manual-tax-rates)
3. 在收银台选「Stripe 手动税率」并创建订单。在出现的地址组件中，USD 用美国加州测试地址，JPY 用日本测试地址。填写邮箱，核对税费，然后在本页用测试卡付款。固定税率由选中的 `txr_...` 决定。

| 税费场景 | 商品小计 | 税率 | 税额 | 应付金额 | Stripe `amount` |
| --- | ---: | ---: | ---: | ---: | ---: |
| 美国 · 加州基础销售税 | $12.50 | 7.25% | $0.91 | $13.41 | `1341` |
| 日本 · 标准消费税 | ¥1,500 | 10% | ¥150 | ¥1,650 | `1650` |

## Stripe Tax 自动算税

1. 在同一个 Stripe 沙箱完成 **Tax → 设置**，填写测试营业地址，再到 **Tax → 登记**添加立即生效的测试登记。例如添加美国加州及日本。沙箱中的登记只用于测试。 [Stripe Tax 沙箱设置](https://docs.stripe.com/tax/testing)
2. 在收银台选择币种和「Stripe Tax 自动算税」，创建订单后填写本页的 Stripe 收货地址。USD 订单采集美国地址，JPY 订单采集日本地址。Stripe 对通用实物商品（`txcd_99999999`）重新算税后，订单栏会显示税额和应付金额。 [Checkout Elements 自动算税](https://docs.stripe.com/tax/checkout/elements) · [Stripe 产品税务分类](https://docs.stripe.com/tax/tax-codes)
3. 用已登记地区的测试地址和测试卡付款。美国可用 `200 N Spring St, Los Angeles, CA 90012`。日本可用 `東京都新宿区西新宿2-8-1, 163-8001`。若要验证日本国内销售的标准消费税，可在专用沙箱把测试营业地址设在日本，再使用日本收货地址和生效的日本登记。Stripe 按地址、商品、登记及跨境规则返回结果，未登记或适用免税时可能显示零税额。 [Stripe 零税额说明](https://docs.stripe.com/tax/zero-tax) · [日本实物销售规则](https://docs.stripe.com/tax/supported-countries/asia-pacific/collect-tax?tax-jurisdiction-asia-pacific=japan#cross-border-sales-of-goods-to-japan)
4. 付款后在收银台核对订单税额和应付金额。`amount` 是含税总额，美元用美分，日元用日元。也可在 Stripe Dashboard 按 `cs_...` 或 `pi_...` 查看对应记录。跨境关税和进口消费税按清关规则另算。 [Stripe 币种最小单位](https://docs.stripe.com/currencies)

## Stripe Tax · 广州发货

1. 选 **美元 USD** 或 **日元 JPY**，再选「Stripe Tax · 广州发货」。点「填写收货地址」，在 Stripe 组件中填写完整地址。可使用上一节的洛杉矶或东京测试地址。
2. 点「计算税费」。后端把中国广州作为本单的 `ship_from_details`，把你填写的地址作为收货地，然后用 Stripe Tax 返回的税额和含税总额创建 `PaymentIntent`。 [Stripe 发货地参数](https://docs.stripe.com/tax/ship-from-address) · [关联税费计算和支付](https://docs.stripe.com/tax/payment-intent/simplified)
3. 核对页面显示的金额，再用测试卡在本页付款。要修改地址，点「开始新订单」后重新算税。需要测试时，在沙箱中完成 Stripe Tax 设置，并添加所需的测试税务登记。

| 测试收货地 | 商品小计 | Stripe 结账税 | 应付金额 |
| --- | ---: | ---: | ---: |
| 美国洛杉矶 | $12.50 | $1.28 | $13.78 |
| 日本东京 | ¥1,500 | ¥0 | ¥1,500 |

表中的金额来自广州发货的沙箱测试。收货地址、税务登记和地区规则都会影响结果。进口关税和进口消费税按清关规则另算。

## 复现 Webhook 确认

1. 安装 Stripe CLI，在另一终端运行 `stripe login` 并选中与 `.env` 相同的沙箱。使用临时沙箱时，可用创建该沙箱的 Stripe CLI 配置。 [Stripe CLI](https://docs.stripe.com/cli/install)
2. 开启事件转发，并保持该终端运行。

   ```bash
   stripe listen --events payment_intent.succeeded,checkout.session.completed,checkout.session.async_payment_succeeded \
     --forward-to http://127.0.0.1:4242/api/stripe/webhook
   ```

3. 将 CLI 输出的 `whsec_...` 填到 `STRIPE_WEBHOOK_SECRET`，重启 Python 服务。再用 `4242` 测试卡付一笔。若回调先将订单更新成功，页面的确认来源会显示 **Stripe Webhook**。若页面查单先提交，来源会显示 **服务端查单**，两种情况都会保留首次确认的来源。 [转发与验签](https://docs.stripe.com/webhooks#local-listener) · [Checkout 完成事件](https://docs.stripe.com/checkout/fulfillment)

4. 选择「常规支付」时，日元订单显示 `amount=1500, currency=jpy`，美元订单显示 `amount=1250, currency=usd`。也可用 `4000 0025 0000 3155` 体验银行验证，用 `4000 0000 0000 0002` 验证拒付后订单保持待支付。 [验证和拒付测试卡](https://docs.stripe.com/testing)

## 前端与后端怎么配合

| 步骤 | 入口与实际行为 |
| --- | --- |
| 创建订单 | `PUT /api/orders/<id>` 接收币种和税费场景。广州发货模式（`stripe_guangzhou`）首次创建时也提交 `shipping`。后端保存 Stripe Tax 报价，并把 `taxcalc_...` 关联到 `PaymentIntent` |
| 确认支付 | 手动税率和 Checkout 自动算税使用 Checkout Elements。广州发货和常规支付使用 Payment Element，并调用 `stripe.confirmPayment` |
| 接收通知 | `POST /api/stripe/webhook` 验签。Checkout 订单核对会话 ID、订单号、币种、小计、税额、总额及付款状态。PaymentIntent 路径核对订单和实收金额 |
| 同步状态 | `GET /api/orders/<id>` 读本地状态。`POST /api/orders/<id>/sync` 主动查询 Stripe，并保存付款结果和实际税额 |

服务默认监听 `127.0.0.1`，SQLite 默认写入同目录的 `orders.sqlite3`。这是本地练习，浏览器用随机 UUID 识别自己的订单。接到公开业务时，应使用登录态校验订单归属，并用部署环境的密钥管理方式配置 Stripe。 [Python 后端](app.py) · [浏览器支付逻辑](frontend/checkout.js)
