import hmac
import os
import secrets
import sqlite3
import time
from contextlib import closing, contextmanager
from datetime import datetime, timezone
from pathlib import Path
from uuid import uuid4

import stripe
from dotenv import load_dotenv
from flask import Flask, abort, flash, redirect, render_template, request, session, url_for
from werkzeug.exceptions import HTTPException


BASE_DIR = Path(__file__).resolve().parent
DEMO = "stripe-subscription-demo"
ENDED = {"canceled", "incomplete_expired"}
SUBSCRIPTION_EVENTS = {
    "customer.subscription.created", "customer.subscription.updated", "customer.subscription.deleted",
    "customer.subscription.paused", "customer.subscription.resumed",
}
INVOICE_EVENTS = {"invoice.paid", "invoice.payment_failed"}


def invoice_subscription_id(invoice):
    parent = invoice.get("parent") or {}
    return (parent.get("subscription_details") or {}).get("subscription") or invoice.get("subscription")


def create_app():
    load_dotenv(BASE_DIR / ".env")
    app = Flask(__name__)
    app.config.update(
        SECRET_KEY=os.getenv("FLASK_SECRET_KEY") or secrets.token_hex(32),
        SESSION_COOKIE_NAME="stripe_subscription_demo",
        SESSION_COOKIE_SAMESITE="Lax",
        MAX_CONTENT_LENGTH=512 * 1024,
    )
    port = int(os.getenv("PORT", "4243"))
    origin = f"http://127.0.0.1:{port}"
    database = os.getenv("STRIPE_SUBSCRIPTION_DATABASE") or BASE_DIR / "subscriptions.sqlite3"
    secret_key = os.getenv("STRIPE_SECRET_KEY", "")
    webhook_secret = os.getenv("STRIPE_WEBHOOK_SECRET", "")
    if secret_key and not secret_key.startswith(("sk_test_", "rk_test_", "rkcs_test_")):
        raise ValueError("这个 Demo 需要 Stripe 沙箱密钥。")
    if webhook_secret and not webhook_secret.startswith("whsec_"):
        raise ValueError("STRIPE_WEBHOOK_SECRET 应以 whsec_ 开头。")
    client = None
    if secret_key:
        client = stripe.StripeClient(
            secret_key, max_network_retries=1, http_client=stripe.RequestsClient(timeout=(4, 6)),
        )
    plans = {
        "month": {"label": "月付会员", "price_id": os.getenv("STRIPE_MONTHLY_PRICE_ID", "")},
        "year": {"label": "年付会员", "price_id": os.getenv("STRIPE_YEARLY_PRICE_ID", "")},
    }

    def connect():
        db = sqlite3.connect(database, timeout=20)
        db.row_factory = sqlite3.Row
        db.execute("PRAGMA foreign_keys = ON")
        return db

    @contextmanager
    def transaction():
        with closing(connect()) as db, db:
            db.execute("BEGIN IMMEDIATE")
            yield db

    with closing(connect()) as db, db:
        db.executescript("""
            CREATE TABLE IF NOT EXISTS members (
                id TEXT PRIMARY KEY,
                customer_id TEXT UNIQUE NOT NULL
            );
            CREATE TABLE IF NOT EXISTS checkout_attempts (
                member_id TEXT PRIMARY KEY REFERENCES members (id),
                id TEXT UNIQUE NOT NULL,
                price_id TEXT NOT NULL,
                started_at INTEGER NOT NULL,
                session_id TEXT UNIQUE
            );
            CREATE TABLE IF NOT EXISTS subscriptions (
                id TEXT PRIMARY KEY,
                member_id TEXT NOT NULL REFERENCES members (id),
                interval TEXT NOT NULL,
                status TEXT NOT NULL,
                cancel_at INTEGER,
                latest_invoice_status TEXT,
                created_at INTEGER NOT NULL
            );
            CREATE INDEX IF NOT EXISTS subscriptions_member ON subscriptions (member_id, created_at);
            CREATE TABLE IF NOT EXISTS paid_invoices (
                id TEXT PRIMARY KEY,
                subscription_id TEXT NOT NULL REFERENCES subscriptions (id),
                paid_until INTEGER NOT NULL
            );
        """)

    @app.after_request
    def private_response(response):
        response.headers["Cache-Control"] = "no-store"
        response.headers["Referrer-Policy"] = "no-referrer"
        return response

    def require_stripe():
        if client is None:
            abort(503, "请在 .env 填写 Stripe 沙箱密钥后重启服务。")

    def require_form():
        member_id = session.get("member_id")
        token = session.get("form_token")
        supplied = request.form.get("token", "")
        if not member_id or not token or not hmac.compare_digest(token.encode(), supplied.encode()):
            abort(400, "表单已过期，请返回首页刷新后再试。")
        require_stripe()
        return member_id

    def get_member(member_id):
        with closing(connect()) as db:
            return db.execute("SELECT * FROM members WHERE id = ?", (member_id,)).fetchone()

    def ensure_member(member_id):
        with transaction() as db:
            member = db.execute("SELECT * FROM members WHERE id = ?", (member_id,)).fetchone()
            if member:
                return member
            customer = client.v1.customers.create(
                {"metadata": {"demo": DEMO, "member_id": member_id}},
                options={"idempotency_key": f"{DEMO}:customer:{member_id}"},
            )
            db.execute("INSERT INTO members VALUES (?, ?)", (member_id, customer.id))
            return db.execute("SELECT * FROM members WHERE id = ?", (member_id,)).fetchone()

    def save_paid_invoice(db, subscription_id, item_id, invoice):
        if invoice.get("status") != "paid" or invoice_subscription_id(invoice) != subscription_id:
            return
        # 发票的 period_end 是生成发票的区间。权益到期日取订阅商品行的服务期。
        periods = []
        for line in invoice["lines"]["data"]:
            details = (line.get("parent") or {}).get("subscription_item_details") or {}
            if details.get("subscription_item") == item_id and not details.get("proration"):
                periods.append(line["period"]["end"])
        if periods:
            db.execute("INSERT OR IGNORE INTO paid_invoices VALUES (?, ?, ?)",
                       (invoice["id"], subscription_id, max(periods)))

    def sync_subscription(subscription_id, invoice_id=None):
        # 相邻 Webhook 可能乱序或并发。串行读取 Stripe 的最新快照，再写入本地事务。
        with transaction() as db:
            subscription = client.v1.subscriptions.retrieve(
                subscription_id, {"expand": ["latest_invoice"]},
            ).to_dict()
            metadata = subscription.get("metadata") or {}
            if subscription["livemode"] or metadata.get("demo") != DEMO:
                return
            member = db.execute("SELECT * FROM members WHERE id = ?", (metadata.get("member_id"),)).fetchone()
            if not member or member["customer_id"] != subscription["customer"]:
                return
            item, = subscription["items"]["data"]
            latest_invoice = subscription.get("latest_invoice") or {}
            cancel_at = subscription.get("cancel_at")
            if subscription.get("cancel_at_period_end") and cancel_at is None:
                cancel_at = item.get("current_period_end")
            db.execute("""
                INSERT INTO subscriptions VALUES (?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT(id) DO UPDATE SET interval = excluded.interval, status = excluded.status,
                    cancel_at = excluded.cancel_at, latest_invoice_status = excluded.latest_invoice_status
            """, (subscription_id, member["id"], item["price"]["recurring"]["interval"], subscription["status"],
                  cancel_at, latest_invoice.get("status"), subscription["created"]))
            if latest_invoice:
                save_paid_invoice(db, subscription_id, item["id"], latest_invoice)
            if invoice_id and invoice_id != latest_invoice.get("id"):
                invoice = client.v1.invoices.retrieve(invoice_id).to_dict()
                save_paid_invoice(db, subscription_id, item["id"], invoice)

    def remote_subscriptions(customer_id):
        return client.v1.subscriptions.list(
            {"customer": customer_id, "status": "all", "limit": 10},
        ).auto_paging_iter()

    @app.template_filter("date_time")
    def date_time(value):
        if value is None:
            return "—"
        return datetime.fromtimestamp(value, timezone.utc).strftime("%Y-%m-%d %H:%M UTC")

    @app.errorhandler(HTTPException)
    def http_error(error):
        if request.path == "/webhook":
            return {"error": error.description}, error.code
        return render_template("index.html", error=error.description), error.code

    @app.errorhandler(stripe.StripeError)
    def stripe_error(error):
        app.logger.warning("Stripe error: request_id=%s code=%s param=%s", error.request_id, error.code, error.param)
        message = "Stripe 请求失败。请检查沙箱配置和终端日志，再重试。"
        if request.path == "/portal" and isinstance(error, stripe.InvalidRequestError):
            message = "Stripe 客户门户暂不可用。请在同一个沙箱保存 Customer Portal 设置后重试。"
        if request.path == "/webhook":
            return {"error": message}, 502
        return render_template("index.html", error=message), 502

    @app.get("/")
    def index():
        if "member_id" not in session:
            session["member_id"] = str(uuid4())
            session["form_token"] = secrets.token_urlsafe(32)
        with closing(connect()) as db:
            member = db.execute("SELECT * FROM members WHERE id = ?", (session["member_id"],)).fetchone()
            subscription = db.execute("""
                SELECT s.*, MAX(p.paid_until) AS paid_until FROM subscriptions s
                LEFT JOIN paid_invoices p ON p.subscription_id = s.id
                WHERE s.member_id = ? GROUP BY s.id
                ORDER BY s.status NOT IN ('canceled', 'incomplete_expired') DESC, s.created_at DESC
                LIMIT 1
            """, (session["member_id"],)).fetchone()
        has_subscription = bool(subscription and subscription["status"] not in ENDED)
        has_access = has_subscription and (subscription["paid_until"] or 0) > time.time()
        return render_template("index.html", member=member, subscription=subscription, has_access=has_access,
                               has_subscription=has_subscription,
                               plans=plans, stripe_ready=bool(client), webhook_ready=bool(webhook_secret))

    @app.post("/checkout")
    def start_checkout():
        member_id = require_form()
        interval = request.form.get("plan")
        plan = plans.get(interval)
        if plan is None:
            abort(400, "请选择月付或年付套餐。")
        if not plan["price_id"].startswith("price_"):
            abort(503, "请在 .env 配置该套餐的循环 Price ID 后重启。")
        member = ensure_member(member_id)
        with transaction() as db:
            attempt = db.execute("SELECT * FROM checkout_attempts WHERE member_id = ?", (member_id,)).fetchone()
            if attempt and attempt["session_id"]:
                previous = client.v1.checkout.sessions.retrieve(attempt["session_id"])
                if previous.status == "open":
                    if attempt["price_id"] == plan["price_id"]:
                        return redirect(previous.url, code=303)
                    client.v1.checkout.sessions.expire(previous.id)
                db.execute("DELETE FROM checkout_attempts WHERE id = ?", (attempt["id"],))
                attempt = None
            if any(sub.status not in ENDED for sub in remote_subscriptions(member["customer_id"])):
                flash("已有订阅。可查询最新状态，或到 Stripe 客户门户管理订阅。")
                return redirect(url_for("index"), code=303)
            if attempt is None:
                price = client.v1.prices.retrieve(plan["price_id"]).to_dict()
                recurring = price.get("recurring") or {}
                if (not price["active"] or recurring.get("interval") != interval or recurring.get("interval_count") != 1
                        or recurring.get("usage_type") != "licensed" or (price.get("unit_amount") or 0) <= 0):
                    abort(409, "套餐需要匹配周期的固定金额循环 Price，每次收取一份套餐费用。")
                attempt_id = str(uuid4())
                db.execute("INSERT INTO checkout_attempts VALUES (?, ?, ?, ?, NULL)",
                           (member_id, attempt_id, price["id"], int(time.time())))
            else:
                attempt_id = attempt["id"]

        # 先保存请求参数和幂等键。网络中断后，使用同一组参数继续创建同一会话。
        with transaction() as db:
            attempt = db.execute("SELECT * FROM checkout_attempts WHERE id = ?", (attempt_id,)).fetchone()
            if attempt is None:
                flash("结账会话已更新，请返回首页查看。")
                return redirect(url_for("index"), code=303)
            if attempt["session_id"]:
                hosted = client.v1.checkout.sessions.retrieve(attempt["session_id"])
            else:
                if time.time() - attempt["started_at"] >= 23 * 3600:
                    abort(409, "旧的支付请求已超过重试窗口，请在 Stripe 沙箱核对结账会话。")
                hosted = client.v1.checkout.sessions.create({
                    "mode": "subscription",
                    "customer": member["customer_id"],
                    "client_reference_id": member_id,
                    "adaptive_pricing": {"enabled": False},
                    "line_items": [{"price": attempt["price_id"], "quantity": 1}],
                    "subscription_data": {"metadata": {"demo": DEMO, "member_id": member_id}},
                    "success_url": origin + "/checkout/return?session_id={CHECKOUT_SESSION_ID}",
                    "cancel_url": origin + "/",
                }, options={"idempotency_key": f"{DEMO}:checkout:{attempt['id']}"})
                db.execute("UPDATE checkout_attempts SET session_id = ? WHERE id = ?", (hosted.id, attempt["id"]))
        if hosted.status != "open":
            flash("已有结账结果，请查询 Stripe 状态。")
            return redirect(url_for("index"), code=303)
        return redirect(hosted.url, code=303)

    @app.get("/checkout/return")
    def checkout_return():
        require_stripe()
        checkout_id = request.args.get("session_id", "")
        with closing(connect()) as db:
            attempt = db.execute("SELECT * FROM checkout_attempts WHERE member_id = ? AND session_id = ?",
                                 (session.get("member_id"), checkout_id)).fetchone()
        if not attempt:
            abort(404, "找不到这个浏览器的结账会话。")
        hosted = client.v1.checkout.sessions.retrieve(checkout_id)
        if hosted.status == "complete" and hosted.subscription:
            sync_subscription(hosted.subscription)
        return redirect(url_for("index"))

    @app.post("/refresh")
    def refresh():
        member = get_member(require_form())
        if member:
            for subscription in remote_subscriptions(member["customer_id"]):
                sync_subscription(subscription.id)
        return redirect(url_for("index"), code=303)

    @app.post("/portal")
    def portal():
        member = get_member(require_form())
        if member is None:
            abort(404, "请先开通订阅，再管理支付方式和账单。")
        hosted = client.v1.billing_portal.sessions.create({"customer": member["customer_id"], "return_url": origin + "/"})
        return redirect(hosted.url, code=303)

    @app.post("/webhook")
    def webhook():
        require_stripe()
        if not webhook_secret:
            abort(503, "请先配置 Webhook 签名密钥。")
        try:
            event = stripe.Webhook.construct_event(request.get_data(), request.headers.get("Stripe-Signature", ""),
                                                   webhook_secret)
        except (ValueError, stripe.SignatureVerificationError):
            abort(400, "Webhook 签名或请求体无效。")
        if event.livemode:
            abort(400, "这个 Demo 只接受沙箱事件。")
        obj = event.data.object.to_dict()
        if event.type in SUBSCRIPTION_EVENTS and (obj.get("metadata") or {}).get("demo") == DEMO:
            sync_subscription(obj["id"])
        elif event.type in INVOICE_EVENTS:
            subscription_id = invoice_subscription_id(obj)
            with closing(connect()) as db:
                known_customer = db.execute("SELECT 1 FROM members WHERE customer_id = ?",
                                            (obj.get("customer"),)).fetchone()
            if subscription_id and known_customer:
                sync_subscription(subscription_id, obj["id"])
        return "", 204

    return app


if __name__ == "__main__":
    create_app().run(host="127.0.0.1", port=int(os.getenv("PORT", "4243")))
