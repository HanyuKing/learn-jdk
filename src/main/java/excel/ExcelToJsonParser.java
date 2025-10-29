package excel;

import com.alibaba.fastjson.JSON;
import excel.dto.CustomProductConfigDto;
import excel.dto.ProductConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelToJsonParser {

    @Test
    public void parseJson() throws Exception {
        String path = "/Users/rogerswang/my/source_code/learn-jdk/src/main/java/excel/";
        String fileName = "顾客定制-服务端配置所需-正式.xlsx";
        String filePath = path + fileName;
        List<ProductTemplate> productTemplateList = parseExcel(filePath);

        List<CustomProductTemplate> customProductTemplateList = toCustomProductList(productTemplateList);

        System.out.println(JSON.toJSONString(customProductTemplateList));

        // 将JSON写入文件
        java.io.FileWriter fileWriter = new java.io.FileWriter(path + "output/" + fileName + "-模板.json");
        fileWriter.write(JSON.toJSONString(customProductTemplateList));
        fileWriter.close();

        List<ProductConfig> productConfigList = ProductConfigParser.parseProductSheet(filePath, "商品相关");

        String sql = generateSQL(customProductTemplateList, productConfigList);
        fileWriter = new java.io.FileWriter(path + "output/" + fileName + "-模板.sql");
        fileWriter.write(sql);
        fileWriter.close();
    }

    private String generateSQL(List<CustomProductTemplate> customProductTemplateList,
                               List<ProductConfig> productConfigList) {

        Map<String, String> productConfigMap = new HashMap<>();
        for (ProductConfig config : productConfigList) {
            try {
                CustomProductConfigDto customProductConfigDto = CustomProductConfigDto.builder()
                        .productId(config.getProductId())
                        .dpi(Integer.parseInt(config.getResolution()))
                        .ratio(Float.parseFloat(config.getAspectRatio().split(":")[0]) / Float.parseFloat(config.getAspectRatio().split(":")[1]))
                        .productFileType(Integer.parseInt(config.getFileType()))
                        .hasBorder("是".equals(config.getHasBorder()))
                        .build();
                productConfigMap.put(config.getSpuName(), JSON.toJSONString(customProductConfigDto));
            } catch (NumberFormatException e) {
                System.out.println("商品相关格式配置错误: " + e + ", config: " + JSON.toJSONString(config));
            }
        }

        StringBuilder sqlBuilder = new StringBuilder();
        for (CustomProductTemplate customProductTemplate : customProductTemplateList) {
            String sql = String.format(
                    "INSERT INTO `trade_pd_custom_product_config` (" +
                            "`product_id`, " +
                            "`product_config`, " +
                            "`product_template_config`) values ('%s', '%s', '%s');\n",
                    customProductTemplate.productId,
                    productConfigMap.get(StringUtils.trim(customProductTemplate.productInfo.productName)),
                    JSON.toJSONString(customProductTemplate)
            );
            sqlBuilder.append(sql);
        }
        return sqlBuilder.toString();
    }

    private List<CustomProductTemplate> toCustomProductList(List<ProductTemplate> productTemplateList) throws Exception {
        List<CustomProductTemplate> customProductTemplateList = new ArrayList<>();

        for (ProductTemplate product : productTemplateList) {
            if (CollectionUtils.isEmpty(product.templateList)) {
                continue;
            }
            CustomProductTemplate customProductTemplate = new CustomProductTemplate();
            customProductTemplate.setProductId(product.productId);
            // 设置产品信息
            customProductTemplate.productInfo = new ProductInfo();
            customProductTemplate.productInfo.productName = product.productName;
            customProductTemplate.productInfo.productId = product.productId;

            // 设置产品面信息
            customProductTemplate.productSide = new ArrayList<>();

            // 所有模板
            List<Template> templateList = product.templateList;
            for (Template template : templateList) {
                FrontSide front = template.front;
                FrontSide back = template.back;

                ProductSide frontSide = toCustomProductSide(front);
                frontSide.title = "正面";
                ProductSide backSide = toCustomProductSide(back);
                backSide.title = "背面";

                if (!CollectionUtils.isEmpty(frontSide.frontSide.getLevelInfo())) {
                    customProductTemplate.productSide.add(frontSide);
                }
                if (!CollectionUtils.isEmpty(backSide.frontSide.getLevelInfo())) {
                    customProductTemplate.productSide.add(backSide);
                }
            }

            // group by title
            Map<String, List<ProductSide>> productSideMap = new HashMap<>();
            for (ProductSide side : customProductTemplate.productSide) {
                productSideMap.computeIfAbsent(side.title, k -> new ArrayList<>()).add(side);
            }

            List<ProductSide> finalProductSideList = new ArrayList<>();
            List<ProductSide> allFrontProductSide = productSideMap.get("正面");
            List<ProductSide> allBackProductSide = productSideMap.get("背面");

            if (!CollectionUtils.isEmpty(allFrontProductSide)) {
                finalProductSideList.add(mergeLevelInfo(allFrontProductSide, "正面"));
            }
            if (!CollectionUtils.isEmpty(allBackProductSide)) {
                finalProductSideList.add(mergeLevelInfo(allBackProductSide, "背面"));
            }

            customProductTemplate.productSide = finalProductSideList;
            customProductTemplateList.add(customProductTemplate);
        }

        for (CustomProductTemplate customProductTemplate : customProductTemplateList) {
            if (customProductTemplate.productSide.size() > 1) {
                List<SideOption> totalSideOptionList = new ArrayList<>();
                for (int i = 0; i < customProductTemplate.productSide.size(); i++) {
                    ProductSide productSide = customProductTemplate.productSide.get(i);
                    SideOption sideOption = new SideOption();
                    sideOption.index = i;
                    sideOption.type = sideNameMap.get(productSide.title);
                    sideOption.name = productSide.title;
                    totalSideOptionList.add(sideOption);
                }

                for (int i = 0; i < customProductTemplate.productSide.size(); i++) {
                    ProductSide productSide = customProductTemplate.productSide.get(i);
                    List<SideOption> otherSideOptionList = new ArrayList<>();
                    for (int j = 0; j < totalSideOptionList.size(); j++) {
                        if (i == j) {
                            continue;
                        }
                        SideOption othertSideOption = totalSideOptionList.get(j);
                        otherSideOptionList.add(othertSideOption);
                    }
                    productSide.sideOptions = otherSideOptionList;
                }
            }
        }

        // 排序
        levelSort(customProductTemplateList);

        return customProductTemplateList;
    }

    private void levelSort(List<CustomProductTemplate> customProductTemplateList) {
        for (CustomProductTemplate customProductTemplate : customProductTemplateList) {
            List<ProductSide> productSideList = customProductTemplate.productSide;
            for (ProductSide productSide : productSideList) {
                List<LevelInfo> levelInfoList = productSide.frontSide.levelInfo;
                levelInfoList.sort((o1, o2) -> levelSortMap.get(o1.getName()) - levelSortMap.get(o2.getName()));
                productSide.frontSide.levelInfo = levelInfoList;
            }
        }
    }

    Map<String, Integer> levelSortMap = new HashMap<String, Integer>() {{
        put("图片", 1);
        put("边框", 2);
        put("背景", 3);
    }};

    Map<String, String> sideNameMap = new HashMap<String, String>() {{
        put("正面", "front");
        put("背面", "back");
    }};

    private ProductSide mergeLevelInfo(List<ProductSide> allFrontProductSide, String sideName) throws UnsupportedEncodingException {
        ProductSide productSideResult = new ProductSide();
        List<LevelInfo> allLevelInfo = new ArrayList<>();
        for (ProductSide productSide : allFrontProductSide) {
            allLevelInfo.addAll(productSide.frontSide.levelInfo);
        }

        Map<String, List<LevelInfo>> levelInfoMap = new HashMap<>();
        for (LevelInfo levelInfo : allLevelInfo) {
            levelInfoMap.computeIfAbsent(levelInfo.getType(), k -> new ArrayList<>()).add(levelInfo);
        }

        int idCount = 1;
        int nameCount = 1;
        int layerId = 1;

        List<LevelInfo> levelInfoList = new ArrayList<>();

        for (Map.Entry<String, List<LevelInfo>> entry : levelInfoMap.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                List<Frame> allFrameList = new ArrayList<>();
                for (LevelInfo levelInfo : entry.getValue()) {
                    String image = levelInfo.getImage();
                    String sImage = null;
                    if (image != null && !image.isEmpty()) {
                        // image = image.replace("https://mm-jjewelry-prod-bj.tos-cn-beijing.volces.com/", "https://static.zaohaowu.com/");
                        image = image + "?x-tos-process=image/format,webp";// URLDecoder.decode(image, "UTF-8");
                        sImage = image + ",image/resize,p_10";
                    }
                    allFrameList.add(Frame.builder()
                            .id(String.valueOf(idCount++))
                            .name(String.valueOf(nameCount++))
                            .image(image)
                            .s_image(sImage)
                            .frameMask(levelInfo.getFrameMask() + "?x-tos-process=image/format,webp")
                            .build());
                }
                if ("customMade".equals(entry.getKey())) {
                    allFrameList = null;
                }
                LevelInfo levelInfoResult = LevelInfo.builder()
                        .id(String.valueOf(layerId++))
                        .type(entry.getKey())
                        .name(entry.getValue().get(0).getName())
                        .zIndex(entry.getValue().get(0).getZIndex())
                        .allowEditing(entry.getValue().get(0).allowEditing)
                        .frameList(allFrameList)
                        .build();
                levelInfoList.add(levelInfoResult);
            }
        }


        productSideResult.title = sideName;
        productSideResult.frontSide = CustomFrontSide.builder().levelInfo(levelInfoList).build();

        return productSideResult;
    }

    private Map<String, String> levelTypeMap = new HashMap<String, String>() {{
        put("背景", "bg");
        put("边框", "border");
        put("用户定制", "customMade");
    }};
    private Map<String, String> levelNameMap = new HashMap<String, String>() {{
        put("背景", "背景");
        put("边框", "边框");
        put("用户定制", "图片");
    }};

    private ProductSide toCustomProductSide(FrontSide front) throws UnsupportedEncodingException {
        ProductSide productSide = new ProductSide();
        productSide.frontSide = new CustomFrontSide();
        productSide.frontSide.levelInfo = new ArrayList<>();

        if (front.layer1 != null && StringUtils.isNotEmpty(front.layer1.layerType)) {
            productSide.frontSide.levelInfo.add(LevelInfo.builder()
                    .type(levelTypeMap.get(front.layer1.layerType))
                    .name(levelNameMap.get(front.layer1.layerType))
                    .zIndex(1)
                    .allowEditing(Boolean.TRUE.equals(front.layer1.editable))
                    .image(front.layer1.imageUrl)
                    .frameList(null)
                    .build());
        }
        if (front.layer2 != null && StringUtils.isNotEmpty(front.layer2.layerType)) {
            productSide.frontSide.levelInfo.add(LevelInfo.builder()
                    .type(levelTypeMap.get(front.layer2.layerType))
                    .name(levelNameMap.get(front.layer2.layerType))
                    .zIndex(2)
                    .allowEditing(Boolean.TRUE.equals(front.layer2.editable))
                    .image(front.layer2.imageUrl)
                    .frameList(null)
                    .build());
        }
        if (front.layer3 != null && StringUtils.isNotEmpty(front.layer3.layerType)) {
            productSide.frontSide.levelInfo.add(LevelInfo.builder()
                    .type(levelTypeMap.get(front.layer3.layerType))
                    .name(levelNameMap.get(front.layer3.layerType))
                    .zIndex(3)
                    .allowEditing(Boolean.TRUE.equals(front.layer3.editable))
                    .image(front.layer3.imageUrl)
                    .frameList(null)
                    .build());
        }

        for (LevelInfo levelInfo : productSide.frontSide.levelInfo) {
            if ("border".equals(levelInfo.getType())) {
                if (front.maskLayer != null && StringUtils.isNotEmpty(front.maskLayer.imageUrl)) {
                    // levelInfo.setFrameMask(URLDecoder.decode(front.maskLayer.imageUrl, "UTF-8"));
                    levelInfo.setFrameMask(front.maskLayer.imageUrl);
                }
            }
        }
        return productSide;
    }

    @Test
    public void testExcelToJson() throws Exception {
        String path = "/Users/rogerswang/my/source_code/learn-jdk/src/main/java/excel/";
        String filePath = path + "顾客定制技术侧所需.xlsx";
        List<ProductTemplate> productList = parseExcel(filePath);

        // 输出JSON
        String json = toJsonString(productList);
        System.out.println(json);

        // 将JSON写入文件
        java.io.FileWriter fileWriter = new java.io.FileWriter(path + "顾客定制技术侧所需.json");
        fileWriter.write(json);
        fileWriter.close();
    }

    public List<ProductTemplate> parseExcel(String filePath) throws Exception {
        List<ProductTemplate> productList = new ArrayList<>();

        FileInputStream file = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheet("商品模板");

        String currentProductName = null;
        String currentProductId = null;
        ProductTemplate currentProduct = null;

        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // 获取商品名称
            Cell productNameCell = row.getCell(0);
            Cell productIdCell = row.getCell(1);
            if (productNameCell != null && !productNameCell.toString().trim().isEmpty()
                    && productIdCell != null && !productIdCell.toString().trim().isEmpty()) {
                currentProductName = productNameCell.toString().trim();
                currentProductId = productIdCell.toString().trim();
                currentProduct = new ProductTemplate();
                currentProduct.productName = currentProductName;
                currentProduct.productId = currentProductId;
                currentProduct.templateList = new ArrayList<>();
                productList.add(currentProduct);
            }

            if (currentProduct == null) continue;

            // 解析模板数据
            Template template = parseTemplate(row);
            if (template != null) {
                currentProduct.templateList.add(template);
            }
        }

        workbook.close();
        file.close();
        return productList;
    }

    private static Template parseTemplate(Row row) {
        Template template = new Template();

        // 模板名称
        Cell templateNameCell = row.getCell(2);
        if (templateNameCell == null || templateNameCell.toString().trim().isEmpty()) {
            return null;
        }
        template.templateName = templateNameCell.toString().trim();

        // 正面图层1
        template.front = new FrontSide();
        template.front.layer1 = parseLayer(row, 3, 4, 5);
        if (StringUtils.isEmpty(template.front.layer1.layerType)) {
            return null;
        }

        // 正面图层2
        template.front.layer2 = parseLayer(row, 6, 7, 8);

        // 正面图层3
        template.front.layer3 = parseLayer(row, 9, 10, 11);

        // 正面蒙层
        template.front.maskLayer = new MaskLayer();
        Cell maskImageCell = row.getCell(12);
        if (maskImageCell != null) {
            template.front.maskLayer.imageUrl = maskImageCell.toString().trim();
        }

        // 背面图层1
        template.back = new FrontSide();
        template.back.layer1 = parseLayer(row, 13, 14, 15);

        return template;
    }

    private static Layer parseLayer(Row row, int typeCol, int imageCol, int editableCol) {
        Layer layer = new Layer();

        Cell typeCell = row.getCell(typeCol);
        Cell imageCell = row.getCell(imageCol);
        Cell editableCell = row.getCell(editableCol);

        if (typeCell != null) {
            layer.layerType = typeCell.toString().trim();
        }

        if (imageCell != null) {
            layer.imageUrl = imageCell.toString().trim();
        }

        if (editableCell != null) {
            String editableStr = editableCell.toString().trim();
            layer.editable = "是".equals(editableStr) || "true".equalsIgnoreCase(editableStr);
        }

        return layer;
    }

    public static String toJsonString(List<ProductTemplate> productList) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < productList.size(); i++) {
            ProductTemplate product = productList.get(i);
            json.append("  {\n");
            json.append("    \"商品名称\": \"").append(escapeJson(product.productName)).append("\",\n");
            json.append("    \"商品ID\": \"").append(escapeJson(product.productId)).append("\",\n");
            json.append("    \"模板列表\": [\n");

            for (int j = 0; j < product.templateList.size(); j++) {
                Template template = product.templateList.get(j);
                json.append("      {\n");
                json.append("        \"模板名称\": \"").append(escapeJson(template.templateName)).append("\",\n");
                json.append("        \"正面\": {\n");

                // 正面图层
                json.append("          \"图层1\": ").append(layerToJson(template.front.layer1)).append(",\n");
                json.append("          \"图层2\": ").append(layerToJson(template.front.layer2)).append(",\n");
                json.append("          \"图层3\": ").append(layerToJson(template.front.layer3)).append(",\n");
                json.append("          \"蒙层\": {\n");
                json.append("            \"图片\": \"").append(escapeJson(template.front.maskLayer.imageUrl)).append("\"\n");
                json.append("          }\n");
                json.append("        },\n");

                // 背面图层
                json.append("        \"背面\": {\n");
                json.append("          \"图层1\": ").append(layerToJson(template.back.layer1)).append("\n");
                json.append("        }\n");

                json.append("      }").append(j < product.templateList.size() - 1 ? "," : "").append("\n");
            }

            json.append("    ]\n");
            json.append("  }").append(i < productList.size() - 1 ? "," : "").append("\n");
        }

        json.append("]");
        return json.toString();
    }

    private static String layerToJson(Layer layer) {
        if (layer == null) return "null";

        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("            \"图层类型\": \"").append(escapeJson(layer.layerType)).append("\",\n");
        json.append("            \"图片\": \"").append(escapeJson(layer.imageUrl)).append("\",\n");
        json.append("            \"是否可编辑\": ").append(layer.editable != null ? layer.editable : "false");
        json.append("\n          }");
        return json.toString();
    }

    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // 数据类
    static class ProductTemplate {
        String productName;
        String productId;
        List<Template> templateList;
    }

    static class Template {
        String templateName;
        FrontSide front;
        FrontSide back;
    }

    static class FrontSide {
        Layer layer1;
        Layer layer2;
        Layer layer3;
        MaskLayer maskLayer;
    }

    static class BackSide {
        Layer layer1;
    }

    static class Layer {
        String layerType;
        String imageUrl;
        Boolean editable;
    }

    static class MaskLayer {
        String imageUrl;
    }

    /************************** 目标结构 **************************/
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class CustomProductTemplate {
        private String productId;
        ProductInfo productInfo;
        List<ProductSide> productSide;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class ProductSide {
        String title;
        CustomFrontSide frontSide;
        List<SideOption> sideOptions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class CustomFrontSide {
        List<LevelInfo> levelInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class LevelInfo {
        String id;
        String name; // 层级类型 bg-背景 border-相框 customMade-图片
        String type; // 层级类型 bg-背景 border-边框 customMade-定制
        int zIndex; // 层级
        boolean allowEditing;
        String image;
        String frameMask;
        private List<Frame> frameList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Frame {
        String id;
        String name;
        String image;
        String s_image;
        String frameMask;
        boolean defaultSelection;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class ProductInfo {
        String productName;
        String productId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class SideOption {
        int index;
        String type; // back
        String name; // "反面"
    }
}