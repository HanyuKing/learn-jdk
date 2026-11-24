const currencySelect = document.querySelector('#currency');
const taxCaseSelect = document.querySelector('#tax-case');
const orderForm = document.querySelector('#order-form');
const createButton = document.querySelector('#create-order');
const newOrderButton = document.querySelector('#new-order');
const paymentForm = document.querySelector('#payment-form');
const shippingFields = document.querySelector('#shipping-fields');
const shippingOptions = document.querySelector('#shipping-options');
const shippingMethod = document.querySelector('#shipping-method');
const promotionCode = document.querySelector('#promotion-code');
const quoteButton = document.querySelector('#quote-shipping');
const submitGuangzhouButton = document.querySelector('#submit-guangzhou');
const guangzhouSummary = document.querySelector('#guangzhou-summary');
const checkoutFields = document.querySelector('#checkout-fields');
const paymentFields = document.querySelector('#payment-fields');
const checkoutButton = document.querySelector('#open-checkout');
const startPaymentButton = document.querySelector('#start-payment');
const payButton = document.querySelector('#pay');
const checkButton = document.querySelector('#check-status');
const message = document.querySelector('#message');
const guangzhouTaxCase = 'stripe_guangzhou';
const taxRateFormatter = new Intl.NumberFormat('zh-CN', { style: 'percent', maximumFractionDigits: 4 });
const storageKey = 'stripe-demo-order';
const cartStorageKey = 'stripe-demo-cart';
const returnUrl = new URL(window.location.pathname, window.location.origin);
const returnParams = new URLSearchParams(window.location.search);
const checkoutReturn = returnParams.get('checkout');
const orderIdParam = returnParams.get('order_id');
const returnedFromPayment = returnParams.has('payment_intent');
const returnedOrderId = (returnedFromPayment || ['success', 'cancel'].includes(checkoutReturn))
  && /^[\da-f]{8}(?:-[\da-f]{4}){3}-[\da-f]{12}$/i.test(orderIdParam ?? '') ? orderIdParam : null;
const redirected = returnedFromPayment || checkoutReturn === 'success';
if (redirected || returnedOrderId) {
  history.replaceState(null, '', returnUrl.pathname);
}

let stripe;
let elements;
let checkout;
let checkoutActions;
let shippingElement;
let pendingShipping;
let pendingPricing;
let addressComplete = false;
let quotePending = false;
let quoteRevision = 0;
const mountedElements = [];
let canConfirm = false;
let confirming = false;
let prices;
let shippingMethods;
let taxCases;
let activeOrder;
let products;
let maxItemQuantity;
const cart = new Map();

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

function currencyFormatter(currency) {
  return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: currency.toUpperCase() });
}

function cartSelections() {
  return Array.from(cart, ([sku, quantity]) => ({ sku, quantity }));
}

function setCartQuantity(sku, quantity) {
  if (quantity < 1) cart.delete(sku);
  else cart.set(sku, quantity);
  sessionStorage.setItem(cartStorageKey, JSON.stringify(cartSelections()));
  invalidateQuote();
  renderPreview();
}

function buildCatalog() {
  const catalog = document.querySelector('#catalog');
  for (const product of products.values()) {
    const card = document.createElement('article');
    card.className = 'product';
    card.dataset.sku = product.sku;
    const name = document.createElement('h3');
    name.textContent = product.name;
    const description = document.createElement('p');
    description.textContent = product.description;
    const price = document.createElement('strong');
    price.className = 'product-price';
    const add = document.createElement('button');
    add.type = 'button';
    add.textContent = '加入购物车';
    add.setAttribute('aria-label', `加入购物车 · ${product.name}`);
    add.addEventListener('click', () => {
      const quantity = cart.get(product.sku) || 0;
      if (quantity < maxItemQuantity) setCartQuantity(product.sku, quantity + 1);
    });
    card.append(name, description, price, add);
    catalog.append(card);
  }
}

function renderCart(currency) {
  const divisor = 10 ** prices[currency].decimals;
  const formatter = currencyFormatter(currency);
  const format = amount => formatter.format(amount / divisor);
  const list = document.querySelector('#cart-items');
  list.replaceChildren();
  for (const card of document.querySelectorAll('#catalog .product')) {
    const product = products.get(card.dataset.sku);
    const quantity = cart.get(product.sku) || 0;
    card.querySelector('.product-price').textContent = format(product.prices[currency]);
    card.querySelector('button').disabled = quantity >= maxItemQuantity;
    if (!quantity) continue;
    const item = document.createElement('li');
    const name = document.createElement('strong');
    name.textContent = product.name;
    const chooseQuantity = document.createElement('select');
    chooseQuantity.setAttribute('aria-label', `${product.name}数量`);
    for (let count = 1; count <= maxItemQuantity; count += 1) {
      chooseQuantity.add(new Option(String(count), String(count)));
    }
    chooseQuantity.value = String(quantity);
    chooseQuantity.addEventListener('change', () => {
      setCartQuantity(product.sku, Number(chooseQuantity.value));
      list.querySelector(`[data-sku="${product.sku}"] select`)?.focus();
    });
    const amount = document.createElement('small');
    amount.textContent = format(product.prices[currency] * quantity);
    const remove = document.createElement('button');
    remove.type = 'button';
    remove.className = 'secondary';
    remove.textContent = '移除';
    remove.setAttribute('aria-label', `移除 ${product.name}`);
    remove.addEventListener('click', () => {
      setCartQuantity(product.sku, 0);
      card.querySelector('button').focus();
    });
    item.dataset.sku = product.sku;
    item.append(name, chooseQuantity, amount, remove);
    list.append(item);
  }
  document.querySelector('#cart-empty').hidden = Boolean(cart.size);
  document.querySelector('#cart-total-row').hidden = !cart.size;
  document.querySelector('#cart-count').textContent = `共 ${[...cart.values()].reduce((sum, quantity) => sum + quantity, 0)} 件`;
  document.querySelector('#cart-total').textContent = format(cartTotal(currency));
}

function cartTotal(currency) {
  return cartSelections().reduce((total, item) => total + products.get(item.sku).prices[currency] * item.quantity, 0);
}

function renderAmounts(currency, quote, subtotalOnly = false, unit = 10 ** prices[currency].decimals) {
  const formatter = currencyFormatter(currency);
  const amount = subtotalOnly ? quote.subtotal ?? quote.amount : quote.amount;
  guangzhouSummary.hidden = true;
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

function renderTaxDetails(quote, formatter) {
  const list = document.querySelector('#tax-rates');
  list.replaceChildren();
  function addRow(label, value) {
    const row = document.createElement('li');
    const name = document.createElement('span');
    const amount = document.createElement('span');
    name.textContent = label;
    amount.textContent = value;
    row.append(name, amount);
    list.append(row);
  }

  if (quote.taxDetails) {
    const { merchandise, shipping } = quote.taxDetails;
    const sharedRate = merchandise.rate != null && shipping?.rate === merchandise.rate;
    if (sharedRate) {
      const region = merchandise.rate > 0 && merchandise.region && merchandise.region === shipping.region
        ? ` · ${merchandise.region}` : '';
      addRow(`商品与运费同税率${region}`, taxRateFormatter.format(merchandise.rate));
    }
    for (const [name, part] of [['商品税', merchandise], ['运费税', shipping]]) {
      if (!part) {
        addRow(`${name} · 无运费`, formatter.format(0));
        continue;
      }
      const region = part.region && (part.rate > 0 || part.exempt) ? `${part.region} ` : '';
      const rate = !sharedRate && part.rate != null
        ? ` · ${region}${taxRateFormatter.format(part.rate)}`
        : '';
      addRow(`${name}${rate}${part.exempt ? '（免税）' : ''}`, formatter.format(part.amount));
    }
  } else {
    for (const item of quote.taxBreakdown) {
      if (item.rate === 0 && item.amount === 0) continue;
      const label = item.rate ? `${item.displayName} · ${taxRateFormatter.format(item.rate)}` : item.displayName;
      addRow(label, formatter.format(item.amount));
    }
  }
  document.querySelector('#tax-details').hidden = !list.childElementCount;
}

function renderGuangzhouQuote(quote) {
  const formatter = currencyFormatter(quote.currency);
  const orderedItems = document.querySelector('#order-items');
  orderedItems.replaceChildren();
  for (const item of quote.items || []) {
    const row = document.createElement('li');
    const name = document.createElement('span');
    const amount = document.createElement('strong');
    name.textContent = `${item.name} × ${item.quantity}`;
    amount.textContent = formatter.format(item.lineTotal);
    row.append(name, amount);
    orderedItems.append(row);
  }
  orderedItems.hidden = !orderedItems.childElementCount;
  const amounts = {
    '#product-price': quote.productTotalPrice,
    '#activity-discount': -quote.discount,
    '#coupon-discount': quote.promotionCodeDiscountAmount ? -quote.promotionCodeDiscountAmount : 0,
    '#merchandise-price': quote.productTotalPrice - quote.discount - quote.promotionCodeDiscountAmount,
    '#shipping-fee': quote.shippingFee,
    '#pretax-amount': quote.taxExclusivePayPrice,
    '#sales-tax': quote.taxAmount,
    '#total': quote.payPrice,
  };
  for (const [selector, amount] of Object.entries(amounts)) {
    document.querySelector(selector).textContent = formatter.format(amount);
  }
  renderTaxDetails(quote, formatter);
  const notes = {
    calculated: quote.taxAmount === 0 ? 'Stripe 已完成试算，本单结账税额为零。' : '',
    skipped_no_address: '填写收货地址后计算税费。',
    skipped_non_us_jp: '当前收货国家未启用 Stripe Tax 试算。',
    stripe_fallback: 'Stripe Tax 暂时未能完成试算，本次按零税额显示。',
  };
  const status = document.querySelector('#sales-tax-status');
  status.textContent = notes[quote.taxStatus] || '';
  status.hidden = !status.textContent;
  document.querySelector('#import-fees-note').hidden = quote.importFeesNoticeKey !== 'builda.checkout.import_fees_notice';
  document.querySelector('#tax-breakdown').hidden = true;
  document.querySelector('#total-label').textContent = '应付金额';
  guangzhouSummary.hidden = false;
}

function renderPreview() {
  const currency = currencySelect.value;
  const guangzhou = taxCaseSelect.value === guangzhouTaxCase;
  const shopping = guangzhou && !activeOrder;
  document.querySelector('#shop').hidden = !shopping;
  if (shopping) renderCart(currency);
  document.querySelector('#ship-from').hidden = !guangzhou;
  createButton.textContent = guangzhou ? '填写收货地址' : '创建订单';
  if (!activeOrder && !shippingElement && stripe) createButton.disabled = guangzhou && !cart.size;
  const price = guangzhou ? { amount: cartTotal(currency) } : prices[currency];
  renderAmounts(currency, price, Boolean(taxCaseSelect.value));
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
  addressComplete = false;
  canConfirm = false;
  for (const element of mountedElements.splice(0)) element.destroy();
  checkoutFields.hidden = true;
  shippingFields.hidden = true;
  shippingOptions.hidden = true;
  quoteButton.disabled = true;
  submitGuangzhouButton.hidden = true;
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
  if (order.quote) renderGuangzhouQuote(order.quote);
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
  for (const [type, id] of [['calculation', order.tax_calculation_id], ['transaction', order.tax_transaction_id]]) {
    const field = document.querySelector(`#tax-${type}-id`);
    field.textContent = id || '';
    field.hidden = !id;
    document.querySelector(`#tax-${type}-label`).hidden = field.hidden;
  }
  document.querySelector('#order-status').textContent = paid ? '已支付' : '待支付';
  const paidVia = document.querySelector('#paid-via');
  paidVia.hidden = !paid;
  document.querySelector('#paid-via-label').hidden = !paid;
  paidVia.textContent = order.paid_via === 'webhook' ? 'Stripe Webhook' : '服务端查单';
  newOrderButton.hidden = false;
  newOrderButton.textContent = '开始新订单';
  checkButton.hidden = paid || !(order.payment_intent_id || order.checkout_session_id);
  if (paid) {
    resetPaymentForm();
    checkoutButton.hidden = true;
    startPaymentButton.hidden = true;
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
  if (shippingElement) return;
  if (!cart.size) throw new Error('请先将商品加入购物车。');
  const currency = currencySelect.value;
  const { country, decimals } = prices[currency];
  const formatter = currencyFormatter(currency);
  shippingMethod.replaceChildren();
  for (const [code, { label, amounts }] of Object.entries(shippingMethods)) {
    if (amounts[currency] != null) {
      shippingMethod.add(new Option(`${label} · ${formatter.format(amounts[currency] / 10 ** decimals)}`, code));
    }
  }
  const address = stripe.elements().create('address', {
    mode: 'shipping', allowedCountries: ['US', 'JP'], defaultValues: { address: { country } },
  });
  shippingElement = address;
  address.on('change', ({ complete }) => {
    if (shippingElement === address) {
      addressComplete = complete;
      invalidateQuote();
    }
  });
  shippingMethod.value = 'STANDARD';
  promotionCode.value = '';
  currencySelect.disabled = true;
  taxCaseSelect.disabled = true;
  createButton.disabled = true;
  newOrderButton.hidden = false;
  newOrderButton.textContent = '返回购物车';
  paymentForm.hidden = false;
  shippingFields.hidden = false;
  shippingOptions.hidden = false;
  mountElement(address, '#shipping-address-element');
  showMessage('填完收货地址后点击「计算税费」。');
}

function invalidateQuote() {
  quoteRevision += 1;
  quoteButton.disabled = quotePending || !addressComplete || !cart.size;
  if (!submitGuangzhouButton.hidden) {
    submitGuangzhouButton.hidden = true;
    pendingShipping = undefined;
    pendingPricing = undefined;
    renderPreview();
    showMessage('结算信息已更新，请重新计算税费。');
  }
}

async function preparePayment() {
  const path = `/api/orders/${activeOrder.id}`;
  const result = activeOrder.shop ? await api(`${path}/payment`, { method: 'POST' }) : await api(path, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      currency: activeOrder.currency, tax_case: activeOrder.tax_case, shipping: pendingShipping, ...pendingPricing,
    }),
  });
  if (activeOrder.tax_case === guangzhouTaxCase) {
    sessionStorage.setItem(storageKey, JSON.stringify(activeOrder));
    pendingShipping = undefined;
    pendingPricing = undefined;
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
  startPaymentButton.hidden = true;
  newOrderButton.hidden = true;
  showMessage(pendingShipping ? '正在按广州发货和收货地址计算税费。' : '正在准备 Stripe 支付。');
  try {
    await preparePayment();
  } catch (error) {
    resetPaymentForm();
    showMessage(error.message, 'error');
    if (activeOrder.shop) {
      startPaymentButton.textContent = '重试加载付款';
      startPaymentButton.hidden = false;
    } else {
      createButton.textContent = '重试创建订单';
      createButton.disabled = false;
    }
    newOrderButton.hidden = false;
  }
}

function showShopOrder(order) {
  currencySelect.disabled = true;
  taxCaseSelect.disabled = true;
  createButton.hidden = true;
  document.querySelector('#shop').hidden = true;
  renderOrder(order);
  startPaymentButton.hidden = order.status === 'paid';
  if (order.status !== 'paid') {
    startPaymentButton.textContent = order.payment_intent_id ? '继续付款' : '去付款';
    showMessage('订单已创建。核对金额后付款。');
  }
}

async function submitShopOrder() {
  createButton.disabled = true;
  newOrderButton.disabled = true;
  newOrderButton.textContent = '开始新订单';
  document.querySelector('#shop').hidden = true;
  showMessage('正在确认税费并创建订单。');
  try {
    const { order } = await api(`/api/orders/${activeOrder.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        currency: activeOrder.currency, tax_case: activeOrder.tax_case, shipping: pendingShipping, ...pendingPricing,
      }),
    });
    sessionStorage.setItem(storageKey, JSON.stringify(activeOrder));
    sessionStorage.removeItem(cartStorageKey);
    cart.clear();
    pendingShipping = undefined;
    pendingPricing = undefined;
    showShopOrder(order);
  } catch (error) {
    createButton.textContent = '重试创建订单';
    createButton.disabled = false;
    showMessage(error.message, 'error');
  } finally {
    newOrderButton.disabled = false;
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
        const taxTransactionPending = order.quote && order.tax_amount > 0 && !order.tax_transaction_id;
        if (order.status === 'paid' && !taxTransactionPending) {
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
shippingMethod.addEventListener('change', invalidateQuote);
promotionCode.addEventListener('input', invalidateQuote);

orderForm.addEventListener('submit', async event => {
  event.preventDefault();
  if (activeOrder?.shop) {
    if (pendingPricing) await submitShopOrder();
    return;
  }
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
  const revision = quoteRevision;
  quotePending = true;
  quoteButton.disabled = true;
  submitGuangzhouButton.hidden = true;
  renderPreview();
  newOrderButton.disabled = true;
  showMessage('正在按收货地址、优惠和运费试算。');
  try {
    const { complete, value } = await shippingElement.getValue();
    if (!complete) throw new Error('请填写完整的收货地址。');
    const pricing = {
      items: cartSelections(), shippingMethodCode: shippingMethod.value, promotionCode: promotionCode.value,
    };
    const quote = await api('/api/tax/quote', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ currency: currencySelect.value, shipping: value, ...pricing }),
    });
    if (revision !== quoteRevision) {
      showMessage('结算信息已更新，请重新计算税费。');
      return;
    }
    pendingShipping = value;
    pendingPricing = pricing;
    renderGuangzhouQuote(quote);
    submitGuangzhouButton.hidden = false;
    const invalidCoupon = pricing.promotionCode.trim() && quote.promotionCodeStatus !== 0;
    showMessage(invalidCoupon ? '优惠码无效。可修改优惠码，或按当前金额创建订单。' : '请核对结算明细，再创建订单。');
  } catch (error) {
    showMessage(error.message, 'error');
  } finally {
    quotePending = false;
    newOrderButton.disabled = false;
    quoteButton.disabled = !addressComplete || !cart.size;
  }
});

submitGuangzhouButton.addEventListener('click', async () => {
  if (submitGuangzhouButton.hidden || !pendingShipping || activeOrder) return;
  activeOrder = { id: crypto.randomUUID(), currency: currencySelect.value, tax_case: guangzhouTaxCase, shop: true };
  resetPaymentForm();
  await submitShopOrder();
});

startPaymentButton.addEventListener('click', () => loadOrder());

paymentForm.addEventListener('submit', async event => {
  event.preventDefault();
  if (confirming || !canConfirm) return;
  confirming = true;
  updatePayButton();
  checkButton.disabled = true;
  newOrderButton.disabled = true;
  showMessage('正在确认付款，请等待 Stripe 返回结果。');
  try {
    const paymentReturn = new URL(returnUrl);
    paymentReturn.searchParams.set('order_id', activeOrder.id);
    const result = checkoutActions
      ? await checkoutActions.confirm({ redirect: 'if_required' })
      : await stripe.confirmPayment({ elements, confirmParams: { return_url: paymentReturn.href }, redirect: 'if_required' });
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
  pendingPricing = undefined;
  sessionStorage.removeItem(storageKey);
  document.querySelector('#order-detail').hidden = true;
  checkoutButton.hidden = true;
  startPaymentButton.hidden = true;
  newOrderButton.hidden = true;
  checkButton.hidden = true;
  currencySelect.disabled = false;
  taxCaseSelect.disabled = false;
  createButton.hidden = false;
  createButton.disabled = false;
  renderTaxOptions(taxCaseSelect.value in taxCases ? taxCaseSelect.value : '');
  renderPreview();
  showMessage(taxCaseSelect.value === guangzhouTaxCase ? '选择商品，再填写收货地址。' : '选择币种和税费场景后创建订单。');
});

async function initialize() {
  try {
    const config = await api('/api/config');
    prices = config.prices;
    shippingMethods = config.shipping_methods;
    taxCases = config.tax_cases ?? {};
    products = new Map(config.catalog.map(product => [product.sku, product]));
    maxItemQuantity = config.max_item_quantity;
    const savedCart = JSON.parse(sessionStorage.getItem(cartStorageKey) || '[]');
    for (const { sku, quantity } of savedCart) {
      if (products.has(sku) && Number.isInteger(quantity) && quantity >= 1 && quantity <= maxItemQuantity) {
        cart.set(sku, quantity);
      }
    }
    buildCatalog();
    currencySelect.add(new Option('美元 USD', 'usd'));
    currencySelect.add(new Option('日元 JPY', 'jpy'));
    renderTaxOptions(guangzhouTaxCase);
    renderPreview();
    if (!config.publishable_key) {
      showMessage('请在 .env 填入 Stripe 沙箱密钥，再重启 Python 服务。');
      return;
    }
    if (window.Stripe) stripe = window.Stripe(config.publishable_key, { locale: 'zh' });
    activeOrder = JSON.parse(sessionStorage.getItem(storageKey) || 'null');
    if (returnedOrderId && activeOrder?.id !== returnedOrderId) {
      const order = await api(`/api/orders/${returnedOrderId}`);
      activeOrder = { id: order.id, currency: order.currency, tax_case: order.tax_case, shop: Boolean(order.quote?.items) };
      sessionStorage.setItem(storageKey, JSON.stringify(activeOrder));
    }
    if (activeOrder && prices[activeOrder.currency]) {
      currencySelect.value = activeOrder.currency;
      renderTaxOptions(activeOrder.tax_case ?? '');
      renderPreview();
      if (activeOrder.shop) {
        const order = await api(`/api/orders/${activeOrder.id}`);
        showShopOrder(order);
        if (redirected && order.status !== 'paid') {
          showMessage('正在查询 Stripe 支付结果。');
          await checkPayment({ waitForWebhook: true });
        }
        return;
      }
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
    createButton.disabled = !cart.size;
    showMessage('选择商品，再填写收货地址。');
  } catch (error) {
    showMessage(error.message, 'error');
  }
}

initialize();
