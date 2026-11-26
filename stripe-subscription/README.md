# Stripe 订阅 Demo

这是一个独立的 Flask + SQLite 小项目。你可以用测试卡开通一个月付或年付会员，让 Stripe 自动生成下一期账单，再到 Stripe 客户门户取消续费。网页会显示订阅状态、最近账单和已付会员期。Demo 默认运行在 `4243`，可以和 `stripe-payment` 的一次性收银台同时使用。

## 先认识四个对象

| 对象 | 用会员订阅举例 | Stripe ID |
| --- | --- | --- |
| Product | 你卖的东西，例如「Demo 会员」 | `prod_...` |
| Price | 怎么收钱，例如每月 10 美元或每年 100 美元 | `price_...` |
| Customer | 哪个人购买。Demo 会在开始结账时创建客户 | `cus_...` |
| Subscription | 这个客户办的会员。每到一个周期，Stripe 会为它生成一张 Invoice（账单，`in_...`） | `sub_...` |

**Checkout** 是 Stripe 提供的付款页面，负责收集卡信息和完成首期付款。**Webhook** 是 Stripe 向你的后端发出的通知，即使用户关掉页面，后端也能收到付款或取消消息。 [Stripe 订阅入门](https://docs.stripe.com/billing/subscriptions/overview) · [Webhook 入门](https://docs.stripe.com/webhooks)

## 1. 进入 Stripe 沙箱，创建月付和年付价格

1. 登录 Stripe Dashboard。点击左上角的账号选择器，进入 **Sandboxes（沙箱）**，选择已有沙箱或创建一个独立沙箱。接下来的商品、价格、密钥和客户门户都在这个沙箱里操作。沙箱的测试卡流程不会实际扣款。 [Stripe 沙箱](https://docs.stripe.com/testing-use-cases)
2. 打开 **Product catalogue（产品目录）**，点击 **Add product（添加产品）**，填入名称 `Demo 会员`。
3. 在价格区选择 **Recurring（循环收费）** 和 **Flat rate（固定金额）**。币种选 `USD`，金额填 `10.00`，周期选 **Monthly（每月）**。保存后进入商品详情，复制这条价格的 `price_...`，记为「月付」。
4. 在同一商品下点击 **Add another price（添加另一个价格）**。仍选循环收费和固定金额，填 `USD 100.00`，周期改为 **Yearly（每年）**。复制第二个 `price_...`，记为「年付」。这些金额是示例，也可以在沙箱里填自己的固定价格。
5. 在该沙箱打开 **Developers → API keys（开发者 → API 密钥）**，复制以 `sk_test_` 开头的 **Secret key（密钥）**。如果看到的是 `prod_...`，你复制的是产品 ID。这个 Demo 使用 Stripe 托管的 Checkout，配置表只需要服务端密钥。 [产品与价格](https://docs.stripe.com/products-prices/how-products-and-prices-work) · [Stripe API 密钥](https://docs.stripe.com/keys)

## 2. 安装并配置本地项目

用终端进入仓库根目录。以下命令适用于 macOS 和 Linux，要求 Python 3.10 或以上。

```bash
cd stripe-subscription
python3 -m venv .venv
.venv/bin/python -m pip install -r requirements.txt
test -f .env || cp .env.example .env
.venv/bin/python -c 'import secrets; print(secrets.token_hex(32))'
```

最后一条命令会打印一段随机字符串。用编辑器打开本目录下的 `.env`，在等号后面填写对应值。

| `.env` 变量 | 填什么 |
| --- | --- |
| `STRIPE_SECRET_KEY` | 第 1 步复制的 `sk_test_...` |
| `STRIPE_MONTHLY_PRICE_ID` | 月付的 `price_...` |
| `STRIPE_YEARLY_PRICE_ID` | 年付的 `price_...` |
| `FLASK_SECRET_KEY` | 刚才生成的随机字符串，用于签名本地浏览器会话。保留原值，重启后同一个浏览器还能找到自己的订阅 |
| `STRIPE_WEBHOOK_SECRET` | 先留空，下一步从 Stripe CLI 复制 |
| `PORT` | 保留 `4243` |

`.env` 已被 Git 忽略，示例文件中的空值可以提交。浏览器只需访问本地页面，服务端会读取 Stripe 密钥。

## 3. 转发 Webhook，然后启动网页

本机 `127.0.0.1` 无法被 Stripe 直接访问。Stripe CLI 会把沙箱里的事件转发到本机。安装后用 `stripe --version` 确认可以运行。macOS 用 Homebrew 可执行 `brew install stripe/stripe-cli/stripe`。 [Stripe CLI 安装说明](https://docs.stripe.com/stripe-cli/install)

**终端 A**进入 `stripe-subscription` 目录，运行下列命令。它从 `.env` 读取同一个沙箱的密钥。

```bash
.venv/bin/dotenv run -- sh -c '
  export STRIPE_API_KEY="$STRIPE_SECRET_KEY"
  exec stripe listen \
    --events customer.subscription.created,customer.subscription.updated,customer.subscription.deleted,customer.subscription.paused,customer.subscription.resumed,invoice.paid,invoice.payment_failed \
    --forward-to http://127.0.0.1:4243/webhook
'
```

等终端输出 `Ready!`，复制它提示的 `whsec_...`，填入 `.env` 的 `STRIPE_WEBHOOK_SECRET`。终端 A 保持运行。后端用这个值验证通知签名。Dashboard 事件目标的签名密钥用于该目标，请把 CLI 输出的密钥用于本地转发。 [Stripe 本地监听](https://docs.stripe.com/webhooks#local-listener)

**终端 B**也进入 `stripe-subscription`，运行服务。

```bash
.venv/bin/python app.py
```

打开 [订阅 Demo](http://127.0.0.1:4243)。使用 `127.0.0.1` 贯穿整个测试，Stripe 会返回这个地址，浏览器也会用这个地址找到本次会话。如果更换 `PORT`，同步修改浏览器地址和终端 A 的转发地址。

## 4. 测试首次付款

1. 在页面点 **订阅月付会员**。浏览器会打开 Stripe Checkout，核对币种、金额和「每月」周期。
2. 使用测试邮箱 `member@example.com`。选择银行卡，输入 `4242 4242 4242 4242`、任意未来有效期（如 `12/35`）、CVC `123`。如页面要求姓名、邮编等，可填写相应的测试信息。 [Stripe 测试卡](https://docs.stripe.com/testing#cards)
3. 点击 Stripe 页面上的订阅按钮，等待跳回 Demo。预期显示 **订阅正常（`active`）**、**最近账单已支付**、**会员权限已开通**，还有 `sub_...` 和「已付款至」日期。日期显示为 UTC。
4. 在终端 A 找到 `invoice.paid` 转发记录，确认 HTTP 状态是 `204`。在 Dashboard 的 **Billing → Subscriptions（账单 → 订阅）**也能用页面上的 `sub_...` 找到这笔订阅及其账单。

测试年付时，可以另开一个无痕窗口作为新会员。用 `4000 0000 0000 0002` 可以测试首次付款被拒，未付账单的会员权益保持关闭。未完成的结账可以回到本地页继续。相同套餐会继续使用原结账会话，改选套餐会先关闭旧的未付款会话。

## 5. 测试自动续费

1. 保持两个终端运行。在 Dashboard 沙箱的订阅列表打开刚创建的月付订阅，选择 **Run simulation（运行模拟）**。
2. 使用 **Advance time（推进时间）**，把模拟时钟推进到下一次扣款时间之后。如果账单仍是草稿，再推进一小段时间到发票完成并付款。等 Stripe 完成模拟后查看新增账单和 `invoice.paid` 事件。年付订阅的操作相同，推进到它的下一个年度账期。 [Stripe 模拟订阅](https://docs.stripe.com/billing/testing/test-clocks/simulate-subscriptions)
3. 回到 Demo 刷新网页。第二张账单已支付后，「已付款至」会更新到下一期的结束日期。如果本地 CLI 当时断开，点击 **查询 Stripe 状态** 可重新拉取最新账单和订阅状态。

Stripe 负责按周期生成账单、尝试自动扣款和执行沙箱的重试配置。续费失败时，Stripe 会发送 `invoice.payment_failed`。Demo 保留上一张已付账单覆盖的会员期，等下一张账单付款后才延长。 [Stripe 订阅付款失败处理](https://docs.stripe.com/billing/subscriptions/webhooks#payment-failures)

## 6. 管理支付方式和取消续费

1. 在同一个沙箱的 **Settings → Billing → Customer portal（设置 → 账单 → 客户门户）** 打开配置。启用更新支付方式、查看账单和取消订阅。示例选择**当前周期结束时取消**并保存设置。这个固定套餐示例里，关闭门户中的套餐切换即可。 [客户门户配置](https://docs.stripe.com/customer-management/configure-portal)
2. 在本地 Demo 点击 **管理订阅和账单**。Stripe 会打开这个测试客户的门户。进入订阅详情，点击 **Cancel subscription（取消订阅）**并确认。
3. 返回 Demo。到期取消时，页面会显示**计划取消时间**，已付会员期内仍可使用。到期后，Stripe 发送订阅结束通知，页面显示 **已结束（`canceled`）**。如果门户配置为立即取消，订阅结束后权益立即关闭。

## 代码是怎么接起来的

| 操作 | 本项目的实现 |
| --- | --- |
| 选择套餐 | `POST /checkout` 根据表单中的 `month` 或 `year` 从服务端选 Price，验证它是对应周期的循环固定价格 |
| 打开 Checkout | 服务端创建 `mode="subscription"` 的 Checkout Session，传入客户 ID、Price ID 和返回地址，再跳到 `session.url` |
| 结账返回 | `GET /checkout/return` 核对本浏览器的 Checkout Session，通过 Stripe API 查订阅和账单 |
| 接收事件 | `POST /webhook` 用原始 HTTP 请求体验证 Stripe 签名。用 Stripe API 读取最新订阅，处理相邻事件乱序的情况 |
| 更新会员期 | 只记录状态为 `paid` 的订阅账单，从该账单的订阅商品行取服务期结束时间。按 `in_...` 去重，用最大的已付结束时间作为会员期 |
| 自助管理 | `POST /portal` 用本地会话对应的客户 ID 创建客户门户会话 |

Stripe 会发两类关键通知。`invoice.paid` 和 `invoice.payment_failed` 用于同步已付或未付账单。`customer.subscription.created`、`.updated`、`.deleted`、`.paused` 和 `.resumed` 用于同步订阅生命周期。付款成功页只是一个返回入口，后端以 Stripe 验证过的订阅和账单为准。 [订阅事件](https://docs.stripe.com/billing/subscriptions/webhooks) · [事件顺序](https://docs.stripe.com/webhooks#event-ordering)

## 常见问题

| 现象 | 检查哪里 |
| --- | --- |
| 按钮灰色或提示缺少配置 | 检查 `stripe-subscription/.env` 内的密钥和两个 Price ID，保存后重启 Python 服务 |
| Stripe 报 `No such price` | 确认 Price 和 API 密钥来自同一个 Stripe 沙箱，检查前缀和复制范围 |
| 提示价格周期不匹配 | 月付对应每 `1` 月，年付对应每 `1` 年，且都是固定金额循环价格 |
| Webhook 返回 `400` | 检查是否填了当前 CLI 输出的 `whsec_...`，修改后重启 Python 服务 |
| CLI 持续显示 `204`，页面仍没有订阅 | 从这个 Demo 的 Checkout 创建订单。`stripe trigger` 生成的是独立测试对象，只有属于这个 Demo 及当前浏览器的订阅才会显示 |
| 客户门户打不开 | 检查是否在同一个沙箱保存了 Customer Portal 配置 |
| 临时沙箱无法创建测试时钟 | 使用 Stripe Dashboard 中的沙箱及该沙箱的 `sk_test_...` 密钥。CLI 创建的未认领沙箱权限较少 |
| 以前的订阅不见了 | 使用原浏览器、原 `FLASK_SECRET_KEY` 和原 `subscriptions.sqlite3`。本地示例用浏览器会话识别会员 |
| 已启用支付宝或微信，结账页找不到 | 标准 Checkout 的 `subscription` 模式不支持这两个钱包。Demo 用银行卡测试自动续费。 [Stripe 钱包兼容性](https://docs.stripe.com/payments/wallets#product-support) |

`app.py` 包含 Stripe 请求和 Webhook 处理，`templates/index.html` 是本地页面。`subscriptions.sqlite3` 会在运行时自动生成。迁入正式应用时，用登录用户关联 `cus_...`，配置 HTTPS 返回地址及生产 Webhook，并按业务规则管理试用、续费失败宽限期和取消后的权益。
