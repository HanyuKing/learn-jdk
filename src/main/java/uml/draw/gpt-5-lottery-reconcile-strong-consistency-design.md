# `LotteryReconcileService#reconcileActivity` 强一致方案分析

## 1. 先说结论

`reconcileActivity` 这种“定时比 DB 和 Redis，再决定是否重建”的思路，**不可能**把实时变化的库存做成强一致。

原因不是实现细节没写好，而是问题本身就不成立：

1. 库存是实时变化的。
2. MySQL 和 Redis 是两个独立状态机。
3. 只要它们都承载“实时库存真相”，就不存在一个后台对账方法能在不停写的情况下证明它们“此刻完全相等”。

所以要得到“完全一致”，不能继续优化 `reconcileActivity`，而必须**重做库存真相模型**。

这个仓库里，唯一现实可落地的“完全一致”方案是：

1. **MySQL 成为唯一库存真相源**
2. **抽奖决策、库存扣减、余额扣减、保底计数、抽奖记录写入，全部放进同一个 DB 事务**
3. **Redis 退出库存真相链路**
4. `reconcileActivity` 不再负责“纠正库存”，只负责：
   - 审计
   - 缓存修复
   - 卡单扫描

如果仍然要求“Redis 和 DB 都实时持有库存值，而且任何时刻严格相等”，那要么放弃 Redis 存实时库存，要么放弃 MySQL 存实时库存；两边都存且要求严格同步，在当前技术栈下不成立。

---

## 2. 为什么现在这个 `reconcileActivity` 天生做不到

当前实现：

- `LotteryReconcileService#reconcileActivity`
- `LotteryRedisService#rebuildFromDB`
- `LotteryDrawServiceImpl#executeDraw`
- `AwardFulfillService#fulfill`

当前对账逻辑做的是：

1. 从 DB 查 `countSuccess`
2. 从 Redis 查 `issued`
3. 不相等就 `rebuildFromDB`

表面上像“发现偏差再修正”，但这在实时库存里有 4 个根问题。

## 2.1 比较的不是同一时刻

当前代码中：

- DB 查询发生在一个时间点
- Redis 查询发生在另一个时间点

库存是实时变化的，所以这两个值天然不是一个快照。

只要中间有一笔抽奖成功：

- DB 可能还是旧值
- Redis 已经是新值

此时“不相等”不代表异常，只代表你拿的是两个时刻的数据。

## 2.2 当前写路径本来就把状态拆成了三份

当前实现里，库存相关状态其实分布在三处：

1. Redis `stock/issued`
2. `jewelry_lottery_activity_draw_record`
3. `jewelry_lottery_award.stock`

而且它们不是同一时刻更新：

1. `executeDraw` 先扣 Redis
2. 再写 `draw_record(status=PROCESSING)`
3. 履约成功后才扣 `jewelry_lottery_award.stock`

也就是说，现在系统设计本身就接受了：

- Redis 先变
- `draw_record` 后变
- `award.stock` 最后变

那么 `reconcileActivity` 再去追求“它们必须一样”，逻辑上就已经和主链路设计冲突了。

## 2.3 `rebuildFromDB` 会覆盖在线流量

当前 `rebuildFromDB` 用的是 Redis pipeline 覆盖写。

这意味着：

1. 对账线程刚读完 DB
2. 在线流量又产生新扣减
3. 对账线程拿旧值覆盖 Redis

结果不是修复，而是把在线刚发生的库存变化抹掉。

## 2.4 `reconcileActivity` 无法定义“什么叫完全一致”

当前系统里：

- `draw_record.PROCESSING` 代表已抽中但未履约完成
- `award.stock` 代表履约后才扣
- Redis `issued` 代表预占或实时占用

三者语义并不相同。

如果语义不同，就不存在“字面值一样才叫一致”这种判断。

所以现在最大的问题不是“对账方法写烂了”，而是**库存真相模型没有统一**。

---

## 3. 先把“完全一致”定义清楚

如果不先定义清楚，后面所有方案都会跑偏。

## 3.1 两种完全一致

### A. 字节级实时一致

意思是：

- MySQL 库存值
- Redis 库存值

在任意时刻都严格相等。

这个目标在当前技术栈下不现实，除非：

1. 只有一个系统保存实时库存
2. 另一个系统根本不保存实时库存，或者只保存不可影响业务结果的缓存

否则只要两边都写，就一定面临：

- 写入顺序
- 网络失败
- 进程崩溃
- 部分成功
- 重试幂等

这些问题不是 `reconcileActivity` 能补出来的。

### B. 业务强一致

意思是：

1. 不会超发
2. 不会漏扣
3. 一次成功抽奖一定唯一对应一条已提交的库存占用事实
4. 任意重试不会改变已提交结果
5. Redis 即使短时失效，也不会改变正确性

这个目标是可以做到的。

**本方案追求的是 B，而且这是当前仓库里唯一现实可落地的“完全一致”。**

---

## 4. 真正的强一致方案

## 4.1 核心原则

### 原则 1：库存只有一个真相源

必须二选一：

1. MySQL 是库存真相
2. Redis 是库存真相

不能两个都是真相。

对于这个仓库，更现实的是：

- **MySQL 是真相**
- Redis 只是缓存/锁/限流

### 原则 2：库存要在“抽中那一刻”扣，不要在履约成功时扣

如果库存是在履约成功时才扣，那就意味着：

- 抽奖结果已经给用户了
- 但库存还没真正落账

这会把“中奖”和“占库存”拆开，天然引入歧义。

如果要求强一致，应该改成：

1. 抽中 PRODUCT 时，在抽奖事务里就扣掉 DB 库存
2. 之后异步履约只是发券/发货
3. 履约失败时库存仍然保留，等重试或显式取消

也就是说，**库存表示“已保留名额”，不是“已履约完成数”**。

### 原则 3：影响抽奖结果的状态必须都进同一个事务

包括：

1. 用户抽奖次数
2. 奖品库存
3. 阶段配额判断
4. 保底连续未中次数
5. 抽奖记录
6. 请求幂等

只要其中任何一个状态还依赖 Redis 实时值，结果就不是完全一致。

### 原则 4：Redis 不能参与库存正确性判断

Redis 可以继续做：

1. 用户锁
2. 请求防抖
3. 规则缓存
4. 只读展示缓存

但不能再做：

1. 实时库存判定
2. 实时已发放判定
3. 保底次数真相
4. 配额占用真相

---

## 4.2 数据语义必须先统一

当前表结构里，`jewelry_lottery_award.stock` 的注释还是：

> 当前可用库存（随发放扣减）

这正是问题根源之一。

如果要做强一致，建议把语义改成：

- `jewelry_lottery_award.stock` = **当前剩余可保留库存**

也就是说：

1. 抽奖事务里扣它
2. 履约服务不再扣它
3. 履约失败但待重试时不回补
4. 只有明确取消这次中奖资格时，才回补

这样库存语义就统一了：

- `stock` 只表达“还能不能再抽中”

而不是现在这样既像“可抽库存”，又像“已履约库存”。

## 4.3 `draw_record` 状态语义也要统一

当前 `draw_record` 有：

- `PROCESSING`
- `SUCCESS`
- `FAIL`

但这个状态同时承担了：

1. 是否占库存
2. 是否履约成功

两个职责。

强一致方案里应该拆开理解：

- `PROCESSING/RESERVED`：已经占库存，尚未履约完成
- `SUCCESS`：已经占库存，且履约成功
- `RETRYING/FAIL_RETRYABLE`：已经占库存，但履约失败待重试
- `CANCELLED`：取消资格，库存已回补

重点是：

**失败待重试不能释放库存。**

否则会出现：

1. 老记录暂时失败
2. 库存被释放
3. 新用户又抽中
4. 老记录重试又成功

这就是典型超发。

---

## 5. 推荐的强一致抽奖流程

下面这个流程不是“最终一致优化版”，而是“真强一致版”。

## 5.1 预处理阶段

可以继续使用缓存读取这些**不影响事务正确性**的数据：

1. 活动是否开启
2. 规则 JSON
3. 奖品静态元数据

这些数据可以从缓存取，因为它们不是实时库存。

但一旦进入“决定是否中奖、能否占库存”的阶段，必须切回 DB 事务。

## 5.2 DB 事务内的完整流程

### 第一步：请求幂等落 DB

不能再靠 Redis 1 秒 `requestId` 防抖。

必须让请求幂等进入 DB 真相层，做法二选一：

1. 给 `draw_record` 增加 `request_id` 唯一键
2. 或增加独立 `lottery_request` 表，`request_id` 唯一

事务开始后先占这个唯一键。

这样即使：

1. 客户端超时重试
2. 应用重启
3. Redis 幂等 key 丢失

最终也不会产生第二笔真实抽奖。

### 第二步：锁用户余额行

对 `jewelry_lottery_activity_balance` 做行锁。

事务内完成：

1. 校验 `lottery_remaining > 0`
2. 扣减 1 次

这样余额和库存会在同一事务里保持一致。

### 第三步：锁该活动下所有 PRODUCT 奖品行

这是整个强一致方案最关键的一步。

如果要做到：

1. 库存实时变化下的正确可用判断
2. 当前阶段配额判断
3. 保底判定
4. 精确加权抽奖

那么就必须在**同一个事务快照**里看到该活动当前所有可抽奖品的真实状态。

最直接、最诚实的做法是：

1. 把该活动所有 PRODUCT 奖品行按固定顺序 `SELECT ... FOR UPDATE`
2. 在锁定后的 DB 快照上完成：
   - 当前可用库存判断
   - 当前阶段累计已占用判断
   - 保底命中判断
   - 加权随机决策

这一步的代价很明确：

- 同一活动下的抽奖会被串行化或半串行化

但这是“完全一致”的真实代价。

如果不愿意付这个代价，就不要再说完全一致，只能说性能优先下的近似一致或最终一致。

### 第四步：在事务内做抽奖决策

决策所依据的数据必须全部来自事务内的 DB 状态：

1. 用户 `miss_streak`
2. 各奖品当前剩余库存
3. 当前阶段配额已占用量
4. 奖品权重

不能再从 Redis 读：

1. `miss`
2. `stock`
3. `issued`

否则强一致立即破坏。

### 第五步：事务内直接扣库存

一旦抽中 PRODUCT：

1. 立即扣 `jewelry_lottery_award.stock`
2. 立即更新相关计数
3. 立即写 `draw_record`

这里的扣减是“占库存”，不是“履约完成”。

如果抽中兜底 WANT：

1. 不扣 PRODUCT 库存
2. 正常写记录

### 第六步：事务内更新保底状态

`miss_streak` 不能继续只在 Redis。

它既然影响中奖决策，就必须进入 DB 真相层。

建议：

1. 抽中 PRODUCT：`miss_streak = 0`
2. 抽中 WANT/FALLBACK：`miss_streak = miss_streak + 1`

并和库存、余额、抽奖记录一起提交。

### 第七步：提交事务

提交成功的瞬间，业务真相已经成立：

1. 这次抽奖存在
2. 余额已经扣
3. 库存已经占
4. 保底状态已更新
5. 结果不可逆

之后即使：

1. 应用崩溃
2. Redis 不可用
3. 发券服务超时

都不会影响库存正确性。

---

## 6. 履约阶段应该怎么改

强一致方案里，履约阶段只能做“发奖”，不能再碰库存真相。

## 6.1 履约只做副作用

异步履约负责：

1. 发券
2. 发 want 次数
3. 写履约日志
4. 更新 `draw_record` 状态

**不再扣 `jewelry_lottery_award.stock`。**

因为库存在抽奖事务里已经扣过了。

## 6.2 履约失败时不能自动释放库存

如果履约失败，只能进入：

- `RETRYING`
- `FAIL_RETRYABLE`

库存继续占着。

只有在明确业务判断为“取消这次中奖资格”时，才启动一笔**独立补偿事务**：

1. 回补 `award.stock`
2. 回滚用户状态
3. 修改 `draw_record` 为 `CANCELLED`

这笔补偿事务本身也必须强一致。

## 6.3 这样做的本质

把“中奖占坑”与“奖品发放”分成两个阶段：

1. 第一阶段强一致
2. 第二阶段可重试

这是高可靠抽奖系统最常见的正确做法。

---

## 7. Redis 在强一致方案里还能做什么

可以做，但只能做**不影响正确性**的事。

## 7.1 可以保留的 Redis 用途

1. 用户级锁
2. 接口防抖
3. 规则缓存
4. 奖品静态数据缓存
5. 看板只读缓存
6. 履约任务的轻量去重

## 7.2 不应该再放 Redis 的实时状态

下面这些都不应该再作为真相：

1. `stock`
2. `issued`
3. `miss_streak`
4. 阶段已占用计数

因为只要这些状态还能决定抽奖结果，就意味着结果依赖 Redis，而不是依赖同一个 DB 事务。

---

## 8. 这样一来，`reconcileActivity` 应该变成什么

如果按上面的强一致方案改，`reconcileActivity` 的职责必须降级。

它不再负责“把 Redis 修成和 DB 一样”，而只负责以下几类事情。

## 8.1 审计

例如：

1. 找出状态长时间停留在 `PROCESSING/RETRYING` 的记录
2. 找出履约日志异常记录
3. 找出重复履约尝试

## 8.2 缓存修复

如果 Redis 只是看板缓存或展示缓存，那么 `reconcileActivity` 可以做：

1. 删除缓存
2. 预热缓存
3. 刷新统计投影

即使失败，也不会影响抽奖正确性。

## 8.3 补偿任务触发器

例如：

1. 发现某条履约记录重试超过阈值
2. 进入人工处理或取消流程

但它不能再直接“重建库存”。

---

## 9. 这个方案为什么才是真正的“完全一致”

## 9.1 同一事实只在一个地方定义

库存真相只在 MySQL。

所以不会再出现：

1. Redis 说还有
2. DB 说没了

这种本体冲突。

## 9.2 所有关键判断发生在同一事务内

抽奖是否成功依赖的所有关键状态：

1. 库存
2. 余额
3. 保底
4. 配额
5. 记录

都在一个事务里完成。

所以不会再出现：

1. 库存先扣了
2. 记录没写成
3. 或者记录写了，库存还没扣

这种分裂。

## 9.3 Redis 失效不会改变结果

Redis 在新方案里只是辅助层。

所以：

1. Redis 丢了
2. Redis 超时
3. Redis 缓存脏了

都不会让库存超发。

## 9.4 `reconcileActivity` 退出正确性链路

一旦系统正确性不再依赖后台对账修正，整个系统才算真正稳定。

后台任务最多负责：

1. 发现异常
2. 清理缓存
3. 触发补偿

而不是“帮主链路擦屁股”。

---

## 10. 代价也必须讲清楚

这个方案不是没有代价，只是代价明确且可控。

## 10.1 吞吐会下降

如果同一活动下所有 PRODUCT 奖品都在事务里 `FOR UPDATE`，那么该活动的抽奖吞吐一定会下降。

这是完全一致的代价。

如果业务不能接受这个代价，就必须退回到：

- 性能优先
- 最终一致
- Redis 预扣库存

但那就不要再要求完全一致。

## 10.2 实现复杂度会上升

需要统一改造：

1. 库存语义
2. 履约语义
3. 状态机
4. 幂等
5. 补偿

但这是值得的，因为当前问题不是某个方法的 bug，而是库存模型本身裂开了。

## 10.3 看板口径需要调整

以后：

- `award.stock` 表示剩余可占用库存
- 履约成功数应来自履约日志或单独计数

不能再混用“已占用”和“已履约”两个口径。

---

## 11. 如果担心 DB 锁热点，仍然有强一致扩展路径

如果后面单活动 QPS 很高，活动级 `FOR UPDATE` 成为瓶颈，仍然有强一致扩展路径，但它们的共同点都一样：

**依然必须让 DB 成为真相，不能把 Redis 重新放回库存真相链路。**

可以考虑的强一致扩展方式有：

1. 按奖品分行锁，而不是活动全量行锁
2. 库存分桶表
3. 预生成库存 ticket/token 表，按 token 认领
4. 热门奖品拆分段库存

这些都是在 DB 真相前提下做扩展。

但在当前仓库体量下，最直接、最稳妥、最容易保证正确性的方案，仍然是：

- **活动内 PRODUCT 奖品行锁 + 事务内完成决策和扣减**

---

## 12. 给当前代码的直接结论

### 当前 `reconcileActivity` 的结论

它不是“有漏洞但还能修”，而是**职责定义错了**。

对于实时库存，它根本不该负责“修正一致性”。

### 当前 `executeDraw` 的结论

它现在是典型的 Redis 先占用、DB 后落账、履约再扣库存的最终一致实现。

这种实现可以追求高性能，但不能同时宣称完全一致。

### 当前强一致改造的落点

如果要真做完全一致，应该这样改方向：

1. Redis 退出库存真相链路
2. DB 事务内完成库存/余额/保底/记录
3. 履约只发奖，不扣库存
4. `reconcileActivity` 改成审计/缓存修复任务

---

## 13. 最终建议

如果你的目标真的是“库存实时变化下的完全一致”，那我建议直接定下面这条原则，不要再在 `reconcileActivity` 上做补丁：

> 任何会影响抽奖结果的实时状态，只能在同一个 DB 事务里读取和修改；Redis 不参与库存正确性判断。

只要这条原则不立住，后面无论怎么改对账，都会继续在“两个真相源”之间反复打补丁。

只要这条原则立住，`reconcileActivity` 就可以从“库存修正器”变成“审计与缓存维护器”，整个系统的复杂度会立刻下降一层。

---

## 14. 相关代码位置

- `jjewelry-api-service/src/main/java/cn/mathmagic/jjewelry/business/lottery/service/LotteryReconcileService.java`
- `jjewelry-api-service/src/main/java/cn/mathmagic/jjewelry/business/lottery/service/impl/LotteryDrawServiceImpl.java`
- `jjewelry-api-service/src/main/java/cn/mathmagic/jjewelry/business/lottery/service/LotteryRedisService.java`
- `jjewelry-api-service/src/main/java/cn/mathmagic/jjewelry/business/lottery/service/LotteryFallbackService.java`
- `jjewelry-api-service/src/main/java/cn/mathmagic/jjewelry/business/lottery/service/AwardFulfillService.java`
- `jjewelry-api-service/src/main/java/cn/mathmagic/jjewelry/business/lottery/repository/mapper/LotteryDrawRecordMapper.java`
- `jjewelry-api-service/src/main/java/cn/mathmagic/jjewelry/business/lottery/repository/mapper/LotteryAwardMapper.java`
- `jewelry_lottery_activity.sql`
