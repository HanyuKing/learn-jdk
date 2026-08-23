# 分布式系统经典论文清单

> 本文优先列出原始论文或作者/会议官方版本。`P0` 表示主线必读，`P1` 表示精读，`P2` 表示按方向选读，`P3` 表示遇到具体设计问题再查。论文的年份以正式发表版本为准；同一主题的教程性文章会单独标明，不替代原始论文。

## 1. 如何使用清单

### 1.1 先读这 20 篇

如果完全不知道从哪里开始，先按以下顺序读：

1. Lamport 1978：逻辑时钟与因果顺序。
2. Chandy-Lamport 1985：分布式快照。
3. Fischer-Lynch-Paterson 1985：FLP 不可能定理。
4. Chandra-Toueg 1996：故障检测器。
5. Gilbert-Lynch 2002：CAP 形式化。
6. Herlihy-Wing 1990：线性一致性。
7. Oki-Liskov 1988：Viewstamped Replication。
8. Lamport 2001：Paxos Made Simple。
9. Ongaro-Ousterhout 2014：Raft。
10. Castro-Liskov 1999：PBFT。
11. Gray 1978：事务和数据库操作系统。
12. Skeen 1981：非阻塞提交协议。
13. Garcia-Molina-Salem 1987：Sagas。
14. Berenson et al. 1995：SQL 隔离级别批评。
15. DeCandia et al. 2007：Dynamo。
16. Ghemawat et al. 2003：GFS。
17. Chang et al. 2006：Bigtable。
18. Dean-Ghemawat 2004：MapReduce。
19. Corbett et al. 2012：Spanner。
20. Zaharia et al. 2012：Spark RDD。

### 1.2 论文笔记模板

```markdown
# 论文：

## 一句话问题
## 系统模型与故障模型
## 核心抽象
## 关键不变量
## 安全性（Safety）
## 活性（Liveness）
## 正常路径与故障路径
## 复杂度：消息、轮次、存储、恢复
## 实验是否支持结论
## 局限与今天的替代方案
## 可复现实验
```

## 2. 分布式系统基础理论

### 2.1 时间、因果关系和全局状态

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *Time, Clocks, and the Ordering of Events in a Distributed System* | Leslie Lamport, 1978 | happened-before、Lamport Clock、全序 | [作者 PDF](https://lamport.azurewebsites.net/pubs/time-clocks.pdf) |
| P0 | *Distributed Snapshots: Determining Global States of Distributed Systems* | Chandy, Lamport, 1985 | 全局状态、Marker、在途消息 | [DOI](https://doi.org/10.1145/214451.214456) |
| P1 | *Logical Time in Distributed Computing Systems* | Colin Fidge, 1991 | 向量时间、偏序和并发检测 | [DOI](https://doi.org/10.1109/71.80168) |
| P1 | *Virtual Time and Global States of Distributed Systems* | Friedemann Mattern, 1989 | 向量时钟与一致全局状态 | [作者 PDF](https://www.vs.inf.ethz.ch/publ/papers/VirtTimeGlobStates.pdf) |
| P2 | *Logical Physical Clocks and Consistent Snapshots in Globally Distributed Databases* | Kulkarni et al., 2014 | HLC、物理时钟和逻辑时钟结合 | [arXiv](https://arxiv.org/abs/1407.4805) |

### 2.2 不可能性、共识和故障检测

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *Impossibility of Distributed Consensus with One Faulty Process* | Fischer, Lynch, Paterson, 1985 | 异步系统中的 FLP 不可能性 | [MIT PDF](https://groups.csail.mit.edu/tds/papers/Lynch/jacm85.pdf) |
| P0 | *Unreliable Failure Detectors for Reliable Distributed Systems* | Chandra, Toueg, 1996 | 完美/最终完美故障检测器 | [DOI](https://doi.org/10.1145/226643.226647) |
| P0 | *Brewer's Conjecture and the Feasibility of Consistent, Available, Partition-Tolerant Web Services* | Gilbert, Lynch, 2002 | CAP 的形式化证明 | [DOI](https://doi.org/10.1145/564585.564601) |
| P1 | *The Byzantine Generals Problem* | Lamport, Shostak, Pease, 1982 | Byzantine 故障、口头/签名消息模型 | [作者 PDF](https://lamport.azurewebsites.net/pubs/byz.pdf) |
| P1 | *Using Time Instead of Timeout for Byzantine Fault-Tolerant Systems* | Castro, Liskov, 2001 | BFT 超时和视图切换设计 | [DOI](https://doi.org/10.1109/71.969926) |

### 2.3 一致性模型与可观察历史

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *Linearizability: A Correctness Condition for Concurrent Objects* | Herlihy, Wing, 1990 | 线性一致性的正式定义 | [DOI](https://doi.org/10.1145/78969.78972) |
| P1 | *Session Guarantees for Weakly Consistent Replicated Data* | Terry et al., 1994 | Read-your-writes、Monotonic Reads 等会话保证 | [DOI](https://doi.org/10.1145/185478.185482) |
| P1 | *Eventually Consistent* | Werner Vogels, 2009 | 最终一致性的工程语义 | [ACM Queue](https://queue.acm.org/detail.cfm?id=1466448) |

### 2.4 RPC、成员发现、Gossip 与覆盖网络

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *Implementing Remote Procedure Calls* | Birrell, Nelson, 1984 | RPC 绑定、调用语义、异常与重复请求 | [DOI](https://doi.org/10.1145/2080.357392) |
| P1 | *Epidemic Algorithms for Replicated Database Maintenance* | Demers et al., 1987 | Gossip、Anti-Entropy 和谣言传播 | [DOI](https://doi.org/10.1145/41840.41841) |
| P1 | *SWIM: Scalable Weakly-consistent Infection-style Process Group Membership Protocol* | Das et al., 2002 | 可扩展成员检测、怀疑和传播 | [DOI](https://doi.org/10.1109/DSN.2002.1028914) |
| P1 | *Consistent Hashing and Random Trees* | Karger et al., 1997 | 一致性哈希、节点变更和负载分布 | [DOI](https://doi.org/10.1145/258533.258660) |
| P1 | *Chord: A Scalable Peer-to-peer Lookup Service for Internet Applications* | Stoica et al., 2001 | DHT、Finger Table 和路由复杂度 | [DOI](https://doi.org/10.1145/383059.383071) |
| P2 | *Pastry: Scalable, Decentralized Object Location, and Routing for Large-scale Peer-to-peer Systems* | Rowstron, Druschel, 2001 | 前缀路由、局部性和覆盖网络 | [DOI](https://doi.org/10.1007/3-540-45518-3_18) |

## 3. 复制与共识

### 3.1 状态机复制与经典协议

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *Viewstamped Replication: A New Primary Copy Method to Support Highly-Available Distributed Systems* | Oki, Liskov, 1988 | Primary/Backup、View、日志复制 | [DOI](https://doi.org/10.1145/62266.62278) |
| P1 | *Viewstamped Replication Revisited* | Liskov, Cowling, 2012 | 现代 VR、成员变更、恢复 | [作者 PDF](https://pmg.csail.mit.edu/papers/vr-revisited.pdf) |
| P0 | *The Part-Time Parliament* | Leslie Lamport, 1998 | Paxos 原始论文 | [作者 PDF](https://lamport.azurewebsites.net/pubs/lamport-paxos.pdf) |
| P0 | *Paxos Made Simple* | Leslie Lamport, 2001 | Paxos 教程性表述 | [作者 PDF](https://lamport.azurewebsites.net/pubs/paxos-simple.pdf) |
| P0 | *In Search of an Understandable Consensus Algorithm* | Ongaro, Ousterhout, 2014 | Raft、可理解性、日志复制 | [USENIX PDF](https://www.usenix.org/system/files/conference/atc14/atc14-paper-ongaro.pdf) |
| P1 | *Chain Replication for Supporting High Throughput and Availability* | van Renesse, Schneider, 2004 | 链式复制和故障修复 | [DOI](https://doi.org/10.1145/1015467.1015483) |
| P1 | *The Chubby Lock Service for Loosely-Coupled Distributed Systems* | Burrows, 2006 | Paxos 类协调服务的工程实践 | [USENIX OSDI](https://www.usenix.org/legacy/events/osdi06/tech/burrows.html) |
| P1 | *ZooKeeper: Wait-free Coordination for Internet-scale Systems* | Hunt et al., 2010 | Zab、顺序写、协调 API | [USENIX ATC](https://www.usenix.org/legacy/events/atc10/tech/full_papers/Hunt.pdf) |

### 3.2 Byzantine Fault Tolerance

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *Practical Byzantine Fault Tolerance* | Castro, Liskov, 1999 | PBFT、3f+1、副本状态机 | [DOI](https://doi.org/10.1145/571637.571640) |
| P1 | *UpRight Cluster Services* | Clement et al., 2009 | BFT 服务组合和恢复 | [SOSP](https://dl.acm.org/doi/10.1145/1629575.1629592) |
| P1 | *Zyzzyva: Speculative Byzantine Fault Tolerance* | Kotla et al., 2007 | 乐观执行和推测响应 | [SOSP](https://doi.org/10.1145/1294261.1294277) |
| P1 | *HotStuff: BFT Consensus with Linearity and Responsiveness* | Yin et al., 2019 | 线性通信、响应性、模块化 BFT | [arXiv](https://arxiv.org/abs/1803.05069) |
| P2 | *The Honey Badger of BFT Protocols* | Miller et al., 2016 | 异步 BFT 和批处理 | [ACM CCS](https://doi.org/10.1145/2976749.2978399) |

## 4. 分布式事务、恢复与并发控制

### 4.1 事务基础与数据库恢复

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *Notes on Database Operating Systems* | Jim Gray, 1978 | 事务、锁、日志、恢复的基础框架 | [Microsoft Research](https://www.microsoft.com/en-us/research/publication/notes-on-database-operating-systems/) |
| P1 | *The Transaction Concept: Virtues and Limitations* | Jim Gray, 1981 | ACID、事务边界和系统代价 | [Microsoft Research](https://www.microsoft.com/en-us/research/publication/the-transaction-concept-virtues-and-limitations/) |
| P1 | *Concurrency Control in Distributed Database Systems* | Bernstein, Goodman, 1981 | 分布式 2PL、时间戳和可串行化 | [ACM Digital Library](https://dl.acm.org/) |
| P1 | *Principles of Transaction-Oriented Database Recovery* | Haerder, Reuter, 1983 | WAL、Undo/Redo、恢复管理器 | [ACM Digital Library](https://dl.acm.org/) |
| P1 | *A Critique of ANSI SQL Isolation Levels* | Berenson et al., 1995 | 隔离异常、锁和 Snapshot Isolation | [DOI](https://doi.org/10.1145/223784.223785) |
| P1 | *Generalized Isolation Level Definitions* | Adya, Liskov, O'Neil, 2000 | 依赖图、G0/G1/G2、隔离级别 | [MIT PDF](https://pmg.csail.mit.edu/papers/adya-phd.pdf) |
| P2 | *Making Snapshot Isolation Serializable* | Cahill, Röhm, Fekete, 2008 | SSI、冲突检测和可串行化 | [DOI](https://doi.org/10.1145/1376616.1376620) |

### 4.2 Atomic Commit：2PC、3PC、共识提交

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *Nonblocking Commit Protocols* | Dale Skeen, 1981 | 2PC 阻塞、3PC 的同步假设 | [DOI](https://doi.org/10.1145/358818.358824) |
| P1 | *Transaction Management in the R* Distributed Database Management System* | Mohan et al., 1986 | 生产级 2PC、Presumed Abort/Commit | [IBM Research](https://research.ibm.com/publications/transaction-management-in-the-r-distributed-database-management-system) |
| P1 | *Consensus on Transaction Commit* | Gray, Lamport, 2006 | Atomic Commit 与 Consensus 的关系 | [Microsoft Research](https://www.microsoft.com/en-us/research/publication/consensus-on-transaction-commit/) |
| P2 | *Three-Phase Commit Protocol* | Skeen, 1982 | 非阻塞提交的进一步讨论 | [ACM Digital Library](https://dl.acm.org/) |

### 4.3 Sagas、长事务和补偿

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *Sagas* | Garcia-Molina, Salem, 1987 | 长事务拆分、补偿事务、前向/后向恢复 | [DOI](https://doi.org/10.1145/38713.38742) |
| P1 | *ConTracts: A Means for Extending Control Beyond Transaction Boundaries* | Wachter, Reuter, 1992 | 跨事务边界的流程控制 | [ACM Digital Library](https://dl.acm.org/) |
| P1 | *Life beyond Distributed Transactions: An Apostate's Opinion* | Pat Helland, 2007 | 大规模系统中的业务事务、消息和补偿 | [CIDR](https://www.vldb.org/cidrdb/) |
| P1 | *Building on Quicksand* | Helland, Campbell, 2009 | 跨实体事务和业务不变量 | [CIDR](https://www.vldb.org/cidrdb/) |

> Saga 不是数据库回滚的远程版本。原始论文必须和实际补偿设计一起读：补偿是新的业务操作，可能失败、重试、重复或需要人工介入；中间状态也可能已经被其他参与者观察到。

### 4.3.1 TCC、Outbox 与工程模式的资料边界

TCC（Try-Confirm-Cancel）、Transactional Outbox、Inbox、事务消息和工作流编排通常来自工业系统、专利、产品文档或工程模式演化，并不存在一篇被学界普遍认定为“TCC 原始论文”的单一来源。因此学习时采用以下分层：

- 理论基础读 `Sagas`、2PC、原子提交和隔离级别论文。
- 工程实现读 [Saga Pattern](https://microservices.io/patterns/data/saga.html)、[Transactional Outbox](https://microservices.io/patterns/data/transactional-outbox.html) 和 [Seata 官方文档](https://seata.apache.org/docs/overview/what-is-seata)。
- 设计评审必须说明 Try/Confirm/Cancel 的幂等、空回滚、悬挂、超时、重试、补偿失败和人工介入语义，不能把模式名称当作一致性证明。

### 4.4 高可用事务和协调规避

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P1 | *Highly Available Transactions: Virtues and Limitations* | Bailis et al., 2014 | 分区下事务可用性的边界 | [VLDB PDF](https://www.vldb.org/pvldb/vol7/p181-bailis.pdf) |
| P1 | *Scalable Atomic Visibility with RAMP Transactions* | Bailis et al., 2014 | 无全局同步的原子可见性 | [SOSP](https://doi.org/10.1145/2660193.2660231) |
| P1 | *Coordination Avoidance in Database Systems* | Bailis et al., 2015 | I-confluence 和无协调更新 | [VLDB PDF](https://www.vldb.org/pvldb/vol8/p185-bailis.pdf) |
| P2 | *Calvin: Fast Distributed Transactions for Partitioned Database Systems* | Thomson et al., 2012 | 预先排序、确定性执行 | [USENIX OSDI](https://www.usenix.org/conference/osdi12/calvin-fast-distributed-transactions-partitioned-database-systems) |

## 5. 复制数据、一致性和 CRDT

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *Bayou: A Weakly Connected Replicated Storage System* | Terry et al., 1995 | 弱连接、冲突解决、应用定义合并 | [USENIX](https://www.usenix.org/conference/osdi95/bayou-weakly-connected-replicated-storage-system) |
| P0 | *Dynamo: Amazon's Highly Available Key-value Store* | DeCandia et al., 2007 | 一致性哈希、Quorum、向量时钟、Hinted Handoff | [SOSP PDF](https://www.allthingsdistributed.com/files/amazon-dynamo-sosp2007.pdf) |
| P1 | *COPS: Consistency in the Cloud* | Lloyd et al., 2011 | 因果加一致性和依赖追踪 | [SOSP](https://doi.org/10.1145/2043556.2043592) |
| P1 | *Stronger Semantics for Low-Latency Geo-Replicated Storage* | Lloyd et al., 2013 | Eiger、因果一致只读/写事务 | [NSDI](https://www.usenix.org/conference/nsdi13/technical-sessions/presentation/lloyd) |
| P0 | *Conflict-free Replicated Data Types* | Shapiro et al., 2011 | CvRDT、CmRDT、可合并数据类型 | [HAL](https://inria.hal.science/inria-00555588/document) |
| P1 | *A Comprehensive Study of Convergent and Commutative Replicated Data Types* | Shapiro et al., 2011 | CRDT 分类、收敛性和代价 | [INRIA](https://inria.hal.science/inria-00609399/document) |
| P2 | *Eventual Consistency Today: Limitations, Extensions, and Beyond* | Bailis, Ghodsi, 2013 | 最终一致性的局限与扩展 | [ACM Queue](https://queue.acm.org/detail.cfm?id=2462076) |
| P2 | *Anna: A KVS for Any Scale* | Wu et al., 2018 | 无协调、无共享和自动扩缩 | [VLDB PDF](https://www.vldb.org/pvldb/vol12/p171-wu.pdf) |

## 6. 分布式存储与数据库系统

### 6.1 文件系统、KV 和列式存储

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *The Design and Implementation of a Log-Structured File System* | Rosenblum, Ousterhout, 1992 | 顺序写、日志结构和清理 | [DOI](https://doi.org/10.1145/146941.146943) |
| P0 | *The Log-Structured Merge-Tree* | O'Neil et al., 1996 | MemTable、SSTable、Compaction | [Acta Informatica](https://doi.org/10.1007/s002360050048) |
| P0 | *The Google File System* | Ghemawat, Gobioff, Leung, 2003 | Chunk、Master、租约、恢复 | [Google Research](https://research.google/pubs/the-google-file-system/) |
| P0 | *Bigtable: A Distributed Storage System for Structured Data* | Chang et al., 2006 | Tablet、SSTable、分层存储 | [Google Research](https://research.google/pubs/bigtable-a-distributed-storage-system-for-structured-data/) |
| P1 | *Dynamo* | DeCandia et al., 2007 | 高可用 KV 与最终一致性 | [SOSP PDF](https://www.allthingsdistributed.com/files/amazon-dynamo-sosp2007.pdf) |
| P1 | *FAWN: A Fast Array of Wimpy Nodes* | Andersen et al., 2009 | 低功耗节点、内存索引、Flash 存储 | [SOSP](https://doi.org/10.1145/1629575.1629597) |
| P1 | *Windows Azure Storage: A Highly Available Cloud Storage Service with Strong Consistency* | Calder et al., 2011 | 分区、流式存储和强一致性 | [SOSP](https://doi.org/10.1145/2043556.2043590) |

### 6.2 分布式事务数据库和 NewSQL

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P1 | *Megastore: Providing Scalable, Highly Available Storage for Interactive Services* | Baker et al., 2011 | Entity Group、Paxos 和跨地域事务 | [CIDR](https://research.google/pubs/megastore-providing-scalable-highly-available-storage-for-interactive-services/) |
| P0 | *Spanner: Google's Globally-Distributed Database* | Corbett et al., 2012 | TrueTime、外部一致性、2PC+Paxos | [Google Research](https://research.google/pubs/spanner-googles-globally-distributed-database/) |
| P1 | *F1: The Fault-Tolerant Distributed RDBMS for Google's Ad Business* | Shute et al., 2013 | 分布式 SQL、分层架构和在线 schema | [Google Research](https://research.google/pubs/f1-the-fault-tolerant-distributed-rdbms-for-googles-ad-business/) |
| P1 | *Percolator: Large-scale Incremental Processing Using Distributed Transactions and Notifications* | Peng, Dabek, 2010 | 基于 Bigtable 的事务和通知 | [OSDI](https://www.usenix.org/legacy/events/osdi10/tech/full_papers/Peng_new.pdf) |
| P1 | *MDCC: Multi-Data Center Consistency* | Kraska et al., 2013 | 广域网事务和无主协调 | [EuroSys](https://doi.org/10.1145/2465351.2465375) |
| P1 | *TAPIR: Building Consistent Transactions with Inconsistent Replication* | Zhang et al., 2015 | OCC、验证和复制解耦 | [SOSP](https://doi.org/10.1145/2815400.2815415) |
| P1 | *FaRM: Fast Remote Memory* | Dragojevic et al., 2014 | RDMA、分布式事务、无复制提交 | [NSDI](https://www.usenix.org/conference/nsdi14/technical-sessions/dragojevic) |
| P1 | *FoundationDB: A Distributed Unbundled Transactional Key Value Store* | Appleby et al., 2021 | 分层、OCC、事务 KV | [CIDR](https://www.foundationdb.org/files/fdb-paper.pdf) |
| P2 | *Calvin* | Thomson et al., 2012 | 确定性事务数据库 | [USENIX](https://www.usenix.org/conference/osdi12/calvin-fast-distributed-transactions-partitioned-database-systems) |

## 7. 分布式计算、消息和流处理

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *MapReduce: Simplified Data Processing on Large Clusters* | Dean, Ghemawat, 2004 | Map、Reduce、容错和数据局部性 | [Google Research](https://research.google/pubs/mapreduce-simplified-data-processing-on-large-clusters/) |
| P1 | *Dryad: Distributed Data-Parallel Programs from Sequential Building Blocks* | Isard et al., 2007 | 通用数据流 DAG | [EuroSys](https://doi.org/10.1145/1272998.1273005) |
| P0 | *Resilient Distributed Datasets: A Fault-Tolerant Abstraction for In-Memory Cluster Computing* | Zaharia et al., 2012 | RDD、血缘恢复、内存计算 | [NSDI](https://www.usenix.org/conference/nsdi12/technical-sessions/zaharia) |
| P1 | *Pregel: A System for Large-Scale Graph Processing* | Malewicz et al., 2010 | BSP、图计算和消息传递 | [Google Research](https://research.google/pubs/pregel-a-system-for-large-scale-graph-processing/) |
| P0 | *Dremel: Interactive Analysis of Web-Scale Datasets* | Melnik et al., 2010 | 列式存储、树形执行 | [Google Research](https://research.google/pubs/dremel-interactive-analysis-of-web-scale-datasets/) |
| P1 | *Kafka: A Distributed Messaging System for Log Processing* | Kreps et al., 2011 | 分区日志、顺序、消费者和吞吐 | [DOI](https://doi.org/10.1145/1966445.1966456) |
| P1 | *MillWheel: Fault-Tolerant Stream Processing at Internet Scale* | Akidau et al., 2013 | 持久状态、低水位、事件时间 | [Google Research](https://research.google/pubs/millwheel-fault-tolerant-stream-processing-at-internet-scale/) |
| P0 | *The Dataflow Model: A Practical Approach to Balancing Correctness, Latency, and Cost* | Akidau et al., 2015 | 事件时间、窗口、Watermark、触发器 | [VLDB](https://www.vldb.org/pvldb/vol8/p1792-Akidau.pdf) |
| P1 | *Naiad: A Timely Dataflow System* | Murray et al., 2013 | 及时数据流和迭代计算 | [SOSP](https://doi.org/10.1145/2517349.2522738) |
| P1 | *Apache Flink: Stream and Batch Processing in a Single Engine* | Carbone et al., 2015 | 有状态流处理统一模型 | [IEEE Data Engineering](https://www.microsoft.com/en-us/research/publication/apache-flink-stream-and-batch-processing-in-a-single-engine/) |
| P2 | *The Log: What Every Software Engineer Should Know About Real-time Data's Unifying Abstraction* | Jay Kreps, 2013 | 日志、事件和系统整合 | [LinkedIn Engineering](https://engineering.linkedin.com/content/engineering/en-us/blog/2013/the-log-what-every-software-engineer-should-know-about-real-time-datas-unifying-abstraction) |

## 8. 集群管理、微服务与可观测性

| 级别 | 原始论文 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *Mesos: A Platform for Fine-Grained Resource Sharing in the Data Center* | Hindman et al., 2011 | 两级调度、资源共享 | [NSDI](https://www.usenix.org/conference/nsdi11/mesos-platform-fine-grained-resource-sharing-data-center) |
| P0 | *Large-scale Cluster Management at Google with Borg* | Verma et al., 2015 | 集群调度、隔离、资源利用 | [EuroSys](https://doi.org/10.1145/2741948.2741964) |
| P1 | *Omega: Flexible, Scalable Schedulers for Large Compute Clusters* | Schwarzkopf et al., 2013 | 共享状态、乐观并发调度 | [EuroSys](https://doi.org/10.1145/2465351.2465386) |
| P0 | *Dapper, a Large-Scale Distributed Systems Tracing Infrastructure* | Sigelman et al., 2010 | Trace、Span、采样、因果链路 | [Google Research](https://research.google/pubs/dapper-a-large-scale-distributed-systems-tracing-infrastructure/) |
| P0 | *The Tail at Scale* | Dean, Barroso, 2013 | 长尾延迟、请求复制和过载 | [Communications of the ACM](https://cacm.acm.org/research/the-tail-at-scale/) |
| P1 | *Occupy the Cloud: Distributed Computing for the 99%* | Jonas et al., 2017 | Serverless 平台、弹性计算和系统研究议程 | [arXiv](https://arxiv.org/abs/1702.04024) |

## 9. 验证、测试与可靠性

| 级别 | 资料 | 作者/年份 | 重点 | 官方入口 |
| --- | --- | --- | --- | --- |
| P0 | *The Temporal Logic of Actions* | Leslie Lamport, 1994 | TLA、动作、状态与时序性质 | [作者 PDF](https://lamport.azurewebsites.net/pubs/lamport-actions.pdf) |
| P0 | *Specifying Systems* | Leslie Lamport, 2002 | TLA+ 规格、精化与模型检查 | [作者免费版](https://lamport.azurewebsites.net/tla/book.html) |
| P1 | *Testing and Verifying Concurrent Objects* | Wing, Gong, 1993 | 线性一致性历史的测试与验证 | [DOI](https://doi.org/10.1016/0167-6423(93)90010-P) |
| P1 | *An Analysis of Network-Partitioning Failures in Cloud Systems* | Yuan et al., 2018 | 真实系统网络分区故障分析 | [USENIX OSDI](https://www.usenix.org/conference/osdi18/presentation/yuan) |
| P1 | *Aphyr's Jepsen Analyses* | Kyle Kingsbury | 分布式数据库故障实验和一致性报告 | [Jepsen Analysis](https://jepsen.io/analyses) |
| P2 | *Lineage-Driven Fault Injection* | Gunawi et al., 2011 | 大规模故障注入 | [SOSP](https://doi.org/10.1145/2043556.2043583) |

## 10. 课程和教材补充

### 10.1 课程

- [MIT 6.5840 Distributed Systems](https://pdos.csail.mit.edu/6.824/)：论文、讲义和 Go 实验的主线。
- [MIT 6.033 Computer Systems Engineering](https://web.mit.edu/6.033/www/)：系统设计、接口和故障思维。
- [CMU 15-440 Distributed Systems](https://www.cs.cmu.edu/~dga/15-440/F12/lectures.shtml)：分布式系统工程课程。
- [Berkeley CS 262A Advanced Topics in Computer Systems](https://cs.berkeley.edu/education/courses)：系统论文和研究讨论。
- [CMU 15-445/645 Database Systems](https://15445.courses.cs.cmu.edu/)：存储引擎、并发控制和恢复。
- [Stanford CS244b Distributed Systems](https://cs.stanford.edu/people/eroberts/courses/soco/projects/2003-04/distributed-systems/)：分布式基础和系统案例。

### 10.2 书籍

- Nancy Lynch, *Distributed Algorithms*：形式化模型和算法证明。
- George Coulouris et al., *Distributed Systems: Concepts and Design*：全面教材型地图。
- Martin Kleppmann, *Designing Data-Intensive Applications*：工程实践、数据系统和一致性。
- Jim Gray, Andreas Reuter, *Transaction Processing: Concepts and Techniques*：事务、恢复和并发控制。
- Tanenbaum, van Steen, *Distributed Systems*：概念、算法和系统案例。
- Martin Fowler 等，*Patterns of Enterprise Application Architecture*：事务脚本、消息和企业集成模式。

## 11. 阅读分组与验收

| 主题 | 最少精读 | 最少实验 |
| --- | ---: | --- |
| 理论与一致性 | 6 篇 | Lamport/Vector Clock、Snapshot、历史检查 |
| 共识与复制 | 8 篇 | 6.5840 Raft、线性一致 KV |
| 事务 | 10 篇 | 2PC、Saga+Outbox、MVCC/隔离测试 |
| 存储 | 10 篇 | LSM、分片、副本、恢复 |
| 计算与消息 | 8 篇 | MapReduce、Kafka、Flink 状态恢复 |
| 云原生与可靠性 | 8 篇 | Controller、Trace、故障注入 |

总计建议精读至少 40 篇，泛读至少 80 篇。精读必须留下“不变量、失败场景和代价”，否则只算摘要阅读。

## 12. 资料质量说明

- 论文标题优先链接 DOI、作者主页、会议官网或机构研究页。
- 原始论文和教程性文章分开，不用博客替代论文。
- 课程实验链接可能随学期变化，执行前以 MIT 课程主页当前版本为准。
- 需要登录、访问不稳定或可能失效的页面保留标题和站点入口，不在代码中依赖它们。
