import os
import sqlite3
from contextlib import closing
from decimal import Decimal, InvalidOperation
from pathlib import Path
from uuid import UUID, uuid4

from alipay.aop.api.exception.Exception import RequestException, ResponseException
from dotenv import load_dotenv
from flask import Flask, Response, abort, redirect, render_template, request, url_for
from rsa import VerificationError

from alipay_gateway import AlipaySandbox


BASE_DIR = Path(__file__).resolve().parent
PRODUCT = "定制树脂挂件"
UNIT_PRICE = 1  # 分
STATUSES = {"pending": "待支付", "paid": "已支付", "failed": "支付失败", "closed": "已关闭"}
PAYMENT_ERRORS = (RequestException, ResponseException, OSError, VerificationError,
                  InvalidOperation, ValueError, KeyError, TypeError, sqlite3.IntegrityError)


def create_app():
    load_dotenv(BASE_DIR / ".env")
    mode = os.getenv("ALIPAY_MODE", "mock")
    if mode not in {"mock", "sandbox"}:
        raise ValueError("ALIPAY_MODE 请选择 mock 或 sandbox。")
    provider = AlipaySandbox(BASE_DIR) if mode == "sandbox" else None
    database = Path(os.getenv("ALIPAY_DATABASE") or BASE_DIR / f"orders-{mode}.sqlite3")
    app = Flask(__name__)
    app.config["MAX_CONTENT_LENGTH"] = 64 * 1024

    def connect():
        connection = sqlite3.connect(database)
        connection.row_factory = sqlite3.Row
        return connection

    with closing(connect()) as connection, connection:
        connection.execute("""CREATE TABLE IF NOT EXISTS orders (
            id TEXT PRIMARY KEY, product TEXT NOT NULL, quantity INTEGER NOT NULL,
            amount INTEGER NOT NULL, status TEXT NOT NULL DEFAULT 'pending',
            transaction_id TEXT UNIQUE, confirmed_by TEXT
        )""")

    def order_for(order_id):
        with closing(connect()) as connection:
            order = connection.execute("SELECT * FROM orders WHERE id = ?", (order_id.hex,)).fetchone()
        if order is None:
            abort(404)
        return order

    def order_page(order_id, message=None, error=False):
        return render_template("checkout.html", mode=mode, statuses=STATUSES, order=order_for(order_id),
                               order_id=order_id, message=message), 502 if error else 200

    def record_result(order, status, source, transaction_id=None):
        with closing(connect()) as connection, connection:
            connection.execute("""UPDATE orders SET status = ?, transaction_id = ?, confirmed_by = ?
                                  WHERE id = ? AND status = 'pending'""", (status, transaction_id, source, order["id"]))
            saved = connection.execute("SELECT status, transaction_id FROM orders WHERE id = ?", (order["id"],)).fetchone()
            if status == "paid" and (saved["status"] != status or saved["transaction_id"] != transaction_id):
                raise ValueError("同一订单的支付记录冲突。")

    def confirm(order, result, source):
        if (result["out_trade_no"] != order["id"] or result.get("seller_id", provider.seller_id) != provider.seller_id
                or Decimal(result["total_amount"]) * 100 != order["amount"]):
            raise ValueError("支付宝返回的订单号、商家或订单金额不匹配。")
        if result["trade_status"] in {"TRADE_SUCCESS", "TRADE_FINISHED"}:
            if not result["trade_no"]:
                raise ValueError("缺少支付宝交易号。")
            record_result(order, "paid", source, result["trade_no"])
        elif result["trade_status"] == "TRADE_CLOSED":
            record_result(order, "closed", source)

    @app.after_request
    def no_cache(response):
        response.headers["Cache-Control"] = "no-store"
        response.headers["Referrer-Policy"] = "no-referrer"
        return response

    @app.get("/")
    def shop():
        return render_template("checkout.html", mode=mode, order=None, product=PRODUCT, unit_price=UNIT_PRICE)

    @app.post("/orders")
    def create_order():
        quantity = request.form.get("quantity", type=int)
        if quantity not in range(1, 10):
            abort(400, "数量须为 1 到 9。")
        order_id = uuid4()
        with closing(connect()) as connection, connection:
            connection.execute("INSERT INTO orders (id, product, quantity, amount) VALUES (?, ?, ?, ?)",
                               (order_id.hex, PRODUCT, quantity, quantity * UNIT_PRICE))
        return redirect(url_for("show_order", order_id=order_id), 303)

    @app.get("/orders/<uuid:order_id>")
    def show_order(order_id):
        return order_page(order_id)

    @app.post("/orders/<uuid:order_id>/pay")
    def start_payment(order_id):
        order = order_for(order_id)
        if order["status"] != "pending":
            return redirect(url_for("show_order", order_id=order_id), 303)
        if provider is None:
            return redirect(url_for("mock_wallet", order_id=order_id), 303)
        try:
            return_path = url_for("payment_return", order_id=order_id, _method="GET")
            return redirect(provider.checkout(order, return_path), 303)
        except PAYMENT_ERRORS:
            return order_page(order_id, "生成支付宝链接失败，请核对沙箱配置并重试。", error=True)

    @app.get("/orders/<uuid:order_id>/return")
    @app.post("/orders/<uuid:order_id>/sync")
    def payment_return(order_id):
        order = order_for(order_id)
        if provider and order["status"] == "pending":
            try:
                result = provider.query(order["id"])
                if result is None:
                    return order_page(order_id, "支付宝暂未查到这笔交易。付款后可再次查询。")
                confirm(order, result, "支付宝查单")
            except PAYMENT_ERRORS:
                return order_page(order_id, "查询支付宝结果失败，请稍后再试。", error=True)
        return redirect(url_for("show_order", order_id=order_id), 303)

    @app.route("/orders/<uuid:order_id>/mock-wallet", methods=["GET", "POST"])
    def mock_wallet(order_id):
        if provider:
            abort(404)
        order = order_for(order_id)
        if request.method == "POST":
            status = request.form.get("result")
            if status not in {"paid", "failed"}:
                abort(400)
            if order["status"] == "pending":
                transaction_id = f"mock_{order['id']}" if status == "paid" else None
                record_result(order, status, "本地模拟", transaction_id)
            return redirect(url_for("show_order", order_id=order_id), 303)
        return render_template("checkout.html", mode=mode, order=order, order_id=order_id,
                               statuses=STATUSES, wallet=True)

    @app.post("/api/alipay/notify")
    def notify():
        if provider is None:
            abort(404)
        try:
            result = provider.read_notification(request.form)
            confirm(order_for(UUID(result["out_trade_no"])), result, "支付宝异步通知")
        except PAYMENT_ERRORS:
            return Response("failure", status=400, mimetype="text/plain")
        return Response("success", mimetype="text/plain")

    return app


if __name__ == "__main__":
    create_app().run(host="127.0.0.1", port=int(os.getenv("PORT", "4244")))
