const currencySelect = document.querySelector('#currency');
const taxCaseSelect = document.querySelector('#tax-case');
const orderForm = document.querySelector('#order-form');
const createButton = document.querySelector('#create-order');
const newOrderButton = document.querySelector('#new-order');
const paymentForm = document.querySelector('#payment-form');
const shippingFields = document.querySelector('#shipping-fields');
const quoteButton = document.querySelector('#quote-shipping');
const checkoutFields = document.querySelector('#checkout-fields');
const paymentFields = document.querySelector('#payment-fields');
const checkoutButton = document.querySelector('#open-checkout');
const payButton = document.querySelector('#pay');
const checkButton = document.querySelector('#check-status');
const message = document.querySelector('#message');
const guangzhouTaxCase = 'stripe_guangzhou';
const taxRateFormatter = new Intl.NumberFormat('zh-CN', { style: 'percent', maximumFractionDigits: 2 });
const storageKey = 'stripe-demo-order';
const returnUrl = new URL(window.location.pathname, window.location.origin);
const returnParams = new URLSearchParams(window.location.search);
const checkoutReturn = returnParams.get('checkout');
const orderIdParam = returnParams.get('order_id');
const returnedOrderId = ['success', 'cancel'].includes(checkoutReturn)
  && /^[\da-f]{8}(?:-[\da-f]{4}){3}-[\da-f]{12}$/i.test(orderIdParam ?? '') ? orderIdParam : null;
const redirected = returnParams.has('payment_intent') || checkoutReturn === 'success';
if (redirected || returnedOrderId) {
  history.replaceState(null, '', returnUrl.pathname);
}

let stripe;
let elements;
let checkout;
let checkoutActions;
let shippingElement;
let pendingShipping;
const mountedElements = [];
let canConfirm = false;
let confirming = false;
let prices;
let taxCases;
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

function renderTaxOptions(selectedCase = '') {
  taxCaseSelect.replaceChildren(new Option('常规支付', ''));
  for (const [id, label] of Object.entries(taxCases)) {
    taxCaseSelect.add(new Option(label, id));
  }
  if (selectedCase && !(selectedCase in taxCases)) {
    taxCaseSelect.add(new Option('历史税费订单', selectedCase));
  }
  taxCaseSelect.value = selectedCase;
}

function renderAmounts(currency, quote, subtotalOnly = false, unit = 10 ** prices[currency].decimals) {
  const formatter = new Intl.NumberFormat('zh-CN', { style: 'currency', currency: currency.toUpperCase() });
  const amount = subtotalOnly ? quote.subtotal ?? quote.amount : quote.amount;
  document.querySelector('#total-label').textContent = subtotalOnly ? '商品小计' : '应付金额';
  document.querySelector('#total').textContent = formatter.format(amount / unit);
  const hasTax = Boolean(quote.tax_case) && !subtotalOnly;
  document.querySelector('#tax-breakdown').hidden = !hasTax;
  if (hasTax) {
    const rate = quote.tax_rate_bps ? taxRateFormatter.format(quote.tax_rate_bps / 10000) : null;
    document.querySelector('#subtotal').textContent = formatter.format(quote.subtotal / unit);
    document.querySelector('#tax-label').textContent = rate ? `税费（${rate}）` : '税费';
    document.querySelector('#tax-amount').textContent = formatter.format(quote.tax_amount / unit);
  }
}

function renderPreview() {
  const currency = currencySelect.value;
  const guangzhou = taxCaseSelect.value === guangzhouTaxCase;
  document.querySelector('#ship-from').hidden = !guangzhou;
  createButton.textContent = guangzhou ? '填写收货地址' : '创建订单';
  renderAmounts(currency, prices[currency], Boolean(taxCaseSelect.value));
}

function updatePayButton() {
  payButton.disabled = !canConfirm || confirming;
}

function mountElement(element, selector) {
  mountedElements.push(element);
  element.mount(selector);
}

function resetPaymentForm() {
  checkout = null;
  checkoutActions = null;
  elements = null;
  shippingElement = null;
  canConfirm = false;
  for (const element of mountedElements.splice(0)) element.destroy();
  checkoutFields.hidden = true;
  shippingFields.hidden = true;
  quoteButton.hidden = true;
  quoteButton.disabled = true;
  paymentFields.hidden = true;
  paymentForm.hidden = true;
  updatePayButton();
}

function renderCheckoutAmounts(session, taxCase) {
  const currency = session.currency.toLowerCase();
  const taxPending = taxCase === 'stripe_auto' && session.taxAmounts == null;
  const quote = {
    tax_case: taxCase,
    subtotal: session.total.subtotal.minorUnitsAmount,
    tax_amount: session.total.taxExclusive.minorUnitsAmount,
    amount: session.total.total.minorUnitsAmount,
  };
  renderAmounts(currency, quote, taxPending, session.minorUnitsAmountDivisor);
  document.querySelector('#stripe-amount').textContent = taxPending
    ? '填写收货地址后计算税费'
    : `amount=${quote.amount}, currency=${currency}`;
  canConfirm = Boolean(checkoutActions) && session.canConfirm && !taxPending;
  updatePayButton();
}

function renderOrder(order) {
  const paid = order.status === 'paid';
  const awaitingTax = order.tax_case === 'stripe_auto' && !paid;
  renderAmounts(order.currency, order, awaitingTax);
  document.querySelector('#order-detail').hidden = false;
  document.querySelector('#order-id').textContent = order.id;
  document.querySelector('#stripe-amount').textContent = awaitingTax
    ? '付款后显示含税金额'
    : `amount=${order.amount}, currency=${order.currency}`;
  document.querySelector('#intent-id').textContent = order.payment_intent_id || '尚未生成';
  const session = document.querySelector('#session-id');
  session.hidden = !order.checkout_session_id;
  session.textContent = order.checkout_session_id || '';
  document.querySelector('#session-label').hidden = session.hidden;
  document.querySelector('#order-status').textContent = paid ? '已支付' : '待支付';
  const paidVia = document.querySelector('#paid-via');
  paidVia.hidden = !paid;
  document.querySelector('#paid-via-label').hidden = !paid;
  paidVia.textContent = order.paid_via === 'webhook' ? 'Stripe Webhook' : '服务端查单';
  newOrderButton.hidden = false;
  checkButton.hidden = paid;
  if (paid) {
    resetPaymentForm();
    checkoutButton.hidden = true;
    showMessage('支付成功。订单已在服务端标记为已支付。', 'success');
  } else if (checkoutActions && order.stripe_status === 'requires_payment_method') {
    renderCheckoutAmounts(checkoutActions.getSession(), order.tax_case);
  }
}

async function mountCheckout(clientSecret, taxCase) {
  const sdk = stripe.initCheckoutElementsSdk({ clientSecret });
  checkout = sdk;
  sdk.on('change', session => {
    if (checkout === sdk) renderCheckoutAmounts(session, taxCase);
  });
  const loaded = await sdk.loadActions();
  if (loaded.type === 'error') throw new Error(loaded.error.message);
  checkoutActions = loaded.actions;
  checkoutFields.hidden = false;
  shippingFields.hidden = false;
  paymentFields.hidden = false;
  paymentForm.hidden = false;
  mountElement(sdk.createShippingAddressElement(), '#shipping-address-element');
  mountElement(sdk.createContactDetailsElement(), '#contact-details-element');
  const payment = sdk.createPaymentElement();
  payment.on('loaderror', () => showMessage('Stripe 付款表单加载失败，请检查网络后重试。', 'error'));
  mountElement(payment, '#payment-element');
  renderCheckoutAmounts(checkoutActions.getSession(), taxCase);
  showMessage('先填收货地址，再核对税费并确认付款。');
}

function showGuangzhouAddress() {
  if (!stripe) throw new Error('未能加载 Stripe.js，请检查网络并刷新页面。');
  const country = prices[currencySelect.value].country;
  const address = stripe.elements().create('address', { mode: 'shipping', allowedCountries: [country] });
  shippingElement = address;
  address.on('change', ({ complete }) => {
    if (shippingElement === address && !newOrderButton.disabled) quoteButton.disabled = !complete;
  });
  currencySelect.disabled = true;
  taxCaseSelect.disabled = true;
  createButton.disabled = true;
  newOrderButton.hidden = false;
  paymentForm.hidden = false;
  shippingFields.hidden = false;
  quoteButton.hidden = false;
  mountElement(address, '#shipping-address-element');
  showMessage('填完收货地址后点击「计算税费」。');
}

async function preparePayment() {
  const result = await api(`/api/orders/${activeOrder.id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ currency: activeOrder.currency, tax_case: activeOrder.tax_case, shipping: pendingShipping }),
  });
  if (activeOrder.tax_case === guangzhouTaxCase) {
    sessionStorage.setItem(storageKey, JSON.stringify(activeOrder));
    pendingShipping = undefined;
    if (shippingElement) resetPaymentForm();
  }
  renderOrder(result.order);
  if (result.order.status === 'paid') return;
  if (result.order.stripe_status === 'canceled') {
    showMessage('这笔支付已取消或过期，可以创建新订单。');
    return;
  }
  if (result.order.stripe_status === 'processing') {
    showMessage('Stripe 正在处理，可以稍后查询付款结果。');
    return;
  }

  if (result.checkout_url) {
    checkoutButton.onclick = () => window.location.assign(result.checkout_url);
    checkoutButton.hidden = false;
    showMessage('在 Stripe Checkout 填写收货地址，核对税费并用测试卡付款。');
    return;
  }

  if (!stripe) throw new Error('未能加载 Stripe.js，请检查网络并刷新页面。');
  if (result.checkout_client_secret) {
    await mountCheckout(result.checkout_client_secret, result.order.tax_case);
    return;
  }

  elements = stripe.elements({ clientSecret: result.client_secret, locale: 'zh' });
  const payment = elements.create('payment');
  payment.on('ready', () => { canConfirm = true; updatePayButton(); });
  payment.on('loaderror', () => showMessage('Stripe 付款表单加载失败，请检查网络后重试。', 'error'));
  paymentForm.hidden = false;
  paymentFields.hidden = false;
  mountElement(payment, '#payment-element');
  showMessage('在 Stripe 表单中输入测试卡并确认付款。');
}

async function loadOrder() {
  currencySelect.disabled = true;
  taxCaseSelect.disabled = true;
  createButton.disabled = true;
  checkoutButton.hidden = true;
  newOrderButton.hidden = true;
  showMessage(pendingShipping ? '正在按广州发货和收货地址计算税费。' : '正在准备 Stripe 支付。');
  try {
    await preparePayment();
  } catch (error) {
    resetPaymentForm();
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
      resetPaymentForm();
      checkoutButton.hidden = true;
      showMessage('这笔支付已取消或过期，可以创建新订单。');
    } else if (order.stripe_status === 'processing') {
      resetPaymentForm();
      checkoutButton.hidden = true;
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

currencySelect.addEventListener('change', renderPreview);
taxCaseSelect.addEventListener('change', renderPreview);

orderForm.addEventListener('submit', async event => {
  event.preventDefault();
  if (!activeOrder && taxCaseSelect.value === guangzhouTaxCase) {
    try {
      showGuangzhouAddress();
    } catch (error) {
      resetPaymentForm();
      currencySelect.disabled = false;
      taxCaseSelect.disabled = false;
      createButton.disabled = false;
      newOrderButton.hidden = true;
      showMessage(error.message, 'error');
    }
    return;
  }
  if (!activeOrder) {
    activeOrder = {
      id: crypto.randomUUID(),
      currency: currencySelect.value,
      tax_case: taxCaseSelect.value || null,
    };
    sessionStorage.setItem(storageKey, JSON.stringify(activeOrder));
  }
  await loadOrder();
});

quoteButton.addEventListener('click', async () => {
  if (quoteButton.disabled || !shippingElement) return;
  const address = shippingElement;
  quoteButton.disabled = true;
  newOrderButton.disabled = true;
  try {
    const { complete, value } = await address.getValue();
    if (!complete) throw new Error('请填写完整的收货地址。');
    pendingShipping = value;
    activeOrder = { id: crypto.randomUUID(), currency: currencySelect.value, tax_case: guangzhouTaxCase };
    await loadOrder();
  } catch (error) {
    showMessage(error.message, 'error');
  } finally {
    newOrderButton.disabled = false;
    if (shippingElement === address) quoteButton.disabled = false;
  }
});

paymentForm.addEventListener('submit', async event => {
  event.preventDefault();
  if (confirming || !canConfirm) return;
  confirming = true;
  updatePayButton();
  checkButton.disabled = true;
  newOrderButton.disabled = true;
  showMessage('正在确认付款，请等待 Stripe 返回结果。');
  try {
    const result = checkoutActions
      ? await checkoutActions.confirm({ redirect: 'if_required' })
      : await stripe.confirmPayment({ elements, confirmParams: { return_url: returnUrl.href }, redirect: 'if_required' });
    if (result.error) {
      showMessage(result.error.message, 'error');
      return;
    }
    showMessage('正在确认服务端订单状态。');
    await checkPayment({ waitForWebhook: true });
  } catch (error) {
    showMessage(error.message, 'error');
  } finally {
    confirming = false;
    updatePayButton();
    checkButton.disabled = false;
    newOrderButton.disabled = false;
  }
});

checkButton.addEventListener('click', () => checkPayment());

newOrderButton.addEventListener('click', () => {
  resetPaymentForm();
  activeOrder = null;
  pendingShipping = undefined;
  sessionStorage.removeItem(storageKey);
  document.querySelector('#order-detail').hidden = true;
  checkoutButton.hidden = true;
  newOrderButton.hidden = true;
  checkButton.hidden = true;
  currencySelect.disabled = false;
  taxCaseSelect.disabled = false;
  createButton.disabled = false;
  renderTaxOptions(taxCaseSelect.value in taxCases ? taxCaseSelect.value : '');
  renderPreview();
  showMessage('选择币种和税费场景后创建订单。');
});

async function initialize() {
  try {
    const config = await api('/api/config');
    prices = config.prices;
    taxCases = config.tax_cases ?? {};
    currencySelect.add(new Option('美元 USD', 'usd'));
    currencySelect.add(new Option('日元 JPY', 'jpy'));
    renderTaxOptions();
    renderPreview();
    if (!config.publishable_key) {
      showMessage('请在 .env 填入 Stripe 沙箱密钥，再重启 Python 服务。');
      return;
    }
    if (window.Stripe) stripe = window.Stripe(config.publishable_key, { locale: 'zh' });
    activeOrder = JSON.parse(sessionStorage.getItem(storageKey) || 'null');
    if (returnedOrderId && activeOrder?.id !== returnedOrderId) {
      const order = await api(`/api/orders/${returnedOrderId}`);
      activeOrder = { id: order.id, currency: order.currency, tax_case: order.tax_case };
      sessionStorage.setItem(storageKey, JSON.stringify(activeOrder));
    }
    if (activeOrder && prices[activeOrder.currency]) {
      currencySelect.value = activeOrder.currency;
      renderTaxOptions(activeOrder.tax_case ?? '');
      renderPreview();
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
    taxCaseSelect.disabled = false;
    createButton.disabled = false;
    showMessage('选择币种和税费场景后创建订单。');
  } catch (error) {
    showMessage(error.message, 'error');
  }
}

initialize();
