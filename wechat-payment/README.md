# 微信支付 Demo

用 Flask 和 SQLite 体验从下单到微信 Native 二维码支付。示例商品为定制树脂挂件，单价 ¥0.01，数量由页面选择，订单金额由后端计算。默认使用本地模拟模式。

## 本地模拟

使用 Python 3.10 或更高版本。在仓库根目录运行下面的命令。

```bash
cd wechat-payment
python3 -m venv .venv
.venv/bin/python -m pip install -r requirements.txt
test -f .env || cp .env.example .env
.venv/bin/python app.py
```

打开 [本地微信收银台](http://127.0.0.1:4243/)，选择数量并创建订单。点「生成收款码」后打开模拟钱包，选择模拟支付成功或失败。返回订单页可看到状态，成功时还会显示模拟交易号。模拟二维码指向本机地址，可用「打开模拟钱包」在同一台电脑操作。

## 使用商户号联调 Native 支付

**`native` 模式调用微信支付正式网关。扫码确认会产生真实人民币交易。** 微信支付 API v3 暂无独立的通用沙箱。这个示例接普通商户的境内 Native 接口，只使用 CNY。跨境商户接口由其签约产品决定。 [API v3 测试说明](https://pay.wechatpay.cn/doc/v3/partner/4012082456) · [Native 下单](https://pay.wechatpay.cn/doc/v3/merchant/4012791877)

在已开通 Native 支付的商户号中配置以下值，然后重启应用。密钥文件放在安全位置，用路径引用。

| `.env` | 填写内容 |
| --- | --- |
| `WECHATPAY_MODE` | `native` |
| `WECHATPAY_APP_ID`、`WECHATPAY_MCH_ID` | 已关联的 App ID 和微信支付商户号 |
| `WECHATPAY_MCH_SERIAL`、`WECHATPAY_MCH_PRIVATE_KEY_PATH` | 商户 API 证书序列号和对应的 RSA 私钥 PEM |
| `WECHATPAY_PUBLIC_KEY_ID`、`WECHATPAY_PUBLIC_KEY_PATH` | 微信支付公钥 ID 和下载的公钥 PEM，用于验签 |
| `WECHATPAY_API_V3_KEY` | 商户平台设置的 32 字节 API v3 密钥，用于解密通知 |
| `WECHATPAY_NOTIFY_URL` | 可从公网到达的 HTTPS 地址，例如 `https://your-domain.example/api/wechatpay/notify`，由你自己的反向代理转发到本服务 |

1. 创建订单后点「生成收款码」。服务端签名请求 `POST /v3/pay/transactions/native`，验证微信响应，再把返回的 `code_url` 转为二维码。示例订单的付款时限为 15 分钟。
2. 用微信扫一扫付款。微信异步通知由后端验签和解密。也可以点「查询支付结果」，用 `GET /v3/pay/transactions/out-trade-no/{out_trade_no}` 主动确认。
3. 服务端核对商户、订单号、交易类型、CNY 金额和微信交易号，再保存支付状态。重复通知或查单沿用首次保存的确认来源。 [微信支付公钥验签](https://pay.wechatpay.cn/doc/v3/partner/4013059017) · [查单接口](https://pay.wechatpay.cn/doc/v3/merchant/4012791880) · [支付通知](https://pay.wechatpay.cn/doc/v3/merchant/4012791882)

服务默认监听 `127.0.0.1:4243`，订单按模式分别写入 `orders-mock.sqlite3` 和 `orders-native.sqlite3`。
