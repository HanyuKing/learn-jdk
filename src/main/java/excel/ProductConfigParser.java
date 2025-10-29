package excel;

import excel.dto.ProductConfig;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductConfigParser {

    public static List<ProductConfig> parseProductSheet(String filePath, String sheetName) {
        List<ProductConfig> productList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("未找到名为" + sheetName + "的Sheet");
            }

            // 跳过表头，从第2行开始读取（索引从0开始）
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                ProductConfig product = new ProductConfig();

                // 商品（SPU）
                product.setSpuName(getCellStringValue(row.getCell(0)));

                // 商品ID
                product.setProductId(getCellStringValue(row.getCell(1)));

                // 列表页商品tag
                product.setTag(getCellStringValue(row.getCell(2)));

                // 分辨率
                product.setResolution(getCellStringValue(row.getCell(3)));

                // 扩边距
                product.setExpandMargin(getCellStringValue(row.getCell(4)));

                // 是否打孔
                product.setPunch(getCellStringValue(row.getCell(5)));

                // 出血线
                product.setBleedLine(getCellStringValue(row.getCell(6)));

                // 是否有刀线
                product.setHasCutLine(getCellStringValue(row.getCell(7)));

                // 宽高比例
                product.setAspectRatio(getCellStringValue(row.getCell(8)));

                // 文件类型
                product.setFileType(getCellStringValue(row.getCell(9)));

                // 背景
                product.setHasBorder(getCellStringValue(row.getCell(10)));

                productList.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return productList;
    }

    private static String getCellStringValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        String filePath = "/Users/rogerswang/my/source_code/learn-jdk/src/main/java/excel/顾客定制-服务端配置所需-正式.xlsx";
        List<ProductConfig> products = parseProductSheet(filePath, "商品相关");

        // 打印结果
        products.forEach(System.out::println);
    }
}