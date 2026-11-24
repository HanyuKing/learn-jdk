# Stripe 电商 Demo

用 Python Flask、SQLite 和 Stripe 沙箱体验选商品、购物车、优惠码、配送、算税、下单和支付。默认使用「Stripe Tax · 广州发货」，支持送往美国或日本，以美元或日元支付。「税费场景」里也可以选择常规支付和 Stripe 手动税率。 [Stripe 沙箱与测试卡](https://docs.stripe.com/testing)

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

打开 [本地收银台](http://127.0.0.1:4242)，按下面的步骤走完一笔订单。测试卡用 `4242 4242 4242 4242`、任意未来有效期和三位 CVC。需要银行验证或站外授权的支付方式会按 Stripe 流程完成验证并返回收银台。 [Stripe 测试卡](https://docs.stripe.com/testing#cards)

## 完整电商流程

1. **选商品。** 首页默认选中「Stripe Tax · 广州发货」。把定制树脂手办、挂件或桌面摆件加入购物车，可以更改数量、移除商品，并切换 USD 或 JPY 查看对应价格。购物车在同一标签页刷新后可恢复。
2. **填写收货和配送信息。** 点击「填写收货地址」，在 Stripe 地址组件中选择美国或日本。配送方式包括标准配送（US$5 或 ¥750）和免运费配送，美元订单可另选 **US$20 加急配送**。美国测试地址可用 `200 N Spring St, Los Angeles, CA 90012`，日本可用 `東京都新宿区西新宿2-8-1, 163-8001`。
3. **输入优惠码并试算。** 商品自动享受 9 折，输入 `PROMO_EXAMPLE` 每单再减 **US$5** 或 **¥750**。点「计算税费」，核对商品明细、折扣、运费、税率和含税应付。改商品、数量、地址、配送或优惠码后，重新试算。
4. **提交订单。** 点「提交订单」后，后端重新读取目录价格，验证数量和优惠码，重新计算税费，并把商品、配送选项、金额和试算记录保存到 SQLite。页面显示订单号与「待支付」。
5. **支付。** 点「去付款」，后端从订单读取含税金额，创建或复用 PaymentIntent。同一个订单号可在同一标签页刷新后继续付款。卡信息由 Stripe Payment Element 收集。
6. **确认结果。** 成功后页面显示「已支付」、确认来源和 Stripe 支付意图。后端通过验签的 Webhook 或主动查单核对币种和实收金额。有结账税额的订单还会登记 Stripe 税务交易。可用 `4000 0000 0000 0002` 验证拒付后仍可用同一订单重试。 [Stripe 测试卡](https://docs.stripe.com/testing#cards)

## Stripe 手动税率

1. 在与 `.env` 密钥对应的 **Stripe 沙箱**中打开税率页面，新建两个**价外税率（Exclusive）**。美国例子填加州基础销售税 **7.25%**，日本例子填消费税 **10%**。也可用 Tax Rates API 创建。 [Stripe 沙箱税率](https://dashboard.stripe.com/test/tax-rates) · [Stripe 创建手动税率](https://docs.stripe.com/tax/tax-rates) · [加州基础税率](https://cdtfa.ca.gov/taxes-and-fees/sut-rates-description.htm) · [日本标准消费税率](https://www.nta.go.jp/taxes/shiraberu/taxanswer/shohi/6303.htm)
2. 把美国的 `txr_...` 填入 `STRIPE_US_TAX_RATE_ID`，日本的填入 `STRIPE_JP_TAX_RATE_ID`，再重启服务。后端按币种选择对应的 ID，并将它传给 Checkout 商品行的 `tax_rates`。 [本地配置模板](.env.example) · [Checkout 手动税率](https://docs.stripe.com/payments/checkout/use-manual-tax-rates)
3. 在收银台选「Stripe 手动税率」并创建订单。在出现的地址组件中，USD 用美国加州测试地址，JPY 用日本测试地址。填写邮箱，核对税费，然后在本页用测试卡付款。固定税率由选中的 `txr_...` 决定。

| 税费场景 | 商品小计 | 税率 | 税额 | 应付金额 | Stripe `amount` |
| --- | ---: | ---: | ---: | ---: | ---: |
| 美国 · 加州基础销售税 | $12.50 | 7.25% | $0.91 | $13.41 | `1341` |
| 日本 · 标准消费税 | ¥1,500 | 10% | ¥150 | ¥1,650 | `1650` |

## Stripe Tax · 广州发货

在同一个 Stripe 沙箱完成 **Tax → 设置**，再到 **Tax → 登记**为要测试的地区添加立即生效的测试登记。Stripe Tax 只为远程销售场景支持中国大陆，因此 Demo 通过每笔试算的 `ship_from_details` 指定广州发货。Stripe 根据发货地、收货地、商品和适用的税务登记算税。 [Stripe Tax 沙箱设置](https://docs.stripe.com/tax/testing) · [Stripe 发货地址](https://docs.stripe.com/tax/ship-from-address) · [Stripe 零税额说明](https://docs.stripe.com/tax/zero-tax)

| 测试收货地 | 商品原价 | 活动优惠 | 优惠码减免 | 折后商品 | 运费 | 税前小计 | 结账税 | 应付 |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| 美国洛杉矶 | $100.00 | −$10.00 | −$5.00 | $85.00 | $5.00 | $90.00 | $8.71 | $98.71 |
| 日本东京 | ¥15,000 | −¥1,500 | −¥750 | ¥12,750 | ¥750 | ¥13,500 | ¥0 | ¥13,500 |

表中购物车内有一件「定制树脂手办」，使用标准配送和示例优惠码，金额来自 Stripe 沙箱。页面的「税费明细」分别显示商品税和运费税。实际结果随收货地址、税务登记和地区规则变化。收银台也会提示清关时可能产生关税和进口税。

试算调用 `POST /v1/tax/calculations`。请求把折后商品作为单条 `merchandise`，传 `tax_code=txcd_99999999` 和 `tax_behavior=exclusive`。正数运费放到 `shipping_cost`，同样使用价外税。Stripe 收货地址使用 `address_source=shipping`。每笔请求都带广州发货地址 `CN`、`44`、`Guangzhou`、`510000` 和 `Guangzhou fulfillment center`。 [项目算税说明](Stripe%20国际支付入门.md#结算时怎样算税) · [Stripe 试算 API](https://docs.stripe.com/api/tax/calculations/create)

预览复用 90 秒内的相同成功试算，Stripe Tax 的读取超时为 2 秒。失败时页面显示试算失败状态，报价沿用税前金额。接口从 `tax_amount_exclusive` 读取订单税额，从商品行和运费各自的 `tax_breakdown` 读取税率，并直接显示 Stripe 返回的 `amount_tax`。示例的商品价、活动、优惠码和配送费由 Demo 后端预设。 [Stripe 试算响应](https://docs.stripe.com/api/tax/calculations/object)

## 商品税率与运费税率示例

以下金额来自 2026 年 11 月的沙箱试算。

| Stripe 沙箱试算 | 商品税率和税额 | 运费税率和税额 | 总税费 | 含税应付 |
| --- | ---: | ---: | ---: | ---: |
| 广州发往洛杉矶 · 运费免税 | 10.25% · US$8.71 | 0% · US$0.00 | US$8.71 | US$98.71 |
| 广州发往纽约 · 商品与运费同税率 | 8.875% · US$7.55 | 8.875% · US$0.44 | US$7.99 | US$97.99 |
| 日本境内大米和单独收费的配送服务 · 两项不同且都计税 | 8% · ¥160 | 10% · ¥50 | ¥210 | ¥2,710 |

美国商品与运费各自适用正税率的计算示例见 [学习问答 · 奥罗拉市的 8% 和 3.75%](Stripe%20国际支付入门.md#美国有没有商品和运费都收税税率又不同的例子)。

### 在收银台复现洛杉矶和纽约

在沙箱中启用加州和纽约州的测试税务登记。收银台选择 USD 和「Stripe Tax · 广州发货」，加一件「定制树脂手办」，选择标准配送，输入 `PROMO_EXAMPLE` 后试算。折后商品为 **US$85.00**，运费为 **US$5.00**。 [Stripe 沙箱税务登记](https://docs.stripe.com/tax/testing)

- 洛杉矶收货地址用 `200 N Spring St, Los Angeles, CA 90012`。页面分别显示商品税率 **10.25%** 和运费 **0%（免税）**。 [洛杉矶收货地址](https://cityclerk.lacity.org/) · [加州运费税务规则](https://cdtfa.ca.gov/formspubs/pub100/applying-sales-tax.htm)
- 纽约收货地址用 `1 Centre St, New York, NY 10007`。页面合并显示 **8.875%**，商品税额和运费税额各占一行。 [纽约收货地址](https://www.nyc.gov/site/dcas/business/dcasmanagedbuildings/david-n-dinkins-manhattan-municipal-building.page) · [纽约州运费税务规则](https://www.tax.ny.gov/pubs_and_bulls/tg_bulletins/st/shipping_and_delivery_charges.htm)

### 用 Stripe Tax API 复现日本境内的 8% 和 10%

日本境内销售的大米适用食品优惠税率。单独计价的国内配送服务适用标准税率。这笔试算将 **¥2,000 的大米**和 **¥500 的独立配送服务**作为两条明细提交。用 `shipping_cost` 提交本例的随商品运费时，实测 Stripe 返回了 **8%**。 [日本国税厅消费税率](https://www.nta.go.jp/taxes/shiraberu/taxanswer/shohi/6102.htm) · [日本国税厅运费问答第 39 问](https://www.nta.go.jp/taxes/shiraberu/zeimokubetsu/shohi/keigenzeiritsu/pdf/qa/03-01.pdf)

使用设有日本测试营业地址和有效日本税务登记的沙箱，在 `stripe-payment` 目录运行下列命令。两笔费用都按价外税试算，`txcd_40010002` 代表大米，`txcd_20030000` 代表一般服务。 [Stripe 税码表](https://docs.stripe.com/tax/tax-codes)

```bash
.venv/bin/python - <<'PY'
import os

import stripe
from dotenv import load_dotenv

from app import tax_component

load_dotenv(".env")
key = os.environ["STRIPE_SECRET_KEY"]
if not key.startswith(("sk_test_", "rk_test_", "rkcs_test_")):
    raise SystemExit("请使用 Stripe 沙箱密钥")
client = stripe.StripeClient(key)
calculation = client.v1.tax.calculations.create({
    "currency": "jpy",
    "customer_details": {"address_source": "shipping", "address": {
        "country": "JP", "state": "13", "city": "新宿区",
        "line1": "西新宿2-8-1", "postal_code": "163-8001",
    }},
    "ship_from_details": {"address": {
        "country": "JP", "state": "13", "city": "千代田区",
        "line1": "丸の内1-9-1", "postal_code": "100-0005",
    }},
    "line_items": [
        {"reference": "rice", "amount": 2000, "tax_code": "txcd_40010002", "tax_behavior": "exclusive"},
        {"reference": "delivery", "amount": 500, "tax_code": "txcd_20030000", "tax_behavior": "exclusive"},
    ],
    "expand": ["line_items", "line_items.data.tax_breakdown"],
})
for item in calculation.to_dict()["line_items"]["data"]:
    tax = tax_component(item, 1)
    print(f"{item['reference']} {tax['rate']:.0%} · ¥{tax['amount']:.0f}")
print(f"总税费 ¥{calculation.tax_amount_exclusive}，应付 ¥{calculation.amount_total}")
PY
```

示例输出为 `rice 8% · ¥160`、`delivery 10% · ¥50` 和 `总税费 ¥210，应付 ¥2710`。

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
| 商品与购物车 | `GET /api/config` 返回商品 SKU、各币种标价和数量上限。浏览器购物车保存 SKU 和数量 |
| 试算税费 | `POST /api/tax/quote` 接收币种、`items`、收货地址、`shippingMethodCode` 和 `promotionCode`，返回商品及结算明细 |
| 创建订单 | `PUT /api/orders/<id>` 提交试算输入与税费场景。带 `items` 的订单先保存商品、正式试算 ID 和应付金额，同一 UUID 可重试 |
| 发起支付 | `POST /api/orders/<id>/payment` 从已创建订单读取应付金额，创建或复用 `PaymentIntent` |
| 确认支付 | 手动税率使用 Checkout Elements。广州发货和常规支付使用 Payment Element，并调用 `stripe.confirmPayment` |
| 接收通知 | `POST /api/stripe/webhook` 验签。Checkout 订单核对会话 ID、订单号、币种、小计、税额、总额及付款状态。PaymentIntent 路径核对订单和实收金额 |
| 同步状态 | `GET /api/orders/<id>` 读本地状态。`POST /api/orders/<id>/sync` 主动查询 Stripe，并保存付款结果和实际税额 |

服务默认监听 `127.0.0.1`，SQLite 默认写入同目录的 `orders.sqlite3`。这是本地练习，浏览器用随机 UUID 识别自己的订单。接到公开业务时，应使用登录态校验订单归属，并用部署环境的密钥管理方式配置 Stripe。 [Python 后端](app.py) · [浏览器支付逻辑](frontend/checkout.js)
