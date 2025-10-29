package excel;

import excel.dto.FrontBackCategoryMapping;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FrontBackCategoryParser {

    public static List<FrontBackCategoryMapping> parseCategoryMappingSheet(String filePath) {
        List<FrontBackCategoryMapping> mappingList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("前后台类目对应关系");
            if (sheet == null) {
                throw new IllegalArgumentException("未找到名为'前后台类目对应关系'的Sheet");
            }

            String currentFrontendCategory = "";
            String currentFrontendCategoryId = "";

            // 从第2行开始读取（索引从0开始，第1行是表头）
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                FrontBackCategoryMapping mapping = new FrontBackCategoryMapping();

                // 前台类目（A列）- 如果为空则使用上一行的值
                String frontendCategory = getCellStringValue(row.getCell(0));
                if (!frontendCategory.isEmpty()) {
                    currentFrontendCategory = frontendCategory;
                }
                mapping.setFrontendCategory(currentFrontendCategory);

                // 前台类目ID（B列）- 如果为空则使用上一行的值
                String frontendCategoryId = getCellStringValue(row.getCell(1));
                if (!frontendCategoryId.isEmpty()) {
                    currentFrontendCategoryId = frontendCategoryId;
                }
                mapping.setFrontendCategoryId(currentFrontendCategoryId);

                // 后台类目名称（C列）
                mapping.setBackendCategoryName(getCellStringValue(row.getCell(2)));

                // 后台类目code（D列）
                mapping.setBackendCategoryCode(getCellStringValue(row.getCell(3)));

                // 只有当后台类目名称和code都不为空时才添加到列表
                if (!mapping.getBackendCategoryName().isEmpty() && 
                    !mapping.getBackendCategoryCode().isEmpty()) {
                    mappingList.add(mapping);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mappingList;
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
                    // 处理数字类型的ID和code，去掉小数部分
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (int) numericValue) {
                        return String.valueOf((int) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }

    public static void main(String[] args) throws Exception {
        String fileName = "顾客定制-服务端配置所需-qa.xlsx";
        String filePath = "/Users/rogerswang/my/source_code/learn-jdk/src/main/java/excel/";
        List<FrontBackCategoryMapping> mappings = parseCategoryMappingSheet(filePath + fileName);

        String sql = generateSQL(mappings);
        FileWriter fileWriter = new java.io.FileWriter(filePath + "output/" + fileName + "-前后台类目.sql");
        fileWriter.write(sql);
        fileWriter.close();
    }

    private static String generateSQL(List<FrontBackCategoryMapping> mappings) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO `trade_pd_category_relation` (`id`, `front_category_id`, `category_id`, `version`, `closed`, `create_by`, `update_by`, `create_time`, `modified_time`) VALUES\n");
        
        for (int i = 0; i < mappings.size(); i++) {
            FrontBackCategoryMapping mapping = mappings.get(i);
            sql.append(String.format("(%d, '%s', '%s', 1, 0, 'why', 'why', '2025-09-29 17:00:40', '2025-09-29 17:00:43')", 
                    i + 1, 
                    mapping.getFrontendCategoryId(), 
                    Long.parseLong(mapping.getBackendCategoryCode()) - 10000));
            
            if (i < mappings.size() - 1) {
                sql.append(",\n");
            }
        }
        
        sql.append(";");
        return sql.toString();
    }
}