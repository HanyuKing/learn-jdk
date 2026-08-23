# 分布式系统课程、业界分享与源码资料

> 本文只保存合法公开入口，不复制受版权保护的论文、视频或 PPT。建议优先使用官方课程、作者主页、会议页面和项目文档；中文文章用于建立直觉，遇到关键结论回到原始论文。

## 1. 主线资料（建议按顺序使用）

| 顺序 | 资源 | 作用 |
| ---: | --- | --- |
| 1 | [MIT 6.5840 / 6.824](https://pdos.csail.mit.edu/6.824/) | Go 实验、论文阅读、分布式系统主实践线 |
| 2 | [MIT 6.033 Computer Systems Engineering](https://web.mit.edu/6.033/www/) | 系统边界、接口、威胁和故障分析 |
| 3 | [CMU 15-445/645 Database Systems](https://15445.courses.cs.cmu.edu/) | 存储引擎、并发控制、恢复和查询执行 |
| 4 | [MIT 6.824 课程讲义/阅读列表](https://pdos.csail.mit.edu/6.824/schedule.html) | 论文与实验的先修关系 |
| 5 | [Martin Kleppmann: DDIA materials](https://dataintensive.net/) | 工程视角的数据系统地图 |
| 6 | [Jepsen analyses](https://jepsen.io/analyses) | 真实系统一致性和故障测试案例 |
| 7 | [USENIX proceedings](https://www.usenix.org/publications/proceedings) | OSDI、NSDI、FAST、ATC 等系统论文官方入口 |
| 8 | [ACM Digital Library](https://dl.acm.org/) | SOSP、SIGMOD、VLDB、EuroSys 等论文元数据和 DOI |

## 2. 公开课程与讲义

### 分布式系统

- [MIT 6.5840 Distributed Systems](https://pdos.csail.mit.edu/6.824/)：课程主页、实验框架、论文、讲义和历年材料。
- [CMU 15-440 Distributed Systems](https://www.cs.cmu.edu/~dga/15-440/F12/lectures.shtml)：RPC、故障、共识和分布式存储讲义。
- [Berkeley CS 262A](https://cs.berkeley.edu/education/courses)：高级系统论文讨论，适合完成基础后追踪研究主题。
- [Stanford CS244b](https://cs.stanford.edu/people/eroberts/courses/soco/projects/2003-04/distributed-systems/)：分布式概念与案例的公开页面。
- [Cornell CS 6410 Distributed Systems](https://www.cs.cornell.edu/courses/cs6410/2013fa/)：分布式算法、共识和验证方向。
- [EPFL Distributed Systems](https://dslab.epfl.ch/education/courses/)：复制、容错、系统研究材料。

### 数据库、存储与流处理

- [CMU 15-445/645 Database Systems](https://15445.courses.cs.cmu.edu/)：课程视频、讲义、项目和作业。
- [CMU 15-721 Advanced Database Systems](https://15721.courses.cs.cmu.edu/)：事务、分布式查询、现代数据库论文。
- [Stanford CS245 Database System Principles](https://web.stanford.edu/class/cs245/)：数据库理论和系统实现。
- [Berkeley CS186 Database Systems](https://cs186berkeley.net/)：存储、索引、事务、恢复和项目。
- [Apache Flink training](https://nightlies.apache.org/flink/flink-docs-stable/docs/learn-flink/overview/)：官方流处理教程。
- [Apache Kafka documentation](https://kafka.apache.org/documentation/)：日志、分区、复制、事务和 Consumer Group。

### 云原生、可靠性与可观测性

- [Kubernetes 官方架构文档](https://kubernetes.io/docs/concepts/architecture/)：API Server、etcd、Scheduler、Controller 和声明式协调。
- [Kubernetes 官方任务与教程](https://kubernetes.io/docs/tutorials/)：部署、控制器、网络和故障排查。
- [OpenTelemetry 文档](https://opentelemetry.io/docs/)：Trace、Metrics、Logs 和 Collector。
- [Google SRE Books](https://sre.google/books/)：SRE、可用性目标、容量、发布和应急响应。
- [The Site Reliability Workbook](https://sre.google/workbook/table-of-contents/)：可执行的 SLO、告警和演练方法。
- [CNCF Landscape](https://landscape.cncf.io/)：云原生项目地图，按问题检索，不建议从头通读。

## 3. 官方技术报告、论文和演讲入口

### Google

- [Google Research Publications](https://research.google/pubs/)：GFS、MapReduce、Bigtable、Spanner、Dapper、MillWheel、Borg 等原始论文。
- [Google Cloud Architecture Center](https://cloud.google.com/architecture)：分布式系统、数据、可靠性和多地域架构指南。
- [Google SRE resources](https://sre.google/resources/)：SRE 书籍、演讲和案例。
- [Google Tech Talks archive](https://www.youtube.com/@GoogleTechTalks)：检索论文作者和系统名称，优先看原作者演讲。

### Amazon / AWS

- [All Things Distributed](https://www.allthingsdistributed.com/)：Werner Vogels 与 AWS 团队的分布式系统文章和演讲。
- [AWS Architecture Center](https://aws.amazon.com/architecture/)：参考架构、可靠性和多区域设计。
- [AWS re:Invent videos](https://www.youtube.com/@amazonwebservices)：检索 Dynamo、Aurora、EBS、S3、Kinesis、故障演练。
- [Amazon Builders' Library](https://aws.amazon.com/builders-library/)：超时、重试、幂等、限流、分区和运营经验。

### Microsoft / Azure

- [Microsoft Research Publications](https://www.microsoft.com/en-us/research/publications/)：Azure Storage、Cosmos DB、FaRM、事务和系统论文。
- [Azure Architecture Center](https://learn.microsoft.com/en-us/azure/architecture/)：云架构模式、可靠性和数据设计。
- [Azure Cosmos DB consistency](https://learn.microsoft.com/en-us/azure/cosmos-db/consistency-overview)：一致性模型和实际 API 语义。

### Meta / Facebook

- [Meta Engineering](https://engineering.fb.com/)：TAO、Cassandra、RocksDB、Haystack、服务基础设施和存储论文。
- [RocksDB Wiki](https://github.com/facebook/rocksdb/wiki)：LSM、Compaction、WAL、性能和运维资料。

### Netflix / Uber / LinkedIn

- [Netflix Technology Blog](https://netflixtechblog.com/)：服务治理、缓存、混沌工程、流处理和可观测性。
- [Uber Engineering](https://www.uber.com/en-US/blog/engineering/)：分布式数据库、调度、消息、服务化和多地域系统。
- [LinkedIn Engineering](https://engineering.linkedin.com/blog)：Kafka、Samza、流处理、数据平台和可观测性。

### 中国业界官方资料

- [阿里云开发者社区](https://developer.aliyun.com/)、[阿里中间件团队](https://www.alibabacloud.com/blog)：RocketMQ、Seata、Sentinel、PolarDB、OceanBase 相关资料；检索时优先官方团队和论文。
- [OceanBase 官方文档](https://www.oceanbase.com/docs)：分布式 SQL、分区、事务、租户和运维。
- [PingCAP TiDB 文档](https://docs.pingcap.com/tidb/stable/)：TiDB、TiKV、Placement Driver 和事务实现。
- [Apache RocketMQ 文档](https://rocketmq.apache.org/docs/)：消息顺序、事务消息、重试和消费语义。
- [Apache Dubbo 文档](https://dubbo.apache.org/zh-cn/overview/)：RPC、服务治理和扩展机制。
- [腾讯云架构中心](https://cloud.tencent.com/architecture)：云架构、消息、数据库和高可用实践。
- [字节跳动技术团队](https://juejin.cn/user/712139234247733)：只把官方团队发布作为辅助材料，关键结论回到原论文或项目文档。

## 4. 分布式系统项目与源码

### 共识、协调和复制

- [etcd](https://github.com/etcd-io/etcd)：Raft、MVCC、Watch、Lease 和线性一致读。
- [Hashicorp Raft](https://github.com/hashicorp/raft)：可嵌入 Raft 库，适合阅读工程 API 和 Snapshot。
- [Apache ZooKeeper](https://github.com/apache/zookeeper)：Zab、协调 API、会话和 Watch。
- [CockroachDB Pebble](https://github.com/cockroachdb/pebble)：Go LSM 存储引擎。
- [TLA+ examples](https://github.com/tlaplus/Examples)：协议规格与模型检查样例。

### 数据库与存储

- [RocksDB](https://github.com/facebook/rocksdb)：LSM、Compaction、WAL 和性能调优。
- [LevelDB](https://github.com/google/leveldb)：较小的 LSM 实现，适合建立调用链。
- [TiKV](https://github.com/tikv/tikv)：Raft Store、MVCC、Region、分裂和事务。
- [TiDB](https://github.com/pingcap/tidb)：分布式 SQL、事务层和调度。
- [CockroachDB](https://github.com/cockroachdb/cockroach)：Range、Raft、MVCC、Serializable 和多地域。
- [FoundationDB](https://github.com/apple/foundationdb)：事务 KV、层和控制面/数据面解耦。
- [OceanBase](https://github.com/oceanbase/oceanbase)：分布式关系数据库和多租户。

### 消息和流处理

- [Apache Kafka](https://github.com/apache/kafka)：分区日志、复制、事务和 Consumer Group。
- [Apache RocketMQ](https://github.com/apache/rocketmq)：事务消息、顺序消息、重试和存储。
- [Apache Pulsar](https://github.com/apache/pulsar)：Broker/BookKeeper 分离和多租户。
- [Apache Flink](https://github.com/apache/flink)：状态后端、Checkpoint、Watermark 和运行时。
- [Apache Spark](https://github.com/apache/spark)：RDD、调度、Shuffle 和容错。

### 云原生和可观测性

- [Kubernetes](https://github.com/kubernetes/kubernetes)：API、Controller、Scheduler、Informer 和 etcd 使用。
- [containerd](https://github.com/containerd/containerd)：容器运行时和镜像分发。
- [Envoy](https://github.com/envoyproxy/envoy)：代理、负载均衡、重试、熔断和可观测性。
- [OpenTelemetry Collector](https://github.com/open-telemetry/opentelemetry-collector)：遥测管线和扩展机制。
- [Prometheus](https://github.com/prometheus/prometheus)：时序数据模型、抓取和告警规则。

## 5. 会议与视频检索

### 会议入口

- [SOSP](https://www.sigops.org/sosp/)
- [OSDI](https://www.usenix.org/conferences/byname/174)
- [NSDI](https://www.usenix.org/conferences/byname/17)
- [FAST](https://www.usenix.org/conferences/byname/fast)
- [USENIX ATC](https://www.usenix.org/conferences/byname/atc)
- [SIGMOD](https://sigmod.org/)
- [VLDB Proceedings](https://www.vldb.org/pvldb/)
- [EuroSys](https://2025.eurosys.org/)
- [HotOS](https://www.sigops.org/hotos/)

### 推荐检索方式

在会议官网或官方 YouTube 频道按以下关键词检索，而不是泛搜“分布式系统 PPT”：

- `Raft`, `Paxos`, `PBFT`, `HotStuff`, `state machine replication`
- `Spanner`, `Dynamo`, `Bigtable`, `FoundationDB`, `TiKV`, `CockroachDB`
- `Sagas`, `atomic commit`, `MVCC`, `serializability`, `outbox`
- `MapReduce`, `RDD`, `Dataflow`, `Flink`, `Kafka`, `stream processing`
- `Borg`, `Kubernetes`, `Dapper`, `tail latency`, `chaos engineering`

### 公开演讲和 PPT 的使用原则

1. 先读论文，再看作者演讲；演讲适合建立系统全貌，不替代细节和证明。
2. 以会议官网、作者主页、公司官方博客或官方频道为优先入口。
3. PPT 只记录标题、作者、会议、年份和链接；不要把脱离上下文的截图当作结论。
4. 对公司内部分享或转载材料，明确标记“非原始来源”，并回链对应论文。

## 6. 中文辅助资料

中文资料的价值主要是术语翻译、代码导读和经验总结。建议按下面顺序使用：

- [极客时间：分布式系统相关专栏](https://time.geekbang.org/)：适合建立工程直觉，具体课程需以当前目录为准。
- [InfoQ 中文](https://www.infoq.cn/)：会议报道、架构实践和技术访谈；对结论回查原文。
- [阿里云中间件社区](https://middleware.blog.csdn.net/)：RocketMQ、Seata 等官方团队文章入口。
- [PingCAP 博客](https://www.pingcap.com/blog/)：TiDB、分布式数据库和事务实践。
- [美团技术团队](https://tech.meituan.com/)：高并发、消息、存储和服务治理案例；将其作为案例而非定理来源。
- [高可用架构](https://dbaplus.cn/)：中文会议资料和案例，注意核对作者与原始出处。

不要把“CAP 三选二”“BASE 就是最终一致”“MQ 保证 Exactly-once”这类口号直接写进设计文档；遇到类似表述先回到论文定义和具体系统边界。

## 7. 推荐的资料检索卡片

```markdown
# 资源检索卡片

主题：
资源类型：论文 / 课程 / PPT / 演讲 / 源码 / 实验
原始来源：
作者与年份：
解决的问题：
系统模型与故障模型：
关键保证：
可以复现的实验：
与已有主线的关系：
链接状态：可访问 / 登录受限 / 失效待替换
```

## 8. 维护和链接核验

- 每季度检查一次外链；会议页面和学期课程页最容易变更。
- 论文链接失效时，优先替换为 DOI、作者主页或会议存档，不直接换成不明转载。
- 项目链接应指向官方仓库的稳定分支或 release 文档。
- 对课程 Lab，记录使用的年份和 commit，保证测试和接口可复现。
- 新增资料时至少写清“为什么值得读”和“放在学习路线哪一阶段”。
