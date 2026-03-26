# gemini-lottery-implementation-design.md

## 1. 强幂等性保障方案（DB 级去重）

目前的 `requestId` 仅在 Redis 停留 1 秒，无法防止跨秒的重复请求或超时重试。为了实现真正的幂等，我们需要将请求标识与业务结果绑定并持久化。

### 落地实现建议
利用 `lottery_draw_record` 表增加 `request_id` 唯一索引，或者建立独立的幂等表。

**SQL 变更：**
```sql
ALTER TABLE `lottery_draw_record` ADD UNIQUE INDEX `uk_request_id` (`request_id`);
```

**Service 代码层面的调整：**
```java
@Transactional(rollbackFor = Exception.class)
public void drawAndDeduct(String actId, String userId, String requestId, AwardBO chosen, int stage) {
    // 1. 尝试插入记录，若 requestId 已存在，数据库抛出 DuplicateKeyException
    try {
        LotteryDrawRecord record = buildRecord(actId, userId, requestId, chosen, stage);
        drawRecordRepository.save(record);
    } catch (DuplicateKeyException e) {
        log.warn("检测到重复抽奖请求, requestId={}", requestId);
        throw new ServiceException(ErrorCodeEnum.REPEATED_REQUEST, "请勿重复提交");
    }

    // 2. 扣减余额
    boolean success = balanceService.deductBalance(actId, userId, 1);
    if (!success) {
        throw new ServiceException(ErrorCodeEnum.BALANCE_INSUFFICIENT, "余额不足");
    }
}
```

---

## 2. Redis 与 DB 的补偿一致性（反向回撤）

解决“Redis 预扣成功，但后续 DB 事务回滚”导致的库存“凭空消失”问题。

### 2.1 增加 Redis 补偿 Lua 脚本
在 `LotteryRedisService` 中封装一个 `rollbackStock` 方法。

**Lua 脚本逻辑：**
```lua
-- KEYS[1]: 抽奖状态 Hash Key (lottery:state:actId)
-- ARGV[1]: awardId, ARGV[2]: userId, ARGV[3]: isProduct (true/false)

local awardId = ARGV[1]
local userId = ARGV[2]
local isProduct = ARGV[3]

if isProduct == "true" then
    -- 1. 增加库存
    redis.call("HINCRBY", KEYS[1], "stock:" .. awardId, 1)
    -- 2. 减少已发放数
    redis.call("HINCRBY", KEYS[1], "issued:" .. awardId, -1)
else
    -- 3. 如果是非产品奖（如谢谢参与），回撤连续未中奖计数（可选）
    redis.call("HINCRBY", KEYS[1], "miss:" .. userId, -1)
end
return 1
```

### 2.2 抽奖主逻辑中的 Try-Catch 补偿
```java
public DrawResult executeDraw(String actId, String userId, String requestId) {
    // ... 规则加载与引擎决策 ...
    AwardBO chosen = drawEngine.decideDraw(...);

    // 1. Redis 预扣库存
    boolean redisDeducted = false;
    try {
        long res = redisService.executeStockDeduction(actId, userId, chosen, ...);
        if (res > 0) redisDeducted = true;
    } catch (Exception e) {
        log.error("Redis扣减异常", e);
        throw new ServiceException(ErrorCodeEnum.SYSTEM_ERROR, "系统繁忙，请重试");
    }

    // 2. DB 事务操作
    try {
        transactionService.drawAndDeduct(actId, userId, requestId, chosen, stage);
    } catch (Exception e) {
        // 【核心点】若 DB 事务失败且 Redis 之前扣成功了，必须执行反向补偿
        if (redisDeducted) {
            log.error("DB事务失败，触发Redis库存回撤补偿: userId={}, awardId={}", userId, chosen.getAwardId());
            // 异步或同步执行 Redis 补偿脚本
            redisService.rollbackStock(actId, userId, chosen.getAwardId(), chosen.isProduct());
        }
        throw e;
    }

    // 3. 事务成功后发布履约事件
    eventPublisher.publishEvent(new AwardFulfillEvent(this, drawId));
}
```

---

## 3. 异步履约的可靠性（防丢失）

目前使用 `ApplicationEventPublisher` 发送异步事件，若进程在事件消费前宕机，该笔抽奖将停留在 `PROCESSING` 状态。

### 落地优化方案：
1. **使用 TransactionalEventListener**：确保只有事务真正 Commit 后才触发异步逻辑。
2. **离线扫表补偿**：
   - 启动一个 Schedule 任务（例如每 2 分钟运行一次）。
   - 查询 `status = 'PROCESSING'` 且 `create_time < now() - 5min` 的记录。
   - 对这些“卡单”记录重新触发 `AwardFulfillService.fulfill(drawId)`。

```java
@Component
public class LotteryFulfillScheduler {
    @Scheduled(cron = "0 */2 * * * ?")
    public void compensateProcessingRecords() {
        List<LotteryDrawRecord> timeoutRecords = drawRecordRepository.findTimeoutProcessing(5);
        for (LotteryDrawRecord record : timeoutRecords) {
            log.info("触发卡单补偿履约: drawId={}", record.getDrawId());
            eventPublisher.publishEvent(new AwardFulfillEvent(this, record.getDrawId()));
        }
    }
}
```

---

## 4. 高并发下的热点与倾斜优化

### 4.1 Redis 槽位分片
如果某活动的并发极高（每秒数万次），单个 Hash Key 会造成 Redis 单分片瓶颈。
- **方案**：根据 `userId` 的 Hash 值将用户的 `missStreak` 分布在不同的 Key 中（如 `lottery:miss:actId:{0..9}`）。
- **奖品库存**：对于大奖，可以将库存预分配到多个 Slot（如 `stock:awardId:slot1`），抽奖时随机选一个 Slot 进行扣减。

### 4.2 本地缓存预热
规则数据（`ActivityRules`）目前使用 Guava 缓存 30 秒，这在高并发下是合理的。但要注意**缓存穿透**（请求不存在的活动）和**缓存击穿**（活动开始瞬间大量请求涌入）。
- **方案**：活动开始前 1 分钟通过 Job 预热缓存，并对不存在的活动 ID 缓存一个 Null 对象。

---

## 5. 总结

该方案的核心逻辑是：**Redis 预占名额确保高性能，DB 唯一索引确保强幂等，异常捕获下的反向 Lua 脚本确保库存回撤，定时任务确保异步履约不丢单。** 这套组合拳可以有效解决 99.99% 的高并发抽奖一致性问题。
