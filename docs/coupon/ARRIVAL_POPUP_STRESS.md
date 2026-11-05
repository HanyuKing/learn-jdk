# 到账弹窗与发券服务压测

接口为 `POST /internal/coupon/v1/stress/arrival-popup`，压测请求直接并发调用 `ArrivalPopupQueryService.query`、`ArrivalPopupService.popup` 或 `CouponIssueService.issueToUser`，统计的是 Service 调用延迟。

以下示例中的地址和租户 ID 需要替换为隔离 QA 环境的实际值：

```bash
export ORDER_API="http://qa-order-api:8080"
export QA_TENANT_ID="qa-isolated-tenant"
```

## 参数约束

- `templateCount` 是本次自动创建的模板数，范围 1 到 100；模板为全店立减 0.01 元、每人限领 1 张、无限库存、领取后 7 天有效。压测结束后服务自动作废模板，已发出的用户券保留。
- `audienceType` 只能是 `ALL_USER` 或 `NEW_USER`。普通用户场景省略 `userIds`，服务按 `userCount` 生成随机 ID，不写入 `biz_user`。新人券场景必须传真实 `userIds`，数量须等于 `userCount`；账号需在模板规定的注册期限内，且不能已有新人券。
- `QUERY_NEWCOMER` 固定使用 `NEW_USER`。新人券自动领取场景只能创建一个模板；`ISSUE_*` 中的 `NEW_USER` 只允许 `issueSourceType=AUTO_RECEIVE`，`DIRECTED + NEW_USER` 会被拒绝。
- 多用户弹窗和发券场景要求 `userCount=requestCount`，每个请求使用不同用户。单用户场景要求 `userCount=1`，并发弹窗或发券至少需要 2 个线程和 2 个请求。查询场景可以让多个请求轮询使用 `userIds`。
- 弹窗和发券场景的 `warmUpCount` 必须为 0，防止预热改变测试数据。查询场景可以预热，预热调用不计入样本和 TPS。
- `ISSUE_*` 必须传 `issueBizKey` 和 `issueSourceType`。同一轮压测中，相同用户和模板生成相同幂等 `requestNo`，可观察 `CREATED` 与 `REPLAYED`；每次新压测建议使用新的 `issueBizKey`。新人券自动领取的幂等键按用户维度生成。
- 通用上限：`threadCount` 32、`requestCount` 1000、`userCount` 1000、查询预热 100 次、超时 120 秒。服务同时只允许一场压测运行。弹窗/发券用户应使用未领过本轮测试券且没有未处理到账弹窗的隔离 QA 账号。
- 为保证候选券核对准确，租户应为压测专用租户，避免存在本次以外的有效候选模板。

## 查询全部候选 `QUERY_ALL`

普通自动券模板由服务创建，普通用户 ID 自动生成；查询请求会在用户间轮询。

```bash
curl --request POST "${ORDER_API}/internal/coupon/v1/stress/arrival-popup" \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "mode": "QUERY_ALL",
    "tenantId": "'"${QA_TENANT_ID}"'",
    "templateCount": 2,
    "userCount": 5,
    "audienceType": "ALL_USER",
    "threadCount": 4,
    "requestCount": 20,
    "warmUpCount": 3,
    "timeoutSeconds": 60
  }'
```

## 仅查询新人券 `QUERY_NEWCOMER`

传入真实新人账号；所有账号都必须满足新人注册期限，并且没有已领取的新人券。

```bash
curl --request POST "${ORDER_API}/internal/coupon/v1/stress/arrival-popup" \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "mode": "QUERY_NEWCOMER",
    "tenantId": "'"${QA_TENANT_ID}"'",
    "templateCount": 1,
    "userCount": 2,
    "audienceType": "NEW_USER",
    "userIds": ["qa-new-user-001", "qa-new-user-002"],
    "threadCount": 4,
    "requestCount": 20,
    "warmUpCount": 3,
    "timeoutSeconds": 60
  }'
```

## 多用户弹窗 `POPUP_MULTI_USER`

`userCount` 必须等于 `requestCount`。普通场景无需传 `userIds`，服务生成不同的随机 ID；如需真实新人用户，将 `audienceType` 改为 `NEW_USER` 并提供等量合格账号，同时将 `templateCount` 设为 1。

```bash
curl --request POST "${ORDER_API}/internal/coupon/v1/stress/arrival-popup" \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "mode": "POPUP_MULTI_USER",
    "tenantId": "'"${QA_TENANT_ID}"'",
    "templateCount": 2,
    "userCount": 5,
    "audienceType": "ALL_USER",
    "threadCount": 5,
    "requestCount": 5,
    "warmUpCount": 0,
    "timeoutSeconds": 60
  }'
```

## 同一用户并发弹窗 `POPUP_SINGLE_USER`

普通场景使用服务生成的单个随机用户；请求数至少为 2。锁忙造成的空弹窗会单独计数，并检查同一用户最多只有一个请求返回弹窗内容。

```bash
curl --request POST "${ORDER_API}/internal/coupon/v1/stress/arrival-popup" \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "mode": "POPUP_SINGLE_USER",
    "tenantId": "'"${QA_TENANT_ID}"'",
    "templateCount": 2,
    "userCount": 1,
    "audienceType": "ALL_USER",
    "threadCount": 10,
    "requestCount": 20,
    "warmUpCount": 0,
    "timeoutSeconds": 60
  }'
```

## 多用户发券 `ISSUE_MULTI_USER`

此例使用全部用户人群和 `DIRECTED` 来源，自动创建定向发券模板；每个请求使用独立的随机用户。普通用户 ID 不要求预先写入用户表。

```bash
curl --request POST "${ORDER_API}/internal/coupon/v1/stress/arrival-popup" \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "mode": "ISSUE_MULTI_USER",
    "tenantId": "'"${QA_TENANT_ID}"'",
    "templateCount": 2,
    "userCount": 5,
    "audienceType": "ALL_USER",
    "issueBizKey": "issue-run-20261105-multi-01",
    "issueSourceType": "DIRECTED",
    "threadCount": 5,
    "requestCount": 5,
    "warmUpCount": 0,
    "timeoutSeconds": 60
  }'
```

如果要测试普通自动领取来源，可将来源改为 `AUTO_RECEIVE`；`ALL_USER + AUTO_RECEIVE` 创建普通自动券。`NEW_USER + AUTO_RECEIVE` 则必须提供真实 `userIds`、符合新人资格，并且只能创建一个模板。

## 同一用户并发发券 `ISSUE_SINGLE_USER`

此例覆盖新人券自动发放。调用前需准备尚未领取过新人券、且注册时间符合新人期限的 QA 账号。同一轮的并发调用会使用用户级新人券幂等键。

```bash
curl --request POST "${ORDER_API}/internal/coupon/v1/stress/arrival-popup" \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "mode": "ISSUE_SINGLE_USER",
    "tenantId": "'"${QA_TENANT_ID}"'",
    "templateCount": 1,
    "userCount": 1,
    "audienceType": "NEW_USER",
    "userIds": ["qa-new-user-003"],
    "issueBizKey": "issue-run-20261105-new-user-01",
    "issueSourceType": "AUTO_RECEIVE",
    "threadCount": 10,
    "requestCount": 20,
    "warmUpCount": 0,
    "timeoutSeconds": 60
  }'
```

## 报告字段

报告会返回本次创建的 `preparedTemplateIds`、实际使用的 `resolvedUserIds`、`templateCleanupStatus` 与 `templateCleanupErrors`。清理状态为 `SUCCESS`、`FAILED` 或 `DEFERRED`；超时后若 Service worker 仍在运行，报告为 `DEFERRED`，最后一个 worker 退出后才会作废模板。

性能指标包括样本数、TP99、TP999、TPS、成功/异常/空弹窗数。发券报告还包含 `CREATED`、`REPLAYED`、发券失败数和重复券 ID；变更型场景按用户对账，报告预期与实际券数、模板差异以及多发/少发。弹窗场景还检查重复返回券 ID 和同一用户是否有多个请求返回弹窗内容。
