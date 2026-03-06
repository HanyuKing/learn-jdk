package excel.op;

import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 解析同目录下“品类交付时效.xlsx”：
 * key = 交付时效_live * 24
 * value = 三级类目ID（按时效分组、逗号拼接）
 */
public class CategoryDeliveryTimeParser {

    private static final String FILE_PATH = "/Users/rogerswang/my/source_code/learn-jdk/src/main/java/excel/op/品类交付时效.xlsx";
    private static final String TARGET_SHEET = "品类交付时效";
    private static final String CATEGORY_ID_HEADER = "三级类目ID";
    private static final String LIVE_TIME_HEADER = "交付时效_live";

    public static void main(String[] args) throws Exception {
        Map<String, String> result = parse(FILE_PATH, TARGET_SHEET);
        System.out.println(JSON.toJSONString(result, true));
        for (Map.Entry<String, String> entry : result.entrySet()) {
            String hours = entry.getKey();
            int count = countIds(entry.getValue());
            String days = toDayString(hours);
            System.out.println(days + " (" + hours + "小时): " + count);
        }
        System.out.println("类目总数: " + countCategories(result));
    }

    public static Map<String, String> parse(String filePath, String sheetName) throws Exception {
        DataFormatter formatter = new DataFormatter();
        TreeMap<Integer, List<String>> grouped = new TreeMap<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("未找到 Sheet: " + sheetName);
            }

            int headerRowIndex = findHeaderRow(sheet, formatter);
            Row headerRow = sheet.getRow(headerRowIndex);
            int categoryIdCol = findColumnIndex(headerRow, CATEGORY_ID_HEADER, formatter);
            int liveTimeCol = findColumnIndex(headerRow, LIVE_TIME_HEADER, formatter);

            if (categoryIdCol < 0 || liveTimeCol < 0) {
                throw new IllegalArgumentException("未找到所需列: " + CATEGORY_ID_HEADER + " / " + LIVE_TIME_HEADER);
            }

            for (int rowIndex = headerRowIndex + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }

                String categoryId = getCellText(row.getCell(categoryIdCol), formatter);
                String liveTime = getCellText(row.getCell(liveTimeCol), formatter);
                if (categoryId.isEmpty() || liveTime.isEmpty()) {
                    continue;
                }

                int categoryIdInt;
                int key;
                try {
                    categoryIdInt = new BigDecimal(categoryId).intValueExact();
                    key = new BigDecimal(liveTime)
                            .multiply(BigDecimal.valueOf(24))
                            .intValueExact();
                } catch (Exception e) {
                    continue;
                }

                grouped.computeIfAbsent(key, k -> new ArrayList<>())
                        .add(String.valueOf(categoryIdInt));
            }
        }

        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, List<String>> entry : grouped.entrySet()) {
            result.put(String.valueOf(entry.getKey()), String.join(",", entry.getValue()));
        }
        return result;
    }

    private static int findHeaderRow(Sheet sheet, DataFormatter formatter) {
        int maxScan = Math.min(sheet.getLastRowNum(), 10);
        for (int i = 0; i <= maxScan; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            int idCol = findColumnIndex(row, CATEGORY_ID_HEADER, formatter);
            int liveCol = findColumnIndex(row, LIVE_TIME_HEADER, formatter);
            if (idCol >= 0 && liveCol >= 0) {
                return i;
            }
        }
        throw new IllegalArgumentException("未找到包含目标表头的行");
    }

    private static int findColumnIndex(Row headerRow, String headerName, DataFormatter formatter) {
        for (Cell cell : headerRow) {
            String value = getCellText(cell, formatter);
            if (headerName.equals(value)) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }

    private static String getCellText(Cell cell, DataFormatter formatter) {
        if (cell == null) {
            return "";
        }
        return formatter.formatCellValue(cell).trim();
    }

    private static int countCategories(Map<String, String> groupedCategoryIds) {
        int total = 0;
        for (String ids : groupedCategoryIds.values()) {
            total += countIds(ids);
        }
        return total;
    }

    private static int countIds(String ids) {
        if (ids == null || ids.trim().isEmpty()) {
            return 0;
        }
        return ids.split(",").length;
    }

    private static String toDayString(String hours) {
        BigDecimal day = new BigDecimal(hours).divide(BigDecimal.valueOf(24));
        return day.stripTrailingZeros().toPlainString();
    }
}
