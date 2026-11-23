const currencySelect = document.querySelector('#currency');
const orderForm = document.querySelector('#order-form');
const createButton = document.querySelector('#create-order');
const newOrderButton = document.querySelector('#new-order');
const paymentForm = document.querySelector('#payment-form');
const payButton = document.querySelector('#pay');
const checkButton = document.querySelector('#check-status');
const message = document.querySelector('#message');
const storageKey = 'stripe-demo-order';
const returnUrl = new URL(window.location.pathname, window.location.origin);
const redirected = new URLSearchParams(window.location.search).has('payment_intent');
if (redirected) {
  history.replaceState(null, '', returnUrl.pathname);
}

let stripe;
let elements;
let paymentElement;
let prices;
let activeOrder;

async function api(path, options = {}) {
  const response = await fetch(path, options);
  const body = await response.json();
  if (!response.ok) {
    throw new Error(body.error || '请求失败，请稍后重试。');
  }
  return body;
}

function showMessage(text, tone = '') {
  message.textContent = text;
  message.dataset.tone = tone;
}

function renderTotal() {
  const currency = currencySelect.value;
  const price = prices[currency];
  const formatter = new Intl.NumberFormat('zh-CN', { style: 'currency', currency: currency.toUpperCase() });
  document.querySelector('#total').textContent = formatter.format(price.amount / 10 ** price.decimals);
}

function renderOrder(order) {
  document.querySelector('#order-detail').hidden = false;
  document.querySelector('#order-id').textContent = order.id;
  document.querySelector('#stripe-amount').textContent = `amount=${order.amount}, currency=${order.currency}`;
  document.querySelector('#intent-id').textContent = order.payment_intent_id || '尚未生成';
  const paid = order.status === 'paid';
  document.querySelector('#order-status').textContent = paid ? '已支付' : '待支付';
  const paidVia = document.querySelector('#paid-via');
  paidVia.hidden = !paid;
  document.querySelector('#paid-via-label').hidden = !paid;
  paidVia.textContent = order.paid_via === 'webhook' ? 'Stripe Webhook' : '服务端查单';
  newOrderButton.hidden = false;
  checkButton.hidden = paid;
  if (paid) {
    paymentForm.hidden = true;
    showMessage('支付成功。订单已在服务端标记为已支付。', 'success');
  }
}

async function preparePayment() {
  const result = await api(`/api/orders/${activeOrder.id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ currency: activeOrder.currency }),
  });
  renderOrder(result.order);
  if (result.order.status === 'paid') return;
  if (result.order.stripe_status === 'canceled') {
    showMessage('这笔支付已取消，可以创建新订单。');
    return;
  }
  if (result.order.stripe_status === 'processing') {
    showMessage('Stripe 正在处理，可以稍后查询付款结果。');
    return;
  }

  elements = stripe.elements({ clientSecret: result.client_secret, locale: 'zh' });
  paymentElement = elements.create('payment');
  paymentElement.on('ready', () => { payButton.disabled = false; });
  paymentElement.on('loaderror', () => showMessage('Stripe 付款表单加载失败，请检查网络后重试。', 'error'));
  paymentForm.hidden = false;
  paymentElement.mount('#payment-element');
  showMessage('在 Stripe 表单中输入测试卡并确认付款。');
}

async function loadOrder() {
  currencySelect.disabled = true;
  createButton.disabled = true;
  newOrderButton.hidden = true;
  showMessage('正在准备 Stripe 支付。');
  try {
    await preparePayment();
  } catch (error) {
    showMessage(error.message, 'error');
    createButton.textContent = '重试创建订单';
    createButton.disabled = false;
    newOrderButton.hidden = false;
  }
}

async function checkPayment({ waitForWebhook = false } = {}) {
  checkButton.disabled = true;
  const wasNewOrderDisabled = newOrderButton.disabled;
  newOrderButton.disabled = true;
  const path = `/api/orders/${activeOrder.id}`;
  try {
    if (waitForWebhook) {
      for (let attempt = 0; attempt < 4; attempt += 1) {
        const order = await api(path);
        if (order.status === 'paid') {
          renderOrder(order);
          return;
        }
        await new Promise(resolve => setTimeout(resolve, 700));
      }
    }
    const order = await api(`${path}/sync`, { method: 'POST' });
    renderOrder(order);
    if (order.status === 'paid') return;
    if (order.stripe_status === 'canceled') {
      paymentForm.hidden = true;
      showMessage('这笔支付已取消，可以创建新订单。');
    } else if (order.stripe_status === 'processing') {
      paymentForm.hidden = true;
      showMessage('Stripe 正在处理，可以稍后查询付款结果。');
    } else {
      showMessage('订单仍待支付，请检查付款操作是否已完成。');
    }
  } catch (error) {
    showMessage(error.message, 'error');
  } finally {
    checkButton.disabled = false;
    newOrderButton.disabled = wasNewOrderDisabled;
  }
}

currencySelect.addEventListener('change', renderTotal);

orderForm.addEventListener('submit', async event => {
  event.preventDefault();
  if (!activeOrder) {
    activeOrder = { id: crypto.randomUUID(), currency: currencySelect.value };
    sessionStorage.setItem(storageKey, JSON.stringify(activeOrder));
  }
  await loadOrder();
});

paymentForm.addEventListener('submit', async event => {
  event.preventDefault();
  payButton.disabled = true;
  checkButton.disabled = true;
  newOrderButton.disabled = true;
  showMessage('正在确认付款，请等待 Stripe 返回结果。');
  try {
    const result = await stripe.confirmPayment({
      elements,
      confirmParams: { return_url: returnUrl.href },
      redirect: 'if_required',
    });
    if (result.error) {
      showMessage(result.error.message, 'error');
      return;
    }
    showMessage('正在确认服务端订单状态。');
    await checkPayment({ waitForWebhook: true });
  } catch (error) {
    showMessage(error.message, 'error');
  } finally {
    payButton.disabled = false;
    checkButton.disabled = false;
    newOrderButton.disabled = false;
  }
});

checkButton.addEventListener('click', () => checkPayment());

newOrderButton.addEventListener('click', () => {
  paymentElement?.destroy();
  paymentElement = null;
  elements = null;
  activeOrder = null;
  sessionStorage.removeItem(storageKey);
  document.querySelector('#order-detail').hidden = true;
  paymentForm.hidden = true;
  newOrderButton.hidden = true;
  checkButton.hidden = true;
  payButton.disabled = true;
  currencySelect.disabled = false;
  createButton.disabled = false;
  createButton.textContent = '创建订单';
  showMessage('先选择币种并创建订单。');
});

async function initialize() {
  try {
    const config = await api('/api/config');
    prices = config.prices;
    currencySelect.add(new Option('美元 USD', 'usd'));
    currencySelect.add(new Option('日元 JPY', 'jpy'));
    renderTotal();
    if (!config.publishable_key) {
      showMessage('请在 .env 填入 Stripe 沙箱密钥，再重启 Python 服务。');
      return;
    }
    if (!window.Stripe) {
      showMessage('未能加载 Stripe.js，请检查网络并刷新页面。', 'error');
      return;
    }
    stripe = window.Stripe(config.publishable_key);
    activeOrder = JSON.parse(sessionStorage.getItem(storageKey) || 'null');
    if (activeOrder && prices[activeOrder.currency]) {
      currencySelect.value = activeOrder.currency;
      renderTotal();
      if (redirected) {
        renderOrder(await api(`/api/orders/${activeOrder.id}`));
        showMessage('正在查询 Stripe 支付结果。');
        await checkPayment({ waitForWebhook: true });
        if (!document.querySelector('#paid-via').hidden) return;
      }
      await loadOrder();
      return;
    }
    currencySelect.disabled = false;
    createButton.disabled = false;
    showMessage('先选择币种并创建订单。');
  } catch (error) {
    showMessage(error.message, 'error');
  }
}

initialize();
