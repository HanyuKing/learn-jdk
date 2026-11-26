# Stripe 一次性购买 Demo

使用 Flask、SQLite 和 Stripe 托管的 Checkout，购买一件在 **Stripe Product Catalog** 中配置的商品。示例商品「数字素材包」是 **US$19.90** 的一次性价格。一次付款对应一笔本地订单，可以查看 `cs_...` 结账会话、`pi_...` 支付意图和付款确认来源。

## 1. 创建一次性商品价格

1. 登录 [Stripe Dashboard](https://dashboard.stripe.com/sandboxes)，进入你要使用的沙箱。商品、价格、API 密钥和 Webhook 监听使用同一个沙箱。
2. 在 **Product catalog（产品目录）** 点击 **Add a product**，填写商品名，例如「数字素材包」。
3. 在 **Pricing** 选择 **One-off**，填写 `19.90` 和 `USD`，然后保存。`One-off` 对应 API 中的 `type=one_time`。本例收取商品定价，结账金额为一件商品的价格。 [Stripe 商品和价格](https://docs.stripe.com/products-prices/how-products-and-prices-work)
4. 在商品详情的价格列表复制 `price_...`。这个 ID 代表具体的价格，商品本身的 ID 以 `prod_` 开头。接着从 **API keys** 复制同一个沙箱的服务端密钥 `sk_test_...`。 [Stripe API 密钥](https://docs.stripe.com/keys)

## 2. 安装并填写 `.env`

从仓库根目录运行以下命令。示例使用 macOS 或 Linux 终端，要求 Python 3.10 或以上。

```bash
cd stripe-one-off
python3 -m venv .venv
.venv/bin/python -m pip install -r requirements.txt
test -f .env || cp .env.example .env
.venv/bin/python -c 'import secrets; print(secrets.token_hex(32))'
```

打开 `stripe-one-off/.env`，填写下面的项目。如果已有可用配置，保留原值即可。

| `.env` 变量 | 填什么 |
| --- | --- |
| `STRIPE_SECRET_KEY` | Stripe 沙箱的服务端密钥 |
| `STRIPE_ONE_OFF_PRICE_ID` | 刚复制的 `price_...` |
| `STRIPE_WEBHOOK_SECRET` | 下一步从 Stripe CLI 获取的 `whsec_...` |
| `FLASK_SECRET_KEY` | 安装步骤最后一条命令生成的随机字符串。保留这个值，服务重启后浏览器还能查看自己的订单 |
| `PORT` | 默认 `4244` |

`.env` 已被 Git 忽略。托管 Checkout 使用服务端密钥创建会话，浏览器通过 Stripe 页面输入测试卡。

## 3. 启动 Webhook 转发和网页

安装 Stripe CLI 后，在**终端 A**从 `stripe-one-off` 运行以下命令。macOS 可以用 `brew install stripe/stripe-cli/stripe` 安装。 [Stripe CLI 安装](https://docs.stripe.com/stripe-cli/install)

```bash
.venv/bin/dotenv run -- sh -c '
  export STRIPE_API_KEY="$STRIPE_SECRET_KEY"
  exec stripe listen \
    --events checkout.session.completed,checkout.session.async_payment_succeeded,checkout.session.async_payment_failed,checkout.session.expired \
    --forward-to http://127.0.0.1:4244/webhook
'
```

终端 A 显示 `Ready!` 后，将这次输出的 `whsec_...` 填入 `STRIPE_WEBHOOK_SECRET`。保持终端 A 运行，让 Stripe 能把沙箱事件转发到本机。 [Stripe Webhook 本地转发](https://docs.stripe.com/webhooks#local-listener)

在**终端 B**也进入 `stripe-one-off`，启动服务。

```bash
.venv/bin/python app.py
```

打开 [商品购买页](http://127.0.0.1:4244)。整个流程使用 `127.0.0.1`，这样 Stripe 回跳后可以找到当前浏览器的订单。更换端口时，同步修改 CLI 转发地址。

## 4. 用测试卡买一次

1. 商品页会显示 Stripe 产品目录里的名称和价格。点 **前往 Stripe 付款**，确认金额后选择银行卡。
2. 用测试邮箱 `buyer@example.com`、卡号 `4242 4242 4242 4242`、未来有效期（如 `12/35`）和 CVC `123` 付款。如页面要求地址或姓名，填写相应的测试值。沙箱付款不会实际扣款。 [Stripe 测试卡](https://docs.stripe.com/testing#cards)
3. 付款后 Stripe 跳回订单详情，显示 **已支付** 和确认来源。可在终端 A 查看 `checkout.session.completed` 的转发记录，或在 Stripe 沙箱付款列表里用 `pi_...` 查找。
4. 要体验中途退出，从 Checkout 点返回，本地会显示 **待付款**。再次点 **继续付款**会打开这笔订单原来的 Checkout。也可以用测试卡 `4000 0000 0000 0002`，观察 Stripe 拒付后让买家修改卡信息。

## 代码如何串起来

| 步骤 | 本地逻辑 |
| --- | --- |
| 获取商品 | 后端通过 `price_...` 读取已启用的 `one_time` Price，以及关联的 Product。名称、金额和币种来自 Stripe |
| 创建订单 | 后端把商品和价格保存到 SQLite，再创建 `mode="payment"` 的 Checkout Session，传 `line_items=[{"price": "price_...", "quantity": 1}]`。重复提交同一订单使用相同的 Stripe 幂等键 |
| 确认付款 | 后端收到验签的 Webhook，或根据回跳结果查询 Stripe，核对会话、订单号、金额、币种和 `payment_status=paid` 后写入 **已支付** |
| 跟踪延迟付款 | Checkout 完成后若仍未支付，页面显示 **付款处理中**。最终由 `checkout.session.async_payment_succeeded` 或 `checkout.session.async_payment_failed` 更新，也可手动查询 |

同一个 Product 和 Price 可以重复用于多笔一次性订单。`mode="payment"` 每笔完成一次收款。`mode="subscription"` 则用循环价格来开通和续费。 [Checkout Session API](https://docs.stripe.com/api/checkout/sessions/create) · [Checkout 付款确认](https://docs.stripe.com/checkout/fulfillment)

订单保存在这个目录的 `orders.sqlite3`。Demo 用签名的浏览器会话识别买家，正式业务要用登录用户和 HTTPS。现有支付宝或微信支付设置在符合资格的一次性 Checkout 中可用，是否显示由 Stripe 按沙箱配置和订单条件决定。 [Stripe 动态支付方式](https://docs.stripe.com/payments/payment-methods/dynamic-payment-methods)
