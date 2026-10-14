# 优惠券系统架构：业界资料整理与设计参考

> 整理日期：2026-10-14
> 配套：[coupon-system-design.md](coupon-system-design.md)（本仓库已有的高并发抢券设计方案）、`trade-order-api/src/test/java/cn/mathmagic/jjewelry/coupon`（算价/分摊 demo）
> 说明：本文第 1~7 节是业界公开资料的提炼，第 8 节对照本仓库现状。资料来源见文末，引用处标 [n]。

---

## 0. 一页总览

| 主题 | 业界共识 | 本仓库现状 |
|---|---|---|
| 领域模型 | 规则 / 模板(批次) / 实例 三层 | 只有实例表 `trade_coupons`，规则内联在实例上 |
| 发放方式 | 领券接口、定向发券、券码兑换 三种 | 券码兑换一种（预生成 code，领取时绑定用户） |
| 库存防超发 | Redis+Lua 预扣 → 异步落库；或 DB 库存拆字段 | 无库存概念（code 即库存，一码一券） |
| 幂等 | 唯一索引 + 幂等键 | 分布式锁（按 userId） + status 校验 |
| 用券一致性 | TCC：下单冻结 → 支付成功核销 → 失败解冻 | status 有"冻结"态，流程需确认 |
| 分摊 | 按比例到行、尾差最后一行兜底、记承担方 | 无（demo 已实现，未接入） |
| 退款 | 部分退不退券、实付与优惠同比例退；全退退券 | `CouponsRefundRecord` 存在，规则需确认 |
| 过期 | 延时消息循环投递 / 通知表定时扫 | 读时判断 `expireTime` / `takeExpireTime` |

---

## 1. 领域模型

业界对核心概念的定义高度一致 [1][2][3]：

```
rule（规则）            可复用的优惠规则：类型、门槛、面额/折扣、适用范围、互斥
   │ 1:N
coupon_batch / template（券批次 / 券模板）
                        一次发放计划：总量、余量、已发数、领取/使用时间窗、每人限领
   │ 1:N
coupon（券实例）        发到具体用户的一张券：user_id、batch_id、status、order_id、时间戳
```

- **券批次**就是"一批券的模板"。例：商家建 1000 张券，双 11 当天数码类目满 100 减 50 [1]。
- 规则单独建表的好处：多批次复用同一规则；规则变更不影响已发券（已发券冗余快照）。
- 实例表按 `user_id` 分库分表，并**冗余部分规则字段**减少查询 IO [4]。vivo 的路由：库后缀 = hash(userId)/M%N，表后缀 = hash(userId)%M。

### 1.1 典型字段

**coupon_batch / template** [1][5]
```
batch_no, name, type(满减/折扣/现金), rule_json, source_type(平台券/店铺券)
total, remaining, assign_count
receive_start/end, use_start/end, valid_type(固定日期/领取后 N 天), valid_days
apply_range(全场/品类/商品/店铺), per_user_limit, per_day_limit, is_mutex
```

**coupon（实例）** [1]
```
coupon_id(分布式唯一), user_id, batch_id, status, order_id
received_time, valid_at, expire_at, used_time
```

**状态机**
```
UNCLAIMED(可选) → UNUSED → FROZEN(下单锁定) → USED(支付成功)
                   │           │
                   │           └── 支付失败/取消 → UNUSED
                   └── 过期 → EXPIRED
USED → 整单退款 → UNUSED / 或发新券（取决于业务）
```

### 1.2 券与商品关系 [4]
- 黑名单：适用 = 全部 − 黑名单，适合范围广的券
- 白名单：适用 = 白名单，适合范围窄的券
- 超级黑名单：对所有券生效（如特价、定制、虚拟商品）

---

## 2. 系统拆分

业务生命周期四步：**创、发、用、计**（创建 / 发放 / 使用 / 统计）[4]。按此拆服务 [1][2]：

```
┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
│ 模板服务  │  │ 发放服务  │  │ 核销服务  │  │ 统计服务  │
│ B端配置   │  │ 领/发/兑 │  │ 查可用券  │  │ 发放/使用 │
│ 规则/批次 │  │ 库存扣减  │  │ 冻结/核销 │  │ 成本/ROI │
│          │  │ 幂等/限流 │  │ 退回      │  │          │
└──────────┘  └──────────┘  └──────────┘  └──────────┘
```

核销服务通常不做算价，算价（含分摊）在订单/营销域，核销服务只负责券状态流转。

---

## 3. 发放

### 3.1 三种方式 [4]

| 方式 | 场景 | 要点 |
|---|---|---|
| 统一领券接口 | 活动页/商详页用户主动领，低价值券 | 防超领、防超发、一键领多张 |
| 定向发券 | 运营按人群批量发，高价值券；亿级规模 | 去事务、批量插入、可重试、参数动态可调 |
| 券码兑换 | 站外营销（短信、线下） | 码不可猜、防重复兑换、防撞库 |

### 3.2 防超发：两条路线

**路线 A：Redis + Lua 预扣，异步落库** [5][6][7]
- 库存放 Redis 单 key，Lua 脚本把"查库存 → 扣库存 → 记用户领取"打成一个原子操作，单分片 10w ops/s 级别 [7]。
- 扣减成功后发 MQ，消费者写 MySQL；本地事务表 + 最大努力通知保证最终一致 [5]。
- **分段增补**：Redis 剩余 < M 时，异步从 DB 补 N 个，保证 Redis 库存 ≤ DB 库存，双重兜底 [5]。M 是发券速率兜底，N 是极端情况下最大可丢失库存。
- 字节跳动春节活动实测：MySQL 单实例写约 4000 QPS，Redis 单分片写 2w / 读 10w；目标 12w QPS → 30 个 MySQL 主库、至少 6 个 Redis 分片；线上按压测 1.5 倍预留 [6]。

**路线 B：DB 库存拆字段** [4]
- vivo 商城的选择：把单个库存字段拆成 M 份写入库存记录表，领券时随机选一份扣减，定时任务汇总。
- 理由："突破了数据库单行锁的瓶颈限制，且实现简单"，不用处理缓存丢失/宕机恢复。
- 适合 QPS 在千级、团队更看重简单可靠的场景。

### 3.3 热点 key：库存拆桶 [6]
同一个爆款券模板的库存 key 落在固定 Redis 分片，会打爆单分片。做法：
- 建模板时就把库存拆成 `stock_key_1 … stock_key_k`
- 每次请求生成**随机不重复的分片序号数组**轮询扣减（不能总从 1 开始，否则压力没分散）
- 记录各子库存余量，耗尽的直接跳过
- 对比"key 备份"方案：适合读多写少，不适合发券这种写密集场景

### 3.4 幂等与防重领

- **幂等**：业务方传幂等键，存实例表并建唯一索引 [4][6]；MQ 消费端必须幂等，否则重复消费 = 超发 [1]。
- **防重领**：Redis Set `batch_id:{id}:users` 做 SISMEMBER/SADD [1]；或 Lua 里顺带校验用户每日/总上限 [5]。
- **一键领多张**：锁 key 不能拼成 `uid+A+B+C`（和 `uid+A` 不互斥），要对每张券分别加锁 `uid+A`、`uid+B`，任一失败则自旋等待 [4]。

### 3.5 二级缓存 [6]
模板读取是强依赖，Redis 超时概率万分之 2~3，这部分发券必然失败。解法是本地内存二级缓存 + 定时刷新；回写 Redis 时加分布式锁避免多实例并发回写。

---

## 4. 使用（核销）

### 4.1 校验时机 [1]
在**确认订单页**校验，而不是提交订单或支付时：过期、适用范围、门槛、互斥。用户体验是"选券时就知道能不能用"。

### 4.2 跨服务一致性：TCC [1]
```
Try     创建订单 → 券置 FROZEN
Confirm 支付成功 → 券置 USED，记 order_id
Cancel  支付失败 / 超时 / 取消 → 券回 UNUSED
```
订单关闭的补偿任务要能找到冻结中的券并解冻，否则券"丢"在冻结态。

### 4.3 多优惠叠加顺序 [8][9]

业界（淘宝/京东）层级：
```
单品级   秒杀、限时折扣、前 N 件        默认取最低价生效，不叠加
   ↓
店铺级   店铺满减、店铺券、商品券         一笔子订单一个店铺活动 + 一张店铺券
   ↓
跨店级   跨店满减、品类券、购物津贴、红包  平台承担
   ↓
资产     积分、礼品卡、余额
```

互斥规则要穷举出来 [8]：
- 店铺券与商品券互斥，默认生效力度最大的
- 店铺券 + 店铺满减活动可叠加
- 单品优惠和店铺优惠通常互斥，否则让利太大

**平行式门槛** [9]：淘宝、京东各层门槛都按"单品优惠价"判断，各层互不影响。另一种是逐层递减基数（上一层扣完后的金额判下一层门槛），本仓库 demo 用的是后者，更保守。

### 4.4 分摊 [8][9][10]

**为什么**：券抵的是整单，退款/结算/开票按商品行。分摊到行是退款金额和成本核算的依据，也防恶意退单薅羊毛。

**公式**
```
行分摊额 = 优惠总额 × 行金额 / 参与行金额合计
最后一行 = 优惠总额 − 其余行已分摊之和        ← 尾差兜底
```

**原则**
- 只在满足条件的商品范围内分摊，不盲分 [9]
- 多层优惠逐层分摊：先券后红包，红包基数是"用完券后的价格"，比例仍按原价占比 [10]
- 分摊前按价格**升序**排列，高价放最后：避免最后一行只有 0.02 却要承担 0.04 的尾差 [10]（评论区指出这只能减少、不能根治，根治要靠"每份不超上限、溢出前移"的分摊器）
- 1 分钱商品不参与抵扣 [10]
- 记录**承担方**：店铺券/店铺活动由商家承担，跨店满减/品类券/红包由平台承担，也有按比例共担 [9]

**例子** [10]（A 5.01、B 3.42、C 2.13，合计 10.56，券 1.57）
```
A: 1.57 × 0.47 = 0.73 → 4.28
B: 1.57 × 0.32 = 0.50 → 2.92
C: 1.57 − 0.73 − 0.50 = 0.34 → 1.79    （不能按比例算成 0.31，否则总和不等于 1.57）
```

---

## 5. 退款与退券

### 5.1 规则 [10][11]

| 场景 | 退款金额 | 券 |
|---|---|---|
| 整单取消/全退 | 实付原路退 | 退回整张券（或发新券） |
| 部分退款 | **实付与优惠同比例退** | 不退 |
| 多次部分退累计 100% | 最后一次 = 各支付方式实付 − 已退 | 退回整张券 |

"同比例退"的意思：退商品 A 时，退款 = A 实付分摊 × 退款比例，A 上的券分摊也同比例"消耗"掉。不能先退实付再退券，否则被薅 [10]。

### 5.2 部分退款后不满门槛
两种策略，是业务选择：
- **不追回**：用户薅到，平台认亏。体验好，多数平台的选择。
- **追回**：从退款里扣回其他行上的优惠。规则复杂、易客诉，一般只在高客单价/风控场景用。

红包等虚拟资产随每次退款按比例返还，券不按比例返还（一张券不可分割）[10]。

---

## 6. 过期处理

| 方案 | 优点 | 缺点 |
|---|---|---|
| 定时扫实例表 | 简单 | 数据量大时慢 SQL |
| 延时消息 | 精确 | 30 天长效券造成 MQ 积压；延时有上限需循环投递 [6] |
| 通知表 + 定时扫 [1] | 建券时写 `notify_msg`，`uid+batch+日期` 唯一索引保证每天通知一次，扫完删除 | 多一张表 |
| 读时判断 | 零成本 | 统计报表不准，需补离线任务 |

字节的做法是延时消息到期后若未真正过期就再投一次，直到过期 [6]。

---

## 7. 工程保障

- **限流**：对各上游调用方分别限流；前端按钮短时置灰；部分请求直接跳繁忙页 [1][6]
- **超时**：发券内部 < 100ms，RPC 超时 500ms，避免被上游拖垮 [6]
- **隔离/熔断**：外部依赖隔离，防雪崩 [4]
- **读写分离**：主从延迟 < 1ms 用户无感 [4]
- **对账**：reconciler 校验 Redis 库存 + 已发实例 = 原始供给 [7]
- **不停机迁移** [4]：停运营操作 → 单写旧库 → 迁静态数据 → 双写 → 迁动态数据 → 切读 → 关双写
- **压测**：先找单实例瓶颈（MySQL 一主、Redis 单分片）再估资源整体压；资源按 1.5 倍预留 [6]
- **设计模式** [12]：优惠计算/返奖规则用策略模式抽象（新老用户不同策略），责任链串校验，工厂按类型装配。美团那篇原文链接已 404，要点来自搜索摘要。

---

## 8. 对照本仓库

### 8.1 现状（`business/coupon`）

```
trade_coupons（Coupon 实体）
  code            预生成券码，一码一券
  userId          领取后绑定
  status          0 未使用 1 已使用 2 冻结 3 待领取
  discountType    1 固定价购买 2 折扣 3 优惠价
  discountValue   优惠值
  applicableProducts  JSON：shopProduct / customizedProduct 白名单
  type            0 小红书 1 隐藏款 2 得物
  expireTime / takeExpireTime  码的过期 / 领取后 1 个月过期
  userTagName     人群标签

CouponService
  generateAndSaveCoupons  批量生成 code 并逐条 save
  claimCoupon             分布式锁(userId) + 校验 code/状态/过期/同 type 唯一 → 绑定用户
  unUsedByUserId / isTakeExpired

CouponsRefundRecord       退券记录：orderId, promotionChannel, rightsStatus, retryCount
```

这是一个**券码兑换型**的轻量实现：没有模板/批次层，规则（类型、面额、适用商品、过期）直接写在每张券上；"库存"就是预生成的 code 数量，不存在并发扣减问题。

### 8.2 差距与建议（按投入从小到大）

| # | 差距 | 建议 | 投入 |
|---|---|---|---|
| 1 | 无分摊，退款/结算无依据 | 把 demo 的 `PricingEngine` 接入下单算价，订单行落 `promo_discount` / `coupon_discount` / `pay_amount` | 中 |
| 2 | 规则内联在实例上，改规则要改每张券；同批次无法统计 | 引入 `coupon_batch` 表，实例加 `batch_id`，实例上保留规则快照字段做兼容 | 中 |
| 3 | `generateAndSaveCoupons` 逐条 save | 批量插入；失败的 code 记录后可重试 | 小 |
| 4 | 过期只在读时判断 | 加定时任务刷 status=EXPIRED，报表才准 | 小 |
| 5 | 冻结态流转和订单关闭的解冻补偿 | 确认 TCC 三步是否闭环，补偿任务扫 FROZEN 超时券 | 需调研 |
| 6 | 领券锁按 userId | 当前一人一次领一张，够用；若要一键领多张，参考 3.4 按券分别加锁 | 暂不需要 |
| 7 | 无用户主动领券/定向发券 | 按业务需要再做，做的时候才需要 Redis 库存那一套 | 大 |

分摊是最先要补的：它不依赖券模型重构，且退款、对账现在就需要。

---

## 9. 资料来源

**中文**
1. [大厂的优惠券系统是如何设计的？](https://cloud.tencent.com/developer/article/1998935) — 腾讯云开发者社区。三表模型、MQ 发券、TCC 核销、站内信演进、过期通知表。知乎同源版：[优惠券系统设计](https://zhuanlan.zhihu.com/p/472524058)
2. [浅谈优惠券系统设计](https://blog.csdn.net/usst_lidawei/article/details/79494386) — CSDN。发券 vs 领券区分、接口清单
3. [电商优惠券实战设计](https://blog.csdn.net/qq_44627031/article/details/146256717) — CSDN。完整 `coupon_template` 建表 SQL
4. [vivo 全球商城：优惠券系统架构设计与实践](https://www.cnblogs.com/vivotech/p/15117264.html) — vivo 互联网技术。创发用计、三种发放方式、DB 库存拆字段、分库分表、不停机迁移。腾讯云转载：[链接](https://cloud.tencent.com/developer/article/1858565)
5. [万级 TPS 优惠券系统设计与实践](https://cloud.tencent.com/developer/article/2465372) — 腾讯云。Redis+Lua 扣减脚本校验顺序、分段增补、热点三方案
6. [如何从零搭建 10 万级 QPS 优惠券系统](https://developer.volcengine.com/articles/7599494235956215854) — 字节跳动/火山引擎。容量数字、库存拆桶、二级缓存、压测方法
7. [记录些系统设计架构实现方案（8）优惠券系统](https://blog.csdn.net/lichunericli/article/details/141233633) — CSDN。10w QPS 抢券、缓存拆分
8. [电商系统-优惠券叠加规则、优惠分摊介绍](https://blog.csdn.net/liudada8265/article/details/127020471) — CSDN。淘宝层级、互斥规则、四个分摊例子
9. [优惠券叠加 & 分摊逻辑](https://zhuanlan.zhihu.com/p/479921433) — 知乎。平行式门槛、承担方
10. [电商场景下，多种优惠叠加时的商品平摊和退款规则](https://www.woshipm.com/pd/5777215.html) — 人人都是产品经理。尾差、排序、部分退款同比例。知乎版：[链接](https://zhuanlan.zhihu.com/p/617179389)
11. [自营电商平台优惠券密卷篇（二）：优惠券系统架构设计](https://www.woshipm.com/pd/4358854.html) — 人人都是产品经理。发放/核销/退回系统划分
12. [设计模式在外卖营销业务中的实践](https://tech.meituan.com/2020/03/19/design-pattern-practice-in-marketing.html) — 美团技术团队（原链接已 404，要点来自搜索摘要）
13. [电商库存系统的防超卖和高并发扣减方案](https://juejin.cn/post/7263124928663683132) — 京东云技术团队。库存扣减通用方案，可类比券库存
14. [亿级高并发优惠券系统全方位架构设计实践](https://developer.aliyun.com/article/1202117) — 阿里云开发者社区（正文需登录/JS 渲染，未能抓取）

**英文**
15. [System Design: E-Commerce Flash Sales (10M Users, Coupon System, One-Per-User)](https://crackingwalnuts.com/post/flash-sale-system-design) — 等候室、令牌门、Lua 扣减、幂等、Saga 回滚的完整链路
16. [Flash Sale System Design: Architecture, Scale, and Oversell](https://singhajit.com/flash-sale-system-design/) — 含完整 Lua 脚本
17. [From Database Locks to Redis+Lua: 100x QPS Boost in Coupon Flash Sales](https://dev.to/_eb7f2a654e97a60ae9f96e/from-database-locks-to-redislua-100x-qps-boost-in-coupon-flash-sales-13o0) — DB 锁为什么不行
18. [GitHub: victorzhong0110/flashcoupon](https://github.com/victorzhong0110/flashcoupon) — Go 参考实现：Lua 预扣、Redis Stream 异步落库、XAUTOCLAIM 重试、死信、对账
19. [GitHub: eswar1207/flash-sale-engine](https://github.com/eswar1207/flash-sale-engine) — 四种库存策略 k6 压测对比
20. [GitHub: tidi-dev/high-concurrency-flash-sale](https://github.com/tidi-dev/high-concurrency-flash-sale) — 幂等为什么必须（reservationId 做主键）
21. [Design a Flash Sale System That Never Oversells Stock](https://www.matthewswong.com/en/blog/design-flash-sale-inventory-system/) — 何时该在 Postgres 前加 Redis
22. [Coupon & Discount Engine Development](https://www.codesoltech.com/blog/coupon-discount-engine-development/) — 规则引擎：资格评估 → 计算 → 记录核销；叠加分组
