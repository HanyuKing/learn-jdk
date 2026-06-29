# CN to EN Translation Record

记录项目国际化翻译前后的文案。每个项目一个章节，表格固定四列：中文、English、业务场景、产品建议。

填写约定：
- `中文`：原始中文文案。
- `English`：最终英文翻译。
- `业务场景`：文案所在业务场景或用途。
- `产品建议`：留空，等待产品决定最终英文。

## hunter-service

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 审核失败 | Audit failed | 手机号风险审核接口错误提示 |  |
| 手机号不能为空 | Phone number cannot be empty | 手机号风险审核/检测参数校验 |  |
| 审核结果不能为空 | Audit result cannot be empty | 手机号风险审核参数校验 |  |
| 审核人不能为空 | Auditor cannot be empty | 手机号风险审核参数校验 |  |
| 用户ID和手机号映射列表不能为空 | User ID and phone number mapping list cannot be empty | 按用户 ID 批量检测参数校验 |  |
| 用户ID不能为空 | User ID cannot be empty | 按用户 ID 批量检测参数校验 |  |
| 手机号列表不能为空 | Phone number list cannot be empty | 手机号批量检测参数校验 |  |
| 待审核 | Pending Review | 风险审核结果枚举 |  |
| 作弊 | Cheating | 风险审核结果枚举 |  |
| 未作弊 | Not Cheating | 风险审核结果枚举 |  |
| 拦截 | Block | 手机号风险判定枚举 |  |
| 通过 | Pass | 手机号风险判定枚举 |  |
| 正常 | Normal | 火山风险分值枚举 |  |
| 中风险 | Medium Risk | 火山风险分值枚举 |  |
| 高风险 | High Risk | 火山风险分值枚举 |  |
| 极高风险 | Critical Risk | 火山风险分值枚举 |  |
| 参数名不能为空 | Parameter name cannot be empty | URL 构造工具运行时异常 |  |
| 查询成功 | Query succeeded | 火山风险接口返回码描述 |  |
| 参数非法 | Invalid parameter | 火山风险接口返回码描述 |  |
| 鉴权失败 | Authentication failed | 火山风险接口返回码描述 |  |
| 权限不足 | Insufficient permission | 火山风险接口返回码描述 |  |
| QPS超过限制 | QPS limit exceeded | 火山风险接口返回码描述 |  |
| 账户余额不足 | Insufficient account balance | 火山风险接口返回码描述 |  |
| 系统内部错误 | Internal system error | 火山风险接口返回码描述 |  |
| 未知错误 | Unknown error | 火山风险接口返回码描述 |  |
| 自定义白名单 | Custom Whitelist | 火山风险标签枚举 |  |
| 自定义黑名单 | Custom Blacklist | 火山风险标签枚举 |  |
| 账户黑名单 | Account Blacklist | 火山风险标签枚举 |  |
| 账户行为风险 | Account Behavior Risk | 火山风险标签枚举 |  |
| 手机号风险 | Phone Number Risk | 火山风险标签枚举 |  |
| 邮箱域名异常 | Email Domain Anomaly | 火山风险标签枚举 |  |
| 账号环境异常 | Account Environment Anomaly | 火山风险标签枚举 |  |
| 批量注册账号风险 | Batch Registration Account Risk | 火山风险标签枚举 |  |
| 用户小号风险 | Alt Account Risk | 火山风险标签枚举 |  |
| 杀猪盘账号 | Romance Scam Account | 火山风险标签枚举 |  |
| 广告导流账号 | Ad Traffic Diversion Account | 火山风险标签枚举 |  |
| 账号异常聚集 | Account Anomaly Cluster | 火山风险标签枚举 |  |
| IP环境异常 | IP Environment Anomaly | 火山风险标签枚举 |  |
| IP黑名单 | IP Blacklist | 火山风险标签枚举 |  |
| IP行为异常 | IP Behavior Anomaly | 火山风险标签枚举 |  |
| IP异常聚集 | IP Anomaly Cluster | 火山风险标签枚举 |  |
| 设备黑名单 | Device Blacklist | 火山风险标签枚举 |  |
| 设备聚集异常 | Device Cluster Anomaly | 火山风险标签枚举 |  |
| 设备行为异常 | Device Behavior Anomaly | 火山风险标签枚举 |  |
| 设备环境异常 | Device Environment Anomaly | 火山风险标签枚举 |  |
| 设备脚本作弊 | Device Script Cheating | 火山风险标签枚举 |  |
| 设备参数篡改 | Device Parameter Tampering | 火山风险标签枚举 |  |
| 设备抹机异常 | Device Wipe Anomaly | 火山风险标签枚举 |  |
| 设备模拟点击 | Device Simulated Click | 火山风险标签枚举 |  |
| 设备越狱破解 | Device Jailbreak or Root | 火山风险标签枚举 |  |
| 设备网络异常 | Device Network Anomaly | 火山风险标签枚举 |  |
| 设备定制ROM | Device Custom ROM | 火山风险标签枚举 |  |
| 设备多开作弊 | Device Multi-Instance Cheating | 火山风险标签枚举 |  |
| 设备硬件异常 | Device Hardware Anomaly | 火山风险标签枚举 |  |
| 设备调试工具 | Device Debugging Tool | 火山风险标签枚举 |  |
| 伪造虚假设备 | Fake Device | 火山风险标签枚举 |  |
| 接入参数异常 | Access Parameter Anomaly | 火山风险标签枚举 |  |
| 设备指纹异常 | Device Fingerprint Anomaly | 火山风险标签枚举 |  |
| 手机号黑名单 | Phone Number Blacklist | 火山风险标签枚举 |  |
| 手机号状态异常 | Phone Number Status Anomaly | 火山风险标签枚举 |  |
| 手机号行为异常 | Phone Number Behavior Anomaly | 火山风险标签枚举 |  |
| 手机号异常聚集 | Phone Number Anomaly Cluster | 火山风险标签枚举 |  |
| 杀猪盘手机号 | Romance Scam Phone Number | 火山风险标签枚举 |  |
| 广告导流手机号 | Ad Traffic Diversion Phone Number | 火山风险标签枚举 |  |
| 营销活动薅羊毛 | Marketing Campaign Coupon Abuse | 火山风险标签枚举 |  |
| 营销活动真人众包 | Marketing Campaign Human Crowdsourcing | 火山风险标签枚举 |  |
| 营销活动作弊风险 | Marketing Campaign Cheating Risk | 火山风险标签枚举 |  |
| 内容欺诈风险 | Content Fraud Risk | 火山风险标签枚举 |  |
| 内容导流风险 | Content Traffic Diversion Risk | 火山风险标签枚举 |  |
| 虚假刷量风险 | Fake Engagement Risk | 火山风险标签枚举 |  |
| 情感欺诈风险 | Emotional Fraud Risk | 火山风险标签枚举 |  |
| 致富欺诈风险 | Get-Rich Fraud Risk | 火山风险标签枚举 |  |
| 引流欺诈风险 | Traffic Diversion Fraud Risk | 火山风险标签枚举 |  |
| 仿冒欺诈风险 | Impersonation Fraud Risk | 火山风险标签枚举 |  |
| 欺诈黑名单 | Fraud Blacklist | 火山风险标签枚举 |  |
| 疑似爬虫风险 | Suspected Crawler Risk | 火山风险标签枚举 |  |
| 充值欺诈风险 | Recharge Fraud Risk | 火山风险标签枚举 |  |
| 异常退款风险 | Abnormal Refund Risk | 火山风险标签枚举 |  |
| 转账欺诈风险 | Transfer Fraud Risk | 火山风险标签枚举 |  |
| 转账安全风险 | Transfer Security Risk | 火山风险标签枚举 |  |
| 异常渠道激活 | Abnormal Channel Activation | 火山风险标签枚举 |  |
| 异常广告点击 | Abnormal Ad Click | 火山风险标签枚举 |  |
| 疑似黄牛党 | Suspected Ticket Scalper | 火山风险标签枚举 |  |
| 疑似录播 | Suspected Loop Broadcast | 火山风险标签枚举 |  |
| 未知标签 | Unknown Tag | 火山风险标签枚举 |  |
| 命中标签数量达到阈值 | Matched tag count reached the threshold | 手机号风险策略说明 |  |
| 命中策略标签 | Matched strategy tags | 手机号风险策略说明 |  |
| 命中条件 | Matched condition | 手机号风险策略说明 |  |
| 命中火山标签 | Matched Volcengine risk tags | 手机号风险策略说明 |  |
| 本次仅命中火山标签 | Only matched Volcengine risk tags this time | 手机号风险策略说明 |  |
| 未满足任一判定条件 | No decision condition was met | 手机号风险策略说明 |  |
| 命中火山标签汇总 | Matched Volcengine risk tag summary | 手机号风险策略说明 |  |
| 策略风险判定条件 | Strategy risk decision conditions | 手机号风险策略说明 |  |
| 同时命中标签 | Match tags at the same time | 手机号风险策略说明 |  |
| 暂无组合配置 | No combination configured | 手机号风险策略说明 |  |
| 无 | None | 手机号风险策略空值展示 |  |
| 敏感词校验 | Sensitive Word Check | 内容安全告警标题 |  |
| 安全校验失败 | Security Check Failed | 内容安全告警标题 |  |
| 敏感词校验失败 | Sensitive Word Check Failed | 内容安全告警标题 |  |
| language,参数错误 | language parameter error | 内容安全告警正文 |  |
| edu language,参数错误 | edu language parameter error | 内容安全告警正文 |  |
| 火山接口报错 | Volcengine API error | 内容安全告警正文 |  |
| 不支持该类型 | Unsupported type | 内容安全异常和告警正文 |  |
| 检查超时 | Check timed out | 内容安全/原创性检测结果消息 |  |
| 检查失败 | Check failed | 内容安全/原创性检测结果消息 |  |
| 商品主题和商品描述生成 Prompt | Product title and product description generation prompt | AI 商品信息生成 prompt |  |
| 帖子评论互动度检测 Prompt | Post comment interaction authenticity detection prompt | Nacos 互动度检测 prompt 默认值 |  |
| 图片主体背景类型分析 Prompt | Main image object background type analysis prompt | Nacos 图片背景检测 prompt 默认值 |  |
| 知识产权审核AI Prompt | Intellectual property review AI prompt | 内容安全 IP 检测 prompt |  |
| 造好物社区原创性审核 Prompt | Zaohaowu community originality review prompt | 原创性 VLM 检测 prompt |  |
| 源图片或候选图片为空 | Source images or candidate images are empty | 原创性 VLM 检测结果原因 |  |
| 源帖 | Source Post | 原创性 VLM 用户 prompt 分隔符 |  |
| 候选帖 | Candidate Post | 原创性 VLM 用户 prompt 分隔符 |  |
| 源图 | Source Image | 原创性 VLM 图片标签 |  |
| 候选图 | Candidate Image | 原创性 VLM 图片标签 |  |
| 第 N 视图 | View N | 原创性 VLM 图片视图标签 |  |
| 直接 prompt | Direct prompt | 原创性 VLM prompt 标签 |  |
| 祖先 prompt | Ancestor prompts | 原创性 VLM prompt 标签 |  |

## jjewelry-aigc-agent

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 用户要求基于上一轮相同输入重新生成一个新版本，请以”重试生成结果如下～”开头，然后直接调用原始的tool call，你可以微调传入prompt。 | The user requested a regenerated version based on the same input from the previous round. Start with "Retry generation result below~", then directly call the original tool call. You may slightly adjust the prompt passed in. | Agent 重试生成内部 prompt。 |  |
| 原始需求：%s | Original request: %s | Agent 重试 prompt 原始需求标签。 |  |
| （无文字） | (no text) | 内容审核/标题来源文本兜底。 |  |
| 用户： | User: | 内容审核/标题来源对话角色标签。 |  |
| 助手： | Assistant: | 内容审核/标题来源对话角色标签。 |  |
| AIGC Agent 大模型调用失败 | AIGC Agent LLM Call Failed | Reply/Title/Suggestion Agent 告警标题。 |  |
| 用户本轮上传了 N 张参考图，请生成简短会话标题。 | The user uploaded N reference images in this round. Generate a short conversation title. | 会话标题生成 fallback prompt。 |  |
| 补充信息：用户本轮上传了以下参考图（用户视角这些图片可能从左到右排列）。 | Additional information: the user uploaded the following reference images in this round (from the user's perspective, these images may be arranged from left to right). | Reply 阶段 user message 参考图说明。 |  |
| 当你需要调用依赖参考图的工具时，请优先使用这些 URL 作为 sourceImage 或 sourceImages 参数。 | When you need to call tools that depend on reference images, prioritize these URLs as the sourceImage or sourceImages parameter. | Reply 阶段 tool 参数提示。 |  |
| 如果图片包含 uniqueImageId，说明该图片是之前生成的，请将其 uniqueImageId 填入工具的 parentGenerateId 参数。 | If an image contains uniqueImageId, it means the image was generated previously. Put that uniqueImageId into the tool's parentGenerateId parameter. | Reply 阶段生成血缘参数提示。 |  |
| [限制]：本轮指定使用%s作为算法模型 (algoTopicId)。 | [Constraint]: use %s as the algorithm model (algoTopicId) for this round. | Reply 阶段算法模型限制提示。 |  |
| (空) | (empty) | Prompt 占位符空 key fallback。 |  |
| 系统本轮回复时发生异常，未正常完成。 | An exception occurred while the system was replying in this round, so the reply did not complete normally. | 历史对话注入中的失败回复占位。 |  |
| 本轮回复已被取消。 | The reply in this round has been canceled. | 历史对话注入中的取消回复占位。 |  |
| 系统上一轮回复未完成即中断。 | The system's previous reply was interrupted before completion. | 历史对话注入中的流式中断占位。 |  |
| [工具调用: %s, status=%s] | [Tool call: %s, status=%s] | 历史对话注入中的工具调用摘要。 |  |
| [工具请求: %s] | [Tool request: %s] | 历史对话注入中的工具请求摘要。 |  |
| [工具结果图片: %s] | [Tool result images: %s] | 历史对话注入中的工具结果图片摘要。 |  |
| [工具错误信息 errorInfo: %s] | [Tool error info errorInfo: %s] | 历史对话注入中的工具错误摘要。 |  |
| [用户上传图片] | [User uploaded images] | 历史对话注入中的用户图片摘要。 |  |
| submit 返回 null | submit returned null | 生成工具告警详情。 |  |
| queryJobProgress 返回 null | queryJobProgress returned null | 生成工具轮询告警详情。 |  |
| AIGC Agent Generator 调用失败 | AIGC Agent Generator Call Failed | 生成工具告警标题。 |  |
| 【告警类型】 | [Alert Type] | 飞书告警字段标签。 |  |
| 【当前环境】 | [Current Environment] | 飞书告警字段标签。 |  |
| 【当前服务】 | [Current Service] | 飞书告警字段标签。 |  |
| 【当前服务身份】 | [Current Service Identity] | 飞书告警字段标签。 |  |
| 【异常信息】 | [Exception Details] | 飞书告警字段标签。 |  |
| 原图片地址 | Source image URL | 工具 JSON schema 参数说明。 |  |
| 生图的提示词 | Prompt for image generation | 工具 JSON schema 参数说明。 |  |
| 生视频的提示词 | Prompt for video generation | 工具 JSON schema 参数说明。 |  |
| 生图使用的算法模型，允许为空字符串 | Algorithm model used for image generation; an empty string is allowed | 工具 JSON schema 参数说明。 |  |
| 生成图片数量，默认4 | Number of images to generate, default 4 | 工具 JSON schema 参数说明。 |  |
| 生成图片数量，默认1 | Number of images to generate, default 1 | 工具 JSON schema 参数说明。 |  |
| 生成图片宽度 | Generated image width | 工具 JSON schema 参数说明。 |  |
| 生成图片高度 | Generated image height | 工具 JSON schema 参数说明。 |  |
| 如果用户使用的图片包含 uniqueImageId，则将该 uniqueImageId 填入此字段，用于建立生成血缘关系 | If the image used by the user contains uniqueImageId, put that uniqueImageId in this field to establish generation lineage | 工具 JSON schema 参数说明。 |  |
| 模型风格 | Model style | 工具 JSON schema 参数说明。 |  |
| 根据图片生成图片 | Generate images from an image | 工具描述。 |  |
| 生成视频数量，默认1 | Number of videos to generate, default 1 | 工具 JSON schema 参数说明。 |  |
| 生成视频的时长 | Generated video duration | 工具 JSON schema 参数说明。 |  |
| 根据图片生成视频，如果algoTopicId为空表示调用通用图生视频模型，如果指定了algoTopicId表示调用特定品类/模型的 图生视频模型 | Generate videos from an image. If algoTopicId is empty, call the generic image-to-video model. If algoTopicId is specified, call the image-to-video model for the specified category/model. | 图生视频工具描述。 |  |
| 原图片地址（数组） | Source image URLs | Kontext 工具 sourceImages 参数说明。 |  |
| 编辑图片 | Edit image | Kontext 工具描述。 |  |
| 根据文本生成图片 | Generate images from text | 文生图工具描述。 |  |
| 生成数量，默认1 | Number of outputs to generate, default 1 | 高清工具 batchSize 参数说明。 |  |
| 图片高清 | Upscale image | 高清工具描述。 |  |
| prompt.yml 主回复 Prompt | Reply prompt | Agent 主回复长 prompt 配置。 |  |
| suggestion-prompt | Suggestion prompt | Agent 推荐指令 prompt 配置。 |  |
| model-recommendation-prompt | Model recommendation prompt | Agent 模型推荐 prompt 配置。 |  |
| meta-prompt | Meta prompt | Agent 标题生成 prompt 配置。 |  |
| 手办 | figurine | Agent prompt 结构化产品类型。 |  |
| 棉花娃娃 | cotton doll | Agent prompt 结构化产品类型。 |  |
| 包挂 | bag charm | Agent prompt 结构化产品类型。 |  |
| 怪诞风格 | weird style | Agent prompt 结构化风格。 |  |
| 小怪兽风格 | little monster style | Agent prompt 结构化风格。 |  |
| 搞怪风格 | funny style | Agent prompt 结构化风格。 |  |
| 盲盒风格 | blind box style | Agent prompt 结构化风格。 |  |
| Q版风格 | chibi style | Agent prompt 结构化风格。 |  |
| 马元素盲盒风格 | Year of the Horse blind box style | Agent prompt 结构化风格。 |  |
| 半哑光树脂 | semi-matte resin | Agent prompt 结构化材质。 |  |
| 亮光树脂 | glossy resin | Agent prompt 结构化材质。 |  |
| 毛绒 | plush | Agent prompt 结构化材质。 |  |
| 文生图 | text-to-image | Agent prompt 模型 mode。 |  |
| 图生图 | image-to-image | Agent prompt 模型 mode。 |  |
| 图生视频 | image-to-video | Agent prompt 模型 mode。 |  |

## jjewelry-generator

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 【告警类型】 | [Alert Type] | 飞书告警字段标签。 |  |
| 【当前环境】 | [Current Environment] | 飞书告警字段标签。 |  |
| 【当前服务】 | [Current Service] | 飞书告警字段标签。 |  |
| 【当前服务身份】 | [Current Service Identity] | 飞书告警字段标签。 |  |
| 【异常信息】 | [Exception Details] | 飞书告警字段标签。 |  |
| Model对象 | Model object | Swagger 模型对象说明。 |  |
| 模型表 | Model table | Swagger 表说明。 |  |
| Topic对象 | Topic object | Swagger 话题对象说明。 |  |
| 话题表 | Topic table | Swagger 表说明。 |  |
| TopicContent对象 | TopicContent object | Swagger 话题内容对象说明。 |  |
| 话题内容表 | Topic content table | Swagger 表说明。 |  |
| TopicDraft对象 | TopicDraft object | Swagger 活动草稿对象说明。 |  |
| 活动表（内测） | Campaign table (beta) | Swagger 表说明。 |  |
| User对象 | User object | Swagger 用户对象说明。 |  |
| 用户表 | User table | Swagger 表说明。 |  |
| 主键 / 主键ID | Primary key / Primary key ID | Swagger 字段说明。 |  |
| 模型唯一ID / 话题唯一ID / 活动唯一ID | Unique model/topic/campaign ID | Swagger 字段说明。 |  |
| 标题 | Title | Swagger 字段说明。 |  |
| 配置 | Config | Swagger 字段说明。 |  |
| 模型类型 | Model type | Swagger 字段说明。 |  |
| 是否发布:0=否,1=是,2下架 | Publish status: 0 = no, 1 = yes, 2 = offline | Swagger 状态字段说明。 |  |
| 创建人ID | Creator ID | Swagger 字段说明。 |  |
| 更新人ID | Updater ID | Swagger 字段说明。 |  |
| 是否删除:0=正常,1=删除 | Deleted flag: 0 = normal, 1 = deleted | Swagger 字段说明。 |  |
| 是否可以want 1 是 0 否 | Wantable: 1 = yes, 0 = no | Swagger WantIt 字段说明。 |  |
| 1：开启集赞：0: 关闭集赞 | 1 = enable like collection, 0 = disable like collection | Swagger 活动奖励配置说明。 |  |
| 1：开启评委评奖：0: 关闭评委评奖 | 1 = enable judge awards, 0 = disable judge awards | Swagger 活动奖励配置说明。 |  |
| 活动类型 0=普通 1=edu | Campaign type: 0 = normal, 1 = edu | Swagger 活动类型说明。 |  |
| 活动状态：0-未开始，1-进行中，2-已结束-评奖中 3已结束-获奖名单已公示 | Campaign status: 0 = not started, 1 = in progress, 2 = ended - judging, 3 = ended - winners published | Swagger 活动状态说明。 |  |
| 描述 | Description | Swagger 字段说明。 |  |
| 封面图json | Cover image JSON | Swagger 字段说明。 |  |
| 内容图json | Content image JSON | Swagger 字段说明。 |  |
| 点赞总数 | Total likes | Swagger 字段说明。 |  |
| 浏览总数 | Total views | Swagger 字段说明。 |  |
| 新用户发帖增能:0=否,1=是 | New user post boost: 0 = no, 1 = yes | Swagger 字段说明。 |  |
| want 数量 | Want count | Swagger 字段说明。 |  |
| 评论数量 | Comment count | Swagger 字段说明。 |  |
| 是否可以购买 0 不可以 1 可以 | Purchasable: 0 = no, 1 = yes | Swagger 字段说明。 |  |
| 是否隐藏prompt 0 否 1 是 | Hide prompt: 0 = no, 1 = yes | Swagger 字段说明。 |  |
| 初始状态 | Initial status | ParentGenerateTrace 任务状态枚举展示值。 |  |
| 执行中 | Running | ParentGenerateTrace 任务状态枚举展示值。 |  |
| 执行成功 | Succeeded | ParentGenerateTrace 任务状态枚举展示值。 |  |
| 执行失败 | Failed | ParentGenerateTrace 任务状态枚举展示值。 |  |
| 已取消 | Canceled | ParentGenerateTrace 任务状态枚举展示值。 |  |
| 生图 | Generate image | Swagger 生成响应说明。 |  |
| 生3D结果 | Generated 3D result | Swagger 生成响应说明。 |  |
| 生视频结果 | Generated video result | Swagger 生成响应说明。 |  |
| 结果类型 | Result type | Swagger 生成响应说明。 |  |
| ParentGenerateTrace任务请求 | ParentGenerateTrace task request | Swagger 任务请求说明。 |  |
| ParentGenerateTrace任务进度响应 | ParentGenerateTrace task progress response | Swagger 任务响应说明。 |  |
| 是否强制更新（true: 覆盖所有数据，false: 只更新空数据） | Force update (true: overwrite all data, false: update only empty data) | Swagger 任务参数说明。 |  |
| 任务是否完成 | Whether the task is completed | Swagger 任务响应字段。 |  |
| 任务信息 | Task information | Swagger 任务响应字段。 |  |
| 响应信息 | Response information | Swagger 任务响应字段。 |  |
| 任务状态 | Task status | Swagger 任务字段。 |  |
| 任务描述 | Task description | Swagger 任务字段。 |  |
| 错误信息 | Error message | Swagger 任务字段。 |  |
| 总数 / 批次数 / 已处理记录数 / 成功记录数 / 失败记录数 / 跳过的记录数 | Total count / Batch count / Processed record count / Successful record count / Failed record count / Skipped record count | Swagger 任务统计字段。 |  |
| OBJ/GLB/STL/FBX 3D模型地址 | OBJ/GLB/STL/FBX 3D model URL | Swagger 3D 模型资源地址字段。 |  |
| 网球运动员 | Tennis player | ModelPrompt 职业 prompt 展示值。 |  |
| 消防员 | Firefighter | ModelPrompt 职业 prompt 展示值。 |  |
| 运动员 | Athlete | ModelPrompt 职业 prompt 展示值。 |  |
| 程序员 | Programmer | ModelPrompt 职业 prompt 展示值。 |  |
| 歌手 | Singer | ModelPrompt 职业 prompt 展示值。 |  |
| 士兵 | Soldier | ModelPrompt 职业 prompt 展示值。 |  |
| 艺术家 | Artist | ModelPrompt 职业 prompt 展示值。 |  |
| 警察 | Police officer | ModelPrompt 职业 prompt 展示值。 |  |
| 厨师 | Chef | ModelPrompt 职业 prompt 展示值。 |  |
| 工程师 | Engineer | ModelPrompt 职业 prompt 展示值。 |  |
| 医生 | Doctor | ModelPrompt 职业 prompt 展示值。 |  |
| 科学家 | Scientist | ModelPrompt 职业 prompt 展示值。 |  |
| 教授 | Professor | ModelPrompt 职业 prompt 展示值。 |  |
| 足球运动员 | Soccer player | ModelPrompt 职业 prompt 展示值。 |  |
| 护士 | Nurse | ModelPrompt 职业 prompt 展示值。 |  |
| 化学家 | Chemist | ModelPrompt 职业 prompt 展示值。 |  |
| 园丁 | Gardener | ModelPrompt 职业 prompt 展示值。 |  |
| 新用户引导场景 | New user onboarding scenario | 生成服务场景枚举描述。 |  |
| 生图结果表 / 生图记录表 / 生图记录汇总表 / 生视频记录表 | Generated image result table / Image generation record table / Image generation record summary table / Generated video record table | Swagger 生成实体说明。 |  |
| 原图webp地址 / 原图地址 / 原图地址水印 | Original image WebP URL / Original image URL / Original watermarked image URL | Swagger 图片地址字段。 |  |
| 小图地址 | Thumbnail URL | Swagger 缩略图地址字段。 |  |
| 生产状态,0:默认;1:生产中 | Production status: 0 = default, 1 = in production | Swagger 生产状态字段。 |  |
| 图片扩展信息 | Image extension information | Swagger 图片扩展信息字段。 |  |
| 图片在工程侧是否通过审核 | Image approved by engineering review | Swagger 审核状态字段。 |  |
| 图片在算法侧是否通过审核 | Image approved by algorithm review | Swagger 审核状态字段。 |  |
| prompt翻译后的内容,JSON | Translated prompt content, JSON | Swagger prompt 字段说明。 |  |
| 生图状态,0:初始状态;1:执行中;2:执行成功;3:执行失败;4部分失败 | Image generation status: 0 = initial, 1 = running, 2 = succeeded, 3 = failed, 4 = partially failed | Swagger 生成状态字段。 |  |
| 生成对象的数量 | Number of generated objects | Swagger batchSize 字段。 |  |
| 生图任务来源,0:主站 1:校园版 | Image generation task source: 0 = main site, 1 = campus edition | Swagger 来源字段。 |  |
| 血缘模型关系 | Generation lineage model relationship | Swagger 生成血缘字段。 |  |
| 任务耗时 | Task duration | Swagger 任务耗时字段。 |  |
| 图生图记录表 | Image-to-image generation record table | Swagger 图生图实体说明。 |  |
| 强度 | Strength | Swagger 图生图参数字段。 |  |
| 作品 | Work | 多视角命名失败兜底作品名。 |  |
| 角色命名系统指令 | Character Naming System Instruction | 多视角命名 Vision system prompt。 |  |
| 物种档案官 | Species Archivist | 多视角命名角色设定。 |  |
| 名字 / 一句话人设 / 转发钩子 | name / one-sentence persona / share hook | 多视角命名 JSON 示例值。 |  |
| 未找到 | Not found | ParentGenerateTrace 任务查询异常文案。 |  |
| 任务不存在 | Task does not exist | 生成任务异常文案。 |  |
| 任务状态[...]不允许取消 | Task status [...] cannot be canceled | ParentGenerateTrace 取消失败文案。 |  |
| 全量刷新ParentGenerateTrace字段 | Full refresh of ParentGenerateTrace fields | ParentGenerateTrace 任务描述。 |  |
| 增量更新ParentGenerateTrace字段 | Incremental update of ParentGenerateTrace fields | ParentGenerateTrace 任务描述。 |  |
| 记录没有父级链，跳过处理 | The record has no parent chain; skip processing | ParentGenerateTrace 失败原因。 |  |
| 算法节点占用超时 | Algorithm Node Occupancy Timeout | 算法节点告警标题。 |  |
| 算法节点可用性变更 | Algorithm Node Availability Changed | 算法节点告警标题。 |  |
| 场景未配置算法节点 | No algorithm node is configured for this scene | 任务失败错误信息。 |  |
| 场景无可用算法节点 | No Available Algorithm Node for Scene | 算法节点告警标题。 |  |
| 任务执行超时 | Task execution timed out | 任务失败错误信息。 |  |
| 算法生成任务执行失败【%s】 | Algorithm generation task execution failed [%s] | 算法生成失败告警标题。 |  |
| 任务id: %s，失败原因: %s | Task ID: %s, failure reason: %s | 算法生成失败告警正文。 |  |
| 任务状态异常 | Invalid task status | 生成任务查询异常文案。 |  |
| imageId不能为空 | imageId cannot be empty | 参数校验异常文案。 |  |
| 任务类型不存在 | Task type does not exist | 生成任务异常文案。 |  |
| WebSocket消息发送超时 | WebSocket message sending timed out | WebSocket 进度推送超时错误信息。 |  |
| 重新触发成功，失败任务已提交 | Retriggered successfully. Failed tasks have been submitted. | 视频上传任务重试接口返回文案。 |  |
| 任务状态已变更，请勿重复点击 | Task status has changed. Please do not click repeatedly. | 视频上传任务重试接口返回文案。 |  |
| 任务进行中，请勿重复点击 | Task is in progress. Please do not click repeatedly. | 视频上传任务重试接口返回文案。 |  |
| 任务已成功，无需重新触发 | Task has succeeded. No retrigger is needed. | 视频上传任务重试接口返回文案。 |  |
| 当前状态不支持重新触发 | Retriggering is not supported in the current status. | 视频上传任务重试接口返回文案。 |  |
| **失败信息**：已重试 N 次 | **Failure Details**: retried N times | 视频上传失败飞书告警字段。 |  |
| **视频地址** | **Video URL** | 视频上传失败飞书告警字段。 |  |
| **重新触发任务** | **Retrigger Task** | 视频上传失败飞书告警字段。 |  |
| **通知** | **Notify** | 视频上传失败飞书告警字段。 |  |
| 本地视频转码任务失败 | Local Video Transcoding Task Failed | 视频上传失败飞书卡片标题。 |  |
| 本地视频审核任务失败 | Local Video Review Task Failed | 视频上传失败飞书卡片标题。 |  |
| 飞机内 | inside an airplane | Line Friends group photo 特殊 prompt 模板。 |  |
| 吃饭 | having a meal | Line Friends group photo 特殊 prompt 模板。 |  |
| 手举礼物 | holding a gift | Line Friends group photo 特殊 prompt 模板。 |  |
| 草地 | on grass | Line Friends group photo 特殊 prompt 模板。 |  |
| 下车接机 | getting out of a car for airport pickup | Line Friends group photo 特殊 prompt 模板。 |  |
| 蛋糕许愿 | making a wish over a cake | Line Friends group photo 特殊 prompt 模板。 |  |
| 机场外 | outside the airport | Line Friends group photo 特殊 prompt 模板。 |  |
| 吃蛋糕 | eating cake | Line Friends group photo 特殊 prompt 模板。 |  |

## jjewelry-java

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 用户主动注销 | User-initiated account closure | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 请先登录 | Please log in first | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 账号已注销 | Account has been closed | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 造好物-用户主动注销 | Zaohaowu - user-initiated account closure | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| fileUrl不能为空 | fileUrl cannot be empty | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 非法审核结果, 仅支持 PASS/BLOCK | Invalid audit result. Only PASS/BLOCK is supported | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 取消任务成功 | Task canceled successfully | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 当前没有未结束的任务 | There are no unfinished tasks currently | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 批量取消完成：成功 %d 个，失败 %d 个 | Batch cancellation completed: %d succeeded, %d failed | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 清理完成，共清理 %d 个缓存条目 | Cleanup completed. Cleared %d cache entries | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 手机号列表不能为空 | Phone number list cannot be empty | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 用户ID列表不能为空 | User ID list cannot be empty | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 任务已启动 | Task started | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 查询成功，共找到 %d 个未结束的任务 | Query succeeded. Found %d unfinished tasks | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 查询小程序码scene的详细信息失败 | Failed to query miniapp code scene details | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 已发送Fake点赞MQ消息，用于调试，userId: | Fake like MQ message has been sent for debugging, userId: | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 视频数量不能超过%d个 | Video count cannot exceed %d | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| 恭喜你的作品被官方评为「精选」！被评为「精选」的作品可以在造好物社区得到官方流量扶持，得到更多曝光！请再接再厉，使用造好物AI继续创作优秀内容～ | Congratulations! Your work has been officially selected as Featured. Featured works can receive official traffic support and more exposure in the Zaohaowu community. Keep creating great content with Zaohaowu AI! | jjewelry-java 业务文案。 |  |
| 已完成 | Completed | jjewelry-java 业务文案。 |  |
| 快回来看看你的图片作品成品！ | Come back and check the finished image work! | jjewelry-java 业务文案。 |  |
| 快回来看看你的视频作品成品！ | Come back and check the finished video work! | jjewelry-java 业务文案。 |  |
| @造好物官方 | @Zaohaowu Official | jjewelry-java 业务文案。 |  |
| 你之前想要的产品已上架啦！来低价抢购吧 | The product you wanted is now available! Grab it at a low price | jjewelry-java 业务文案。 |  |
| 元 | CNY | jjewelry-java 业务文案。 |  |
| 在作品中@了你 | mentioned you in a work | jjewelry-java 业务文案。 |  |
| 在评论区@了你 | mentioned you in a comment | jjewelry-java 业务文案。 |  |
| 已发货 | Shipped | jjewelry-java 业务文案。 |  |
| 恭喜你的作品已集满「想要」！来免费领取吧 | Your work has collected enough Wants! Claim it for free now | jjewelry-java 业务文案。 |  |
| 抱歉你想要的作品暂无法生产，已退还... | Sorry, the work you wanted cannot be produced yet. It has been refunded... | jjewelry-java 业务文案。 |  |
| 抱歉您的作品经评估无法生产，请添加... | Sorry, your work cannot be produced after evaluation. Please add... | jjewelry-java 业务文案。 |  |
| 有用户给你的作品投出了「想要」 | A user cast a Want for your work | jjewelry-java 业务文案。 |  |
| 来看看谁又新关注了你！ | See who just followed you! | jjewelry-java 业务文案。 |  |
| 生产中 | In production | jjewelry-java 业务文案。 |  |
| 进入领奖页面，点击【查看赏品】前往订单页 | Go to the prize claim page and tap [View Prize] to open the order page | jjewelry-java 业务文案。 |  |
| 发现 %d 条站内信配置处于处理中状态，请检查处理逻辑 | Found %d site message configs in processing status. Please check the processing logic | 消息、推送或微信触达文案。 |  |
| 系统异常 | System exception | 消息、推送或微信触达文案。 |  |
| 任务进度查询被中断 | Task progress query was interrupted | jjewelry-java 业务文案。 |  |
| <br>   失败 userIds: | <br>   Failed userIds: | jjewelry-java 业务文案。 |  |
| targetDate=%s, error=%s, 请人工排查并兜底补发 | targetDate=%s, error=%s. Please investigate manually and reissue as fallback | jjewelry-java 业务文案。 |  |
| 处理异常: | Processing exception: | jjewelry-java 业务文案。 |  |
| 失败用户详细原因详见服务日志，关键字: [RewardReissueScanJob] | See service logs for detailed failed-user reasons. Keyword: [RewardReissueScanJob] | jjewelry-java 业务文案。 |  |
| 无 COMPLETED 用户，已跳过 | No COMPLETED users. Skipped | jjewelry-java 业务文案。 |  |
| 日期： | Date: | jjewelry-java 业务文案。 |  |
| 全量对账完成: processed=%d, missing=%d, embeddingDrift=%d, scalarUpdated=%d, | Full reconciliation completed: processed=%d, missing=%d, embeddingDrift=%d, scalarUpdated=%d, | jjewelry-java 业务文案。 |  |
| 定向同步完成: total=%d, missing=%d, embeddingDrift=%d, scalarUpdated=%d, | Targeted sync completed: total=%d, missing=%d, embeddingDrift=%d, scalarUpdated=%d, | jjewelry-java 业务文案。 |  |
| [resized-sync] 全量同步完成: processed=%d, missing=%d, embeddingDrift=%d, scalarUpdated=%d, | [resized-sync] Full sync completed: processed=%d, missing=%d, embeddingDrift=%d, scalarUpdated=%d, | jjewelry-java 业务文案。 |  |
| [resized-sync] 定向同步完成: total=%d, missing=%d, embeddingDrift=%d, scalarUpdated=%d, | [resized-sync] Targeted sync completed: total=%d, missing=%d, embeddingDrift=%d, scalarUpdated=%d, | jjewelry-java 业务文案。 |  |
| ALoader加载失败: | ALoader load failed: | jjewelry-java 业务文案。 |  |
| ALoader加载的数据结果 | Data result loaded by ALoader | jjewelry-java 业务文案。 |  |
| 服务A调用失败 | Service A call failed | jjewelry-java 业务文案。 |  |
| BLoader加载失败: | BLoader load failed: | jjewelry-java 业务文案。 |  |
| 服务B调用失败 | Service B call failed | jjewelry-java 业务文案。 |  |
| CLoader加载失败: | CLoader load failed: | jjewelry-java 业务文案。 |  |
| CLoader加载的数据结果 | Data result loaded by CLoader | jjewelry-java 业务文案。 |  |
| 服务C调用失败 | Service C call failed | jjewelry-java 业务文案。 |  |
| 并发加载被拒绝：%s | Concurrent loading rejected: %s | jjewelry-java 业务文案。 |  |
| 并发加载超时，超时时间：%d %s | Concurrent loading timed out, timeout: %d %s | jjewelry-java 业务文案。 |  |
| 图片编辑 | Image Editing | 业务枚举展示名。 |  |
| 图生图 | Image to Image | 业务枚举展示名。 |  |
| 图生图-添加赛事名称 | Image to Image - Add campaign name | 业务枚举展示名。 |  |
| 图生图-马拉松 | Image to Image - Marathon | 业务枚举展示名。 |  |
| 文改图 | Text-based Image Editing | 业务枚举展示名。 |  |
| 文生图 | Text to Image | 业务枚举展示名。 |  |
| 参数名不能为空 | Parameter name cannot be empty | jjewelry-java 业务文案。 |  |
| 造好物.zip | Zaohaowu.zip | jjewelry-java 业务文案。 |  |
| App限时福利:每日活跃奖励已经到账! | Limited-time App benefit: daily active reward has arrived! | 活动业务展示、状态或异常文案。 |  |
| 想要活动 | Want It activity | 活动业务展示、状态或异常文案。 |  |
| 活跃活动 | Active activity | 活动业务展示、状态或异常文案。 |  |
| 检测到同一账号已下过重复订单～ | Detected that the same account has placed a duplicate order~ | 活动业务展示、状态或异常文案。 |  |
| 检测到用户主动退出体验AI造物流程～ | Detected that the user actively exited the AI creation experience flow~ | 活动业务展示、状态或异常文案。 |  |
| 检测到用户尚未完成体验AI造物流程～ | Detected that the user has not completed the AI creation experience flow~ | 活动业务展示、状态或异常文案。 |  |
| 主动退出 | Exited voluntarily | 活动业务展示、状态或异常文案。 |  |
| 初始化 | Initialization | 活动业务展示、状态或异常文案。 |  |
| 已下单 | Ordered | 活动业务展示、状态或异常文案。 |  |
| 已分享 | Shared | 活动业务展示、状态或异常文案。 |  |
| 已发布 | Published | 活动业务展示、状态或异常文案。 |  |
| 活跃里程碑弹窗 | Active milestone popup | 活动业务展示、状态或异常文案。 |  |
| 老用户首登弹窗 | Existing user first-login popup | 活动业务展示、状态或异常文案。 |  |
| 想要活动弹窗 | Want It activity popup | 活动业务展示、状态或异常文案。 |  |
| 想要活动第二天弹窗 | Want It activity second-day popup | 活动业务展示、状态或异常文案。 |  |
| 活跃活动弹窗 | Active activity popup | 活动业务展示、状态或异常文案。 |  |
| 购买弹窗 | Purchase popup | 活动业务展示、状态或异常文案。 |  |
| 奖励池快照类型 | Reward pool snapshot type | 活动业务展示、状态或异常文案。 |  |
| 已失效 | Expired | 活动业务展示、状态或异常文案。 |  |
| 已领取 | Claimed | 活动业务展示、状态或异常文案。 |  |
| 未领取 | Unclaimed | 活动业务展示、状态或异常文案。 |  |
| 不发放（超过单日上限等） | Not issued (daily limit exceeded, etc.) | 活动业务展示、状态或异常文案。 |  |
| 不发放（超过新用户归因窗口） | Not issued (outside new user attribution window) | 活动业务展示、状态或异常文案。 |  |
| 已发放 | Issued | 活动业务展示、状态或异常文案。 |  |
| 未发放（待发放） | Not issued (pending) | 活动业务展示、状态或异常文案。 |  |
| App 下载引流 tips | App download conversion tips | 活动业务展示、状态或异常文案。 |  |
| *再投 %d 个想要&购买至少 %d 件想要商品 | *Cast %d more Wants and purchase at least %d Want products | 活动业务展示、状态或异常文案。 |  |
| *再投 %d 个想要, 即可领取奖品 | *Cast %d more Wants to claim a prize | 活动业务展示、状态或异常文案。 |  |
| *已投满 %d 个想要&购买过至少 %d 件想要商品, 可领取奖品 | *You have cast %d Wants and purchased at least %d Want products; you can claim a prize | 活动业务展示、状态或异常文案。 |  |
| *已投满 %d 个想要, 可领取奖品 | *You have cast %d Wants and can claim a prize | 活动业务展示、状态或异常文案。 |  |
| *已投满 %d 个想要, 需购买至少 %d 件想要商品 | *You have cast %d Wants; purchase at least %d Want products | 活动业务展示、状态或异常文案。 |  |
| *已购买过至少 %d 件想要商品, 需再投 %d 个想要 | *You have purchased at least %d Want products; cast %d more Wants | 活动业务展示、状态或异常文案。 |  |
| 1.因不可抗力退回的想要次数（作品被删除/无法做货），重新投出时不计入想要任务累计计数<br>2.每日限量1000件; 如当日领取不了, 请隔日再来或联系平台客服 | 1. Want It counts returned due to force majeure (work deleted/cannot be produced) will not count toward Want It task accumulation when cast again.<br>2. Daily limit is 1000 items. If you cannot claim today, come back tomorrow or contact platform customer service. | 活动业务展示、状态或异常文案。 |  |
| 再投%d个想要&购买%d件想要商品, 即可免费领取实物奖励 | Cast %d more Wants and purchase %d Want products to claim a physical reward for free | 活动业务展示、状态或异常文案。 |  |
| 再投%d个想要, 即可免费领取实物奖励 | Cast %d more Wants to claim a physical reward for free | 活动业务展示、状态或异常文案。 |  |
| 再投<highlight> %d 个想要&购买 %d 件想要商品</highlight>，就可领取“%s”！ | Cast <highlight>%d more Wants and purchase %d Want products</highlight> to claim \"%s\"! | 活动业务展示、状态或异常文案。 |  |
| 再投<highlight> %d 个想要</highlight>，就可领取“%s”！ | Cast <highlight>%d more Wants</highlight> to claim \"%s\"! | 活动业务展示、状态或异常文案。 |  |
| 再购买%d件想要商品, 即可免费领取实物奖励 | Purchase %d more Want products to claim a physical reward for free | 活动业务展示、状态或异常文案。 |  |
| 再购买<highlight> %d 件想要商品</highlight>，就可领取“%s”！ | Purchase <highlight>%d more Want products</highlight> to claim \"%s\"! | 活动业务展示、状态或异常文案。 |  |
| 冲鸭\uD83E\uDD29！只要再投%d个想要&购买%d件想要商品，你就可以免费领取「%s」一件！ | Go for it! Cast %d more Wants and purchase %d Want products to claim one %s for free! | 活动业务展示、状态或异常文案。 |  |
| 冲鸭\uD83E\uDD29！只要再投%d个想要，你就可以免费领取「%s」一件！ | Go for it! Cast %d more Wants to claim one %s for free! | 活动业务展示、状态或异常文案。 |  |
| 去领取 | Claim | 活动业务展示、状态或异常文案。 |  |
| 哇噻！去看看 | Wow! Take a look | 活动业务展示、状态或异常文案。 |  |
| 已投满%d个想要&购买过%d件想要商品, 快来领取实物奖励！ | You have cast %d Wants and purchased %d Want products. Claim your physical reward now! | 活动业务展示、状态或异常文案。 |  |
| 已投满%d个想要, 快来领取实物奖励！ | You have cast %d Wants. Claim your physical reward now! | 活动业务展示、状态或异常文案。 |  |
| 已投满<highlight> %d 个想要&购买过 %d 件想要商品</highlight>，快来领取“%s”！ | You have cast <highlight>%d Wants and purchased %d Want products</highlight>. Claim \"%s\" now! | 活动业务展示、状态或异常文案。 |  |
| 已投满<highlight> %d 个想要</highlight>，快来领取“%s”！ | You have cast <highlight>%d Wants</highlight>. Claim \"%s\" now! | 活动业务展示、状态或异常文案。 |  |
| 开始玩「想要」解锁超多平台激励！ | Start playing Want to unlock more platform rewards! | 活动业务展示、状态或异常文案。 |  |
| 开始玩「想要」解锁超多平台激励，海量奖品等你来拿！ | Start playing Want to unlock more platform rewards. Tons of prizes are waiting for you! | 活动业务展示、状态或异常文案。 |  |
| 得「%s」%d件 | Get \"%s\" x %d | 活动业务展示、状态或异常文案。 |  |
| 明天接着来！前3天投满%d个{want_icon}想要，就能免费领取「%s」实物奖励！ | Come back tomorrow! Cast %d {want_icon} Wants in the first 3 days to claim the \"%s\" physical reward for free! | 活动业务展示、状态或异常文案。 |  |
| 玩「想要」解锁超多平台激励！ | Play Want to unlock more platform rewards! | 活动业务展示、状态或异常文案。 |  |
| 玩「想要」解锁超多平台激励，海量奖品等你来拿！ | Play Want to unlock more platform rewards. Tons of prizes are waiting for you! | 活动业务展示、状态或异常文案。 |  |
| 玩想要, 赢官方奖励 | Play Want, Win Official Rewards | 活动业务展示、状态或异常文案。 |  |
| 登录“造好物”，当天即可领取免费毛绒挂件～ | Log in to Zaohaowu today to claim a free plush charm. | 活动业务展示、状态或异常文案。 |  |
| 该任务奖励当日已达领取上限，请明日再来或联系官方客服。 | Today's claim limit for this task reward has been reached. Please come back tomorrow or contact official support. | 活动业务展示、状态或异常文案。 |  |
| 领取「%s」%d件 | Claim \"%s\" x %d | 活动业务展示、状态或异常文案。 |  |
| 领奖励 | Claim reward | 活动业务展示、状态或异常文案。 |  |
| 新人专享 | New user exclusive | 活动业务展示、状态或异常文案。 |  |
| 1天内投满%d个想要 | Cast %d Wants within 1 day | 活动业务展示、状态或异常文案。 |  |
| 1天内投满%d个想要%s(%d/%d)%s | Cast %d Wants within 1 day%s(%d/%d)%s | 活动业务展示、状态或异常文案。 |  |
| 3天内投满%d个想要 | Cast %d Wants within 3 days | 活动业务展示、状态或异常文案。 |  |
| 3天内投满%d个想要%s(%d/%d)%s | Cast %d Wants within 3 days%s(%d/%d)%s | 活动业务展示、状态或异常文案。 |  |
| 你已累计投满 %d 个想要<br>可免费领取%d件「%s」！！ | You have cast %d Wants<br>You can claim %d \"%s\" for free!! | 活动业务展示、状态或异常文案。 |  |
| 你已累计投满%d个想要&购买过至少%d件想要商品<br>可免费领取%d件「%s」！！ | You have cast %d Wants and purchased at least %d Want products<br>You can claim %d \"%s\" for free!! | 活动业务展示、状态或异常文案。 |  |
| 平均14天达成 | Average completion: 14 days | 活动业务展示、状态或异常文案。 |  |
| 平均161天达成 | Average completion: 161 days | 活动业务展示、状态或异常文案。 |  |
| 平均320天达成 | Average completion: 320 days | 活动业务展示、状态或异常文案。 |  |
| 平均33天达成 | Average completion: 33 days | 活动业务展示、状态或异常文案。 |  |
| 平均65天达成 | Average completion: 65 days | 活动业务展示、状态或异常文案。 |  |
| 平均7天达成 | Average completion: 7 days | 活动业务展示、状态或异常文案。 |  |
| 平均97天达成 | Average completion: 97 days | 活动业务展示、状态或异常文案。 |  |
| 投满%d个想要 | Cast %d Wants | 活动业务展示、状态或异常文案。 |  |
| 投满%d个想要%s(%d/%d)%s | Cast %d Wants%s(%d/%d)%s | 活动业务展示、状态或异常文案。 |  |
| 投满%d个想要%s(%d/%d)%s且购买过%d件想要商品%s(不含退款 %d/%d)%s | Cast %d Wants%s(%d/%d)%s and purchase %d Want products%s(excluding refunds %d/%d)%s | 活动业务展示、状态或异常文案。 |  |
| 投满%d个想要且购买过%d件想要商品 | Cast %d Wants and purchase %d Want products | 活动业务展示、状态或异常文案。 |  |
| 下载App | Download App | 活动业务展示、状态或异常文案。 |  |
| 即可获得更多 想要& 免费奖品池更新啦 | Get more Wants & the free prize pool has been updated | 活动业务展示、状态或异常文案。 |  |
| 奖励+%s | Reward +%s | 活动业务展示、状态或异常文案。 |  |
| 感谢下单,春节限时额外赠送 %s 次{want_icon}想要！ | Thanks for your order. Spring Festival limited-time bonus: %s extra {want_icon} Wants! | 活动业务展示、状态或异常文案。 |  |
| 感谢下单,额外赠送你 %s 次{want_icon}想要 | Thanks for your order. You received %s extra {want_icon} Wants | 活动业务展示、状态或异常文案。 |  |
| 新人首日活跃奖励想要 | New user first-day active Want reward | 活动业务展示、状态或异常文案。 |  |
| 新年限时增发- | New Year limited-time extra grant - | 活动业务展示、状态或异常文案。 |  |
| 春节玩想要, 福利加倍! | Play Want during Spring Festival, double the benefits! | 活动业务展示、状态或异常文案。 |  |
| 春节福利-新用户注册奖励已到账!{want_icon}想要+5 | Spring Festival benefit - new user registration reward received! {want_icon} Wants +5 | 活动业务展示、状态或异常文案。 |  |
| 春节福利-每日活跃奖励已到账!{want_icon}想要+4 | Spring Festival benefit - daily active reward received! {want_icon} Wants +4 | 活动业务展示、状态或异常文案。 |  |
| 欢迎老用户来APP！<br>额外奖励你10次{want_icon}想要 | Welcome back to the App!<br>You get 10 extra {want_icon} Wants. | 活动业务展示、状态或异常文案。 |  |
| 每日活跃想要 | Daily active Wants | 活动业务展示、状态或异常文案。 |  |
| 解锁惊喜千元大奖 限量版Labubu全套盲盒 | Unlock a surprise grand prize worth thousands: a full Limited Edition Labubu blind box set | 活动业务展示、状态或异常文案。 |  |
| 造好物App上线啦！快来体验更完整顺滑的造物之旅 | The Zaohaowu App is live! Come experience a more complete and smoother creation journey. | 活动业务展示、状态或异常文案。 |  |
| 里程碑配置不存在 | Milestone config does not exist | 活动业务展示、状态或异常文案。 |  |
| 限量版 | Limited Edition | 活动业务展示、状态或异常文案。 |  |
| 额外想要次数 +%s | Extra Want chances +%s | 活动业务展示、状态或异常文案。 |  |
| 马上投出 | Cast Now | 活动业务展示、状态或异常文案。 |  |
| 任务奖励订单通知 | Task reward order notification | 活动业务展示、状态或异常文案。 |  |
| 反作弊策略退款 | Anti-cheat policy refund | 活动业务展示、状态或异常文案。 |  |
| 您已经领取过该礼品 或 有待支付的礼品订单（如有疑问，请联系造好物官方客服） | You have already claimed this gift or have a pending gift order. If you have questions, contact Zaohaowu official customer service. | 活动业务展示、状态或异常文案。 |  |
| 您的「想要」任务奖励订单因未满足活动规则已被取消，订单号 {orderId}。如有疑问请联系造好物客服。 | Your Want It task reward order was canceled because the activity rules were not met. Order No. {orderId}. If you have questions, contact Zaohaowu customer service. | 活动业务展示、状态或异常文案。 |  |
| 您的账号状态异常，无法进行此操作。如有疑问，请联系造好物官方客服。 | Your account status is abnormal, so this action cannot be performed. If you have any questions, please contact Zaohaowu official customer service. | 活动业务展示、状态或异常文案。 |  |
| 连续登录进度数据不存在 | Consecutive login progress data does not exist | 活动业务展示、状态或异常文案。 |  |
| 里程碑信息为空 | Milestone information is empty | 活动业务展示、状态或异常文案。 |  |
| [事件类型]： | [Event Type]: | 活动业务展示、状态或异常文案。 |  |
| [手机号]： | [Phone Number]: | 活动业务展示、状态或异常文案。 |  |
| [检测时间]： | [Check Time]: | 活动业务展示、状态或异常文案。 |  |
| [用户ID]： | [User ID]: | 活动业务展示、状态或异常文案。 |  |
| [策略决策]：** | [Strategy Decision]: ** | 活动业务展示、状态或异常文案。 |  |
| [策略描述]：<br> | [Strategy Description]:<br> | 活动业务展示、状态或异常文案。 |  |
| [风险等级]：** | [Risk Level]: ** | 活动业务展示、状态或异常文案。 |  |
| 无 | None | 活动业务展示、状态或异常文案。 |  |
| 活动完成时手机号风险检测 | Phone number risk check when activity is completed | 活动业务展示、状态或异常文案。 |  |
| 用户高风险检测 | High-risk user detection | 活动业务展示、状态或异常文案。 |  |
| 【拉新活动奖励发放】 | [Referral activity reward issuance] | 活动业务展示、状态或异常文案。 |  |
| 【任务中心服务】 | [Task Center Service] | 活动业务展示、状态或异常文案。 |  |
| 任务奖励兑奖黑名单用户取消奖励发放 | Cancel reward grant for task reward redemption blacklist user | 活动业务展示、状态或异常文案。 |  |
| 奖励发放超过当日上限，已加入次日 10:00 自动补发队列。如明日 10:30 后仍未收到补发完成通知，请人工补发兜底。message: | Reward grants exceeded today's limit and have been added to the automatic reissue queue for 10:00 tomorrow. If no reissue completion notice is received after 10:30 tomorrow, please manually reissue as fallback. message: | 活动业务展示、状态或异常文案。 |  |
| 批量添加黑名单失败: | Failed to batch add blacklist: | 活动业务展示、状态或异常文案。 |  |
| 批量移除黑名单失败: | Failed to batch remove blacklist: | 活动业务展示、状态或异常文案。 |  |
| 更新黑名单配置失败: | Failed to update blacklist config: | 活动业务展示、状态或异常文案。 |  |
| 未找到有效的优惠引用（externalBenefitRef） | No valid discount reference found (externalBenefitRef) | 活动业务展示、状态或异常文案。 |  |
| 没有COMPLETED状态任务或处理失败 | No COMPLETED task exists or processing failed | 活动业务展示、状态或异常文案。 |  |
| 用户ID列表不能为空 | User ID list cannot be empty | 活动业务展示、状态或异常文案。 |  |
| 用户不在黑名单中或无效 | User is not in the blacklist or invalid | 活动业务展示、状态或异常文案。 |  |
| 用户已在黑名单中或无效 | User is already in the blacklist or invalid | 活动业务展示、状态或异常文案。 |  |
| 退款后有效购买笔数低于档位契约要求，撤回未核销的任务奖励优惠 | After refund, valid purchase count is below tier requirements; revoke unused task reward discount | 活动业务展示、状态或异常文案。 |  |
| 否 | No | 活动业务展示、状态或异常文案。 |  |
| 命中策略 | Matched strategy | 活动业务展示、状态或异常文案。 |  |
| 场景 | Scenario | 活动业务展示、状态或异常文案。 |  |
| 收货手机号 | Recipient phone number | 活动业务展示、状态或异常文案。 |  |
| 是 | Yes | 活动业务展示、状态或异常文案。 |  |
| 是否作弊 | Cheating flag | 活动业务展示、状态或异常文案。 |  |
| 检查时间 | Check time | 活动业务展示、状态或异常文案。 |  |
| 注册手机号 | Registration phone number | 活动业务展示、状态或异常文案。 |  |
| 用户ID | User ID | 活动业务展示、状态或异常文案。 |  |
| 风险检测ID | Risk check ID | 活动业务展示、状态或异常文案。 |  |
| 取消优惠失败, userId= | Failed to cancel discount, userId= | 活动业务展示、状态或异常文案。 |  |
| 取消优惠失败: | Failed to cancel discount: | 活动业务展示、状态或异常文案。 |  |
| 添加优惠失败, userId= | Failed to add discount, userId= | 活动业务展示、状态或异常文案。 |  |
| 添加优惠失败: | Failed to add discount: | 活动业务展示、状态或异常文案。 |  |
| 用户信息不完整, userId= | User information is incomplete, userId= | 活动业务展示、状态或异常文案。 |  |
| 用户ID不能为空 | User ID cannot be empty | 活动业务展示、状态或异常文案。 |  |
| - [任务ID]： | - [Task ID]: | 活动业务展示、状态或异常文案。 |  |
| - [优惠ID]： | - [Discount ID]: | 活动业务展示、状态或异常文案。 |  |
| - [发放单ID]： | - [Grant ID]: | 活动业务展示、状态或异常文案。 |  |
| - [商品ID]： | - [Product ID]: | 活动业务展示、状态或异常文案。 |  |
| - [处置结果]： | - [Disposition]: | 活动业务展示、状态或异常文案。 |  |
| - [奖励档位]： | - [Reward tier]: | 活动业务展示、状态或异常文案。 |  |
| - [想要奖励兑换订单ID]： | - [Want reward redemption order ID]: | 活动业务展示、状态或异常文案。 |  |
| - [用户ID]： | - [User ID]: | 活动业务展示、状态或异常文案。 |  |
| - [订单支付时间]： | - [Order payment time]: | 活动业务展示、状态或异常文案。 |  |
| - [订单条目ID]： | - [Order item ID]: | 活动业务展示、状态或异常文案。 |  |
| - [购买笔数 要求/当前]： | - [Purchase count required/current]: | 活动业务展示、状态或异常文案。 |  |
| - [风控检查时间]： | - [Risk check time]: | 活动业务展示、状态或异常文案。 |  |
| 想要任务领奖后风控命中（CHEAT）- 已处理 | Want task reward post-claim risk hit (CHEAT) - processed | 活动业务展示、状态或异常文案。 |  |
| 检测到想要任务领奖后购买笔数不满足兑奖要求 | Detected that post-claim purchase count does not meet reward redemption requirements | 活动业务展示、状态或异常文案。 |  |
| gapDay要大于0 | gapDay must be greater than 0 | 接口/数据模型展示字段。 |  |
| contentGapDay要大于0 | contentGapDay must be greater than 0 | 接口/数据模型展示字段。 |  |
| count要大于0 | count must be greater than 0 | 接口/数据模型展示字段。 |  |
| dataDayGap要大于0 | dataDayGap must be greater than 0 | 接口/数据模型展示字段。 |  |
| maxContentCountForVv不能为空 | maxContentCountForVv cannot be null | 接口/数据模型展示字段。 |  |
| maxContentCountForVv要大于0 | maxContentCountForVv must be greater than 0 | 接口/数据模型展示字段。 |  |
| topUserCountByContent不能为空 | topUserCountByContent cannot be null | 接口/数据模型展示字段。 |  |
| topUserCountByContent要大于0 | topUserCountByContent must be greater than 0 | 接口/数据模型展示字段。 |  |
| topUserCountByInteraction不能为空 | topUserCountByInteraction cannot be null | 接口/数据模型展示字段。 |  |
| topUserCountByInteraction要大于0 | topUserCountByInteraction must be greater than 0 | 接口/数据模型展示字段。 |  |
| userDayGap要大于0 | userDayGap must be greater than 0 | 接口/数据模型展示字段。 |  |
| updateRecommendUser, 参数为空 | updateRecommendUser, parameters are empty | 服务层异常或业务提示文案。 |  |
| 任务参数为空 | Task parameters are empty | 服务层异常或业务提示文案。 |  |
| 初始化参数 | Initialize parameters | 服务层异常或业务提示文案。 |  |
| 周榜 | weekly ranking | 服务层异常或业务提示文案。 |  |
| 日榜 | daily ranking | 服务层异常或业务提示文案。 |  |
| 日榜和周榜 | daily and weekly rankings | 服务层异常或业务提示文案。 |  |
| 步骤1-获取发帖数据并按用户分组 | Step 1 - Get posts and group by user | 服务层异常或业务提示文案。 |  |
| 步骤2-按发帖数筛选用户 | Step 2 - Filter users by post count | 服务层异常或业务提示文案。 |  |
| 步骤3-计算互动数并筛选用户 | Step 3 - Calculate interactions and filter users | 服务层异常或业务提示文案。 |  |
| 步骤4-合并用户 | Step 4 - Merge users | 服务层异常或业务提示文案。 |  |
| 步骤5-批量查询活跃度和分享数 | Step 5 - Batch query activity and share counts | 服务层异常或业务提示文案。 |  |
| 步骤6-计算帖子互动数并筛选VV帖子 | Step 6 - Calculate post interactions and filter VV posts | 服务层异常或业务提示文案。 |  |
| 步骤7-批量查询VV数据 | Step 7 - Batch query VV data | 服务层异常或业务提示文案。 |  |
| 步骤8-计算用户热度分数 | Step 8 - Calculate user hot score | 服务层异常或业务提示文案。 |  |
| 步骤9-排序并保存榜单 | Step 9 - Sort and save rankings | 服务层异常或业务提示文案。 |  |
| 用户榜单数据缺失，今天是周一，缺少%s数据。rankingDates=%s, 实际查询到的榜单数量=%d | User ranking data is missing. Today is Monday and %s data is missing. rankingDates=%s, actual ranking count=%d | 服务层异常或业务提示文案。 |  |
| 用户榜单数据缺失，缺少日榜数据。rankingDates=%s, 实际查询到的榜单数量=%d | User ranking data is missing. Daily ranking data is missing. rankingDates=%s, actual ranking count=%d | 服务层异常或业务提示文案。 |  |
| Mia珠宝控 | Mia Jewelry Fan | 协作活动展示、任务或状态文案。 |  |
| 叶知秋 | Ye Zhiqiu | 协作活动展示、任务或状态文案。 |  |
| 林夏 | Lin Xia | 协作活动展示、任务或状态文案。 |  |
| 沈轻舟 | Shen Qingzhou | 协作活动展示、任务或状态文案。 |  |
| 苏念 | Su Nian | 协作活动展示、任务或状态文案。 |  |
| 造一个梦 | Make a Dream | 协作活动展示、任务或状态文案。 |  |
| 造化弄人 | Fate Maker | 协作活动展示、任务或状态文案。 |  |
| 造型师Amy | Stylist Amy | 协作活动展示、任务或状态文案。 |  |
| 造梦家 | Dream Maker | 协作活动展示、任务或状态文案。 |  |
| 造浪少年 | Wave Maker | 协作活动展示、任务或状态文案。 |  |
| 造点美好 | Make Something Good | 协作活动展示、任务或状态文案。 |  |
| 造物星辰 | Creation Star | 协作活动展示、任务或状态文案。 |  |
| 造趣生活 | Fun Life Maker | 协作活动展示、任务或状态文案。 |  |
| 陈默 | Chen Mo | 协作活动展示、任务或状态文案。 |  |
| 顾清欢 | Gu Qinghuan | 协作活动展示、任务或状态文案。 |  |
| 分享 | Share | 协作活动展示、任务或状态文案。 |  |
| 发布话题 | Publish topic | 协作活动展示、任务或状态文案。 |  |
| 购买 | Purchase | 协作活动展示、任务或状态文案。 |  |
| 已结束 | Ended | 协作活动展示、任务或状态文案。 |  |
| 未开始 | Not Started | 协作活动展示、任务或状态文案。 |  |
| 进行中 | In Progress | 协作活动展示、任务或状态文案。 |  |
| 创作 | Create | 协作活动展示、任务或状态文案。 |  |
| 取消购买 | Cancel purchase | 协作活动展示、任务或状态文案。 |  |
| 初始化 | Initialization | 协作活动展示、任务或状态文案。 |  |
| 取消 | Cancel | 协作活动展示、任务或状态文案。 |  |
| 成功 | Success | 协作活动展示、任务或状态文案。 |  |
| 超出上限 | Exceeded limit | 协作活动展示、任务或状态文案。 |  |
| %s*** %d分钟前抽中「%s」 | %s*** won %d minutes ago: \"%s\" | 协作活动展示、任务或状态文案。 |  |
| 活动不存在或无奖品配置 | Activity does not exist or has no award configuration | 协作活动展示、任务或状态文案。 |  |
| 活动已结束 | Activity has ended | 协作活动展示、任务或状态文案。 |  |
| 活动未开始 | Activity has not started | 协作活动展示、任务或状态文案。 |  |
| 联名活动不存在 | Collaboration activity does not exist | 协作活动展示、任务或状态文案。 |  |
| 联名活动已结束 | Collaboration activity has ended | 协作活动展示、任务或状态文案。 |  |
| 联名活动未开始 | Collaboration activity has not started | 协作活动展示、任务或状态文案。 |  |
| confirmBuyTasks 处理异常 | confirmBuyTasks processing exception | 协作活动展示、任务或状态文案。 |  |
| comment评论 | comment reply | 评论业务展示、状态或异常文案。 |  |
| want it评论 | Want It reply | 评论业务展示、状态或异常文案。 |  |
| 审核中 | Under Review | 评论业务展示、状态或异常文案。 |  |
| 审核拒绝 | Rejected | 评论业务展示、状态或异常文案。 |  |
| 审核通过 | Approved | 评论业务展示、状态或异常文案。 |  |
| 已删除 | Deleted | 评论业务展示、状态或异常文案。 |  |
| 默认 | Default | 评论业务展示、状态或异常文案。 |  |
| 帖子 | Post | 评论业务展示、状态或异常文案。 |  |
| 评论 | Comment | 评论业务展示、状态或异常文案。 |  |
| 长期活动 | Long-term activity | 业务枚举展示名。 |  |
| 限时活动 | Limited-time activity | 业务枚举展示名。 |  |
| 未开始 | Not Started | 业务枚举展示名。 |  |
| 结束-获奖公示 | Ended - winners announced | 业务枚举展示名。 |  |
| 结束-评奖中 | Ended - judging | 业务枚举展示名。 |  |
| 进行中 | In Progress | 业务枚举展示名。 |  |
| 实际调用超时，waitMs=%d, callMs%s%d, threshold=%d %s | Actual call timed out, waitMs=%d, callMs%s%d, threshold=%d %s | 服务层异常或业务提示文案。 |  |
| 并发加载被拒绝（线程池已满），waitMs=%d, reason=%s。提示：所在线程池 queue 已满，建议立即排查下游慢调用或扩容。 | Concurrent loading rejected (thread pool full), waitMs=%d, reason=%s. Tip: the queue of the thread pool is full. Check downstream slow calls or scale capacity immediately. | 服务层异常或业务提示文案。 |  |
| 线程池排队超时，任务尚未启动；waitMs=%d, threshold=%d %s。提示：共享线程池可能已打满，建议检查线程池容量或下游可用性。 | Thread pool queue timed out and task has not started; waitMs=%d, threshold=%d %s. Tip: the shared thread pool may be full. Check pool capacity or downstream availability. | 服务层异常或业务提示文案。 |  |
| multi-loader 池已满，caller 同步执行兜底（数据未丢）\| reason=%s | multi-loader pool is full; caller executed synchronously as fallback (data not lost) \| reason=%s | 服务层异常或业务提示文案。 |  |
| 异常 | exception | 服务层异常或业务提示文案。 |  |
| 慢调用 | slow call | 服务层异常或业务提示文案。 |  |
| 调用耗时 %dms 超过业务阈值 %dms，结果已正常返回未丢弃 \| waitMs=%d, callMs=%d | Call duration %dms exceeded business threshold %dms. Result returned normally and was not discarded \| waitMs=%d, callMs=%d | 服务层异常或业务提示文案。 |  |
| 调用耗时 %dms 超过业务阈值 %dms，结果已正常返回未丢弃 \| waitMs=0, callMs=%d | Call duration %dms exceeded business threshold %dms. Result returned normally and was not discarded \| waitMs=0, callMs=%d | 服务层异常或业务提示文案。 |  |
| 降级硬超时熔断 %dms，caller 已解套；worker 仍异步跑直到 Feign read-timeout 兜底 | Fallback hard timeout circuit break at %dms; caller has been released; worker continues asynchronously until Feign read-timeout fallback | 服务层异常或业务提示文案。 |  |
| 版本号为空 | Version number is empty | jjewelry-java 业务文案。 |  |
| 提示词: | Prompt: | 下载业务提示或状态文案。 |  |
| 文件URL不能为空 | File URL cannot be empty | 下载业务提示或状态文案。 |  |
| 文件下载失败 | File download failed | 下载业务提示或状态文案。 |  |
| 造好物- | Zaohaowu- | 下载业务提示或状态文案。 |  |
| 造好物.mp4 | Zaohaowu.mp4 | 下载业务提示或状态文案。 |  |
| 造好物.zip | Zaohaowu.zip | 下载业务提示或状态文案。 |  |
| 非视频贴 | Not a video post | 下载业务提示或状态文案。 |  |
| 作品创建者不存在 | Work creator does not exist | 下载业务提示或状态文案。 |  |
| 处理中... | Processing... | 下载业务提示或状态文案。 |  |
| 批量下载启动失败 | Failed to start batch download | 下载业务提示或状态文案。 |  |
| 未找到有效的生成记录: | No valid generation records found: | 下载业务提示或状态文案。 |  |
| 未找到有效的视频URL | No valid video URL found | 下载业务提示或状态文案。 |  |
| 未找到有效的视频信息 | No valid video information found | 下载业务提示或状态文案。 |  |
| 未找到记录的有效项目详情: | No valid item details found for records: | 下载业务提示或状态文案。 |  |
| 查询进度失败 | Failed to query progress | 下载业务提示或状态文案。 |  |
| 父生成记录不存在 | Parent generation record does not exist | 下载业务提示或状态文案。 |  |
| 生成记录不存在 | Generation record does not exist | 下载业务提示或状态文案。 |  |
| 视频URL不存在 | Video URL does not exist | 下载业务提示或状态文案。 |  |
| 视频处理失败 | Video processing failed | 下载业务提示或状态文案。 |  |
| 视频资源不存在 | Video resource does not exist | 下载业务提示或状态文案。 |  |
| 启动批量水印任务失败，processId不存在，状态: | Failed to start batch watermark task, processId does not exist, status: | 下载业务提示或状态文案。 |  |
| 启动批量水印任务失败，状态: | Failed to start batch watermark task, status: | 下载业务提示或状态文案。 |  |
| 查询任务进度失败，状态: | Failed to query task progress, status: | 下载业务提示或状态文案。 |  |
| 水印服务响应格式错误：缺少tosUrl字段 | Invalid watermark service response format: missing tosUrl field | 下载业务提示或状态文案。 |  |
| 视频水印服务返回错误状态: | Video watermark service returned error status: | 下载业务提示或状态文案。 |  |
| 批量下载任务已启动 | Batch download task has started | 接口/数据模型展示字段。 |  |
| 下载失败： | Download failed: | 接口/数据模型展示字段。 |  |
| 任务不存在 | Task does not exist | 接口/数据模型展示字段。 |  |
| 恭喜，你的作品已被超过%s位用户表示想要购买！ | Congratulations, more than %s users have shown interest in buying your work! | jjewelry-java 业务文案。 |  |
| 创建人不能为空 | Creator cannot be empty | jjewelry-java 业务文案。 |  |
| 创建属性及选项失败 | Failed to create attribute and options | jjewelry-java 业务文案。 |  |
| 获取属性列表失败 | Failed to get attribute list | jjewelry-java 业务文案。 |  |
| 请求参数不能为空 | Request parameters cannot be empty | jjewelry-java 业务文案。 |  |
| 类目ID不能为空 | Category ID cannot be empty | jjewelry-java 业务文案。 |  |
| 获取类目属性失败 | Failed to get category attributes | jjewelry-java 业务文案。 |  |
| 获取类目树失败 | Failed to get category tree | jjewelry-java 业务文案。 |  |
| 下线商品失败 | Failed to take product offline | jjewelry-java 业务文案。 |  |
| 创建商品参数不能为空 | Create product parameters cannot be empty | jjewelry-java 业务文案。 |  |
| 创建商品失败 | Failed to create product | jjewelry-java 业务文案。 |  |
| 删除商品失败 | Failed to delete product | jjewelry-java 业务文案。 |  |
| 商品ID不能为空 | Product ID cannot be empty | jjewelry-java 业务文案。 |  |
| 商品ID列表不能为空 | Product ID list cannot be empty | jjewelry-java 业务文案。 |  |
| 商品不存在 | Product does not exist | jjewelry-java 业务文案。 |  |
| 商品创建者信息不存在 | Product creator information does not exist | jjewelry-java 业务文案。 |  |
| 商品查询失败 | Product query failed | jjewelry-java 业务文案。 |  |
| 批量下线商品失败 | Failed to batch take products offline | jjewelry-java 业务文案。 |  |
| 提交审核失败 | Failed to submit review | jjewelry-java 业务文案。 |  |
| 更新商品参数不能为空 | Update product parameters cannot be empty | jjewelry-java 业务文案。 |  |
| 更新商品失败 | Failed to update product | jjewelry-java 业务文案。 |  |
| 查询商品创建者信息失败 | Failed to query product creator information | jjewelry-java 业务文案。 |  |
| 手机号不能为空 | Phone number cannot be empty | jjewelry-java 业务文案。 |  |
| 用户信息获取失败 | Failed to get user information | jjewelry-java 业务文案。 |  |
| 认证未通过 | Authentication failed | jjewelry-java 业务文案。 |  |
| 【生图服务】 | [Image Generation Service] | 服务层异常或业务提示文案。 |  |
| 查询日志失败: | Failed to query logs: | 服务层异常或业务提示文案。 |  |
| A级奖品 | A-level award | 抽奖活动展示、状态或异常文案。 |  |
| B级奖品 | B-level award | 抽奖活动展示、状态或异常文案。 |  |
| C级奖品 | C-level award | 抽奖活动展示、状态或异常文案。 |  |
| 兜底奖品 | Fallback award | 抽奖活动展示、状态或异常文案。 |  |
| 兜底(想要券) | Fallback (Want It coupon) | 抽奖活动展示、状态或异常文案。 |  |
| 实物奖品 | Physical award | 抽奖活动展示、状态或异常文案。 |  |
| 任务奖励 | Task reward | 抽奖活动展示、状态或异常文案。 |  |
| 取消扣减 | Cancel deduction | 抽奖活动展示、状态或异常文案。 |  |
| 手动调整 | Manual adjustment | 抽奖活动展示、状态或异常文案。 |  |
| 抽奖扣减 | Draw deduction | 抽奖活动展示、状态或异常文案。 |  |
| 发放中 | Issuing | 抽奖活动展示、状态或异常文案。 |  |
| 失败 | Failed | 抽奖活动展示、状态或异常文案。 |  |
| 成功 | Success | 抽奖活动展示、状态或异常文案。 |  |
| 处理中 | Processing | 抽奖活动展示、状态或异常文案。 |  |
| 已使用 | Used | 抽奖活动展示、状态或异常文案。 |  |
| 未使用 | Unused | 抽奖活动展示、状态或异常文案。 |  |
| 保底规则 | Fallback rule | 抽奖活动展示、状态或异常文案。 |  |
| 时间规则 | Time rule | 抽奖活动展示、状态或异常文案。 |  |
| 权重规则 | Weight rule | 抽奖活动展示、状态或异常文案。 |  |
| 配额规则 | Quota rule | 抽奖活动展示、状态或异常文案。 |  |
| 发放优惠券失败 | Failed to issue coupon | 抽奖活动展示、状态或异常文案。 |  |
| 扣减奖品库存失败 | Failed to deduct award stock | 抽奖活动展示、状态或异常文案。 |  |
| Codex压测兜底奖 | Codex benchmark fallback award | 抽奖活动展示、状态或异常文案。 |  |
| Codex压测实物奖 | Codex benchmark physical award | 抽奖活动展示、状态或异常文案。 |  |
| 不能小于0 | cannot be less than 0 | 抽奖活动展示、状态或异常文案。 |  |
| 唯一话题ID已存在，请更换后重试 | Unique topic ID already exists. Please change it and try again | 抽奖活动展示、状态或异常文案。 |  |
| 奖品不存在 | Award does not exist | 抽奖活动展示、状态或异常文案。 |  |
| 必须大于0 | must be greater than 0 | 抽奖活动展示、状态或异常文案。 |  |
| 活动ID已存在，请更换后重试 | Activity ID already exists. Please change it and try again | 抽奖活动展示、状态或异常文案。 |  |
| 活动奖品已存在，请勿重复创建 | Activity award already exists. Do not create it again | 抽奖活动展示、状态或异常文案。 |  |
| 用户列表不能为空 | User list cannot be empty | 抽奖活动展示、状态或异常文案。 |  |
| 至少需要一个奖品权重大于0 | At least one award weight must be greater than 0 | 抽奖活动展示、状态或异常文案。 |  |
| 规则不存在 | Rule does not exist | 抽奖活动展示、状态或异常文案。 |  |
| 抽奖次数不足 | Insufficient draw chances | 抽奖活动展示、状态或异常文案。 |  |
| 奖品已发放完毕 | Awards have all been issued | 抽奖活动展示、状态或异常文案。 |  |
| 抽奖异常 | Lottery draw exception | 抽奖活动展示、状态或异常文案。 |  |
| 抽奖异常, actId= | Lottery draw exception, actId= | 抽奖活动展示、状态或异常文案。 |  |
| 抽奖异常，请重试 | Lottery draw error, please retry | 抽奖活动展示、状态或异常文案。 |  |
| 操作过于频繁，请稍后重试 | Operations are too frequent. Please try again later | 抽奖活动展示、状态或异常文案。 |  |
| 活动不存在或无奖品配置 | Activity does not exist or has no award configuration | 抽奖活动展示、状态或异常文案。 |  |
| 请求处理中，请勿重复请求 | Request is being processed. Do not submit duplicate requests | 抽奖活动展示、状态或异常文案。 |  |
| 上传失败 | Upload failed | 服务层异常或业务提示文案。 |  |
| 上传失败，未返回TOS地址 | Upload failed; TOS URL was not returned | 服务层异常或业务提示文案。 |  |
| 下载图片失败，图片数据为空 | Failed to download image; image data is empty | 服务层异常或业务提示文案。 |  |
| 图片URL为空 | Image URL is empty | 服务层异常或业务提示文案。 |  |
| 转换图片文件失败 | Failed to convert image file | 服务层异常或业务提示文案。 |  |
| ${content:@%s通过单件购买下单了您的作品，您将获得20%%的分成。每季度统一结算分成，详情可咨询客服} | ${content:@%s purchased your work as a single item. You will receive a 20%% revenue share, settled quarterly. Contact customer service for details} | 消息、推送或微信触达文案。 |  |
| ${content:你的实物奖励到了噢～快发帖给造好物其他小伙伴嘚瑟下吧！} | ${content:Your physical reward has arrived. Post it and show it to other Zaohaowu creators!} | 消息、推送或微信触达文案。 |  |
| ${title:作者大大，来show一下你赢的实物好物吧！} | ${title:Creator, show off the physical prize you won!} | 消息、推送或微信触达文案。 |  |
| 帖子 | Post | 消息、推送或微信触达文案。 |  |
| 评论 | Comment | 消息、推送或微信触达文案。 |  |
| 互动通知 | Interaction Notifications | 消息、推送或微信触达文案。 |  |
| 关注作者的更新 | Updates from Followed Creators | 消息、推送或微信触达文案。 |  |
| 创作者权益通知 | Creator Benefits Notifications | 消息、推送或微信触达文案。 |  |
| 想要互动 | Want It Interactions | 消息、推送或微信触达文案。 |  |
| 想要做货/玩法重要节点通知 | Want It Production and Key Gameplay Notifications | 消息、推送或微信触达文案。 |  |
| 新增关注 | New Followers | 消息、推送或微信触达文案。 |  |
| 服务通知 | Service Notifications | 消息、推送或微信触达文案。 |  |
| 社区内容通知 | Community Content Notifications | 消息、推送或微信触达文案。 |  |
| 订单信息 | Order Information | 消息、推送或微信触达文案。 |  |
| 运营活动通知 | Campaign Notifications | 消息、推送或微信触达文案。 |  |
| 普通优先级 | Normal priority | 消息、推送或微信触达文案。 |  |
| 最高优先级 | Highest priority | 消息、推送或微信触达文案。 |  |
| Android端 | Android | 消息、推送或微信触达文案。 |  |
| App端 | App | 消息、推送或微信触达文案。 |  |
| Web和小程序端 | Web and Miniapp | 消息、推送或微信触达文案。 |  |
| Web端 | Web | 消息、推送或微信触达文案。 |  |
| iOS端 | iOS | 消息、推送或微信触达文案。 |  |
| 全端 | All platforms | 消息、推送或微信触达文案。 |  |
| 小程序端 | Miniapp | 消息、推送或微信触达文案。 |  |
| 任务完成：成功%d人，重复%d人，失败%d人，跳过%d人，总参与%d人 | Task completed: success %d, duplicate %d, failed %d, skipped %d, total participants %d | 消息、推送或微信触达文案。 |  |
| 已处理 %d 人 | Processed %d users | 消息、推送或微信触达文案。 |  |
| 点赞差值信息缺失 | Like gap information is missing | 消息、推送或微信触达文案。 |  |
| 赞,阈值 | likes, threshold | 消息、推送或微信触达文案。 |  |
| 距获奖门槛差距过大( | Too far from award threshold ( | 消息、推送或微信触达文案。 |  |
| 场景标题或内容未配置 | Scenario title or content is not configured | 消息、推送或微信触达文案。 |  |
| 场景配置不存在: | Scenario config does not exist: | 消息、推送或微信触达文案。 |  |
| 场景配置为空 | Scenario config is empty | 消息、推送或微信触达文案。 |  |
| 推送总开关未开启 | Global push switch is not enabled | 消息、推送或微信触达文案。 |  |
| ...用户 | ... user | 消息、推送或微信触达文案。 |  |
| ...等用户 | ... and others | 消息、推送或微信触达文案。 |  |
| 个赞，快来看看吧！ | likes yesterday. Come take a look! | 消息、推送或微信触达文案。 |  |
| 位用户 | users | 消息、推送或微信触达文案。 |  |
| 你昨日累计收到了 | You received | 消息、推送或微信触达文案。 |  |
| 处理用户数: %d, 成功: %d, 失败: %d | Processed users: %d, success: %d, failed: %d | 消息、推送或微信触达文案。 |  |
| 推送成功率中等 | Medium push success rate | 消息、推送或微信触达文案。 |  |
| 推送成功率低 | Low push success rate | 消息、推送或微信触达文案。 |  |
| 推送成功率高 | High push success rate | 消息、推送或微信触达文案。 |  |
| 每日点赞推送任务 | Daily like push task | 消息、推送或微信触达文案。 |  |
| 用户 | user | 消息、推送或微信触达文案。 |  |
| 等用户 | and others | 消息、推送或微信触达文案。 |  |
| 【Push服务】 | [Push Service] | 消息、推送或微信触达文案。 |  |
| 不是客户端 | Not a client | 消息、推送或微信触达文案。 |  |
| 参数错误 | Parameter error | 消息、推送或微信触达文案。 |  |
| 奖励日期已过或超过当天最晚发送时间，不发送短信 | Reward date has passed or the latest send time of the day has been exceeded; SMS will not be sent | 消息、推送或微信触达文案。 |  |
| 恭喜，你的作品已被超过%s位用户表示想要购买！ | Congratulations, more than %s users have shown interest in buying your work! | 消息、推送或微信触达文案。 |  |
| 【%s】短信发送失败:businessId:%s, templateId:%s, params:%s, sendResultDto:%s | [%s] SMS send failed: businessId:%s, templateId:%s, params:%s, sendResultDto:%s | 消息、推送或微信触达文案。 |  |
| 【%s】通知类型错误 | Invalid notification type: [%s] | 消息、推送或微信触达文案。 |  |
| 商品ID[%s]不能为空 | Product ID [%s] cannot be empty | 消息、推送或微信触达文案。 |  |
| 手机号不能为空 | Phone number cannot be empty | 消息、推送或微信触达文案。 |  |
| 未查询到用户[%s]的手机号，短信通知异常 | Phone number for user [%s] was not found; SMS notification exception | 消息、推送或微信触达文案。 |  |
| 用户[%s]不存在 | User [%s] does not exist | 消息、推送或微信触达文案。 |  |
| 用户[%s]在手机号绑定白名单，跳过短信通知 | User [%s] is in the phone binding whitelist; skip SMS notification | 消息、推送或微信触达文案。 |  |
| 用户[%s]未绑定手机号，短信通知异常 | User [%s] has not bound a phone number; SMS notification exception | 消息、推送或微信触达文案。 |  |
| 短信模板ID不能为空 | SMS template ID cannot be empty | 消息、推送或微信触达文案。 |  |
| 话题ID[%s]不能为空 | Topic ID [%s] cannot be empty | 消息、推送或微信触达文案。 |  |
| 通知类型不能为空 | Notification type cannot be empty | 消息、推送或微信触达文案。 |  |
| 多路召回帖子数量过少， | Multi-route recalled post count is too low, | jjewelry-java 业务文案。 |  |
| 新用户回帖子数量过少， | New user recalled post count is too low, | jjewelry-java 业务文案。 |  |
| 作品上架提醒 | Work listing reminder | Nacos 配置默认展示文案。 |  |
| 查看商品页面>> | View product page >> | Nacos 配置默认展示文案。 |  |
| 🎉 恭喜您！您的作品已有超%s位朋友想要购买，成功上架啦！作为作者专享权益，您可以用专属优惠带走自己的作品～赶紧前往商店下单吧！ | Congratulations! More than %s friends want to buy your work, and it has been successfully listed. As an author-exclusive benefit, you can purchase your own work with an exclusive discount. Go to the store and place an order now! | Nacos 配置默认展示文案。 |  |
| pgc用户 | PGC user | 业务枚举展示名。 |  |
| 想要过 | Wanted | 业务枚举展示名。 |  |
| 点赞过 | Liked | 业务枚举展示名。 |  |
| 进入主页过 | Visited profile | 业务枚举展示名。 |  |
| Feed响应延迟过高 - %s | High feed response latency - %s | 服务层异常或业务提示文案。 |  |
| Feed服务异常 - %s | Feed service exception - %s | 服务层异常或业务提示文案。 |  |
| Feed类型: %s<br>错误类型: %s<br>错误详情: %s | Feed type: %s<br>Error type: %s<br>Error details: %s | 服务层异常或业务提示文案。 |  |
| Redis查询延迟过高 | Redis query latency too high | 服务层异常或业务提示文案。 |  |
| 关注用户数量过高 - 用户: %s | Following user count too high - user: %s | 服务层异常或业务提示文案。 |  |
| 个想要贴前插入 | insert before Want It post | 服务层异常或业务提示文案。 |  |
| 个想要贴加入最后 | Want It posts added to the end | 服务层异常或业务提示文案。 |  |
| 个非想要贴; | non-Want It posts; | 服务层异常或业务提示文案。 |  |
| 剩余 | remaining | 服务层异常或业务提示文案。 |  |
| 第 | No. | 服务层异常或业务提示文案。 |  |
| 暂不支持该排序方式: | Unsupported sort type: | 服务层异常或业务提示文案。 |  |
| 话题ID不能为空 | Topic ID cannot be empty | 商品业务展示、状态或异常文案。 |  |
| \uD83C\uDF89恭喜你上榜，奖励你额外%d次「⚡\uFE0F想要」！你是造好物本日最热创作者之一！继续加油，官方看好你～～ | \uD83C\uDF89 Congratulations on making the ranking! You have earned %d extra Want Its. You are one of Zaohaowu's hottest creators today. Keep going! | 排行榜业务展示或筛选文案。 |  |
| \uD83D\uDD25本日Top3最热创作者就是你，奖励你额外%d次「⚡\uFE0F想要」！再接再厉，官方想持续看到你～ | \uD83D\uDD25 You are one of today's Top 3 hottest creators! You have earned %d extra Want Its. Keep it up! | 排行榜业务展示或筛选文案。 |  |
| \uD83E\uDD29哇塞！造好物本周最热创作者榜单有你一席！奖励你额外%d次「⚡\uFE0F想要」！快跟官方一起见证你的荣誉时刻～ | \uD83E\uDD29 Wow! You made Zaohaowu's hottest creator ranking this week and earned %d extra Want Its. Celebrate this moment! | 排行榜业务展示或筛选文案。 |  |
| \uD83E\uDD29太牛了！你荣登造好物本周Top3最热创作者！！奖励你额外%d次「⚡\uFE0F想要」！快来打卡你的荣誉时刻吧\uD83C\uDFC6～ | \uD83E\uDD29 Amazing! You made Zaohaowu's Top 3 hottest creators this week and earned %d extra Want Its. Come celebrate your moment\uD83C\uDFC6! | 排行榜业务展示或筛选文案。 |  |
| 刚刚发布，抢1折购买席 | Just published, grab 90% off purchase slots | 排行榜业务展示或筛选文案。 |  |
| 马上集满，最后名额折扣买 | Almost full, last slots discounted | 排行榜业务展示或筛选文案。 |  |
| 帖子 | Post | 业务枚举展示名。 |  |
| <br>举报人: | <br>Reporter: | 服务层异常或业务提示文案。 |  |
| 举报 | Report | 服务层异常或业务提示文案。 |  |
| 举报原因: | Report reason: | 服务层异常或业务提示文案。 |  |
| 其他说明: | Additional notes: | 服务层异常或业务提示文案。 |  |
| 用户举报通知 | User report notification | 服务层异常或业务提示文案。 |  |
| 目标ID: | Target ID: | 服务层异常或业务提示文案。 |  |
| 目标类型: | Target type: | 服务层异常或业务提示文案。 |  |
| 话题内容 | Topic content | 审核业务状态或结果文案。 |  |
| 拦截 | Blocked | 审核业务状态或结果文案。 |  |
| 未审核 | Not reviewed | 审核业务状态或结果文案。 |  |
| 通过 | Approved | 审核业务状态或结果文案。 |  |
| 未成年人审核 | Minor review | 审核业务状态或结果文案。 |  |
| 精确+模糊复合召回策略 - 单查询同时执行精确和模糊匹配 | Exact + fuzzy composite recall strategy - executes exact and fuzzy matching in a single query | 搜索业务展示或配置文案。 |  |
| 默认召回策略配置 | Default recall strategy config | 搜索业务展示或配置文案。 |  |
| 低门槛做货结束倒计时：%d天 | Low-threshold production countdown: %d days | Topic/内容业务展示、状态或异常文案。 |  |
| 活动已结束 | Activity has ended | Topic/内容业务展示、状态或异常文案。 |  |
| 活动未开始 | Activity has not started | Topic/内容业务展示、状态或异常文案。 |  |
| 获奖名单已公示 | Winner list published | Topic/内容业务展示、状态或异常文案。 |  |
| 评奖中 | Judging | Topic/内容业务展示、状态或异常文案。 |  |
| 限时活动：%d天后截稿 | Limited-time activity: submission closes in %d days | Topic/内容业务展示、状态或异常文案。 |  |
| 制作中待上架 | In production, pending listing | Topic/内容业务展示、状态或异常文案。 |  |
| 实物已上架 | Physical product listed | Topic/内容业务展示、状态或异常文案。 |  |
| 限时挑战弹窗弹窗 | Limited-time challenge popup | Topic/内容业务展示、状态或异常文案。 |  |
| 参数错误 | Parameter error | Topic/内容业务展示、状态或异常文案。 |  |
| 该作品已经在收集想要啦 | This work is already collecting Wants | Topic/内容业务展示、状态或异常文案。 |  |
| 请重新选择挑战赛 | Please select the challenge again | Topic/内容业务展示、状态或异常文案。 |  |
| App端 | App | Topic/内容业务展示、状态或异常文案。 |  |
| Web端 | Web | Topic/内容业务展示、状态或异常文案。 |  |
| 全部端 | All platforms | Topic/内容业务展示、状态或异常文案。 |  |
| 小程序端 | Miniapp | Topic/内容业务展示、状态或异常文案。 |  |
| 普通 | Regular | Topic/内容业务展示、状态或异常文案。 |  |
| 忘记了 | Forgotten | Topic/内容业务展示、状态或异常文案。 |  |
| batch 内 contentId 重复 | Duplicate contentId in batch | Topic/内容业务展示、状态或异常文案。 |  |
| items 为空 | items is empty | Topic/内容业务展示、状态或异常文案。 |  |
| 单次最多 | Maximum per batch: | Topic/内容业务展示、状态或异常文案。 |  |
| 存在已有的 cid 或缺失的 cid，请修正后重试 | Existing or missing cids found. Please fix and retry | Topic/内容业务展示、状态或异常文案。 |  |
| 条，实际 | items, actual | Topic/内容业务展示、状态或异常文案。 |  |
| CTR未达标 | CTR did not meet target | Topic/内容业务展示、状态或异常文案。 |  |
| CTR达标 | CTR met target | Topic/内容业务展示、状态或异常文案。 |  |
| 互动率未达标 | Interaction rate did not meet target | Topic/内容业务展示、状态或异常文案。 |  |
| 互动率达标 | Interaction rate met target | Topic/内容业务展示、状态或异常文案。 |  |
| 当前内容池热门内容数量：%d | Current hot content count in content pool: %d | Topic/内容业务展示、状态或异常文案。 |  |
| 使用同款模型 | Use the same model | Topic/内容业务展示、状态或异常文案。 |  |
| 去创作 | Create | Topic/内容业务展示、状态或异常文案。 |  |
| 已关闭不允许开启 | Already disabled; enabling is not allowed | Topic/内容业务展示、状态或异常文案。 |  |
| 帖子不存在 | Post does not exist | Topic/内容业务展示、状态或异常文案。 |  |
| 无本地上传权限 | No local upload permission | Topic/内容业务展示、状态或异常文案。 |  |
| 该挑战未开放本地上传 | Local upload is not enabled for this challenge | Topic/内容业务展示、状态或异常文案。 |  |
| 帖子被删除 | Post has been deleted | Topic/内容业务展示、状态或异常文案。 |  |
| 【订单服务】 | [Order Service] | jjewelry-java 业务文案。 |  |
| 【电商服务】 | [E-commerce Service] | 商品业务展示、状态或异常文案。 |  |
| 已绑定 | Bound | 用户业务展示、状态或异常文案。 |  |
| 未绑定 | Not bound | 用户业务展示、状态或异常文案。 |  |
| 白名单无需绑定 | Whitelist does not require binding | 用户业务展示、状态或异常文案。 |  |
| 人工授予 | Manually granted | 用户业务展示、状态或异常文案。 |  |
| 活动获得 | Obtained from activity | 用户业务展示、状态或异常文案。 |  |
| 算法推荐 | Algorithm recommendation | 用户业务展示、状态或异常文案。 |  |
| 系统自动 | System automatic | 用户业务展示、状态或异常文案。 |  |
| 购买获得 | Obtained by purchase | 用户业务展示、状态或异常文案。 |  |
| 邀请获得 | Obtained by invitation | 用户业务展示、状态或异常文案。 |  |
| 指定用户UserThirdParty数据同步失败: | Specified users' UserThirdParty data sync failed: | jjewelry-java 控制器/API 运行时响应或参数文案。 |  |
| scenarioType 和 pushId 不能为空 | scenarioType and pushId cannot be empty | 用户业务展示、状态或异常文案。 |  |
| 参数错误 | Parameter error | 用户业务展示、状态或异常文案。 |  |
| 参数错误：未指定发放用户，且未开启全量发放 | Parameter error: no target users specified and full grant is not enabled | 用户业务展示、状态或异常文案。 |  |
| 微信小程序登录 | WeChat Miniapp login | 用户业务展示、状态或异常文案。 |  |
| 微信开放平台登录 | WeChat Open Platform login | 用户业务展示、状态或异常文案。 |  |
| 手机登录 | Mobile login | 用户业务展示、状态或异常文案。 |  |
| smsId 为空 | smsId is empty | 用户业务展示、状态或异常文案。 |  |
| 任务不存在或已过期 | Task does not exist or has expired | 用户业务展示、状态或异常文案。 |  |
| 任务参数不能为空 | Task parameters cannot be empty | 用户业务展示、状态或异常文案。 |  |
| 任务完成：成功%d人，重复%d人，失败%d人，跳过%d人 | Task completed: %d succeeded, %d duplicated, %d failed, %d skipped | 用户业务展示、状态或异常文案。 |  |
| 任务完成：未指定有效用户 | Task completed: no valid users specified | 用户业务展示、状态或异常文案。 |  |
| 任务已被手动取消 | Task has been manually canceled | 用户业务展示、状态或异常文案。 |  |
| 任务并发冲突，请稍后重试 | Task concurrency conflict, please try again later | 用户业务展示、状态或异常文案。 |  |
| 全量发放进度：已处理 %d/%d | Full grant progress: processed %d/%d | 用户业务展示、状态或异常文案。 |  |
| 存在未结束的任务，无法启动新任务。未结束的任务ID: %s | There are unfinished tasks, so a new task cannot be started. Unfinished task IDs: %s | 用户业务展示、状态或异常文案。 |  |
| 已处理 %d/%d | Processed %d/%d | 用户业务展示、状态或异常文案。 |  |
| 幂等键生成失败 | Failed to generate idempotency key | 用户业务展示、状态或异常文案。 |  |
| 当前已有任务进行中，taskId=%s | A task is already running, taskId=%s | 用户业务展示、状态或异常文案。 |  |
| 批量去重失败，已跳过本批次 | Batch deduplication failed. This batch was skipped | 用户业务展示、状态或异常文案。 |  |
| 用户ID为空 | User ID is empty | 用户业务展示、状态或异常文案。 |  |
| 用户不存在或未记录活跃时间 | User does not exist or active time was not recorded | 用户业务展示、状态或异常文案。 |  |
| 用户已处理 | User already processed | 用户业务展示、状态或异常文案。 |  |
| 用户最近%d天内未活跃 | User has not been active in the last %d days | 用户业务展示、状态或异常文案。 |  |
| 用户活跃时间解析失败 | Failed to parse user active time | 用户业务展示、状态或异常文案。 |  |
| 未查询到手机号 | Phone number not found | 用户业务展示、状态或异常文案。 |  |
| 未查询到短信模板参数 | SMS template parameters not found | 用户业务展示、状态或异常文案。 |  |
| ByteHouse 未启用，无法查询「注册第二日未活跃用户」，本次跳过。 | ByteHouse is not enabled. Cannot query second-day inactive registered users. Skipped this time. | 用户业务展示、状态或异常文案。 |  |
| REGISTER_SECOND_DAY_INACTIVE_REMIND 任务服务未注册，「注册第二日未活跃」短信本次跳过。 | REGISTER_SECOND_DAY_INACTIVE_REMIND task service is not registered. Second-day inactive registration SMS skipped this time. | 用户业务展示、状态或异常文案。 |  |
| 「注册第二日未活跃」候选用户 %d 超过上限 %d，已截断，请确认口径。 | Second-day inactive registration candidate users %d exceeded limit %d and were truncated. Please confirm the criteria. | 用户业务展示、状态或异常文案。 |  |
| 「注册第二日未活跃」每日 SMS 任务执行失败： | Second-day inactive registration daily SMS task failed: | 用户业务展示、状态或异常文案。 |  |
| 操作人不能为空 | Operator cannot be empty | 用户业务展示、状态或异常文案。 |  |
| 操作人无权限执行用户注销操作: | Operator has no permission to close user account: | 用户业务展示、状态或异常文案。 |  |
| 更新发帖内容失败: | Failed to update post content: | 用户业务展示、状态或异常文案。 |  |
| 注销失败: | Account closure failed: | 用户业务展示、状态或异常文案。 |  |
| 注销用户失败: | Failed to close user account: | 用户业务展示、状态或异常文案。 |  |
| 用户ID不能为空 | User ID cannot be empty | 用户业务展示、状态或异常文案。 |  |
| 用户中心删除用户失败 | User center failed to delete user | 用户业务展示、状态或异常文案。 |  |
| 用户中心注销失败 | User center account closure failed | 用户业务展示、状态或异常文案。 |  |
| 添加用户到Nacos白名单失败, dataId= | Failed to add user to Nacos whitelist, dataId= | 用户业务展示、状态或异常文案。 |  |
| 折扣 | Discount | Want It 业务展示、风控或异常文案。 |  |
| 直减 | Fixed discount | Want It 业务展示、风控或异常文案。 |  |
| 正常 | Normal | Want It 业务展示、风控或异常文案。 |  |
| 参数错误：未指定发放用户，且未开启全量发放 | Parameter error: no target users specified and full grant is not enabled | Want It 业务展示、风控或异常文案。 |  |
| 超过当天最晚发送时间，不发送短信 | Latest send time of the day exceeded; SMS will not be sent | Want It 业务展示、风控或异常文案。 |  |
| 【%s】请配置商品详情页 | [%s] Please configure the product detail page | Want It 业务展示、风控或异常文案。 |  |
| 【%s】请配置并上架商品详情页 | [%s] Please configure and publish the product detail page | Want It 业务展示、风控或异常文案。 |  |
| 去配置 | Configure | Want It 业务展示、风控或异常文案。 |  |
| 该作品符合生产要求，且满足限时活动下Top点赞要求，请<at user_id=\"%s\"></at>创建商品详情页，编写商品信息，完成上架。请<at user_id=\"%s\"></at>跟进做货流程，及时同步做货进度，更新素材同步至<at user_id=\"%s\"></at>。 | This work meets production requirements and satisfies the Top-like requirement for the limited-time activity. Please <at user_id=\"%s\"></at> create the product detail page, write product information, and publish it. Please <at user_id=\"%s\"></at> follow the production process, sync progress in time, and update materials to <at user_id=\"%s\"></at>. | Want It 业务展示、风控或异常文案。 |  |
| 该作品符合生产要求，请<at user_id=\"%s\"></at>提前创建商品详情页，编写商品信息。 | This work meets production requirements. Please <at user_id=\"%s\"></at> create the product detail page and write product information in advance. | Want It 业务展示、风控或异常文案。 |  |
| Excel导出失败: | Excel export failed: | Want It 业务展示、风控或异常文案。 |  |
| 中风险 | Medium risk | Want It 业务展示、风控或异常文案。 |  |
| 否 | No | Want It 业务展示、风控或异常文案。 |  |
| 处理异常: | Processing exception: | Want It 业务展示、风控或异常文案。 |  |
| 导出失败: | Export failed: | Want It 业务展示、风控或异常文案。 |  |
| 帖子ID | Post ID | Want It 业务展示、风控或异常文案。 |  |
| 帖子来源 | Post source | Want It 业务展示、风控或异常文案。 |  |
| 手机号 | Phone number | Want It 业务展示、风控或异常文案。 |  |
| 批量手机号风险检测失败: | Batch phone risk check failed: | Want It 业务展示、风控或异常文案。 |  |
| 批量用户ID风险检测失败: | Batch user ID risk check failed: | Want It 业务展示、风控或异常文案。 |  |
| 投票时间 | Vote time | Want It 业务展示、风控或异常文案。 |  |
| 无手机号或检测失败 | No phone number or detection failed | Want It 业务展示、风控或异常文案。 |  |
| 是 | Yes | Want It 业务展示、风控或异常文案。 |  |
| 是否在黑名单 | In blacklist | Want It 业务展示、风控或异常文案。 |  |
| 未知 | Unknown | Want It 业务展示、风控或异常文案。 |  |
| 极高风险 | Critical risk | Want It 业务展示、风控或异常文案。 |  |
| 标签 | Tags | Want It 业务展示、风控或异常文案。 |  |
| 标签说明 | Tag description | Want It 业务展示、风控或异常文案。 |  |
| 检测异常: | Detection exception: | Want It 业务展示、风控或异常文案。 |  |
| 检测时间 | Check time | Want It 业务展示、风控或异常文案。 |  |
| 用户ID | User ID | Want It 业务展示、风控或异常文案。 |  |
| 策略描述 | Strategy description | Want It 业务展示、风控或异常文案。 |  |
| 详细信息 | Details | Want It 业务展示、风控或异常文案。 |  |
| 错误信息 | Error message | Want It 业务展示、风控或异常文案。 |  |
| 风险分 | Risk score | Want It 业务展示、风控或异常文案。 |  |
| 风险分数 | Risk score | Want It 业务展示、风控或异常文案。 |  |
| 风险描述 | Risk description | Want It 业务展示、风控或异常文案。 |  |
| 风险标签 | Risk tags | Want It 业务展示、风控或异常文案。 |  |
| 风险等级 | Risk level | Want It 业务展示、风控或异常文案。 |  |
| 高风险 | High risk | Want It 业务展示、风控或异常文案。 |  |
| 新人wantIt活动 | New user Want It activity | Want It 业务展示、风控或异常文案。 |  |
| 用户id: %s，日期: %s，类型: %s/%s，阈值: %d，当前值: %d | User ID: %s, date: %s, type: %s/%s, threshold: %d, current value: %d | Want It 业务展示、风控或异常文案。 |  |
| 老用户wantIt活动 | Existing user Want It activity | Want It 业务展示、风控或异常文案。 |  |
| 、购买%d件想要商品 | and purchase %d Want products | Want It 业务展示、风控或异常文案。 |  |
| 要%s | needs %s | Want It 业务展示、风控或异常文案。 |  |
| API响应为空 | API response is empty | Want It 业务展示、风控或异常文案。 |  |
| API响应数据为空 | API response data is empty | Want It 业务展示、风控或异常文案。 |  |
| API调用失败: | API call failed: | Want It 业务展示、风控或异常文案。 |  |
| IP异常聚集 | IP anomaly cluster | Want It 业务展示、风控或异常文案。 |  |
| IP环境异常 | IP environment anomaly | Want It 业务展示、风控或异常文案。 |  |
| IP行为异常 | IP behavior anomaly | Want It 业务展示、风控或异常文案。 |  |
| IP黑名单 | IP blacklist | Want It 业务展示、风控或异常文案。 |  |
| 仿冒欺诈风险 | Impersonation fraud risk | Want It 业务展示、风控或异常文案。 |  |
| 伪造虚假设备 | Fake device | Want It 业务展示、风控或异常文案。 |  |
| 充值欺诈风险 | Top-up fraud risk | Want It 业务展示、风控或异常文案。 |  |
| 内容导流风险 | Content traffic diversion risk | Want It 业务展示、风控或异常文案。 |  |
| 内容欺诈风险 | Content fraud risk | Want It 业务展示、风控或异常文案。 |  |
| 广告导流手机号 | Ad traffic diversion phone number | Want It 业务展示、风控或异常文案。 |  |
| 广告导流账号 | Ad traffic diversion account | Want It 业务展示、风控或异常文案。 |  |
| 异常广告点击 | Abnormal ad click | Want It 业务展示、风控或异常文案。 |  |
| 异常渠道激活 | Abnormal channel activation | Want It 业务展示、风控或异常文案。 |  |
| 异常退款风险 | Abnormal refund risk | Want It 业务展示、风控或异常文案。 |  |
| 引流欺诈风险 | Traffic diversion fraud risk | Want It 业务展示、风控或异常文案。 |  |
| 情感欺诈风险 | Emotional fraud risk | Want It 业务展示、风控或异常文案。 |  |
| 手机号异常聚集 | Phone number anomaly cluster | Want It 业务展示、风控或异常文案。 |  |
| 手机号状态异常 | Phone number status anomaly | Want It 业务展示、风控或异常文案。 |  |
| 手机号行为异常 | Phone number behavior anomaly | Want It 业务展示、风控或异常文案。 |  |
| 手机号风险 | Phone number risk | Want It 业务展示、风控或异常文案。 |  |
| 手机号黑名单 | Phone number blacklist | Want It 业务展示、风控或异常文案。 |  |
| 批量注册账号风险 | Batch registration account risk | Want It 业务展示、风控或异常文案。 |  |
| 接入参数异常 | Access parameter anomaly | Want It 业务展示、风控或异常文案。 |  |
| 未知错误 | Unknown error | Want It 业务展示、风控或异常文案。 |  |
| 杀猪盘手机号 | Romance scam phone number | Want It 业务展示、风控或异常文案。 |  |
| 杀猪盘账号 | Romance scam account | Want It 业务展示、风控或异常文案。 |  |
| 欺诈黑名单 | Fraud blacklist | Want It 业务展示、风控或异常文案。 |  |
| 用户小号风险 | Alt account risk | Want It 业务展示、风控或异常文案。 |  |
| 疑似录播 | Suspected recorded broadcast | Want It 业务展示、风控或异常文案。 |  |
| 疑似爬虫风险 | Suspected crawler risk | Want It 业务展示、风控或异常文案。 |  |
| 疑似黄牛党 | Suspected scalper | Want It 业务展示、风控或异常文案。 |  |
| 自定义白名单 | Custom whitelist | Want It 业务展示、风控或异常文案。 |  |
| 自定义黑名单 | Custom blacklist | Want It 业务展示、风控或异常文案。 |  |
| 致富欺诈风险 | Get-rich fraud risk | Want It 业务展示、风控或异常文案。 |  |
| 营销活动作弊风险 | Marketing campaign cheating risk | Want It 业务展示、风控或异常文案。 |  |
| 营销活动真人众包 | Marketing campaign human crowdsourcing | Want It 业务展示、风控或异常文案。 |  |
| 营销活动薅羊毛 | Marketing campaign abuse | Want It 业务展示、风控或异常文案。 |  |
| 虚假刷量风险 | Fake engagement boosting risk | Want It 业务展示、风控或异常文案。 |  |
| 设备参数篡改 | Device parameter tampering | Want It 业务展示、风控或异常文案。 |  |
| 设备多开作弊 | Device multi-instance cheating | Want It 业务展示、风控或异常文案。 |  |
| 设备定制ROM | Device custom ROM | Want It 业务展示、风控或异常文案。 |  |
| 设备抹机异常 | Device wipe anomaly | Want It 业务展示、风控或异常文案。 |  |
| 设备指纹异常 | Device fingerprint anomaly | Want It 业务展示、风控或异常文案。 |  |
| 设备模拟点击 | Device simulated click | Want It 业务展示、风控或异常文案。 |  |
| 设备环境异常 | Device environment anomaly | Want It 业务展示、风控或异常文案。 |  |
| 设备硬件异常 | Device hardware anomaly | Want It 业务展示、风控或异常文案。 |  |
| 设备网络异常 | Device network anomaly | Want It 业务展示、风控或异常文案。 |  |
| 设备聚集异常 | Device cluster anomaly | Want It 业务展示、风控或异常文案。 |  |
| 设备脚本作弊 | Device script cheating | Want It 业务展示、风控或异常文案。 |  |
| 设备行为异常 | Device behavior anomaly | Want It 业务展示、风控或异常文案。 |  |
| 设备调试工具 | Device debugging tool | Want It 业务展示、风控或异常文案。 |  |
| 设备越狱破解 | Device jailbreak/root | Want It 业务展示、风控或异常文案。 |  |
| 设备黑名单 | Device blacklist | Want It 业务展示、风控或异常文案。 |  |
| 账号异常聚集 | Account anomaly cluster | Want It 业务展示、风控或异常文案。 |  |
| 账号环境异常 | Account environment anomaly | Want It 业务展示、风控或异常文案。 |  |
| 账户行为风险 | Account behavior risk | Want It 业务展示、风控或异常文案。 |  |
| 账户黑名单 | Account blacklist | Want It 业务展示、风控或异常文案。 |  |
| 转账安全风险 | Transfer security risk | Want It 业务展示、风控或异常文案。 |  |
| 转账欺诈风险 | Transfer fraud risk | Want It 业务展示、风控或异常文案。 |  |
| 邮箱域名异常 | Email domain anomaly | Want It 业务展示、风控或异常文案。 |  |
| 任务不存在或已过期 | Task does not exist or has expired | Want It 业务展示、风控或异常文案。 |  |
| 任务参数不能为空 | Task parameters cannot be empty | Want It 业务展示、风控或异常文案。 |  |
| 任务完成：成功%d人，重复%d人，失败%d人，跳过%d人 | Task completed: %d succeeded, %d duplicated, %d failed, %d skipped | Want It 业务展示、风控或异常文案。 |  |
| 任务完成：未指定有效用户 | Task completed: no valid users specified | Want It 业务展示、风控或异常文案。 |  |
| 任务已被手动取消 | Task has been manually canceled | Want It 业务展示、风控或异常文案。 |  |
| 任务并发冲突，请稍后重试 | Task concurrency conflict, please try again later | Want It 业务展示、风控或异常文案。 |  |
| 全量发放进度：已处理 %d/%d | Full grant progress: processed %d/%d | Want It 业务展示、风控或异常文案。 |  |
| 存在未结束的任务，无法启动新任务。未结束的任务ID: %s | There are unfinished tasks, so a new task cannot be started. Unfinished task IDs: %s | Want It 业务展示、风控或异常文案。 |  |
| 已处理 %d/%d | Processed %d/%d | Want It 业务展示、风控或异常文案。 |  |
| 当前已有任务进行中，taskId=%s | A task is already running, taskId=%s | Want It 业务展示、风控或异常文案。 |  |
| 用户ID为空 | User ID is empty | Want It 业务展示、风控或异常文案。 |  |
| 用户不存在或未记录活跃时间 | User does not exist or active time was not recorded | Want It 业务展示、风控或异常文案。 |  |
| 用户存在未过期的注册发放记录 | User has an unexpired registration grant record | Want It 业务展示、风控或异常文案。 |  |
| 用户最近%d天内未活跃 | User has not been active in the last %d days | Want It 业务展示、风控或异常文案。 |  |
| 用户活跃时间解析失败 | Failed to parse user active time | Want It 业务展示、风控或异常文案。 |  |
| 【%s】Want it作品达到%s推送 | [%s] Want work reached %s push | Want It 业务展示、风控或异常文案。 |  |
| 【%s】Want it作品达到%s推送【请加急审核⚡️】 | [%s] Want work reached %s push [urgent review requested] | Want It 业务展示、风控或异常文案。 |  |
| 【%s】用户收货地址更新 | [%s] User shipping address updated | Want It 业务展示、风控或异常文案。 |  |
| 【%s】请操作商品上架【请加急上架⚡️】 | [%s] Please publish the product [urgent] | Want It 业务展示、风控或异常文案。 |  |
| 【%s】请配置并上架商品详情页【请加急配置并上架⚡️】 | [%s] Please configure and publish the product detail page [urgent] | Want It 业务展示、风控或异常文案。 |  |
| 作品未审核，请审核生产可行性。 | Work not reviewed. Please review production feasibility. | Want It 业务展示、风控或异常文案。 |  |
| 去上架 | Publish | Want It 业务展示、风控或异常文案。 |  |
| 去审核 | Review | Want It 业务展示、风控或异常文案。 |  |
| 去查看 | View | Want It 业务展示、风控或异常文案。 |  |
| 商品未配置，请配置商品详情页。 | Product not configured. Please configure the product detail page. | Want It 业务展示、风控或异常文案。 |  |
| 用户[%s]的收货地址已更新，请<at user_id=\"%s\"></at>准备发货，请<at user_id=\"%s\"></at>更新用户创意描述。%s | User [%s]'s shipping address has been updated. Please <at user_id=\"%s\"></at> prepare shipment, and please <at user_id=\"%s\"></at> update the user's creative description. %s | Want It 业务展示、风控或异常文案。 |  |
| 该作品符合生产要求，且达到 %s wanted，请<at user_id=\"%s\"></at>创建商品详情页，编写商品信息，完成上架。请<at user_id=\"%s\"></at>跟进做货流程，及时同步做货进度，更新素材同步至<at user_id=\"%s\"></at>。 | This work meets production requirements and has reached %s wants. Please <at user_id=\"%s\"></at> create the product detail page, write product information, and publish it. Please <at user_id=\"%s\"></at> follow the production process, sync progress in time, and update materials to <at user_id=\"%s\"></at>. | Want It 业务展示、风控或异常文案。 |  |
| 该作品达到 %s wanted，请<at user_id=\"%s\"></at>上架商品详情页，请<at user_id=\"%s\"></at>跟进做货流程，及时同步做货进度，更新素材同步至<at user_id=\"%s\"></at>。 | This work has reached %s wants. Please <at user_id=\"%s\"></at> publish the product detail page. Please <at user_id=\"%s\"></at> follow the production process, sync progress in time, and update materials to <at user_id=\"%s\"></at>. | Want It 业务展示、风控或异常文案。 |  |
| 该作品达到 %s wanted，请<at user_id=\"%s\"></at>评估生产可行性，标记是否可生产。 | This work has reached %s wants. Please <at user_id=\"%s\"></at> evaluate production feasibility and mark whether it can be produced. | Want It 业务展示、风控或异常文案。 |  |
| 小程序access_token | Mini Program access_token | 消息、推送或微信触达文案。 |  |
| 强制刷新小程序access_token | Force refresh Mini Program access_token | 消息、推送或微信触达文案。 |  |
| 不支持的时间单位: | Unsupported time unit: | jjewelry-java 业务文案。 |  |
| 时间周期字符串不能为空 | Time period string cannot be empty | jjewelry-java 业务文案。 |  |
| 时间周期格式不正确，支持格式：数字+单位(s/m/h/d/w/M/y)，例如：30s, 5m, 2h, 1d, 3w, 6M, 1y | Invalid time period format. Supported format: number + unit (s/m/h/d/w/M/y), for example: 30s, 5m, 2h, 1d, 3w, 6M, 1y | jjewelry-java 业务文案。 |  |
| ### 以下是用户输入： | ### User input: | jjewelry-java 业务文案。 |  |
| ##你是一位专业的创意总监，负责商品视频广告拍摄兼提示词优化师，你将根据用户输入的图片内容，来匹配最佳的商品广告拍摄创意，并输出优化提示词后的结果，使其更符合商品视频拍摄。根据以下规则一步步执行： <br> | ##You are a professional creative director for product video advertising and a prompt optimizer. Based on the user's input image content, match the best product advertising shooting concept and output an optimized prompt that better fits product video shooting. Follow these rules step by step: <br> | jjewelry-java 业务文案。 |  |
| 1. 识别用户输入提示词的主体和其他元素，包括：设计意象，材质组合，设计特征，视觉风格等。 <br> | 1. Identify the subject and other elements in the user's prompt, including design imagery, material combinations, design features, visual style, and more. <br> | jjewelry-java 业务文案。 |  |
| 1. 识别用户输入提示词的主体，核心意象，材质组合，设计特征，视觉风格。<br> | 1. Identify the subject, core imagery, material combinations, design features, and visual style in the user's prompt.<br> | jjewelry-java 业务文案。 |  |
| 2. 在不改变用户输入原本意图（主体，核心意象，商品类型，材质，设计特征，视觉风格）的情况下，依据主体给出最适合的动作描述，运镜，光线的描述词，可以参考以下常见的描述词。 <br> | 2. Without changing the user's original intent, subject, core imagery, product type, materials, design features, or visual style, provide the most suitable action description, camera movement, and lighting descriptors based on the subject. You may refer to the common descriptors below. <br> | jjewelry-java 业务文案。 |  |
| 2. 在不改变用户输入原本意图（主体，核心意象，商品类型，材质，设计特征，视觉风格）的情况下，依据商品描述结构（包含核心意象，商品类型，材质，设计特征，视觉风格几个部分）补充用户提示词或增加细节描述。<br> | 2. Without changing the user's original intent, subject, core imagery, product type, materials, design features, or visual style, enrich the prompt or add details according to the product description structure, including core imagery, product type, materials, design features, and visual style.<br> | jjewelry-java 业务文案。 |  |
| 3. 避免扩写技术规格词语（如8k渲染, 15cm）。 <br> | 3. Do not expand technical specification terms, such as 8k rendering or 15cm. <br> | jjewelry-java 业务文案。 |  |
| 3. 避免扩写技术规格词语（如8k渲染, 15cm）。<br> | 3. Do not expand technical specification terms, such as 8k rendering or 15cm.<br> | jjewelry-java 业务文案。 |  |
| 4. 只输出优化后的提示词，提示词和提示词之间只用逗号链接，不要出现冒号、叹号、数字。 <br> | 4. Output only the optimized prompt. Connect prompt phrases only with commas. Do not include colons, exclamation marks, or numbers. <br> | jjewelry-java 业务文案。 |  |
| 4. 只输出优化后的提示词，提示词和提示词之间只用逗号链接，不要出现冒号、叹号、数字。<br> | 4. Output only the optimized prompt. Connect prompt phrases only with commas. Do not include colons, exclamation marks, or numbers.<br> | jjewelry-java 业务文案。 |  |
| 5. 过滤生成结果中的违禁词，如政治敏感人物，色情、暴力、赌博、毒品。 <br> | 5. Filter prohibited words from the generated result, such as politically sensitive figures, pornography, violence, gambling, and drugs. <br> | jjewelry-java 业务文案。 |  |
| 5. 过滤生成结果中的违禁词，如政治敏感人物，色情、暴力、赌博、毒品。<br> | 5. Filter prohibited words from the generated result, such as politically sensitive figures, pornography, violence, gambling, and drugs.<br> | jjewelry-java 业务文案。 |  |
| 人物： 微笑、挥手、转身、奔跑、跳跃、起舞、沉思、回眸、头发飘动、衣服摆动。 <br> | People: smiling, waving, turning around, running, jumping, dancing, contemplating, looking back, hair moving, clothing swaying. <br> | jjewelry-java 业务文案。 |  |
| 你是一位专业的提示词优化师，你将根据用户提供的提示词，来解决优化提示词使其更符合商品描述形式的任务。根据以下规则一步步执行：<br> | You are a professional prompt optimizer. Based on the prompt provided by the user, optimize it so it better fits a product description format. Follow these rules step by step:<br> | jjewelry-java 业务文案。 |  |
| 光线变化： 光线逐渐增强/减弱、光影移动、颜色渐变（如从金色变为蓝色）。<br> | Light changes: gradually increasing or decreasing light, moving light and shadow, color gradients such as gold to blue.<br> | jjewelry-java 业务文案。 |  |
| 光线类型： 自然光、电影感光线、戏剧性光束、霓虹灯光、温暖夕阳、柔和晨光、阴天漫射光。 <br> | Light types: natural light, cinematic lighting, dramatic beams, neon lighting, warm sunset, soft morning light, overcast diffused light. <br> | jjewelry-java 业务文案。 |  |
| 只输出优化后的提示词，在任何情况下，都不允许在结果中输出优化思路和策略，比如说用括号说明优化思路<br> | Only output the optimized prompt. Under no circumstances may the result include optimization reasoning or strategy, such as using parentheses to explain the optimization.<br> | jjewelry-java 业务文案。 |  |
| 如果用户输入的内容包含了一些无法识别的字符，必须输出以下回答：您的输入似乎包含了一些无法识别的字符。我需要您提供关键信息，例如商品、设计元素和风格等。这将帮助我拓展您的创意～ 输出格式要求： 只输出优化后的提示词，在任何情况下，都不允许在结果中输出优化思路和策略，比如说用括号说明优化思路。提示词之间必须用逗号链接 输出的中文提示词字符数不能超过500字 满足上述所有规则 <br> | If the user's input contains unrecognizable characters, you must output the following response: Your input seems to contain unrecognizable characters. I need key information from you, such as product, design elements, and style. This will help me expand your idea. Output format requirements: only output the optimized prompt. Under no circumstances may the result include optimization reasoning or strategy, such as using parentheses to explain the optimization. Prompt phrases must be connected with commas. The output prompt must not exceed 500 Chinese characters. Satisfy all rules above. <br> | jjewelry-java 业务文案。 |  |
| 如果用户输入的内容包含了一些无法识别的字符，必须输出以下回答：您的输入似乎包含了一些无法识别的字符。我需要您提供关键信息，例如商品、设计元素和风格等。这将帮助我拓展您的创意～<br> | If the user's input contains unrecognizable characters, you must output the following response: Your input seems to contain unrecognizable characters. I need key information from you, such as product, design elements, and style. This will help me expand your idea.<br> | jjewelry-java 业务文案。 |  |
| 常见的光线控制: <br> | Common lighting controls: <br> | jjewelry-java 业务文案。 |  |
| 常见的动作描述词有: <br> | Common action descriptors include: <br> | jjewelry-java 业务文案。 |  |
| 常见的运镜描述词有: <br> | Common camera movement descriptors include: <br> | jjewelry-java 业务文案。 |  |
| 抽象： 色彩流动、几何形态变换、墨水扩散。 <br> | Abstract: color flow, geometric shape transformation, ink diffusion. <br> | jjewelry-java 业务文案。 |  |
| 推拉： 缓慢推进、平稳拉远、急速放大。 <br> | Dolly/zoom: slow push-in, smooth pull-back, rapid zoom-in. <br> | jjewelry-java 业务文案。 |  |
| 提示词之间必须用逗号链接<br> | Prompt phrases must be connected with commas.<br> | jjewelry-java 业务文案。 |  |
| 摇移： 水平横摇、垂直俯仰、跟随拍摄。 <br> | Pan/tilt: horizontal pan, vertical tilt, tracking shot. <br> | jjewelry-java 业务文案。 |  |
| 满足上述所有规则<br> | Satisfy all rules above.<br> | jjewelry-java 业务文案。 |  |
| 物体/自然： 花瓣飘落、雪花飞舞、旗帜飘扬、水流涌动、云雾流动、灯光闪烁、树叶摇曳。 <br> | Objects/nature: petals falling, snowflakes dancing, flags fluttering, water flowing, mist moving, lights flickering, leaves swaying. <br> | jjewelry-java 业务文案。 |  |
| 特殊： 轨道环绕、无人机航拍、延时摄影、动态模糊、手持抖动感。 <br> | Special: orbital tracking, drone aerial shot, time-lapse, motion blur, handheld shake. <br> | jjewelry-java 业务文案。 |  |
| 输出格式要求：<br> | Output format requirements:<br> | jjewelry-java 业务文案。 |  |
| 输出的中文提示词字符数不能超过500字<br> | The output prompt must not exceed 500 Chinese characters.<br> | jjewelry-java 业务文案。 |  |
| 年 | Year | 服务层异常或业务提示文案。 |  |
| 日期 | Date | 服务层异常或业务提示文案。 |  |
| 时间 | Time | 服务层异常或业务提示文案。 |  |
| 月 | Month | 服务层异常或业务提示文案。 |  |
| 用户ID | User ID | 服务层异常或业务提示文案。 |  |
| 用户名 | Username | 服务层异常或业务提示文案。 |  |
| 频道ID | Channel ID | 服务层异常或业务提示文案。 |  |
| 频道名称 | Channel name | 服务层异常或业务提示文案。 |  |
| 搜索 @ | Search @ | jjewelry-java 业务文案。 |  |

## notify-java

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 系统批量通知 | System Batch Notification | 站内信类型枚举 |  |
| 系统通知-通用消息 | System Notification - General Message | 站内信类型枚举 |  |
| 点赞 | Like | 站内信类型枚举 |  |
| 登录送票 | Login Ticket Grant | 站内信类型枚举 |  |
| 注册送票 | Registration Ticket Grant | 站内信类型枚举 |  |
| 有人 want it | Someone Wants It | 站内信类型枚举 |  |
| 可以做货-通知作者 | Can Make It Real - Notify Creator | 站内信类型枚举 |  |
| 可以做货-通知wantit用户 | Can Make It Real - Notify WantIt Users | 站内信类型枚举 |  |
| 不可以做货-通知作者 | Cannot Make It Real - Notify Creator | 站内信类型枚举 |  |
| 不可以做货-通知用户 | Cannot Make It Real - Notify User | 站内信类型枚举 |  |
| 做货中 | In Production | 站内信类型枚举 |  |
| 关注 | Follow | 站内信类型枚举 |  |
| 回关 | Follow Back | 站内信类型枚举 |  |
| 评论 | Comment | 站内信类型枚举 |  |
| 回复 | Reply | 站内信类型枚举 |  |
| 支付成功 | Payment Successful | 站内信类型枚举 |  |
| 生产完成 | Production Completed | 站内信类型枚举 |  |
| 退款 | Refund | 站内信类型枚举 |  |
| 已发货 | Shipped | 站内信类型枚举 |  |
| 作品删除-通知wantit用户 | Post Deleted - Notify WantIt Users | 站内信类型枚举 |  |
| 活动作品获奖-做货奖励 | Event Content Award - Make-It-Real Reward | 站内信类型枚举 |  |
| 活动作品获奖-非做货奖励 | Event Content Award - Non-Make-It-Real Reward | 站内信类型枚举 |  |
| 活动作品获奖-做货奖励-无法做货 | Event Content Award - Make-It-Real Reward - Cannot Produce | 站内信类型枚举 |  |
| 用户榜单 | User Ranking | 站内信类型枚举 |  |
| @提醒 | @Mention | 站内信类型枚举 |  |
| 系统定向通知 | Targeted System Notification | 站内信类型枚举 |  |
| 好物赏-特殊赏中赏通知 | IKJ Draw - Special Award Notification | 站内信类型枚举 |  |
| 售卖分成 | Sales Commission | 站内信类型枚举 |  |
| 微信小程序 | WeChat Mini Program | 目标设备枚举展示名 |  |
| sse建立长链接 | Establish SSE long-lived connection | SSE 接口 Swagger 操作 |  |
| 建立SSE连接 | Establish SSE connection | SSE 接口 Swagger 操作 |  |
| uuid不能为空 | uuid cannot be empty | SSE 参数校验异常 |  |
| 系统繁忙，请稍后重试 | The system is busy. Please try again later. | SSE 连接数限制异常 |  |
| 建立SSE连接失败 | Failed to establish SSE connection | SSE 初始化异常 |  |
| 创建实体响应 | Entity creation response | Swagger 响应模型 |  |
| 创建成功的实体主键 | Primary key of the created entity | Swagger 响应字段 |  |
| Notify对象 | Notify object | Swagger 通知实体模型 |  |
| 通知表 | Notification table | Swagger 通知实体说明 |  |
| 主键 | Primary key | Swagger 字段说明 |  |
| 消息唯一id | Unique message ID | Swagger 字段说明 |  |
| 消息类型：1点赞，2登录送票，3注册送票，4有人 want it，5可以做货-所有用户，6可以做货-wantit用户 | Message type: 1 Like, 2 Login Ticket Grant, 3 Registration Ticket Grant, 4 WantIt, 5 Can Make It Real - All Users, 6 Can Make It Real - WantIt Users | Swagger 字段说明 |  |
| 1互动消息，2系统消息 | 1 Interaction message, 2 System message | Swagger 字段说明 |  |
| 点赞唯一id | Unique like ID | Swagger 字段说明 |  |
| 帖子内容唯一id | Unique topic content ID | Swagger 字段说明 |  |
| 发送者唯一id；0=系统操作 | Sender unique ID; 0 = system action | Swagger 字段说明 |  |
| 接收者唯一id | Receiver unique ID | Swagger 字段说明 |  |
| 已读标识：1=是，0=否 | Read flag: 1 = yes, 0 = no | Swagger 字段说明 |  |
| 是否删除:0=正常,1=删除 | Deleted flag: 0 = normal, 1 = deleted | Swagger 字段说明 |  |
| 是否隐藏:0=正常,1=隐藏 | Hidden flag: 0 = normal, 1 = hidden | Swagger 字段说明 |  |
| UserThirdParty对象 | UserThirdParty object | Swagger 第三方账号实体模型 |  |
| 第三方用户关联表 | Third-party user association table | Swagger 第三方账号实体说明 |  |
| 主键ID | Primary key ID | Swagger 字段说明 |  |
| 内部用户ID | Internal user ID | Swagger 字段说明 |  |
| 来源：3=短信登录，4=微信登录 | Source: 3 = SMS login, 4 = WeChat login | Swagger 字段说明 |  |
| 存储手机号或第三方uuid。微信对应appId | Stores mobile number or third-party UUID. WeChat corresponds to appId | Swagger 字段说明 |  |
| 第三方扩展信息 | Third-party extension info | Swagger 字段说明 |  |
| 微信开放平台登录 | WeChat Open Platform login | 第三方登录类型枚举 |  |
| 手机登录 | Mobile login | 第三方登录类型枚举 |  |
| User对象 | User object | Swagger 用户实体模型 |  |
| 用户表 | User table | Swagger 用户实体说明 |  |
| 用户名 | Username | Swagger 字段说明 |  |
| 密码 | Password | Swagger 字段说明 |  |
| 昵称 | Nickname | Swagger 字段说明 |  |
| 头像 | Avatar | Swagger 字段说明 |  |
| 创建者ID | Creator ID | Swagger 字段说明 |  |
| 更新者ID | Updater ID | Swagger 字段说明 |  |
| UserExtra对象 | UserExtra object | Swagger 用户扩展实体模型 |  |

## operation-platform

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
|  |  |  |  |

## order-java

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 历史退款单数据迁移完成 | Historical refund order data migration completed | 售后历史退款单迁移接口返回文案 |  |
| 历史退款单数据迁移失败:  | Historical refund order data migration failed:  | 售后历史退款单迁移异常返回文案 |  |
| 统计完成 | Statistics completed | 售后迁移统计接口返回文案 |  |
| 统计失败:  | Statistics failed:  | 售后迁移统计异常返回文案 |  |
| Excel 文件不能为空 | Excel file cannot be empty | 分成批量处理 Excel 校验 |  |
| refund返回空 | refund returned empty | 众筹退款接口异常提示 |  |
| 商品ID不能为空 | Product ID cannot be empty | 商品精选/内部接口参数校验 |  |
| 排序序号必须大于0 | Sort order must be greater than 0 | 商品精选排序参数校验 |  |
| 商品不存在 | Product does not exist | 商品精选业务校验 |  |
| 不能添加定制商品 | Customized products cannot be added | 商品精选业务规则 |  |
| 不能添加自有商品 | Owned products cannot be added | 商品精选业务规则 |  |
| 成功 | Success | 快递订阅/接口返回成功文案 |  |
| 平台订单号,采购单号,入库单号,发货单号,商品名称,下单时间,订单完成时间,商品数量,优惠金额,用户实付金额(元),订单状态,售后状态 | Platform order No.,Purchase order No.,Inbound order No.,Shipment order No.,Product name,Order time,Order completion time,product quantity,Discount amount,User paid amount (CNY),Order status,After-sale status | 正向对账导出 CSV 表头 |  |
| 平台订单号,采购单号,售后单号,退款单号,渠道流水号,商品名称,下单时间,退款申请时间,退款回调完成时间,商品数量,优惠金额,用户实付金额(元),退款金额,订单状态,售后状态 | Platform order No.,Purchase order No.,After-sale order No.,Refund order No.,Channel transaction No.,Product name,Order time,Refund request time,Refund callback completion time,product quantity,Discount amount,User paid amount (CNY),Refund amount,Order status,After-sale status | 逆向对账导出 CSV 表头 |  |
| 造好物平台- | Zaohaowu Platform- | 对账导出文件名前缀 |  |
| 结算对账单(正向).csv | reconciliation_statement_forward.csv | 正向对账导出文件名 |  |
| 结算对账单(逆向).csv | reconciliation_statement_reverse.csv | 逆向对账导出文件名 |  |
| 逆向交易明细表 | reverse transaction details | 对账导出工作表名称 |  |
| 总计 | Total | 对账导出汇总行 |  |
| 商品预计%s内发货 | Product ships within %s | 商品预计发货时间展示 |  |
| 商品 | Product | 促销适用对象展示 |  |
| 立减 | Direct reduction | 促销优惠类型展示 |  |
| 百分比优惠 | Percentage discount | 促销优惠类型展示 |  |
| 促销价 | Promotional price | 促销优惠类型展示 |  |
| 自营 | Self-operated | 业务模式枚举展示 |  |
| 三方外采 | Third-party procurement | 业务模式枚举展示 |  |
| 自有商品 | Owned product | 业务模式枚举展示 |  |
| 边框+主体 | Border + main body | 定制生产文件层类型 |  |
| 背景白墨(满铺) | Background white ink (full coverage) | 定制生产文件层类型 |  |
| 主体白墨 | Main body white ink | 定制生产文件层类型 |  |
| 主体 | Main body | 定制生产文件层类型 |  |
| 边框+主体+背景合成层 | Border + main body + background merged layer | 定制生产文件层类型 |  |
| 背景 | Background | 定制生产文件层类型 |  |
| 正面[边框+主体白墨，背景白墨(满铺)]，背面[背景层] | Front [border + main body white ink, background white ink (full coverage)], back [background layer] | 定制生产文件类型说明 |  |
| 正面[边框+主体+背景合成层] | Front [border + main body + background merged layer] | 定制生产文件类型说明 |  |
| 正面[边框+主体白墨] | Front [border + main body white ink] | 定制生产文件类型说明 |  |
| 正面[主体] | Front [main body] | 定制生产文件类型说明 |  |
| 边框 | Border | 定制侧面图层类型 |  |
| 图片 | Image | 定制侧面图层类型 |  |
| 正面 | Front | 定制商品面类型 |  |
| 背面 | Back | 定制商品面类型 |  |
| 杯子 | Cup | 虚拟预览类型 |  |
| 吧唧 | Badge | 虚拟预览类型 |  |
| 未关联 | Not linked | ERP/SKU 关联状态 |  |
| 已关联 | Linked | ERP/SKU 关联状态 |  |
| 关联失败 | Link failed | ERP/SKU 关联状态 |  |
| 已创建 | Created | 订单状态展示 |  |
| 待支付 | Pending payment | 订单/支付状态展示 |  |
| 已经支付 | Paid | 订单/支付状态展示 |  |
| 生产中 | In production | 订单状态展示 |  |
| 已发货 | Shipped | 订单/消息状态展示 |  |
| 已完成 | Completed | 订单状态展示 |  |
| 已关闭 | Closed | 订单/支付状态展示 |  |
| 退款中 | Refunding | 退款状态展示 |  |
| 手动配置 | Manual configuration | 商品创建方式 |  |
| 批量导入 | Batch import | 商品创建方式 |  |
| 3D建模 | 3D modeling | 商品阶段展示 |  |
| 3D打印生产 | 3D printing production | 商品阶段展示 |  |
| 产品交付 | Product delivery | 商品阶段展示 |  |
| 全部 | All | 商品 Tab / 目标用户范围展示 |  |
| 精选 | Featured | 商品 Tab 展示 |  |
| 默认 | Default | 商品渠道/类型默认展示 |  |
| 想要活动 | Want activity | 商品渠道展示 |  |
| 集赞活动 | Like-collection activity | 商品渠道展示 |  |
| 运营想要 | Operations Want | 商品渠道展示 |  |
| 设计师想要 | Designer Want | 商品渠道展示 |  |
| B端客户 | B-side customer | 商品渠道展示 |  |
| 模版定制 | Template customization | 商品定制类型展示 |  |
| AI创作 | AI creation | 商品定制类型展示 |  |
| 统一运费 | Fixed shipping fee | 商品运费设置展示 |  |
| 运费模板 | Shipping template | 商品运费设置展示 |  |
| 关联模型ID | Associated model ID | 商品关联类型展示 |  |
| 关联图片 | Associated image | 商品关联类型展示 |  |
| 预售 | Pre-sale | 发货模式展示 |  |
| 现货 | In stock | 发货模式展示 |  |
| 实时计算 | Real-time calculation | 促销折扣计算类型 |  |
| 折扣 | Discount | 促销折扣类型 |  |
| 所有商品快照 | All product snapshots | 促销适用范围展示 |  |
| 部分商品 | Partial products | 促销适用范围展示 |  |
| 所有商品 | All products | 促销适用范围展示 |  |
| 部分商品类型 | Partial product types | 促销适用范围展示 |  |
| 部分商品品类 | Partial product categories | 促销适用范围展示 |  |
| 部分商品SKU | Partial product SKUs | 促销适用范围展示 |  |
| 表达式圈定商品范围 | Product scope selected by expression | 促销适用范围展示 |  |
| 未开始 | Not started | 促销状态展示 |  |
| 进行中 | In progress | 促销状态展示 |  |
| 已结束 | Ended | 促销状态展示 |  |
| 已失效 | Invalid | 促销状态展示 |  |
| 新人 | New user | 促销目标用户组展示 |  |
| 粉丝 | Fan | 促销目标用户组展示 |  |
| 作者 | Author | 促销目标用户组展示 |  |
| 学生或老师新用户 | New student or teacher user | 促销目标用户组展示 |  |
| 学生或老师老用户 | Existing student or teacher user | 促销目标用户组展示 |  |
| 人气投票创作者 | Popular-vote creator | 促销目标用户组展示 |  |
| 运营精选创作者 | Operations-selected creator | 促销目标用户组展示 |  |
| 人气投票和社区精选创作者 | Popular-vote and community-selected creator | 促销目标用户组展示 |  |
| 小红书优惠券用户 | Xiaohongshu coupon user | 促销目标用户组展示 |  |
| 投票用户 | Voting user | 促销目标用户组展示 |  |
| 母亲节用户 | Mother's Day user | 促销目标用户组展示 |  |
| 得物优惠券用户 | Dewu coupon user | 促销目标用户组展示 |  |
| 签到新用户 | Check-in new user | 促销目标用户组展示 |  |
| LINE_FRIENDS优惠券用户 | LINE FRIENDS coupon user | 促销目标用户组展示 |  |
| 限时 | Limited time | 促销类型展示 |  |
| 限量 | Limited quantity | 促销类型展示 |  |
| 单品 | Single item | 促销类型展示 |  |
| 人群 | Target group | 促销类型展示 |  |
| 母亲节 | Mother's Day | 促销类型展示 |  |
| 自动取消 | Auto cancel | 订单自动取消任务配置 |  |
| 订单号: %s, skuCustomizedId: %s, categoryId: %s, 生产文件ID: %s, errorMsg: %s | Order No.: %s, skuCustomizedId: %s, categoryId: %s, production file ID: %s, errorMsg: %s | 订单生产文件分配异常提示 |  |
| 售后单号: %s, 错误: %s | After-sale order No.: %s, error: %s | 售后/退款任务异常提示 |  |
| 售后单ID: %s, 错误: %s | After-sale order ID: %s, error: %s | 售后处理任务异常提示 |  |
| 检测到解冻失败，重试第 | Unfreeze failure detected, retry # | 优惠券权益退回重试提示 |  |
| recordId: %s, userId: %s, orderId: %s, 需要人工介入处理 | recordId: %s, userId: %s, orderId: %s, manual intervention required | 优惠券权益退回失败处理提示 |  |
| 达到最大重试次数，标记为退失败 | Maximum retry count reached; marked as return failed | 优惠券权益退回重试结果 |  |
| 重试解冻操作 | Retry unfreeze operation | 优惠券权益退回重试操作 |  |
| 重试解冻成功 | Retry unfreeze succeeded | 优惠券权益退回重试结果 |  |
| 重试解冻失败:  | Retry unfreeze failed:  | 优惠券权益退回重试结果 |  |
| 姓名 | Name | 小红书订单拉取收件人字段 |  |
| 电话 | Phone | 小红书订单拉取收件人字段 |  |
| 地址 | Address | 小红书订单拉取收件人字段 |  |
| 解密失败 | Decryption failed | 小红书订单拉取解密错误 |  |
| 待审核 | Pending review | 售后审核状态展示 |  |
| 审核通过 | Approved | 售后审核状态展示 |  |
| 审核不通过 | Rejected | 售后审核状态展示 |  |
| 售后成功 | After-sale succeeded | 售后状态展示 |  |
| 售后失败 | After-sale failed | 售后状态展示 |  |
| 售后单关闭 | After-sale order closed | 售后状态展示 |  |
| 已撤销 | Canceled | 售后状态展示 |  |
| 未发货仅退款 | Refund only before shipment | 售后类型展示 |  |
| 仅退款不退货 | Refund only without return | 售后类型展示 |  |
| 退货退款 | Return and refund | 售后类型展示 |  |
| 补发货 | Reshipment | 售后类型展示 |  |
| 平台不可做货取消 | Canceled because the platform cannot produce | 售后申请原因 |  |
| 用户取消 | User canceled | 售后申请原因 |  |
| 第三方不可做货取消 | Canceled because the third party cannot produce | 售后申请原因 |  |
| 客服协商取消 | Canceled after customer service negotiation | 售后申请原因 |  |
| 不涉及 | Not applicable | 售后审核/优惠券权益状态 |  |
| 售后单状态异常：无法进行审核操作, afterSaleOrderId: %s, currentStatus: %s, approved: %s | After-sale order status exception: cannot perform review, afterSaleOrderId: %s, currentStatus: %s, approved: %s | 售后审核状态机异常提示 |  |
| 审核通过： | Review approved:  | 售后状态日志备注 |  |
| 审核不通过： | Review rejected:  | 售后状态日志备注 |  |
| 用户撤销售后申请 | User revoked after-sale request | 售后状态日志备注 |  |
| 售后处理完成 | After-sale processing completed | 售后状态日志备注 |  |
| 用户创建售后申请 | User created after-sale request | 售后状态日志备注 |  |
| 售后单创建成功 | After-sale order created successfully | 售后创建返回文案 |  |
| 待审核状态，可以审核通过 | Pending review status can be approved | 售后审核状态机提示 |  |
| 售后单已审核通过，幂等操作 | After-sale order has already been approved; idempotent operation | 售后审核幂等提示 |  |
| 售后单已成功，幂等操作 | After-sale order has already succeeded; idempotent operation | 售后审核幂等提示 |  |
| 售后单已审核不通过，无法转为审核通过状态 | After-sale order has already been rejected and cannot be changed to approved status | 售后状态机提示 |  |
| 售后单已关闭，无法进行审核操作 | After-sale order has been closed and cannot be reviewed | 售后状态机提示 |  |
| 售后单已撤销，无法进行审核操作 | After-sale order has been canceled and cannot be reviewed | 售后状态机提示 |  |
| 售后单已失败，无法转为审核通过状态 | After-sale order has failed and cannot be changed to approved status | 售后状态机提示 |  |
| 待审核状态，可以审核不通过 | Pending review status can be rejected | 售后审核状态机提示 |  |
| 售后单已审核不通过，幂等操作 | After-sale order has already been rejected; idempotent operation | 售后审核幂等提示 |  |
| 售后单已审核通过，无法转为审核不通过状态 | After-sale order has already been approved and cannot be changed to rejected status | 售后状态机提示 |  |
| 售后单已成功，无法转为审核不通过状态 | After-sale order has already succeeded and cannot be changed to rejected status | 售后状态机提示 |  |
| 售后单已失败，无法转为审核不通过状态 | After-sale order has failed and cannot be changed to rejected status | 售后状态机提示 |  |
| 未知的售后单状态： | Unknown after-sale order status:  | 售后状态机兜底提示 |  |
| ERP取消失败补建失败记录 | ERP cancellation failed; added failure record | 售后 ERP 取消补偿备注 |  |
| 订单履约信息不存在 | Order fulfillment information does not exist | 售后申请业务校验 |  |
| 商品已发货，不能申请未发货退款 | The product has already been shipped and cannot use a pre-shipment refund | 售后申请业务校验 |  |
| 商品已进入生产或待发货状态，不能申请未发货退款 | The product is already in production or pending shipment and cannot use a pre-shipment refund | 售后申请业务校验 |  |
| 当前订单状态不支持未发货仅退款 | The current order status does not support pre-shipment refund only | 售后申请业务校验 |  |
| 商品尚未发货，请选择未发货仅退款 | The product has not been shipped yet; select pre-shipment refund only | 售后申请业务校验 |  |
| 当前订单状态不支持仅退款不退货 | The current order status does not support refund only without return | 售后申请业务校验 |  |
| 商品尚未发货，无法申请补发货 | The product has not been shipped yet, so reshipment cannot be requested | 售后申请业务校验 |  |
| 当前订单状态不支持补发货 | The current order status does not support reshipment | 售后申请业务校验 |  |
| 商品尚未发货，无法申请退货退款 | The product has not been shipped yet, so return and refund cannot be requested | 售后申请业务校验 |  |
| 当前订单状态不支持退货退款 | The current order status does not support return and refund | 售后申请业务校验 |  |
| 关键属性 | Key attribute | 商品属性类型展示 |  |
| 销售属性 | Sales attribute | 商品属性类型展示 |  |
| 其他属性 | Other attribute | 商品属性类型展示 |  |
| 根类目 | Root category | 类目转换默认名称 |  |
| 创建模板 | Create template | 类目属性模板操作日志/展示 |  |
| 更新模板 | Update template | 类目属性模板操作日志/展示 |  |
| 删除模板 | Delete template | 类目属性模板操作日志/展示 |  |
| 模板名称 | Template name | 类目属性模板校验 |  |
| 参数不正确 | Invalid parameters | 全局 ID 工具参数校验 |  |
| 获取锁失败，系统繁忙 | Failed to acquire lock. System is busy | 分布式锁获取失败提示 |  |
| 操作被中断 | Operation was interrupted | 分布式锁中断提示 |  |
| 待退回 | Pending return | 优惠券权益退回状态 |  |
| 退回中 | Returning | 优惠券权益退回状态 |  |
| 已退回 | Returned | 优惠券权益退回状态 |  |
| 退失败 | Return failed | 优惠券权益退回状态 |  |
| yyyy年MM月dd日 | yyyy-MM-dd | 优惠券日期展示格式 |  |
| 优惠码错误 | Invalid coupon code | 优惠码领取校验 |  |
| 优惠码不存在 | Coupon code does not exist | 优惠码领取校验 |  |
| 优惠码已被领取 | Coupon code has already been claimed | 优惠码领取校验 |  |
| 优惠码已过期 | Coupon code has expired | 优惠码领取校验 |  |
| 只能领取一个限时优惠 | Only one limited-time discount can be claimed | 优惠码领取规则 |  |
| 优惠券领取失败，请稍后重试! | Failed to claim coupon. Please try again later! | 优惠券领取失败提示 |  |
| 众筹进度计算异常 | Crowdfunding progress calculation exception | 众筹进度计算异常提示 |  |
| 调用失败 | Call failed | ERP 调用结果说明 |  |
| %s%s: 请求=%s, 响应=%s | %s%s: request=%s, response=%s | ERP 调用追踪文案 |  |
| 未返回货品数据 | No goods data returned | ERP 货品查询异常提示 |  |
| 调用异常 | Call exception | ERP 调用异常说明 |  |
| 解析异常 | Parse exception | ERP 响应解析异常说明 |  |
| 未返回数据 | No data returned | ERP 响应为空提示 |  |
| 部分失败 | Partially failed | ERP 批量处理结果 |  |
| 返回数据为空 | Returned data is empty | ERP 响应为空提示 |  |
| ERP取消订单失败: 系统异常 | ERP order cancellation failed: system exception | ERP 取消订单异常提示 |  |
| ERP取消订单失败，请稍后重试 | ERP order cancellation failed. Please try again later. | ERP 取消订单用户提示 |  |
| 履约中台返回失败 | Fulfillment center returned failure | ERP 取消订单返回提示 |  |
| 待生产 | Pending production | 履约状态展示 |  |
| 待发货 | Pending shipment | 履约/三方订单状态展示 |  |
| 已送达 | Delivered | 履约状态展示 |  |
| 已取消 | Canceled | 履约/三方订单状态展示 |  |
| 订单号: %s, 错误: %s | Order No.: %s, error: %s | 履约/支付异常提示 |  |
| 亚克力生成生产文件失败 | Failed to generate acrylic production file | 图片服务生产文件异常提示 |  |
| 手机支架生成生产文件失败 | Failed to generate phone stand production file | 图片服务生产文件异常提示 |  |
| 有效 | Valid | 促销内部状态展示 |  |
| 无效 | Invalid | 促销内部状态展示 |  |
| 配额初始化失败 | Quota initialization failed | 活动作者配额初始化提示 |  |
| 找不到跟商品price一致的sku | No SKU matching the product price was found | 营销扩展服务异常提示 |  |
| 用户ID:  | User ID:  | 营销/支付异常详情字段 |  |
| 商品ID:  | Product ID:  | 营销异常详情字段 |  |
|  商品价格:  |  Product price:  | 营销异常详情字段 |  |
|  折扣价:  |  Discount price:  | 营销异常详情字段 |  |
|  折扣:  |  Discount:  | 营销异常详情字段 |  |
|  折扣渠道:  |  Discount channel:  | 营销异常详情字段 |  |
| 所选商品已存在同类型促销活动，不可重复设置。冲突商品ID列表： | The selected products already have the same type of promotion activity and cannot be configured again. Conflicting product ID list: | 促销活动冲突校验 |  |
| 商品ID= | Product ID= | 促销活动冲突详情字段 |  |
| 活动ID= | Activity ID= | 促销活动冲突详情字段 |  |
| 所选商品类型已存在同类型促销活动，不可重复设置。冲突商品类型列表： | The selected product types already have the same type of promotion activity and cannot be configured again. Conflicting product type list: | 促销活动冲突校验 |  |
| 商品类型= | Product type= | 促销活动冲突详情字段 |  |
| 短信 | SMS | 订单通知动作类型 |  |
| 邮件 | Email | 订单通知动作类型 |  |
| 站内信 | In-app message | 订单通知动作类型 |  |
| 支付成功 | Payment successful | 订单通知消息类型 |  |
| 生产完成 | Production completed | 订单通知消息类型 |  |
| 退款 | Refund | 订单通知消息类型 |  |
| PGC用户新订单 | New PGC user order | 订单通知消息类型 |  |
| 促销 | Promotion | 操作日志业务系统 |  |
| 订单 | Order | 操作日志业务系统 |  |
| 用户 | User | 操作日志业务系统 |  |
| 系统 | System | 操作日志业务系统 |  |
| 优惠券 | Coupon | 操作日志业务系统 |  |
| 通用 | Common | 操作日志业务系统 |  |
| 创建 | Create | 操作日志操作类型 |  |
| 修改 | Update | 操作日志操作类型 |  |
| 删除 | Delete | 操作日志操作类型 |  |
| 取消 | Cancel | 操作日志操作类型 |  |
| 用户主动 | User initiated | 订单关闭原因 |  |
| 用户退款关闭 | User refund closed | 订单关闭原因 |  |
| 任务关闭 | Task closed | 订单关闭原因 |  |
| 商户退款关闭 | Merchant refund closed | 订单关闭原因 |  |
| 普通订单 | Normal order | 订单类型展示 |  |
| 已提现 | Withdrawn | 分成提现状态 |  |
| 未提现 | Not withdrawn | 分成提现状态 |  |
| 订单不存在 | Order does not exist | 订单内部服务业务校验 |  |
| 订单状态不允许发货，当前状态:  | The order status does not allow shipment. Current status:  | 订单发货业务校验 |  |
| 订单已发货 | The order has already been shipped | 订单发货业务校验 |  |
| 供应商不匹配 | Supplier mismatch | 订单发货业务校验 |  |
| 不涉及退款 | No refund involved | 订单退款状态展示 |  |
| 退款失败 | Refund failed | 订单退款状态展示 |  |
| 可退款 | Refundable | 订单退款状态展示 |  |
| Excel 无工作表 | Excel has no worksheet | 分成批量 Excel 校验 |  |
| Excel 无有效数据行 | Excel has no valid data rows | 分成批量 Excel 校验 |  |
| Excel 数据行超过上限  | Excel data rows exceed the limit  | 分成批量 Excel 校验 |  |
| 第%d行：库中不存在明细 unique_sale_share_detail_id=%s | Row %d: detail does not exist in the database, unique_sale_share_detail_id=%s | 分成批量 Excel 校验 |  |
| Excel 第一行必须为表头 | The first Excel row must be the header | 分成批量 Excel 校验 |  |
| Excel 表头缺少列:  | Excel header is missing column:  | 分成批量 Excel 校验 |  |
| 第%d行：unique_sale_share_detail_id 为空 | Row %d: unique_sale_share_detail_id is empty | 分成批量 Excel 校验 |  |
| 第%d行：expected_share_amount 格式非法，值=%s | Row %d: expected_share_amount format is invalid, value=%s | 分成批量 Excel 校验 |  |
| 履约中台返回为空 | Fulfillment center returned empty | 订单履约异常提示 |  |
| %s失败，错误信息：%s, orderId: %s, skuId: %s | %s failed, error message: %s, orderId: %s, skuId: %s | 订单处理异常提示 |  |
| 解析高优订单ID失败，订单数量：%d，前10个订单ID：%s，异常类型：%s，异常信息：%s | Failed to resolve high-priority order IDs, order count: %d, first 10 order IDs: %s, exception type: %s, exception message: %s | 高优订单解析异常提示 |  |
| 无 | None | 高优订单解析异常详情 |  |
| ... (共%d个订单) | ... (%d orders in total) | 高优订单解析异常详情 |  |
| 超时关闭 | Closed due to timeout | 支付关闭原因 |  |
| Stripe APP支付 | Stripe app payment | 支付渠道展示 |  |
| Stripe APP支付渠道 | Stripe APP payment channel | 支付渠道展示 |  |
| Stripe退款渠道 | Stripe refund channel | 退款渠道展示 |  |
| 订单号:  | Order No.:  | 支付异常详情字段 |  |
| \n支付方式:  | \nPayment method:  | 支付异常详情字段 |  |
| \n错误:  | \nerror:  | 支付异常详情字段 |  |
| 订单号: %s, 支付单号: %s, 错误: %s | Order No.: %s, payment No.: %s, error: %s | 支付结果异常提示 |  |
| PaymentMap初始化失败 | PaymentMap initialization failed | 支付组件初始化提示 |  |
| 售后单号: %s, 退款单号: %s, 错误: %s | After-sale order No.: %s, Refund order No.: %s, error: %s | 退款异常提示 |  |
| \n支付单号:  | \nPayment No.:  | Stripe 支付异常详情字段 |  |
| \n支付意图ID:  | \nPayment intent ID:  | Stripe 支付异常详情字段 |  |
| \n期望金额(分):  | \nExpected amount (cents):  | Stripe 支付金额校验字段 |  |
| \n实际金额(分):  | \nActual amount (cents):  | Stripe 支付金额校验字段 |  |
| 退款单号=%s, 状态=%s | Refund order No.=%s, Status=%s | Stripe 退款状态提示 |  |
| 退款单号: | Refund order No.: | Stripe 退款异常详情字段 |  |
| 未知异常 | Unknown exception | 支付异常兜底文案 |  |
| 参数名不能为空 | Parameter name cannot be empty | URL 构建工具参数校验 |  |
| 百分比打折 | Percentage discount | 询价折扣类型展示 |  |
| 一口价 | Fixed promotion price | 询价折扣类型展示 |  |
| 普通标签 | Normal tag | 询价标签类型展示 |  |
| 实时计算标签 | Real-time calculated tag | 询价标签类型展示 |  |
| 未登录用户 | Guest user | 询价异常详情字段 |  |
| 锁定 | Locked | SKU 库存流水状态 |  |
| 已扣减 | Deducted | SKU 库存流水状态 |  |
| 已释放 | Released | SKU 库存流水状态 |  |
| 属性[%s(ID:%d)]在目标类目中不存在 | Attribute [%s (ID:%d)] does not exist in the target category | 商品类目迁移校验 |  |
| 属性[%s]类型不匹配: %d -> %d | Attribute [%s] type mismatch: %d -> %d | 商品类目迁移校验 |  |
| 未知属性 | Unknown attribute | 商品 SKU 同步兜底字段 |  |
| 获取一件定制tag失败: record/tagName为空. productId=%s, skuCId=%s, imageId=%s | Failed to get one-off custom tag: record/tagName is empty. productId=%s, skuCId=%s, imageId=%s | 商品 SKU 同步异常提示 |  |
| 获取一件定制tag异常: productId=%s, skuCId=%s, imageId=%s, err=%s | Exception while getting one-off custom tag: productId=%s, skuCId=%s, imageId=%s, err=%s | 商品 SKU 同步异常提示 |  |
| SKU图&主图都不存在: %s, SkuId: %s, 请及时处理 | Neither SKU image nor main image exists: %s, SkuId: %s, please handle it promptly | 商品 SKU 同步异常提示 |  |
| URL解码失败: %s, SkuCId: %s, 请及时处理 | URL decode failed: %s, SkuCId: %s, please handle it promptly | 商品 SKU 同步异常提示 |  |
| 一件定制存在非jpg\|jpeg\|png\|webp图片，是否需要转格式，需人工确认: %s, SkuCId: %s, 请及时处理 | One-off customization contains a non-jpg\|jpeg\|png\|webp image. Confirm manually whether format conversion is needed: %s, SkuCId: %s, please handle it promptly | 商品 SKU 同步异常提示 |  |
| 模版商品，productId: %s, skuCustomizedId: %s | Template product, productId: %s, skuCustomizedId: %s | 商品 SKU 同步异常详情 |  |
| 共计%s条货品, 请及时处理 | %s goods in total, please handle promptly | 商品 SKU 同步异常汇总 |  |
| 第%d行数据有误: %s | Invalid data on row %d: %s | 商品发布导入校验 |  |
| 商品创建初始化库存 | Product creation initial inventory | 商品创建库存流水备注 |  |
| 商品未上架! | Product is not on sale! | 商品读取业务校验 |  |
| md5计算失败 | MD5 calculation failed | 商品属性哈希异常提示 |  |
| 增加库存 | Increase stock | 库存操作类型展示 |  |
| 减少库存 | Decrease stock | 库存操作类型展示 |  |
| 锁库存 | Lock stock | 库存操作类型展示 |  |
| 解锁库存 | Unlock stock | 库存操作类型展示 |  |
| 扣减库存 | Deduct stock | 库存操作类型展示 |  |
| 释放库存 | Release stock | 库存操作类型展示 |  |
| 请求参数不能为空 | Request parameters cannot be empty | 库存错误码文案 |  |
| orderId不能为空 | orderId cannot be empty | 库存错误码文案 |  |
| skuIdList不能为空 | skuIdList cannot be empty | 库存错误码文案 |  |
| 库存变更数量必须大于0 | Stock change quantity must be greater than 0 | 库存错误码文案 |  |
| orderAction不能为空 | orderAction cannot be empty | 库存错误码文案 |  |
| skuId无效 | skuId is invalid | 库存错误码文案 |  |
| 新可售库存数量不能为负数 | New available stock quantity cannot be negative | 库存错误码文案 |  |
| SKU库存信息不存在 | SKU stock information does not exist | 库存错误码文案 |  |
| SKU重复 | Duplicate SKU | 库存错误码文案 |  |
| 库存不足 | Insufficient stock | 库存错误码文案 |  |
| 库存冻结失败 | Stock freeze failed | 库存错误码文案 |  |
| 库存解冻失败 | Stock unfreeze failed | 库存错误码文案 |  |
| 库存扣减失败 | Stock deduction failed | 库存错误码文案 |  |
| 库存释放失败 | Stock release failed | 库存错误码文案 |  |
| 库存增加失败 | Stock increase failed | 库存错误码文案 |  |
| 库存减少失败 | Stock decrease failed | 库存错误码文案 |  |
| 设置可售库存失败 (并发修改失败，请重试) | Failed to set available stock (concurrent modification failed, please retry) | 库存错误码文案 |  |
| 不支持的库存操作类型 | Unsupported stock operation type | 库存错误码文案 |  |
| 库存为0 | Stock is 0 | 库存错误码文案 |  |
| 库存记录不存在 | Stock record does not exist | 库存错误码文案 |  |
| 库存记录已存在 | Stock record already exists | 库存错误码文案 |  |
| 库存变更记录已存在 | Stock change record already exists | 库存错误码文案 |  |
| 系统错误 | System error | 库存错误码文案 |  |
| 部分商品库存不足或状态异常 | Some products have insufficient inventory or abnormal status | 库存扣减业务提示 |  |
| 系统异常 | System exception | 库存扣减业务提示 |  |
| 发货通知 | Delivery notification | 三方订单同步任务类型 |  |
| 收货通知 | Receipt notification | 三方订单同步任务类型 |  |
| 待执行 | Pending execution | 三方订单同步任务状态 |  |
| 执行中 | Processing | 三方订单同步任务状态 |  |
| 失败 | Failed | 三方订单同步任务状态 |  |
| 三方订单未找到对应本站商品: 外平台新增sku或者本站sku已删除 | No matching local product was found for the third-party order: the external platform added a new SKU or the local SKU was deleted | 三方订单转换结果 |  |
| 三方订单未找到对应本站商品: 本站商品已删除 | No matching local product was found for the third-party order: the local product was deleted | 三方订单转换结果 |  |
| 取消订单 | Cancel order | 三方订单事件类型 |  |
| 更新订单 | Update order | 三方订单事件类型 |  |
| 待采购 | Pending purchase | 三方订单状态展示 |  |
| 已采购 | Purchased | 三方订单状态展示 |  |
| 已下单待付款 | Order placed, pending payment | 小红书订单状态展示 |  |
| 已支付处理中 | Paid, processing | 小红书订单状态展示 |  |
| 清关中 | Customs clearance in progress | 小红书订单状态展示 |  |
| 部分发货 | Partially shipped | 小红书订单状态展示 |  |
| 待收货 | Pending receipt | 小红书订单状态展示 |  |
| 换货申请中 | Exchange request pending | 小红书订单状态展示 |  |
| 小红书订单列表查询失败 | Failed to query Xiaohongshu order list | 小红书订单网关异常提示 |  |
| 小红书订单详情查询失败 | Failed to query Xiaohongshu order details | 小红书订单网关异常提示 |  |
| 小红书收件人信息查询失败 | Failed to query Xiaohongshu receiver information | 小红书订单网关异常提示 |  |
| 小红书文本解密失败 | Failed to decrypt Xiaohongshu text | 小红书订单网关异常提示 |  |
| 小红书订单通知发货失败 | Failed to notify Xiaohongshu order shipment | 小红书订单网关异常提示 |  |
| 采购单折扣配置异常 | Purchase order discount configuration exception | 三方订单采购折扣校验 |  |
| 采购单折扣配置异常-三方订单ID: %s | Purchase order discount configuration exception - third-party order ID: %s | 三方订单采购折扣校验 |  |
| 采购单折扣金额异常 | Purchase order discount amount exception | 三方订单采购折扣校验 |  |
| 采购单折扣金额异常-三方订单ID: %s, discountedItemAmount: %s | Purchase order discount amount exception - third-party order ID: %s, discountedItemAmount: %s | 三方订单采购折扣校验 |  |
| 事件已记录 | Event recorded | 三方订单事件处理结果 |  |
| 处理三方订单支付状态失败 | Failed to process third-party order payment status | 三方订单状态处理异常 |  |
| 处理三方订单支付状态失败-本站订单ID:  | Failed to process third-party order payment status - local order ID:  | 三方订单状态处理异常 |  |
| 三方订单待发货状态失败 | Failed to process third-party order pending-shipment status | 三方订单状态处理异常 |  |
| 处理三方订单收货失败-业务错误不重试 | Failed to process third-party order receipt - business error, no retry | 三方订单收货处理异常 |  |
| 处理三方订单收货失败-本站订单ID: %s, 三方订单ID: %s | Failed to process third-party order receipt - local order ID: %s, third-party order ID: %s | 三方订单收货处理异常 |  |
| 处理三方订单收货失败-进入任务重试流程 | Failed to process third-party order receipt - entering task retry flow | 三方订单收货处理异常 |  |
| 处理三方订单收货失败 | Failed to process third-party order receipt | 三方订单收货处理异常 |  |
| 小红书取消订单 | Xiaohongshu canceled order | 三方订单关单原因 |  |
| 处理三方订单关单失败-三方订单ID: %s | Failed to process third-party order close - third-party order ID: %s | 三方订单关单异常 |  |
| 数量 | Quantity | 发货商品明细字段 |  |
| 查询物流信息失败 物流号: | Failed to query tracking info. Tracking No.:  | 物流轨迹查询异常提示 |  |
|  错误信息: |  error message:  | 物流轨迹查询异常详情 |  |
| 物流号: | Tracking No.:  | 快递订阅异常详情 |  |
|  响应码: |  Response code:  | 快递订阅异常详情 |  |
|  错误: |  error:  | 快递订阅异常详情 |  |
| 异步注册异常 单号: | Async registration exception. Tracking No.:  | 快递订阅异常提示 |  |
| 服务器错误或提交不规范 | Server error or invalid submission | 快递订阅返回码文案 |  |
| 重复订阅，等待status=abort或shutdown再试 | Duplicate subscription; wait for status=abort or shutdown before retrying | 快递订阅返回码文案 |  |
| 含敏感词被拦截 | Blocked due to sensitive words | 快递订阅返回码文案 |  |
| 授权Key错误或无可用单量 | Authorization key is incorrect or no quota is available | 快递订阅返回码文案 |  |
| Key已过期或无单量 | The key has expired or no quota is available | 快递订阅返回码文案 |  |
| 不支持的快递公司 | Unsupported express company | 快递订阅返回码文案 |  |
| 订阅数据错误或回调地址错误 | Subscription data error or callback URL error | 快递订阅返回码文案 |  |
| 识别不到快递公司或无单量 | Unable to identify the express company or no quota is available | 快递订阅返回码文案 |  |
| 未知错误 | Unknown error | 快递订阅返回码文案 |  |
| 订阅失败，单号: | Subscription failed, tracking number: | 快递订阅失败提示 |  |
|  原因: |  reason: | 快递订阅失败详情 |  |
| 手机号格式不正确 | Invalid mobile number format | 收货地址校验错误码 |  |
| 邮政编码格式不正确 | Invalid postal code format | 收货地址校验错误码 |  |
| 国家码不能为空 | Country code cannot be empty | 收货地址校验错误码 |  |
| 收件人姓名不能为空 | Recipient name cannot be empty | 收货地址校验错误码 |  |
| 手机号不能为空 | Mobile number cannot be empty | 收货地址校验错误码 |  |
| 详细地址不能为空 | Detailed address cannot be empty | 收货地址校验错误码 |  |
| 邮政编码不能为空 | Postal code cannot be empty | 收货地址校验错误码 |  |
| 省/州不能为空 | Province/state cannot be empty | 收货地址校验错误码 |  |
| 城市不能为空 | City cannot be empty | 收货地址校验错误码 |  |
| 区/县不能为空 | District/county cannot be empty | 收货地址校验错误码 |  |
| 地址数量超过上限（最多 8 个） | Address count exceeds the limit (maximum 8) | 收货地址校验错误码 |  |
| 地址不存在 | Address does not exist | 收货地址校验错误码 |  |
| 收件人姓名过长 | Recipient name is too long | 收货地址校验错误码 |  |
| 详细地址过长 | Detailed address is too long | 收货地址校验错误码 |  |

## push-service

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 默认 | Default | Push 业务类型枚举 |  |
| @提及 | @Mention | Push 业务类型枚举 |  |
| 评论 | Comment | Push 业务类型枚举 |  |
| 评论的回复 | Comment Reply | Push 业务类型枚举 |  |
| 关注 | Follow | Push 业务类型枚举 |  |
| 昨日互动 | Yesterday's Interactions | Push 业务类型枚举 |  |
| 热门想要 | Popular WantIt | Push 业务类型枚举 |  |
| 想要进度 | WantIt Progress | Push 业务类型枚举 |  |
| 想要达成祝贺（主态） | WantIt Completion Congrats (Owner) | Push 业务类型枚举 |  |
| 想要达成祝贺（用户侧） | WantIt Completion Congrats (User) | Push 业务类型枚举 |  |
| 连续活跃 | Consecutive Activity | Push 业务类型枚举 |  |
| 想要活动进度 | WantIt Activity Progress | Push 业务类型枚举 |  |
| 登录券过期 | Login Ticket Expired | Push 业务类型枚举 |  |
| 券过期 | Ticket Expired | Push 业务类型枚举 |  |
| AI生成结果 | AI Generation Result | Push 业务类型枚举 |  |
| 内容精选 | Featured Content | Push 业务类型枚举 |  |
| 日榜 | Daily Ranking | Push 业务类型枚举 |  |
| 周榜 | Weekly Ranking | Push 业务类型枚举 |  |
| 昨日收益 | Yesterday's Earnings | Push 业务类型枚举 |  |
| 上周收益 | Last Week's Earnings | Push 业务类型枚举 |  |
| 订单开始生产 | Order Production Started | Push 业务类型枚举 |  |
| 订单开始配送 | Order Delivery Started | Push 业务类型枚举 |  |
| 订单配送 | Order Delivery | Push 业务类型枚举 |  |
| 订单橱窗 | Order Showcase | Push 业务类型枚举 |  |
| 关注作者新内容 | New Content from Followed Creator | Push 业务类型枚举 |  |
| 激励玩法奖品更新 | Incentive Gameplay Reward Update | Push 业务类型枚举 |  |
| P0运营活动上新 | P0 Operation Activity Launch | Push 业务类型枚举 |  |
| 新功能上线 | New Feature Launch | Push 业务类型枚举 |  |
| 活动倒计时 | Event Countdown | Push 业务类型枚举 |  |
| 活动中奖 | Event Award | Push 业务类型枚举 |  |
| 成功 | Success | Push 错误码枚举 |  |
| 参数错误 | Invalid parameter | Push 错误码枚举 |  |
| 多个token | Multiple tokens | Push 错误码枚举 |  |
| 内部错误 | Internal error | Push 错误码枚举 |  |
| 未登录 | Logged out | Push token 状态枚举 |  |
| 有效 | Active | Push token 状态枚举 |  |
| 超过单日push总量 | Daily push total limit exceeded | Push 日限额拦截原因 |  |
| 超过单日公信push总量 | Daily public push total limit exceeded | Push 日限额拦截原因 |  |
| 超过单日{bizType}push总量 | Daily {bizType} push total limit exceeded | Push 日限额拦截原因 |  |
| 总开关关闭 | Master switch disabled | Push 免打扰拦截原因 |  |
| 当前时段免打扰 | Do-not-disturb time window | Push 免打扰拦截原因 |  |
| push开关关闭 | Push switch disabled | Push 开关拦截原因 |  |

## reward-task-center

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 运维接口 | Operations APIs | Swagger 运维接口分组，后台/内部 API 语义。 |  |
| 健康检查 | Health check | Swagger 操作文案。 |  |
| 系统繁忙 | The system is busy. Please try again later. | 异常兜底文案。 |  |
| 奖励领取风险告警 | Reward Claim Risk Alert | 飞书/告警标题，面向运维和风控。 |  |
| 检测到想要商品订单已退款，不满足兑奖要求 | A WantIt product order has been refunded and no longer meets the reward redemption requirements | 奖励领取风险告警正文。 |  |
| [任务ID]：%s | [Task ID]: %s | 告警字段标签。 |  |
| [用户ID]：%s | [User ID]: %s | 告警字段标签。 |  |
| [兑奖时间]：%s | [Redemption Time]: %s | 告警字段标签。 |  |
| [想要奖励兑换订单ID]：%s | [WantIt Reward Redemption Order ID]: %s | 告警字段标签。 |  |
| [奖励档位]：%s | [Reward Tier]: %s | 告警字段标签。 |  |
| [购买笔数 要求/当前]：%s / %s | [Purchase Count Required/Current]: %s / %s | 告警字段标签。 |  |
| [订单列表]： | [Order List]: | 告警字段标签。 |  |
| 想要商品购买退单ID：%s，退款时间：%s | WantIt Product Refunded Purchase Order ID: %s, Refund Time: %s | 告警订单明细。 |  |
| 订单数量：0 | Order Count: 0 | 告警字段标签。 |  |
| 风险告警 | Risk Alert | 飞书卡片默认标题。 |  |
| **告警类型**： | **Alert Type**: | 飞书 Markdown 字段标签。 |  |
| **当前环境**： | **Current Environment**: | 飞书 Markdown 字段标签。 |  |
| **当前服务**： | **Current Service**: | 飞书 Markdown 字段标签。 |  |
| **告警信息**： | **Alert Details**: | 飞书 Markdown 字段标签。 |  |
| **通知**： | **Notify**: | 飞书 Markdown at 人字段标签。 |  |

## short_link

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 短链接相关 | Short Link | Swagger 分组名称。 |  |
| 短链接相关接口 | Short Link APIs | Swagger tag 名称。 |  |
| 生成短链接 | Create short link | Swagger 操作文案。 |  |
| 访问短链接时重定向到原始链接 | Redirect to the original URL when visiting a short link | Swagger 操作文案。 |  |
| 获取原始参数 | Get original parameters | Swagger 操作文案。 |  |
| 自定义 | Custom | 枚举展示名。 |  |
| JewelryGenerateRecord对象 | ShortLinkMapping object | 原文疑似复制残留，按当前实体语义翻译。 |  |
| 生图记录表 | Short link mapping table | 原文疑似复制残留，按表 `biz_short_link_mapping` 语义翻译。 |  |
| 主键 | Primary key | Swagger 字段说明。 |  |
| 业务id | Business ID | Swagger 字段说明。 |  |
| 短链接生成方式：1.手动输入，2.根据自增id转为Base64码 | Short link generation method: 1. manual input, 2. convert auto-increment ID to Base64 | Swagger 字段说明。 |  |
| 可以唯一标识原始链接的uuid | UUID that uniquely identifies the original link | Swagger 字段说明。 |  |
| 原始url | Original URL | Swagger 字段说明。 |  |
| 是否删除:0=正常,1=删除 | Deleted flag: 0 = normal, 1 = deleted | Swagger 字段说明。 |  |
| 生成的短链code码 | Generated short link code | Swagger 字段说明。 |  |
| 造好物 |  | `brand-name` 品牌名配置，暂未修改。 |  |

## sms-service

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 短信回调异常通知 | SMS Callback Exception Alert | 告警一级标题。 |  |
| 处理回调状态失败【火山】 | Failed to process callback status [Volcengine] | 火山短信服务按 Volcengine 翻译。 |  |
| 短信发送延迟【火山】 | SMS Send Delay [Volcengine] | 告警标题。 |  |
| 短信发送失败+回调超时【火山】 | SMS Send Failed + Callback Timeout [Volcengine] | 告警标题。 |  |
| 短信发送失败【火山】 | SMS Send Failed [Volcengine] | 告警标题。 |  |
| 短信发送失败+回调超时，修改短信发送记录状态为失败，期望耗时不超过1分钟，实际耗时[...]ms | SMS send failed + callback timeout. The SMS record status was changed to failed. Expected duration is no more than 1 minute; actual duration [...]ms | 告警正文，变量占位保持原拼接。 |  |
| 短信发送失败，修改短信发送记录状态为失败，实际耗时[...]ms | SMS send failed. The SMS record status was changed to failed. Actual duration [...]ms | 告警正文，变量占位保持原拼接。 |  |
| 该状态码[...] | status code [...] | 告警计数对象。 |  |
| 短信回调超时，期望耗时不超过1分钟，实际耗时[...]ms | SMS callback timeout. Expected duration is no more than 1 minute; actual duration [...]ms | 告警正文，变量占位保持原拼接。 |  |
| 该模板 | this template | 告警计数对象。 |  |
| 今天...已经出现 X 次 | Today, ... has occurred X times | 告警计数句式。 |  |
| 今天...统计失败（Redis异常），无法获取出现次数 | Today, counting ... failed (Redis exception), unable to get occurrence count | 告警计数句式。 |  |
| 短信记录不存在 | SMS record does not exist | 告警正文。 |  |
| 该情况 | this case | 告警计数对象。 |  |
| 短信发送异常通知 | SMS Send Exception Alert | 告警一级标题。 |  |
| 【营销】短信异常行为 | [Marketing] Abnormal SMS Behavior | 告警标题。 |  |
| 延迟短信发mq失败。 cmd [%s] | Failed to send delayed SMS MQ message. cmd [%s] | 告警正文，保留 cmd 占位。 |  |
| 【营销】短信发送失败 | [Marketing] SMS Send Failed | 告警标题。 |  |
| 营销短信发送失败: | Marketing SMS send failed: | 告警正文。 |  |
| 延迟短信参数错误，empty smsRecordId | Delayed SMS parameter error: empty smsRecordId | 告警正文。 |  |
| 延迟短信记录不存在 | Delayed SMS record does not exist | 告警正文。 |  |
| %d小时%d分钟 | %d hours %d minutes | 延迟营销短信时间格式。 |  |
| 【通知】短信异常行为 | [Notification] Abnormal SMS Behavior | 告警标题。 |  |
| 通知短信1分钟内发送过于频繁: | Notification SMS sent too frequently within 1 minute: | 告警正文。 |  |
| 【通知】短信发送失败 | [Notification] SMS Send Failed | 告警标题。 |  |
| 通知短信发送失败: | Notification SMS send failed: | 告警正文。 |  |
| 【验证码】短信异常行为 | [Verification Code] Abnormal SMS Behavior | 告警标题。 |  |
| 验证码短信1分钟内发送过于频繁: | Verification code SMS sent too frequently within 1 minute: | 告警正文。 |  |
| 【验证码】短信发送失败 | [Verification Code] SMS Send Failed | 告警标题。 |  |
| 验证码短信发送失败: | Verification code SMS send failed: | 告警正文。 |  |
| 验证码校验失败次数超过30分钟50次，拉黑ip: | Verification code validation failed more than 50 times in 30 minutes; blocking IP: | 告警正文。 |  |
| 验证码校验失败次数超过30分钟10次，验证码已失效: | Verification code validation failed more than 10 times in 30 minutes; the verification code has been invalidated: | 告警正文。 |  |
| 【验证码】短信核销异常 | [Verification Code] SMS Write-Off Exception | 告警标题。 |  |
| 验证码短信核销失败: | Verification code SMS write-off failed: | 告警正文。 |  |
| 数美万物 |  | SMS `sign` 短信签名配置，暂未修改。 |  |

## user-center

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 生成签名失败 | Failed to generate signature | 小程序/App 签名生成接口错误提示。 |  |
| 获取素材失败，请稍后重试 | Failed to get media. Please try again later. | 微信开放平台素材获取接口错误提示。 |  |
| 成功 | success | 通用 API 成功响应，复用 messages_en.properties 的 errorCode.200。 |  |
| SHA-256 压缩失败 | SHA-256 compression failed | HashUtil SHA-256 压缩异常。 |  |
| 微信开放平台登录 | WeChat Open Platform login | 第三方登录类型枚举。 |  |
| 微信APP登录-iOS | WeChat App login - iOS | 第三方登录类型枚举。 |  |
| 微信APP登录-Android | WeChat App login - Android | 第三方登录类型枚举。 |  |
| 微信小程序登录 | WeChat Mini Program login | 第三方登录类型枚举。 |  |
| 手机登录 | Mobile login | 第三方登录类型枚举。 |  |
| Apple登录-iOS | Apple login - iOS | 第三方登录类型枚举。 |  |
| Google登录-APP | Google login - App | 第三方登录类型枚举。 |  |
| 临时登录凭证已过期或无效 | Temporary login credentials have expired or are invalid | 小程序静默登录凭证校验错误。 |  |
| 用户信息获取失败 | Failed to get user information | 小程序静默登录用户信息获取错误。 |  |
| 获取微信用户信息失败 | Failed to get WeChat user information | 微信小程序/APP 登录用户信息获取错误。 |  |
| 用户不存在 | User does not exist | 登录和内部用户接口不存在提示，部分场景复用现有 i18n errorCode.2004。 |  |
| 登录失败： | Login failed: | 登录异常前缀。 |  |
| 学校名称 | School Name | 教育账号 Excel 导入/导出表头。 |  |
| 学校代码 | School Code | 教育账号 Excel 导入/导出表头。 |  |
| 老师账号 | Teacher Account | 教育账号教师导入/导出表头。 |  |
| 老师手机号 | Teacher Mobile | 教育账号教师导入/导出表头。 |  |
| 创建状态 | Creation Status | 教育账号批量创建结果表头。 |  |
| 创建失败原因 | Failure Reason | 教育账号批量创建结果表头。 |  |
| 创建账号时间 | Account Creation Time | 教育账号批量创建结果表头。 |  |
| 创建批次 | Creation Batch | 教育账号批量创建结果表头。 |  |
| 入学年份 | Enrollment Year | 教育账号学生导入/导出表头。 |  |
| 班级 | Class | 教育账号学生导入/导出表头。 |  |
| 创建数量 | Creation Count | 教育账号学生导入/导出表头。 |  |
| 年级 | Grade | 教育账号学生创建结果表头。 |  |
| Excel模板格式错误，请使用正确的模板 | Invalid Excel template format. Please use the correct template. | 教育账号 Excel 模板校验错误。 |  |
| 失败 | Failed | 教育账号批量创建结果状态。 |  |
| 必填字段为空 | Required fields are empty | 教育账号 Excel 行校验失败原因。 |  |
| 学校代码必须为四位英文小写字母 | School code must be four lowercase English letters | 教育账号 Excel 行校验失败原因。 |  |
| 用户名必须为四位英文小写加两位数字 | Username must be four lowercase English letters followed by two digits | 教育账号教师账号格式校验失败原因。 |  |
| 手机号格式不正确 | Invalid mobile number format | 教育账号教师手机号校验失败原因。 |  |
| 用户名必须以学校代码开头 | Username must start with the school code | 教育账号教师账号校验失败原因。 |  |
| 用户名重复 | Duplicate username | 教育账号教师账号校验失败原因。 |  |
| 成功 | Succeeded | 教育账号批量创建结果状态。 |  |
| 用户名已存在:%s | Username already exists: %s | 教育账号批量创建失败原因，保留参数占位。 |  |
| 创建结果 | Creation Result | 教育账号批量创建结果 Sheet 名称。 |  |
| 入学年份必须为两位数字 | Enrollment year must be two digits | 教育账号学生导入行校验失败原因。 |  |
| 班级必须为两位数字 | Class must be two digits | 教育账号学生导入行校验失败原因。 |  |
| [学校代码+入学年份+班级]重复 | [School Code + Enrollment Year + Class] is duplicated | 教育账号学生导入行唯一性校验失败原因。 |  |
| 你获取的生成任务不存在 | The generation task you requested does not exist | 教育账号上传生成任务查询错误。 |  |
| 当前有正在生成的任务，请稍候重试 | A generation task is already in progress. Please try again later. | 教育账号批量生成任务并发限制提示。 |  |
| _结果 | _result | 教育账号批量创建结果文件名后缀。 |  |
| identityToken 或 bindToken 至少传一个 | Either identityToken or bindToken is required | Apple App 登录参数校验错误。 |  |
| identityToken 与 bindToken 不能同时传 | identityToken and bindToken cannot both be provided | Apple App 登录参数校验错误。 |  |
| 绑定时需传 mobile 和 smsCode | mobile and smsCode are required for binding | Apple/WeChat 绑定手机号参数校验错误。 |  |
| bindToken 已过期或无效，请重新发起 Apple 登录 | bindToken has expired or is invalid. Please start Apple login again. | Apple App 绑定登录 token 失效提示。 |  |
| 绑定数据异常 | Binding data is invalid | Apple/WeChat 绑定登录数据异常。 |  |
| 参数不能为空 | Parameters cannot be empty | 第三方外部用户绑定参数校验错误。 |  |
| identifierId长度不能超过256个字符 | identifierId cannot exceed 256 characters | 第三方外部用户绑定参数校验错误。 |  |
| 获取手机号失败 | Failed to get mobile number | 极光一键登录手机号获取错误。 |  |
| 保存用户信息失败 | Failed to save user information | 第三方登录用户保存错误。 |  |
| 极光认证失败 | Jiguang authentication failed | 极光一键登录认证错误。 |  |
| 极光认证失败: | Jiguang authentication failed: | 极光一键登录认证错误前缀。 |  |
| 服务配置异常 | Service configuration is invalid | 极光一键登录服务配置错误。 |  |
| 解密手机号失败 | Failed to decrypt mobile number | 极光一键登录手机号解密错误。 |  |
| code 或 bindToken 至少传一个 | Either code or bindToken is required | WeChat App 登录参数校验错误。 |  |
| code 与 bindToken 不能同时传 | code and bindToken cannot both be provided | WeChat App 登录参数校验错误。 |  |
| bindToken 已过期或无效，请重新发起微信登录 | bindToken has expired or is invalid. Please start WeChat login again. | WeChat App 绑定登录 token 失效提示。 |  |
| 获取微信access_token失败 | Failed to get WeChat access_token | WeChat App access_token 获取错误。 |  |
| 微信API调用失败: | WeChat API call failed: | WeChat API 错误前缀。 |  |
| 该手机号已注册过造好物账号，是否选择直接将微信号与该账号绑定 | This mobile number has already registered a zaohaowu account. Do you want to bind this WeChat account to it directly? | 微信开放平台手机号已注册时的绑定提示。 |  |
| 获取微信素材失败 | Failed to get WeChat media | 微信开放平台素材获取错误。 |  |
| 上传微信素材到tos失败 | Failed to upload WeChat media to TOS | 微信开放平台素材上传 TOS 错误。 |  |
| 消息处理失败 | Message processing failed | 微信推送消息处理结果错误。 |  |
| 账号格式不正确 | Invalid account format | 账号校验失败结果。 |  |
| 电商模块 | E-commerce Module | 权限菜单分类枚举展示名。 |  |
| 社区模块 | Community Module | 权限菜单分类枚举展示名。 |  |
| 其他后台工具 | Other Admin Tools | 权限菜单分类枚举展示名。 |  |
| 未绑定 | Not bound | 手机绑定状态枚举展示名。 |  |
| 已绑定 | Bound | 手机绑定状态枚举展示名。 |  |
| 白名单无需绑定 | Whitelisted, binding not required | 手机绑定状态枚举展示名。 |  |
| 菜单 | Menu | 权限类型枚举展示名。 |  |
| 操作 | Operation | 权限类型枚举展示名。 |  |
| 其他 | Other | 权限类型枚举展示名。 |  |
| 标签页 | Tab Page | 权限类型枚举展示名。 |  |
| 默认角色 | Default Role | 角色类型枚举展示名。 |  |
| 运营角色 | Operations Role | 角色类型枚举展示名。 |  |
| 权限码 | Permission code | 权限码唯一性校验参数名。 |  |
| 参数名不能为空 | Parameter name cannot be empty | URL 参数构造工具异常。 |  |
| 官方账号 | Official Account | 用户标签枚举展示名。 |  |
| 好物设计师 | Good Product Designer | 用户标签枚举展示名。 |  |
| 签约艺术家 | Signed Artist | 用户标签枚举展示名。 |  |
| 极光一键登录未配置 | Jiguang one-key login is not configured | App 一键登录配置缺失错误。 |  |
| 参数错误 | Parameter error | 内部外部用户接口参数错误响应。 |  |
| 未找到该业务方配置 | Business partner configuration not found | 外部用户业务方配置查询错误。 |  |
| 单次批量查询的手机号不能超过100个 | A single batch query cannot include more than 100 mobile numbers | 外部用户批量手机号查询限制。 |  |
| 用户ID列表不能为空 | User ID list cannot be empty | 外部用户批量用户查询参数校验。 |  |
| 单次批量查询的用户ID不能超过100个 | A single batch query cannot include more than 100 user IDs | 外部用户批量用户查询限制。 |  |
| 手机号不能为空 | Mobile number cannot be empty | 外部用户手机号绑定参数校验。 |  |
| 手机号格式错误 | The mobile number format is incorrect. | 外部用户手机号绑定参数校验，复用现有 i18n errorCode.2102。 |  |
| 第三方平台标识不能为空 | Third-party platform identifier cannot be empty | 外部用户第三方绑定参数校验。 |  |
| 第三方平台用户唯一标识不能为空 | Third-party platform user identifier cannot be empty | 外部用户第三方绑定参数校验。 |  |
| 用户ID不能为空 | User ID cannot be empty | 内部用户接口参数校验。 |  |
| 用户已经注销 | User has already been deleted | 内部注销接口幂等成功提示。 |  |
| platformName \|\| permissions 参数必须存在 | platformName and permissions are required | 权限保存接口参数校验。 |  |
| 保存失败 | Failed to save | 权限保存接口失败提示。 |  |
| 用户[%s]不存在 | User [%s] does not exist | 短信通知用户校验错误。 |  |
| 用户[%s]在手机号绑定白名单，跳过短信通知 | User [%s] is in the mobile binding whitelist. SMS notification skipped | 短信通知手机号绑定白名单跳过原因。 |  |
| 用户[%s]未绑定手机号，短信通知异常 | User [%s] has not bound a mobile number. SMS notification failed | 短信通知手机号缺失错误。 |  |
| 未查询到用户[%s]的手机号，短信通知异常 | Mobile number for user [%s] was not found. SMS notification failed | 短信通知手机号查询失败错误。 |  |
