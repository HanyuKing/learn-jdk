---
title: 卡券系统 E-R 图（MVP 版）
---
```mermaid
erDiagram
    %% 卡券系统 E-R 图（配套 docs/coupon-tables-ddl.sql），共 6 张表
    %% 关系符号：||--|| 一对一，||--o{ 一对多
    %% 省略了 create_time / modified_time / closed / tenant_id 等通用列
    %% batch_id = 券批次ID（一次建券），coupon_id = 券ID（用户手里的一张券）

    trade_coupon_batch ||--|| trade_coupon_stock : "计数 MVP只有0号桶"
    trade_coupon_batch ||--o{ trade_coupon_scope : "指定商品时才有"
    trade_coupon_batch ||--o{ trade_user_coupon_00 : "发放"
    trade_user_coupon_00 ||--o{ trade_user_coupon_log : "状态变更"
    trade_user_coupon_00 ||--o{ trade_order_discount_detail : "分摊到订单行"

    trade_coupon_batch {
        bigint id PK
        bigint batch_id UK "券批次ID 雪花"
        varchar name "管理端名称"
        tinyint issue_channel "1全网公开 2直播 3客服 4领券中心 5自有 6新用户"
        tinyint countable "是否计入大促校验价"
        tinyint register_limit_type "注册时间限制 0不限 1注册N天内 2注册时间段"
        int register_within_days "注册后N天"
        datetime register_start_time "注册时间段开始"
        datetime register_end_time "注册时间段结束"
        datetime receive_start_time "领取开始"
        datetime receive_end_time "领取结束"
        tinyint valid_type "1同领取时间 2有效天数 3自定义"
        int relative_valid_days "valid_type=2"
        datetime valid_start_time "valid_type=3"
        datetime valid_end_time "valid_type=3"
        tinyint discount_type "1立减 2满减 3折扣"
        decimal discount_value "减免金额"
        decimal threshold_amount "满减门槛"
        decimal discount_rate "折扣率"
        decimal max_discount_amount "折扣封顶"
        int max_per_user "每人限领"
        tinyint scope_type "1指定商品 2全店商品"
        tinyint stack_layer "1商家券 2平台券"
        tinyint stackable "是否可叠加"
        tinyint funder "1平台 2商家"
        tinyint status "1正常 2已作废 其余状态按时间推导"
        datetime void_time "作废时间"
        varchar void_by "作废人"
        varchar void_reason "作废原因"
    }

    trade_coupon_stock {
        bigint id PK
        bigint batch_id FK
        int bucket_no "分桶号 MVP固定0"
        int total_stock "发放量 NULL=不限"
        int issued_count "已发放 领券时条件更新"
        int used_count "已核销 按流水批量累加"
    }

    trade_coupon_scope {
        bigint id PK
        bigint batch_id FK
        tinyint scope_type "1商品"
        varchar ref_id "商品ID"
    }

    trade_user_coupon_00 {
        bigint id PK
        bigint coupon_id UK "券ID 雪花 低位含分片号"
        varchar user_id "分片键"
        bigint batch_id FK
        tinyint source_type "1主动领取 2系统发放"
        tinyint status "1可用 2冻结 3已使用 4已作废 过期按时间推导"
        varchar order_id "冻结或核销的订单"
        datetime receive_time
        datetime valid_start_time "领取时算好"
        datetime valid_end_time "领取时算好"
        datetime used_time
        tinyint discount_type "快照"
        decimal discount_value "快照"
        decimal threshold_amount "快照"
        decimal discount_rate "快照"
        decimal max_discount_amount "快照"
        varchar request_no "发放幂等号"
        int version "乐观锁"
    }

    trade_user_coupon_log {
        bigint id PK
        bigint coupon_id FK
        varchar user_id
        bigint batch_id
        varchar action "ISSUE FREEZE USE RELEASE REVOKE"
        tinyint before_status
        tinyint after_status
        varchar order_id
        varchar request_no UK "幂等号"
        tinyint mq_sent "MQ是否已发 补偿用"
        varchar operator
        varchar remark
    }

    trade_order_discount_detail {
        bigint id PK
        varchar main_order_id
        varchar order_id "子订单"
        varchar line_id "订单行"
        varchar sku_id
        varchar layer_code "PROMOTION SHOP_COUPON PLATFORM_COUPON"
        int layer_order "执行顺序"
        bigint batch_id
        bigint coupon_id FK
        varchar promotion_id
        tinyint funder "1平台 2商家"
        decimal original_amount "本行原价"
        decimal amount "分摊到本行减免额"
        json unit_amount_json "按件分摊"
        decimal refunded_amount "已退分摊额"
    }
```

## 建券页面 → 字段

| 页面区块 | 控件 | 字段 |
|---|---|---|
| 基础规则 | 推广方式 | `trade_coupon_batch.issue_channel` |
| | 不计入大促校验价 | `trade_coupon_batch.countable` |
| | 新用户渠道：注册时间 | `register_limit_type` + `register_within_days` 或 `register_start_time` / `register_end_time` |
| | 优惠券名称 | `trade_coupon_batch.name` |
| | 领取时间 | `receive_start_time` / `receive_end_time` |
| | 使用时间三选一 | `valid_type` + 对应字段 |
| 优惠设置 | 立减 | `discount_type=1` + `discount_value` |
| | 满 20 减 10 | `discount_type=2` + `threshold_amount=20` + `discount_value=10` |
| | 折扣 | `discount_type=3` + `discount_rate` |
| | 券发放量：不限 / 10000 张 | `trade_coupon_stock.total_stock`：NULL / 10000 |
| | 每人限领 | `trade_coupon_batch.max_per_user` |
| | 指定商品（商品券） | `scope_type=1` + `trade_coupon_scope` 多行 |
| | 全店商品（店铺券） | `scope_type=2`，不写范围表 |

## 主要流程用到哪些表

**建券**：同一事务写 `trade_coupon_batch` 一行、`trade_coupon_stock` 一行；指定商品时再写 `trade_coupon_scope`。

**领券**：
1. 读 `trade_coupon_batch` 校验状态和领取时间（走缓存）
2. 查 `trade_user_coupon_xx` 统计这个人领了几张，校验每人限领
3. 条件更新 `trade_coupon_stock.issued_count`，影响 0 行就是领完了
4. 写 `trade_user_coupon_xx`，`valid_*` 按使用时间规则算好，规则字段做快照
5. 写 `trade_user_coupon_log`，动作 ISSUE

**下单 → 支付 → 退款**：只改 `trade_user_coupon_xx.status` 和 `order_id`，每次写一条 log；下单时写 `trade_order_discount_detail`，退款时读它、累加 `refunded_amount`。

**核销统计**：定时任务按流水批量累加 `trade_coupon_stock.used_count`。

**作废券**：改 `trade_coupon_batch.status = 2`，记作废时间、人、原因。之后不能再领，已领的券照常可用。

## 状态：只存操作结果，时间相关的状态查询时推导

不用定时任务改状态，状态永远和当前时间一致。

### 券（管理后台列表筛选）

存储只有 `1正常` `2已作废`。筛选项按下面推导，命中即停：

| 筛选项 | 条件 |
|---|---|
| 已作废 | `status = 2` |
| 未开始 | `status = 1` 且 当前时间 < `receive_start_time` |
| 已过期 | `status = 1` 且 当前时间 > `receive_end_time` |
| 生效中 | `status = 1` 且 在领取时间内 |

已领完不单独作为状态，仍显示生效中，领券时由计数表拦截。作废不可恢复。

### 用户券（用户券列表 tab）

存储 `1可用` `2冻结` `3已使用` `4已作废`：

| tab | 条件 |
|---|---|
| 可使用 | `status = 1` 且 当前时间 <= `valid_end_time` |
| 已过期 | `status = 1` 且 当前时间 > `valid_end_time` |
| 已使用 | `status = 3` |
| 已作废 | `status = 4` |

```
                              ┌──────── 超时/取消 ────────┐
                              ▼                          │
  领取 ──> 1 可用 ──下单锁券──> 2 冻结 ──支付成功──> 3 已使用 ──整单退款──> 1 可用
             │                   │
             └───────运营作废────┴──> 4 已作废
```

下单锁券时校验券在有效期内；锁券后到期的允许继续支付。

## 使用时间在领取时怎么算

| valid_type | 生效 | 失效 |
|---|---|---|
| 1 与领取时间相同 | 领取时刻 | `receive_end_time` |
| 2 限制有效天数 | 领取时刻 | 领取时刻 + `relative_valid_days` 天 |
| 3 自定义时间 | `valid_start_time` | `valid_end_time` |

例：领取时间 11.1~11.19，用户 11.3 领取，选"与领取时间相同"，可用期为 11.3~11.19。
