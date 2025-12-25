package excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

import java.util.ArrayList;
import java.util.List;

public class ExcelToSingleSQL {

    public static void main(String[] args) {
        String excelPath = "/Users/rogerswang/my/source_code/learn-jdk/src/main/java/excel/want_it_3.xlsx";
        String tableName = "trade_pd_product_auto_create_task";

        // 生成一条INSERT语句
        String insertSQL = generateSingleInsertSQL(excelPath, tableName);
        System.out.println("生成的INSERT语句：");
        System.out.println(insertSQL);
    }

    /**
     * 生成一条包含所有数据的INSERT语句
     */
    public static String generateSingleInsertSQL(String excelPath, String tableName) {
        // 存储所有Excel数据
        List<ExcelData> allData = new ArrayList<>();

        // 读取Excel文件
        EasyExcel.read(excelPath, ExcelData.class, new ReadListener<ExcelData>() {
            @Override
            public void invoke(ExcelData data, AnalysisContext context) {
                allData.add(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                // 所有数据处理完成后会自动调用
            }
        }).sheet().doRead();

        // 生成单个INSERT语句
        return buildSingleInsertSQL(allData, tableName);
    }

    /**
     * 构建一条INSERT语句插入所有数据
     */
    private static String buildSingleInsertSQL(List<ExcelData> dataList, String tableName) {
        if (dataList.isEmpty()) {
            return "-- 没有数据";
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO ").append(tableName).append(" (`want_id`, `content_id`, `topic_id`, `status`, `version`, `ext`, `closed`, `create_by`, `update_by`, `create_time`, `modified_time`) VALUES\n");

        for (int i = 0; i < dataList.size(); i++) {
            ExcelData data = dataList.get(i);

            // 添加VALUES部分
            sqlBuilder.append("  ('")
                    .append(escapeSql(data.getWantId()))
                    .append("', '")
                    .append(escapeSql(data.getContentId()))
                    .append("', '")
                    .append(escapeSql(data.getTopicId()))
                    .append("', ")
                    .append("'0', ")
                    .append("'1', ")
                    .append("'', ")
                    .append("'0', ")
                    .append("'why', ")
                    .append("'why', ")
                    .append("'2025-12-16 03:34:28', ")
                    .append("'2025-12-16 03:34:28'")
                    .append(")");

            // 如果不是最后一行，添加逗号
            if (i < dataList.size() - 1) {
                sqlBuilder.append(",\n");
            } else {
                sqlBuilder.append(";");
            }
        }

        return sqlBuilder.toString();
    }

    /**
     * 转义SQL中的特殊字符
     */
    private static String escapeSql(String value) {
        if (value == null) {
            return "";
        }
        // 转义单引号
        return value.replace("'", "''");
    }

    /**
     * Excel数据对应的Java类
     */
    public static class ExcelData {
        // 对应Excel的列，可以根据Excel表头名称映射
        private String wantId;
        private String contentId;
        private String topicId;

        // 必须有无参构造函数
        public ExcelData() {
        }

        // getters 和 setters
        public String getWantId() {
            return wantId;
        }

        public void setWantId(String wantId) {
            this.wantId = wantId;
        }

        public String getContentId() {
            return contentId;
        }

        public void setContentId(String contentId) {
            this.contentId = contentId;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }
    }
}