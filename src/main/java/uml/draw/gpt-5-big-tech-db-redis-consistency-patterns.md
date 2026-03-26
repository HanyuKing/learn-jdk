# 大厂常见的 DB 与 Redis 一致性方案

## 1. 这份文档讲什么

这份文档不是讲某一家公司的私有实现细节，而是基于公开工程博客和官方文档，整理大厂在 **DB 与 Redis 数据一致性** 上最常见的几类路线。

要先说明一点：

**大厂真正追求的通常不是“DB 和 Redis 任意时刻数字完全相等”，而是：**

1. 明确谁是真相源
2. 明确允许的是强一致还是最终一致
3. 出问题后可以靠事件、幂等、补偿、对账把状态收回来

也就是说，大厂通常不会长期接受“双真相源 + 定时比对聚合值修正”这种模式。

更常见的是：

1. **DB 是真相，Redis 是缓存**
2. **Redis 是热路径真相，DB 是最终账本**
3. **缓存内联到主数据访问层，写成功后同步更新缓存**

选哪条路，不取决于“技术偏好”，而取决于：

1. 写入并发有多高
2. 业务是否允许短暂脏读
3. 是否绝对不能超发/重复发
4. Redis 宕机时你是要降级、失败，还是继续服务

---

## 2. 大厂方案的共同原则

虽然不同公司的实现细节差很多，但公开资料里反复出现的原则基本一致。

### 2.1 单一真相源

大厂通常不会让 DB 和 Redis 同时长期承担“最终真相”。

更常见的是两种选择：

1. **DB 真相**
   - Redis 只是缓存
   - 缓存脏了可以删、可以重建

2. **Redis 热路径真相**
   - Redis 负责实时扣减、配额、库存
   - DB 负责异步账本、审计、补偿依据

真正麻烦的，往往就是：

1. Redis 保存实时库存
2. DB 也保存实时库存
3. 两边都参与线上判定
4. 最后靠后台任务比 `count(db)` 与 `get(redis)` 去修

公开实践里，这种做法几乎都被视为高风险。

### 2.2 事件闭环比聚合对账更重要

大厂更关注：

1. 某一笔写请求有没有成功
2. 某一个事件有没有落账
3. 某一笔 reservation 有没有补偿

而不是单纯看：

1. `dbIssued == redisIssued`
2. `dbStock == redisStock`

因为聚合值只告诉你“有偏差”，但不能告诉你：

1. 哪一笔错了
2. 是重复了还是丢了
3. 应该回滚谁
4. 回滚多少次才安全

### 2.3 幂等是基础设施，不是锦上添花

无论走哪条路线，大厂资料里几乎都会强调：

1. 请求幂等
2. 消费幂等
3. 补偿幂等

没有幂等，一次重试就会把一次异常放大成多次异常。

### 2.4 一致性方案必须带可观测性

公开资料里，大厂不是只讲“协议怎么设计”，还会讲：

1. 怎么量化 stale rate
2. 怎么监控 invalidation 失败
3. 怎么扫 pending
4. 怎么发现重试积压

这很关键。没有观测，所谓一致性设计很多时候只是理论。

---

## 3. 方案一：DB 是真相，Redis 是缓存

这是最常见、最经典的一条路线。

典型适用场景：

1. 读多写少
2. 允许短暂缓存脏读
3. 不能接受 Redis 变成强依赖真相源

### 3.1 常见实现：Cache Aside

这类方案里，最常见的是 `cache aside`。

基本流程：

1. 读：
   - 先查 Redis
   - miss 再查 DB
   - 回填 Redis

2. 写：
   - 先写 DB
   - 再删 Redis

不是“先写 DB，再更新 Redis”，而更常见的是“先写 DB，再删缓存”。

### 3.2 为什么大厂偏好“删缓存”而不是“更新缓存”

公开资料里，这几乎是共识。

原因很现实：

1. 更新缓存是第二次写操作
2. 并发时容易发生覆盖顺序错误
3. 更新失败比删除失败更难恢复

Alibaba Cloud 的公开文章就明确举了并发覆盖例子：

1. 线程 A 先更新 DB，后更新缓存
2. 线程 B 后更新 DB，但更早更新缓存
3. 最终 DB 是新值，缓存却被旧值覆盖

所以大厂公开实践里，更常见的是：

1. 写 DB 成功
2. 让缓存失效
3. 下次读 miss 后从 DB 重建

### 3.3 这条路怎么提升一致性

大厂不会只停在“写 DB 删缓存”。

常见增强手段有：

1. 重试删缓存
2. 延迟双删
3. binlog / CDC 订阅后删缓存
4. key 版本号
5. 合理 TTL

Meta 的公开文章也强调过：

1. 缓存不一致通常来自读填充和写失效之间的竞态
2. 版本号是常见冲突解决机制
3. 但仅有版本号还不够，因为缓存会 eviction，状态会丢失

### 3.4 大厂对这条路线的真实态度

这条路线并不追求：

> Redis 与 DB 任意时刻完全一致

它追求的是：

1. DB 永远正确
2. Redis 最终被删掉或重建
3. 脏读窗口可接受、可量化、可收敛

### 3.5 优点

1. 业务模型清晰
2. Redis 可以丢、可以重建
3. DB 查询始终能兜底
4. 运维和排障相对简单

### 3.6 缺点

1. 高写并发场景下，缓存命中率会下降
2. 写后短时间内仍可能脏读
3. 想把脏读窗口继续压低，会越来越依赖 CDC / 版本 / 删除重试

### 3.7 哪些大厂公开资料支持这条路线

可以归纳到这条路线下的公开资料包括：

1. Meta 的缓存一致性问题分析与版本冲突解决思路
2. AWS 的 cache-aside / write-through 缓存模式文档
3. Alibaba Cloud 关于 cache-aside、延迟双删、binlog 删缓存的文章

---

## 4. 方案二：DB 是真相，但用 CDC / Binlog / Outbox 驱动 Redis

这条路线本质上还是 **DB 真相**，只是比普通 cache aside 更工程化。

典型适用场景：

1. 写操作较多
2. 不能只靠应用层“写完手动删缓存”
3. 需要更稳的缓存失效与异步传播

### 4.1 为什么会演进到这条路线

单纯“写 DB，再删 Redis”有两个现实问题：

1. 删缓存失败
2. 双写顺序与重试逻辑容易漂

于是很多大厂会把“缓存变更”从业务线程里移出来，改成：

1. DB 先提交
2. 再由 binlog / CDC / outbox 异步驱动 Redis 变更

### 4.2 两种常见实现

#### 实现一：Binlog / CDC 驱动缓存失效

思路是：

1. 业务线程只管写 DB
2. DB 产生 binlog
3. 独立 CDC 服务订阅 binlog
4. CDC 服务删 Redis 或写 invalidation marker

Uber 的公开文章里就属于这类增强版方案。

它们一开始依赖：

1. TTL
2. CDC tailer（Flux）去异步失效或回填 Redis

后来因为 TTL + CDC 的最终一致还不够强，又把**写路径直接 invalidation**也补了进去，形成：

1. TTL
2. CDC
3. 同步写路径 invalidation

三套机制叠加，提高一致性。

#### 实现二：Transactional Outbox

这类方案主要解决“DB 更新 + 事件发送”双写问题。

AWS 的 Prescriptive Guidance 里对 transactional outbox 的表述很标准：

1. 在同一个 DB 事务里：
   - 更新业务表
   - 写 outbox 表
2. 事务提交后
   - 异步扫描 outbox
   - 发 MQ / 事件
   - 下游再更新 Redis / 物化视图 / 索引

它的价值在于：

1. DB 业务变更和“我要发出事件”同事务提交
2. 不再出现“DB 成功了但消息没发出去”的裸双写

### 4.3 这条路线的优势

1. 比单纯应用层删缓存更稳
2. 更适合服务拆分和事件驱动
3. 便于统一审计和重放
4. 一旦 CDC / outbox 平台成熟，可以复用到很多业务

### 4.4 这条路线的短板

1. 一样不是强一致
2. 仍然会有传播延迟
3. 需要补齐重试、死信、消费幂等
4. 监控链路更长，故障面更广

### 4.5 哪些大厂公开资料支持这条路线

公开可见的代表性材料包括：

1. Uber：MySQL binlog CDC + invalidation + TTL + 缓存观测
2. AWS：Transactional Outbox / CDC
3. Alibaba 公开文章：binlog 订阅删缓存、Canal 同步思路

---

## 5. 方案三：内联缓存 / Write-Through Cache

这条路线的重点不是“自己写 Redis 逻辑”，而是把缓存能力内联到主数据访问层。

典型适用场景：

1. 读多写也不少
2. 希望客户端不直接操作 Redis
3. 希望把缓存读写封装进统一 API

### 5.1 代表思路

AWS DAX 是最典型的公开例子。

它是一个读写穿透缓存：

1. 读 miss 时自动从 DynamoDB 拉数据并缓存
2. 写请求先写 DynamoDB
3. 底层写成功后再更新缓存

这样客户端只对一个 endpoint 编程，不自己处理 Redis 读写。

### 5.2 这种方案的核心价值

它把：

1. 读缓存
2. 填缓存
3. 写穿透
4. 缓存更新

都封装在“数据访问 API”里，减少业务代码中的缓存逻辑错误。

AWS 的公开资料就明确强调：

1. 读写都通过 DAX
2. DynamoDB 成功后才更新缓存
3. 客户端不再自己做 side cache 的双步操作

### 5.3 这种方案为什么大厂会喜欢

因为它减少了业务侧 bug：

1. 不用每个服务都自己写 cache aside
2. 不用每个服务都自己想 invalidation 时序
3. 客户端只用主存储 API

### 5.4 这种方案的代价

1. 缓存本身成了数据访问链路的一部分
2. 对缓存平台可用性要求高
3. 更适合统一存储平台，不太适合零散业务逻辑自己拼装

### 5.5 Uber 的做法更进一步

Uber 的 CacheFront 不是通用 Redis side cache，而是把缓存嵌入到查询引擎层。

它公开讲到的关键点包括：

1. 读先查 Redis
2. 写本来不拦截，因为 conditional update 不知道影响哪些行
3. 后来他们改造了写路径，让存储层返回本次事务真正影响的 row keys
4. 然后在写请求返回时直接 invalidation Redis
5. 背后仍保留 CDC/TTL 作为补强机制

这说明一个典型大厂方向：

> 当 TTL + 异步 CDC 不够时，会逐步往“写路径也直接驱动缓存一致性”演进

### 5.6 这条路线适不适合你

如果你的抽奖或库存系统已经有一个统一的存储访问层，这条路线有价值。

如果没有，而是应用层直接操作 MySQL + Redis，这条路线改造成本会很高。

---

## 6. 方案四：Redis 是热路径真相，DB 是最终账本

这条路线最适合你当前关注的高并发抽奖、库存、配额、限流、秒杀类问题。

典型适用场景：

1. 高并发写入
2. 线上判定必须非常快
3. 允许最终一致
4. 不能靠 DB 实时锁表判定

### 6.1 这条路线的本质

不是“Redis 做缓存”，而是：

1. Redis 负责实时扣减
2. Redis 负责实时判定
3. DB 负责异步账本
4. 对账按事件，不按聚合库存

### 6.2 这种方案怎么做

常见做法是：

1. Redis Lua 原子完成：
   - 幂等校验
   - 库存扣减
   - 配额扣减
   - 用户状态更新
   - 生成业务事件

2. 异步消费者把事件落 DB

3. DB 只负责：
   - 账本
   - 审计
   - 人工回溯
   - 补偿依据

4. 如果 DB 落账失败：
   - 通过 reservation / eventId 反向补偿 Redis

### 6.3 为什么大厂会这么做

因为对高并发写场景来说，真正难的不是“缓存一致性”，而是：

1. 怎么不超卖
2. 怎么不重复扣
3. 怎么高并发下还保持低延迟

这时候 DB 通常扛不住实时判定，Redis 更适合做热路径。

AWS 公开材料在讲 side-cache 与 write-back/write-behind 时，也明确提到：

1. 有些场景会先写缓存
2. 然后异步 de-stage 到持久层
3. 代价是这类写本身是 eventually consistent 且有丢数据风险

所以大厂如果走这条路，绝不会只“写 Redis 然后异步写 DB”就结束，通常还会补齐：

1. reservation
2. 幂等
3. 死信
4. 重试
5. 补偿

### 6.4 这条路线最容易做错的地方

不是 Lua 不够原子，而是后半段闭环没补齐。

比如：

1. Redis 成功扣减
2. DB 事务失败
3. 没有 reservation 记录
4. 没有补偿任务

那就会永久分叉。

所以真正成熟的大厂方案里，核心一般不是“Lua 很复杂”，而是：

1. 单笔事件有唯一 ID
2. 有 pending 集合
3. 有超时扫描
4. 有反向补偿
5. 有消费幂等

### 6.5 这条路线的优点

1. 热路径性能最好
2. 适合库存、抽奖、配额类场景
3. 更容易抗热点和突发流量

### 6.6 这条路线的缺点

1. 系统复杂度最高
2. Redis 持久化和可用性要求更高
3. 没有补偿体系就会很危险
4. DB 与 Redis 的聚合值通常不会在活动进行中随时相等

---

## 7. 幂等：大厂为什么都把它当底座

DB 与 Redis 一致性问题，很多时候表面上像缓存问题，实质上是“重试问题”。

公开资料里，Stripe 和 Shopify 都给了非常典型的思路。

### 7.1 Stripe 的做法

Stripe 的 idempotency 文档很直接：

1. 客户端带 idempotency key
2. 服务端保存第一次请求的结果
3. 后续同 key 请求直接返回同一个结果
4. 包括失败结果也会复用

这解决的是：

1. 网络超时后客户端重试
2. 服务端其实已经执行成功
3. 但客户端不知道结果

如果没有这个机制，很多“双写一致性问题”都会被重试放大。

### 7.2 Shopify 的做法

Shopify 在支付服务里公开讲得更进一步：

1. idempotency key 是 API 入参的一部分
2. 服务端按 “client + idempotency key” 做锁
3. 把请求记录存 DB
4. 如果是重试，就读取原请求状态和进度
5. 把请求拆成 recovery points，逐步恢复

这其实非常适合有外部副作用的流程：

1. 本地事务
2. 远程调用
3. 日志
4. MQ
5. 发券

### 7.3 对 DB 与 Redis 一致性的启发

只要一个业务链路里有：

1. Redis
2. DB
3. MQ
4. 远程履约

就必须先有幂等。

否则：

1. 重试一次
2. 你就分不清是第一次没成功，还是第一次成功了但响应丢了

大厂通常会把幂等建在：

1. requestId / idempotency key
2. eventId
3. messageId
4. outbox row id

这些稳定锚点上。

---

## 8. 观测：大厂怎么量化一致性

这也是公开资料里非常有价值但常被忽略的一部分。

### 8.1 Meta：Polaris + consistency tracing

Meta 的公开文章强调：

1. 设计一致缓存是一回事
2. 生产里量化一致性、定位 inconsistency 是另一回事

他们做了两类能力：

1. **Polaris**
   - 从客户端可观察角度量化缓存一致性
   - 不是只看内部指标

2. **Consistency tracing**
   - 只在真正可能引入缓存不一致的窗口里做 tracing
   - 用来定位根因

这说明大厂对一致性的真实态度是：

> 不可观测的一致性方案，等于没有方案

### 8.2 Uber：Cache Inspector

Uber 的公开文章里也有类似思路：

1. 用延迟的 CDC 流去对比缓存与数据库
2. 量化 mismatch rate
3. 看 staleness 分布
4. 再据此决定 TTL 是否能继续放大

这和简单 `count(db) != count(redis)` 的思路完全不同。

它关注的是：

1. stale 的比例
2. stale 持续时长
3. 哪类表更容易脏

### 8.3 对你们的启发

如果你们后面真的要把 Redis 用在抽奖热路径上，至少要补这些指标：

1. reservation pending 数
2. reservation 超时数
3. DB 落账失败数
4. 补偿成功数 / 失败数
5. 单笔 `drawId` 的最终状态分布
6. Redis 与 DB 的事件缺口，而不是只看聚合差值

---

## 9. 这些大厂方案怎么映射到抽奖 / 库存场景

结合你当前的抽奖需求，可以把公开大厂路线映射成三种可选方向。

### 方向 A：DB 真相 + Redis 缓存

适合：

1. QPS 不算极端
2. 能接受短暂脏读
3. 更重视模型简单和排障简单

典型实现：

1. DB 实时扣库存
2. Redis 只缓存可读数据
3. 写 DB 后删 Redis
4. binlog / CDC 辅助删缓存

优点：

1. 正确性最好解释
2. Redis 宕机不影响真相

缺点：

1. 热点库存场景吞吐一般

### 方向 B：Redis 热路径真相 + DB 最终账本

适合：

1. 高并发抽奖
2. 秒杀
3. 限额/配额/券抢占

典型实现：

1. Redis Lua 原子扣减
2. 生成 `drawId/reservationId`
3. 异步写 DB
4. 失败则单笔补偿

优点：

1. 性能最好

缺点：

1. 设计复杂
2. 必须补足幂等、补偿、观测

### 方向 C：统一存储访问层 + 内联缓存

适合：

1. 团队已经有平台化存储层
2. 不想让业务代码到处手写 Redis 逻辑

典型实现：

1. 统一 API
2. 读穿透
3. 写穿透
4. 同步 invalidation + 异步 CDC 双保险

优点：

1. 业务侧简单

缺点：

1. 平台改造成本大

---

## 10. 如果只总结成一句话

大厂对 DB 与 Redis 一致性的主流做法，不是“让两个地方永远同步到同一个数字”，而是：

> 明确单一真相源，用事件、幂等、补偿和观测把分布式状态闭环起来。

具体到路线选择：

1. **读多写少**：DB 真相 + cache aside / CDC 失效
2. **统一平台型缓存**：读穿透 / 写穿透 / 内联缓存
3. **高并发库存型业务**：Redis 热路径真相 + DB 最终账本 + 单笔事件补偿

真正成熟的大厂系统，通常不会长期依赖：

1. `dbIssued == redisIssued`
2. `dbStock == redisStock`

这种聚合值相等来证明一致性。

它们更依赖：

1. `requestId`
2. `eventId`
3. `reservationId`
4. `outbox id`

这些单笔业务锚点。

---

## 11. 对你当前抽奖系统的直接建议

如果只结合你现在这个抽奖系统来判断，我的结论还是一样：

### 最像大厂高并发方案的路线

是：

1. Redis 负责热路径库存/配额/保底计数
2. DB 负责最终账本
3. 每一笔抽奖有 `drawId/reservation`
4. 异步落库
5. 超时扫描 + 单笔补偿

### 最不建议继续走的路线

是：

1. Redis 和 DB 同时保存实时库存真相
2. 活动进行中拿聚合值比对
3. 不等就全量重建 Redis

这不符合公开大厂实践，也不适合高并发抽奖。

---

## 12. 参考资料

以下都是公开可访问的一手资料或官方文档：

1. Meta Engineering: Cache made consistent  
   https://engineering.fb.com/2022/06/08/core-infra/cache-made-consistent/

2. Uber Engineering: How Uber Serves over 150 Million Reads per Second from Integrated Cache with Stronger Consistency Guarantees  
   https://www.uber.com/en-IL/blog/how-uber-serves-over-150-million-reads/

3. AWS Prescriptive Guidance: Transactional outbox pattern  
   https://docs.aws.amazon.com/prescriptive-guidance/latest/cloud-design-patterns/transactional-outbox.html

4. AWS Database Blog: Amazon DynamoDB Accelerator (DAX): A Read-Through/Write-Through Cache for DynamoDB  
   https://aws.amazon.com/blogs/database/amazon-dynamodb-accelerator-dax-a-read-throughwrite-through-cache-for-dynamodb/

5. AWS Builders' Library: Caching challenges and strategies  
   https://aws.amazon.com/builders-library/caching-challenges-and-strategies/

6. AWS Whitepaper: Caching patterns - Database Caching Strategies Using Redis  
   https://docs.aws.amazon.com/whitepapers/latest/database-caching-strategies-using-redis/caching-patterns.html

7. Stripe Docs: Idempotent requests  
   https://docs.stripe.com/api/idempotent_requests

8. Shopify Engineering: Building Resilient GraphQL APIs Using Idempotency  
   https://shopify.engineering/building-resilient-graphql-apis-using-idempotency

9. Alibaba Cloud Community: Exploring Cache Data Consistency  
   https://www.alibabacloud.com/blog/600308
