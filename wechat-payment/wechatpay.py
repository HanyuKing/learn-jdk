import base64
import json
import os
import time
from pathlib import Path
from urllib.parse import urlencode, urlsplit
from uuid import uuid4

import requests
from cryptography.hazmat.primitives import hashes, serialization
from cryptography.hazmat.primitives.asymmetric import padding, rsa
from cryptography.hazmat.primitives.ciphers.aead import AESGCM


class WechatPay:
    GATEWAY = "https://api.mch.weixin.qq.com"

    def __init__(self, base_dir: Path):
        self.app_id = os.environ["WECHATPAY_APP_ID"]
        self.mch_id = os.environ["WECHATPAY_MCH_ID"]
        self.mch_serial = os.environ["WECHATPAY_MCH_SERIAL"]
        self.public_key_id = os.environ["WECHATPAY_PUBLIC_KEY_ID"]
        self.notify_url = os.environ["WECHATPAY_NOTIFY_URL"]
        callback = urlsplit(self.notify_url)
        if callback.scheme != "https" or not callback.hostname or not callback.path or callback.query or callback.fragment:
            raise ValueError("WECHATPAY_NOTIFY_URL 需要完整的 HTTPS 回调地址，且不带查询参数。")
        if not all((self.app_id, self.mch_id, self.mch_serial)) or not self.public_key_id.startswith("PUB_KEY_ID_"):
            raise ValueError("请填写有效的商户配置和微信支付公钥 ID。")
        api_key = os.environ["WECHATPAY_API_V3_KEY"].encode()
        if len(api_key) != 32:
            raise ValueError("WECHATPAY_API_V3_KEY 必须为 32 字节。")
        self.cipher = AESGCM(api_key)
        self.private_key = serialization.load_pem_private_key(
            (base_dir / os.environ["WECHATPAY_MCH_PRIVATE_KEY_PATH"]).read_bytes(), password=None,
        )
        self.public_key = serialization.load_pem_public_key(
            (base_dir / os.environ["WECHATPAY_PUBLIC_KEY_PATH"]).read_bytes(),
        )
        if not isinstance(self.private_key, rsa.RSAPrivateKey) or not isinstance(self.public_key, rsa.RSAPublicKey):
            raise ValueError("微信支付需要 RSA 密钥。")

    def verify(self, headers, body):
        timestamp = headers["Wechatpay-Timestamp"]
        nonce = headers["Wechatpay-Nonce"]
        if headers["Wechatpay-Serial"] != self.public_key_id or abs(time.time() - int(timestamp)) > 300:
            raise ValueError("微信支付签名公钥 ID 或时间戳无效。")
        message = f"{timestamp}\n{nonce}\n".encode() + body + b"\n"
        signature = base64.b64decode(headers["Wechatpay-Signature"], validate=True)
        self.public_key.verify(signature, message, padding.PKCS1v15(), hashes.SHA256())

    def request(self, method, target, payload=None):
        body = json.dumps(payload, ensure_ascii=False, separators=(",", ":")).encode() if payload else b""
        nonce = uuid4().hex
        timestamp = str(int(time.time()))
        message = f"{method}\n{target}\n{timestamp}\n{nonce}\n".encode() + body + b"\n"
        signature = base64.b64encode(self.private_key.sign(message, padding.PKCS1v15(), hashes.SHA256())).decode()
        authorization = (
            f'WECHATPAY2-SHA256-RSA2048 mchid="{self.mch_id}",nonce_str="{nonce}",'
            f'timestamp="{timestamp}",serial_no="{self.mch_serial}",signature="{signature}"'
        )
        response = requests.request(
            method, self.GATEWAY + target, data=body, timeout=(4, 8), allow_redirects=False,
            headers={"Authorization": authorization, "Wechatpay-Serial": self.public_key_id,
                     "Accept": "application/json", "Content-Type": "application/json"},
        )
        response.raise_for_status()
        self.verify(response.headers, response.content)
        return response.json()

    def create(self, order):
        return self.request("POST", "/v3/pay/transactions/native", {
            "appid": self.app_id, "mchid": self.mch_id,
            "description": order["product"], "out_trade_no": order["id"],
            "time_expire": order["expires_at"], "notify_url": self.notify_url,
            "amount": {"total": order["amount"], "currency": "CNY"},
        })["code_url"]

    def query(self, order_id):
        return self.request("GET", f"/v3/pay/transactions/out-trade-no/{order_id}?" + urlencode({"mchid": self.mch_id}))

    def read_notification(self, headers, body):
        self.verify(headers, body)
        event = json.loads(body)
        resource = event["resource"]
        if event["event_type"] != "TRANSACTION.SUCCESS" or resource["algorithm"] != "AEAD_AES_256_GCM":
            raise ValueError("不支持的微信支付通知。")
        plaintext = self.cipher.decrypt(
            resource["nonce"].encode(), base64.b64decode(resource["ciphertext"], validate=True),
            resource.get("associated_data", "").encode(),
        )
        return json.loads(plaintext)
