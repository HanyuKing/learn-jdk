import os
import sqlite3
from contextlib import closing
from datetime import datetime, timedelta, timezone
from io import BytesIO
from pathlib import Path
from uuid import UUID, uuid4

import qrcode
from cryptography.exceptions import InvalidSignature, InvalidTag
from dotenv import load_dotenv
from flask import Flask, abort, redirect, render_template, request, send_file, url_for
from requests import RequestException

from wechatpay import WechatPay


BASE_DIR = Path(__file__).resolve().parent
PRODUCT = "定制树脂挂件"
UNIT_PRICE = 1  # 分
STATUSES = {"pending": "待支付", "paid": "已支付", "failed": "支付失败", "closed": "已关闭"}
PAYMENT_ERRORS = (RequestException, InvalidSignature, InvalidTag, ValueError, KeyError, TypeError)


def create_app():
    load_dotenv(BASE_DIR / ".env")
    mode = os.getenv("WECHATPAY_MODE", "mock")
    if mode not in {"mock", "native"}:
        raise ValueError("WECHATPAY_MODE 请选择 mock 或 native。")
    provider = WechatPay(BASE_DIR) if mode == "native" else None
    database = Path(os.getenv("WECHATPAY_DATABASE") or BASE_DIR / f"orders-{mode}.sqlite3")
    app = Flask(__name__)
    app.config["MAX_CONTENT_LENGTH"] = 64 * 1024

    def connect():
        connection = sqlite3.connect(database)
        connection.row_factory = sqlite3.Row
        return connection

    with closing(connect()) as connection, connection:
        connection.execute("""CREATE TABLE IF NOT EXISTS orders (
            id TEXT PRIMARY KEY, product TEXT NOT NULL, quantity INTEGER NOT NULL,
            amount INTEGER NOT NULL, expires_at TEXT NOT NULL, code_url TEXT,
            status TEXT NOT NULL DEFAULT 'pending', transaction_id TEXT UNIQUE, confirmed_by TEXT
        )""")

    def order_for(order_id):
        with closing(connect()) as connection:
            order = connection.execute("SELECT * FROM orders WHERE id = ?", (order_id.hex,)).fetchone()
        if order is None:
            abort(404)
        return order

    def order_page(order_id, message=None, error=False):
        order = order_for(order_id)
        expired = datetime.now(timezone.utc) >= datetime.fromisoformat(order["expires_at"])
        return render_template("checkout.html", mode=mode, statuses=STATUSES, order=order, order_id=order_id,
                               expired=expired, message=message), 502 if error else 200

    def record_result(order, status, source, transaction_id=None):
        with closing(connect()) as connection, connection:
            connection.execute("""UPDATE orders SET status = ?, transaction_id = ?, confirmed_by = ?
                                  WHERE id = ? AND status = 'pending'""", (status, transaction_id, source, order["id"]))
            saved = connection.execute("SELECT status, transaction_id FROM orders WHERE id = ?", (order["id"],)).fetchone()
            if status == "paid" and (saved["status"] != status or saved["transaction_id"] != transaction_id):
                raise ValueError("同一订单的支付记录冲突。")

    def confirm(order, result, source):
        amount = result.get("amount") or {}
        if (result["out_trade_no"] != order["id"] or result["mchid"] != provider.mch_id
                or result["appid"] != provider.app_id):
            raise ValueError("微信支付返回的订单或商户不匹配。")
        if result["trade_state"] == "SUCCESS":
            if (result["trade_type"] != "NATIVE" or not result["transaction_id"]
                    or amount["total"] != order["amount"] or amount["currency"] != "CNY"):
                raise ValueError("微信支付返回的交易类型、金额或币种不匹配。")
            record_result(order, "paid", source, result["transaction_id"])
        elif result["trade_state"] == "CLOSED":
            record_result(order, "closed", source)

    @app.after_request
    def no_cache(response):
        response.headers["Cache-Control"] = "no-store"
        response.headers["Referrer-Policy"] = "no-referrer"
        return response

    @app.get("/")
    def shop():
        return render_template("checkout.html", mode=mode, product=PRODUCT, unit_price=UNIT_PRICE, order=None)

    @app.post("/orders")
    def create_order():
        quantity = request.form.get("quantity", type=int)
        if quantity not in range(1, 10):
            abort(400, "数量须为 1 到 9。")
        order_id = uuid4()
        expires_at = (datetime.now(timezone.utc) + timedelta(minutes=15)).isoformat(timespec="seconds")
        with closing(connect()) as connection, connection:
            connection.execute("INSERT INTO orders (id, product, quantity, amount, expires_at) VALUES (?, ?, ?, ?, ?)",
                               (order_id.hex, PRODUCT, quantity, quantity * UNIT_PRICE, expires_at))
        return redirect(url_for("show_order", order_id=order_id), 303)

    @app.get("/orders/<uuid:order_id>")
    def show_order(order_id):
        return order_page(order_id)

    @app.post("/orders/<uuid:order_id>/pay")
    def start_payment(order_id):
        order = order_for(order_id)
        if order["status"] == "pending" and not order["code_url"]:
            if datetime.now(timezone.utc) >= datetime.fromisoformat(order["expires_at"]):
                return order_page(order_id, "付款时间已过，请创建新订单。")
            try:
                code_url = provider.create(order) if provider else url_for("mock_wallet", order_id=order_id, _external=True)
                if provider and not code_url.startswith("weixin://wxpay/"):
                    raise ValueError("微信支付返回了无效的二维码链接。")
                with closing(connect()) as connection, connection:
                    connection.execute("UPDATE orders SET code_url = ? WHERE id = ? AND code_url IS NULL",
                                       (code_url, order["id"]))
            except PAYMENT_ERRORS:
                return order_page(order_id, "生成收款码失败。请核对商户配置或重试此订单。", error=True)
        return redirect(url_for("show_order", order_id=order_id), 303)

    @app.get("/orders/<uuid:order_id>/qr.png")
    def payment_qr(order_id):
        order = order_for(order_id)
        if not order["code_url"] or order["status"] != "pending" or datetime.now(timezone.utc) >= datetime.fromisoformat(order["expires_at"]):
            abort(404)
        image = BytesIO()
        qrcode.make(order["code_url"]).save(image, format="PNG")
        image.seek(0)
        return send_file(image, mimetype="image/png")

    @app.post("/orders/<uuid:order_id>/sync")
    def sync_order(order_id):
        order = order_for(order_id)
        if provider and order["status"] == "pending" and order["code_url"]:
            try:
                confirm(order, provider.query(order["id"]), "微信支付查单")
            except PAYMENT_ERRORS + (sqlite3.IntegrityError,):
                return order_page(order_id, "查询支付结果失败，请稍后再试。", error=True)
        return redirect(url_for("show_order", order_id=order_id), 303)

    @app.route("/orders/<uuid:order_id>/mock-wallet", methods=["GET", "POST"])
    def mock_wallet(order_id):
        if provider:
            abort(404)
        order = order_for(order_id)
        if not order["code_url"]:
            abort(404)
        if request.method == "POST":
            status = request.form.get("result")
            if status not in {"paid", "failed"}:
                abort(400)
            if order["status"] == "pending" and datetime.now(timezone.utc) >= datetime.fromisoformat(order["expires_at"]):
                record_result(order, "closed", "本地模拟")
            elif order["status"] == "pending":
                transaction_id = f"mock_{order['id']}" if status == "paid" else None
                record_result(order, status, "本地模拟", transaction_id)
            return redirect(url_for("show_order", order_id=order_id), 303)
        return render_template("checkout.html", mode=mode, order=order, order_id=order_id,
                               statuses=STATUSES, wallet=True)

    @app.post("/api/wechatpay/notify")
    def notify():
        if provider is None:
            abort(404)
        try:
            result = provider.read_notification(request.headers, request.get_data())
            if result["trade_state"] != "SUCCESS":
                raise ValueError("通知中的交易未成功。")
            confirm(order_for(UUID(result["out_trade_no"])), result, "微信支付回调")
        except PAYMENT_ERRORS + (sqlite3.IntegrityError,):
            return {"code": "FAIL", "message": "支付通知验签或校验失败"}, 400
        return "", 204

    return app


if __name__ == "__main__":
    create_app().run(host="127.0.0.1", port=int(os.getenv("PORT", "4243")))
