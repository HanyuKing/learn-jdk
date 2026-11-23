# Stripe 支付实战示例

用 Python Flask、SQLite 和 Stripe Payment Element 收一笔沙箱订单。可选 **12.50 美元**或 **1,500 日元**。真实 Stripe 沙箱会创建和确认 PaymentIntent，测试卡不会移动真实资金。 [Stripe 沙箱与测试卡](https://docs.stripe.com/testing)

## 启动收银台

需要 Python 3.10 或更高版本，以及同一个 Stripe 沙箱的 `pk_test_...` 和 `sk_test_...`。可在 Stripe Dashboard 的沙箱中获取。Stripe CLI 创建的临时沙箱也可用，其服务端密钥为 `rkcs_test_...`。 [Stripe API 密钥](https://docs.stripe.com/keys)

```bash
cd stripe-payment
python3 -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
test -f .env || cp .env.example .env
```

把公开密钥写入 `.env` 的 `STRIPE_PUBLISHABLE_KEY`，把服务端密钥写入 `STRIPE_SECRET_KEY`。`.env` 已被 Git 忽略。若已有初始化好的 `.env`，可以直接启动。 [本地配置模板](.env.example)

```bash
python app.py
```

打开 [本地收银台](http://127.0.0.1:4242)。先创建订单，选择银行卡支付，再输入 `4242 4242 4242 4242`、任意未来有效期和三位 CVC，按 Stripe 表单要求填写邮编。点击「确认付款」后页面会显示订单状态、`pi_...` 和确认来源。没开启回调时，页面会通过后端查 Stripe 得到结果。可用支付方式由沙箱配置和币种决定。 [Stripe 测试卡](https://docs.stripe.com/testing#cards) · [动态支付方式](https://docs.stripe.com/payments/payment-methods/dynamic-payment-methods)

## 复现 Webhook 确认

1. 安装 Stripe CLI，在另一终端运行 `stripe login` 并选中与 `.env` 相同的沙箱。使用临时沙箱时，可用创建该沙箱的 Stripe CLI 配置。 [Stripe CLI](https://docs.stripe.com/cli/install)
2. 开启事件转发，并保持该终端运行。

   ```bash
   stripe listen --events payment_intent.succeeded \
     --forward-to http://127.0.0.1:4242/api/stripe/webhook
   ```

3. 将 CLI 输出的 `whsec_...` 填到 `STRIPE_WEBHOOK_SECRET`，重启 Python 服务。再用 `4242` 测试卡付一笔。若回调先将订单更新成功，页面的确认来源会显示 **Stripe Webhook**。若页面查单先提交，来源会显示 **服务端查单**，两种情况都会保留首次确认的来源。 [转发与验签](https://docs.stripe.com/webhooks#local-listener)

4. 试一笔日元订单。页面显示 `amount=1500, currency=jpy`。美元订单显示 `amount=1250, currency=usd`。也可用 `4000 0025 0000 3155` 体验银行验证，用 `4000 0000 0000 0002` 验证拒付后订单保持待支付。 [币种最小单位](https://docs.stripe.com/currencies) · [验证和拒付测试卡](https://docs.stripe.com/testing)

## 前端与后端怎么配合

| 步骤 | 入口与实际行为 |
| --- | --- |
| 创建订单 | 浏览器生成订单 UUID，`PUT /api/orders/<id>` 提交币种。后端从固定价格表取金额，用订单号构造 Stripe 幂等键，保存订单和 `pi_...` 的关联 |
| 确认支付 | 浏览器用返回的 `client_secret` 挂载 Stripe 表单，调用 `stripe.confirmPayment`。卡信息由 Stripe 组件采集并提交 |
| 接收通知 | `POST /api/stripe/webhook` 用原始请求体验签，收到成功事件后核对金额、实收、币种、订单号和 `pi_...` |
| 同步状态 | `GET /api/orders/<id>` 读本地状态。必要时 `POST /api/orders/<id>/sync` 主动去 Stripe 查。两条路径共用同一套落库校验 |

服务默认监听 `127.0.0.1`，SQLite 默认写入同目录的 `orders.sqlite3`。这是本地练习，浏览器用随机 UUID 识别自己的订单。接到公开业务时，应使用登录态校验订单归属，并用部署环境的密钥管理方式配置 Stripe。 [Python 后端](app.py) · [浏览器支付逻辑](frontend/checkout.js)
