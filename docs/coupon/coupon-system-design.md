# 电商高并发卡券系统设计方案

## 1. 业务背景与目标

### 1.1 业务场景
- 大促秒杀抢券（双11、618）：瞬时并发 10w+ QPS
- 日常发券/领券：常态 1w QPS
- 下单核销：与订单系统联动，要求强一致性
- 券批次管理：运营后台创建/修改券模板

### 1.2 核心指标
| 指标 | 目标值 |
|------|--------|
| 抢券接口 RT | P99 < 50ms |
| 峰值 QPS | 50w（含缓存层） |
| 库存扣减准确率 | 100%（不超发） |
| 核销一致性 | 最终一致，延迟 < 1s |
| 系统可用性 | 99.99% |

## 2. 系统架构总览

```
                        ┌─────────────┐
                        │   CDN/WAF   │
                        └──────┬──────┘
                               │
                        ┌──────▼──────┐
                        │  API Gateway │  限流/鉴权/灰度
                        └──────┬──────┘
                               │
              ┌────────────────┼────────────────┐
              │                │                │
      ┌───────▼──────┐ ┌──────▼───────┐ ┌──────▼───────┐
      │ 券模板服务    │ │  领券服务     │ │  核销服务     │
      │ (coupon-tmpl)│ │ (coupon-grab)│ │(coupon-verify)│
      └───────┬──────┘ └──────┬───────┘ └──────┬───────┘
              │                │                │
      ┌───────▼────────────────▼────────────────▼───────┐
      │                  Redis Cluster                   │
      │  (库存预扣 / 用户领取记录 / 分布式锁 / 令牌桶)    │
      └───────┬────────────────┬────────────────┬───────┘
              │                │                │
      ┌───────▼────────────────▼────────────────▼───────┐
      │              RocketMQ / Kafka                    │
      │         (异步落库 / 事件驱动 / 削峰填谷)          │
      └───────┬────────────────┬────────────────┬───────┘
              │                │                │
      ┌───────▼──────┐ ┌──────▼───────┐ ┌──────▼───────┐
      │  MySQL 主从   │ │ TiDB/分库分表 │ │  ES (查询)   │
      └──────────────┘ └──────────────┘ └──────────────┘
```

## 3. 核心数据模型

### 3.1 券批次表 (coupon_batch)
```sql
CREATE TABLE coupon_batch (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    batch_no        VARCHAR(32) NOT NULL UNIQUE COMMENT '批次号',
    name            VARCHAR(128) NOT NULL COMMENT '券名称',
    type            TINYINT NOT NULL COMMENT '1-满减 2-折扣 3-无门槛 4-运费券',
    discount_type   TINYINT NOT NULL COMMENT '1-固定金额 2-比例折扣',
    discount_value  DECIMAL(10,2) NOT NULL COMMENT '优惠值',
    threshold_amount DECIMAL(10,2) DEFAULT 0 COMMENT '使用门槛金额',
    total_count     INT NOT NULL COMMENT '发行总量',
    remain_count    INT NOT NULL COMMENT '剩余数量',
    per_user_limit  INT DEFAULT 1 COMMENT '每人限领',
    valid_type      TINYINT NOT NULL COMMENT '1-固定时间 2-领取后N天',
    valid_start     DATETIME COMMENT '固定开始时间',
    valid_end       DATETIME COMMENT '固定结束时间',
    valid_days      INT COMMENT '领取后有效天数',
    grab_start      DATETIME NOT NULL COMMENT '领取开始时间',
    grab_end        DATETIME NOT NULL COMMENT '领取结束时间',
    scope_type      TINYINT NOT NULL COMMENT '1-全场 2-品类 3-指定商品 4-店铺',
    status          TINYINT DEFAULT 0 COMMENT '0-待生效 1-进行中 2-已结束 3-已作废',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_status_grab(status, grab_start, grab_end)
) COMMENT '券批次表';
```

### 3.2 用户券实例表 (user_coupon) — 按 user_id 分库分表
```sql
CREATE TABLE user_coupon_{0~63} (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    coupon_no       VARCHAR(32) NOT NULL UNIQUE COMMENT '券码(雪花ID)',
    user_id         BIGINT NOT NULL COMMENT '用户ID',
    batch_no        VARCHAR(32) NOT NULL COMMENT '批次号',
    status          TINYINT DEFAULT 0 COMMENT '0-未使用 1-已锁定 2-已核销 3-已过期 4-已退回',
    order_no        VARCHAR(32) COMMENT '关联订单号',
    valid_start     DATETIME NOT NULL,
    valid_end       DATETIME NOT NULL,
    used_at         DATETIME COMMENT '核销时间',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_status(user_id, status),
    INDEX idx_batch(batch_no),
    INDEX idx_order(order_no)
) COMMENT '用户券实例表';
```

### 3.3 券适用范围表 (coupon_scope)
```sql
CREATE TABLE coupon_scope (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    batch_no    VARCHAR(32) NOT NULL,
    scope_type  TINYINT NOT NULL COMMENT '1-品类ID 2-商品SPU 3-店铺ID',
    scope_value VARCHAR(64) NOT NULL COMMENT '范围值',
    INDEX idx_batch(batch_no),
    INDEX idx_scope(scope_type, scope_value)
) COMMENT '券适用范围';
```

## 4. 核心流程设计

### 4.1 抢券流程（高并发核心）

```
用户请求 ──▶ Gateway限流(令牌桶) ──▶ 领券服务
                                        │
                          ┌─────────────▼──────────────┐
                          │ 1. Redis Lua 原子操作:       │
                          │    - CHECK 活动时间窗口      │
                          │    - CHECK 用户领取次数       │
                          │    - DECR 库存              │
                          │    - SADD 用户领取记录       │
                          │    (以上在一个Lua脚本中原子执行)│
                          └─────────────┬──────────────┘
                                        │
                              ┌─────────▼─────────┐
                              │ 2. 发送MQ消息       │
                              │    (异步落库)       │
                              └─────────┬─────────┘
                                        │
                              ┌─────────▼─────────┐
                              │ 3. 返回抢券成功     │
                              │    (券码即时生成)    │
                              └───────────────────┘

MQ Consumer:
  ──▶ INSERT user_coupon ──▶ UPDATE coupon_batch.remain_count
  ──▶ 失败重试(幂等保证: coupon_no 唯一键)
```

#### Redis Lua 脚本（核心防超发逻辑）
```lua
-- KEYS[1]: coupon:stock:{batchNo}     库存key
-- KEYS[2]: coupon:user:{batchNo}      用户领取集合
-- ARGV[1]: userId
-- ARGV[2]: perUserLimit
-- ARGV[3]: grabStart (timestamp)
-- ARGV[4]: grabEnd (timestamp)

local now = redis.call('TIME')
local ts = tonumber(now[1])

-- 校验活动时间
if ts < tonumber(ARGV[3]) or ts > tonumber(ARGV[4]) then
    return -1  -- 不在活动时间
end

-- 校验用户领取次数
local userCount = redis.call('HGET', KEYS[2], ARGV[1])
if userCount and tonumber(userCount) >= tonumber(ARGV[2]) then
    return -2  -- 超过限领次数
end

-- 扣减库存
local stock = redis.call('DECR', KEYS[1])
if stock < 0 then
    redis.call('INCR', KEYS[1])
    return -3  -- 库存不足
end

-- 记录用户领取
redis.call('HINCRBY', KEYS[2], ARGV[1], 1)
return 1  -- 成功
```

### 4.2 库存分片方案（应对热点Key）

单个券批次库存可能成为 Redis 热点 Key，采用库存分片：

```
coupon:stock:{batchNo}:0
coupon:stock:{batchNo}:1
...
coupon:stock:{batchNo}:7    (8个分片)

总库存 10000 → 每片 1250

领券时: shard = hash(userId) % 8
先扣本分片，若本片为0，尝试相邻分片（借库存）
```

```java
public class StockShardService {
    private static final int SHARD_COUNT = 8;

    public boolean decrStock(String batchNo, long userId) {
        int shard = (int)(userId % SHARD_COUNT);
        // 优先扣自己分片
        String key = "coupon:stock:" + batchNo + ":" + shard;
        Long remain = redisTemplate.opsForValue().decrement(key);
        if (remain != null && remain >= 0) return true;
        // 本片不足，回滚并尝试其他分片
        redisTemplate.opsForValue().increment(key);
        for (int i = 1; i < SHARD_COUNT; i++) {
            int next = (shard + i) % SHARD_COUNT;
            key = "coupon:stock:" + batchNo + ":" + next;
            remain = redisTemplate.opsForValue().decrement(key);
            if (remain != null && remain >= 0) return true;
            redisTemplate.opsForValue().increment(key);
        }
        return false;
    }
}
```

### 4.3 下单核销流程

```
下单请求 ──▶ 订单服务 ──▶ 调用核销服务(同步RPC)
                                │
                    ┌───────────▼───────────┐
                    │ 1. SELECT user_coupon  │
                    │    WHERE coupon_no=?   │
                    │    AND status=0        │
                    │    FOR UPDATE          │
                    ├───────────────────────┤
                    │ 2. 校验有效期/适用范围  │
                    ├───────────────────────┤
                    │ 3. UPDATE status=1    │
                    │    (锁定，非直接核销)   │
                    ├───────────────────────┤
                    │ 4. 返回优惠金额        │
                    └───────────┬───────────┘
                                │
支付成功回调 ──▶ MQ ──▶ UPDATE status=2 (已核销)
取消/超时   ──▶ MQ ──▶ UPDATE status=0 (释放)
```

状态机：
```
未使用(0) ──锁定──▶ 已锁定(1) ──支付成功──▶ 已核销(2)
    │                   │
    │                   └──取消/超时──▶ 未使用(0)
    │
    └──过期Job──▶ 已过期(3)

已核销(2) ──退款──▶ 已退回(4)
```

### 4.4 券退回流程
```
退款成功 ──▶ MQ消息 ──▶ 核销服务
                          │
                ┌─────────▼─────────┐
                │ 1. 校验券是否可退   │
                │    (已核销 & 未过期) │
                ├───────────────────┤
                │ 2. UPDATE status=4 │
                ├───────────────────┤
                │ 3. 重新发放同批次券  │
                │    (可选策略)       │
                └───────────────────┘
```

## 5. 高并发关键设计

### 5.1 多级限流

```
┌─────────────────────────────────────────────┐
│ L1: Nginx/Gateway 层                         │
│     - IP 维度: 10次/秒                       │
│     - 用户维度: 5次/秒                        │
│     - 接口维度: 总量 50w/秒                   │
├─────────────────────────────────────────────┤
│ L2: 应用层 Sentinel/自研                      │
│     - 热点参数限流(batchNo维度)               │
│     - 自适应限流(CPU > 80% 触发降级)          │
├─────────────────────────────────────────────┤
│ L3: Redis 层                                 │
│     - Lua 脚本内置前置校验，无效请求不落DB     │
└─────────────────────────────────────────────┘
```

### 5.2 缓存架构

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│ 本地缓存  │───▶│ Redis L1 │───▶│ Redis L2 │───▶│   MySQL  │
│ Caffeine │    │ (热数据)  │    │ (温数据)  │    │ (冷数据)  │
│ 10s TTL  │    │ 5min TTL │    │ 30min TTL│    │          │
└──────────┘    └──────────┘    └──────────┘    └──────────┘

- 券模板信息: 本地缓存 + Redis，变更时通过MQ广播失效
- 库存数据: 仅Redis，不做本地缓存（强一致要求）
- 用户券列表: Redis缓存，写操作后主动失效
```

### 5.3 防刷策略

| 层级 | 策略 | 实现 |
|------|------|------|
| 网关层 | 设备指纹 + IP频控 | WAF + Redis滑动窗口 |
| 业务层 | 用户行为风控 | 规则引擎(新注册/异常IP/批量特征) |
| 数据层 | 幂等 + 唯一约束 | coupon_no唯一键 + Redis SETNX |

### 5.4 降级预案

| 场景 | 降级策略 |
|------|---------|
| Redis 不可用 | 切换到本地令牌桶限流 + DB直接扣减(悲观锁) |
| MQ 不可用 | 同步写DB，RT升高但保证可用 |
| DB 不可用 | 返回"系统繁忙"，Redis数据定时补偿 |
| 大促峰值超预期 | 动态调整限流阈值 + 排队机制 |

## 6. 分库分表策略

### 6.1 user_coupon 分片规则
```
分库: user_id % 8  (8个库)
分表: user_id % 64 (每库8张表，共64张)

路由示例:
  user_id = 123456
  库: 123456 % 8 = 0  → coupon_db_0
  表: 123456 % 64 = 0 → user_coupon_0
```

### 6.2 查询路由
| 查询场景 | 路由方式 |
|---------|---------|
| 用户查自己的券 | user_id 直接路由 |
| 按订单号查券 | order_no → ES 查 user_id → 路由 |
| 按批次统计 | ES 聚合查询 |
| 运营后台查询 | ES 全量索引 |

## 7. 可观测性

### 7.1 核心监控指标
```yaml
# Prometheus metrics
coupon_grab_total:        # 领券总量 (by batch, result)
coupon_grab_rt:           # 领券RT分布
coupon_stock_remain:      # 实时库存 (by batch)
coupon_verify_total:      # 核销总量
coupon_mq_lag:            # MQ消费延迟
coupon_cache_hit_rate:    # 缓存命中率
coupon_redis_rt:          # Redis调用RT
```

### 7.2 告警规则
- 库存扣减速率异常（可能被刷）
- MQ 消费延迟 > 5s
- Redis Cluster 节点故障
- 领券成功率突降

## 8. 容量评估

### 8.1 大促场景（双11级别）
```
预估参数:
  - 券批次数: 500+
  - 单批次最大库存: 1000w
  - 峰值抢券 QPS: 50w
  - 日均核销量: 5000w

Redis:
  - 库存Key: 500 × 8分片 = 4000 key
  - 用户领取记录: ~2亿用户 × 平均5券 = 10亿条 Hash
  - 内存: ~200GB → 6节点集群(每节点64GB, 3主3从)

MySQL/TiDB:
  - user_coupon: 64张表 × 平均1500w行 = ~10亿行
  - 单表1500w，查询性能可控
  - 8主8从，SSD存储

MQ:
  - 峰值: 50w msg/s
  - Topic: coupon-grab / coupon-verify / coupon-expire
  - Broker: 6节点集群
```

## 9. 技术选型总结

| 组件 | 选型 | 理由 |
|------|------|------|
| 服务框架 | Spring Boot + Dubbo | 成熟稳定，团队熟悉 |
| 缓存 | Redis Cluster 7.x | 原生集群，Lua脚本支持 |
| 消息队列 | RocketMQ 5.x | 事务消息，延迟消息，阿里大促验证 |
| 数据库 | MySQL 8.0 + ShardingSphere | 分库分表成熟方案 |
| 搜索 | Elasticsearch 8.x | 复杂查询 + 聚合统计 |
| 限流 | Sentinel | 热点参数限流，自适应降级 |
| 分布式ID | 雪花算法(Leaf) | 趋势递增，高性能 |
| 监控 | Prometheus + Grafana | 云原生标准 |
| 链路追踪 | SkyWalking | 全链路追踪，性能分析 |

## 10. 扩展能力

- 券叠加规则引擎：支持多券叠加/互斥规则配置
- 智能发券：基于用户画像的精准发券（对接推荐系统）
- 跨店券/平台券：多商户结算分摊
- 券转赠：用户间券流转（需额外状态机）
- 国际化：多币种/多时区支持
