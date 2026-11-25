# 支付宝支付 Demo

用 Flask、SQLite 和支付宝官方 Python SDK 体验电脑网站支付。示例商品为定制树脂挂件，单价 ¥0.01，数量由页面选择，订单金额由后端计算。默认使用本地模拟，配好沙箱凭据即可跳到支付宝官方测试收银台。

## 本地模拟

使用 Python 3.10 或更高版本。在仓库根目录运行下面的命令。

```bash
cd alipay-payment
python3 -m venv .venv
.venv/bin/python -m pip install -r requirements.txt
test -f .env || cp .env.example .env
.venv/bin/python app.py
```

打开 [本地支付宝收银台](http://127.0.0.1:4244/)，选择数量并创建订单。点「前往模拟收银台」，选择模拟支付成功或失败。返回订单页可看到状态，成功时还会显示模拟交易号。

## 使用支付宝官方沙箱

登录支付宝开放平台，在「沙箱」中找到沙箱 APPID、商家 PID 和测试买家账号。使用 RSA2 密钥配置，将应用公钥上传到沙箱并获取支付宝公钥。这个示例使用境内电脑网站支付的 CNY 沙箱，Antom 跨境收款有独立接口和沙箱。 [支付宝沙箱账号说明](https://aipay.alipay.com/article/3725) · [Antom 沙箱](https://docs.antom.com/ac/ref/sandbox)

在 `.env` 配置下列值，重启后打开收银台。文件路径支持本目录下的相对路径和绝对路径。

| `.env` | 填写内容 |
| --- | --- |
| `ALIPAY_MODE` | `sandbox` |
| `ALIPAY_APP_ID`、`ALIPAY_SELLER_ID` | 沙箱应用 APPID、沙箱商家 PID |
| `ALIPAY_APP_PRIVATE_KEY_PATH` | 与已上传应用公钥对应的 RSA 私钥 PEM |
| `ALIPAY_PUBLIC_KEY_PATH` | 沙箱支付宝公钥 PEM。若控制台只提供 Base64 公钥正文，请保存为包含 `BEGIN PUBLIC KEY` 和 `END PUBLIC KEY` 的 PEM 文件 |
| `ALIPAY_RETURN_ORIGIN` | 浏览器付款后返回的本服务地址，默认 `http://127.0.0.1:4244` |
| `ALIPAY_NOTIFY_URL` | 可选。验证异步通知时，填写公网 HTTPS 地址，例如 `https://your-domain.example/api/alipay/notify`，由你自己的反向代理转发到本服务 |

1. 点「前往支付宝沙箱付款」。后端用 `alipay.trade.page.pay` 和 `FAST_INSTANT_TRADE_PAY` 签出付款 URL，浏览器跳到支付宝测试收银台。用沙箱买家账号完成支付。
2. 浏览器回到 Demo 后，服务端调用 `alipay.trade.query` 确认订单。如果关了付款页，可回到 Demo 点「查询支付结果」。查单成功返回经 SDK 验签，页面按服务端结果显示订单状态。
3. 如配置了异步通知，后端用支付宝公钥验签，同时校验 APPID、商家 PID、订单号和金额，再更新订单。这条路径可以通过订单页的确认来源查看。 [支付宝官方 Python SDK](https://github.com/alipay/alipay-sdk-python-all) · [电脑网站支付接口示例](https://github.com/alipay/alipay-easysdk/blob/master/APIDoc.md#电脑网站支付)

服务默认监听 `127.0.0.1:4244`，订单按模式分别写入 `orders-mock.sqlite3` 和 `orders-sandbox.sqlite3`。
