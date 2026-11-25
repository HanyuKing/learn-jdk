import json
import os
from pathlib import Path
from urllib.parse import urlsplit

from alipay.aop.api.AlipayClientConfig import AlipayClientConfig
from alipay.aop.api.DefaultAlipayClient import DefaultAlipayClient
from alipay.aop.api.domain.AlipayTradePagePayModel import AlipayTradePagePayModel
from alipay.aop.api.domain.AlipayTradeQueryModel import AlipayTradeQueryModel
from alipay.aop.api.request.AlipayTradePagePayRequest import AlipayTradePagePayRequest
from alipay.aop.api.request.AlipayTradeQueryRequest import AlipayTradeQueryRequest
from alipay.aop.api.util.SignatureUtils import get_sign_content, verify_with_rsa
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric import rsa


class AlipaySandbox:
    GATEWAY = "https://openapi-sandbox.dl.alipaydev.com/gateway.do"

    def __init__(self, base_dir: Path):
        self.app_id = os.environ["ALIPAY_APP_ID"]
        self.seller_id = os.environ["ALIPAY_SELLER_ID"]
        if not self.app_id.isdecimal() or not self.seller_id.isdecimal():
            raise ValueError("请填写沙箱 APPID 和沙箱商家 PID。")

        self.return_origin = os.getenv("ALIPAY_RETURN_ORIGIN", f"http://127.0.0.1:{os.getenv('PORT', '4244')}").rstrip("/")
        origin = urlsplit(self.return_origin)
        self.notify_url = os.getenv("ALIPAY_NOTIFY_URL")
        if (origin.scheme not in {"http", "https"} or not origin.hostname or origin.username
                or origin.path or origin.query or origin.fragment):
            raise ValueError("ALIPAY_RETURN_ORIGIN 只填写协议、主机和端口。")
        if self.notify_url:
            callback = urlsplit(self.notify_url)
            if callback.scheme != "https" or not callback.hostname or callback.query or callback.fragment:
                raise ValueError("ALIPAY_NOTIFY_URL 需要完整的 HTTPS 回调地址，且不带查询参数。")

        private_key = serialization.load_pem_private_key(
            (base_dir / os.environ["ALIPAY_APP_PRIVATE_KEY_PATH"]).read_bytes(), password=None,
        )
        public_key = serialization.load_pem_public_key((base_dir / os.environ["ALIPAY_PUBLIC_KEY_PATH"]).read_bytes())
        if not isinstance(private_key, rsa.RSAPrivateKey) or not isinstance(public_key, rsa.RSAPublicKey):
            raise ValueError("支付宝需要 RSA 密钥。")
        config = AlipayClientConfig()
        config.server_url = self.GATEWAY
        config.app_id = self.app_id
        config.sign_type = "RSA2"
        config.timeout = 8
        config.app_private_key = private_key.private_bytes(
            serialization.Encoding.PEM, serialization.PrivateFormat.TraditionalOpenSSL, serialization.NoEncryption(),
        ).decode().strip()
        self.public_key = public_key.public_bytes(
            serialization.Encoding.PEM, serialization.PublicFormat.SubjectPublicKeyInfo,
        ).decode().strip()
        config.alipay_public_key = self.public_key
        self.client = DefaultAlipayClient(config)

    def checkout(self, order, return_path):
        model = AlipayTradePagePayModel()
        model.out_trade_no = order["id"]
        model.product_code = "FAST_INSTANT_TRADE_PAY"
        model.subject = order["product"]
        model.total_amount = f"{order['amount'] // 100}.{order['amount'] % 100:02d}"
        model.timeout_express = "15m"
        request = AlipayTradePagePayRequest(biz_model=model)
        request.return_url = self.return_origin + return_path
        if self.notify_url:
            request.notify_url = self.notify_url
        return self.client.page_execute(request, http_method="GET")

    def query(self, order_id):
        model = AlipayTradeQueryModel()
        model.out_trade_no = order_id
        result = json.loads(self.client.execute(AlipayTradeQueryRequest(biz_model=model)))
        if result.get("sub_code") == "ACQ.TRADE_NOT_EXIST":
            return None
        if result.get("code") != "10000":
            raise ValueError("支付宝查单返回失败。")
        return result

    def read_notification(self, form):
        if any(len(values) != 1 for _, values in form.lists()) or form.get("sign_type") != "RSA2":
            raise ValueError("支付宝通知格式有误。")
        values = form.to_dict()
        signature = values.pop("sign")
        values.pop("sign_type")
        content = get_sign_content({key: value for key, value in values.items() if value}).encode()
        if not verify_with_rsa(self.public_key, content, signature):
            raise ValueError("支付宝通知验签失败。")
        if values["app_id"] != self.app_id or values["seller_id"] != self.seller_id:
            raise ValueError("支付宝 App ID 或商家 PID 不匹配。")
        return values
