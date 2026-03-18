# 珠宝抽奖活动 — 技术设计文档

> 版本：3.0 | 日期：2026-03-18

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
| UID 命中记录 | **Redis uid_won 缓存 + draw_record 兜底** | Pipeline 读 Redis 快检，Redis 缺失时从 draw_record 查询 |
| 保底语义 | **最终 100%** | 即时 100% 会破坏阶段配额控制（目标2与目标3取舍） |
| miss_streak 更新 | **Redis Lua 实时更新，异步 MQ 回写 DB** | Lua 内原子更新保证实时准确，DB 回写用于崩溃恢复 |
| 库存扣减 | **Redis Lua 合并脚本（combined_draw.lua）** | 库存扣减 + miss 更新在同一 Lua 内原子执行，1 次 round trip |
| 幂等保障 | **SET NX 原子占位 + DB UNIQUE KEY 兜底** | SET NX 一步完成检查+占位，DB draw_id 唯一键作最终防线 |
| 同步 DB 写次数 | **1 TX（2 写）** | 余额扣减 + draw_record 合并为单事务，统计字段异步 MQ 更新 |
| balance_log 角色 | **仅记录 TASK 充值** | DRAW 类型不再写入，draw_record 即抽奖日志，避免冗余写入 |
| 一致性模型 | **最终一致 + 不超发不多扣** | draw_record 为唯一权威源，Redis 可随时从 DB 重建 |

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
stock       : 当前剩余库存，异步最终一致（正常路径由 MQ 消费异步扣减；Redis 故障降级时直接同步扣减）
              不参与正常抽奖主链路，仅用于 Redis 故障降级 和 运营展示
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
biz_type : TASK（任务充值）
biz_id   : 业务唯一ID，UNIQUE KEY 保幂等
use      : +N 充值
```

> **注意**：DRAW（消耗）类型不再写入 balance_log。draw_record 本身即抽奖日志，
> 余额扣减与 draw_record 在同一事务中完成，draw_record 的存在即证明余额已扣。
> 避免冗余写入，减少同步 DB 操作。

#### jewelry_lottery_activity_draw_record — 抽奖记录

```
draw_id         : 后端生成的全局唯一ID（UUID/雪花ID），UNIQUE KEY 保幂等
stage_no        : 抽奖时所处阶段（1/2/3），用于审计和统计
weight_snapshot : 抽奖时本奖品的有效权重（快照，便于追溯）
is_pity         : 是否保底命中（0/1），用于监控保底触发率
status          : SUCCESS / FAIL
fail_reason     : STOCK_EMPTY / STAGE_QUOTA_EMPTY / SYSTEM_ERROR
```

> draw_record 是整个系统的**唯一权威数据源**：
> - Redis issued/stock/miss/uid_won 均可从 draw_record 重建
> - draw_record(SUCCESS) 存在 = 这次抽奖完成 = checkpoint

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
| `lottery:idem:{requestId}` | String | SET NX 原子占位（值：PROCESSING→抽奖结果序列化） | PROCESSING=30s / 结果=10min |

---

#### Lua 脚本：combined_draw.lua（库存扣减 + miss 更新原子执行）

```lua
-- combined_draw.lua
-- KEYS[1] = lottery:stock:{actId}:{awardId}   全局库存
-- KEYS[2] = lottery:issued:{actId}:{awardId}  累计已发
-- KEYS[3] = lottery:miss:{actId}:{userId}     连续未中次数
-- ARGV[1] = cumulativeAllowed                 阶段累计允许上限（isProduct=0 时传 0）
-- ARGV[2] = isProduct                         1=命中实物奖  0=直接兜底
-- 返回:
--   1   : 扣减成功（isWin），miss_streak 已归零
--  -1   : 全局库存不足，已 INCR miss_streak
--  -2   : 阶段配额满，已 INCR miss_streak
--   0   : isProduct=0 直接兜底，已 INCR miss_streak

local isProduct = tonumber(ARGV[2])

if isProduct == 1 then
    local stock   = tonumber(redis.call('GET', KEYS[1]) or 0)
    local issued  = tonumber(redis.call('GET', KEYS[2]) or 0)
    local allowed = tonumber(ARGV[1])

    if stock <= 0 then
        redis.call('INCR', KEYS[3])
        return -1  -- 全局库存耗尽，已记 miss
    end
    if issued >= allowed then
        redis.call('INCR', KEYS[3])
        return -2  -- 阶段配额满，已记 miss
    end

    redis.call('DECR', KEYS[1])
    redis.call('INCR', KEYS[2])
    redis.call('SET',  KEYS[3], 0)
    return 1  -- 扣减成功，miss 归零
else
    redis.call('INCR', KEYS[3])
    return 0  -- 兜底，已记 miss
end
```

> 调用方只需判断返回值：`== 1` 表示中奖；`-1/-2` 表示库存失败需降级 FALLBACK；`0` 表示本就是兜底。
> miss_streak 的写入在脚本内部一并完成，不需要额外 round trip。
> `cumulativeAllowed` 由 Java 层根据 QUOTA rule 和当前阶段计算后传入，避免在 Lua 中解析 JSON。

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

### 5.3 权重抽取

`baseWeight` 来自 `jewelry_lottery_rule` 的 WEIGHT rule JSON，在加载奖品列表时与 `jewelry_lottery_award` 合并注入 `Award` 对象：

```java
// loadAwards：合并 award 表 + WEIGHT rule，生成带 baseWeight 的 Award 列表
List<Award> loadAwards(String actId, Map<String, Integer> weightMap) {
    List<Award> awards = awardMapper.listByActivity(actId);  // 从 jewelry_lottery_award 读
    for (Award award : awards) {
        // baseWeight 从 WEIGHT rule JSON 按 awardId 取值
        award.baseWeight = weightMap.getOrDefault(award.awardId, 0);
    }
    return awards;
}

// 调用方：loadRules 已包含 WEIGHT rule，解析后传入
ActivityRules rules   = loadRules(actId);                        // 含 WEIGHT rule JSON
Map<String, Integer> weightMap = rules.weightMap();              // 解析 WEIGHT rule → Map
List<Award> awards    = loadAwards(actId, weightMap);            // award 表 + weight 合并
```

> `jewelry_lottery_award` 表**不存 weight 字段**，权重统一在 WEIGHT rule 中配置，运营可随时修改规则 JSON 调整权重，无需变更奖品表数据。

```java
// WeightRange：记录某奖品对应的随机数区间 [start, end]
record WeightRange(int start, int end, Award award) {}

Award weightedDraw(String userId, List<Award> awards) {

    // ── 1. 构建权重区间池 ────────────────────────────────────────────
    List<WeightRange> pool = new ArrayList<>();
    int cursor = 0;
    for (Award award : awards) {
        if (award.isFallback()) continue;                               // 兜底最后追加
        if (award.available <= 0) continue;                            // 可用量0，跳过
        if (award.isUidRestricted() && !inWhitelist(userId, award)) continue; // UID限制
        pool.add(new WeightRange(cursor + 1, cursor + award.baseWeight, award));
        cursor += award.baseWeight;
    }
    // 兜底权重固定为 baseWeight（不吸收移除奖品的权重），总权重动态计算
    int fallbackWeight = FALLBACK_AWARD.baseWeight;  // 11893，从 WEIGHT rule 读取
    int totalWeight = cursor + fallbackWeight;
    pool.add(new WeightRange(cursor + 1, totalWeight, FALLBACK_AWARD));

    // ── 2. 生成随机数，命中区间 ──────────────────────────────────────
    int rand = ThreadLocalRandom.current().nextInt(1, totalWeight + 1);

    for (WeightRange range : pool) {
        if (rand >= range.start && rand <= range.end) {
            return range.award;
        }
    }
    return FALLBACK_AWARD;  // 防御性兜底，正常不会走到
}
```

> **区间说明（以场景B为例，所有奖品可用）：**
>
> | 随机数范围 | 奖品 | 权重 |
> |-----------|------|------|
> | 1 ~ 2 | 演唱会门票 | 2 |
> | 3 ~ 4 | 游乐场门票 | 2 |
> | 5 ~ 7 | 超大公仔 | 3 |
> | 8 ~ 47 | 毛绒挂件 | 40 |
> | 48 ~ 57 | 早安机 | 10 |
> | 58 ~ 82 | 毛绒背包 | 25 |
> | 83 ~ 107 | 保温杯 | 25 |
> | 108 ~ 1107 | 亚克力相框挂件 | 1000 |
> | 1108 ~ 13000 | 想要票×1（兜底） | 11893 |
>
> 当部分奖品 available=0 时，对应区间从池中移除，后续奖品区间前移，**总权重相应缩小**，
> 剩余实物奖概率微升（有利于应发尽发），兜底权重始终为 baseWeight(11893) 不变。

### 5.4 保底决策

```java
// 在指定等级内，按 baseWeight 比例随机选一个可用奖品
// 返回 null 表示该等级无可用奖品
Award findAvailableByLevel(List<Award> awards, String level) {

    // 1. 筛选：同等级 + 有可用量
    List<Award> candidates = awards.stream()
        .filter(a -> level.equals(a.awardLevel) && a.available > 0)
        .collect(toList());

    if (candidates.isEmpty()) return null;

    // 2. 同等级内按 baseWeight 加权随机，保持概率分布一致性
    //    示例：B级 毛绒挂件(40) + 毛绒背包(25) + 保温杯(25) → 总权重 90
    //         早安机 available=0 已被过滤，不参与本次选取
    int totalWeight = candidates.stream().mapToInt(a -> a.baseWeight).sum();
    int rand = ThreadLocalRandom.current().nextInt(1, totalWeight + 1);
    int cursor = 0;
    for (Award a : candidates) {
        cursor += a.baseWeight;
        if (rand <= cursor) return a;
    }
    return candidates.get(candidates.size() - 1);  // 防御性兜底
}
```

> **为什么用加权随机而非直接取第一个**：同等级内往往有多个奖品（如 B 级有 4 种），直接取第一个会造成某一奖品被保底优先消耗，与正常权重分配不一致。加权随机保持各奖品在保底场景下的相对概率，避免某类奖品被提前耗尽。

```java
Award decideDraw(String userId, String actId, PityConfig pity, List<Award> awards,
                 int missStreak, boolean uidAlreadyWon) {

    // 1. UID 白名单检查（演唱会门票）
    if (inUidWhitelist(userId, actId)) {
        Award concert = findAward("award_concert");
        // uidAlreadyWon 来自 Pipeline 读取的 Redis uid_won 缓存
        if (!uidAlreadyWon && available(concert) > 0) {
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

### 5.5 完整抽奖执行链路

将 5.1～5.4 各步骤串联，展示一次完整抽奖的 Java 侧执行顺序。

> **设计原则**：同步路径只保留 1 次 DB 事务（2 写），统计字段异步 MQ 更新。
> draw_record(SUCCESS) 是唯一权威数据源，Redis 是可随时从 draw_record 重建的缓存。

```java
DrawResult executeDraw(String actId, String userId, String requestId) {

    // ── 1. 幂等占位（SET NX 一步完成检查+占位）──────────────────────
    boolean claimed = redis.set("lottery:idem:" + requestId,
                                "PROCESSING", "NX", "EX", 30);
    if (!claimed) return Result.processing();  // code=1006, 请求处理中

    String drawId = generateDrawId();  // 后端生成唯一ID

    // ── 2. 获取用户级分布式锁（防同一用户不同 requestId 并发）────────
    boolean locked = redis.set("lottery:draw_lock:" + actId + ":" + userId,
                               "1", "NX", "EX", 5);
    if (!locked) {
        redis.del("lottery:idem:" + requestId);  // 释放占位
        throw new TooManyRequestsException();
    }

    try {
        // ── 3. 读规则（本地缓存 30s → Redis HGETALL → DB 兜底）────────
        ActivityRules rules = loadRules(actId);                    // TIME/WEIGHT/QUOTA/PITY/UID
        int stage            = getCurrentStage(rules);             // 5.1
        Map<String, Integer> allowed = calcAllCumulativeAllowed(   // 5.2，所有奖品
                                           rules.quota, stage);

        // ── 4. Pipeline 批量读 Redis 状态（1 次 round trip）──────────
        //    miss_streak + 各奖品 stock/issued + uid_won（一次 round trip）
        RedisPipelineResult state = redis.pipeline(
            GET("lottery:miss:"     + actId + ":" + userId),
            MGET(stockKeys(actId, awards)),
            MGET(issuedKeys(actId, awards)),
            GET("lottery:uid_won:"  + actId + ":award_concert:" + userId)
        );

        // ── 5. 计算各奖品可用量，构建可用奖品视图 ────────────────────
        List<Award> available = buildAvailableAwards(awards, state, allowed);  // 5.2

        // ── 6. 抽奖决策（纯 Java 计算，零 IO）──────────────────────
        //    优先级：UID白名单 → 保底 → 正常权重
        boolean uidAlreadyWon = "1".equals(state.uidWon);
        Award selected = decideDraw(userId, actId, rules.pity,
                                    available, state.missStreak,
                                    uidAlreadyWon);               // 5.3 + 5.4
        boolean isProduct = selected.isProduct();

        // ── 7. Redis Lua 原子执行：扣库存 + 更新 miss（1 次 round trip）
        int luaResult = redis.eval(COMBINED_DRAW_LUA,
            keys(stockKey(actId, selected.awardId),
                 issuedKey(actId, selected.awardId),
                 missKey(actId, userId)),
            args(isProduct ? allowed.get(selected.awardId) : 0,
                 isProduct ? 1 : 0));

        // ── 8. 确定最终结果 ─────────────────────────────────────────
        //   1=中奖  0=兜底  -1=全局库存空  -2=阶段配额满
        boolean isWin    = (luaResult == 1);
        Award finalAward = isWin ? selected : FALLBACK_AWARD;
        boolean isPity   = (state.missStreak >= rules.pity.threshold) && isWin;

        // ── 9. 唯一 DB 事务：扣余额 + 写 draw_record（2 写 / 1 TX）──
        drawAndDeduct(actId, userId, drawId, finalAward, stage, isPity);

        // ── 10. Pipeline 收尾：覆盖幂等缓存 + 释放锁 + uid_won 标记 ──
        DrawResult result = new DrawResult(drawId, finalAward, isWin, isPity);
        Pipeline p = redis.pipeline();
        p.set("lottery:idem:" + requestId, serialize(result), "EX", 600);
        p.del("lottery:draw_lock:" + actId + ":" + userId);
        if (isWin && "award_concert".equals(finalAward.awardId)) {
            p.set("lottery:uid_won:" + actId + ":award_concert:" + userId,
                  "1", "EX", activityTtl);
        }
        p.sync();

        // ── 11. 异步发奖 + 统计更新（MQ）──────────────────────────
        mq.send(new DrawEvent(drawId, actId, userId, finalAward,
                              isWin, isPity, state.missStreak));

        return result;

    } catch (InsufficientLotteryException e) {
        redis.del("lottery:idem:" + requestId);
        redis.del("lottery:draw_lock:" + actId + ":" + userId);
        throw e;  // 余额不足，释放占位和锁
    } catch (Exception e) {
        redis.del("lottery:idem:" + requestId);
        redis.del("lottery:draw_lock:" + actId + ":" + userId);
        throw e;
    }
}

// 唯一 DB 事务
@Transactional
void drawAndDeduct(String actId, String userId, String drawId,
                   Award award, int stage, boolean isPity) {
    // 扣余额（乐观锁，affected=0 说明余额不足）
    int affected = balanceMapper.deduct(actId, userId);
    //  UPDATE balance SET remaining=remaining-1
    //  WHERE activity_id=? AND user_id=? AND remaining > 0
    if (affected == 0) throw new InsufficientLotteryException();

    // 写抽奖记录（直接 SUCCESS，draw_id UNIQUE KEY 天然幂等）
    drawRecordMapper.insert(DrawRecord.builder()
        .drawId(drawId).activityId(actId).userId(userId)
        .awardId(award.awardId).stageNo(stage)
        .weightSnapshot(award.baseWeight)
        .isPity(isPity).status("SUCCESS").build());
}
```

**关键执行顺序说明：**

| 步骤 | 操作 | 说明 |
|------|------|------|
| 1 | SET NX idem | 幂等占位，一步完成检查+占位 |
| 2 | SET NX draw_lock | 用户级互斥，防同一用户并发 |
| 4 | Pipeline 批量读 Redis | miss/stock/issued/uid_won 合并为 1 次 round trip |
| 6 | 纯 Java 决策 | 不写任何状态，仅读取 step4 的结果 |
| 7 | Lua 原子写 Redis | 扣库存 + 更新 miss 合并为 1 次 round trip |
| 9 | **唯一 DB 事务** | 余额-1 + draw_record INSERT，2 写 / 1 TX |
| 10 | Pipeline 收尾 | 幂等覆盖 + 释放锁 + uid_won，1 次 round trip |

> 同步路径：**3 次 Redis round trip + 1 次 DB 事务**。
> 相比 v2（2 次 DB 事务 + 3 次 Redis RT），减少 1 次 DB 事务和 3 次 DB 写操作。

**MQ 消费者异步处理：**

```java
void onDrawEvent(DrawEvent event) {
    // 1. 更新统计字段（不影响主链路）
    balanceMapper.updateStats(event.actId, event.userId,
        event.isWin, event.newMissStreak());
    //  UPDATE balance SET miss_streak=?,
    //         total_lottery=total_lottery+1,
    //         total_wins=total_wins+?,
    //         last_lottery_time=NOW()

    // 2. 异步扣减 DB 库存（运营展示用）
    if (event.isWin) {
        awardMapper.deductStock(event.awardId);
        //  UPDATE award SET stock=stock-1 WHERE award_id=? AND stock>0
    }

    // 3. 实物奖发放
    if (event.isWin) {
        awardService.deliver(event.drawId, event.userId, event.awardId);
    }
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

    LS->>RC: SET NX idem:{requestId} PROCESSING EX 30
    RC-->>LS: OK（首次请求占位成功）
    Note over LS: 占位失败直接返回 code=1006 处理中
    LS->>LS: 生成 drawId（UUID）

    LS->>RC: SET draw_lock:{actId}:{userId} NX EX 5
    RC-->>LS: OK
    Note over LS,RC: 获取锁失败释放占位返回 TOO_MANY_REQUESTS

    LS->>LC: 读本地配置缓存
    LC-->>LS: miss
    LS->>RC: HGETALL rules:{actId}
    RC-->>LS: TIME / WEIGHT / QUOTA / PITY / UID
    LS->>LC: 写本地缓存 TTL 30s
    LS->>LS: 校验时间 + 计算 stage_no + cumulativeAllowed

    LS->>RC: Pipeline GET miss + MGET stock/issued + GET uid_won
    RC-->>LS: missStreak=N，各奖品可用量，uid_won状态

    LS->>LS: 抽奖决策 UID白名单 / 保底 / 正常权重

    LS->>RC: EVAL combined_draw.lua [cumulativeAllowed, isProduct]
    RC-->>LS: 1=中奖 / 0=兜底 / -1=库存空 / -2=阶段满
    Note over LS: -1/-2 降级FALLBACK，miss_streak 已在 Lua 内更新

    LS->>DB: BEGIN TX { balance-1 + INSERT draw_record(SUCCESS) } COMMIT
    DB-->>LS: OK
    Note over LS,DB: affected=0 → 次数不足，释放锁和占位返回错误

    LS->>RC: Pipeline SET idem=结果 EX 600 + DEL draw_lock + SET uid_won(如需)
    RC-->>LS: OK

    LS-->>GW: {drawId, awardId, awardName, isWin, isPity, remaining}
    GW-->>FE: HTTP 200
    FE->>U: 展示抽奖结果

    LS->>MQ: 异步 DRAW_EVENT {drawId, userId, awardId, isWin, isPity}
    Note over MQ: 消费者异步更新 balance 统计字段 + DB stock-1 + 发奖
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
    LS->>RC: Pipeline GET miss + MGET stock/issued
    RC-->>LS: miss_streak=25（未达阈值）
    LS->>LS: 正常权重抽取命中FALLBACK
    LS->>RC: EVAL combined_draw.lua [isProduct=0]
    RC-->>LS: 0（兜底，miss_streak=26）
    LS->>DB: TX { balance-1 + draw_record(FALLBACK, SUCCESS) }
    LS-->>U: 想要票x1

    U->>LS: 第27次抽奖
    LS->>RC: Pipeline GET miss + MGET stock/issued
    RC-->>LS: miss_streak=26（>=threshold）

    alt C(小奖) available > 0
        LS->>LS: 保底决策选中小奖
        LS->>RC: EVAL combined_draw.lua [isProduct=1]
        RC-->>LS: 1（成功，miss_streak=0）
        LS->>DB: TX { balance-1 + draw_record(小奖, SUCCESS, isPity=1) }
        LS-->>U: 亚克力相框挂件（保底命中）
    else C不可用查找B(中等奖)
        LS->>LS: 尝试B级奖品
    else 所有非兜底奖不可用（保底延后）
        LS->>LS: 走正常权重抽取FALLBACK
        LS->>RC: EVAL combined_draw.lua [isProduct=0]
        RC-->>LS: 0（兜底，miss_streak=27 继续累加）
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
        AS->>DB: UPDATE award SET stock=stock-1 WHERE award_id=? AND stock>0
        Note over AS,DB: 异步扣减 DB 库存，与主链路解耦；幂等靠 drawId（重复消费 ship_order INSERT 会冲突拦截，不重复扣 stock）
        AS->>NS: 推送通知 您已中奖请填写收货地址
    else awardType=WANT 虚拟票
        AS->>DB: UPDATE user_wallet SET want_ticket+=N
        AS->>NS: 推送通知 想要票x1已到账
    end

    AS->>MQ: ACK
    Note over AS,MQ: 失败重试3次进死信队列人工介入
```

> **award.stock 最终一致说明**：
> - 正常流程库存扣减链路：`Redis stock DECR`（实时）→ `MQ 消费后 DB stock-1`（异步，延迟秒级）
> - 两者之差 = 已从 Redis 扣减但 MQ 尚未消费的数量，正常情况下趋于 0
> - `award.stock` 只用于 **运营展示** 和 **Redis 故障降级**，不在抽奖主链路读取，短暂不一致不影响正确性

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
        LS->>DB: BEGIN TX
        LS->>DB: SELECT * FROM award WHERE award_id=? FOR UPDATE
        DB-->>LS: 获得行锁（同奖品并发请求在此串行化）
        LS->>DB: SELECT COUNT(*) FROM draw_record WHERE award_id=? AND status=SUCCESS
        DB-->>LS: issued=N（锁内读，无并发窗口）
        Note over LS,DB: realStock = total_stock - issued
        alt realStock>0 AND issued<cumulativeAllowed
            LS->>DB: UPDATE award SET stock=stock-1 WHERE award_id=?
            LS->>DB: COMMIT
            DB-->>LS: 扣减成功
        else
            LS->>DB: ROLLBACK
            LS-->>LS: 降级为FALLBACK
        end
        Note over LS,DB: Redis恢复后由WAL恢复任务重建Redis库存
    end
```

> **并发安全说明**：
> - 直接 COUNT → 检查 → UPDATE 三步不原子，并发请求读到相同快照后都能通过检查，`WHERE stock>0` 虽是最后防线但窗口期仍存在超发风险
> - `SELECT FOR UPDATE` 先锁住 award 行，同奖品并发请求在此处排队串行化；COUNT 在锁内执行，每次都读到上一事务提交后的准确值，彻底消除 TOCTOU 窗口
> - 降级路径本身是 Redis 故障的低频场景，行锁排队带来的延迟可接受

---

## 7. 程序流程图

### 7.1 抽奖主流程

```mermaid
flowchart TD
    A([用户发起抽奖]) --> B{活动时间校验}
    B -- 不在时间内 --> ERR1[返回: 活动未开始/已结束]
    B -- 通过 --> C{SET NX idem 幂等占位}
    C -- 占位失败 --> RET0[返回: 请求处理中 1006]
    C -- 占位成功 --> D[获取用户分布式锁]
    D -- 获取失败 --> ERR2[释放占位, 返回: 请求过于频繁]
    D -- 获取成功 --> F[加载规则 + 计算阶段\nPipeline 读 Redis 状态]

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
    O --> P[random 1~totalWeight\n命中 awardId]

    H --> Q{命中实物奖?}
    K --> Q
    M --> Q
    P --> Q

    Q -- 是 --> R[combined_draw.lua\n扣库存 + miss_streak=0]
    R -- 成功 --> S[isWin=true]
    R -- -1 全局库存空\n-2 阶段配额满 --> T[降级 FALLBACK\nmiss_streak++]
    Q -- FALLBACK --> T2[combined_draw.lua\nmiss_streak++]

    S --> W{DB 事务\nbalance-1 + draw_record}
    T --> W
    T2 --> W
    W -- affected=0 余额不足 --> ERR3[释放锁和占位\n返回: 次数不足]
    W -- 成功 --> X[Pipeline: SET idem=结果\nDEL lock + SET uid_won]
    X --> Y[MQ 异步: 统计更新 + 发奖]
    Y --> END([返回抽奖结果])
```

---

## 8. 高并发设计

### 8.1 并发控制层次

```
层次0：SET NX idem 幂等占位
  - 同一 requestId 只有一个请求能通过
  - 一步完成检查+占位，无窗口

层次1：API 网关限流
  - 全局 QPS 限制（令牌桶，e.g. 2000 QPS）
  - 单用户 QPS 限制（e.g. 每秒 1 次）
  - 活动维度限流（防刷接口）

层次2：用户级分布式锁
  - SET lottery:draw_lock:{actId}:{userId} 1 NX EX 5
  - 同一用户同一时刻只能有一次抽奖在处理
  - 防止前端重复点击生成不同 requestId 导致的并发

层次3：库存原子扣减（Redis Lua）
  - 全局库存 + 阶段配额 + miss_streak 在同一 Lua 脚本内原子执行
  - 不可能超发

层次4：数据库乐观锁
  - UPDATE balance SET remaining=remaining-1
    WHERE userId=? AND remaining>0
  - affected=0 即次数不足，无需悲观锁
```

### 8.2 热点数据缓存策略

| 数据 | 缓存位置 | 更新时机 |
|------|---------|---------|
| 活动配置（TIME/WEIGHT/PITY/UID rule） | Redis + 本地 30s | 运营修改规则后主动失效（Redis Pub/Sub 通知各实例） |
| 奖品列表 | Redis | 同上 |
| 奖品全局库存 `stock` | Redis（原子扣减主链路） | 每次发放 DECR |
| 奖品累计已发 `issued` | Redis（INCR） | 每次发放 INCR |
| 用户 `miss_streak` | Redis（Lua 内 INCR/SET） | 每次抽奖后 Lua 原子更新 |
| 用户 `uid_won` | Redis（Pipeline SET） | 中奖后 Pipeline 写入 |
| 用户余额 `remaining` | **不缓存** | DB 乐观锁直接扣，量级不高 |

### 8.3 写 DB 异步化

```
抽奖主链路（同步，1 TX / 2 写）：
  Redis Lua 扣库存 → DB 事务 { balance-1 + draw_record INSERT } → 返回结果

异步（MQ 消费）：
  balance 统计字段更新（miss_streak/total_lottery/total_wins）
  → DB stock-1（运营展示用）
  → 实物奖发放 → 发奖通知
  → Redis vs DB 库存周期对账
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

> **draw_record(SUCCESS) 是唯一权威数据源，Redis 是可随时从 draw_record 重建的缓存。**

Redis Lua 的原子性只保证**并发隔离**（执行期间其他命令不插入），不保证**崩溃安全**。
因此不在 Redis 上建立一致性，而是以 DB draw_record 为锚点，Redis 偏差通过重建修正。

一致性保障等级：
- **不超发**：Redis Lua 原子检查 stock/issued，Lua 内不可能超发
- **不多扣余额**：DB 乐观锁 `remaining > 0`，affected=0 拦截
- **最终一致**：Redis 偏差通过周期对账 + 事件驱动重建修正，窗口 ≤ 60s

---

#### 崩溃场景分析

```
Step 1:  SET NX idem (PROCESSING)
           ↓                          ← 崩溃点 A
Step 7:  Redis Lua (stock-1, issued+1, miss 更新)
           ↓                          ← 崩溃点 B（最危险窗口）
Step 9:  DB TX (balance-1 + draw_record)
           ↓                          ← 崩溃点 C
Step 10: Pipeline (idem=结果, DEL lock, SET uid_won)
```

| 崩溃点 | DB 状态 | Redis 状态 | 用户影响 | 恢复方式 |
|--------|---------|-----------|---------|---------|
| A：SET NX 之后、Lua 之前 | 未变化 | idem=PROCESSING | 无 | idem 30s 过期 → 客户端重试 → SET NX 成功 → 正常执行 |
| B：Lua 之后、DB TX 之前 | 未变化（余额未扣） | stock-1, issued+1, miss 变化 | 无（余额没扣，用户不亏） | 周期对账发现 Redis issued > draw_record COUNT → 重建修正（≤60s） |
| C：DB TX 之后、idem 覆盖之前 | 全部正确 | idem=PROCESSING | 客户端收到 1006 | 重试 → SET NX 失败 → 返回 processing → idem 被覆盖或 30s 过期后重试成功 |

> **崩溃点 B 详解**：这是最关键的窗口。Redis 库存被多扣了 1 个（stock-1），但 DB 没有对应的 draw_record。
> 后果：≤60s 内某奖品 available 少 1 个，可能导致某用户本该中奖变兜底。
> 不会超发（draw_record 是发奖依据，不存在就不发奖），不会多扣余额（DB TX 未执行）。
> 周期对账自动修正，无需人工干预。

---

#### Redis 重建（覆盖一切 Redis 偏差）

**触发时机**：
1. Redis 重连/故障转移（Sentinel `+switch-master` / Cluster failover）
2. 周期对账发现偏差（每分钟）
3. 运营手动触发

**重建流程**：

```java
void rebuildRedisFromDB(String actId) {
    // 1. issued / stock：以 draw_record 为权威源，SET 覆盖写（幂等）
    for (Award award : activeAwards(actId)) {
        long issued = drawRecordMapper.countSuccess(actId, award.awardId);
        redis.set("lottery:issued:" + actId + ":" + award.awardId, issued);
        redis.set("lottery:stock:"  + actId + ":" + award.awardId,
                  award.totalStock - issued);
    }

    // 2. miss_streak：懒加载重建
    //    GET miss key 返回 null 时，从 DB balance 读取并 SET
    //    正在抽奖的用户自动修复，无需预先全量加载
    //    （全量重建用户量大时耗时长，懒加载更实用）

    // 3. uid_won：从 draw_record 重建（只重建有中奖记录的用户）
    List<DrawRecord> concertWins = drawRecordMapper.listSuccessByAward(actId, "award_concert");
    for (DrawRecord r : concertWins) {
        redis.set("lottery:uid_won:" + actId + ":award_concert:" + r.userId, "1");
    }
}
```

> `miss_streak` 懒加载：生产中用户量大，全量重建耗时长。改为 GET miss key 返回 null 时从 DB 读取并 SET。
> 由于 miss_streak 异步 MQ 回写 DB，崩溃时 DB 值可能比 Redis 少 1。
> 这意味着用户保底可能延迟 1 次触发，下次抽奖自然修正，可接受。

---

#### 对账策略

| 时机 | 手段 | 覆盖的故障场景 |
|------|------|--------------|
| Redis 重连/故障转移 | 强制从 draw_record 重建 stock/issued/uid_won | Redis 崩溃丢数据 |
| 活动进行中 | issued vs draw_record COUNT 周期采样（每分钟） | 崩溃点 B 导致的静默偏差 |
| 活动结束后 | draw_record COUNT vs total_stock | 无在途操作，精确核算最终发放量 |

**周期采样对账（兜底）**：

```java
@Scheduled(fixedRate = 60_000)
void sampleReconcile() {
    for (Award award : activeAwards()) {
        long redisIssued = Long.parseLong(redis.get("lottery:issued:...") or "0");
        long dbIssued    = drawRecordMapper.countSuccess(actId, award.awardId);
        if (Math.abs(redisIssued - dbIssued) > TOLERANCE) {  // TOLERANCE 建议为 0
            alert("issued 偏差 award=" + award.awardId
                  + " redis=" + redisIssued + " db=" + dbIssued);
            rebuildRedisFromDB(actId);
        }
    }
}
```

---

#### 发奖一致性

```
draw_record INSERT（同步）→ MQ 发 DRAW_EVENT
发奖服务幂等消费（drawId 唯一键）
MQ 失败 → 重试 3 次 → 死信队列 → 告警人工补发

对账：draw_record status=SUCCESS 但无发奖记录 → 重投 MQ
```

---

#### 幂等设计

| 场景 | 保障手段 |
|------|---------|
| 客户端重复提交同一 requestId | SET NX idem 原子占位（PROCESSING → 结果覆盖） |
| 同一用户并发提交不同 requestId | 用户级分布式锁（draw_lock）串行化 |
| Redis 崩溃恢复重建 | SET 覆盖写，天然幂等 |
| DB 层面重复写入 | draw_record.draw_id UNIQUE KEY |
| 重复发奖 | 发奖服务幂等 INSERT（drawId 唯一键） |
| MQ 重复消费 | 消费者幂等（drawId 去重） |

### 9.4 数据库事务边界

整个抽奖主链路**只有 1 个 DB 事务**，包含 2 次写操作：

```java
@Transactional
void drawAndDeduct(String actId, String userId, String drawId,
                   Award award, int stage, boolean isPity) {
    // 1. 扣余额（乐观锁，affected=0 说明余额不足）
    int affected = balanceMapper.deduct(actId, userId);
    //  UPDATE balance SET remaining=remaining-1
    //  WHERE activity_id=? AND user_id=? AND remaining > 0
    if (affected == 0) throw new InsufficientLotteryException();

    // 2. 写抽奖记录（直接 SUCCESS，draw_id UNIQUE KEY 天然幂等）
    drawRecordMapper.insert(DrawRecord.builder()
        .drawId(drawId).activityId(actId).userId(userId)
        .awardId(award.awardId).stageNo(stage)
        .weightSnapshot(award.baseWeight)
        .isPity(isPity).status("SUCCESS").build());
}
```

> **为什么余额扣减放在 Redis Lua 之后而非之前**：
> - Lua 之后扣余额：若 Lua 成功但 DB 事务失败（崩溃点 B），用户余额未扣、draw_record 未写，仅 Redis 多扣了一个库存 → 周期对账修正，用户无损失
> - Lua 之前扣余额（v2 方案）：若余额已扣但 Lua 和 draw_record 都没完成 → 用户余额被扣但没有抽奖结果 → 需要孤儿检测+退款，逻辑复杂
> - 结论：Lua 后扣余额的崩溃恢复更简单——只需修正 Redis，不涉及余额退还

---

### 9.5 Redis 字段回写 DB 策略

#### 总览

| Redis Key | 对应 DB 字段 | 写入时机 | 一致性级别 |
|-----------|-------------|---------|-----------|
| `lottery:stock:{actId}:{awardId}` | `award.stock` | 异步：MQ DRAW_EVENT 消费时 | 最终一致（秒级延迟） |
| `lottery:issued:{actId}:{awardId}` | 无直接字段 | 不回写 DB；恢复时从 draw_record COUNT 重建 | 强一致（恢复时以 draw_record 为权威源） |
| `lottery:miss:{actId}:{userId}` | `activity_balance.miss_streak` | 异步：MQ DRAW_EVENT 消费时 | 最终一致（秒级延迟，崩溃时 ±1 偏差可接受） |
| `lottery:uid_won:{actId}:{awardId}:{userId}` | 无直接字段 | 同步：Pipeline 收尾时 SET | 恢复时从 draw_record 查询重建 |

---

#### miss_streak：异步回写，Redis 为主

```
正常流程：Redis Lua 内原子更新（INCR 或 SET 0）
         ↓ MQ DRAW_EVENT 消费时
         ↓ UPDATE balance SET miss_streak=? WHERE activity_id=? AND user_id=?
         延迟：秒级
崩溃恢复：GET miss key = null → 从 DB balance.miss_streak 读取并 SET（懒加载）
```

选择异步回写而非同步的原因：
- miss_streak 每次抽奖都要更新，同步写 DB 会增加主链路延迟
- Redis Lua 内原子更新保证高并发下的准确性
- 崩溃时 DB 值可能比 Redis 少 1（最近一次异步还未完成），影响 = 保底延迟 1 次触发，下次自然修正

---

#### issued：不回写，以 draw_record 为权威源

`lottery:issued` 是 `draw_record` 成功记录数的 Redis 物化计数，不存在对应 DB 字段。

```
正常流程：每次 Lua INCR issued → Redis 实时计数
崩溃恢复：SET issued = COUNT(draw_record WHERE status=SUCCESS AND award_id=?)
          以 draw_record 为单一权威源，SET 覆盖写天然幂等
```

---

#### stock：异步回写，最终一致

```
正常流程：Redis DECR stock（实时）
          ↓ draw_record 落盘后 MQ 发 DRAW_EVENT
          ↓ 发奖服务消费：UPDATE award SET stock=stock-1 WHERE award_id=? AND stock>0
          延迟：秒级
幂等保障：ship_order INSERT(drawId UNIQUE KEY) 冲突拦截重复消费，不重复扣 stock
崩溃恢复：SET stock = total_stock - COUNT(draw_record WHERE status=SUCCESS)
```

`award.stock` 短暂滞后不影响正确性：
- 主链路库存判断走 Redis `issued` + `cumulativeAllowed`（不读 `award.stock`）
- Redis 故障降级时改从 draw_record 实时 COUNT 计算（见 6.5），同样不依赖 `award.stock`

---

#### uid_won：同步写 Redis，从 draw_record 重建

```
正常流程：用户首次中演唱会门票 → Pipeline 收尾时 SET lottery:uid_won:{actId}:{awardId}:{userId} 1 EX ttl
崩溃恢复：从 draw_record 查询成功记录 → SET uid_won=1
```

`draw_record` 是唯一权威源，uid_won 只是快速查询缓存。

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
| 1006 | 请求处理中（idem 占位冲突，客户端稍后重试） |
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
| 保底触发率 | draw_record is_pity 统计 | > 15% 触发成本评估 |
| Redis 延迟 | Redis slowlog | > 50ms 告警 |
| 幂等占位冲突率（1006） | idem SET NX 失败统计 | > 1% 排查客户端重试策略 |
| DB 主从延迟 | show slave status | > 1s 告警 |
| MQ 积压 | 消费者 lag | > 1000 告警 |
| DB 与 Redis 库存差异 | 对账任务 | 差异 > 0 立即告警 |
| 各阶段发放进度 vs 配额 | issued / cumulativeAllowed | 进度 < 50% 且阶段过半告警 |
| 保底延后次数 | miss_streak >= threshold 但走了 FALLBACK | > 0 关注库存是否不足 |

---

*文档 v3 基于 v2 优化：废弃 balance_log WAL 方案，改为 draw_record 唯一权威源 + Redis 可重建模型；
同步 DB 写从 5 次/2-3TX 优化为 2 次/1TX；幂等 GET+SET 改为单次 SET NX 原子占位；
权重池改为动态总权重；统一 Lua 脚本和锁超时参数。*
