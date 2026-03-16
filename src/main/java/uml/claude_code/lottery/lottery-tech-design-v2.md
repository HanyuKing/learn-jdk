# 珠宝抽奖活动 — 技术设计文档

> 版本：2.0 | 日期：2026-03-16

---

## 目录

1. [背景与核心目标](#1-背景与核心目标)
2. [关键技术决策](#2-关键技术决策)
3. [整体架构](#3-整体架构)
4. [数据存储设计](#4-数据存储设计)
5. [核心算法设计](#5-核心算法设计)
6. [详细时序图](#6-详细时序图)
7. [程序流程图](#7-程序流程图)
8. [高并发设计](#8-高并发设计)
9. [高可用与一致性设计](#9-高可用与一致性设计)
10. [前端 API 接口](#10-前端-api-接口)
11. [监控与告警](#11-监控与告警)

---

## 1. 背景与核心目标

### 1.1 奖池基准（13000 权重）

| 等级 | 奖品 | 数量 | 权重 | 概率 |
|------|------|------|------|------|
| A（大奖） | 单依纯演唱会门票×2 | 2 | 2 | 0.02% |
| A（大奖） | 游乐场门票×2 | 2 | 2 | 0.02% |
| A（大奖） | 超大公仔（一对） | 3 | 3 | 0.02% |
| B（中等奖） | 毛绒挂件 | 40 | 40 | 0.31% |
| B（中等奖） | 早安机 | 10 | 10 | 0.08% |
| B（中等奖） | 毛绒背包 | 25 | 25 | 0.19% |
| B（中等奖） | 保温杯 | 25 | 25 | 0.19% |
| C（小奖） | 亚克力相框挂件 | 1000 | 1000 | 7.69% |
| FALLBACK（兜底） | 想要票×1 | 不限 | 11893 | 91.48% |

### 1.2 四个核心目标

| # | 目标 | 实现策略 |
|---|------|---------|
| 1 | 应发尽发（尽量） | 累计配额 + 自然追差 |
| 2 | 时间均匀，前期不透支 | 三段阶段累计上限控制 |
| 3 | 保底最终 100%（非即时） | miss_streak 持续累加，有可用奖时触发 |
| 4 | 演唱会门票指定 UID + 限中一次 | UID rule + draw_record 查询 |

> **目标3说明**：保底是"最终保障"，不是"即时保障"。
> 当阶段配额已满（可用量=0）时，保底延后，miss_streak 继续累加；
> 一旦下一阶段配额释放，下次抽奖立即触发保底。
> 这样阶段配额不会被保底超发。

---

## 2. 关键技术决策

| 决策点 | 结论 | 原因 |
|--------|------|------|
| 是否新增表 | **不新增** | 现有 5 张表已足够 |
| 可用量计算方式 | **累计法** | 与 per-stage+rollover 数学等价，但更简单、阶段修改无需迁移 |
| 阶段配额存储 | **rule 表 QUOTA 类型** | rule_json 存比例配置，issued 从 draw_record COUNT |
| UID 命中记录 | **查 draw_record** | 无需独立白名单表，draw_record 是权威数据源 |
| 保底语义 | **最终 100%** | 即时 100% 会破坏阶段配额控制（目标2与目标3取舍） |
| miss_streak 更新 | **始终 INCR（方案B）** | 无冻结状态，逻辑最简单，INCR 一条命令 |
| 库存扣减 | **Redis Lua 原子脚本** | 保证高并发下不超发 |
| 幂等保障 | **requestId + drawId 分离** | 客户端生成 requestId 防重，后端生成 drawId 作记录主键 |

---

## 3. 整体架构

```
┌──────────────────────────────────────────────────────────────────┐
│                        客户端（H5 / APP）                         │
└───────────────────────────────┬──────────────────────────────────┘
                                │ HTTPS
┌───────────────────────────────▼──────────────────────────────────┐
│                           API 网关                                │
│               鉴权（JWT）  /  限流（令牌桶）  /  路由              │
└───────────────────────────────┬──────────────────────────────────┘
                                │
┌───────────────────────────────▼──────────────────────────────────┐
│                         抽奖服务（无状态，可水平扩展）              │
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐   │
│  │  活动配置缓存 │  │   规则引擎   │  │     抽奖核心         │   │
│  │  (本地+Redis) │  │ (TIME/WEIGHT │  │  (算法 + 保底 + UID) │   │
│  └──────────────┘  │  /PITY/UID)  │  └──────────────────────┘   │
│                    └──────────────┘                               │
└────────┬────────────────────┬──────────────────┬─────────────────┘
         │                   │                  │
┌────────▼────────┐  ┌───────▼────────┐  ┌─────▼──────────────┐
│     MySQL       │  │  Redis Cluster │  │    消息队列 (MQ)    │
│ (持久化/幂等)   │  │ (库存/锁/计数) │  │  (异步发奖/通知)   │
└─────────────────┘  └────────────────┘  └────────────────────┘
                                                  │
                                         ┌────────▼───────────┐
                                         │     发奖服务        │
                                         │ (实物/虚拟币发放)   │
                                         └────────────────────┘
```

---

## 4. 数据存储设计

### 4.1 MySQL 表（共 5 张，不新增）

#### jewelry_lottery_award — 奖品表

```sql
award_type  : WANT(兜底) / PRODUCT(实物)
award_level : A(大奖) / B(中等奖) / C(小奖) / FALLBACK(兜底)
              字母枚举便于扩展（如新增 D 等级），保底按 PITY rule 中 priority 顺序依次查找对应 level 的可用奖品
total_stock : 活动总库存，活动开始后不修改
stock       : 当前剩余库存，随每次发放扣减（全局上限兜底用）
```

> **注意**：`award_level` 为新增字段（`DEFAULT 'FALLBACK'`）。
> 执行 DDL 前需确认代码中无 `INSERT INTO jewelry_lottery_award VALUES (...)` 的不写列名写法，
> 执行后需手动 UPDATE 存量数据的 `award_level` 值。

#### jewelry_lottery_rule — 规则表（存所有配置）

所有活动配置通过 `rule_type` 区分，`rule_json` 存具体内容：

**TIME** — 活动时间与阶段定义

```json
{
  "activityName": "珠宝春季抽奖",
  "start": "2026-04-01T10:00:00+08:00",
  "end":   "2026-04-30T23:59:59+08:00",
  "status": "RUNNING",
  "phases": [60, 25, 15]
}
```

> `phases` 表示三段时间各占活动总时长的百分比，合计 100。

**WEIGHT** — 各奖品基准权重

```json
{
  "award_concert": 2,
  "award_park":    2,
  "award_doll":    3,
  "award_plush":   40,
  "award_alarm":   10,
  "award_bag":     25,
  "award_cup":     25,
  "award_acrylic": 1000,
  "award_fallback": 11893
}
```

**QUOTA** — 各阶段累计释放比例

```json
{
  "stage_ratios": [50, 30, 20]
}
```

> 含义：截至第1/2/3阶段结束，各奖品**累计**允许释放的比例分别为 50% / 80% / 100%。

**PITY** — 保底配置

```json
{
  "threshold": 26,
  "exclude_levels": ["A"],
  "priority": ["C", "B"]
}
```

**UID** — 演唱会门票指定用户

```json
{
  "award_id": "award_concert",
  "uids": ["u001", "u002", "u003"]
}
```

---

#### jewelry_lottery_activity_balance — 用户抽奖余额

```
lottery_remaining : 剩余可抽次数（抽一次减1）
miss_streak       : 连续未中实物奖次数（保底计数，始终累加，中奖后归0）
total_lottery     : 累计抽奖次数（统计用）
total_wins        : 累计中非兜底次数（统计用）
```

#### jewelry_lottery_activity_balance_log — 余额流水

```
biz_type : TASK（任务充值）/ DRAW（消耗）
biz_id   : 业务唯一ID，UNIQUE KEY 保幂等
use      : +N 充值 / -1 消耗
```

#### jewelry_lottery_activity_draw_record — 抽奖记录

```
draw_id         : 后端生成的全局唯一ID（UUID/雪花ID），UNIQUE KEY 保幂等
stage_no        : 抽奖时所处阶段（1/2/3），用于审计和统计
weight_snapshot : 抽奖时本奖品的有效权重（快照，便于追溯）
status          : INIT → SUCCESS / FAIL
fail_reason     : STOCK_EMPTY / STAGE_QUOTA_EMPTY / SYSTEM_ERROR
```

---

### 4.2 索引补充建议

```sql
-- draw_record 需支持：按 activity+award+status 统计 issued 数量（可用量计算）
ALTER TABLE jewelry_lottery_activity_draw_record
  ADD INDEX idx_activity_award_status (activity_id, award_id, status);

-- draw_record 支持：查询某用户是否中过指定奖品（UID 命中检查）
-- 已有 idx_user_id，联合查询足够
```

---

### 4.3 Redis 设计

#### Key 清单

| Key | 类型 | 说明 | TTL |
|-----|------|------|-----|
| `lottery:config:{actId}` | Hash | 活动配置（TIME rule 缓存） | 活动结束 +1h |
| `lottery:rules:{actId}` | Hash | 所有规则 rule_json（field=rule_type） | 同上 |
| `lottery:stock:{actId}:{awardId}` | String | 全局剩余库存（原子扣减主链路） | 同上 |
| `lottery:issued:{actId}:{awardId}` | String | 累计已发放数量（加速 available 计算） | 同上 |
| `lottery:uid_won:{actId}:{awardId}:{userId}` | String | UID 是否已命中指定奖品（1=已中） | 同上 |
| `lottery:miss:{actId}:{userId}` | String | 用户连续未中次数（miss_streak） | 活动结束 +1d |
| `lottery:draw_lock:{actId}:{userId}` | String | 用户级抽奖互斥锁 | 5s（超时自动释放） |
| `lottery:idem:{requestId}` | String | 请求幂等缓存（值=drawId，防客户端重试重复处理） | 10min |

---

#### Lua 脚本 1：库存原子扣减

```lua
-- deduct_stock.lua
-- KEYS[1] = lottery:stock:{actId}:{awardId}    全局库存
-- KEYS[2] = lottery:issued:{actId}:{awardId}   累计已发
-- ARGV[1] = cumulativeAllowed                  本阶段累计允许发放上限（由 Java 层计算传入）
-- 返回: 1=成功  -1=全局库存不足  -2=阶段配额已满

local stock   = tonumber(redis.call('GET', KEYS[1]) or 0)
local issued  = tonumber(redis.call('GET', KEYS[2]) or 0)
local allowed = tonumber(ARGV[1])

if stock <= 0 then
    return -1  -- 全局库存耗尽
end
if issued >= allowed then
    return -2  -- 当前阶段累计配额已满
end

redis.call('DECR',  KEYS[1])
redis.call('INCR',  KEYS[2])
return 1
```

> `cumulativeAllowed` 由 Java 层根据 QUOTA rule 和当前阶段计算后传入，
> 避免在 Lua 中解析 JSON，保持脚本简单。

---

#### Lua 脚本 2：miss_streak 更新

```lua
-- update_miss.lua
-- KEYS[1] = lottery:miss:{actId}:{userId}
-- ARGV[1] = isWin  1=中实物奖  0=兜底
-- 返回：更新后的 miss_streak

if tonumber(ARGV[1]) == 1 then
    redis.call('SET', KEYS[1], 0)
    return 0
else
    return tonumber(redis.call('INCR', KEYS[1]))
end
```

---

## 5. 核心算法设计

### 5.1 阶段判断

```java
int getCurrentStage(long startMs, long endMs, int[] phases) {
    long now = System.currentTimeMillis();
    double progress = (double)(now - startMs) / (endMs - startMs) * 100;
    int cumulative = 0;
    for (int i = 0; i < phases.length; i++) {
        cumulative += phases[i];
        if (progress <= cumulative) return i + 1;  // stage 从1开始
    }
    return phases.length;  // 末期
}
```

### 5.2 累计可用量计算（累计法）

```java
// stageRatios = [50, 30, 20]
// currentStage = 2  →  cumulativePct = 50+30 = 80
int cumulativePct = IntStream.range(0, currentStage)
                             .map(i -> stageRatios[i])
                             .sum();
int cumulativeAllowed = totalStock * cumulativePct / 100;

// totalIssued 优先从 Redis lottery:issued 读，缓存miss才查DB
int totalIssued = redisGet("lottery:issued:{actId}:{awardId}");
int available   = Math.max(0, cumulativeAllowed - totalIssued);
```

> **追差天然成立**：前期少发了，cumulative 不变，available 自动变大，无需额外字段。
> 阶段比例修改只需更新 QUOTA rule_json，下次读缓存即生效，无需数据迁移。

### 5.3 权重池构建

```java
// 排除可用量=0 的奖品（兜底始终保留）
List<WeightRange> pool = new ArrayList<>();
int cursor = 0;
for (Award award : awards) {
    if (award.isFallback()) continue;           // 兜底最后追加
    if (available(award) <= 0) continue;        // 可用量0跳过
    if (isUidRestricted(award) && !inWhitelist(userId, award)) continue; // UID限制
    pool.add(new WeightRange(cursor + 1, cursor + award.baseWeight, award));
    cursor += award.baseWeight;
}
// 兜底占剩余权重
int totalWeight = 13000;
pool.add(new WeightRange(cursor + 1, totalWeight, FALLBACK_AWARD));

int rand = ThreadLocalRandom.current().nextInt(1, totalWeight + 1);
return pool.stream().filter(r -> rand >= r.start && rand <= r.end)
           .findFirst().map(r -> r.award).orElse(FALLBACK_AWARD);
```

### 5.4 保底决策

```java
Award decideDraw(String userId, String actId, PityConfig pity, List<Award> awards) {

    int missStreak = redisGetMissStreak(actId, userId);  // GET，不修改

    // 1. UID 白名单检查（演唱会门票）
    if (inUidWhitelist(userId, actId)) {
        Award concert = findAward("award_concert");
        boolean alreadyWon = drawRecordMapper.existsSuccess(userId, actId, "award_concert");
        if (!alreadyWon && available(concert) > 0) {
            return concert;  // 100% 命中
        }
    }

    // 2. 保底检查（最终保证，非即时）
    if (missStreak >= pity.threshold) {
        for (String level : pity.priority) {   // ["C", "B"]  即小奖优先，中等奖次之
            Award candidate = findAvailableByLevel(awards, level);
            if (candidate != null) {
                return candidate;  // 保底触发
            }
        }
        // 所有非兜底奖不可用 → 保底延后，miss_streak 继续累加，走正常抽取
    }

    // 3. 正常权重抽取
    return weightedDraw(userId, awards);
}
```

---

## 6. 详细时序图

### 6.1 完整抽奖时序（主流程）

```mermaid
    actor U as "用户"
    participant FE as "前端"
    participant GW as "API网关"
    participant LS as "抽奖服务"
    participant LC as "本地缓存"
    participant RC as "Redis"
    participant DB as "MySQL"
    participant MQ as "消息队列"

    U->>FE: 点击抽奖
    FE->>FE: 生成 requestId（UUID）
    FE->>GW: POST /lottery/draw {activityId, requestId}
    GW->>GW: JWT鉴权 + 令牌桶限流
    GW->>LS: 转发请求

    LS->>RC: GET idem:{requestId}
    RC-->>LS: null（首次请求）
    Note over LS: 非null直接返回缓存结果（幂等）
    LS->>LS: 生成 drawId（UUID）

    LS->>LC: 读本地配置缓存
    LC-->>LS: miss
    LS->>RC: HGETALL rules:{actId}
    RC-->>LS: TIME / WEIGHT / QUOTA / PITY / UID
    LS->>LC: 写本地缓存 TTL 30s
    LS->>LS: 校验时间 + 计算 stage_no + cumulativeAllowed

    LS->>RC: SET draw_lock:{actId}:{userId} NX EX 10
    RC-->>LS: OK
    Note over LS,RC: 获取锁失败返回 TOO_MANY_REQUESTS

    LS->>DB: BEGIN TX balance-1 + balance_log(redis_applied=false) COMMIT
    DB-->>LS: OK
    Note over LS,DB: affected=0 → 次数不足，释放锁返回错误

    LS->>RC: Pipeline GET miss + MGET stock/issued + GET uid_won
    RC-->>LS: missStreak=N，各奖品可用量，uid_won状态

    LS->>LS: 抽奖决策 UID白名单 / 保底 / 正常权重

    alt 命中实物奖
        LS->>RC: EVAL deduct_stock.lua [cumulativeAllowed]
        RC-->>LS: 1=成功 / -1=库存空 / -2=阶段满
        Note over LS: 扣减失败降级FALLBACK
    end

    LS->>RC: EVAL update_miss.lua [isWin]
    RC-->>LS: 新的 miss_streak

    LS->>DB: BEGIN TX INSERT draw_record + UPDATE balance_log(redis_applied=true) COMMIT
    DB-->>LS: OK（draw_record落盘 + WAL checkpoint完成）

    LS->>RC: Pipeline SET idem:{requestId} EX 600 + DEL draw_lock
    RC-->>LS: OK

    LS-->>GW: {drawId, awardId, awardName, isWin, isPity, remaining}
    GW-->>FE: HTTP 200
    FE->>U: 展示抽奖结果

    LS->>MQ: 异步 AWARD_EVENT {drawId, userId, awardId}
```

---

### 6.2 并发抢库存时序

```mermaid
    participant U1 as "用户A"
    participant U2 as "用户B"
    participant LS as "抽奖服务"
    participant RC as "Redis"

    Note over U1,U2: 两用户同时权重命中亚克力相框挂件（库存=1，阶段remaining=1）

    U1->>RC: EVAL deduct_stock.lua [cumulativeAllowed=500]
    Note over RC: stock=1, issued=499, allowed=500 通过
    RC-->>U1: 1（扣减成功）stock=0, issued=500

    U2->>RC: EVAL deduct_stock.lua [cumulativeAllowed=500]
    Note over RC: issued=500, allowed=500 阶段配额满
    RC-->>U2: -2（阶段配额满）

    Note over LS: U2 降级为 FALLBACK 想要票x1
```

---

### 6.3 保底触发时序

```mermaid
    actor U as "用户 miss_streak=25 threshold=26"
    participant LS as "抽奖服务"
    participant RC as "Redis"
    participant DB as "MySQL"

    U->>LS: 第26次抽奖
    LS->>RC: GET miss:{actId}:{userId}
    RC-->>LS: 25（未达阈值）
    LS->>LS: 正常权重抽取命中FALLBACK
    LS->>RC: EVAL update_miss.lua [isWin=0]
    RC-->>LS: miss_streak=26（INCR后达到阈值）
    LS-->>U: 想要票x1

    U->>LS: 第27次抽奖
    LS->>RC: GET miss:{actId}:{userId}
    RC-->>LS: 26（>=threshold）

    alt C(小奖) available > 0
        LS->>RC: EVAL deduct_stock.lua（小奖）
        RC-->>LS: 1（成功）
        LS->>RC: EVAL update_miss.lua [isWin=1]
        RC-->>LS: miss_streak=0
        LS-->>U: 亚克力相框挂件（保底命中）
    else C不可用查找B(中等奖)
        LS->>LS: 尝试B级奖品
    else 所有非兜底奖不可用（保底延后）
        LS->>LS: 走正常权重抽取FALLBACK
        LS->>RC: EVAL update_miss.lua [isWin=0]
        RC-->>LS: miss_streak=27（继续累加）
        LS-->>U: 想要票x1（保底延后，下次阶段释放后触发）
    end
```

---

### 6.4 异步发奖时序

```mermaid
    participant MQ as "消息队列"
    participant AS as "发奖服务"
    participant NS as "通知服务"
    participant DB as "MySQL"

    MQ->>AS: 消费 AWARD_EVENT {drawId, userId, awardId, awardType}
    AS->>DB: SELECT draw_record WHERE draw_id=? AND status=SUCCESS
    DB-->>AS: 记录存在（幂等校验）

    alt awardType=PRODUCT 实物
        AS->>DB: INSERT ship_order(drawId, userId, awardId, PENDING)
        AS->>NS: 推送通知 您已中奖请填写收货地址
    else awardType=WANT 虚拟票
        AS->>DB: UPDATE user_wallet SET want_ticket+=N
        AS->>NS: 推送通知 想要票x1已到账
    end

    AS->>MQ: ACK
    Note over AS,MQ: 失败重试3次进死信队列人工介入
```

---

### 6.5 Redis 故障降级时序

```mermaid
    participant LS as "抽奖服务"
    participant CB as "熔断器"
    participant RC as "Redis"
    participant DB as "MySQL"

    LS->>CB: 请求Redis扣库存
    CB->>RC: EVAL deduct_stock.lua

    alt Redis正常
        RC-->>CB: 1（成功）
        CB-->>LS: 正常响应
    else Redis超时或不可用熔断器开启
        CB-->>LS: 熔断走降级路径
        LS->>DB: SELECT FOR UPDATE award WHERE award_id=?
        DB-->>LS: stock=N
        alt stock>0 AND available>0
            LS->>DB: UPDATE award SET stock=stock-1 WHERE stock>0
            DB-->>LS: 扣减成功
        else
            LS-->>LS: 降级为FALLBACK
        end
        Note over LS,DB: Redis恢复后由WAL恢复任务重建Redis库存
    end
```

---

## 7. 程序流程图

### 7.1 抽奖主流程

```mermaid
flowchart TD
    A([用户发起抽奖]) --> B{活动时间校验}
    B -- 不在时间内 --> ERR1[返回: 活动未开始/已结束]
    B -- 通过 --> C{幂等检查\ndrawId 是否已处理}
    C -- 已处理 --> RET0[直接返回原结果]
    C -- 首次 --> D[获取用户分布式锁]
    D -- 获取失败 --> ERR2[返回: 请求过于频繁]
    D -- 获取成功 --> E{扣减用户抽奖次数\n乐观锁 remaining-1}
    E -- remaining=0 --> ERR3[释放锁, 返回: 次数不足]
    E -- 成功 --> F[计算当前阶段 stage_no\n及各奖品 cumulativeAllowed]

    F --> G{UID 白名单检查}
    G -- 在名单内且未命中过\n且 available>0 --> H[强制命中演唱会门票]
    G -- 否 --> I{miss_streak >= threshold?}

    I -- 是 --> J{C(小奖) 可用量>0?}
    J -- 是 --> K[保底命中小奖]
    J -- 否 --> L{B(中等奖) 可用量>0?}
    L -- 是 --> M[保底命中中等奖]
    L -- 否 --> N[保底延后\n走正常抽取]
    I -- 否 --> N

    N --> O[构建权重区间\n排除 available=0 的奖品]
    O --> P[random(1~totalWeight)\n命中 awardId]

    H --> Q{命中实物奖?}
    K --> Q
    M --> Q
    P --> Q

    Q -- 是 --> R[Redis Lua 原子扣库存]
    R -- 成功 --> S[isWin=true]
    R -- -1 全局库存空\n-2 阶段配额满 --> T[降级 FALLBACK\nisWin=false]
    Q -- FALLBACK --> T

    S --> U[update_miss.lua\nmiss_streak=0]
    T --> V[update_miss.lua\nmiss_streak++]

    U --> W[写 draw_record\nstatus=SUCCESS]
    V --> W
    W --> X[写幂等缓存 idem]
    X --> Y[发 MQ 异步发奖]
    Y --> Z[释放分布式锁]
    Z --> END([返回抽奖结果])
```

---

## 8. 高并发设计

### 8.1 并发控制层次

```
层次1：API 网关限流
  - 全局 QPS 限制（令牌桶，e.g. 2000 QPS）
  - 单用户 QPS 限制（e.g. 每秒 1 次）
  - 活动维度限流（防刷接口）

层次2：用户级分布式锁
  - SET lottery:draw_lock:{actId}:{userId} 1 NX EX 10
  - 同一用户同一时刻只能有一次抽奖在处理
  - 防止前端重复点击或网络重传导致的并发

层次3：库存原子扣减（Redis Lua）
  - 全局库存 + 阶段配额在同一 Lua 脚本内原子检查+扣减
  - 不可能超发

层次4：数据库乐观锁
  - UPDATE balance SET remaining=remaining-1
    WHERE userId=? AND remaining>0
  - affected=0 即次数不足，无需悲观锁
```

### 8.2 热点数据缓存策略

| 数据 | 缓存位置 | 更新时机 |
|------|---------|---------|
| 活动配置（TIME/WEIGHT/PITY/UID rule） | Redis + 本地 30s | 运营修改规则后主动失效 |
| 奖品列表 | Redis | 同上 |
| 奖品全局库存 `stock` | Redis（原子扣减主链路） | 每次发放 DECR |
| 奖品累计已发 `issued` | Redis（INCR） | 每次发放 INCR |
| 用户 `miss_streak` | Redis（INCR/SET） | 每次抽奖后 |
| 用户余额 `remaining` | **不缓存** | DB 乐观锁直接扣，量级不高 |

### 8.3 写 DB 异步化

```
抽奖主链路（同步）：
  扣减余额(DB) → Redis 库存扣减 → 写 draw_record(DB) → 返回结果

异步（MQ/后台线程）：
  更新 balance.miss_streak → 发奖通知 → DB 与 Redis 库存核对
```

---

## 9. 高可用与一致性设计

### 9.1 高可用架构

```
抽奖服务：无状态，水平扩展，多实例部署
Redis：Cluster 模式（3主3从），哨兵自动故障转移
MySQL：主从复制，写主库，读从库（balance/draw_record 写主库）
MQ：至少3节点集群，消息持久化
```

### 9.2 Redis 故障降级

```
熔断器（Circuit Breaker）监控 Redis 响应时间和错误率：
  - 错误率 > 50% 或 超时 > 100ms → 熔断器打开
  - 降级策略：库存扣减走 DB SELECT FOR UPDATE（悲观锁）
  - 半开恢复：5s 后放一条请求探测，成功则关闭熔断器
  - Redis 恢复后：后台任务将 DB 库存同步回 Redis
```

### 9.3 一致性保障

#### 设计原则

Redis Lua 的原子性只保证**并发隔离**（执行期间其他命令不插入），不保证**崩溃安全**：

```
redis.call('DECR',  stock_key)   -- 执行完
redis.call('INCR',  issued_key)  -- 执行完
-- ← Redis 在此宕机
redis.call('SET',   other_key)   -- 未执行

AOF 记录单条命令模式：前两条已持久化，第三条丢失 → 部分执行
AOF 记录整个脚本模式：恢复后脚本重跑 → DECR 执行两次 → 多扣
```

因此 **Redis 是可重建的缓存，不是一致性的保障者**。一致性由 DB 侧的 WAL 保证。

---

#### WAL：balance_log 作为 redo log

借鉴 MySQL redo log 思想：**先写日志（DB），再操作缓存（Redis）**。

balance_log 写在 DB 事务里，天然持久化，作为每次抽奖操作的意图记录：

```java
@Transactional
void deductAndLog(String actId, String userId, String drawId,
                  String awardId, int stage) {
    int affected = balanceMapper.deductOne(actId, userId);
    if (affected == 0) throw new InsufficientLotteryException();
    // redis_applied=false：Redis 操作意图已记录，尚未确认执行
    balanceLogMapper.insert(actId, drawId, "DRAW", userId, -1,
        String.format("{\"award_id\":\"%s\",\"stage\":%d,\"redis_applied\":false}",
                      awardId, stage));
}
```

操作顺序：

```
Step 1: DB 事务（balance-1 + balance_log redis_applied=false）← WAL 落盘
Step 2: Redis Lua 扣库存
Step 3: balance_log 标记 redis_applied=true              ← checkpoint
```

`redis_applied=false` = redo log 未 checkpoint；`true` = 已刷盘完成。

---

#### 崩溃恢复（redo log 重放）

扫描超时的 PENDING 记录（纯 DB 内查询，无跨系统比较竞态）：

```sql
SELECT biz_id, ext_info
FROM jewelry_lottery_activity_balance_log
WHERE biz_type = 'DRAW'
  AND JSON_EXTRACT(ext_info, '$.redis_applied') = false
  AND create_time < NOW() - INTERVAL 30 SECOND;
```

对每条 PENDING 记录，用 **SET 覆盖写**重建 Redis（幂等，不用 INCR/DECR）：

```java
// 从 draw_record COUNT 推算准确值，SET 覆盖确保幂等
long issued = drawRecordMapper.countSuccess(actId, awardId);
redis.set("lottery:stock:"  + actId + ":" + awardId, totalStock - issued);
redis.set("lottery:issued:" + actId + ":" + awardId, issued);
balanceLogMapper.markApplied(drawId);
```

---

#### 对账策略

实时跨 Redis+DB 比较不可靠（Redis 实时变化，无全局快照），采用以下分层策略：

| 时机 | 手段 | 说明 |
|------|------|------|
| 实时 | 扫 balance_log PENDING | 纯 DB 操作，精确，发现 Redis 未应用的操作 |
| 活动进行中 | 速率异常监控 | Redis stock 下降速率 vs draw_record 新增速率，检测系统性异常 |
| 活动结束后 | draw_record COUNT vs total_stock | 无在途操作，精确计算真实剩余与 Redis 最终值的差值 |

> `award.stock`（DB）仅用于展示，由活动结束后对账任务从 draw_record COUNT 派生写入，
> 不参与任何实时对账比较。

---

#### 发奖一致性

```
draw_record INSERT（同步）→ MQ 发 AWARD_EVENT
发奖服务幂等消费（drawId 唯一键）
MQ 失败 → 重试 3 次 → 死信队列 → 告警人工补发

对账：draw_record status=SUCCESS 但无发奖记录 → 重投 MQ
```

---

#### 幂等设计

| 场景 | 保障手段 |
|------|---------|
| 客户端重复提交同一 requestId | Redis idem 缓存（TTL 10min） |
| 余额扣减重复 | DB unique key(biz_id) on balance_log |
| Redis 崩溃恢复重放 | SET 覆盖写，天然幂等 |
| 重复发奖 | 发奖服务幂等 INSERT（drawId 唯一键） |
| MQ 重复消费 | 消费者幂等（drawId 去重） |

### 9.4 数据库事务边界

```java
// 扣余额 + 写流水 在同一事务
@Transactional
void deductBalance(String actId, String userId, String drawId) {
    int affected = balanceMapper.deductOne(actId, userId);
    if (affected == 0) throw new InsufficientLotteryException();
    balanceLogMapper.insert(actId, drawId, "DRAW", userId, -1);
}

// 写 draw_record 单独事务（不与库存扣减耦合）
@Transactional
void saveDrawRecord(DrawRecord record) {
    drawRecordMapper.insert(record);  // unique key(draw_id) 自动幂等
}
```

---

## 10. 前端 API 接口

### 10.1 执行抽奖

```
POST /api/v1/lottery/draw
Authorization: Bearer <token>
```

Request:
```json
{
  "activityId": "ACT_2026_JEWELRY_01",
  "requestId": "REQ_20260416143000_a3f8"
}
```

> `requestId` 由客户端生成（UUID），用于防重试重复处理。后端生成 `drawId` 作为抽奖记录主键。

Response 200：
```json
{
  "code": 0,
  "data": {
    "drawId": "DRAW_20260416143000_u001_a3f8",
    "awardId": "award_acrylic",
    "awardName": "亚克力相框挂件",
    "awardImgUrl": "https://cdn.example.com/awards/acrylic.png",
    "awardLevel": "C",
    "isWin": true,
    "isPity": false,
    "remainingLottery": 2
  }
}
```

错误码：

| code | 说明 |
|------|------|
| 0 | 成功 |
| 1001 | 活动不存在 |
| 1002 | 活动未开始 / 已结束 |
| 1003 | 抽奖次数不足 |
| 1004 | 请求过于频繁（用户并发锁） |
| 1005 | drawId 重复（返回原结果） |
| 5000 | 系统异常 |

---

### 10.2 获取活动信息

```
GET /api/v1/lottery/activity/{activityId}
```

Response:
```json
{
  "code": 0,
  "data": {
    "activityId": "ACT_2026_JEWELRY_01",
    "activityName": "珠宝春季抽奖",
    "startTime": "2026-04-01T10:00:00+08:00",
    "endTime": "2026-04-30T23:59:59+08:00",
    "status": "RUNNING",
    "currentStage": 2,
    "userRemaining": 3,
    "awards": [
      {
        "awardId": "award_acrylic",
        "awardName": "亚克力相框挂件",
        "awardImgUrl": "https://cdn.example.com/awards/acrylic.png",
        "awardLevel": "MINOR"
      }
    ]
  }
}
```

---

### 10.3 用户抽奖记录

```
GET /api/v1/lottery/records?activityId=ACT_2026_JEWELRY_01&page=1&size=10
```

Response:
```json
{
  "code": 0,
  "data": {
    "total": 25,
    "records": [
      {
        "drawId": "DRAW_20260416143000_u001_a3f8",
        "drawTime": "2026-04-16T14:30:00+08:00",
        "awardName": "亚克力相框挂件",
        "awardLevel": "C",
        "isWin": true
      }
    ]
  }
}
```

---

### 10.4 运营后台（内部）

| 方法 | 路径 | 说明 |
|------|------|------|
| PUT | `/admin/lottery/rule/{activityId}/{ruleType}` | 更新规则（QUOTA/PITY/UID等），修改后主动失效 Redis 缓存 |
| GET | `/admin/lottery/dashboard/{activityId}` | 实时奖品发放看板（各奖品 issued/total/available） |
| POST | `/admin/lottery/balance/grant` | 批量发放抽奖次数（任务奖励） |
| GET | `/admin/lottery/reconcile/{activityId}` | 触发 Redis vs DB 库存核对 |

---

## 11. 监控与告警

| 指标 | 采集方式 | 告警阈值 |
|------|---------|---------|
| 抽奖 QPS | 网关 metrics | > 1500/s 告警 |
| 库存扣减失败率（-1/-2） | Redis Lua 返回值统计 | > 5% 告警 |
| 保底触发率 | draw_record 统计 | > 15% 触发成本评估 |
| Redis 延迟 | Redis slowlog | > 50ms 告警 |
| 幂等命中率（重复 requestId） | idem 缓存命中统计 | > 1% 排查客户端重试策略 |
| DB 主从延迟 | show slave status | > 1s 告警 |
| MQ 积压 | 消费者 lag | > 1000 告警 |
| DB 与 Redis 库存差异 | 对账任务 | 差异 > 0 立即告警 |

---

*文档基于产品设计对话最终确认版本，覆盖应发尽发（累计法）、保底最终保障、不新增数据表等核心设计决策。*
