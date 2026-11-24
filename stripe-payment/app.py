import json
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
    "usd": {"amount": 1250, "decimals": 2, "country": "US"},
    "jpy": {"amount": 1500, "decimals": 0, "country": "JP"},
}
CHECKOUT_TAX_CASES = {
    "stripe_manual": "Stripe 手动税率",
    "stripe_auto": "Stripe Tax 自动算税",
}
GUANGZHOU_TAX_CASE = "stripe_guangzhou"
TAX_CASES = {**CHECKOUT_TAX_CASES, GUANGZHOU_TAX_CASE: "Stripe Tax · 广州发货"}
GUANGZHOU_SHIP_FROM = {"country": "CN", "city": "Guangzhou"}


def normalize_shipping(shipping, country):
    if not isinstance(shipping, dict) or not isinstance(shipping.get("address"), dict):
        abort(400, "请填写收货地址。")
    name = shipping.get("name")
    address = shipping["address"]
    if address.get("country") != country:
        abort(400, "收货国家必须与示例币种匹配。")
    fields = ("line1", "line2", "city", "state", "postal_code")
    if not isinstance(name, str) or not name.strip() or len(name) > 200 or any(
        not isinstance(address[field], str) or len(address[field]) > 200
        for field in fields
        if address.get(field) is not None
    ):
        abort(400, "请填写有效的收件人姓名和地址。")
    normalized = {field: (address.get(field) or "").strip() for field in fields}
    if any(not normalized[field] for field in ("line1", "city", "state", "postal_code")):
        abort(400, "请填写完整的街道、城市、州或都道府县和邮编。")
    normalized["country"] = country
    return json.dumps({"name": name.strip(), "address": normalized}, sort_keys=True, ensure_ascii=False)


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

    manual_tax_ids = {
        "usd": os.getenv("STRIPE_US_TAX_RATE_ID", ""),
        "jpy": os.getenv("STRIPE_JP_TAX_RATE_ID", ""),
    }
    checkout_origin = f"http://127.0.0.1:{int(os.getenv('PORT', '4242'))}"
    database = os.getenv("STRIPE_DEMO_DATABASE") or BASE_DIR / "orders.sqlite3"

    def connect():
        connection = sqlite3.connect(database)
        connection.row_factory = sqlite3.Row
        return connection

    with closing(connect()) as connection, connection:
        # 多个进程同时启动时，只允许一个进程检查和升级 SQLite 表。
        connection.execute("BEGIN IMMEDIATE")
        connection.execute("""
            CREATE TABLE IF NOT EXISTS orders (
                id TEXT PRIMARY KEY,
                currency TEXT NOT NULL,
                amount INTEGER NOT NULL,
                status TEXT NOT NULL DEFAULT 'awaiting_payment',
                created_at INTEGER NOT NULL,
                payment_intent_id TEXT UNIQUE,
                charge_id TEXT,
                paid_via TEXT,
                tax_case TEXT,
                tax_rate_bps INTEGER NOT NULL DEFAULT 0,
                tax_amount INTEGER NOT NULL DEFAULT 0,
                checkout_session_id TEXT,
                stripe_tax_rate_id TEXT,
                checkout_ui_mode TEXT,
                shipping_details TEXT,
                tax_calculation_id TEXT,
                tax_calculation_expires_at INTEGER
            )
        """)
        columns = {column["name"] for column in connection.execute("PRAGMA table_info(orders)")}
        for name, definition in (
            ("tax_case", "TEXT"),
            ("tax_rate_bps", "INTEGER NOT NULL DEFAULT 0"),
            ("tax_amount", "INTEGER NOT NULL DEFAULT 0"),
            ("checkout_session_id", "TEXT"),
            ("stripe_tax_rate_id", "TEXT"),
            ("checkout_ui_mode", "TEXT"),
            ("shipping_details", "TEXT"),
            ("tax_calculation_id", "TEXT"),
            ("tax_calculation_expires_at", "INTEGER"),
        ):
            if name not in columns:
                connection.execute(f"ALTER TABLE orders ADD COLUMN {name} {definition}")
        connection.execute("""
            CREATE UNIQUE INDEX IF NOT EXISTS orders_checkout_session ON orders (checkout_session_id)
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
        result.pop("shipping_details")
        result["subtotal"] = order["amount"] - order["tax_amount"]
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

    def checkout_order(order, session, source):
        tax_amount = (session.get("total_details") or {}).get("amount_tax")
        total = session.get("amount_total")
        subtotal = order["amount"] - order["tax_amount"]
        payment_intent_id = session.get("payment_intent")
        is_paid = session.get("payment_status") == "paid"
        if (
            session.get("livemode") is not False
            or session.get("mode") != "payment"
            or session.get("id") != order["checkout_session_id"]
            or session.get("client_reference_id") != order["id"]
            or session.get("currency") != order["currency"]
            or session.get("amount_subtotal") != subtotal
            or not isinstance(tax_amount, int)
            or tax_amount < 0
            or total != subtotal + tax_amount
            or (is_paid and (session.get("status") != "complete" or not payment_intent_id))
        ):
            abort(409, "Stripe Checkout 金额或订单信息不一致。请检查税率是否设置为价外加收。")

        with closing(connect()) as connection, connection:
            connection.execute("""
                UPDATE orders
                SET amount = ?, tax_amount = ?, payment_intent_id = ?, status = ?, paid_via = ?
                WHERE id = ? AND checkout_session_id = ? AND status = 'awaiting_payment'
            """, (total, tax_amount, payment_intent_id,
                  "paid" if is_paid else "awaiting_payment", source if is_paid else None,
                  order["id"], session["id"]))
        return require_order(order["id"])

    def require_retry_window(order):
        # Stripe 的幂等键至少保留 24 小时。超过窗口时先复核 Stripe 记录。
        if time.time() - order["created_at"] >= 23 * 60 * 60:
            abort(409, "这笔订单的创建重试窗口已过，请先在 Stripe 查对原订单。")

    def prepare_guangzhou_tax(order):
        address = json.loads(order["shipping_details"])["address"]
        # 日本的税务计算使用国家级税率，收货省府仍保留在 PaymentIntent 的配送地址中。
        if address["country"] == "JP":
            address.pop("state")
        calculation = stripe_client.v1.tax.calculations.create(
            {
                "currency": order["currency"],
                "line_items": [{
                    "amount": order["amount"], "reference": order["id"],
                    "tax_code": "txcd_99999999", "tax_behavior": "exclusive",
                }],
                "customer_details": {"address": address, "address_source": "shipping"},
                "ship_from_details": {"address": GUANGZHOU_SHIP_FROM},
            },
            options={"idempotency_key": f"order-java-demo:tax:{order['id']}"},
        )
        if (
            calculation.livemode is not False
            or calculation.currency != order["currency"]
            or calculation.tax_amount_exclusive < 0
            or calculation.tax_amount_inclusive != 0
            or calculation.amount_total != order["amount"] + calculation.tax_amount_exclusive
            or not isinstance(calculation.expires_at, int)
        ):
            abort(502, "Stripe Tax 返回的税额或总额不一致。")
        with closing(connect()) as connection, connection:
            connection.execute("""
                UPDATE orders SET amount = ?, tax_amount = ?, tax_calculation_id = ?, tax_calculation_expires_at = ?
                WHERE id = ? AND tax_calculation_id IS NULL
            """, (calculation.amount_total, calculation.tax_amount_exclusive,
                  calculation.id, calculation.expires_at, order["id"]))
        return require_order(order["id"])

    def prepare_checkout(order):
        if order["checkout_session_id"]:
            session = stripe_client.v1.checkout.sessions.retrieve(order["checkout_session_id"])
        else:
            require_retry_window(order)
            currency = order["currency"]
            is_automatic = order["tax_case"] == "stripe_auto"
            line_item = {
                "quantity": 1,
                "price_data": {
                    "currency": currency,
                    "unit_amount": order["amount"],
                    "tax_behavior": "exclusive",
                    "product_data": {"name": "定制树脂商品", "tax_code": "txcd_99999999"},
                },
            }
            if not is_automatic:
                line_item["tax_rates"] = [order["stripe_tax_rate_id"]]
            return_url = f"{checkout_origin}/?order_id={order['id']}&checkout="
            # 已创建的订单沿用原来的会话参数，保持 Stripe 幂等重试一致。
            if order["checkout_ui_mode"] == "elements":
                navigation = {"ui_mode": "elements", "return_url": return_url + "success"}
            else:
                navigation = {"success_url": return_url + "success", "cancel_url": return_url + "cancel"}
            session = stripe_client.v1.checkout.sessions.create(
                {
                    "mode": "payment",
                    "line_items": [line_item],
                    "automatic_tax": {"enabled": is_automatic},
                    "adaptive_pricing": {"enabled": False},
                    "shipping_address_collection": {"allowed_countries": [PRICES[currency]["country"]]},
                    "client_reference_id": order["id"],
                    "payment_intent_data": {"metadata": {"order_id": order["id"], "tax_case": order["tax_case"]}},
                    **navigation,
                },
                options={"idempotency_key": f"order-java-demo:checkout:{order['id']}"},
            )
            with closing(connect()) as connection, connection:
                connection.execute("""
                    UPDATE orders SET checkout_session_id = ? WHERE id = ? AND checkout_session_id IS NULL
                """, (session.id, order["id"]))
            order = require_order(order["id"])

        session_data = session.to_dict()
        order = checkout_order(order, session_data, "query")
        can_open = session.status == "open" and order["status"] != "paid"
        return jsonify(
            order=public_order(order, checkout_status(session)),
            checkout_url=session_data.get("url") if can_open else None,
            checkout_client_secret=session_data.get("client_secret") if can_open else None,
            client_secret=None,
        )

    def checkout_status(session):
        if session.status == "expired":
            return "canceled"
        if session.status == "complete":
            if session.payment_status == "unpaid" and session.payment_intent:
                intent = stripe_client.v1.payment_intents.retrieve(session.payment_intent)
                if intent.status in {"requires_payment_method", "canceled"}:
                    return "canceled"
            return "processing"
        return "requires_payment_method"

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
        if isinstance(error, stripe.InvalidRequestError) and "head office address" in (error.user_message or "").lower():
            return jsonify(error="请先在 Stripe 沙箱的 Tax 设置填写营业地址，再试自动算税。"), 503
        if error.code in {"customer_tax_location_invalid", "shipping_address_invalid"}:
            return jsonify(error="Stripe 无法识别收货地址，请核对后创建新订单。"), 400
        return jsonify(error="Stripe 请求失败。请检查沙箱配置，稍后可重试同一笔订单。"), 502

    @app.get("/")
    def checkout():
        return app.send_static_file("index.html")

    @app.get("/api/config")
    def config():
        return jsonify(
            prices=PRICES, tax_cases=TAX_CASES,
            publishable_key=publishable_key, webhook_ready=bool(webhook_secret),
        )

    @app.put("/api/orders/<uuid:order_id>")
    def create_order(order_id):
        if stripe_client is None:
            abort(503, "请先在 .env 配置 Stripe 沙箱密钥并重启。")
        data = request.get_json(silent=True)
        currency = data.get("currency") if isinstance(data, dict) else None
        if not isinstance(currency, str) or currency not in PRICES:
            abort(400, "请选择美元或日元。")

        tax_case_id = data.get("tax_case")
        if tax_case_id is not None and not isinstance(tax_case_id, str):
            abort(400, "请选择有效的税费场景。")
        shipping = None
        if tax_case_id == GUANGZHOU_TAX_CASE and data.get("shipping") is not None:
            shipping = normalize_shipping(data["shipping"], PRICES[currency]["country"])
        order_id = str(order_id)
        order = find_order(order_id)
        if order is None:
            if tax_case_id is not None and tax_case_id not in TAX_CASES:
                abort(400, "请选择有效的税费场景。")
            if tax_case_id == GUANGZHOU_TAX_CASE and shipping is None:
                abort(400, "请先填写收货地址，再计算广州发货订单的税费。")
            tax_rate_id = None
            if tax_case_id == "stripe_manual":
                tax_rate_id = manual_tax_ids[currency]
                if not tax_rate_id.startswith("txr_"):
                    abort(503, "请按 README 在 Stripe 沙箱新建手动税率，并在 .env 配置对应的 txr_ ID。")
            with closing(connect()) as connection, connection:
                connection.execute("""
                    INSERT INTO orders (
                        id, currency, amount, created_at, tax_case, stripe_tax_rate_id, checkout_ui_mode, shipping_details
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    ON CONFLICT(id) DO NOTHING
                """, (order_id, currency, PRICES[currency]["amount"], int(time.time()), tax_case_id,
                      tax_rate_id, "elements" if tax_case_id in CHECKOUT_TAX_CASES else None, shipping))
            order = require_order(order_id)

        if order["currency"] != currency:
            abort(409, "同一订单不能更换币种，请创建新订单。")
        if order["tax_case"] != tax_case_id:
            abort(409, "同一订单不能更换税费场景，请创建新订单。")
        if shipping is not None and shipping != order["shipping_details"]:
            abort(409, "同一订单不能更换收货地址，请创建新订单。")
        if order["status"] == "paid":
            return jsonify(order=public_order(order), client_secret=None)
        if order["tax_case"] in CHECKOUT_TAX_CASES:
            return prepare_checkout(order)

        if order["payment_intent_id"]:
            intent = stripe_client.v1.payment_intents.retrieve(order["payment_intent_id"])
        else:
            require_retry_window(order)
            metadata = {"order_id": order_id, "integration": "order-java-demo"}
            if order["tax_case"] == GUANGZHOU_TAX_CASE:
                if order["tax_calculation_id"] is None:
                    order = prepare_guangzhou_tax(order)
                if time.time() >= order["tax_calculation_expires_at"]:
                    abort(409, "税费报价已过期，请创建新订单。")
                metadata.update(tax_case=GUANGZHOU_TAX_CASE, tax_amount=str(order["tax_amount"]))
                tax_details = {
                    "shipping": json.loads(order["shipping_details"]),
                    "hooks": {"inputs": {"tax": {"calculation": order["tax_calculation_id"]}}},
                }
            else:
                tax_details = {}
            if order["tax_case"] not in {None, GUANGZHOU_TAX_CASE}:
                metadata.update(
                    tax_case=order["tax_case"],
                    tax_rate_bps=str(order["tax_rate_bps"]),
                    tax_amount=str(order["tax_amount"]),
                )
            intent = stripe_client.v1.payment_intents.create(
                {
                    "amount": order["amount"],
                    "currency": currency,
                    "automatic_payment_methods": {"enabled": True},
                    "metadata": metadata,
                    **tax_details,
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
        if (
            intent.status not in {"succeeded", "canceled", "processing"}
            and order["tax_calculation_expires_at"] is not None
            and time.time() >= order["tax_calculation_expires_at"]
        ):
            abort(409, "税费报价已过期，请创建新订单。")
        secret = intent.client_secret if intent.status not in {"succeeded", "canceled"} else None
        return jsonify(order=public_order(order, intent.status), client_secret=secret)

    @app.get("/api/orders/<uuid:order_id>")
    def get_order(order_id):
        return jsonify(public_order(require_order(order_id)))

    @app.post("/api/orders/<uuid:order_id>/sync")
    def sync_order(order_id):
        order = require_order(order_id)
        if order["status"] == "paid" or not (order["payment_intent_id"] or order["checkout_session_id"]):
            return jsonify(public_order(order))
        if stripe_client is None:
            abort(503, "Stripe 沙箱密钥未配置。")

        if order["checkout_session_id"]:
            session = stripe_client.v1.checkout.sessions.retrieve(order["checkout_session_id"])
            order = checkout_order(order, session.to_dict(), "query")
            return jsonify(public_order(order, checkout_status(session)))

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
            if order is not None and order["tax_case"] not in CHECKOUT_TAX_CASES:
                mark_paid(order, intent, "webhook")
            elif order is None:
                app.logger.info("Ignored Stripe event without matching local order event_id=%s", event.id)
        elif event.type in {"checkout.session.completed", "checkout.session.async_payment_succeeded"}:
            session = event.data.object.to_dict()
            order_id = session.get("client_reference_id")
            order = find_order(order_id) if order_id else None
            if order is not None and order["checkout_session_id"] == session.get("id"):
                checkout_order(order, session, "webhook")
        return "", 204

    return app


if __name__ == "__main__":
    # Stripe 跳转回页面时可能携带 client secret，避免在开发服务器访问日志记录 URL。
    logging.getLogger("werkzeug").setLevel(logging.WARNING)
    create_app().run(host="127.0.0.1", port=int(os.getenv("PORT", "4242")))
