# `LotteryDrawServiceImpl#executeDraw` 一致性问题复查

## 1. 先说结论

按当前代码，`executeDraw` 仍然是一个**最终一致**方案，不是强一致方案。

它的核心特征没有变：

1. Redis 先承担实时扣减和实时判定。
2. DB 随后写 `draw_record` 和余额流水。
3. 奖品履约异步执行，`jewelry_lottery_award.stock` 在履约成功时才真正扣减。

但和我上一个版本的分析相比，当前代码已经有两个重要变化：

1. **Redis 故障不再降级 DB**
   - 现在 Redis 异常会直接抛错，抽奖失败。
   - 所以“Redis 降级到 DB 导致双扣 `stock`”这个问题，在当前代码里**已经不存在**。

2. **履约阶段已经补上 DB 扣库存结果校验**
   - `AwardFulfillService` 现在会检查 `deductStock` 的影响行数。
   - 扣库存失败不会继续发券，也不会把记录更新成 `SUCCESS`。
   - 所以“库存没扣成但记录成功”的问题，在当前代码里也**已经修掉**。

当前还真实存在的一致性风险，主要剩下这几类：

1. Redis 扣成功后，DB 事务失败，没有反向补偿。
2. `reconcileActivity` 仍然是错误的聚合值对账思路。
3. `FAIL` 记录会与重试模型、对账模型互相打架。
4. `requestId` 仍然只是 1 秒防抖，不是真幂等。
5. Redis 不可用时直接失败，正确性更简单了，但可用性下降。

---

## 2. 当前真实执行链路

当前抽奖链路以以下代码为准：

- `LotteryDrawServiceImpl#executeDraw`
- `LotteryDrawTransactionService#drawAndDeduct`
- `AwardFulfillService#fulfill`
- `LotteryReconcileService#reconcileActivity`

### 2.1 抽奖主链路

按当前实现，时序是：

1. 校验活动状态。
2. 基于 `requestId` 做 Redis 幂等占位。
3. 获取用户级 Redis 分布式锁。
4. 从 DB 查询用户余额，做 fast-fail。
5. 读取规则，计算当前阶段和各奖品累计允许发放数。
6. 从 Redis 读取：
   - `stock`
   - `issued`
   - `missStreak`
7. 引擎根据 Redis 状态做抽奖决策。
8. 通过 Redis Lua 扣减：
   - 产品奖：`stock - 1`、`issued + 1`、`miss = 0`
   - 兜底奖：`miss + 1`
9. 如果 Lua 返回 `-1/-2`，则把结果降级成兜底奖。
10. 进入 DB 事务：
    - 扣 `lottery_remaining`
    - 插入 `draw_record(status=PROCESSING)`
11. 事务返回后发布履约事件。
12. 异步履约：
    - PRODUCT：扣 DB `award.stock`，发券，改 `SUCCESS`
    - WANT：发 want，改 `SUCCESS`

### 2.2 Redis 故障时的行为

这一点现在已经很明确：

当前没有 Redis 降级 DB 的逻辑。

`executeStockDeduction` 对 Redis 相关异常会直接抛 `ServiceException`，所以：

1. Redis 不可用
2. 不会写 `draw_record`
3. 不会扣 DB 余额
4. 不会走 DB 库存校验

这意味着：

- 一致性更简单
- 可用性更差

这是当前代码已经做出的取舍。

---

## 3. 当前系统里到底有哪些“状态”

当前抽奖系统不是“Redis 一份库存，DB 一份库存”这么简单，而是至少有三套状态：

| 位置 | 字段 | 当前语义 |
|---|---|---|
| Redis | `stock/issued/miss` | 实时抽奖热状态 |
| `draw_record` | `PROCESSING/SUCCESS/FAIL` | 抽奖业务记录状态 |
| `jewelry_lottery_award.stock` | `stock` | 履约成功后才真正扣减的 DB 库存 |

所以不要误以为“Redis 和 DB 应该始终相等”。

当前代码里：

1. Redis 的 `issued` 更接近“已实时占用”
2. `draw_record(PROCESSING/SUCCESS)` 更接近“已占坑或已完成”
3. `award.stock` 更接近“已履约落账”

这三者天然不是同一个时点更新。

---

## 4. 当前哪些不一致是正常的

### 4.1 Redis 先于 DB

Redis Lua 扣减发生在 DB 事务之前。

所以一定存在窗口：

1. Redis 已 `stock - 1 / issued + 1`
2. `draw_record` 还没提交

这个窗口不是 bug，是设计结果。

### 4.2 `draw_record` 先于 `award.stock`

`draw_record` 在事务里会先写成 `PROCESSING`。

而 `award.stock` 要等异步履约成功才扣。

所以也一定存在窗口：

1. `draw_record = PROCESSING`
2. `award.stock` 还没变

这同样不是 bug，是设计结果。

### 4.3 `countSuccess` 其实不是“成功履约数”

`LotteryDrawRecordMapper#countSuccess` 实际统计的是：

```sql
status IN ('SUCCESS', 'PROCESSING')
```

所以它的真实语义更接近：

> 已占用名额数

而不是：

> 已履约成功数

如果后面有人拿它去和 `award.stock` 做一一对应，结论一定会错。

---

## 5. 当前已经修掉的问题

这一节很重要，因为上一个版本文档里有两条已经不再成立。

### 5.1 “Redis 降级 DB 导致双扣库存”已经不成立

当前代码已经没有 Redis 故障降级 DB 的路径。

所以不存在下面这个旧问题：

1. 降级路径先扣一次 DB `stock`
2. 履约阶段再扣一次 DB `stock`

在当前代码里，DB `stock` 只在 `AwardFulfillService#fulfill` 的 PRODUCT 分支里扣。

### 5.2 “扣库存失败但仍然 SUCCESS”已经修掉

当前 `AwardFulfillService` 已经这样做：

1. 调用 `awardRepository.deductStock`
2. 检查影响行数是否为 `1`
3. 不是 `1` 就报警并抛异常
4. 不会继续发券
5. 不会更新 `draw_record = SUCCESS`

这块当前是符合预期的。

所以旧文档里那条“履约不检查扣库存结果”的高优先级问题，也已经不再成立。

---

## 6. 当前仍然存在的一致性问题

## 6.1 Redis 扣成功后，DB 事务失败，没有补偿

这是当前最核心的问题之一。

### 现状

当前顺序是：

1. Redis Lua 扣减成功
2. `transactionService.drawAndDeduct(...)`

如果第 2 步失败，例如：

1. `useBalance` 失败
2. `draw_record` 插入失败
3. 事务整体回滚

那么 Redis 已经发生的变更不会被回滚：

1. `stock`
2. `issued`
3. `miss`

### 结果

这会导致：

1. Redis 认为名额已经被占用
2. DB 里没有对应 `draw_record`
3. 用户也没有真正完成抽奖

最终表现为：

1. Redis 库存被吃掉
2. DB 查不到记录
3. 后续只能靠对账或重建去“扫地”

### 本质

当前系统没有把：

> Redis 预占成功 -> DB 事务提交成功

之间的这段窗口闭环掉。

也就是说，缺一个**反向补偿**机制。

### 怎么改

如果保持当前架构不大改，短期最稳妥的是：

1. Lua 成功后，记录一个可补偿凭证
   - 至少包含 `drawId`、`awardId`、`userId`、`isProduct`
2. DB 事务成功后，把凭证标记为已提交
3. DB 事务失败后，执行反向 Lua：
   - `stock + 1`
   - `issued - 1`
   - `miss` 回滚

否则 Redis 与 DB 的偏差永远只能靠事后修。

---

## 6.2 `reconcileActivity` 仍然是错误的对账模型

当前 `LotteryReconcileService#reconcileActivity` 还是：

1. 读 `dbIssued = countSuccess(...)`
2. 读 `redisIssued = getIssued(...)`
3. 不相等就 `rebuildFromDB`

这个思路在实时库存场景下仍然不成立。

### 问题一：比较的是两个非原子快照

DB 查询与 Redis 查询不在同一时刻。

只要中间刚好有新抽奖发生，就会出现：

1. DB 还是旧值
2. Redis 已经是新值

这种“不相等”很多时候只是时间差，不是真异常。

### 问题二：`PROCESSING` 本来就是 in-flight

`countSuccess` 包含 `PROCESSING`，这意味着：

1. 正在处理中但未履约完成的记录也被算作已占用
2. Redis 与 DB 之间会天然存在短时间差

当前对账逻辑无法区分：

1. 正常 in-flight
2. 真正丢数据

### 问题三：`rebuildFromDB` 会覆盖在线 Redis

当前 `rebuildFromDB` 用的是 pipeline 批量覆盖 Redis。

所以存在典型竞态：

1. 重建线程刚从 DB 算出 `issued = N`
2. 在线流量又成功扣了一笔，Redis 变成 `N + 1`
3. 重建线程把旧值 `N` 覆盖回去

结果不是修复，而是把在线成功请求的热状态抹掉。

### 结论

`reconcileActivity` 当前不应该继续承担“库存修复器”这个角色。

如果要保留这个类，更合理的职责应该是：

1. 审计
2. 卡单发现
3. 缓存清理
4. 触发单笔补偿

而不是继续按聚合库存值重建 Redis。

---

## 6.3 `FAIL` 与重试机制，会和对账模型打架

这个问题是当前代码里很容易被忽略，但风险很高的一点。

### 现状

当前有两个相关事实：

1. `countSuccess` 只统计 `SUCCESS/PROCESSING`
2. `listRetryable` 会把 `FAIL` 和超时 `PROCESSING` 拉出来重试

也就是说：

- `FAIL` 记录会继续被重试
- 但 `FAIL` 又不会被算进“已占用名额”

### 风险场景

场景如下：

1. Redis 已经为某次 PRODUCT 抽奖扣掉库存
2. `draw_record` 已写成 `PROCESSING`
3. 履约失败，记录变成 `FAIL`
4. 此时 `countSuccess` 不再统计这条记录
5. `reconcileActivity` 如果用 `countSuccess` 重建 Redis，就可能把这个名额释放回去
6. 后续 `retryAll` 又把这条 `FAIL` 记录履约成功

这会带来两个后果：

1. 旧记录最终成功履约
2. 名额在重建阶段又被释放给了新请求

这就是**超发风险**。

### 本质

当前状态语义里，`FAIL` 同时承担了：

1. 临时失败待重试
2. 不再占用库存

这两个含义。

这在一致性上是不成立的。

### 怎么改

更合理的做法是把状态拆开：

1. `PROCESSING`：已占用库存，待履约
2. `RETRYING`：履约失败待重试，仍占用库存
3. `SUCCESS`：履约完成
4. `CANCELLED`：明确取消，库存可释放

也就是说：

**失败待重试不能从库存占用集合里移除。**

---

## 6.4 `requestId` 仍然不是真幂等

当前 `requestId` 只是：

1. `SET NX`
2. `EX 1s`

而且成功后不会把最终结果与 `requestId` 绑定保存下来。

### 结果

它更像“1 秒防抖”，不是真正幂等。

只要发生下面任一场景：

1. 客户端超时
2. 服务端其实已成功提交
3. 用户 1 秒后重试

就可能进入第二次真实抽奖。

### 这个问题为什么会放大一致性风险

因为当前主链路里还有：

1. Redis 先扣
2. DB 后写
3. 事件后发

如果客户端因为超时或异常重试，而 `requestId` 幂等已经过期，就会把原本一次问题变成二次问题。

### 怎么改

至少要做到：

1. `requestId` 落 DB 或独立请求表
2. `requestId` 有唯一约束
3. 重复请求能返回旧结果，而不是再走一次抽奖

否则这个链路只能叫“短暂去重”，不能叫“请求幂等”。

---

## 6.5 Redis 不可用时直接失败，是当前代码的明确取舍

这条不是一致性 bug，但会直接影响你怎么看这套方案。

当前代码已经明确选择：

1. 不做 Redis 降级 DB
2. Redis 异常直接失败

这意味着：

### 好处

1. 不会再引入 DB 降级分支的额外库存语义
2. 一致性模型更干净
3. 少了一条“热点时 DB 被打穿”的兜底链路

### 代价

1. Redis 成为强依赖
2. Redis 故障时，抽奖不可用

所以当前代码本质上是在用**可用性**换**一致性复杂度下降**。

这个取舍本身没问题，但必须明确接受。

---

## 7. 当前代码里哪些部分其实是比较稳的

为了避免分析只盯着问题，这里也明确一下当前已经比较稳的部分。

### 7.1 `drawAndDeduct` 内部事务边界是合理的

`LotteryDrawTransactionService#drawAndDeduct` 里：

1. 余额扣减
2. `draw_record` 插入

在同一个事务中完成。

所以如果 `draw_record` 插入失败：

1. 余额不会单独扣掉
2. 余额流水也会一起回滚

这部分 DB 内部一致性是好的。

### 7.2 履约阶段的库存检查现在是闭环的

当前 PRODUCT 履约分支已经做到：

1. 扣库存失败 -> 报警
2. 扣库存失败 -> 不发券
3. 扣库存失败 -> 不改 `SUCCESS`
4. 最终走 `FAIL + retryCount+1`

这部分比之前要稳很多。

---

## 8. 我对当前实现的判断

当前版本最真实的描述应该是：

> 它是一个“Redis 热路径实时占用 + DB 抽奖记录落账 + 履约时扣 DB 商品库存”的最终一致方案。

它现在已经比旧版本干净了一些，因为：

1. 去掉了 Redis 故障降级 DB 这条混乱路径
2. 修掉了履约阶段“没扣成库存也成功”的 bug

但核心结构问题还在：

1. Redis 预占成功之后，没有 DB 失败补偿
2. `FAIL` 与重试、对账的状态语义没统一
3. 聚合值对账思路仍然错误
4. `requestId` 幂等仍然太弱

所以结论不是“这个链路已经没问题了”，而是：

> 当前代码已经从“有明显实现 bug”的状态，进入了“实现上相对收敛，但架构级最终一致问题仍然没闭环”的状态。

---

## 9. 当前最值得优先做的事情

如果只按优先级排，我建议这样做：

### 第一优先级：补 Redis -> DB 的反向补偿

这是当前最核心的闭环缺口。

没有它，Redis 热状态与 DB 账本之间永远存在不可恢复窗口。

#### 具体实现建议

目标是把下面这段窗口闭环：

1. Redis Lua 已成功扣减
2. `drawAndDeduct` 事务失败
3. Redis 热状态无人回滚

推荐按“单笔预占单”来做，不要继续按聚合值修。

#### 需要新增的 Redis 数据

建议为每一笔成功 Lua 预占创建一份 reservation 记录：

1. `jjewelry:lottery:reservation:{drawId}`
   - `drawId`
   - `requestId`
   - `actId`
   - `userId`
   - `awardId`
   - `awardType`
   - `stage`
   - `luaResult`
   - `missBefore`
   - `missAfter`
   - `stockChanged`
   - `issuedChanged`
   - `status`，取值建议：
     - `RESERVED`
     - `COMMITTED`
     - `CANCELLED`

2. `jjewelry:lottery:reservation:pending`
   - `zset`
   - score = reservation 创建时间
   - member = `drawId`

#### 主链路调整建议

当前代码里 `drawId` 是在 Lua 之后生成的。为了让补偿有稳定锚点，建议改成：

1. 在执行 Lua 之前就生成 `drawId`
2. Lua 成功后顺手写入 reservation
3. 只有 reservation 写成功，这次抽奖才算进入 DB 提交阶段

主流程建议变成：

1. 生成 `drawId`
2. 执行 Lua 扣减
3. Lua 成功后写 reservation，状态 = `RESERVED`
4. 调 `transactionService.drawAndDeduct(...)`
5. DB 事务成功后：
   - reservation 标记 `COMMITTED`
   - 从 `pending zset` 删除
6. DB 事务失败后：
   - 立即调用反向 Lua
   - reservation 标记 `CANCELLED`
   - 从 `pending zset` 删除

#### 反向 Lua 要做什么

反向补偿不能写成多条 Redis 命令，必须也是 Lua。

它至少要支持：

1. 如果 `stockChanged=1`
   - `stock + 1`
2. 如果 `issuedChanged=1`
   - `issued - 1`
3. `miss` 恢复到 `missBefore`
4. reservation 从 `RESERVED` -> `CANCELLED`

#### 为什么要记录 `missBefore`

因为当前 Lua 会直接改 `miss`：

1. PRODUCT 成功 -> `miss = 0`
2. WANT 或库存不足 -> `miss + 1`

如果 DB 事务失败不回滚 `miss`，会直接影响用户下一次抽奖结果。

#### 代码改造落点

建议改这些点：

1. `LotteryDrawServiceImpl#executeDraw`
   - 提前生成 `drawId`
   - 在 `drawAndDeduct` 抛异常时触发 `compensateReservation(drawId)`

2. `LotteryRedisService`
   - 新增 `reserveAndRecord(...)`
   - 新增 `commitReservation(drawId)`
   - 新增 `cancelReservation(drawId)`
   - 新增补偿 Lua

3. 补一个 `ReservationCompensateService`
   - 只做单笔 reservation 回滚
   - 方便后面定时任务复用

#### 实施完成后的效果

这一步做完后，至少能保证：

1. Redis 预占成功但 DB 失败，不会永久丢库存
2. `missStreak` 不会因为失败事务被错误推进
3. 每一笔异常都能精确定位到 `drawId`

### 第二优先级：停掉 `reconcileActivity` 的聚合重建思路

在活动进行中，不要再做：

1. 比较 `dbIssued` 与 `redisIssued`
2. 不等就重建 Redis

这套逻辑继续在线上跑，只会放大问题。

#### 具体实现建议

这里不是简单“删掉对账”，而是要把对账从“聚合值重建”改成“事件级补偿扫描”。

#### 先停掉什么

建议先停掉两件事：

1. `reconcileActivity` 中的：
   - `dbIssued != redisIssued`
   - `needRebuild = true`
2. `rebuildRedisFromDB` 在线重建 Redis

这一套在线重建逻辑，在活动进行中风险太高。

#### 改成什么

把 `LotteryReconcileService` 改成“pending reservation 扫描器”。

核心逻辑：

1. 扫描 `reservation:pending zset`
2. 对每个超时 `drawId` 做检查

分支建议如下：

1. DB 已有对应 `draw_record`
   - 说明只是 Redis reservation 没清掉
   - 把 reservation 标记 `COMMITTED`
   - 从 `pending` 移除

2. DB 没有对应 `draw_record`，但距离 reservation 创建时间很短
   - 可能只是线程阻塞或瞬时慢
   - 暂不补偿

3. DB 没有对应 `draw_record`，且超过超时阈值
   - 执行 `cancelReservation(drawId)`
   - 记录日志并报警

#### 新的对账标准

以后不再对账：

1. `dbIssued`
2. `redisIssued`
3. `dbStock`
4. `redisStock`

而是对账：

1. `drawId` 是否存在
2. 这笔 reservation 是否已提交
3. 这笔 reservation 是否需要取消

#### 运营看板怎么改

看板可以继续展示聚合值，但只能做“观测”，不能再驱动自动修复。

也就是说：

1. 看板上的 `dbIssued` / `redisIssued` 可以保留
2. 但出现差值时，不能直接自动重建 Redis
3. 应该跳转到 reservation 异常列表去看具体 `drawId`

#### 保留人工全量重建的前提

如果未来还要保留“从 DB 全量重建 Redis”，必须加两个前提：

1. 活动停服或流量开关关闭
2. 人工确认当前没有新的 reservation 在进入

否则不要在线跑。

### 第三优先级：把 `FAIL` 拆成“待重试”和“已取消”

否则重试与对账永远会互相打架。

#### 具体实现建议

这里的核心不是多加一个枚举值，而是统一“是否仍占用名额”的语义。

当前 `FAIL` 最大的问题是：

1. 它还会被 `retryAll` 重试
2. 但它又不被 `countSuccess` 统计

这会让“是否占名额”变得不确定。

#### 建议的状态机

建议把 `DrawStatusEnum` 拆成至少四个状态：

1. `PROCESSING`
   - 抽奖记录已写入
   - 还没履约成功
   - 仍占用名额

2. `RETRYING`
   - 履约失败待重试
   - 仍占用名额

3. `SUCCESS`
   - 履约完成
   - 仍占用名额

4. `CANCELLED`
   - 明确取消
   - 不再占用名额

#### 对应查询也要改

当前 `countSuccess` 名字和语义都不合适，建议改成：

1. `countOccupied`

SQL 语义应变成：

```sql
status IN ('PROCESSING', 'RETRYING', 'SUCCESS')
```

只有 `CANCELLED` 不再计入占用。

#### 履约失败时怎么走

当前 `updateStatusAndRetry(drawId, FAIL, reason)` 建议改成：

1. 第一次失败 -> `RETRYING`
2. `retry_count + 1`
3. 保持占用

#### 什么情况下才能进入 `CANCELLED`

必须是明确取消，不要因为“发券失败一次”就取消。

进入 `CANCELLED` 的触发条件建议单独定义，例如：

1. 超过最大重试次数
2. 奖品配置被下线
3. 人工确认取消
4. 单笔补偿任务明确执行取消

#### 进入 `CANCELLED` 时必须做的动作

如果这条记录对应的 Redis reservation 仍然是 `RESERVED/COMMITTED but not fulfilled`，则：

1. 执行 reservation 取消补偿
2. 标记 draw_record = `CANCELLED`
3. 记录取消原因

#### 代码改造落点

建议改这些点：

1. `DrawStatusEnum`
2. `LotteryDrawRecordMapper#countSuccess` -> `countOccupied`
3. `LotteryDrawRecordMapper#listRetryable`
   - 扫 `RETRYING`
   - 扫超时 `PROCESSING`
4. `AwardFulfillService`
   - 失败后写 `RETRYING`
   - 不直接写泛化的 `FAIL`

#### 这一步的价值

它会把“失败但仍占库存”与“明确取消可释放库存”彻底分开。

这对后面的补偿和对账都是基础。

### 第四优先级：把 `requestId` 变成真幂等

否则客户端重试会反复放大前面三类问题。

#### 具体实现建议

当前 `requestId` 只是：

1. `SET NX`
2. `EX 1s`

这只能防重复点击，不能防：

1. 客户端超时后重试
2. 服务端实际已成功但响应丢失
3. 服务端卡顿超过 1 秒

#### 推荐幂等模型

`requestId` 至少要有两个层次的保障：

1. Redis 热幂等
2. DB 最终唯一约束

#### Redis 热幂等怎么做

建议把 `IDEM_KEY` 的 value 改成状态机，而不是只放一个 `PROCESSING`：

1. `PROCESSING`
2. `DONE:{drawId}`
3. `FAILED:{reasonCode}`，可选

TTL 也不要再是 1 秒，建议至少到活动结束，或者至少几十分钟。

#### 推荐流程

1. 请求进来
2. 查 `request:{requestId}`

分支如下：

1. 不存在
   - `SET NX request:{requestId} = PROCESSING`
   - 继续执行

2. 已存在且是 `PROCESSING`
   - 返回“请求处理中”

3. 已存在且是 `DONE:{drawId}`
   - 直接返回旧的抽奖结果

#### 要返回旧结果，就需要缓存结果

因此建议新增：

1. `jjewelry:lottery:result:{drawId}`
   - 保存 `DrawResult` 所需字段

Lua 成功或 DB 提交成功后，把结果缓存起来。

#### DB 层还要再兜一层

为了防 Redis key 过期、服务重启或缓存丢失，建议再加一个 DB 兜底：

方案二选一：

1. 在 `draw_record` 表加 `request_id` 字段，并加唯一索引
2. 新建 `lottery_request` 表：
   - `request_id`
   - `draw_id`
   - `status`
   - `create_time`

如果你不想动 `draw_record` 主表，我更建议单独建 `lottery_request` 表。

#### 当前链路下的最佳做法

结合前面三个优先级，推荐完整顺序是：

1. 进入请求，设置 `requestId=PROCESSING`
2. 生成 `drawId`
3. Redis Lua 成功，创建 reservation
4. DB 事务成功
5. `requestId -> DONE:{drawId}`
6. 缓存 `result:{drawId}`
7. 重复请求直接返回旧结果

如果中途失败：

1. 先取消 reservation
2. 再把 `requestId` 改成可重试状态，或者删除

#### 这一步的价值

它能显著减少“客户端重试把一次问题放大成多次问题”的概率。

在当前架构下，这不是优化项，是一致性基础设施。

---

## 10. 最终结论

重新按当前代码复查之后，我的结论是：

1. 旧文档里“Redis 降级 DB 双扣库存”的问题，当前代码已经不成立。
2. 旧文档里“履约不检查扣库存结果”的问题，当前代码已经修掉。
3. 但这条链路仍然不是闭环一致：
   - Redis 预占后无 DB 失败补偿
   - `FAIL` 状态语义错误
   - 聚合值对账仍然危险
   - `requestId` 仍然只是防抖

所以当前版本的真实评价应该是：

> 代码级明显 bug 少了，但架构级最终一致问题还没有真正收口。

如果后面还要继续改，我建议优先补“单笔事件补偿”而不是继续修聚合对账。
