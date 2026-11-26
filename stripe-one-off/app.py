import hmac
import logging
import os
import secrets
import sqlite3
import time
from contextlib import closing
from decimal import Decimal
from pathlib import Path
from uuid import uuid4

import stripe
from dotenv import load_dotenv
from flask import Flask, abort, redirect, render_template, request, session, url_for
from werkzeug.exceptions import HTTPException


BASE_DIR = Path(__file__).resolve().parent
DEMO = "stripe-one-off-demo"
CHECKOUT_EVENTS = {
    "checkout.session.completed",
    "checkout.session.async_payment_succeeded",
    "checkout.session.async_payment_failed",
    "checkout.session.expired",
}


def create_app():
    load_dotenv(BASE_DIR / ".env")
    app = Flask(__name__)
    app.config.update(
        SECRET_KEY=os.getenv("FLASK_SECRET_KEY") or secrets.token_hex(32),
        SESSION_COOKIE_NAME="stripe_one_off_demo",
        SESSION_COOKIE_SAMESITE="Lax",
        MAX_CONTENT_LENGTH=512 * 1024,
    )
    port = int(os.getenv("PORT", "4244"))
    origin = f"http://127.0.0.1:{port}"
    database = os.getenv("STRIPE_ONE_OFF_DATABASE") or BASE_DIR / "orders.sqlite3"
    key = os.getenv("STRIPE_SECRET_KEY", "")
    price_id = os.getenv("STRIPE_ONE_OFF_PRICE_ID", "")
    webhook_secret = os.getenv("STRIPE_WEBHOOK_SECRET", "")
    if key and not key.startswith(("sk_test_", "rk_test_", "rkcs_test_")):
        raise ValueError("这个 Demo 需要 Stripe 沙箱密钥。")
    if webhook_secret and not webhook_secret.startswith("whsec_"):
        raise ValueError("STRIPE_WEBHOOK_SECRET 应以 whsec_ 开头。")
    client = None
    if key:
        client = stripe.StripeClient(
            key, max_network_retries=1, http_client=stripe.RequestsClient(timeout=(4, 6)),
        )

    def connect():
        db = sqlite3.connect(database)
        db.row_factory = sqlite3.Row
        return db

    with closing(connect()) as db, db:
        db.execute("""
            CREATE TABLE IF NOT EXISTS orders (
                id TEXT PRIMARY KEY,
                buyer_id TEXT NOT NULL,
                price_id TEXT NOT NULL,
                product_name TEXT NOT NULL,
                amount INTEGER NOT NULL,
                currency TEXT NOT NULL,
                created_at INTEGER NOT NULL,
                status TEXT NOT NULL DEFAULT 'pending',
                checkout_id TEXT UNIQUE,
                payment_intent_id TEXT,
                paid_via TEXT
            )
        """)

    @app.after_request
    def private_response(response):
        response.headers["Cache-Control"] = "no-store"
        response.headers["Referrer-Policy"] = "no-referrer"
        return response

    @app.template_filter("money")
    def money(record):
        currency = record["currency"]
        if currency == "usd":
            return f"US${Decimal(record['amount']) / 100:,.2f}"
        if currency == "jpy":
            return f"JPY {record['amount']:,}"
        return f"{record['amount']:,} {currency.upper()} 最小货币单位"

    def require_stripe():
        if client is None:
            abort(503, "请在 .env 填写 Stripe 沙箱密钥后重启服务。")

    def require_form():
        buyer_id = session.get("buyer_id")
        token = session.get("form_token")
        submitted = request.form.get("token", "")
        if not buyer_id or not token or not hmac.compare_digest(token.encode(), submitted.encode()):
            abort(400, "表单已过期，请刷新页面后再试。")
        require_stripe()
        return buyer_id

    def offer():
        require_stripe()
        if not price_id.startswith("price_"):
            abort(503, "请在 .env 填写 STRIPE_ONE_OFF_PRICE_ID 后重启服务。")
        price = client.v1.prices.retrieve(price_id, {"expand": ["product"]}).to_dict()
        product = price["product"]
        if (price["livemode"] or price["type"] != "one_time" or not price["active"]
                or not isinstance(product, dict) or not product.get("active")
                or price.get("billing_scheme") != "per_unit" or (price.get("unit_amount") or 0) <= 0):
            abort(409, "请选择有效的 One-off 固定价格，并确认商品和价格均已启用。")
        return {"price_id": price["id"], "product_name": product["name"],
                "amount": price["unit_amount"], "currency": price["currency"]}

    def find_order(order_id):
        with closing(connect()) as db:
            return db.execute("SELECT * FROM orders WHERE id = ?", (str(order_id),)).fetchone()

    def owned_order(order_id):
        order = find_order(order_id)
        if order is None or order["buyer_id"] != session.get("buyer_id"):
            abort(404, "这个浏览器找不到该订单。")
        return order

    def reconcile(order, checkout, source, payment_failed=False):
        metadata = checkout.get("metadata") or {}
        if (checkout.get("livemode") is not False or checkout.get("mode") != "payment"
                or checkout.get("id") != order["checkout_id"]
                or checkout.get("client_reference_id") != order["id"]
                or metadata.get("demo") != DEMO or checkout.get("currency") != order["currency"]
                or checkout.get("amount_total") != order["amount"]
                or checkout.get("amount_subtotal") != order["amount"]):
            abort(409, "Stripe 会话与本地订单的编号、币种或金额不一致。")

        state = checkout.get("status")
        intent_id = checkout.get("payment_intent")
        with closing(connect()) as db, db:
            if checkout.get("payment_status") == "paid":
                if state != "complete" or not intent_id:
                    abort(409, "Stripe 尚未返回完整的付款记录。")
                db.execute("""
                    UPDATE orders SET status = 'paid', payment_intent_id = ?, paid_via = ?
                    WHERE id = ? AND status != 'paid'
                """, (intent_id, source, order["id"]))
            elif state in {"complete", "expired"}:
                status = "expired" if state == "expired" else ("failed" if payment_failed else "processing")
                db.execute("""
                    UPDATE orders SET status = ?, payment_intent_id = ?
                    WHERE id = ? AND status IN ('pending', 'processing')
                """, (status, intent_id, order["id"]))

    @app.errorhandler(HTTPException)
    def http_error(error):
        if request.path == "/webhook":
            return {"error": error.description}, error.code
        return render_template("index.html", error=error.description), error.code

    @app.errorhandler(stripe.StripeError)
    def stripe_error(error):
        app.logger.warning("Stripe request failed code=%s request_id=%s", error.code, error.request_id)
        message = "Stripe 请求失败，请核对沙箱密钥和价格配置后重试。"
        if request.path == "/webhook":
            return {"error": message}, 502
        return render_template("index.html", error=message), 502

    @app.get("/")
    def index():
        if "buyer_id" not in session:
            session["buyer_id"] = str(uuid4())
            session["form_token"] = secrets.token_urlsafe(32)
        with closing(connect()) as db:
            recent = db.execute("""
                SELECT id FROM orders WHERE buyer_id = ? ORDER BY created_at DESC, rowid DESC LIMIT 1
            """, (session["buyer_id"],)).fetchone()
        product = offer() if client and price_id else None
        return render_template("index.html", product=product, order_id=uuid4(), recent=recent,
                               webhook_ready=bool(webhook_secret))

    @app.post("/orders/<uuid:order_id>/checkout")
    def start_checkout(order_id):
        buyer_id = require_form()
        order_id = str(order_id)
        order = find_order(order_id)
        if order is None:
            product = offer()
            with closing(connect()) as db, db:
                db.execute("""
                    INSERT OR IGNORE INTO orders (id, buyer_id, price_id, product_name, amount, currency, created_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                """, (order_id, buyer_id, product["price_id"], product["product_name"],
                      product["amount"], product["currency"], int(time.time())))
        order = owned_order(order_id)
        receipt_path = url_for("receipt", order_id=order_id)
        if order["status"] != "pending":
            return redirect(receipt_path, code=303)
        if order["checkout_id"]:
            checkout = client.v1.checkout.sessions.retrieve(order["checkout_id"])
        else:
            if time.time() - order["created_at"] >= 23 * 3600:
                abort(409, "原结账请求已超过重试窗口，请在 Stripe 沙箱核对后再购买。")
            checkout = client.v1.checkout.sessions.create({
                "mode": "payment",
                "line_items": [{"price": order["price_id"], "quantity": 1}],
                "client_reference_id": order_id,
                "metadata": {"demo": DEMO},
                "adaptive_pricing": {"enabled": False},
                "payment_method_options": {"wechat_pay": {"client": "web"}},
                "success_url": origin + receipt_path + "/return?session_id={CHECKOUT_SESSION_ID}",
                "cancel_url": origin + receipt_path,
            }, options={"idempotency_key": f"{DEMO}:{order_id}"})
            with closing(connect()) as db, db:
                db.execute("UPDATE orders SET checkout_id = ? WHERE id = ? AND checkout_id IS NULL",
                           (checkout.id, order_id))
            order = owned_order(order_id)
        reconcile(order, checkout.to_dict(), "query")
        if checkout.status == "open" and checkout.url:
            return redirect(checkout.url, code=303)
        return redirect(receipt_path, code=303)

    @app.get("/orders/<uuid:order_id>")
    def receipt(order_id):
        return render_template("index.html", order=owned_order(order_id))

    def refresh_order(order):
        checkout = client.v1.checkout.sessions.retrieve(order["checkout_id"]).to_dict()
        failed = False
        if checkout.get("status") == "complete" and checkout.get("payment_status") == "unpaid":
            intent_id = checkout.get("payment_intent")
            if intent_id:
                intent = client.v1.payment_intents.retrieve(intent_id)
                failed = intent.status in {"requires_payment_method", "canceled"}
        reconcile(order, checkout, "query", payment_failed=failed)

    @app.get("/orders/<uuid:order_id>/return")
    def checkout_return(order_id):
        require_stripe()
        order = owned_order(order_id)
        if not order["checkout_id"] or request.args.get("session_id") != order["checkout_id"]:
            abort(404, "该 Checkout 会话与订单不匹配。")
        if order["status"] != "paid":
            refresh_order(order)
        return redirect(url_for("receipt", order_id=order_id))

    @app.post("/orders/<uuid:order_id>/sync")
    def sync(order_id):
        require_form()
        order = owned_order(order_id)
        if order["checkout_id"] and order["status"] != "paid":
            refresh_order(order)
        return redirect(url_for("receipt", order_id=order_id), code=303)

    @app.post("/webhook")
    def webhook():
        if not webhook_secret:
            abort(503, "请先配置 Webhook 签名密钥。")
        try:
            event = stripe.Webhook.construct_event(
                request.get_data(), request.headers.get("Stripe-Signature", ""), webhook_secret,
            )
        except (ValueError, stripe.SignatureVerificationError):
            abort(400, "Webhook 签名或请求体无效。")
        if event.livemode:
            abort(400, "这个 Demo 只接受沙箱事件。")
        if event.type in CHECKOUT_EVENTS:
            checkout = event.data.object.to_dict()
            if (checkout.get("metadata") or {}).get("demo") == DEMO:
                order = find_order(checkout.get("client_reference_id"))
                if order is not None and not order["checkout_id"]:
                    abort(503, "本地会话仍在保存，请稍后重试。")
                if order is not None:
                    reconcile(order, checkout, "webhook", event.type == "checkout.session.async_payment_failed")
        return "", 204

    return app


if __name__ == "__main__":
    logging.getLogger("werkzeug").setLevel(logging.WARNING)
    create_app().run(host="127.0.0.1", port=int(os.getenv("PORT", "4244")))
