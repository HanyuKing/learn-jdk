# 商品国际化专项设计方案

## 1. 文档目的

本文档是《电商国际化总体架构设计方案》的商品域专项展开，重点解决国内商品体系向国际化商品体系演进的问题。

商品国际化不等于商品标题翻译，而是围绕一个 SKU 在不同国家和渠道的可售性、合规性、内容表达、规格单位、类目映射、包装标签、税务清关、媒体素材和上架审核建立完整能力。

本文档适用于以下场景：

- 国内电商商品中心准备支持海外站点。
- 自营跨境电商准备做多国家销售。
- 现有 PIM、ERP、WMS、OMS 需要补齐国际化商品字段。
- 商品要同步到第三方海外平台或本地渠道。

## 2. 核心原则

### 2.1 全球商品主数据与市场 Listing 分离

同一个商品在全球只有一个基础身份，但在不同市场有不同的本地展示、合规要求和上架状态。

```text
Global SPU / SKU
        |
        +-- US Listing
        +-- EU-DE Listing
        +-- GB Listing
        +-- SG Listing
```

全球商品主数据回答“这是什么商品”，市场 Listing 回答“这个商品能否在某个市场以什么方式销售”。

### 2.2 可售性先于上架

商品上架前必须完成目标市场可售性校验。可售性不仅看库存和价格，还要看：

- 商品类目是否允许销售。
- 是否缺少认证、测试报告或授权文件。
- 物流是否可运输。
- 清关资料是否完整。
- 标签、说明书、警示语是否符合目标市场要求。
- 是否存在营销宣称风险。

### 2.3 内容本地化不是直译

商品标题、卖点、详情页和图片文案需要结合本地语言习惯、搜索词、法规限制、用户关注点和渠道规则重新表达。

例如国内商品标题常见“爆款、神器、全网最低、医用级、永久、100%有效”等表达，在海外市场可能触发平台审核、广告审核或消费者保护风险。

### 2.4 商品资料要版本化

商品资料会随供应商、产地、批次、材质、包装、认证、法规变化而变化。需要对关键资料做版本管理：

- 商品主数据版本。
- Listing 内容版本。
- 认证文件版本。
- 标签和说明书版本。
- 包装图版本。
- 合规审核版本。

## 3. 商品国际化能力地图

商品国际化建议拆成 8 个能力模块。

```text
商品主数据
    |
类目与属性体系
    |
本地化内容
    |
规格单位与尺码
    |
媒体与包装标签
    |
商品合规资料
    |
市场可售性规则
    |
渠道发布与生命周期管理
```

## 4. 领域模型设计

### 4.1 Global Product

全球商品表示跨市场共用的商品基础资料。

```text
global_spu_id
brand_id
global_category_id
product_type
product_name_cn
manufacturer
supplier_id
country_of_origin
material
intended_use
target_user_group
created_at
updated_at
```

关键点：

- `global_spu_id` 不跟国家绑定。
- `product_type` 用于驱动合规规则，例如电子产品、儿童用品、纺织品、食品接触材料。
- `intended_use` 和 `target_user_group` 会影响商品安全、标签和认证要求。

### 4.2 Global SKU

全球 SKU 表示具体可售规格。

```text
global_sku_id
global_spu_id
barcode
gtin
model_number
color
size
material
net_weight
gross_weight
package_length
package_width
package_height
battery_flag
liquid_flag
powder_flag
magnetic_flag
hazardous_flag
```

关键点：

- 重量、体积、危险品属性会影响物流可达性和运费。
- 条码、GTIN、型号会影响第三方渠道发布和清关。
- 电池、液体、粉末、磁性等属性必须结构化，不能放在备注里。

### 4.3 Market Listing

市场 Listing 表示商品在某个市场的本地化上架资料。

```text
listing_id
global_spu_id
market_code
locale
local_category_id
title
subtitle
bullet_points
description
search_keywords
brand_display_name
selling_points
warning_text
instruction_language
online_status
approval_status
```

关键点：

- 同一市场可能有多个语言版本，例如加拿大可能需要英语和法语。
- 标题、卖点、详情页和警示语需要分别管理。
- `online_status` 表示是否上架，`approval_status` 表示是否通过业务、合规、内容审核。

### 4.4 Market SKU

市场 SKU 表示某个具体 SKU 在目标市场的销售状态和市场差异。

```text
market_sku_id
global_sku_id
market_code
local_sku_code
local_barcode
size_display
color_display
unit_display
sellable_flag
restricted_reason
launch_date
delist_date
```

关键点：

- 同一全球 SKU 在不同国家可以有不同的本地 SKU 编码。
- 尺码、颜色、单位展示需要本地化。
- 市场维度要允许禁售、暂缓销售和限渠道销售。

### 4.5 Compliance Profile

商品合规档案用于描述商品进入某市场所需的合规条件。

```text
compliance_profile_id
global_sku_id
market_code
product_regulation_category
required_certificates
required_labels
required_warnings
required_documents
responsible_person_required
importer_required
status
reviewed_by
reviewed_at
```

关键点：

- 合规档案应该按 SKU + 市场维护。
- 对于同一 SPU 下不同 SKU，如果材质、电池、适用年龄不同，合规结论可能不同。
- `status` 不应只有通过/拒绝，建议支持资料缺失、待复核、过期、限售等状态。

### 4.6 Product Document

商品文件中心用于管理认证、测试报告、说明书、标签、包装图等资料。

```text
document_id
global_sku_id
market_code
document_type
file_url
issuer
issue_date
expiry_date
version
status
```

常见文件类型：

- 测试报告。
- 符合性声明。
- 儿童产品证书。
- 安全数据表 SDS/MSDS。
- 产品说明书。
- 标签文件。
- 包装展开图。
- 授权书。
- 原产地证明。
- 进口商或责任人信息。

## 5. 类目与属性国际化

### 5.1 全球类目与本地类目分离

国内类目不能直接复用到海外市场。建议建立三层类目：

```text
Global Category
        |
Market Category
        |
Channel Category
```

- Global Category：内部统一商品管理类目。
- Market Category：目标国家或地区的经营类目。
- Channel Category：第三方平台、广告平台、比价平台的类目。

例如：

```text
Global Category: 家用小电器 > 厨房电器 > 便携榨汁机
US Market Category: Home & Kitchen > Kitchen Appliances > Personal Blenders
Channel Category: Amazon US 对应类目 / Google Product Category
```

### 5.2 属性体系

属性需要按类目定义，并区分全局属性和本地属性。

全局属性：

- 品牌。
- 型号。
- 材质。
- 重量。
- 尺寸。
- 颜色。
- 功率。
- 电压。
- 电池容量。
- 适用年龄。

本地属性：

- 本地尺码。
- 本地单位。
- 本地能效等级。
- 本地安全警示。
- 本地税务分类。
- 本地物流限制。
- 本地搜索关键词。

### 5.3 属性治理

属性设计要避免自由文本泛滥：

- 能枚举的字段使用枚举。
- 能结构化的字段不要写备注。
- 单位字段必须拆成数值和单位。
- 属性变更要触发下游重算，例如运费、税费、合规和搜索索引。

## 6. 内容本地化设计

### 6.1 内容结构

商品内容建议拆成结构化字段，而不是只有一个富文本详情。

```text
title
subtitle
bullet_points
short_description
long_description
specifications
package_includes
usage_instruction
care_instruction
safety_warning
faq
seo_title
seo_description
seo_keywords
```

结构化字段的好处：

- 便于翻译工作流。
- 便于渠道映射。
- 便于合规审核。
- 便于搜索和推荐。
- 便于多端展示。

### 6.2 翻译工作流

建议使用“机器初译 + 术语库 + 人工审核 + 合规审核”的流程。

```text
中文源内容
    |
术语库和禁用词检查
    |
机器翻译
    |
本地语言编辑
    |
合规和营销宣称审核
    |
发布
```

需要建设：

- 品牌术语库。
- 类目术语库。
- 禁用词库。
- 营销宣称风险词库。
- 渠道标题长度规则。
- 本地 SEO 关键词库。

### 6.3 标题规则

标题不建议直接沿用国内长标题模式。

建议结构：

```text
Brand + Product Type + Key Attribute + Model/Size + Pack/Variant
```

示例：

```text
Portable Blender, 16 oz Personal Mixer, USB-C Rechargeable, White
```

标题要避免：

- 夸张宣传。
- 未证实的功效。
- 绝对化表达。
- 与竞品品牌碰瓷。
- 关键词堆砌。
- 不符合平台规则的符号和促销词。

### 6.4 详情页规则

详情页需要针对海外用户重构信息顺序：

1. 这个商品是什么。
2. 适合什么场景。
3. 核心规格。
4. 如何使用。
5. 包装内有什么。
6. 安全注意事项。
7. 保修、退货和客服信息。

国内详情页常见的大量营销海报、长图和嵌字图片不适合直接出海。海外站点更需要结构化文字、清晰规格、真实场景图和明确的售后信息。

## 7. 规格、单位与尺码

### 7.1 单位转换

系统要支持单位数值和展示分离。

```text
base_value
base_unit
display_value
display_unit
market_code
```

示例：

- 长度：cm、m、inch、feet。
- 重量：g、kg、oz、lb。
- 容量：ml、L、fl oz。
- 温度：Celsius、Fahrenheit。
- 电压：110V、220V、100-240V。

### 7.2 尺码映射

服饰、鞋靴、戒指、家纺等品类要建立尺码映射表。

```text
global_size
market_code
display_size
body_measurement
product_measurement
tolerance
```

尺码页需要展示：

- 商品实测尺寸。
- 人体参考尺寸。
- 单位换算。
- 测量方式。
- 误差范围。

尺码国际化直接影响退货率，应作为重点治理对象。

## 8. 媒体、包装与标签

### 8.1 商品媒体

建议建立 DAM 管理商品图片、视频、说明图和包装图。

媒体字段建议包括：

```text
asset_id
global_spu_id
market_code
locale
asset_type
usage_scene
file_url
text_embedded_flag
approval_status
version
```

设计要求：

- 避免把大量文字嵌入图片，降低翻译和合规审核成本。
- 主图要符合渠道规则，避免过度设计和虚假效果。
- 场景图要符合本地生活习惯。
- 图片中的插头、包装、标签、尺寸单位要与目标市场一致。
- 视频字幕和旁白需要本地化。

### 8.2 包装与标签

包装标签是商品国际化的关键，不应在发货前临时补贴纸。

需要按市场维护：

- 商品名称。
- 型号。
- 原产国。
- 制造商。
- 进口商。
- 欧盟责任人或授权代表。
- 批次号。
- 条码。
- 警示语。
- 使用说明。
- 年龄标识。
- 材质和成分。
- 回收标识。

对于欧盟部分商品，进口商、责任人、语言、警示语和产品安全信息会影响可售性。欧盟 GPSR 对线上销售、直接进口和产品安全责任提出了更高要求。

参考：<https://commission.europa.eu/document/download/a281b150-19fd-44f9-bef8-c6018f9c4792_en?filename=new_general_product_safety_regulation_-_factsheet.pdf>

## 9. 商品合规体系

### 9.1 合规分类

商品进入目标市场前，需要先做合规分类。

分类维度：

- 普通消费品。
- 电子电器。
- 射频设备。
- 儿童用品。
- 玩具。
- 纺织品。
- 食品接触材料。
- 化妆品。
- 医疗器械。
- 电池和带电商品。
- 液体、粉末、磁性、易燃品。
- 艺术材料和化学品。

不同分类对应不同的准入文件、标签、测试报告和运输限制。

### 9.2 欧盟常见关注点

欧盟商品上架需要关注：

- CE 标志适用性。
- 符合性声明。
- 技术文档。
- 产品安全信息。
- 使用说明语言。
- 进口商或授权代表信息。
- GPSR 产品安全义务。
- 化学品、环保、能效、包装等专项规则。

CE 标志不是所有商品都适用，只适用于受相关欧盟法规覆盖的产品。对适用 CE 的产品，制造商、进口商和分销商都有相应义务。

参考：<https://webgate.acceptance.ec.europa.eu/portal9/en/content/machinery-and-technical-products>

### 9.3 美国常见关注点

美国商品上架需要关注：

- CPSC 消费品安全要求。
- 儿童产品 CPC。
- 儿童产品追踪标签。
- FCC 射频设备授权。
- FDA 相关品类要求。
- FTC 广告和营销宣称。
- 州级要求，例如加州 Prop 65。

儿童产品在美国需要特别谨慎。CPSC 指导中说明，儿童产品需要可见、清晰并尽可能永久附着在产品和包装上的 tracking label；儿童产品在适用安全规则时还需要 Children’s Product Certificate。

参考：

- <https://www.cpsc.gov/Business--Manufacturing/Business-Education/tracking-label>
- <https://www.cpsc.gov/Business--Manufacturing/Online-Sellers-Safety-Guide/Common-Ecommerce-Safety-Violations>

射频设备在美国进口或销售前，需要满足 FCC 设备授权要求。

参考：<https://opendata.fcc.gov/Engineering-Technology/EAS-Equipment-Authorization-Grantee-Registrations/3b3k-34jp>

### 9.4 合规状态机

建议商品合规状态不要简单设计为通过或拒绝，而是设计为：

```text
DRAFT              草稿
INFO_REQUIRED      资料待补充
UNDER_REVIEW       审核中
APPROVED           已通过
CONDITION_APPROVED 条件通过
REJECTED           拒绝
EXPIRED            文件过期
SUSPENDED          暂停销售
RECALL_REQUIRED    需要召回
```

上架系统必须依赖合规状态，不允许绕过合规直接发布。

## 10. 市场可售性规则中心

### 10.1 可售性判断

市场可售性服务建议提供统一接口：

```text
checkSellability(global_sku_id, market_code, channel_code)
```

返回结果：

```json
{
  "sellable": false,
  "blockers": [
    {
      "code": "MISSING_CERTIFICATE",
      "message": "缺少目标市场要求的测试报告"
    }
  ],
  "warnings": [
    {
      "code": "LABEL_REVIEW_REQUIRED",
      "message": "包装标签需要人工复核"
    }
  ],
  "required_actions": [
    "UPLOAD_TEST_REPORT",
    "CONFIRM_IMPORTER_INFO"
  ]
}
```

### 10.2 可售性规则输入

可售性规则需要读取：

- 商品类目。
- 目标市场。
- 渠道。
- 商品属性。
- 危险品属性。
- 认证文件。
- 标签文件。
- 物流可达性。
- 税务分类。
- 库存仓位置。
- 品牌授权状态。

### 10.3 可售性拦截点

可售性校验应接入以下节点：

- 商品创建。
- 商品编辑。
- Listing 提交。
- 上架审批。
- 价格发布。
- 加购物车。
- 结算页。
- 下单。
- 仓库发货。

商品在上架时通过合规，不代表永远可售。法规、文件有效期、物流政策和渠道规则变化都可能导致商品后续不可售。

## 11. 与交易链路的接口

### 11.1 对价格税务的输出

商品域需要向价格和税务系统提供：

- 商品税务分类。
- HS Code。
- 原产国。
- 材质。
- 商品用途。
- 是否食品、药品、医疗、儿童、电子、电池。
- 是否需要特殊税率。

### 11.2 对物流履约的输出

商品域需要向物流系统提供：

- 重量。
- 体积。
- 包装尺寸。
- 电池属性。
- 液体、粉末、磁性、易燃属性。
- 是否危险品。
- 是否易碎。
- 温控要求。
- 清关英文品名。

### 11.3 对订单清关的输出

商品域需要向订单和清关系统提供：

- 英文申报名。
- HS Code。
- 原产国。
- 申报价值。
- 商品数量。
- 材质。
- 用途。
- 品牌。
- 型号。
- 清关所需证书。

### 11.4 对搜索推荐的输出

商品域需要向搜索推荐系统提供：

- 本地语言标题。
- 本地关键词。
- 结构化属性。
- 类目。
- 品牌。
- 价格区间。
- 用户场景标签。
- 禁推、限推、限广告标识。

## 12. 上架流程设计

建议商品国际化上架流程如下：

```text
商品立项
    |
全球商品主数据创建
    |
目标市场选择
    |
类目与属性补全
    |
合规分类和资料要求生成
    |
供应商补充证书、测试报告、标签、说明书
    |
内容本地化和媒体本地化
    |
价格、税务、物流可达性校验
    |
业务审核
    |
合规审核
    |
渠道规则审核
    |
发布上架
    |
售后、投诉、召回和下架监控
```

## 13. 系统建设建议

### 13.1 PIM

PIM 负责商品主数据、类目、属性、SKU、条码、规格、合规字段和市场 Listing。

核心能力：

- 全球商品主数据。
- 市场 Listing。
- 属性模板。
- 批量导入导出。
- 变更审批。
- 版本管理。
- 数据完整度评分。

### 13.2 TMS

TMS 负责翻译和本地化流程。

核心能力：

- 多语言资源管理。
- 术语库。
- 禁用词库。
- 翻译任务流转。
- 人工审核。
- 翻译记忆库。
- 发布前差异对比。

### 13.3 DAM

DAM 负责商品媒体资产。

核心能力：

- 图片、视频、说明图、包装图管理。
- 市场和语言标签。
- 授权和版权管理。
- 版本管理。
- 渠道素材裁剪。
- 媒体审核。

### 13.4 合规规则中心

合规规则中心负责商品准入和资料要求。

核心能力：

- 国家级合规规则。
- 类目级资料要求。
- 商品属性触发规则。
- 文件有效期管理。
- 过期提醒。
- 禁售和限售规则。
- 审批记录和审计日志。

### 13.5 渠道发布中心

渠道发布中心负责把商品发布到自建站、App、第三方平台和广告渠道。

核心能力：

- 类目映射。
- 属性映射。
- 标题长度和图片规则校验。
- 发布任务。
- 发布结果回写。
- 错误重试。
- 渠道状态同步。

## 14. 数据质量标准

商品进入国际市场前，建议设置数据完整度评分。

必填项：

- 商品英文名。
- 本地标题。
- 本地详情。
- 商品主图。
- 净重和毛重。
- 包装尺寸。
- 原产国。
- HS Code。
- 材质。
- 用途。
- 清关英文品名。
- 售后政策。
- 目标市场类目。

按品类必填：

- 电压、功率、插头类型。
- 电池容量、锂电池类型。
- 适用年龄。
- 尺码表。
- 成分表。
- 警示语。
- 使用说明。
- 测试报告。
- 符合性声明。
- SDS/MSDS。

## 15. 组织分工

建议使用以下职责划分：

| 角色 | 职责 |
| --- | --- |
| 商品经理 | 选品、商品定位、目标市场策略 |
| 商品运营 | 资料维护、上架流程、渠道发布 |
| 合规负责人 | 准入规则、认证文件、标签审核 |
| 内容本地化 | 翻译、编辑、SEO、营销表达 |
| 设计/视频团队 | 图片、视频、包装和标签素材 |
| 供应链团队 | 供应商资料、产地、批次、包装 |
| 物流团队 | 运输限制、仓配可达性、清关资料 |
| 技术团队 | PIM、规则中心、工作流、发布系统 |
| 法务/税务 | 法规、责任主体、税务分类和风险审核 |

## 16. 阶段性落地计划

### 16.1 第一期：商品国际化最小闭环

目标：支持一个国家、一个品类上线。

建设内容：

- Global SPU/SKU。
- Market Listing。
- 多语言标题和详情。
- 国际重量、尺寸和包装字段。
- HS Code、原产国、清关英文品名。
- 基础合规资料上传。
- 上架审核流程。
- 可售性校验接口。

### 16.2 第二期：规模化商品治理

目标：支持多个国家和多个品类批量上架。

建设内容：

- 类目和属性模板。
- 商品资料完整度评分。
- 术语库和禁用词库。
- 合规规则中心。
- 文件有效期和过期提醒。
- 批量导入导出。
- 渠道类目和属性映射。

### 16.3 第三期：智能化和自动化

目标：提升上架效率和降低合规风险。

建设内容：

- 自动识别缺失属性。
- 自动生成资料清单。
- 自动翻译初稿。
- 自动检查禁用词和风险宣称。
- 自动推荐目标市场类目。
- 自动触发文件过期下架或复核。
- 售后投诉和商品质量问题反哺合规规则。

## 17. 商品国际化上线检查清单

### 17.1 商品基础

- 是否有全球 SPU/SKU。
- 是否有目标市场 Listing。
- 是否有本地标题、详情、卖点和关键词。
- 是否有本地类目。
- 是否有完整规格属性。
- 是否有主图、场景图和必要视频。

### 17.2 合规资料

- 是否确认目标市场可售。
- 是否完成商品合规分类。
- 是否上传所需证书和测试报告。
- 是否完成标签和说明书审核。
- 是否确认原产国和制造商信息。
- 是否确认进口商、责任人或授权代表信息。
- 是否确认文件有效期。

### 17.3 税务清关

- 是否维护 HS Code。
- 是否维护清关英文品名。
- 是否维护申报要素。
- 是否维护商品税务分类。
- 是否确认申报价值规则。

### 17.4 物流履约

- 是否维护净重、毛重和包装尺寸。
- 是否确认电池、液体、粉末、磁性、危险品属性。
- 是否确认目标国家可配送。
- 是否确认仓库和物流渠道可支持。

### 17.5 内容与渠道

- 是否通过本地语言审核。
- 是否通过营销宣称审核。
- 是否符合渠道标题、图片、属性和类目规则。
- 是否通过 SEO 和搜索关键词检查。
- 是否完成发布预览。

## 18. 关键指标

商品国际化需要关注以下指标：

- 商品资料完整度。
- 商品合规通过率。
- 商品上架周期。
- 翻译返工率。
- Listing 审核驳回率。
- 渠道发布失败率。
- 商品因合规下架次数。
- 清关资料错误率。
- 商品维度退货率。
- 尺码原因退货率。
- 商品质量投诉率。
- 产品召回和风险事件数。

## 19. 落地表结构设计

以下表结构基于 MySQL 8.0 设计，默认使用 InnoDB、utf8mb4、UTC 时间。生产环境如果采用分库分表或微服务拆库，可以不建物理外键，但字段、唯一约束和索引应保留。

### 19.1 建表约定

1. `market_code` 使用业务市场编码，例如 `US`、`GB`、`EU-DE`、`SG`。
2. `locale` 使用语言地区编码，例如 `en-US`、`de-DE`、`fr-FR`。
3. 状态字段使用 `varchar`，避免 enum 后期扩展困难。
4. 金额类字段不放在商品域，进入价格中心；商品域只维护商品、合规、清关、物流和内容字段。
5. 重量统一按克存储，尺寸统一按毫米存储，前端按市场转换展示。
6. JSON 字段用于动态属性、资料要求和规则结果；核心检索字段必须结构化，不能只放 JSON。

### 19.2 市场表：`intl_market`

```sql
CREATE TABLE intl_market (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码，如 US、GB、EU-DE、SG',
    market_name VARCHAR(128) NOT NULL COMMENT '市场名称',
    country_code CHAR(2) NOT NULL COMMENT 'ISO 3166-1 alpha-2 国家代码',
    region_code VARCHAR(32) DEFAULT NULL COMMENT '区域编码，如 NA、EU、SEA',
    default_locale VARCHAR(16) NOT NULL COMMENT '默认语言，如 en-US',
    default_currency CHAR(3) NOT NULL COMMENT '默认币种，如 USD',
    timezone VARCHAR(64) NOT NULL COMMENT '市场默认时区，如 America/Los_Angeles',
    tax_mode VARCHAR(32) NOT NULL DEFAULT 'TAX_EXCLUDED' COMMENT '税模式：TAX_INCLUDED、TAX_EXCLUDED、TAX_ESTIMATE',
    default_fulfillment_mode VARCHAR(32) NOT NULL DEFAULT 'CROSS_BORDER' COMMENT '默认履约：CROSS_BORDER、OVERSEA_WAREHOUSE、LOCAL',
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE、DISABLED',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id),
    UNIQUE KEY uk_market_code (market_code),
    KEY idx_country_code (country_code),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='国际化市场配置表';
```

### 19.3 市场语言表：`intl_market_locale`

```sql
CREATE TABLE intl_market_locale (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    locale VARCHAR(16) NOT NULL COMMENT '语言编码，如 en-US、fr-CA',
    locale_name VARCHAR(64) NOT NULL COMMENT '语言名称',
    is_default TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否默认语言',
    content_required TINYINT(1) NOT NULL DEFAULT 1 COMMENT '该语言内容是否必填',
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE、DISABLED',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id),
    UNIQUE KEY uk_market_locale (market_code, locale),
    KEY idx_locale (locale)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='市场支持语言表';
```

### 19.4 品牌表：`pim_brand`

```sql
CREATE TABLE pim_brand (
    brand_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
    brand_code VARCHAR(64) NOT NULL COMMENT '品牌编码',
    brand_name VARCHAR(128) NOT NULL COMMENT '品牌名称',
    display_name VARCHAR(128) DEFAULT NULL COMMENT '默认展示名称',
    owner_company VARCHAR(256) DEFAULT NULL COMMENT '品牌权利人或所属公司',
    trademark_no VARCHAR(128) DEFAULT NULL COMMENT '商标注册号',
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE、DISABLED',
    created_by VARCHAR(64) DEFAULT NULL,
    updated_by VARCHAR(64) DEFAULT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (brand_id),
    UNIQUE KEY uk_brand_code (brand_code),
    KEY idx_brand_name (brand_name),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='品牌主数据表';
```

### 19.5 全球类目表：`pim_global_category`

```sql
CREATE TABLE pim_global_category (
    category_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '全球类目ID',
    parent_id BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父类目ID，根节点为0',
    category_code VARCHAR(64) NOT NULL COMMENT '类目编码',
    category_name_cn VARCHAR(128) NOT NULL COMMENT '中文类目名',
    category_name_en VARCHAR(128) DEFAULT NULL COMMENT '英文类目名',
    category_level INT NOT NULL COMMENT '类目层级，从1开始',
    category_path VARCHAR(512) NOT NULL COMMENT '类目路径，如 /1/10/100/',
    product_type VARCHAR(64) DEFAULT NULL COMMENT '默认商品类型，用于合规规则',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '排序',
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE、DISABLED',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (category_id),
    UNIQUE KEY uk_category_code (category_code),
    KEY idx_parent_id (parent_id),
    KEY idx_product_type (product_type),
    KEY idx_status_sort (status, sort_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='全球商品类目表';
```

### 19.6 市场类目表：`pim_market_category`

```sql
CREATE TABLE pim_market_category (
    market_category_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '市场类目ID',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    locale VARCHAR(16) NOT NULL COMMENT '语言编码',
    parent_id BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父市场类目ID',
    category_code VARCHAR(128) NOT NULL COMMENT '市场类目编码',
    category_name VARCHAR(256) NOT NULL COMMENT '市场本地类目名',
    category_level INT NOT NULL COMMENT '类目层级',
    category_path VARCHAR(1024) NOT NULL COMMENT '市场类目路径',
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE、DISABLED',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (market_category_id),
    UNIQUE KEY uk_market_category_code (market_code, locale, category_code),
    KEY idx_market_parent (market_code, parent_id),
    KEY idx_market_status (market_code, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='市场本地类目表';
```

### 19.7 类目映射表：`pim_category_mapping`

```sql
CREATE TABLE pim_category_mapping (
    mapping_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '映射ID',
    global_category_id BIGINT UNSIGNED NOT NULL COMMENT '全球类目ID',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    market_category_id BIGINT UNSIGNED NOT NULL COMMENT '市场类目ID',
    mapping_status VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING、APPROVED、REJECTED',
    confidence_score DECIMAL(5,2) DEFAULT NULL COMMENT '自动映射置信度，0-100',
    approved_by VARCHAR(64) DEFAULT NULL COMMENT '审核人',
    approved_at DATETIME(3) DEFAULT NULL COMMENT '审核时间',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (mapping_id),
    UNIQUE KEY uk_global_market_category (global_category_id, market_code),
    KEY idx_market_category (market_code, market_category_id),
    KEY idx_mapping_status (mapping_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='全球类目与市场类目映射表';
```

### 19.8 属性定义表：`pim_attribute_def`

```sql
CREATE TABLE pim_attribute_def (
    attr_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '属性ID',
    attr_code VARCHAR(64) NOT NULL COMMENT '属性编码',
    attr_name_cn VARCHAR(128) NOT NULL COMMENT '中文属性名',
    attr_name_en VARCHAR(128) DEFAULT NULL COMMENT '英文属性名',
    value_type VARCHAR(32) NOT NULL COMMENT '值类型：TEXT、NUMBER、BOOLEAN、ENUM、JSON、DATE',
    unit_group VARCHAR(64) DEFAULT NULL COMMENT '单位组：LENGTH、WEIGHT、VOLUME、TEMPERATURE',
    attr_scope VARCHAR(32) NOT NULL DEFAULT 'GLOBAL' COMMENT '作用域：GLOBAL、MARKET、CHANNEL',
    is_required TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否通用必填',
    is_searchable TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否可搜索',
    is_variant_attr TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否销售规格属性，如颜色、尺码',
    is_compliance_related TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否影响合规',
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE、DISABLED',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (attr_id),
    UNIQUE KEY uk_attr_code (attr_code),
    KEY idx_scope_status (attr_scope, status),
    KEY idx_compliance (is_compliance_related),
    KEY idx_variant (is_variant_attr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品属性定义表';
```

### 19.9 属性枚举表：`pim_attribute_enum`

```sql
CREATE TABLE pim_attribute_enum (
    enum_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '枚举ID',
    attr_id BIGINT UNSIGNED NOT NULL COMMENT '属性ID',
    enum_code VARCHAR(64) NOT NULL COMMENT '枚举编码',
    enum_name_cn VARCHAR(128) NOT NULL COMMENT '中文枚举名',
    enum_name_en VARCHAR(128) DEFAULT NULL COMMENT '英文枚举名',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '排序',
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE、DISABLED',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (enum_id),
    UNIQUE KEY uk_attr_enum (attr_id, enum_code),
    KEY idx_attr_status_sort (attr_id, status, sort_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品属性枚举值表';
```

### 19.10 类目属性模板表：`pim_category_attribute`

```sql
CREATE TABLE pim_category_attribute (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    category_id BIGINT UNSIGNED NOT NULL COMMENT '全球类目ID',
    market_code VARCHAR(32) DEFAULT NULL COMMENT '市场编码，NULL表示全球通用',
    attr_id BIGINT UNSIGNED NOT NULL COMMENT '属性ID',
    requirement_level VARCHAR(32) NOT NULL DEFAULT 'OPTIONAL' COMMENT '要求级别：REQUIRED、RECOMMENDED、OPTIONAL',
    validation_rule JSON COMMENT '校验规则，如长度、范围、正则、单位',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '排序',
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE、DISABLED',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (id),
    UNIQUE KEY uk_category_market_attr (category_id, market_code, attr_id),
    KEY idx_attr_id (attr_id),
    KEY idx_market_requirement (market_code, requirement_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='类目属性模板表';
```

### 19.11 全球 SPU 表：`pim_global_spu`

```sql
CREATE TABLE pim_global_spu (
    global_spu_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '全球SPU ID',
    spu_code VARCHAR(64) NOT NULL COMMENT '全球SPU编码',
    brand_id BIGINT UNSIGNED DEFAULT NULL COMMENT '品牌ID',
    global_category_id BIGINT UNSIGNED NOT NULL COMMENT '全球类目ID',
    product_type VARCHAR(64) NOT NULL COMMENT '商品类型，用于合规分类',
    product_name_cn VARCHAR(256) NOT NULL COMMENT '中文商品名',
    product_name_en VARCHAR(256) DEFAULT NULL COMMENT '英文基础商品名',
    manufacturer_name VARCHAR(256) DEFAULT NULL COMMENT '制造商名称',
    supplier_code VARCHAR(64) DEFAULT NULL COMMENT '供应商编码',
    country_of_origin CHAR(2) DEFAULT NULL COMMENT '原产国',
    material_summary VARCHAR(512) DEFAULT NULL COMMENT '材质摘要',
    intended_use VARCHAR(512) DEFAULT NULL COMMENT '用途说明',
    target_user_group VARCHAR(64) DEFAULT NULL COMMENT '目标用户：ADULT、CHILD、BABY、PET等',
    lifecycle_status VARCHAR(32) NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT、ACTIVE、SUSPENDED、DISCONTINUED',
    owner_user_id VARCHAR(64) DEFAULT NULL COMMENT '商品负责人',
    version_no INT NOT NULL DEFAULT 1 COMMENT '版本号',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    created_by VARCHAR(64) DEFAULT NULL,
    updated_by VARCHAR(64) DEFAULT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (global_spu_id),
    UNIQUE KEY uk_spu_code (spu_code),
    KEY idx_brand_id (brand_id),
    KEY idx_category_status (global_category_id, lifecycle_status),
    KEY idx_product_type (product_type),
    KEY idx_supplier_code (supplier_code),
    KEY idx_updated_at (updated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='全球SPU主数据表';
```

### 19.12 全球 SKU 表：`pim_global_sku`

```sql
CREATE TABLE pim_global_sku (
    global_sku_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '全球SKU ID',
    global_spu_id BIGINT UNSIGNED NOT NULL COMMENT '全球SPU ID',
    sku_code VARCHAR(64) NOT NULL COMMENT '全球SKU编码',
    gtin VARCHAR(64) DEFAULT NULL COMMENT 'GTIN/EAN/UPC',
    barcode VARCHAR(64) DEFAULT NULL COMMENT '内部或供应商条码',
    model_number VARCHAR(128) DEFAULT NULL COMMENT '型号',
    color_code VARCHAR(64) DEFAULT NULL COMMENT '颜色编码',
    size_code VARCHAR(64) DEFAULT NULL COMMENT '尺码编码',
    material VARCHAR(512) DEFAULT NULL COMMENT 'SKU级材质',
    net_weight_g DECIMAL(12,3) DEFAULT NULL COMMENT '净重，克',
    gross_weight_g DECIMAL(12,3) DEFAULT NULL COMMENT '毛重，克',
    package_length_mm DECIMAL(12,3) DEFAULT NULL COMMENT '包装长，毫米',
    package_width_mm DECIMAL(12,3) DEFAULT NULL COMMENT '包装宽，毫米',
    package_height_mm DECIMAL(12,3) DEFAULT NULL COMMENT '包装高，毫米',
    battery_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否含电池',
    battery_type VARCHAR(64) DEFAULT NULL COMMENT '电池类型',
    battery_capacity_wh DECIMAL(12,3) DEFAULT NULL COMMENT '电池容量Wh',
    liquid_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否液体',
    powder_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否粉末',
    magnetic_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否磁性',
    hazardous_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否危险品',
    fragile_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否易碎',
    temperature_control_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否温控',
    lifecycle_status VARCHAR(32) NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT、ACTIVE、SUSPENDED、DISCONTINUED',
    version_no INT NOT NULL DEFAULT 1 COMMENT '版本号',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    created_by VARCHAR(64) DEFAULT NULL,
    updated_by VARCHAR(64) DEFAULT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (global_sku_id),
    UNIQUE KEY uk_sku_code (sku_code),
    KEY idx_spu_status (global_spu_id, lifecycle_status),
    KEY idx_gtin (gtin),
    KEY idx_barcode (barcode),
    KEY idx_flags (battery_flag, liquid_flag, hazardous_flag),
    KEY idx_updated_at (updated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='全球SKU主数据表';
```

### 19.13 SPU 属性值表：`pim_spu_attribute_value`

```sql
CREATE TABLE pim_spu_attribute_value (
    value_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '属性值ID',
    global_spu_id BIGINT UNSIGNED NOT NULL COMMENT '全球SPU ID',
    attr_id BIGINT UNSIGNED NOT NULL COMMENT '属性ID',
    market_code VARCHAR(32) DEFAULT NULL COMMENT '市场编码，NULL表示全球通用',
    locale VARCHAR(16) DEFAULT NULL COMMENT '语言编码，NULL表示非语言属性',
    value_text TEXT COMMENT '文本值',
    value_number DECIMAL(18,6) DEFAULT NULL COMMENT '数值',
    value_bool TINYINT(1) DEFAULT NULL COMMENT '布尔值',
    value_json JSON COMMENT '复杂值',
    unit_code VARCHAR(32) DEFAULT NULL COMMENT '单位',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (value_id),
    UNIQUE KEY uk_spu_attr_market_locale (global_spu_id, attr_id, market_code, locale),
    KEY idx_attr_id (attr_id),
    KEY idx_market_locale (market_code, locale),
    KEY idx_value_number (attr_id, value_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='SPU动态属性值表';
```

### 19.14 SKU 属性值表：`pim_sku_attribute_value`

```sql
CREATE TABLE pim_sku_attribute_value (
    value_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '属性值ID',
    global_sku_id BIGINT UNSIGNED NOT NULL COMMENT '全球SKU ID',
    attr_id BIGINT UNSIGNED NOT NULL COMMENT '属性ID',
    market_code VARCHAR(32) DEFAULT NULL COMMENT '市场编码，NULL表示全球通用',
    locale VARCHAR(16) DEFAULT NULL COMMENT '语言编码，NULL表示非语言属性',
    value_text TEXT COMMENT '文本值',
    value_number DECIMAL(18,6) DEFAULT NULL COMMENT '数值',
    value_bool TINYINT(1) DEFAULT NULL COMMENT '布尔值',
    value_json JSON COMMENT '复杂值',
    unit_code VARCHAR(32) DEFAULT NULL COMMENT '单位',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (value_id),
    UNIQUE KEY uk_sku_attr_market_locale (global_sku_id, attr_id, market_code, locale),
    KEY idx_attr_id (attr_id),
    KEY idx_market_locale (market_code, locale),
    KEY idx_value_number (attr_id, value_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='SKU动态属性值表';
```

### 19.15 市场 Listing 表：`pim_market_listing`

```sql
CREATE TABLE pim_market_listing (
    listing_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Listing ID',
    listing_code VARCHAR(64) NOT NULL COMMENT 'Listing编码',
    global_spu_id BIGINT UNSIGNED NOT NULL COMMENT '全球SPU ID',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    locale VARCHAR(16) NOT NULL COMMENT '语言编码',
    market_category_id BIGINT UNSIGNED DEFAULT NULL COMMENT '市场类目ID',
    brand_display_name VARCHAR(128) DEFAULT NULL COMMENT '本地品牌展示名',
    title VARCHAR(512) NOT NULL COMMENT '本地标题',
    subtitle VARCHAR(512) DEFAULT NULL COMMENT '本地副标题',
    bullet_points JSON COMMENT '卖点数组',
    short_description TEXT COMMENT '短描述',
    long_description MEDIUMTEXT COMMENT '长描述',
    specifications JSON COMMENT '规格参数',
    package_includes TEXT COMMENT '包装清单',
    usage_instruction MEDIUMTEXT COMMENT '使用说明',
    care_instruction MEDIUMTEXT COMMENT '保养说明',
    safety_warning MEDIUMTEXT COMMENT '安全警示',
    faq JSON COMMENT 'FAQ',
    seo_title VARCHAR(512) DEFAULT NULL COMMENT 'SEO标题',
    seo_description VARCHAR(1024) DEFAULT NULL COMMENT 'SEO描述',
    seo_keywords JSON COMMENT 'SEO关键词',
    instruction_language VARCHAR(16) DEFAULT NULL COMMENT '说明书语言',
    content_version INT NOT NULL DEFAULT 1 COMMENT '内容版本',
    online_status VARCHAR(32) NOT NULL DEFAULT 'OFFLINE' COMMENT '上架状态：OFFLINE、ONLINE、SCHEDULED、DELISTED',
    approval_status VARCHAR(32) NOT NULL DEFAULT 'DRAFT' COMMENT '审核状态：DRAFT、PENDING、APPROVED、REJECTED',
    compliance_status VARCHAR(32) NOT NULL DEFAULT 'NOT_CHECKED' COMMENT '合规状态：NOT_CHECKED、APPROVED、BLOCKED、EXPIRED',
    published_at DATETIME(3) DEFAULT NULL COMMENT '发布时间',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    created_by VARCHAR(64) DEFAULT NULL,
    updated_by VARCHAR(64) DEFAULT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (listing_id),
    UNIQUE KEY uk_listing_code (listing_code),
    UNIQUE KEY uk_spu_market_locale (global_spu_id, market_code, locale),
    KEY idx_market_status (market_code, online_status, approval_status),
    KEY idx_category (market_category_id),
    KEY idx_updated_at (updated_at),
    FULLTEXT KEY ft_title_desc (title, short_description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='市场Listing本地化内容表';
```

### 19.16 市场 SKU 表：`pim_market_sku`

```sql
CREATE TABLE pim_market_sku (
    market_sku_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '市场SKU ID',
    listing_id BIGINT UNSIGNED NOT NULL COMMENT 'Listing ID',
    global_sku_id BIGINT UNSIGNED NOT NULL COMMENT '全球SKU ID',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    local_sku_code VARCHAR(64) NOT NULL COMMENT '市场本地SKU编码',
    local_barcode VARCHAR(64) DEFAULT NULL COMMENT '市场本地条码',
    color_display VARCHAR(128) DEFAULT NULL COMMENT '本地颜色展示',
    size_display VARCHAR(128) DEFAULT NULL COMMENT '本地尺码展示',
    unit_display VARCHAR(64) DEFAULT NULL COMMENT '本地单位展示',
    sellable_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否可售',
    restricted_reason_code VARCHAR(64) DEFAULT NULL COMMENT '不可售原因编码',
    launch_date DATE DEFAULT NULL COMMENT '计划上市日期',
    delist_date DATE DEFAULT NULL COMMENT '计划下架日期',
    lifecycle_status VARCHAR(32) NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT、ACTIVE、SUSPENDED、DELISTED',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    created_by VARCHAR(64) DEFAULT NULL,
    updated_by VARCHAR(64) DEFAULT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (market_sku_id),
    UNIQUE KEY uk_market_local_sku (market_code, local_sku_code),
    UNIQUE KEY uk_global_sku_market (global_sku_id, market_code),
    KEY idx_listing_id (listing_id),
    KEY idx_market_sellable (market_code, sellable_flag),
    KEY idx_lifecycle_status (lifecycle_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='市场SKU销售资料表';
```

### 19.17 媒体资产表：`pim_media_asset`

```sql
CREATE TABLE pim_media_asset (
    asset_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '媒体资产ID',
    global_spu_id BIGINT UNSIGNED NOT NULL COMMENT '全球SPU ID',
    global_sku_id BIGINT UNSIGNED DEFAULT NULL COMMENT '全球SKU ID，NULL表示SPU级素材',
    market_code VARCHAR(32) DEFAULT NULL COMMENT '市场编码，NULL表示全球通用',
    locale VARCHAR(16) DEFAULT NULL COMMENT '语言编码',
    asset_type VARCHAR(32) NOT NULL COMMENT '类型：MAIN_IMAGE、GALLERY_IMAGE、VIDEO、SIZE_CHART、PACKAGING、LABEL、MANUAL',
    usage_scene VARCHAR(64) DEFAULT NULL COMMENT '使用场景：PDP、SEARCH、AD、CHANNEL、PACKAGE',
    file_url VARCHAR(1024) NOT NULL COMMENT '文件地址',
    file_hash VARCHAR(128) DEFAULT NULL COMMENT '文件哈希',
    mime_type VARCHAR(128) DEFAULT NULL COMMENT 'MIME类型',
    width INT DEFAULT NULL COMMENT '图片宽度',
    height INT DEFAULT NULL COMMENT '图片高度',
    duration_ms INT DEFAULT NULL COMMENT '视频时长毫秒',
    text_embedded_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否含嵌入文字',
    copyright_owner VARCHAR(256) DEFAULT NULL COMMENT '版权方',
    approval_status VARCHAR(32) NOT NULL DEFAULT 'DRAFT' COMMENT '审核状态：DRAFT、PENDING、APPROVED、REJECTED',
    version_no INT NOT NULL DEFAULT 1 COMMENT '版本号',
    sort_no INT NOT NULL DEFAULT 0 COMMENT '排序',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
    created_by VARCHAR(64) DEFAULT NULL,
    updated_by VARCHAR(64) DEFAULT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (asset_id),
    KEY idx_spu_type (global_spu_id, asset_type, sort_no),
    KEY idx_sku_type (global_sku_id, asset_type),
    KEY idx_market_locale (market_code, locale),
    KEY idx_approval_status (approval_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品媒体资产表';
```

### 19.18 包装标签表：`pim_packaging_label`

```sql
CREATE TABLE pim_packaging_label (
    label_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '包装标签ID',
    global_sku_id BIGINT UNSIGNED NOT NULL COMMENT '全球SKU ID',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    locale VARCHAR(16) NOT NULL COMMENT '语言编码',
    label_type VARCHAR(32) NOT NULL COMMENT '标签类型：PRODUCT_LABEL、WARNING_LABEL、ENERGY_LABEL、CARE_LABEL、PACKAGE_ARTWORK',
    product_name_label VARCHAR(256) DEFAULT NULL COMMENT '标签商品名',
    manufacturer_name VARCHAR(256) DEFAULT NULL COMMENT '制造商名称',
    importer_name VARCHAR(256) DEFAULT NULL COMMENT '进口商名称',
    responsible_person VARCHAR(256) DEFAULT NULL COMMENT '责任人或授权代表',
    country_of_origin CHAR(2) DEFAULT NULL COMMENT '原产国',
    warning_text MEDIUMTEXT COMMENT '警示语',
    instruction_language VARCHAR(16) DEFAULT NULL COMMENT '说明语言',
    file_url VARCHAR(1024) DEFAULT NULL COMMENT '标签或包装文件地址',
    version_no INT NOT NULL DEFAULT 1 COMMENT '版本号',
    approval_status VARCHAR(32) NOT NULL DEFAULT 'DRAFT' COMMENT '审核状态：DRAFT、PENDING、APPROVED、REJECTED',
    approved_by VARCHAR(64) DEFAULT NULL COMMENT '审核人',
    approved_at DATETIME(3) DEFAULT NULL COMMENT '审核时间',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (label_id),
    UNIQUE KEY uk_sku_market_locale_type_version (global_sku_id, market_code, locale, label_type, version_no),
    KEY idx_market_status (market_code, approval_status),
    KEY idx_approved_at (approved_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品包装标签本地化表';
```

### 19.19 合规档案表：`pim_compliance_profile`

```sql
CREATE TABLE pim_compliance_profile (
    profile_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '合规档案ID',
    global_sku_id BIGINT UNSIGNED NOT NULL COMMENT '全球SKU ID',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    product_regulation_category VARCHAR(128) NOT NULL COMMENT '法规分类，如 ELECTRONICS、CHILDREN_PRODUCT、TEXTILE',
    required_certificates JSON COMMENT '所需证书清单',
    required_labels JSON COMMENT '所需标签清单',
    required_documents JSON COMMENT '所需文件清单',
    responsible_person_required TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否需要责任人或授权代表',
    importer_required TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否需要进口商信息',
    compliance_status VARCHAR(32) NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT、INFO_REQUIRED、UNDER_REVIEW、APPROVED、CONDITION_APPROVED、REJECTED、EXPIRED、SUSPENDED',
    effective_from DATE DEFAULT NULL COMMENT '生效日期',
    expires_at DATE DEFAULT NULL COMMENT '过期日期',
    reviewed_by VARCHAR(64) DEFAULT NULL COMMENT '审核人',
    reviewed_at DATETIME(3) DEFAULT NULL COMMENT '审核时间',
    review_comment VARCHAR(1024) DEFAULT NULL COMMENT '审核意见',
    version_no INT NOT NULL DEFAULT 1 COMMENT '版本号',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (profile_id),
    UNIQUE KEY uk_sku_market (global_sku_id, market_code),
    KEY idx_market_status (market_code, compliance_status),
    KEY idx_regulation_category (product_regulation_category),
    KEY idx_expires_at (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品市场合规档案表';
```

### 19.20 合规文件表：`pim_compliance_document`

```sql
CREATE TABLE pim_compliance_document (
    document_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '合规文件ID',
    profile_id BIGINT UNSIGNED NOT NULL COMMENT '合规档案ID',
    global_sku_id BIGINT UNSIGNED NOT NULL COMMENT '全球SKU ID',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    document_type VARCHAR(64) NOT NULL COMMENT '文件类型：TEST_REPORT、DOC、CPC、SDS、MANUAL、CERTIFICATE、AUTHORIZATION',
    document_no VARCHAR(128) DEFAULT NULL COMMENT '文件编号',
    document_name VARCHAR(256) NOT NULL COMMENT '文件名称',
    file_url VARCHAR(1024) NOT NULL COMMENT '文件地址',
    issuer VARCHAR(256) DEFAULT NULL COMMENT '签发机构',
    issue_date DATE DEFAULT NULL COMMENT '签发日期',
    expiry_date DATE DEFAULT NULL COMMENT '过期日期',
    version_no INT NOT NULL DEFAULT 1 COMMENT '版本号',
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING、VALID、REJECTED、EXPIRED、REVOKED',
    verified_by VARCHAR(64) DEFAULT NULL COMMENT '验证人',
    verified_at DATETIME(3) DEFAULT NULL COMMENT '验证时间',
    created_by VARCHAR(64) DEFAULT NULL,
    updated_by VARCHAR(64) DEFAULT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (document_id),
    KEY idx_profile_type (profile_id, document_type),
    KEY idx_sku_market (global_sku_id, market_code),
    KEY idx_expiry_date (expiry_date),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品合规文件表';
```

### 19.21 可售性规则表：`pim_sellability_rule`

```sql
CREATE TABLE pim_sellability_rule (
    rule_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '规则ID',
    rule_code VARCHAR(128) NOT NULL COMMENT '规则编码',
    rule_name VARCHAR(256) NOT NULL COMMENT '规则名称',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    channel_code VARCHAR(64) DEFAULT NULL COMMENT '渠道编码，NULL表示市场通用',
    global_category_id BIGINT UNSIGNED DEFAULT NULL COMMENT '适用全球类目，NULL表示不限',
    product_type VARCHAR(64) DEFAULT NULL COMMENT '适用商品类型，NULL表示不限',
    condition_json JSON NOT NULL COMMENT '触发条件',
    action_json JSON NOT NULL COMMENT '处理动作，如阻断、警告、要求文件',
    block_level VARCHAR(32) NOT NULL DEFAULT 'BLOCK' COMMENT '等级：BLOCK、WARN、INFO',
    effective_from DATETIME(3) NOT NULL COMMENT '生效时间',
    effective_to DATETIME(3) DEFAULT NULL COMMENT '失效时间',
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE、DISABLED',
    version_no INT NOT NULL DEFAULT 1 COMMENT '版本号',
    created_by VARCHAR(64) DEFAULT NULL,
    updated_by VARCHAR(64) DEFAULT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (rule_id),
    UNIQUE KEY uk_rule_code_version (rule_code, version_no),
    KEY idx_market_channel_status (market_code, channel_code, status),
    KEY idx_category_type (global_category_id, product_type),
    KEY idx_effective_time (effective_from, effective_to)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品市场可售性规则表';
```

### 19.22 可售性结果表：`pim_sellability_result`

```sql
CREATE TABLE pim_sellability_result (
    result_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '可售性结果ID',
    global_sku_id BIGINT UNSIGNED NOT NULL COMMENT '全球SKU ID',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    channel_code VARCHAR(64) NOT NULL DEFAULT 'SELF_SITE' COMMENT '渠道编码',
    sellable_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否可售',
    blockers JSON COMMENT '阻断原因列表',
    warnings JSON COMMENT '警告列表',
    required_actions JSON COMMENT '待完成动作',
    rule_version VARCHAR(128) DEFAULT NULL COMMENT '规则版本摘要',
    checked_at DATETIME(3) NOT NULL COMMENT '检查时间',
    expire_at DATETIME(3) DEFAULT NULL COMMENT '结果过期时间',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (result_id),
    UNIQUE KEY uk_sku_market_channel (global_sku_id, market_code, channel_code),
    KEY idx_market_sellable (market_code, sellable_flag),
    KEY idx_checked_at (checked_at),
    KEY idx_expire_at (expire_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品市场可售性检查结果表';
```

### 19.23 本地化任务表：`pim_localization_task`

```sql
CREATE TABLE pim_localization_task (
    task_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '本地化任务ID',
    task_code VARCHAR(64) NOT NULL COMMENT '任务编码',
    source_type VARCHAR(32) NOT NULL COMMENT '来源类型：SPU、SKU、LISTING、MEDIA、LABEL、MANUAL',
    source_id BIGINT UNSIGNED NOT NULL COMMENT '来源ID',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    source_locale VARCHAR(16) NOT NULL COMMENT '源语言',
    target_locale VARCHAR(16) NOT NULL COMMENT '目标语言',
    fields_json JSON NOT NULL COMMENT '需要本地化的字段列表',
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING、TRANSLATING、REVIEWING、APPROVED、REJECTED、CANCELLED',
    translator VARCHAR(64) DEFAULT NULL COMMENT '翻译人',
    reviewer VARCHAR(64) DEFAULT NULL COMMENT '审核人',
    due_at DATETIME(3) DEFAULT NULL COMMENT '截止时间',
    completed_at DATETIME(3) DEFAULT NULL COMMENT '完成时间',
    created_by VARCHAR(64) DEFAULT NULL,
    updated_by VARCHAR(64) DEFAULT NULL,
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (task_id),
    UNIQUE KEY uk_task_code (task_code),
    KEY idx_source (source_type, source_id),
    KEY idx_market_locale_status (market_code, target_locale, status),
    KEY idx_due_at (due_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品内容本地化任务表';
```

### 19.24 渠道表：`pim_channel`

```sql
CREATE TABLE pim_channel (
    channel_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '渠道ID',
    channel_code VARCHAR(64) NOT NULL COMMENT '渠道编码，如 SELF_SITE、AMAZON_US、TIKTOK_SHOP_US',
    channel_name VARCHAR(128) NOT NULL COMMENT '渠道名称',
    market_code VARCHAR(32) NOT NULL COMMENT '所属市场',
    channel_type VARCHAR(32) NOT NULL COMMENT '类型：SELF_SITE、MARKETPLACE、SOCIAL_COMMERCE、AD_PLATFORM',
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE、DISABLED',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (channel_id),
    UNIQUE KEY uk_channel_code (channel_code),
    KEY idx_market_type (market_code, channel_type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品发布渠道表';
```

### 19.25 渠道类目映射表：`pim_channel_category_mapping`

```sql
CREATE TABLE pim_channel_category_mapping (
    mapping_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '映射ID',
    channel_code VARCHAR(64) NOT NULL COMMENT '渠道编码',
    market_code VARCHAR(32) NOT NULL COMMENT '市场编码',
    global_category_id BIGINT UNSIGNED NOT NULL COMMENT '全球类目ID',
    channel_category_id VARCHAR(128) NOT NULL COMMENT '渠道类目ID',
    channel_category_path VARCHAR(1024) DEFAULT NULL COMMENT '渠道类目路径',
    required_attributes JSON COMMENT '渠道必填属性',
    mapping_status VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING、APPROVED、REJECTED',
    approved_by VARCHAR(64) DEFAULT NULL COMMENT '审核人',
    approved_at DATETIME(3) DEFAULT NULL COMMENT '审核时间',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (mapping_id),
    UNIQUE KEY uk_channel_global_category (channel_code, global_category_id),
    KEY idx_market_channel (market_code, channel_code),
    KEY idx_mapping_status (mapping_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='渠道类目映射表';
```

### 19.26 渠道 Listing 发布表：`pim_channel_listing`

```sql
CREATE TABLE pim_channel_listing (
    channel_listing_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '渠道Listing ID',
    listing_id BIGINT UNSIGNED NOT NULL COMMENT '内部Listing ID',
    market_sku_id BIGINT UNSIGNED DEFAULT NULL COMMENT '内部市场SKU ID，NULL表示SPU级发布',
    channel_code VARCHAR(64) NOT NULL COMMENT '渠道编码',
    external_product_id VARCHAR(128) DEFAULT NULL COMMENT '渠道商品ID',
    external_sku_id VARCHAR(128) DEFAULT NULL COMMENT '渠道SKU ID',
    publish_status VARCHAR(32) NOT NULL DEFAULT 'NOT_PUBLISHED' COMMENT '发布状态：NOT_PUBLISHED、PENDING、PUBLISHED、FAILED、DELISTED',
    publish_error_code VARCHAR(128) DEFAULT NULL COMMENT '发布错误码',
    publish_error_message VARCHAR(2048) DEFAULT NULL COMMENT '发布错误信息',
    last_publish_payload JSON COMMENT '最近一次发布报文摘要',
    last_published_at DATETIME(3) DEFAULT NULL COMMENT '最近发布时间',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    PRIMARY KEY (channel_listing_id),
    UNIQUE KEY uk_listing_sku_channel (listing_id, market_sku_id, channel_code),
    KEY idx_channel_external_product (channel_code, external_product_id),
    KEY idx_publish_status (channel_code, publish_status),
    KEY idx_last_published_at (last_published_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='渠道Listing发布状态表';
```

### 19.27 商品变更日志表：`pim_product_change_log`

```sql
CREATE TABLE pim_product_change_log (
    log_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    object_type VARCHAR(32) NOT NULL COMMENT '对象类型：SPU、SKU、LISTING、COMPLIANCE、DOCUMENT、MEDIA、LABEL',
    object_id BIGINT UNSIGNED NOT NULL COMMENT '对象ID',
    market_code VARCHAR(32) DEFAULT NULL COMMENT '市场编码',
    change_type VARCHAR(32) NOT NULL COMMENT '变更类型：CREATE、UPDATE、DELETE、APPROVE、REJECT、PUBLISH、DELIST',
    before_json JSON COMMENT '变更前内容',
    after_json JSON COMMENT '变更后内容',
    operator VARCHAR(64) DEFAULT NULL COMMENT '操作人',
    trace_id VARCHAR(128) DEFAULT NULL COMMENT '链路ID',
    created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    PRIMARY KEY (log_id),
    KEY idx_object (object_type, object_id),
    KEY idx_market_type (market_code, change_type),
    KEY idx_created_at (created_at),
    KEY idx_trace_id (trace_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品变更审计日志表';
```

### 19.28 最小落地表清单

如果第一期只做一个国家和一个品类，建议至少落以下 12 张表：

1. `intl_market`
2. `intl_market_locale`
3. `pim_brand`
4. `pim_global_category`
5. `pim_market_category`
6. `pim_category_mapping`
7. `pim_global_spu`
8. `pim_global_sku`
9. `pim_market_listing`
10. `pim_market_sku`
11. `pim_compliance_profile`
12. `pim_compliance_document`

第二期再补属性体系、媒体、标签、可售性规则、本地化任务和渠道发布表。

### 19.29 核心查询建议

商品详情页查询：

```sql
SELECT
    l.listing_id,
    l.title,
    l.bullet_points,
    l.long_description,
    l.specifications,
    s.global_sku_id,
    ms.market_sku_id,
    ms.local_sku_code,
    ms.color_display,
    ms.size_display,
    ms.sellable_flag
FROM pim_market_listing l
JOIN pim_market_sku ms ON ms.listing_id = l.listing_id
JOIN pim_global_sku s ON s.global_sku_id = ms.global_sku_id
WHERE l.global_spu_id = ?
  AND l.market_code = ?
  AND l.locale = ?
  AND l.online_status = 'ONLINE'
  AND l.approval_status = 'APPROVED'
  AND l.compliance_status = 'APPROVED'
  AND ms.lifecycle_status = 'ACTIVE'
  AND ms.sellable_flag = 1;
```

市场可售 SKU 查询：

```sql
SELECT
    ms.market_sku_id,
    ms.local_sku_code,
    r.sellable_flag,
    r.blockers,
    r.warnings
FROM pim_market_sku ms
LEFT JOIN pim_sellability_result r
       ON r.global_sku_id = ms.global_sku_id
      AND r.market_code = ms.market_code
      AND r.channel_code = ?
WHERE ms.market_code = ?
  AND ms.global_sku_id = ?
  AND ms.lifecycle_status = 'ACTIVE';
```

合规文件过期扫描：

```sql
SELECT
    document_id,
    global_sku_id,
    market_code,
    document_type,
    expiry_date
FROM pim_compliance_document
WHERE status = 'VALID'
  AND expiry_date IS NOT NULL
  AND expiry_date <= DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY);
```

## 20. 结论

商品国际化是电商国际化的前置工程。只有把商品主数据、市场 Listing、本地化内容、合规资料、包装标签、清关资料和渠道规则系统化，后续价格、订单、支付、物流和售后才能稳定运行。

建议从 PIM 改造入手，先建立 Global SPU/SKU、Market Listing、合规档案、商品文件中心和市场可售性服务，再逐步扩展到翻译工作流、媒体资产管理、渠道发布中心和自动化合规规则。
