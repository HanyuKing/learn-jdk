# 商品国际化 SQL 翻译执行记录

## Context

国际化电商业务需要把国内中文命名的商品属性、属性选项、类目属性数据翻译成专业英文。`mm/` 目录下原有 3 份 INSERT SQL 文件（共 444 条 INSERT），列含中文值（`code`/`name`/`value`/`attribute_code`/`attribute_name`/`allowed_attribute_value`）。同目录已存在 `chinese_english_mapping.json`，提供 37 个属性名 + 147+ 个选项值的人工校对翻译，可直接复用。

**目标**：基于现有翻译映射，并列生成 3 个英文版 SQL 文件 + 1 份按表分节的中英对照速查表，原文件不动，新文件 id 与原文件 1:1 对应，新环境部署即可使用英文数据。

## 用户决策（已确认）

1. `code` 与 `name` 两列均使用映射中的 `en_display`（如 `'Appearance Color'`），保持原文件 code = name 一致的特点。
2. 输出 3 个新的 INSERT SQL 文件（不生成 UPDATE）。
3. 纯英文/纯数字值（如 `test`、`15`、`4cm`）原样保留；大小写不一致的中文混写（`UV打印`/`uv打印`/`UV 打印`）统一翻译为同一英文（`UV Printing`），但**保留所有原始 INSERT 行不去重**，仅做值翻译。
4. 额外产出一份按表分节的中英对照 markdown，仅列出 3 份 SQL 实际使用到的中文值。

## 关键文件

### 输入（只读）
- `src/main/java/work/mm/trade_pd_attribute_添加数据.sql` — 103 条，列 `code`/`name`
- `src/main/java/work/mm/trade_pd_attribute_option_添加数据.sql` — 142 条，列 `value`
- `src/main/java/work/mm/trade_pd_category_attribute_添加数据.sql` — 199 条，列 `attribute_code`/`attribute_name`/`allowed_attribute_value`(JSON 数组)
- `src/main/java/work/mm/chinese_english_mapping.json` — 翻译映射源（`attribute_name_mapping` + `option_value_mapping`）

### 输出（已生成）
- `src/main/java/work/mm/trade_pd_attribute_en.sql`
- `src/main/java/work/mm/trade_pd_attribute_option_en.sql`
- `src/main/java/work/mm/trade_pd_category_attribute_en.sql`
- `src/main/java/work/mm/中英文对照表.md`

### 辅助脚本（不入仓，临时文件）
- `/tmp/translate_sql.py` — 翻译脚本
- `/tmp/gen_translation_ref.py` — 中英对照表生成脚本

## 实施步骤（已执行）

### 1. 加载映射
读取 `chinese_english_mapping.json`，建立两张内存表：
- `ATTR_MAP[中文] -> en_display`（用于属性名）
- `OPT_MAP[中文] -> en_display`（用于选项值）

### 2. 通用 SQL 解析器
`split_values(body)` 把 `VALUES(...)` 内的元组拆分为字段列表，处理三种情况：
- 单引号字符串（多数值）
- 未加引号的 `NULL`（用 sentinel 占位）
- 字符串内允许出现 `,`（JSON 数组）

`join_values(vals)` 反向重组，保留 `'..'` 与 `NULL` 区别。`VRE = re.compile(r"VALUES\s*\((.*)\);\s*$", re.DOTALL)` 抓元组体。

### 3. 翻译 `trade_pd_attribute_添加数据.sql`
对每条 INSERT：把字段索引 1（`code`）与 2（`name`）查 `ATTR_MAP` 替换，其他列原样保留。

### 4. 翻译 `trade_pd_attribute_option_添加数据.sql`
对每条 INSERT：把字段索引 3（`value`）查 `OPT_MAP` 替换。
- 中文 → mapping
- 纯英文/数字 → 原样（如 `test`、`4cm`、`18`）
- 大小写变体 → 走 mapping 中各自登记的同一英文（如 `UV打印`/`uv打印`/`UV 打印` → `UV Printing`）

### 5. 翻译 `trade_pd_category_attribute_添加数据.sql`
对每条 INSERT：
- 字段索引 4 (`attribute_code`)、5 (`attribute_name`) → `ATTR_MAP`
- 字段索引 10 (`allowed_attribute_value`) 是 JSON 数组字符串：`json.loads` 解析 → 对每个中文元素查 `OPT_MAP`（兜底查 `ATTR_MAP`）→ `json.dumps(ensure_ascii=False)` 重序列化。纯数字/英文元素（如 `["1"]`）原样保留。

### 6. 文件头注释
每个新文件首行加 SQL 注释：`-- Generated 2026-06-17 from <源文件名> via chinese_english_mapping.json (en_display).`

### 7. 失败策略
任何 mapping 中找不到的中文值，脚本不写出文件、不编造译法，列出未命中行号上报。最终运行 `TOTAL MISSED: 0`。

### 8. 中英对照表生成
`/tmp/gen_translation_ref.py` 复用同一解析器：扫描 3 份原 SQL，提取实际出现的中文值并查 mapping，按表分节输出 markdown：
- §1 `trade_pd_attribute`：37 条
- §2 `trade_pd_attribute_option`：93 条
- §3.1 `trade_pd_category_attribute` (attribute_code/attribute_name)：16 条
- §3.2 `trade_pd_category_attribute` (allowed_attribute_value 元素)：129 条

## 验证（已通过）

| 项 | 结果 |
| --- | --- |
| 行数对齐 | 三组新文件 = 原文件行数 + 1 行注释头 |
| 数据行中文残留扫描 | 0（仅注释头含中文文件名引用） |
| `id` 一致性 | 103/142/199 条 INSERT 第一字段与原文件完全相同 |
| `allowed_attribute_value` JSON 合法性 | 200/200 全部 `json.loads` 通过 |
| mapping 覆盖率 | 未命中 token 数 = 0 |

## 复跑

```bash
python3 /tmp/translate_sql.py        # 重新生成 3 个 _en.sql
python3 /tmp/gen_translation_ref.py  # 重新生成 中英文对照表.md
```

如未来 mapping JSON 新增/修改条目，直接复跑即可，不会改写原 SQL。
