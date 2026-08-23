# 分布式系统专家学习路线（18-24 个月）

> 面向已经具备 Java、Go、数据库、Linux 和基本并发编程经验的工程师。本文从分布式概念重新建立体系，以 MIT 6.824/6.5840 实验为实践主线，以经典论文和真实系统为理论主线。

## 1. 先说清楚：什么叫“成为分布式专家”

分布式系统不是一组中间件的使用教程。真正的能力体现在：面对不可靠网络、部分故障、并发和数据复制时，能准确说明系统保证了什么、牺牲了什么，以及怎样验证这些保证。

完成本路线后，你应当能够：

1. 使用系统模型、故障模型、安全性和活性描述一个分布式算法。
2. 区分线性一致性、顺序一致性、因果一致性、快照隔离、可串行化和最终一致性。
3. 解释并实现领导者选举、日志复制、快照、分片迁移和故障恢复。
4. 判断 2PC、共识、Saga、TCC、Outbox 分别解决什么问题，不能解决什么问题。
5. 阅读论文时识别假设、关键不变量、正确性论证、实验设计和适用边界。
6. 从延迟、吞吐、可用性、一致性、成本和可运维性六个维度评审系统设计。
7. 用故障注入、竞态检测、历史记录检查和性能实验验证系统，而不只是运行 Happy Path。
8. 独立设计、实现和解释一个具备复制、持久化、可观测性及恢复能力的系统。

## 2. 资料库导航

| 文档 | 用途 |
| --- | --- |
| [经典论文清单](distributed_systems_papers.md) | 按主题列出原始论文、阅读级别、前置知识和阅读问题 |
| [实践与验收手册](distributed_systems_practice.md) | MIT 6.5840 实验、扩展项目、故障测试和验收标准 |
| [课程与业界资料](distributed_systems_industry_resources.md) | 官方课程、公开课、演讲、PPT、项目文档和源码入口 |

### 2.1 资料优先级

- **P0 必学**：不掌握就无法继续的概念、论文和实验。
- **P1 精读**：专家能力的核心组成，需要写读书笔记或完成实验。
- **P2 选读**：用于横向比较系统和建立技术判断。
- **P3 检索**：遇到具体设计问题时回查，不要求顺序阅读。

不要按论文数量衡量进度。读懂 40 篇、复现其中 5 篇，比浏览 200 篇摘要更有价值。

## 3. 总体知识地图

```text
计算机基础
├── 网络：TCP/UDP、拥塞、RPC、超时、DNS、TLS
├── 操作系统：进程/线程、调度、虚拟内存、文件系统、fsync
├── 并发：内存模型、锁、无锁、Channel、死锁
└── 数据库：索引、WAL、事务、隔离、查询执行
    │
    ▼
分布式理论
├── 时间：物理时钟、逻辑时钟、向量时钟、HLC、TrueTime
├── 模型：同步/异步、Crash/Byzantine、故障检测器
├── 一致性：线性、顺序、因果、最终、会话保证
├── 不可能性：Two Generals、FLP、CAP
└── 全局状态：分布式快照、稳定属性
    │
    ├───────────────┬────────────────┬─────────────────┐
    ▼               ▼                ▼                 ▼
复制与共识       分布式事务       分布式存储        分布式计算
Raft/Paxos       2PC/3PC           WAL/LSM/B-Tree     MapReduce
VR/Zab           Saga/TCC          分片/副本          Spark/Flink
PBFT/HotStuff     MVCC/OCC          Dynamo/Spanner     Kafka/流处理
    └───────────────┴────────────────┴─────────────────┘
                            │
                            ▼
工程系统：微服务、Kubernetes、多活、可观测性、混沌工程、容量与成本
```

## 4. 建议的学习方法

### 4.1 每周节奏

默认每周投入 10-15 小时：

| 活动 | 建议时间 | 产出 |
| --- | ---: | --- |
| 教材或课程 | 2-3 小时 | 概念卡片、疑问清单 |
| 论文精读 | 3-4 小时 | 一页论文笔记 |
| 编码实验 | 4-6 小时 | 可运行代码、测试记录 |
| 复盘与输出 | 1-2 小时 | 周报、架构图、错误复盘 |

### 4.2 论文四遍阅读法

1. **第一遍，15 分钟**：只看摘要、引言、图表和结论，回答“它解决什么问题”。
2. **第二遍，60 分钟**：梳理系统模型、故障假设、协议流程和评价指标。
3. **第三遍，2-4 小时**：推演关键场景，写出不变量，检查正确性论证和实验是否支持结论。
4. **第四遍，按需复现**：实现核心机制，构造论文没有强调的失败场景，并与替代方案比较。

每篇精读论文至少回答：

```text
问题：现有方案为什么不够？
模型：节点、网络、时钟和故障有哪些假设？
保证：安全性、活性、一致性、持久性分别是什么？
机制：最关键的不变量和状态转换是什么？
代价：正常路径和故障路径的消息轮次、延迟、存储和协调成本是什么？
证据：实验比较对象是否公平，工作负载是否代表真实场景？
边界：哪些情况不能处理？今天有哪些系统继承或改变了它？
```

### 4.3 实验纪律

- 开始编码前先写状态机、持久化边界和三个最重要的不变量。
- 所有网络调用都考虑超时、重试、重复、乱序和响应丢失。
- 使用 `go test -race`，并让随机故障测试持续运行，而不是只跑一次。
- 区分“操作执行一次”和“效果只出现一次”；网络上通常只能实现后者。
- 每次失败留下最小复现、事件时间线、根因和修复后新增的测试。

## 5. 阶段 0：补齐底层基础（第 1-4 周）

### 学习目标

- 能解释 TCP 连接、半连接、重传、队头阻塞以及超时为何不是故障证明。
- 理解线程、Go goroutine、Java Memory Model、Go Memory Model、锁和 Channel 的 happens-before。
- 理解文件缓存、WAL、`fsync`、崩溃一致性、B-Tree 与 LSM-Tree 的基本权衡。
- 熟练使用 Go 测试、竞态检测、pprof；能阅读 Java 并发和存储系统源码。

### 核心资料

- 《Computer Networking: A Top-Down Approach》：应用层、传输层和网络层重点章节。
- OSTEP：[免费官方版本](https://pages.cs.wisc.edu/~remzi/OSTEP/)。重点读并发、持久化和分布式章节。
- CMU 15-445/645：[Database Systems](https://15445.courses.cs.cmu.edu/)。重点读存储、并发控制和恢复。
- Go 官方：[The Go Memory Model](https://go.dev/ref/mem)。
- Java 官方：[Java Language Specification, Chapter 17](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html)。

### 阶段产出

- 写一个支持超时、取消、重试和请求 ID 的 Go RPC 小程序。
- 写一个最小 WAL：追加记录、校验和、崩溃恢复、截断不完整尾记录。
- 画出一次 RPC 从客户端到服务端及响应丢失后的完整事件时间线。

## 6. 阶段 1：分布式理论（第 2-3 个月）

### 学习目标

- 使用 happened-before 偏序分析事件，而不是依赖墙上时钟猜测顺序。
- 理解异步模型中“慢”和“故障”不可区分，以及 FLP 真正证明了什么。
- 能说明 CAP 中的一致性和可用性的形式定义，并避免“CAP 三选二”的误解。
- 掌握分布式快照、故障检测器、Quorum 和一致性模型。

### P0/P1 论文

按顺序精读 [经典论文清单](distributed_systems_papers.md) 中：

1. *Time, Clocks, and the Ordering of Events in a Distributed System*。
2. *Distributed Snapshots*。
3. *Impossibility of Distributed Consensus with One Faulty Process*。
4. *Unreliable Failure Detectors for Reliable Distributed Systems*。
5. *Brewer's Conjecture and the Feasibility of Consistent, Available, Partition-Tolerant Web Services*。
6. *Linearizability: A Correctness Condition for Concurrent Objects*。

### 实验和验收

- 实现 Lamport Clock、Vector Clock 和基于向量时钟的并发写检测。
- 用事件图判断三组历史是因果有序还是并发。
- 实现 Chandy-Lamport 快照模拟器，验证通道中在途消息没有丢失或重复。
- 能用自己的话说明 FLP 不代表“共识不能工作”，CAP 不代表“数据库只能三选二”。

## 7. 阶段 2：复制与共识（第 4-6 个月）

### 学习目标

- 掌握 Primary/Backup、Quorum、状态机复制、Paxos、VR、Raft 和 Zab 的关系。
- 能解释 Raft 的 Election Safety、Log Matching、Leader Completeness 和 State Machine Safety。
- 理解租约、读屏障、线性一致读、成员变更、快照和日志压缩。
- 建立 Crash Fault Tolerance 与 Byzantine Fault Tolerance 的边界。

### 学习顺序

1. Primary/Backup 与状态机复制。
2. Viewstamped Replication，再读 Paxos Made Simple。
3. Raft 论文、助教讲义和可视化。
4. ZooKeeper/Zab 与链式复制，比较不同复制路径。
5. PBFT 和 HotStuff，理解 BFT 法定人数与视图切换。

### 核心实践

开始 [MIT 6.5840 实践主线](distributed_systems_practice.md)：

- MapReduce 实验用于建立分布式故障意识。
- Raft 实验必须完成选举、复制、持久化和 Snapshot。
- 在 Raft 上完成线性一致 KV，并实现客户端请求去重。

### 阶段验收

- 画出发生网络分区、旧 Leader 恢复、日志冲突时每个节点的状态变化。
- 解释为什么提交旧任期日志需要特别处理。
- 官方测试、`go test -race` 和长时间随机测试全部通过。
- 写一篇 Paxos、VR、Raft 的共同抽象和工程差异对比。

## 8. 阶段 3：分布式事务与一致性（第 7-10 个月）

这是本路线的重点，不能把“分布式事务”简化成 2PC，也不能把 Saga 等同于最终一致性。

### 8.1 学习顺序

1. ACID、WAL、严格两阶段锁、OCC、MVCC 和恢复。
2. 可串行化、Snapshot Isolation、Write Skew、Adya 依赖图。
3. Atomic Commit、2PC、Presumed Abort/Commit、3PC 的同步假设。
4. Consensus 与 Atomic Commit 的区别，Paxos Commit 如何容忍协调者故障。
5. `Sagas` 原始论文：拆分、补偿、前向恢复、后向恢复和隔离问题。
6. TCC、Outbox/Inbox、事务消息、幂等、去重、对账和人工处置。
7. Percolator、Spanner、Calvin、CockroachDB、FoundationDB 的事务实现。
8. RAMP、Highly Available Transactions 和 I-confluence，判断何时可以避免协调。

### 8.2 必须澄清的边界

| 机制 | 主要目标 | 关键代价或边界 |
| --- | --- | --- |
| 2PC | 多参与者原子提交 | 协调者或参与者故障可能导致等待，仍需复制保障高可用 |
| Paxos/Raft | 多副本对命令顺序或值达成共识 | 本身不是跨多个资源的完整事务协议 |
| Paxos Commit | 通过共识容错地完成原子提交 | 更高的消息、状态和实现成本 |
| Saga | 用局部事务和补偿管理长业务流程 | 中间状态可见，补偿是语义撤销且也会失败 |
| TCC | 业务资源的 Try/Confirm/Cancel | 强侵入业务，需要悬挂、空回滚和幂等处理 |
| Outbox | 原子地记录业务数据与待发事件 | 只解决本地提交与发消息裂缝，消费者仍要幂等 |
| Exactly-once | 通常指特定边界内的效果一次 | 不等于现实世界副作用天然只执行一次 |

### 8.3 阶段产出

- 实现简化 2PC，注入协调者在不同日志点崩溃的故障。
- 实现 Java Saga + Outbox/Inbox 交易系统，覆盖补偿失败、重复消息和对账。
- 用 Elle/Jepsen 思想检查一组事务历史中的 G0、G1、G2 和 Write Skew。
- 比较 Spanner、Calvin、Percolator、FoundationDB 的排序、复制、提交和时间机制。

## 9. 阶段 4：分布式存储（第 11-14 个月）

### 学习目标

- 理解从磁盘布局到跨地域复制的完整数据路径。
- 掌握一致性哈希、Range Sharding、目录服务、再均衡和热点处理。
- 理解副本修复、Hinted Handoff、Read Repair、Anti-Entropy 和 Merkle Tree。
- 比较 Shared-Nothing、计算存储分离和 Shared-Storage 架构。

### 论文主线

1. LSM-Tree 与 Log-Structured File System。
2. GFS、Bigtable 和 MapReduce，理解 Google 早期基础设施三件套。
3. Dynamo、Cassandra，理解高可用 KV 的权衡。
4. Megastore、Spanner、F1，理解跨地域事务数据库演进。
5. Aurora、Snowflake、FoundationDB，理解存储计算分离和解耦架构。

### 阶段产出

- 实现带 WAL、MemTable、SSTable、Compaction、Bloom Filter 的单机 LSM KV。
- 为 KV 增加一致性哈希或 Range 分片、再均衡和副本恢复。
- 使用 6.5840 Sharded KV 实验验证配置切换与分片迁移。
- 对 TiKV、etcd、CockroachDB 或 FoundationDB 任选其一做源码调用链阅读。

## 10. 阶段 5：消息、批处理与流处理（第 15-17 个月）

### 学习目标

- 理解消息日志、分区、副本、Consumer Group、回压和重放。
- 区分事件时间与处理时间，掌握 Watermark、窗口、状态和 Checkpoint。
- 理解批处理、微批和持续流处理在容错模型上的差异。
- 能精确定义 Kafka/Flink 语境下的 Exactly-once 边界。

### 论文主线

- MapReduce、Dryad、RDD、Dremel、Pregel。
- Kafka、MillWheel、Dataflow、Flink、Naiad。
- The Log、流表二象性和端到端一致性相关工程材料。

### 阶段产出

- 完成一个 Kafka/RocketMQ 生产消费链路，演示重复、乱序、积压和再均衡。
- 用 Flink 完成有状态流处理，验证 Checkpoint 恢复、Watermark 和端到端一致性。
- 对同一任务比较 MapReduce、Spark 和 Flink 的执行图、状态与恢复机制。

## 11. 阶段 6：微服务、云原生与可观测性（第 18-20 个月）

### 学习目标

- 把 RPC 超时、重试、负载均衡、熔断、限流和幂等放进统一故障模型。
- 理解 Kubernetes API、控制器、调度器、etcd 和声明式协调循环。
- 掌握日志、指标、Trace、Profile 的用途以及关联方式。
- 理解尾延迟、负载削峰、请求复制、背压、过载保护和容量规划。

### 阶段产出

- 编写 Kubernetes Controller，处理幂等 Reconcile、最终状态和失败重试。
- 为前述项目加入 OpenTelemetry Trace、Prometheus 指标和结构化日志。
- 构造重试风暴、慢节点、热点 Key、MQ 积压并实现保护措施。
- 阅读 Borg、Omega、Dapper、Tail at Scale 和 SRE 公开材料。

## 12. 阶段 7：系统设计、验证与前沿（第 21-24 个月）

### 专家训练主题

- 跨地域多活、数据主权、RPO/RTO、演练和灾难恢复。
- TLA+/PlusCal、模型检查、属性测试、模糊测试和线性一致性验证。
- CRDT、Local-first、无协调事务和边缘计算。
- Serverless、存储计算分离、RDMA/CXL、分布式 AI 训练与推理基础设施。
- 多租户隔离、资源调度、成本模型、绿色计算和容量治理。

### 最终产出

1. 完成全部核心实践和三个扩展项目，见[实践与验收手册](distributed_systems_practice.md)。
2. 精读至少 40 篇论文，泛读至少 80 篇，形成可检索笔记。
3. 完成一次公开级技术输出：系列文章、公开分享、开源项目或论文复现报告。
4. 选择一个生产级项目持续阅读三个月，并提交架构、数据路径和故障路径分析。

## 13. 三条可选专精路线

完成前 14 个月共同基础后，可将 30%-40% 时间投入一个方向。

### 13.1 分布式数据库

- 深入 MVCC、Serializable、时间戳、分布式 SQL、查询优化和存储计算分离。
- 建议源码：TiKV/TiDB、CockroachDB、FoundationDB、OceanBase 公开资料。
- 目标：能设计事务协议，解释恢复路径，并用历史检查验证隔离级别。

### 13.2 流处理与消息系统

- 深入日志复制、状态后端、Checkpoint、Watermark、乱序、回压和端到端语义。
- 建议源码：Kafka、Flink、Pulsar、RocketMQ。
- 目标：能处理状态膨胀、热点、重放、升级和大规模故障恢复。

### 13.3 云原生与控制系统

- 深入调度、控制器、服务网格、可观测性、多集群和资源隔离。
- 建议源码：Kubernetes、etcd、containerd、Envoy、OpenTelemetry Collector。
- 目标：能设计稳定的控制循环，判断一致性与可用性边界并完成容量治理。

## 14. 月度复盘模板

```markdown
# YYYY-MM 月度复盘

## 本月解决的问题
- 

## 精读论文
| 论文 | 核心贡献 | 关键假设 | 尚未解决的问题 |
| --- | --- | --- | --- |

## 实验与故障
| 实验 | 注入故障 | 观察结果 | 根因/结论 |
| --- | --- | --- | --- |

## 能力验证
- 我现在能从头解释：
- 我现在能独立实现：
- 我仍然无法准确解释：

## 下月调整
- 停止：
- 继续：
- 开始：
```

## 15. 常见误区

1. **只背 CAP**：真实系统设计还需要延迟、隔离、持久性、故障模型和运维成本。
2. **只会调用中间件**：必须至少实现一次共识、事务恢复和持久化数据结构。
3. **把超时当失败**：超时只表示没有按时观察到结果，操作可能已经完成。
4. **把重试当可靠性**：无界重试会放大故障；必须配合预算、退避、幂等和过载保护。
5. **把 Saga 当回滚**：补偿是新的业务操作，不会抹除所有已被观察到的中间状态。
6. **把测试通过当正确**：并发协议需要不变量、模型推演和长时间随机故障测试共同支撑。
7. **追求资料“收藏齐全”**：资料库要持续维护；优先掌握主线，再按问题检索扩展资料。

## 16. 开始执行

第一周只做四件事：

1. 浏览本文知识地图，为不了解的术语建立待办，不立即逐个深挖。
2. 阅读 Lamport 1978 论文的摘要、引言和结论，画出 happened-before 示例。
3. 打开 MIT 6.5840 课程主页，准备 Go 环境并阅读第一个实验说明。
4. 创建论文笔记、实验日志和故障复盘三个固定模板。

然后按阶段推进。遇到新系统时，始终先问：它假设什么会失败，承诺什么不会发生，又通过什么证据证明？
