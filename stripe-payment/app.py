import json
import logging
import os
import sqlite3
import time
from collections import OrderedDict
from contextlib import closing
from decimal import Decimal
from pathlib import Path
from threading import Lock

import stripe
from dotenv import load_dotenv
from flask import Flask, abort, jsonify, request
from werkzeug.exceptions import HTTPException


BASE_DIR = Path(__file__).resolve().parent
PRICES = {
    "usd": {"amount": 1250, "decimals": 2, "country": "US"},
    "jpy": {"amount": 1500, "decimals": 0, "country": "JP"},
}
# 已创建的自动算税订单仍通过原来的 Checkout 会话结算。
CHECKOUT_TAX_CASES = {"stripe_manual", "stripe_auto"}
GUANGZHOU_TAX_CASE = "stripe_guangzhou"
TAX_CASES = {
    GUANGZHOU_TAX_CASE: "Stripe Tax · 广州发货",
    "stripe_manual": "Stripe 手动税率",
}
GUANGZHOU_SHIP_FROM = {
    "country": "CN", "state": "44", "city": "Guangzhou",
    "postal_code": "510000", "line1": "Guangzhou fulfillment center",
}
GUANGZHOU_PRODUCT_TAX_CODE = "txcd_99999999"
GUANGZHOU_PRICING = {
    "usd": {"product": 10000, "discount": 1000, "coupon": 500, "shipping": 500},
    "jpy": {"product": 15000, "discount": 1500, "coupon": 750, "shipping": 750},
}
PRODUCTS = {
    "custom-figure": {
        "name": "定制树脂手办",
        "description": "适合桌面陈列的定制树脂摆件。",
        "prices": {currency: price["product"] for currency, price in GUANGZHOU_PRICING.items()},
    },
    "resin-keychain": {
        "name": "树脂挂件",
        "description": "带金属扣的轻巧树脂挂件，适合挂在包上或钥匙圈上。",
        "prices": {"usd": 3200, "jpy": 4800},
    },
    "resin-desk-ornament": {
        "name": "树脂桌面摆件",
        "description": "小尺寸树脂摆件，适合书桌或书架。",
        "prices": {"usd": 6800, "jpy": 10200},
    },
}
MAX_ITEM_QUANTITY = 9
SHIPPING_METHODS = {
    "STANDARD": {
        "label": "标准配送",
        "amounts": {currency: pricing["shipping"] for currency, pricing in GUANGZHOU_PRICING.items()},
    },
    "EXPRESS": {"label": "加急配送", "amounts": {"usd": 2000}},
    "FREE": {"label": "免运费配送", "amounts": dict.fromkeys(PRICES, 0)},
}
PROMOTION_CODE = "PROMO_EXAMPLE"
IMPORT_FEES_NOTICE_KEY = "builda.checkout.import_fees_notice"
TAX_DETAIL_EXPANSIONS = ["line_items", "line_items.data.tax_breakdown", "shipping_cost.tax_breakdown"]


def cart_quantities(items):
    if not isinstance(items, list) or not 1 <= len(items) <= len(PRODUCTS):
        abort(400, "请先将商品加入购物车。")
    quantities = {}
    for item in items:
        sku = item.get("sku") if isinstance(item, dict) else None
        quantity = item.get("quantity") if isinstance(item, dict) else None
        if (not isinstance(sku, str) or sku not in PRODUCTS or sku in quantities
                or type(quantity) is not int or not 1 <= quantity <= MAX_ITEM_QUANTITY):
            abort(400, f"请选择有效商品，每种商品可购买 1 至 {MAX_ITEM_QUANTITY} 件。")
        quantities[sku] = quantity
    return quantities


def guangzhou_options(data, currency):
    method = data.get("shippingMethodCode", "STANDARD")
    code = data.get("promotionCode", "")
    if not isinstance(method, str) or currency not in SHIPPING_METHODS.get(method, {}).get("amounts", {}):
        abort(400, "请选择当前币种可用的配送方式。")
    if not isinstance(code, str) or len(code) > 80:
        abort(400, "请输入有效的优惠码。")
    return method, code.strip()


def guangzhou_pricing(currency, data, has_address=True):
    method, code = guangzhou_options(data, currency)
    sample = GUANGZHOU_PRICING[currency]
    pricing = {
        "product": sample["product"],
        "discount": sample["discount"],
        "coupon": sample["coupon"] if code == PROMOTION_CODE else 0,
        "shipping": SHIPPING_METHODS[method]["amounts"][currency] if has_address else 0,
        "shipping_method": method,
        "promotion_code": code,
    }
    if "items" in data:
        quantities = cart_quantities(data["items"])
        items = [
            {"sku": sku, "name": product["name"], "quantity": quantities[sku], "unit_amount": product["prices"][currency]}
            for sku, product in PRODUCTS.items() if sku in quantities
        ]
        pricing["items"] = items
        pricing["product"] = sum(item["unit_amount"] * item["quantity"] for item in items)
        pricing["discount"] = sum((item["unit_amount"] // 10) * item["quantity"] for item in items)
        pricing["coupon"] = min(pricing["coupon"], pricing["product"] - pricing["discount"])
    return pricing


def tax_breakdown(calculation):
    items = []
    for tax in calculation.get("tax_breakdown", []):
        details = tax.get("tax_rate_details") or {}
        items.append({
            "type": "sales_tax",
            "rate": round(float(details.get("percentage_decimal") or 0) / 100, 6),
            "amount": tax["amount"],
            "displayName": details.get("state") or details.get("country") or details.get("tax_type") or "sales_tax",
            "reason": tax.get("taxability_reason"),
        })
    return items


def tax_component(item, divisor):
    breakdown = item.get("tax_breakdown")
    rate = None
    region = None
    exempt = False
    if breakdown:
        # Stripe 返回州、市和特殊辖区各自的税率。按相同税基合并，税额直接取自 Stripe。
        rated_entries = [tax for tax in breakdown if tax.get("tax_rate_details")]
        if all(
            tax["tax_rate_details"].get("percentage_decimal") is not None
            and tax["taxable_amount"] == item["amount"]
            for tax in rated_entries
        ) and (rated_entries or item["amount_tax"] == 0):
            percentage = sum(Decimal(tax["tax_rate_details"]["percentage_decimal"]) for tax in rated_entries)
            rate = float(percentage / 100)
        jurisdictions = [tax["jurisdiction"] for tax in (rated_entries or breakdown) if tax.get("jurisdiction")]
        regions = {jurisdiction.get("state") or jurisdiction.get("country") for jurisdiction in jurisdictions}
        if len(regions) == 1:
            region, = regions
        exempt = rate == 0 and any(tax.get("taxability_reason") == "product_exempt" for tax in breakdown)
    return {"amount": item["amount_tax"] / divisor, "rate": rate, "region": region, "exempt": exempt}


def calculation_tax_details(calculation, divisor):
    line_items = (calculation.get("line_items") or {}).get("data", [])
    merchandise = next((item for item in line_items if item["reference"] == "merchandise"), None)
    if merchandise is None:
        return None
    shipping = calculation.get("shipping_cost")
    return {
        "merchandise": tax_component(merchandise, divisor),
        "shipping": tax_component(shipping, divisor) if shipping else None,
    }


def public_quote(pricing, currency, status, tax_amount=0, calculation_id=None, breakdown=(), details=None):
    divisor = 10 ** PRICES[currency]["decimals"]
    pretax = pricing["product"] - pricing["discount"] - pricing["coupon"] + pricing["shipping"]
    result = {
        "currency": currency,
        "productTotalPrice": pricing["product"] / divisor,
        "discount": pricing["discount"] / divisor,
        "promotionCodeDiscountAmount": pricing["coupon"] / divisor,
        "promotionCodeStatus": 0 if pricing["promotion_code"] == PROMOTION_CODE else 2,
        "shippingFee": pricing["shipping"] / divisor,
        "taxExclusivePayPrice": pretax / divisor,
        "taxAmount": tax_amount / divisor,
        "taxCalculationId": calculation_id,
        "taxStatus": status,
        "taxBreakdown": [{**item, "amount": item["amount"] / divisor} for item in breakdown],
        "importFeesNoticeKey": IMPORT_FEES_NOTICE_KEY,
        "payPrice": (pretax + tax_amount) / divisor,
    }
    if details is not None:
        result["taxDetails"] = details
    if "items" in pricing:
        result["items"] = [
            {
                "sku": item["sku"], "name": item["name"], "quantity": item["quantity"],
                "unitPrice": item["unit_amount"] / divisor,
                "lineTotal": item["unit_amount"] * item["quantity"] / divisor,
            }
            for item in pricing["items"]
        ]
    return result


def normalize_shipping(shipping):
    if not isinstance(shipping, dict) or not isinstance(shipping.get("address"), dict):
        abort(400, "请填写收货地址。")
    name = shipping.get("name")
    address = shipping["address"]
    country = address.get("country")
    if not isinstance(country, str) or len(country) != 2 or not country.isascii() or not country.isalpha():
        abort(400, "请选择有效的收货国家。")
    country = country.upper()
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
    tax_client = None
    quote_cache = OrderedDict()
    quote_cache_lock = Lock()
    if publishable_key or secret_key:
        sandbox_key_prefixes = ("sk_test_", "rk_test_", "rkcs_test_")
        if not publishable_key.startswith("pk_test_") or not secret_key.startswith(sandbox_key_prefixes):
            raise ValueError("请配置同一个 Stripe 沙箱的公开密钥和服务端密钥。")
        stripe_client = stripe.StripeClient(secret_key, max_network_retries=2)
        tax_client = stripe.StripeClient(secret_key, max_network_retries=0,
                                         http_client=stripe.RequestsClient(timeout=(5, 2)))
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
                tax_calculation_expires_at INTEGER,
                tax_pricing TEXT,
                tax_quote TEXT,
                tax_transaction_id TEXT
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
            ("tax_pricing", "TEXT"),
            ("tax_quote", "TEXT"),
            ("tax_transaction_id", "TEXT"),
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
        result.pop("tax_pricing")
        raw_quote = result.pop("tax_quote")
        if raw_quote:
            quote = json.loads(raw_quote)
            result["quote"] = quote
            if ("taxDetails" not in quote and order["tax_calculation_id"]
                    and time.time() < (order["tax_calculation_expires_at"] or 0)):
                details = retrieve_tax_details(order["tax_calculation_id"], order["currency"])
                if details is not None:
                    quote["taxDetails"] = details
                    with closing(connect()) as connection, connection:
                        connection.execute("""
                            UPDATE orders SET tax_quote = ? WHERE id = ? AND tax_quote = ?
                        """, (json.dumps(quote, ensure_ascii=False), order["id"], raw_quote))
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
        order = require_order(order["id"])
        register_tax_transaction(order)
        return require_order(order["id"])

    def register_tax_transaction(order):
        if not (order["tax_pricing"] and order["status"] == "paid" and
                order["tax_calculation_id"] and order["tax_amount"] > 0 and not order["tax_transaction_id"]):
            return
        reference = f"order-java-demo:{order['id']}"
        transaction = stripe_client.v1.tax.transactions.create_from_calculation(
            {"calculation": order["tax_calculation_id"], "reference": reference},
            options={"idempotency_key": f"{reference}:tax-transaction"},
        )
        with closing(connect()) as connection, connection:
            connection.execute("""
                UPDATE orders SET tax_transaction_id = ? WHERE id = ? AND tax_transaction_id IS NULL
            """, (transaction.id, order["id"]))

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

    def calculate_tax(params, pretax, idempotency_key=None):
        options = {"idempotency_key": idempotency_key} if idempotency_key else {}
        calculation = tax_client.v1.tax.calculations.create(params, options=options)
        if (
            calculation.livemode is not False
            or calculation.currency != params["currency"]
            or calculation.tax_amount_exclusive < 0
            or calculation.tax_amount_inclusive != 0
            or calculation.amount_total != pretax + calculation.tax_amount_exclusive
            or not isinstance(calculation.expires_at, int)
        ):
            abort(502, "Stripe Tax 返回的税额或总额不一致。")
        return calculation

    def retrieve_tax_details(calculation_id, currency):
        if tax_client is None:
            return None
        try:
            calculation = tax_client.v1.tax.calculations.retrieve(
                calculation_id, {"expand": TAX_DETAIL_EXPANSIONS},
            )
        except stripe.StripeError as error:
            app.logger.warning("Tax details lookup failed code=%s request_id=%s", error.code, error.request_id)
            return None
        if calculation.livemode is False and calculation.currency == currency:
            return calculation_tax_details(calculation.to_dict(), 10 ** PRICES[currency]["decimals"])
        return None

    def guangzhou_quote(currency, pricing, shipping, order_id=None):
        if shipping is None:
            return public_quote(pricing, currency, "skipped_no_address"), None
        address = json.loads(shipping)["address"]
        if address["country"] not in {"US", "JP"}:
            return public_quote(pricing, currency, "skipped_non_us_jp"), None
        merchandise = max(0, pricing["product"] - pricing["discount"] - pricing["coupon"])
        params = {
            "currency": currency,
            "customer_details": {
                "address": {field: address[field] for field in ("country", "state", "city", "postal_code", "line1")},
                "address_source": "shipping",
            },
            "ship_from_details": {"address": GUANGZHOU_SHIP_FROM},
            "line_items": [{
                "amount": merchandise, "reference": "merchandise",
                "tax_code": GUANGZHOU_PRODUCT_TAX_CODE, "tax_behavior": "exclusive",
            }],
        }
        if pricing["shipping"] > 0:
            params["shipping_cost"] = {"amount": pricing["shipping"], "tax_behavior": "exclusive"}
        cache_key = json.dumps(params, sort_keys=True)
        # 预览可以复用。正式订单使用自己的试算 ID 登记税务交易。
        if order_id is None:
            with quote_cache_lock:
                cached = quote_cache.get(cache_key)
                if cached and time.monotonic() < cached[0]:
                    quote_cache.move_to_end(cache_key)
                    calculation = cached[1]
                else:
                    calculation = None
        else:
            calculation = None
        if calculation is None:
            try:
                key = f"order-java-demo:merchandise-tax:{order_id}" if order_id else None
                # 正式订单保留原请求参数，同一幂等键可重试之前已发出的请求。
                request_params = params if order_id else {**params, "expand": TAX_DETAIL_EXPANSIONS}
                calculation = calculate_tax(request_params, merchandise + pricing["shipping"], key)
            except stripe.StripeError as error:
                app.logger.warning("Tax quote failed code=%s request_id=%s", error.code, error.request_id)
                return public_quote(pricing, currency, "stripe_fallback"), None
            if order_id is None:
                with quote_cache_lock:
                    quote_cache[cache_key] = (time.monotonic() + 90, calculation)
                    quote_cache.move_to_end(cache_key)
                    if len(quote_cache) > 128:
                        quote_cache.popitem(last=False)
        raw_calculation = calculation.to_dict()
        details = calculation_tax_details(raw_calculation, 10 ** PRICES[currency]["decimals"])
        return public_quote(
            pricing, currency, "calculated", calculation.tax_amount_exclusive,
            calculation.id, tax_breakdown(raw_calculation), details,
        ), calculation

    def prepare_legacy_guangzhou_tax(order):
        address = json.loads(order["shipping_details"])["address"]
        # 已开始的订单保留原请求及幂等键。
        if address["country"] == "JP":
            address.pop("state")
        calculation = calculate_tax(
            {
                "currency": order["currency"],
                "line_items": [{
                    "amount": order["amount"], "reference": order["id"],
                    "tax_code": "txcd_99999999", "tax_behavior": "exclusive",
                }],
                "customer_details": {"address": address, "address_source": "shipping"},
                "ship_from_details": {"address": {"country": "CN", "city": "Guangzhou"}},
            },
            order["amount"], f"order-java-demo:tax:{order['id']}",
        )
        with closing(connect()) as connection, connection:
            connection.execute("""
                UPDATE orders SET amount = ?, tax_amount = ?, tax_calculation_id = ?, tax_calculation_expires_at = ?
                WHERE id = ? AND tax_calculation_id IS NULL
            """, (calculation.amount_total, calculation.tax_amount_exclusive,
                  calculation.id, calculation.expires_at, order["id"]))
        return require_order(order["id"])

    def prepare_guangzhou_tax(order):
        pricing = json.loads(order["tax_pricing"])
        quote, calculation = guangzhou_quote(order["currency"], pricing, order["shipping_details"], order["id"])
        with closing(connect()) as connection, connection:
            connection.execute("""
                UPDATE orders SET amount = ?, tax_amount = ?, tax_calculation_id = ?,
                    tax_calculation_expires_at = ?, tax_quote = ?
                WHERE id = ? AND tax_quote IS NULL
            """, (
                calculation.amount_total if calculation else order["amount"],
                calculation.tax_amount_exclusive if calculation else 0,
                calculation.id if calculation else None, calculation.expires_at if calculation else None,
                json.dumps(quote, ensure_ascii=False), order["id"],
            ))
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
            guangzhou_pricing=GUANGZHOU_PRICING, shipping_methods=SHIPPING_METHODS,
            catalog=[{"sku": sku, **product} for sku, product in PRODUCTS.items()],
            max_item_quantity=MAX_ITEM_QUANTITY,
            publishable_key=publishable_key, webhook_ready=bool(webhook_secret),
        )

    @app.post("/api/tax/quote")
    def preview_guangzhou_tax():
        if tax_client is None:
            abort(503, "请先在 .env 配置 Stripe 沙箱密钥并重启。")
        data = request.get_json(silent=True)
        currency = data.get("currency") if isinstance(data, dict) else None
        if not isinstance(currency, str) or currency not in PRICES:
            abort(400, "请选择美元或日元。")
        shipping = normalize_shipping(data["shipping"]) if data.get("shipping") is not None else None
        pricing = guangzhou_pricing(currency, data, has_address=shipping is not None)
        quote, _ = guangzhou_quote(currency, pricing, shipping)
        return jsonify(quote)

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
        if "items" in data and tax_case_id != GUANGZHOU_TAX_CASE:
            abort(400, "购物车订单使用广州发货结算。")
        shipping = None
        if tax_case_id == GUANGZHOU_TAX_CASE and data.get("shipping") is not None:
            shipping = normalize_shipping(data["shipping"])
        order_id = str(order_id)
        order = find_order(order_id)
        if order is None:
            if tax_case_id is not None and tax_case_id not in TAX_CASES:
                abort(400, "请选择有效的税费场景。")
            if tax_case_id == GUANGZHOU_TAX_CASE and shipping is None:
                abort(400, "请先填写收货地址，再计算广州发货订单的税费。")
            if "items" in data and json.loads(shipping)["address"]["country"] not in {"US", "JP"}:
                abort(400, "购物车支持寄往美国或日本。")
            tax_rate_id = None
            if tax_case_id == "stripe_manual":
                tax_rate_id = manual_tax_ids[currency]
                if not tax_rate_id.startswith("txr_"):
                    abort(503, "请按 README 在 Stripe 沙箱新建手动税率，并在 .env 配置对应的 txr_ ID。")
            pricing = guangzhou_pricing(currency, data) if tax_case_id == GUANGZHOU_TAX_CASE else None
            amount = (pricing["product"] - pricing["discount"] - pricing["coupon"] + pricing["shipping"]
                      if pricing else PRICES[currency]["amount"])
            with closing(connect()) as connection, connection:
                connection.execute("""
                    INSERT INTO orders (
                        id, currency, amount, created_at, tax_case, stripe_tax_rate_id,
                        checkout_ui_mode, shipping_details, tax_pricing
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    ON CONFLICT(id) DO NOTHING
                """, (order_id, currency, amount, int(time.time()), tax_case_id, tax_rate_id,
                      "elements" if tax_case_id in CHECKOUT_TAX_CASES else None, shipping,
                      json.dumps(pricing) if pricing else None))
            order = require_order(order_id)

        if order["currency"] != currency:
            abort(409, "同一订单不能更换币种，请创建新订单。")
        if order["tax_case"] != tax_case_id:
            abort(409, "同一订单不能更换税费场景，请创建新订单。")
        if shipping is not None and shipping != order["shipping_details"]:
            abort(409, "同一订单不能更换收货地址，请创建新订单。")
        pricing = json.loads(order["tax_pricing"]) if order["tax_pricing"] else {}
        if "items" in data:
            ordered_items = {item["sku"]: item["quantity"] for item in pricing.get("items", [])}
            if cart_quantities(data["items"]) != ordered_items:
                abort(409, "同一订单不能更改商品或数量，请创建新订单。")
        if pricing:
            selected = {"shippingMethodCode": pricing["shipping_method"], "promotionCode": pricing["promotion_code"]}
            selected.update({field: data[field] for field in selected if field in data})
            if guangzhou_options(selected, currency) != (pricing["shipping_method"], pricing["promotion_code"]):
                abort(409, "同一订单不能更改配送方式或优惠码，请创建新订单。")
        if "items" in pricing:
            if order["tax_quote"] is None:
                require_retry_window(order)
                order = prepare_guangzhou_tax(order)
            return jsonify(order=public_order(order), client_secret=None)
        return prepare_order_payment(order)

    @app.post("/api/orders/<uuid:order_id>/payment")
    def start_payment(order_id):
        if stripe_client is None:
            abort(503, "请先在 .env 配置 Stripe 沙箱密钥并重启。")
        order = require_order(order_id)
        if order["tax_pricing"] and not order["tax_quote"]:
            abort(409, "订单仍在确认税费，请稍后重试。")
        return prepare_order_payment(order)

    def prepare_order_payment(order):
        if order["status"] == "paid":
            return jsonify(order=public_order(order), client_secret=None)
        if order["tax_case"] in CHECKOUT_TAX_CASES:
            return prepare_checkout(order)

        if order["payment_intent_id"]:
            intent = stripe_client.v1.payment_intents.retrieve(order["payment_intent_id"])
        else:
            require_retry_window(order)
            order_id = order["id"]
            metadata = {"order_id": order_id, "integration": "order-java-demo"}
            tax_details = {}
            if order["tax_case"] == GUANGZHOU_TAX_CASE:
                if order["tax_pricing"]:
                    if order["tax_quote"] is None:
                        order = prepare_guangzhou_tax(order)
                elif order["tax_calculation_id"] is None:
                    order = prepare_legacy_guangzhou_tax(order)
                expires_at = order["tax_calculation_expires_at"]
                if expires_at is not None and time.time() >= expires_at:
                    abort(409, "税费报价已过期，请创建新订单。")
                metadata.update(tax_case=GUANGZHOU_TAX_CASE, tax_amount=str(order["tax_amount"]))
                tax_details["shipping"] = json.loads(order["shipping_details"])
                if not order["tax_pricing"]:
                    tax_details["hooks"] = {"inputs": {"tax": {"calculation": order["tax_calculation_id"]}}}
            if order["tax_case"] not in {None, GUANGZHOU_TAX_CASE}:
                metadata.update(
                    tax_case=order["tax_case"],
                    tax_rate_bps=str(order["tax_rate_bps"]),
                    tax_amount=str(order["tax_amount"]),
                )
            intent = stripe_client.v1.payment_intents.create(
                {
                    "amount": order["amount"],
                    "currency": order["currency"],
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
        if order["status"] == "paid":
            register_tax_transaction(order)
            return jsonify(public_order(require_order(order_id)))
        if not (order["payment_intent_id"] or order["checkout_session_id"]):
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
