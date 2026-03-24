# 高并发库存系统架构设计与落地实现方案 (Java版)

## 1. 整体架构与请求链路

本方案采用 **“预扣减 + 异步同步 + 最终一致性对账”** 架构，适用于秒杀、大促等极端高并发场景。

**核心链路**：
1. **APP/Web** -> 发起下单请求。
2. **库存服务 (应用层)** -> 拼接 Lua 脚本，向 Redis 发起扣减请求。
3. **Redis (缓存层)** -> 执行原子 Lua 脚本。成功则返回剩余库存，失败返回不足。
4. **库存服务 (应用层)** -> Redis 扣减成功后，向 RocketMQ 发送一条**可靠的库存流水消息**（或使用本地消息表）。
5. **MQ Consumer (异步层)** -> 监听流水消息，执行 DB 更新逻辑。
6. **MySQL (持久层)** -> 利用乐观锁执行实际的库存扣减。
7. **对账中心 (保障层)** -> 异步扫描比对，处理极其偶发的异常。

---

## 2. 核心代码落地 (Java 伪代码)

以下代码基于 Spring Boot、Spring Data Redis (Lettuce) 和 RocketMQ 编写。

### 2.1 Redis Lua 脚本预扣减 (The Try Phase)

首先，我们需要在 Redis 中执行原子的判断与扣减。

```lua
-- stock_deduct.lua
-- KEYS[1]: sku_stock_key (e.g., "stock:sku:1001")
-- ARGV[1]: deduct_quantity (e.g., "2")
local stockKey = KEYS[1]
local quantity = tonumber(ARGV[1])

local currentStock = redis.call('GET', stockKey)

-- 如果库存 key 不存在，或者库存不足
if (not currentStock or tonumber(currentStock) < quantity) then
    return -1 -- 库存不足，防超卖的核心防线
end

-- 执行扣减并返回剩余库存
local remain = redis.call('DECRBY', stockKey, quantity)
return remain
```

### 2.2 Java 应用层：执行 Lua 与发送 MQ

这里我们展示如何在业务代码中调用 Lua，并在成功后通过 MQ 异步通知 DB。

```java
@Service
@Slf4j
public class InventoryService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    // 提前加载 Lua 脚本到 Redis 缓存，提升性能
    private static final DefaultRedisScript<Long> DEDUCT_SCRIPT;
    static {
        DEDUCT_SCRIPT = new DefaultRedisScript<>();
        DEDUCT_SCRIPT.setLocation(new ClassPathResource("lua/stock_deduct.lua"));
        DEDUCT_SCRIPT.setResultType(Long.class);
    }

    /**
     * 执行高并发库存扣减
     * @param skuId 商品ID
     * @param quantity 数量
     * @param orderSn 订单流水号 (用于幂等和对账)
     * @return boolean 是否扣减成功
     */
    public boolean deductStock(Long skuId, int quantity, String orderSn) {
        String stockKey = "stock:sku:" + skuId;
        
        // 1. 执行 Redis Lua 脚本，原子扣减
        Long remainStock = redisTemplate.execute(
            DEDUCT_SCRIPT, 
            Collections.singletonList(stockKey), 
            String.valueOf(quantity)
        );

        if (remainStock != null && remainStock >= 0) {
            log.info("Redis 扣减成功. SKU:{}, 订单号:{}, 剩余库存:{}", skuId, orderSn, remainStock);
            
            // 2. 扣减成功，异步发送可靠消息给 DB 更新层
            // 注意：这里为了严谨，大厂通常会使用 RocketMQ 的【事务消息】或【本地消息表】来保证发送 MQ 和 Redis 操作的最终一致性。
            // 简单起见，这里展示普通可靠发送。
            StockDeductMessage msg = new StockDeductMessage(skuId, quantity, orderSn);
            rocketMQTemplate.syncSend("Topic_Stock_Update", msg);
            
            return true;
        } else {
            log.warn("库存不足或扣减失败. SKU:{}, 订单号:{}", skuId, orderSn);
            return false;
        }
    }
}
```

### 2.3 异步消费者层：DB 更新 (The Confirm Phase)

消费者负责将 Redis 的扣减结果落盘到 MySQL。

```java
@Service
@RocketMQMessageListener(topic = "Topic_Stock_Update", consumerGroup = "stock_update_group")
@Slf4j
public class StockUpdateConsumer implements RocketMQListener<StockDeductMessage> {

    @Autowired
    private InventoryMapper inventoryMapper;
    
    @Autowired
    private StockFlowMapper stockFlowMapper; // 记录幂等流水

    @Override
    @Transactional(rollbackFor = Exception.class) // 开启数据库事务
    public void onMessage(StockDeductMessage message) {
        log.info("收到异步库存扣减消息: {}", message.getOrderSn());
        
        // 1. 幂等性校验：通过 DB 唯一索引(order_sn)防重，或者查询流水表
        int count = stockFlowMapper.countByOrderSn(message.getOrderSn());
        if (count > 0) {
            log.warn("订单号 {} 的库存更新已处理过，触发幂等", message.getOrderSn());
            return;
        }

        // 2. 更新 DB 库存 (利用数据库乐观锁/行锁防超卖作为最后兜底)
        // SQL: UPDATE t_inventory SET stock = stock - #{quantity} WHERE sku_id = #{skuId} AND stock >= #{quantity}
        int updatedRows = inventoryMapper.deductStockInDB(message.getSkuId(), message.getQuantity());
        
        if (updatedRows == 0) {
            // 极端异常情况：Redis 扣成功了，但 DB 没库存了 (可能有人在后台绕过系统手动改了 DB)
            // 此时必须触发严重告警，并进入人工或自动化对账纠偏流程
            log.error("严重异常：DB扣减失败，库存不足！订单号:{}", message.getOrderSn());
            throw new RuntimeException("DB扣减失败，触发MQ重试或转人工");
        }

        // 3. 记录扣减流水，标识此单已成功落盘
        StockFlow flow = new StockFlow();
        flow.setOrderSn(message.getOrderSn());
        flow.setSkuId(message.getSkuId());
        flow.setQuantity(message.getQuantity());
        flow.setStatus("SUCCESS");
        stockFlowMapper.insert(flow);
        
        log.info("DB 更新库存及流水记录成功. 订单号:{}", message.getOrderSn());
    }
}
```

---

## 3. 一致性保障与无损纠偏落地代码

如前所述，极端情况下 Redis 和 DB 的数据会不一致。我们通过**对账扫描任务**来发现问题，并通过 **Delta 无损修复** 来解决。

### 3.1 对账纠偏伪代码 (流水补偿 + 幂等 Lua)

在高并发下，绝不能拿着“某一时刻”的 Redis 余额和 DB 余额直接相减来补偿（因为数据在时刻变化）。
**正确的做法是：只根据“缺失的流水”进行补偿，并且必须保证修复操作的幂等性。**

首先，编写用于纠偏的 Lua 脚本（保证原子性和防重）：

```lua
-- stock_reconcile.lua
-- KEYS[1]: sku_stock_key      (如 "stock:sku:1001")
-- KEYS[2]: sku_reconcile_key  (如 "stock:reconcile:1001")
-- ARGV[1]: 修复流水号/对账批次号 (保证幂等)
-- ARGV[2]: 补偿数量 (delta，正数表示加回库存，负数表示继续扣除)

local stockKey = KEYS[1]
local reconcileLogKey = KEYS[2]
local reqId = ARGV[1]
local delta = tonumber(ARGV[2])

-- 1. 幂等性判断：如果这个修复批次已经执行过，直接返回成功
if redis.call('SISMEMBER', reconcileLogKey, reqId) == 1 then
    return 1
end

-- 2. 执行无损增量修复
redis.call('INCRBY', stockKey, delta)

-- 3. 记录已修复流水号，并设置过期时间防止一直膨胀
redis.call('SADD', reconcileLogKey, reqId)
redis.call('EXPIRE', reconcileLogKey, 86400) -- 保留1天的对账记录

return 1
```

Java 层的对账修复调度逻辑：

```java
@Service
@Slf4j
public class ReconciliationService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private StockFlowMapper stockFlowMapper;

    // 纠偏 Lua 脚本预加载
    private static final DefaultRedisScript<Long> RECONCILE_SCRIPT;
    static {
        RECONCILE_SCRIPT = new DefaultRedisScript<>();
        RECONCILE_SCRIPT.setLocation(new ClassPathResource("lua/stock_reconcile.lua"));
        RECONCILE_SCRIPT.setResultType(Long.class);
    }

    /**
     * 基于流水的无损纠偏
     * @param skuId 商品ID
     * @param missingOrderSn 丢失或异常的订单流水号
     * @param delta 补偿值（例如 Redis 多扣了2件，DB 没记，需加回，则 delta=2）
     */
    public void fixByFlow(Long skuId, String missingOrderSn, int delta) {
        String stockKey = "stock:sku:" + skuId;
        String reconcileLogKey = "stock:reconcile:" + skuId;
        String reconcileReqId = "fix_" + missingOrderSn; // 构造纠偏幂等ID
        
        log.warn("执行异常流水补偿. SKU:{}, 异常流水:{}, 补偿量:{}", skuId, missingOrderSn, delta);

        // 使用 Lua 脚本保证增量补偿的原子性和幂等性
        redisTemplate.execute(
            RECONCILE_SCRIPT, 
            Arrays.asList(stockKey, reconcileLogKey), 
            reconcileReqId, 
            String.valueOf(delta)
        );
        
        log.info("流水补偿完成. SKU:{}, 修复标识:{}", skuId, reconcileReqId);
    }
}
```

### 3.2 获取“缺失流水”的工程方案

在高并发场景下，如何高效地把“Redis 里扣减了，但 DB 里没落盘”的异常流水精准挑出来？业界主要有以下三种架构落地路径：

#### 方案一：实时流计算对账（大厂标配：Flink 双流 Join）
适合千万级以上 QPS 的极致场景，毫秒级发现不一致，不给业务数据库增加任何查询压力。
*   **左流（Redis 侧）**：应用服务在执行 Redis Lua 扣减成功后，向 Kafka 发送一条极简的对账日志 `{ReqID, Status: Redis_OK}`。
*   **右流（DB 侧）**：通过 Canal 监听 MySQL Binlog，当 DB 流水表真正落盘后，向 Kafka 发送 `{ReqID, Status: DB_OK}`。
*   **Flink 处理逻辑**：开启一个 **T+5 分钟的滚动窗口**。以 `ReqID` 为 Key 尝试将左流与右流 Join。如果左流数据在窗口期内等不到右流对应的数据，Flink 会将这条孤立的左流数据输出到“异常侧输出流（Side Output）”。
*   **结果**：对账服务直接消费异常流，拿到的就是百分百缺失的流水。

#### 方案二：OLAP 离线/近线对账（中大厂推荐：ClickHouse）
适合百万级 QPS，通过列式存储数据库强大的分析能力进行分钟级全量窗口比对。
*   **数据汇聚**：将 Redis 扣减日志（通过 Flume/Filebeat）和 DB 增量流水（通过 Canal）全部实时同步到 ClickHouse 中。
*   **T+N 窗口扫描**：定时任务每分钟执行一次对账 SQL，锁定过去的 `[T-10分钟, T-5分钟]` 窗口（避开正在处理的在途数据）。
*   **获取缺失流水的 SQL**：
    ```sql
    -- 找出 Redis 成功了，但是 DB 流水表里没有的 ReqID
    SELECT r.req_id, r.sku_id, r.qty 
    FROM redis_log_table r
    LEFT JOIN db_flow_table d ON r.req_id = d.req_id
    WHERE r.create_time BETWEEN '10:00:00' AND '10:05:00'
      AND d.req_id IS NULL; 
    ```
*   **结果**：查询返回的结果集即为缺失流水，直接交给补偿服务处理。

#### 方案三：轻量级 Redis ZSet + DB 扫表（中小型团队首选）
不引入重型大数据组件，完全复用现有的 Redis 和 MySQL，开发成本极低。
*   **Redis 记录轨迹**：在执行核心 Lua 扣减脚本的同时，顺手将成功的单号存入 ZSet，Score 为当前时间戳：`ZADD stock:reconcile:queue {当前时间戳} {ReqID}`。
*   **T+N 定时抓取**：定时任务（如 XXL-Job）每隔一分钟，去 Redis 抓取 `[T-5分钟, T-4分钟]` 窗口内的所有成功单号集合 `Set_R`（例如通过 `ZRANGEBYSCORE`）。
*   **DB 批量反查**：拿着这批单号（几千到几万个），去 MySQL 流水表中执行 `SELECT req_id FROM db_flow WHERE req_id IN (...)`，得到 DB 真实落盘的单号集合 `Set_D`。
*   **内存求差集**：在 Java 内存中执行 `Set_R.removeAll(Set_D)`。剩下的单号就是 DB 漏记的缺失流水。
*   **清理与闭环**：对账完成后，通过 `ZREMRANGEBYSCORE` 删除 Redis ZSet 中的老数据释放内存；将拿到的缺失单号传入 `ReconciliationService.fixByFlow()` 进行无损纠偏。

### 3.3 极端场景：Redis 宕机丢失数据导致超卖的终极解法

**问题背景**：
Redis 的高性能伴随着数据持久化（AOF everysec）和主从复制的**异步性**。如果主节点突然物理宕机，最近几毫秒到一秒的扣减数据可能没有同步到磁盘或从节点。
**后果**：故障转移后，新启动的 Redis 恢复了旧数据（例如实际库存已扣至 100，但恢复后变成了 105）。此时涌入新流量，Redis 会错误地放行 5 个请求，导致**缓存层面的超卖**。

面对这种物理极限，架构上的解决思路不是“强行让 Redis 不丢数据”（那会丧失高并发能力），而是通过**“多级防御 + 业务降级”**来兜底：

#### 防御一：DB 行锁/乐观锁作为最后底线（物理防超卖）
即便 Redis 错误地放行了这 5 个请求，并发送了 5 条 MQ 消息。当这 5 条消息到达消费者并更新 MySQL 时，我们的 DB 更新 SQL 必须带有库存校验条件：
```sql
UPDATE t_inventory 
SET stock = stock - #{quantity} 
WHERE sku_id = #{skuId} AND stock >= #{quantity};
```
由于之前的请求已经把 DB 的真实库存扣到了 100（如果是 0 就扣完了），这多出来的 5 个异常请求在执行 SQL 时，**影响行数（affected rows）必为 0**。
**结论**：不管 Redis 怎么乱，数据库底层的真实库存**绝对不会变成负数**。

#### 防御二：业务层的“异步砍单与退款”（用户体验妥协）
拦截住了物理超卖，但用户那里出了问题：这 5 个用户在下单时，Redis 告诉他们成功了，甚至可能已经付了款。如何处理？
*   **捕获 DB 失败**：MQ 消费者在执行上述 SQL 发现影响行数为 0 时，抛出特定的 `OutOfStockException`。
*   **触发逆向流程（砍单）**：消费者捕获异常后，立即调用订单中心的“逆向取消接口”。
*   **用户侧通知**：将这 5 个订单的状态标记为“交易失败（库存不足）”，并自动触发全额退款。同时给用户发送抱歉短信（例如：“亲，您参与的秒杀过于火爆，系统网络拥挤导致发货失败，已为您全额退款”）。
*   **妥协哲学**：在千万级大促中，万分之一的“异步砍单”在商业上是完全可接受的（12306 和淘宝都存在类似机制），用极小的体验损耗换取了全局系统的可用性。

#### 防御三：主从切换时的“熔断与快照校准”（防患于未然）
为了尽量减少“砍单”的发生，可以在架构基础设施层做手脚：
*   **切换熔断**：当监控系统（Sentinel / Cluster）感知到 Redis 主节点宕机正在进行主从切换时，网关层或应用层立刻对该分片对应的核心热点 SKU 开启**短时间的写熔断（Reject 降级）**。
*   **紧急对账校准**：在这几秒的熔断期间，启动一个紧急脚本，拉取 DB 中该热点 SKU 的最新库存（`真实剩余 = 总库存 - DB已扣减流水`），强行覆写到新选举出的 Redis 主节点中。
*   **恢复流量**：数据校准完毕后（通常只需 1~2 秒），解除熔断，重新放行流量。此时新主节点的库存绝对是准确的，从根本上掐断了因丢失数据引发的连续超卖。

---
## 4. 关键避坑指南 (生产环境必读)

1. **Lua 脚本预热**：务必在系统启动时通过 `EVALSHA` 将脚本缓存到 Redis 节点，避免每次请求带上完整脚本导致网络带宽被打满。
2. **集群限制 (Hash Tag)**：如果是 Redis Cluster，同一个 Lua 脚本中操作的多个 Key 必须在同一个 Slot。建议在 Key 设计上加上 Hash Tag，例如 `{sku_1001}:stock` 和 `{sku_1001}:flow`。
3. **MQ 丢失问题**：上述代码在 Redis 成功后发送普通 MQ，如果在发送瞬间应用宕机，会导致 Redis 扣了但 DB 没扣。
    * **落地解法**：使用 **RocketMQ 事务消息** 或引入 **本地消息表**（Redis 扣减前插入一条状态为"待处理"的本地记录，定时任务扫描发送）。
4. **防并发缓存击穿**：在活动开始前（如 0 点），必须将参与秒杀的 SKU 提前预热写入 Redis。如果 Redis 里没有，不能在并发流量到来时去查 DB，这会直接打挂数据库。
