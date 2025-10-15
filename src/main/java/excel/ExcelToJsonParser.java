package excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.util.*;

public class ExcelToJsonParser {

    public static void main(String[] args) {
        try {
            String filePath = "/Users/hanyuking/my/source_code/learn-jdk/src/main/java/excel/顾客定制技术侧所需.xlsx";
            List<ProductTemplate> productList = parseExcel(filePath);

            // 输出JSON
            String json = toJsonString(productList);
            System.out.println(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<ProductTemplate> parseExcel(String filePath) throws Exception {
        List<ProductTemplate> productList = new ArrayList<>();

        FileInputStream file = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(12); // Sheet1是第13个sheet，索引12

        String currentProductName = null;
        ProductTemplate currentProduct = null;

        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // 获取商品名称
            Cell productNameCell = row.getCell(0);
            if (productNameCell != null && !productNameCell.toString().trim().isEmpty()) {
                currentProductName = productNameCell.toString().trim();
                currentProduct = new ProductTemplate();
                currentProduct.productName = currentProductName;
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
        template.back = new BackSide();
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
        BackSide back;
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
}