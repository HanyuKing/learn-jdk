import logging
import os
import sqlite3
import time
from contextlib import closing
from pathlib import Path

import stripe
from dotenv import load_dotenv
from flask import Flask, abort, jsonify, request
from werkzeug.exceptions import HTTPException


BASE_DIR = Path(__file__).resolve().parent
PRICES = {
    "usd": {"amount": 1250, "decimals": 2},
    "jpy": {"amount": 1500, "decimals": 0},
}


def create_app():
    load_dotenv(BASE_DIR / ".env")
    app = Flask(__name__, static_folder=BASE_DIR / "frontend", static_url_path="/assets")
    app.config["MAX_CONTENT_LENGTH"] = 256 * 1024
    app.logger.setLevel(logging.INFO)

    publishable_key = os.getenv("STRIPE_PUBLISHABLE_KEY", "")
    secret_key = os.getenv("STRIPE_SECRET_KEY", "")
    webhook_secret = os.getenv("STRIPE_WEBHOOK_SECRET", "")
    stripe_client = None
    if publishable_key or secret_key:
        sandbox_key_prefixes = ("sk_test_", "rk_test_", "rkcs_test_")
        if not publishable_key.startswith("pk_test_") or not secret_key.startswith(sandbox_key_prefixes):
            raise ValueError("请配置同一个 Stripe 沙箱的公开密钥和服务端密钥。")
        stripe_client = stripe.StripeClient(secret_key, max_network_retries=2)
    if webhook_secret and not webhook_secret.startswith("whsec_"):
        raise ValueError("STRIPE_WEBHOOK_SECRET 必须是 whsec_ 开头的签名密钥。")

    database = os.getenv("STRIPE_DEMO_DATABASE") or BASE_DIR / "orders.sqlite3"

    def connect():
        connection = sqlite3.connect(database)
        connection.row_factory = sqlite3.Row
        return connection

    with closing(connect()) as connection, connection:
        connection.execute("""
            CREATE TABLE IF NOT EXISTS orders (
                id TEXT PRIMARY KEY,
                currency TEXT NOT NULL,
                amount INTEGER NOT NULL,
                status TEXT NOT NULL DEFAULT 'awaiting_payment',
                created_at INTEGER NOT NULL,
                payment_intent_id TEXT UNIQUE,
                charge_id TEXT,
                paid_via TEXT
            )
        """)

    def find_order(order_id):
        with closing(connect()) as connection:
            return connection.execute("SELECT * FROM orders WHERE id = ?", (order_id,)).fetchone()

    def require_order(order_id):
        order = find_order(str(order_id))
        if order is None:
            abort(404, "找不到这笔本地订单。")
        return order

    def public_order(order, stripe_status=None):
        result = dict(order)
        result["stripe_status"] = "succeeded" if order["status"] == "paid" else stripe_status
        return result

    def mark_paid(order, intent, source):
        # Stripe 签名或者 API 查询证明对象的来源，本地数据用来校验这笔钱属于哪张订单。
        if (
            intent.get("livemode") is not False
            or intent.get("status") != "succeeded"
            or intent.get("id") != order["payment_intent_id"]
            or intent.get("metadata", {}).get("order_id") != order["id"]
            or intent.get("currency") != order["currency"]
            or intent.get("amount") != order["amount"]
            or intent.get("amount_received") != order["amount"]
        ):
            app.logger.warning("Payment mismatch for order_id=%s", order["id"])
            abort(409, "Stripe 支付结果与本地订单不一致。")

        # 条件更新让重复通知与同时查单只触发一次状态推进。
        with closing(connect()) as connection, connection:
            updated = connection.execute("""
                UPDATE orders SET status = 'paid', charge_id = ?, paid_via = ?
                WHERE id = ? AND payment_intent_id = ? AND status = 'awaiting_payment'
            """, (intent.get("latest_charge"), source, order["id"], intent["id"])).rowcount
        if updated:
            app.logger.info("Paid order_id=%s pi_id=%s source=%s", order["id"], intent["id"], source)
        return require_order(order["id"])

    @app.after_request
    def prevent_response_caching(response):
        response.headers["Cache-Control"] = "no-store"
        response.headers["Referrer-Policy"] = "no-referrer"
        return response

    @app.errorhandler(HTTPException)
    def handle_http_error(error):
        return jsonify(error=error.description), error.code

    @app.errorhandler(stripe.StripeError)
    def handle_stripe_error(error):
        app.logger.warning("Stripe request failed code=%s request_id=%s", error.code, error.request_id)
        return jsonify(error="Stripe 请求失败。请检查沙箱配置，稍后可重试同一笔订单。"), 502

    @app.get("/")
    def checkout():
        return app.send_static_file("index.html")

    @app.get("/api/config")
    def config():
        return jsonify(prices=PRICES, publishable_key=publishable_key, webhook_ready=bool(webhook_secret))

    @app.put("/api/orders/<uuid:order_id>")
    def create_order(order_id):
        if stripe_client is None:
            abort(503, "请先在 .env 配置 Stripe 沙箱密钥并重启。")
        data = request.get_json(silent=True)
        currency = data.get("currency") if isinstance(data, dict) else None
        if not isinstance(currency, str) or currency not in PRICES:
            abort(400, "请选择美元或日元。")

        order_id = str(order_id)
        with closing(connect()) as connection, connection:
            connection.execute("""
                INSERT INTO orders (id, currency, amount, created_at) VALUES (?, ?, ?, ?)
                ON CONFLICT(id) DO NOTHING
            """, (order_id, currency, PRICES[currency]["amount"], int(time.time())))

        order = require_order(order_id)
        if order["currency"] != currency:
            abort(409, "同一订单不能更换币种，请创建新订单。")
        if order["status"] == "paid":
            return jsonify(order=public_order(order), client_secret=None)

        if order["payment_intent_id"]:
            intent = stripe_client.v1.payment_intents.retrieve(order["payment_intent_id"])
        else:
            # Stripe 的幂等键至少保留 24 小时。超过窗口时先复核 Stripe 记录。
            if time.time() - order["created_at"] >= 23 * 60 * 60:
                abort(409, "这笔订单的创建重试窗口已过，请先在 Stripe 查对原订单。")
            intent = stripe_client.v1.payment_intents.create(
                {
                    "amount": order["amount"],
                    "currency": currency,
                    "automatic_payment_methods": {"enabled": True},
                    "metadata": {"order_id": order_id, "integration": "order-java-demo"},
                },
                options={"idempotency_key": f"order-java-demo:{order_id}"},
            )
            with closing(connect()) as connection, connection:
                connection.execute("""
                    UPDATE orders SET payment_intent_id = ? WHERE id = ? AND payment_intent_id IS NULL
                """, (intent.id, order_id))
            order = require_order(order_id)
            if order["payment_intent_id"] != intent.id:
                abort(409, "这笔订单已经绑定其他支付，请查对 Stripe 记录。")

        if intent.status == "succeeded":
            order = mark_paid(order, intent.to_dict(), "query")
        secret = intent.client_secret if intent.status not in {"succeeded", "canceled"} else None
        return jsonify(order=public_order(order, intent.status), client_secret=secret)

    @app.get("/api/orders/<uuid:order_id>")
    def get_order(order_id):
        return jsonify(public_order(require_order(order_id)))

    @app.post("/api/orders/<uuid:order_id>/sync")
    def sync_order(order_id):
        order = require_order(order_id)
        if order["status"] == "paid" or order["payment_intent_id"] is None:
            return jsonify(public_order(order))
        if stripe_client is None:
            abort(503, "Stripe 沙箱密钥未配置。")

        intent = stripe_client.v1.payment_intents.retrieve(order["payment_intent_id"])
        if intent.status == "succeeded":
            order = mark_paid(order, intent.to_dict(), "query")
        return jsonify(public_order(order, intent.status))

    @app.post("/api/stripe/webhook")
    def webhook():
        if not webhook_secret:
            abort(503, "Webhook 签名密钥未配置。")
        try:
            event = stripe.Webhook.construct_event(
                request.get_data(), request.headers.get("Stripe-Signature", ""), webhook_secret,
            )
        except (ValueError, stripe.SignatureVerificationError):
            abort(400, "Webhook 签名或请求体无效。")

        if event.type == "payment_intent.succeeded":
            intent = event.data.object.to_dict()
            order_id = intent.get("metadata", {}).get("order_id")
            order = find_order(order_id) if order_id else None
            if order is not None:
                mark_paid(order, intent, "webhook")
            else:
                app.logger.info("Ignored Stripe event without matching local order event_id=%s", event.id)
        return "", 204

    return app


if __name__ == "__main__":
    # Stripe 跳转回页面时可能携带 client secret，避免在开发服务器访问日志记录 URL。
    logging.getLogger("werkzeug").setLevel(logging.WARNING)
    create_app().run(host="127.0.0.1", port=int(os.getenv("PORT", "4242")))
