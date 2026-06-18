# mm 目录 SQL 文件中文列分析

分析范围：`src/main/java/work/mm/` 下 3 个 SQL 文件。

| 文件 | 表名 | 行数 |
|---|---|---|
| `trade_pd_attribute_添加数据.sql` | `trade_pd_attribute` | 103 |
| `trade_pd_attribute_option_添加数据.sql` | `trade_pd_attribute_option` | 143 |
| `trade_pd_category_attribute_添加数据.sql` | `trade_pd_category_attribute` | 200 |

> 机器可读映射文件：[`chinese_english_mapping.json`](./chinese_english_mapping.json)

---

## 总体结论

- **列名（字段名）全部是英文**，没有中文列名。
- 含中文的是**部分列的数据值**，集中在业务语义字段（属性名、选项值、类目属性配置）。

---

## 含中文值的列（汇总）

| 文件 | 含中文值的列 |
|---|---|
| `trade_pd_attribute_添加数据.sql` | `code`、`name` |
| `trade_pd_attribute_option_添加数据.sql` | `value` |
| `trade_pd_category_attribute_添加数据.sql` | `attribute_code`、`attribute_name`、`allowed_attribute_value` |

---

## 完整中文值清单

### 一、属性名（37 个）

来源：`trade_pd_attribute.code/name`、`trade_pd_category_attribute.attribute_code/attribute_name`

| 序号 | 中文 | 英文展示名 | 英文编码 (snake_case) |
| --- | --- | --- | --- |
| 1 | UV打印 | UV Printing | uv_printing |
| 2 | 主石形状 | Main Stone Shape | main_stone_shape |
| 3 | 主石材质 | Main Stone Material | main_stone_material |
| 4 | 二次工艺 | Secondary Process | secondary_process |
| 5 | 印刷部位 | Print Area | print_area |
| 6 | 厚度 | Thickness | thickness |
| 7 | 吊坠材质 | Pendant Material | pendant_material |
| 8 | 图案 | Pattern | pattern |
| 9 | 图案元素 | Pattern Element | pattern_element |
| 10 | 圆球水杯盖颜色 | Sphere Cup Lid Color | sphere_cup_lid_color |
| 11 | 填充 | Filling | filling |
| 12 | 外观规格 | Appearance Specification | appearance_specification |
| 13 | 外观颜色 | Appearance Color | appearance_color |
| 14 | 定制要求 | Customization Requirements | customization_requirements |
| 15 | 定制部位 | Customization Area | customization_area |
| 16 | 容量 | Capacity | capacity |
| 17 | 尺寸 | Size | size |
| 18 | 底部 | Bottom | bottom |
| 19 | 延长链 | Extension Chain | extension_chain |
| 20 | 形状 | Shape | shape |
| 21 | 总重量 | Total Weight | total_weight |
| 22 | 成型工艺 | Forming Process | forming_process |
| 23 | 手寸 | Ring Size | ring_size |
| 24 | 数量 | Quantity | quantity |
| 25 | 材质 | Material | material |
| 26 | 款式 | Style | style |
| 27 | 色板选项 | Color Swatch Option | color_swatch_option |
| 28 | 色纸包边 | Color Paper Edge | color_paper_edge |
| 29 | 表面处理 | Surface Treatment | surface_treatment |
| 30 | 表面工艺 | Surface Process | surface_process |
| 31 | 覆膜款式 | Lamination Style | lamination_style |
| 32 | 规格 | Specification | specification |
| 33 | 配件款式 | Accessory Style | accessory_style |
| 34 | 链条材质 | Chain Material | chain_material |
| 35 | 链长尺寸 | Chain Length | chain_length |
| 36 | 颜色 | Color | color |
| 37 | 颜色yxf-test | Color YXF Test | color_yxf_test |

### 二、属性选项值 — option 表（93 个）

来源：`trade_pd_attribute_option.value`

| 序号 | 中文 | 英文展示名 | 英文编码 (snake_case) |
| --- | --- | --- | --- |
| 1 | 10cm以上 | Above 10cm | above_10cm |
| 2 | 10cm以下 | Below 10cm | below_10cm |
| 3 | 18K金 | 18K Gold | 18k_gold |
| 4 | 20cm及以下 | 20cm and Below | 20cm_and_below |
| 5 | 21cm(含)-50cm(含) | 21cm-50cm (inclusive) | 21cm_50cm_inclusive |
| 6 | 30cm以上 | Above 30cm | above_30cm |
| 7 | 3D硬金 | 3D Hard Gold | 3d_hard_gold |
| 8 | 4cm以下 | Below 4cm | below_4cm |
| 9 | 51cm(含)-80cm(含) | 51cm-80cm (inclusive) | 51cm_80cm_inclusive |
| 10 | 6cm以下 | Below 6cm | below_6cm |
| 11 | 81cm(含)-50cm(含) | 81cm-50cm (inclusive) | 81cm_50cm_inclusive |
| 12 | 81cm及以上 | 81cm and Above | 81cm_and_above |
| 13 | 925银 | 925 Silver | 925_silver |
| 14 | CNC雕刻 | CNC Engraving | cnc_engraving |
| 15 | UV打印 | UV Printing | uv_printing |
| 16 | 不锈钢 | Stainless Steel | stainless_steel |
| 17 | 亚克力 | Acrylic | acrylic |
| 18 | 仿珍珠 | Imitation Pearl | imitation_pearl |
| 19 | 做旧 | Antique Finish | antique_finish |
| 20 | 其他 | Other | other |
| 21 | 其他人造宝石 | Other Synthetic Gemstone | other_synthetic_gemstone |
| 22 | 其他天然宝石 | Other Natural Gemstone | other_natural_gemstone |
| 23 | 几何形状 | Geometric Shape | geometric_shape |
| 24 | 切半面 | Half-Cut Face | half_cut_face |
| 25 | 动物昆虫 | Animals & Insects | animals_insects |
| 26 | 动画动漫 | Animation & Anime | animation_anime |
| 27 | 单色3D打印 | Single-Color 3D Printing | single_color_3d_printing |
| 28 | 印花 | Print / Pattern Print | print_pattern |
| 29 | 圆形 | Round | round |
| 30 | 基本处理 | Basic Treatment | basic_treatment |
| 31 | 完整（不切面） | Complete (Uncut) | complete_uncut |
| 32 | 彩色3D打印 | Color 3D Printing | color_3d_printing |
| 33 | 心形 | Heart Shape | heart_shape |
| 34 | 手工涂装 | Hand Painting | hand_painting |
| 35 | 拉砂 | Brushed Finish | brushed_finish |
| 36 | 方形 | Square | square |
| 37 | 无 | None | none |
| 38 | 日月星辰 | Sun, Moon & Stars | sun_moon_stars |
| 39 | 木质 | Wood | wood |
| 40 | 树脂 | Resin | resin |
| 41 | 棉花 | Cotton Filling | cotton_filling |
| 42 | 植物花卉 | Plants & Flowers | plants_flowers |
| 43 | 橡胶 | Rubber | rubber |
| 44 | 毛绒 | Plush | plush |
| 45 | 水果蔬菜 | Fruits & Vegetables | fruits_vegetables |
| 46 | 水滴 | Teardrop | teardrop |
| 47 | 活口可调节 | Adjustable Open Ring | adjustable_open_ring |
| 48 | 海洋生物 | Marine Life | marine_life |
| 49 | 海绵 | Sponge | sponge |
| 50 | 涂装 | Coating / Painting | coating_painting |
| 51 | 玉石 | Jade | jade |
| 52 | 玫瑰金 | Rose Gold | rose_gold |
| 53 | 玻璃 | Glass | glass |
| 54 | 珍珠 | Pearl | pearl |
| 55 | 珐琅 | Enamel | enamel |
| 56 | 生肖星座 | Zodiac & Constellation | zodiac_constellation |
| 57 | 电绣 | Machine Embroidery | machine_embroidery |
| 58 | 电镀 | Electroplating | electroplating |
| 59 | 矩形 | Rectangle | rectangle |
| 60 | 红色 | Red | red |
| 61 | 纯金 | Pure Gold | pure_gold |
| 62 | 纯银 | Pure Silver | pure_silver |
| 63 | 美度10号 | US Size 10 | us_ring_size_10 |
| 64 | 美度11号 | US Size 11 | us_ring_size_11 |
| 65 | 美度12号 | US Size 12 | us_ring_size_12 |
| 66 | 美度13号 | US Size 13 | us_ring_size_13 |
| 67 | 美度14号 | US Size 14 | us_ring_size_14 |
| 68 | 美度15号 | US Size 15 | us_ring_size_15 |
| 69 | 美度16号 | US Size 16 | us_ring_size_16 |
| 70 | 美度17号 | US Size 17 | us_ring_size_17 |
| 71 | 美度5号 | US Size 5 | us_ring_size_5 |
| 72 | 美度6号 | US Size 6 | us_ring_size_6 |
| 73 | 美度7号 | US Size 7 | us_ring_size_7 |
| 74 | 美度8号 | US Size 8 | us_ring_size_8 |
| 75 | 美度9号 | US Size 9 | us_ring_size_9 |
| 76 | 莫桑钻 | Moissanite | moissanite |
| 77 | 蓝色 | Blue | blue |
| 78 | 贝壳 | Shell | shell |
| 79 | 金色 | Gold | gold |
| 80 | 钛钢 | Titanium Steel | titanium_steel |
| 81 | 钻石 | Diamond | diamond |
| 82 | 铜合金 | Copper Alloy | copper_alloy |
| 83 | 铝合金 | Aluminum Alloy | aluminum_alloy |
| 84 | 银色 | Silver | silver |
| 85 | 锆石 | Cubic Zirconia | cubic_zirconia |
| 86 | 锌合金 | Zinc Alloy | zinc_alloy |
| 87 | 镀K金 | Gold Plated | gold_plated |
| 88 | 镀铂/铑/钯 | Platinum/Rhodium/Palladium Plated | platinum_rhodium_palladium_plated |
| 89 | 镀银 | Silver Plated | silver_plated |
| 90 | 阳极氧化 | Anodizing | anodizing |
| 91 | 随形 | Freeform | freeform |
| 92 | 非金属 | Non-Metal | non_metal |
| 93 | 食品零食 | Food & Snacks | food_snacks |

### 三、属性选项值 — 仅 category 表 allowed_attribute_value（54 个）

来源：`trade_pd_category_attribute.allowed_attribute_value`（未出现在 option 表中）

| 序号 | 中文 | 英文展示名 | 英文编码 (snake_case) |
| --- | --- | --- | --- |
| 1 | 316不锈钢 | 316 Stainless Steel | 316_stainless_steel |
| 2 | 3D 全彩打印 | 3D Full-Color Printing | 3d_full_color_printing |
| 3 | 3D彩色打印直出 | 3D Color Print Direct Output | 3d_color_print_direct |
| 4 | 3D打印+手工涂装 | 3D Print + Hand Painting | 3d_print_hand_painting |
| 5 | IMD双层工艺 | IMD Double-Layer Process | imd_double_layer |
| 6 | UV 打印 | UV Printing | uv_printing |
| 7 | cnc雕刻 | CNC Engraving | cnc_engraving |
| 8 | uv打印 | UV Printing | uv_printing |
| 9 | 全彩打印 | Full-Color Printing | full_color_printing |
| 10 | 冰箱贴底 | Fridge Magnet Base | fridge_magnet_base |
| 11 | 刺绣 | Embroidery | embroidery |
| 12 | 加厚高透亚克力 | Thick High-Transparency Acrylic | thick_high_transparency_acrylic |
| 13 | 华为 Nova 12 | Huawei Nova 12 | huawei_nova_12 |
| 14 | 华为 Nova 13 | Huawei Nova 13 | huawei_nova_13 |
| 15 | 华为 Nova13 | Huawei Nova 13 | huawei_nova_13 |
| 16 | 华为 Pura 70 | Huawei Pura 70 | huawei_pura_70 |
| 17 | 华为 Pura 70 Pro | Huawei Pura 70 Pro | huawei_pura_70_pro |
| 18 | 华为 Pura 70 Ultra | Huawei Pura 70 Ultra | huawei_pura_70_ultra |
| 19 | 华为Nova 12 | Huawei Nova 12 | huawei_nova_12 |
| 20 | 华为Nova13 | Huawei Nova 13 | huawei_nova_13 |
| 21 | 华为Pura 70 | Huawei Pura 70 | huawei_pura_70 |
| 22 | 华为Pura 70 Pro | Huawei Pura 70 Pro | huawei_pura_70_pro |
| 23 | 华为Pura 70 Ultra | Huawei Pura 70 Ultra | huawei_pura_70_ultra |
| 24 | 吧唧底 | Badge Base | badge_base |
| 25 | 塑料 | Plastic | plastic |
| 26 | 奖品 | Prize | prize |
| 27 | 套装 | Set / Bundle | set_bundle |
| 28 | 尼龙 | Nylon | nylon |
| 29 | 布艺 | Fabric Art | fabric_art |
| 30 | 布面 | Fabric Surface | fabric_surface |
| 31 | 法兰绒印花 | Flannel Print | flannel_print |
| 32 | 测试3 | Test 3 | test_3 |
| 33 | 滴胶亚克力 | Epoxy Acrylic | epoxy_acrylic |
| 34 | 热转印 | Heat Transfer Printing | heat_transfer_printing |
| 35 | 白色 | White | white |
| 36 | 白金 | Platinum / White Gold | platinum |
| 37 | 硅胶 | Silicone | silicone |
| 38 | 粉色 | Pink | pink |
| 39 | 纯棉 | Pure Cotton | pure_cotton |
| 40 | 纺羊绒 | Cashmere Blend | cashmere_blend |
| 41 | 组合套装 | Combo Set | combo_set |
| 42 | 绿色 | Green | green |
| 43 | 美码6号 | US Size 6 | us_size_6 |
| 44 | 美码7号 | US Size 7 | us_size_7 |
| 45 | 美码8号 | US Size 8 | us_size_8 |
| 46 | 美码9号 | US Size 9 | us_size_9 |
| 47 | 菲林转印工艺 | Film Transfer Process | film_transfer_process |
| 48 | 补邮费链接 | Postage Fee Link | postage_fee_link |
| 49 | 金属 | Metal | metal |
| 50 | 钛 | Titanium | titanium |
| 51 | 银合金 | Silver Alloy | silver_alloy |
| 52 | 陶瓷 | Ceramic | ceramic |
| 53 | 高透PC | High-Transparency PC | high_transparency_pc |
| 54 | 黑色 | Black | black |

### 四、全部选项类中文值去重并集（147 个）

来源：option 表 + category 表 `allowed_attribute_value` JSON 数组

| 序号 | 中文 | 英文展示名 | 英文编码 (snake_case) |
| --- | --- | --- | --- |
| 1 | 10cm以上 | Above 10cm | above_10cm |
| 2 | 10cm以下 | Below 10cm | below_10cm |
| 3 | 18K金 | 18K Gold | 18k_gold |
| 4 | 20cm及以下 | 20cm and Below | 20cm_and_below |
| 5 | 21cm(含)-50cm(含) | 21cm-50cm (inclusive) | 21cm_50cm_inclusive |
| 6 | 30cm以上 | Above 30cm | above_30cm |
| 7 | 316不锈钢 | 316 Stainless Steel | 316_stainless_steel |
| 8 | 3D 全彩打印 | 3D Full-Color Printing | 3d_full_color_printing |
| 9 | 3D彩色打印直出 | 3D Color Print Direct Output | 3d_color_print_direct |
| 10 | 3D打印+手工涂装 | 3D Print + Hand Painting | 3d_print_hand_painting |
| 11 | 3D硬金 | 3D Hard Gold | 3d_hard_gold |
| 12 | 4cm以下 | Below 4cm | below_4cm |
| 13 | 51cm(含)-80cm(含) | 51cm-80cm (inclusive) | 51cm_80cm_inclusive |
| 14 | 6cm以下 | Below 6cm | below_6cm |
| 15 | 81cm(含)-50cm(含) | 81cm-50cm (inclusive) | 81cm_50cm_inclusive |
| 16 | 81cm及以上 | 81cm and Above | 81cm_and_above |
| 17 | 925银 | 925 Silver | 925_silver |
| 18 | CNC雕刻 | CNC Engraving | cnc_engraving |
| 19 | IMD双层工艺 | IMD Double-Layer Process | imd_double_layer |
| 20 | UV 打印 | UV Printing | uv_printing |
| 21 | UV打印 | UV Printing | uv_printing |
| 22 | cnc雕刻 | CNC Engraving | cnc_engraving |
| 23 | uv打印 | UV Printing | uv_printing |
| 24 | 不锈钢 | Stainless Steel | stainless_steel |
| 25 | 亚克力 | Acrylic | acrylic |
| 26 | 仿珍珠 | Imitation Pearl | imitation_pearl |
| 27 | 做旧 | Antique Finish | antique_finish |
| 28 | 全彩打印 | Full-Color Printing | full_color_printing |
| 29 | 其他 | Other | other |
| 30 | 其他人造宝石 | Other Synthetic Gemstone | other_synthetic_gemstone |
| 31 | 其他天然宝石 | Other Natural Gemstone | other_natural_gemstone |
| 32 | 冰箱贴底 | Fridge Magnet Base | fridge_magnet_base |
| 33 | 几何形状 | Geometric Shape | geometric_shape |
| 34 | 切半面 | Half-Cut Face | half_cut_face |
| 35 | 刺绣 | Embroidery | embroidery |
| 36 | 加厚高透亚克力 | Thick High-Transparency Acrylic | thick_high_transparency_acrylic |
| 37 | 动物昆虫 | Animals & Insects | animals_insects |
| 38 | 动画动漫 | Animation & Anime | animation_anime |
| 39 | 华为 Nova 12 | Huawei Nova 12 | huawei_nova_12 |
| 40 | 华为 Nova 13 | Huawei Nova 13 | huawei_nova_13 |
| 41 | 华为 Nova13 | Huawei Nova 13 | huawei_nova_13 |
| 42 | 华为 Pura 70 | Huawei Pura 70 | huawei_pura_70 |
| 43 | 华为 Pura 70 Pro | Huawei Pura 70 Pro | huawei_pura_70_pro |
| 44 | 华为 Pura 70 Ultra | Huawei Pura 70 Ultra | huawei_pura_70_ultra |
| 45 | 华为Nova 12 | Huawei Nova 12 | huawei_nova_12 |
| 46 | 华为Nova13 | Huawei Nova 13 | huawei_nova_13 |
| 47 | 华为Pura 70 | Huawei Pura 70 | huawei_pura_70 |
| 48 | 华为Pura 70 Pro | Huawei Pura 70 Pro | huawei_pura_70_pro |
| 49 | 华为Pura 70 Ultra | Huawei Pura 70 Ultra | huawei_pura_70_ultra |
| 50 | 单色3D打印 | Single-Color 3D Printing | single_color_3d_printing |
| 51 | 印花 | Print / Pattern Print | print_pattern |
| 52 | 吧唧底 | Badge Base | badge_base |
| 53 | 圆形 | Round | round |
| 54 | 基本处理 | Basic Treatment | basic_treatment |
| 55 | 塑料 | Plastic | plastic |
| 56 | 奖品 | Prize | prize |
| 57 | 套装 | Set / Bundle | set_bundle |
| 58 | 完整（不切面） | Complete (Uncut) | complete_uncut |
| 59 | 尼龙 | Nylon | nylon |
| 60 | 布艺 | Fabric Art | fabric_art |
| 61 | 布面 | Fabric Surface | fabric_surface |
| 62 | 彩色3D打印 | Color 3D Printing | color_3d_printing |
| 63 | 心形 | Heart Shape | heart_shape |
| 64 | 手工涂装 | Hand Painting | hand_painting |
| 65 | 拉砂 | Brushed Finish | brushed_finish |
| 66 | 方形 | Square | square |
| 67 | 无 | None | none |
| 68 | 日月星辰 | Sun, Moon & Stars | sun_moon_stars |
| 69 | 木质 | Wood | wood |
| 70 | 树脂 | Resin | resin |
| 71 | 棉花 | Cotton Filling | cotton_filling |
| 72 | 植物花卉 | Plants & Flowers | plants_flowers |
| 73 | 橡胶 | Rubber | rubber |
| 74 | 毛绒 | Plush | plush |
| 75 | 水果蔬菜 | Fruits & Vegetables | fruits_vegetables |
| 76 | 水滴 | Teardrop | teardrop |
| 77 | 法兰绒印花 | Flannel Print | flannel_print |
| 78 | 活口可调节 | Adjustable Open Ring | adjustable_open_ring |
| 79 | 测试3 | Test 3 | test_3 |
| 80 | 海洋生物 | Marine Life | marine_life |
| 81 | 海绵 | Sponge | sponge |
| 82 | 涂装 | Coating / Painting | coating_painting |
| 83 | 滴胶亚克力 | Epoxy Acrylic | epoxy_acrylic |
| 84 | 热转印 | Heat Transfer Printing | heat_transfer_printing |
| 85 | 玉石 | Jade | jade |
| 86 | 玫瑰金 | Rose Gold | rose_gold |
| 87 | 玻璃 | Glass | glass |
| 88 | 珍珠 | Pearl | pearl |
| 89 | 珐琅 | Enamel | enamel |
| 90 | 生肖星座 | Zodiac & Constellation | zodiac_constellation |
| 91 | 电绣 | Machine Embroidery | machine_embroidery |
| 92 | 电镀 | Electroplating | electroplating |
| 93 | 白色 | White | white |
| 94 | 白金 | Platinum / White Gold | platinum |
| 95 | 矩形 | Rectangle | rectangle |
| 96 | 硅胶 | Silicone | silicone |
| 97 | 粉色 | Pink | pink |
| 98 | 红色 | Red | red |
| 99 | 纯棉 | Pure Cotton | pure_cotton |
| 100 | 纯金 | Pure Gold | pure_gold |
| 101 | 纯银 | Pure Silver | pure_silver |
| 102 | 纺羊绒 | Cashmere Blend | cashmere_blend |
| 103 | 组合套装 | Combo Set | combo_set |
| 104 | 绿色 | Green | green |
| 105 | 美度10号 | US Size 10 | us_ring_size_10 |
| 106 | 美度11号 | US Size 11 | us_ring_size_11 |
| 107 | 美度12号 | US Size 12 | us_ring_size_12 |
| 108 | 美度13号 | US Size 13 | us_ring_size_13 |
| 109 | 美度14号 | US Size 14 | us_ring_size_14 |
| 110 | 美度15号 | US Size 15 | us_ring_size_15 |
| 111 | 美度16号 | US Size 16 | us_ring_size_16 |
| 112 | 美度17号 | US Size 17 | us_ring_size_17 |
| 113 | 美度5号 | US Size 5 | us_ring_size_5 |
| 114 | 美度6号 | US Size 6 | us_ring_size_6 |
| 115 | 美度7号 | US Size 7 | us_ring_size_7 |
| 116 | 美度8号 | US Size 8 | us_ring_size_8 |
| 117 | 美度9号 | US Size 9 | us_ring_size_9 |
| 118 | 美码6号 | US Size 6 | us_size_6 |
| 119 | 美码7号 | US Size 7 | us_size_7 |
| 120 | 美码8号 | US Size 8 | us_size_8 |
| 121 | 美码9号 | US Size 9 | us_size_9 |
| 122 | 莫桑钻 | Moissanite | moissanite |
| 123 | 菲林转印工艺 | Film Transfer Process | film_transfer_process |
| 124 | 蓝色 | Blue | blue |
| 125 | 补邮费链接 | Postage Fee Link | postage_fee_link |
| 126 | 贝壳 | Shell | shell |
| 127 | 金属 | Metal | metal |
| 128 | 金色 | Gold | gold |
| 129 | 钛 | Titanium | titanium |
| 130 | 钛钢 | Titanium Steel | titanium_steel |
| 131 | 钻石 | Diamond | diamond |
| 132 | 铜合金 | Copper Alloy | copper_alloy |
| 133 | 铝合金 | Aluminum Alloy | aluminum_alloy |
| 134 | 银合金 | Silver Alloy | silver_alloy |
| 135 | 银色 | Silver | silver |
| 136 | 锆石 | Cubic Zirconia | cubic_zirconia |
| 137 | 锌合金 | Zinc Alloy | zinc_alloy |
| 138 | 镀K金 | Gold Plated | gold_plated |
| 139 | 镀铂/铑/钯 | Platinum/Rhodium/Palladium Plated | platinum_rhodium_palladium_plated |
| 140 | 镀银 | Silver Plated | silver_plated |
| 141 | 阳极氧化 | Anodizing | anodizing |
| 142 | 陶瓷 | Ceramic | ceramic |
| 143 | 随形 | Freeform | freeform |
| 144 | 非金属 | Non-Metal | non_metal |
| 145 | 食品零食 | Food & Snacks | food_snacks |
| 146 | 高透PC | High-Transparency PC | high_transparency_pc |
| 147 | 黑色 | Black | black |

---

## 英文化映射表

说明：
- **英文展示名**：面向用户/运营展示的英文文案。
- **英文编码**：建议用于 `code`、枚举键、API 字段值的 snake_case 标识；品牌型号、尺寸等保留可读英文。
- 同义重复项（如 `UV打印` / `UV 打印` / `uv打印`）映射到相同英文编码，迁移时需做归一化。

### 属性名映射（37 条）

| 中文 | 英文展示名 | 英文编码 |
| --- | --- | --- |
| UV打印 | UV Printing | uv_printing |
| 主石形状 | Main Stone Shape | main_stone_shape |
| 主石材质 | Main Stone Material | main_stone_material |
| 二次工艺 | Secondary Process | secondary_process |
| 印刷部位 | Print Area | print_area |
| 厚度 | Thickness | thickness |
| 吊坠材质 | Pendant Material | pendant_material |
| 图案 | Pattern | pattern |
| 图案元素 | Pattern Element | pattern_element |
| 圆球水杯盖颜色 | Sphere Cup Lid Color | sphere_cup_lid_color |
| 填充 | Filling | filling |
| 外观规格 | Appearance Specification | appearance_specification |
| 外观颜色 | Appearance Color | appearance_color |
| 定制要求 | Customization Requirements | customization_requirements |
| 定制部位 | Customization Area | customization_area |
| 容量 | Capacity | capacity |
| 尺寸 | Size | size |
| 底部 | Bottom | bottom |
| 延长链 | Extension Chain | extension_chain |
| 形状 | Shape | shape |
| 总重量 | Total Weight | total_weight |
| 成型工艺 | Forming Process | forming_process |
| 手寸 | Ring Size | ring_size |
| 数量 | Quantity | quantity |
| 材质 | Material | material |
| 款式 | Style | style |
| 色板选项 | Color Swatch Option | color_swatch_option |
| 色纸包边 | Color Paper Edge | color_paper_edge |
| 表面处理 | Surface Treatment | surface_treatment |
| 表面工艺 | Surface Process | surface_process |
| 覆膜款式 | Lamination Style | lamination_style |
| 规格 | Specification | specification |
| 配件款式 | Accessory Style | accessory_style |
| 链条材质 | Chain Material | chain_material |
| 链长尺寸 | Chain Length | chain_length |
| 颜色 | Color | color |
| 颜色yxf-test | Color YXF Test | color_yxf_test |

### 选项值映射（147 条）

| 中文 | 英文展示名 | 英文编码 |
| --- | --- | --- |
| 10cm以上 | Above 10cm | above_10cm |
| 10cm以下 | Below 10cm | below_10cm |
| 18K金 | 18K Gold | 18k_gold |
| 20cm及以下 | 20cm and Below | 20cm_and_below |
| 21cm(含)-50cm(含) | 21cm-50cm (inclusive) | 21cm_50cm_inclusive |
| 30cm以上 | Above 30cm | above_30cm |
| 316不锈钢 | 316 Stainless Steel | 316_stainless_steel |
| 3D 全彩打印 | 3D Full-Color Printing | 3d_full_color_printing |
| 3D彩色打印直出 | 3D Color Print Direct Output | 3d_color_print_direct |
| 3D打印+手工涂装 | 3D Print + Hand Painting | 3d_print_hand_painting |
| 3D硬金 | 3D Hard Gold | 3d_hard_gold |
| 4cm以下 | Below 4cm | below_4cm |
| 51cm(含)-80cm(含) | 51cm-80cm (inclusive) | 51cm_80cm_inclusive |
| 6cm以下 | Below 6cm | below_6cm |
| 81cm(含)-50cm(含) | 81cm-50cm (inclusive) | 81cm_50cm_inclusive |
| 81cm及以上 | 81cm and Above | 81cm_and_above |
| 925银 | 925 Silver | 925_silver |
| CNC雕刻 | CNC Engraving | cnc_engraving |
| IMD双层工艺 | IMD Double-Layer Process | imd_double_layer |
| UV 打印 | UV Printing | uv_printing |
| UV打印 | UV Printing | uv_printing |
| cnc雕刻 | CNC Engraving | cnc_engraving |
| uv打印 | UV Printing | uv_printing |
| 不锈钢 | Stainless Steel | stainless_steel |
| 亚克力 | Acrylic | acrylic |
| 仿珍珠 | Imitation Pearl | imitation_pearl |
| 做旧 | Antique Finish | antique_finish |
| 全彩打印 | Full-Color Printing | full_color_printing |
| 其他 | Other | other |
| 其他人造宝石 | Other Synthetic Gemstone | other_synthetic_gemstone |
| 其他天然宝石 | Other Natural Gemstone | other_natural_gemstone |
| 冰箱贴底 | Fridge Magnet Base | fridge_magnet_base |
| 几何形状 | Geometric Shape | geometric_shape |
| 切半面 | Half-Cut Face | half_cut_face |
| 刺绣 | Embroidery | embroidery |
| 加厚高透亚克力 | Thick High-Transparency Acrylic | thick_high_transparency_acrylic |
| 动物昆虫 | Animals & Insects | animals_insects |
| 动画动漫 | Animation & Anime | animation_anime |
| 华为 Nova 12 | Huawei Nova 12 | huawei_nova_12 |
| 华为 Nova 13 | Huawei Nova 13 | huawei_nova_13 |
| 华为 Nova13 | Huawei Nova 13 | huawei_nova_13 |
| 华为 Pura 70 | Huawei Pura 70 | huawei_pura_70 |
| 华为 Pura 70 Pro | Huawei Pura 70 Pro | huawei_pura_70_pro |
| 华为 Pura 70 Ultra | Huawei Pura 70 Ultra | huawei_pura_70_ultra |
| 华为Nova 12 | Huawei Nova 12 | huawei_nova_12 |
| 华为Nova13 | Huawei Nova 13 | huawei_nova_13 |
| 华为Pura 70 | Huawei Pura 70 | huawei_pura_70 |
| 华为Pura 70 Pro | Huawei Pura 70 Pro | huawei_pura_70_pro |
| 华为Pura 70 Ultra | Huawei Pura 70 Ultra | huawei_pura_70_ultra |
| 单色3D打印 | Single-Color 3D Printing | single_color_3d_printing |
| 印花 | Print / Pattern Print | print_pattern |
| 吧唧底 | Badge Base | badge_base |
| 圆形 | Round | round |
| 基本处理 | Basic Treatment | basic_treatment |
| 塑料 | Plastic | plastic |
| 奖品 | Prize | prize |
| 套装 | Set / Bundle | set_bundle |
| 完整（不切面） | Complete (Uncut) | complete_uncut |
| 尼龙 | Nylon | nylon |
| 布艺 | Fabric Art | fabric_art |
| 布面 | Fabric Surface | fabric_surface |
| 彩色3D打印 | Color 3D Printing | color_3d_printing |
| 心形 | Heart Shape | heart_shape |
| 手工涂装 | Hand Painting | hand_painting |
| 拉砂 | Brushed Finish | brushed_finish |
| 方形 | Square | square |
| 无 | None | none |
| 日月星辰 | Sun, Moon & Stars | sun_moon_stars |
| 木质 | Wood | wood |
| 树脂 | Resin | resin |
| 棉花 | Cotton Filling | cotton_filling |
| 植物花卉 | Plants & Flowers | plants_flowers |
| 橡胶 | Rubber | rubber |
| 毛绒 | Plush | plush |
| 水果蔬菜 | Fruits & Vegetables | fruits_vegetables |
| 水滴 | Teardrop | teardrop |
| 法兰绒印花 | Flannel Print | flannel_print |
| 活口可调节 | Adjustable Open Ring | adjustable_open_ring |
| 测试3 | Test 3 | test_3 |
| 海洋生物 | Marine Life | marine_life |
| 海绵 | Sponge | sponge |
| 涂装 | Coating / Painting | coating_painting |
| 滴胶亚克力 | Epoxy Acrylic | epoxy_acrylic |
| 热转印 | Heat Transfer Printing | heat_transfer_printing |
| 玉石 | Jade | jade |
| 玫瑰金 | Rose Gold | rose_gold |
| 玻璃 | Glass | glass |
| 珍珠 | Pearl | pearl |
| 珐琅 | Enamel | enamel |
| 生肖星座 | Zodiac & Constellation | zodiac_constellation |
| 电绣 | Machine Embroidery | machine_embroidery |
| 电镀 | Electroplating | electroplating |
| 白色 | White | white |
| 白金 | Platinum / White Gold | platinum |
| 矩形 | Rectangle | rectangle |
| 硅胶 | Silicone | silicone |
| 粉色 | Pink | pink |
| 红色 | Red | red |
| 纯棉 | Pure Cotton | pure_cotton |
| 纯金 | Pure Gold | pure_gold |
| 纯银 | Pure Silver | pure_silver |
| 纺羊绒 | Cashmere Blend | cashmere_blend |
| 组合套装 | Combo Set | combo_set |
| 绿色 | Green | green |
| 美度10号 | US Size 10 | us_ring_size_10 |
| 美度11号 | US Size 11 | us_ring_size_11 |
| 美度12号 | US Size 12 | us_ring_size_12 |
| 美度13号 | US Size 13 | us_ring_size_13 |
| 美度14号 | US Size 14 | us_ring_size_14 |
| 美度15号 | US Size 15 | us_ring_size_15 |
| 美度16号 | US Size 16 | us_ring_size_16 |
| 美度17号 | US Size 17 | us_ring_size_17 |
| 美度5号 | US Size 5 | us_ring_size_5 |
| 美度6号 | US Size 6 | us_ring_size_6 |
| 美度7号 | US Size 7 | us_ring_size_7 |
| 美度8号 | US Size 8 | us_ring_size_8 |
| 美度9号 | US Size 9 | us_ring_size_9 |
| 美码6号 | US Size 6 | us_size_6 |
| 美码7号 | US Size 7 | us_size_7 |
| 美码8号 | US Size 8 | us_size_8 |
| 美码9号 | US Size 9 | us_size_9 |
| 莫桑钻 | Moissanite | moissanite |
| 菲林转印工艺 | Film Transfer Process | film_transfer_process |
| 蓝色 | Blue | blue |
| 补邮费链接 | Postage Fee Link | postage_fee_link |
| 贝壳 | Shell | shell |
| 金属 | Metal | metal |
| 金色 | Gold | gold |
| 钛 | Titanium | titanium |
| 钛钢 | Titanium Steel | titanium_steel |
| 钻石 | Diamond | diamond |
| 铜合金 | Copper Alloy | copper_alloy |
| 铝合金 | Aluminum Alloy | aluminum_alloy |
| 银合金 | Silver Alloy | silver_alloy |
| 银色 | Silver | silver |
| 锆石 | Cubic Zirconia | cubic_zirconia |
| 锌合金 | Zinc Alloy | zinc_alloy |
| 镀K金 | Gold Plated | gold_plated |
| 镀铂/铑/钯 | Platinum/Rhodium/Palladium Plated | platinum_rhodium_palladium_plated |
| 镀银 | Silver Plated | silver_plated |
| 阳极氧化 | Anodizing | anodizing |
| 陶瓷 | Ceramic | ceramic |
| 随形 | Freeform | freeform |
| 非金属 | Non-Metal | non_metal |
| 食品零食 | Food & Snacks | food_snacks |
| 高透PC | High-Transparency PC | high_transparency_pc |
| 黑色 | Black | black |

---

## 迁移注意事项

1. **`trade_pd_attribute`**：需同时更新 `code` 与 `name`（二者当前基本一致）。
2. **`trade_pd_attribute_option`**：更新 `value` 列；`option_id` 若与中文值绑定需另行评估。
3. **`trade_pd_category_attribute`**：需更新 `attribute_code`、`attribute_name`，以及 `allowed_attribute_value` JSON 数组内的每个元素。
4. **大小写/空格变体**：如 `UV打印`、`UV 打印`、`uv打印`、`cnc雕刻` 建议统一为一个英文编码后再入库。
5. **非中文数据**：`test`/`test2`/`UV` 等测试属性、纯 `cm` 尺寸值（如 `10cm`）无需翻译。

---

*生成时间：2026-06-15*
