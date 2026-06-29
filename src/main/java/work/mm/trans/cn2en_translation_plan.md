# CN to EN Translation Plan

## 目标

将 `zaohaowu_i18n` 下各后端项目中需要面向英文环境展示的中文文案迁移为可国际化、可维护的英文文案，并在翻译记录文档中保留中文原文、英文译文、业务场景和产品建议。

翻译记录文档：`cn2en_translation_record.md`

## 当前代码分支

所有子项目已切到 `feature/cn2en`，并基于 `main` 拉取最新代码。


## 当前执行状态

| 项目 | 状态 | 说明 |
|---|---|---|
| `order-java` | 已完成 | 已优先复用现有 i18n key，并完成翻译记录。 |
| `user-center` | 已完成代码翻译 | 已处理接口响应、第三方登录、教育账号 Excel、权限/角色/用户标签枚举、短信通知错误；`ApiModelProperty`、日志、告警、中文邮件模板和 properties 未处理。 |
| `operation-platform` | 暂停 | 用户确认先不翻译运营平台；已改动内容暂不回滚，后续不继续处理该项目。 |

## 统计口径

需要进入翻译范围：

- `.java`：非注释中文，包括异常文案、枚举展示名、接口/Excel 字段、通知、AI prompt、业务展示文案、会进入运行时展示或返回的 Nacos 配置默认值/兜底文案等。
- `.xml`：MyBatis SQL 中直接返回给接口/导出的中文展示值。
- `.html`：邮件模板中文。
- `.yml/.yaml`：真实配置值中的中文，不包含 `#` 注释。
- `.json`：prompt 配置等非字典中文。

不进入翻译范围：

- 代码注释、JavaDoc、配置注释、SQL `COMMENT`。
- 敏感词/审核词库。
- 地区/物流/承运商静态字典。
- 测试代码、纯技术文档。
- `.sh` 运维脚本文案。
- `.sql` 文件。
- `.properties` 不作为硬编码改造对象，单独做 i18n key 校验和缺失补齐。
- `log.info/warn/error/debug/trace` 等日志输出文案。
- Swagger 注解文案，例如 `ApiModelProperty`、`ApiModel`、`ApiOperation`、`Api`。
- 参数校验注解文案，例如 `NotBlank`、`NotEmpty`、`NotNull` 的 `message`。
- 告警相关文案，例如 Feishu/Lark alarm、风控或运维告警卡片。
- `NacosConfig` 中的 `promotionChannelConfig` 默认 JSON。

当前清单规模：`1143` 个文件 / `8319` 行非注释、非日志中文。

| 类型 | 文件数 | 行数 |
|---|---:|---:|
| `.java` | 1094 | 8128 |
| `.yml` | 41 | 52 |
| `.xml` | 2 | 104 |
| `.html` | 6 | 35 |

完整文件清单：`/Users/rogerswang/my/work/zaohaowu_i18n/cn2en_translation_files.md`

## 落地原则

- 如果项目/模块已有多语言资源、配置或工具类，优先复用现有机制，不新增一套并行方案。
- 如果没有现成多语言机制，本轮不强行搭建新框架，直接在原代码/模板/配置中将中文翻译为英文。
- 所有直接翻译和通过 i18n key 间接翻译的文案，都需要同步记录到 `cn2en_translation_record.md`。
- 只有在直接翻译会破坏中文环境、接口兼容或数据结构时，再单独评估是否补充轻量 i18n 结构。

现有可优先复用的项目/机制：

| 项目 | 现有机制 | 处理策略 |
|---|---|---|
| `order-java` | `messages_zh.properties` / `messages_en.properties`、`I18nMessageService`、`OrderI18nKeys` | 优先新增/复用 key |
| `jjewelry-java` | `messages_zh.properties` / `messages_en.properties`、`LocaleConfig`、`I18nText`、`MessageUtils` | 资源文案走 properties，站内信/消息走现有多语言结构 |
| `user-center` | `messages_zh.properties` / `messages_en.properties`、`LocaleConfig`、i18n 登录链路 | 优先新增/复用 key 和现有 i18n 登录/邮件逻辑 |
| `notify-java` | `LocaleConfig` / `MessageSource` 机制，但当前未找到 `messages_*.properties` | 如涉及该项目，先确认是否补资源；否则直接英文化 |
| 其他项目 | 未发现完整多语言资源体系 | 直接在代码/模板/配置中翻译为英文 |

## 翻译记录要求

每次修改中文文案时，必须同步更新 `cn2en_translation_record.md`。

每个项目一个章节，表格固定四列：

| 中文 | English | 业务场景 | 产品建议 |
|---|---|---|---|
| 原始中文 | 英文译文 | 文案所在业务场景/用途 | 留空，等待产品决定最终英文 |

记录原则：

- 同一中文在不同业务语境中含义不同，需要分别记录。
- 品牌名、活动名、业务专有名词不确定时，英文列可留空或填写当前暂定译法，产品建议列保持为空。
- 面向用户的文案优先保证自然英文；面向后台/运营的文案优先保证准确一致。
- AI prompt 的翻译需要保留模型约束、输出格式、变量占位符和 JSON schema。

## 处理优先级

1. 用户可见错误、参数校验、登录/下单/支付/生成链路提示。
2. 邮件、短信、推送、站内信等触达文案。
3. App/API 直接返回的枚举状态、业务状态、按钮/标签/原因文案。
4. `.xml` 中导出或接口返回的中文 CASE 映射。
5. AI prompt 和模型输出约束。
6. 后台/运营平台 Swagger、Excel、导出字段。
7. 配置值等低频展示文案中仍需英文环境展示的部分。
8. 告警与 `log.*` 日志不处理。

## 改造策略

### Java 文案

- 有现成多语言机制的模块优先复用现有 `messages_zh.properties` / `messages_en.properties`、已有 key 常量、`I18nText` / `MessageUtils` 等本地能力。
- 异常消息、接口返回消息优先迁移到 i18n key；参数校验注解 `message` 暂不处理。
- 枚举展示名优先增加英文展示字段或通过 code + i18n key 解析，避免直接替换导致中文场景回退困难。
- Java 中 `@NacosValue`、`@NacosConfigurationProperties` 绑定类、Nacos 配置默认值和兜底文案，只处理会进入运行时返回、触达消息、AI prompt 或展示逻辑的内容；纯注释、纯内部配置、以及 `NacosConfig.promotionChannelConfig` 不处理。
- 没有现成多语言机制的模块，本轮直接将中文硬编码翻译为英文，避免为单点文案引入新框架。
- Swagger 注解文案本轮不处理；Excel 字段如果导出给英文用户/运营，需要翻译。

### HTML 模板

- 邮件模板按 locale 拆分，例如 `zh-CN` 与 `en-US`。
- 保留变量占位符顺序和数量。
- 邮件标题、正文、按钮文案都需要记录到翻译记录表。

### XML Mapper

- MyBatis 中的中文 CASE 文案需要迁出或按英文场景新增查询/字段映射。
- 优先避免在 SQL 内硬编码多语言；如果短期改动风险较高，可先按英文场景单独输出英文 label。

### AI Prompt / JSON 配置

- prompt 翻译要保持任务目标、输出格式、JSON 字段、限制条件不变。
- 涉及品类、材质、风格、审核规则的词，需要产品确认英文术语。

### YML / JSON

- 配置值只处理真实展示值，不处理注释。
- JSON 只处理 prompt 等非字典配置，不处理地区/物流/承运商字典。

## 验证方式

- 翻译后重新扫描非注释中文，确认目标范围内剩余中文为可接受项。
- 对已有 i18n properties 做中英文 key 一致性校验。
- 对核心链路做接口/单测或最小回归验证，重点覆盖登录、支付、订单、生成、通知。
- 对 HTML 邮件模板检查变量占位符数量一致。
- 对 XML SQL 查询确认字段别名和返回结构不变。

## 当前待确认项

| 项目 | 问题 | 建议 |
|---|---|---|
| 全局 | 品牌名 `造好物` 是否翻译 | 建议产品确认统一英文品牌名，未确认前保留原品牌或使用占位建议 |
| operation-platform | 用户已确认先不翻译运营平台 | 暂停后续处理；此前已改动内容暂不回滚 |
| AI prompt | 中文 prompt 是否直接翻译为英文 | 需要结合模型效果验证，不建议只做机械翻译 |
