# 抽奖模块前端API（基于当前SQL表结构）

说明：接口面向前端，围绕三项功能。
1. 查询抽奖活动详情（前端）
2. 抽奖（前端）
3. 抽奖结果列表（前端）

以下字段与表映射（简要）：
- 活动：`jewelry_draw_activity`
- 规则：`jewelry_lottery_activity_rule`
- 奖品：`jewelry_draw_activity_award`
- 余额/状态：`jewelry_draw_activity_balance`
- 抽奖记录：`jewelry_draw_activity_draw_record`

通用约定：
- 认证：通过登录态或 `Authorization`。
- `user_id` 由服务端从登录态解析，不由前端传入。
- `activity_id` 为业务主键（字符串）。
- 时间格式：`yyyy-MM-dd HH:mm:ss`。

---

## 1) 查询抽奖活动详情（前端）

**GET** `/api/draw/activities/{activityId}`

**Query**
- `with_rules` 可选，`0/1`，默认 `0`。
- `with_awards` 可选，`0/1`，默认 `1`。

**Response 200**
```json
{
  "activity": {
    "activity_id": "A20260312",
    "unique_topic_id": "TOPIC_001",
    "start_time": "2026-03-12 00:00:00",
    "end_time": "2026-03-20 23:59:59",
    "status": "ONLINE",
    "user_total": 12888
  },
  "awards": [
    {
      "award_id": "AWD_001",
      "award_name": "单依纯演唱会门票*2",
      "award_type": "PRODUCT",
      "total_stock": 2,
      "stock": 1
    }
  ],
  "rules": [
    {
      "rule_type": "TIME",
      "rule_version": 1,
      "rule_json": {
        "time_ratios": [60, 25, 15],
        "release_ratios": [50, 30, 20]
      }
    },
    {
      "rule_type": "WEIGHT",
      "rule_version": 1,
      "rule_json": {
        "weights": { "AWD_001": 2, "AWD_002": 40, "AWD_009": 11893 },
        "fallback_award_id": "AWD_009"
      }
    },
    {
      "rule_type": "PITY",
      "rule_version": 1,
      "rule_json": {
        "pity_threshold": 26,
        "pity_allow_award_ids": ["AWD_002", "AWD_003"]
      }
    },
    {
      "rule_type": "UID",
      "rule_version": 1,
      "rule_json": {
        "uid_whitelist": { "AWD_001": ["10001", "10002"] }
      }
    },
    {
      "rule_type": "LIMIT",
      "rule_version": 1,
      "rule_json": {
        "per_user_limit": { "AWD_001": 1 }
      }
    }
  ],
  "user_state": {
    "draw_remaining": 1,
    "miss_streak": 3,
    "total_draws": 12,
    "total_wins": 2,
    "last_draw_time": "2026-03-12 10:02:11",
    "last_win_time": "2026-03-10 08:31:00",
    "last_non_fallback_win_time": "2026-03-09 21:11:00"
  }
}
```

**说明**
- `user_state` 来自 `jewelry_draw_activity_balance`。
- 若 `with_rules=1`，返回 `jewelry_draw_activity_rule` 的当前生效规则。
- `awards` 返回库存与基础信息，规则细节由 `rules` 给出。

---

## 2) 抽奖（前端）

**POST** `/api/draw/activities/{activityId}/draw`

**Request Body**
```json
{
  "draw_id": "DRAW_20260312_00001",
  "client_time": "2026-03-12 10:22:01"
}
```

**Response 200**
```json
{
  "draw_id": "DRAW_20260312_00001",
  "activity_id": "A20260312",
  "award": {
    "award_id": "AWD_006",
    "award_name": "亚克力相框挂件",
    "award_type": "PRODUCT"
  },
  "hit_type": "NORMAL",
  "status": "SUCCESS",
  "miss_streak_before": 3,
  "miss_streak_after": 0,
  "draw_remaining": 0
}
```

**说明**
- 幂等：`draw_id` 唯一（建议后端校验）。
- 规则解析：由 `jewelry_draw_activity_rule` 决定阶段、保底、白名单等。
- 抽中后写入 `jewelry_draw_activity_draw_record`。
- 余额与连续未中次数更新至 `jewelry_draw_activity_balance`。

**可能错误码**
- `ACTIVITY_OFFLINE` 活动未上线或已结束
- `NO_DRAW_REMAINING` 抽奖次数不足
- `AWARD_OUT_OF_STOCK` 可用奖品为 0（可能回落兜底）
- `RULE_NOT_FOUND` 活动规则缺失

---

## 3) 抽奖结果列表（前端）

**GET** `/api/draw/activities/{activityId}/records`

**Query**
- `page` 默认 `1`
- `page_size` 默认 `20`

**Response 200**
```json
{
  "page": 1,
  "page_size": 20,
  "total": 125,
  "records": [
    {
      "draw_id": "DRAW_20260312_00001",
      "award_id": "AWD_006",
      "award_name": "亚克力相框挂件",
      "award_type": "PRODUCT",
      "hit_type": "NORMAL",
      "status": "SUCCESS",
      "create_time": "2026-03-12 10:22:01"
    }
  ]
}
```

**说明**
- 查询来自 `jewelry_draw_activity_draw_record`。
- 需要展示奖品名称时可 join `jewelry_draw_activity_award` 或做缓存映射。

---

## 规则字段参考结构（按类型拆分）

**TIME**
```json
{
  "time_ratios": [60, 25, 15],
  "release_ratios": [50, 30, 20]
}
```

**WEIGHT**
```json
{
  "weights": { "AWD_001": 2, "AWD_002": 40, "AWD_009": 11893 },
  "fallback_award_id": "AWD_009"
}
```

**PITY**
```json
{
  "pity_threshold": 26,
  "pity_allow_award_ids": ["AWD_002", "AWD_003"]
}
```

**UID**
```json
{
  "uid_whitelist": { "AWD_001": ["10001", "10002"] }
}
```

**LIMIT**
```json
{
  "per_user_limit": { "AWD_001": 1 }
}
```
