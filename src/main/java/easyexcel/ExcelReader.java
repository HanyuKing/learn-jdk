package easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Excel读取工具类
 */
@Slf4j
public class ExcelReader {

    //------------------------ 基本读取方法 ------------------------

    /**
     * 读取本地Excel文件
     *
     * @param filePath 文件路径
     * @param clazz    数据模型类
     * @return 数据列表
     */
    public static <T> List<T> readFile(String filePath, Class<T> clazz) {
        List<T> dataList = new ArrayList<>();
        EasyExcel.read(filePath, clazz, new SimpleListener<>(dataList)).sheet().doRead();
        return dataList;
    }

    /**
     * 读取上传的Excel文件（Spring MultipartFile）
     *
     * @param file  上传的文件
     * @param clazz 数据模型类
     * @return 数据列表
     */
    public static <T> List<T> readMultipartFile(MultipartFile file, Class<T> clazz) {
        try (InputStream inputStream = file.getInputStream()) {
            List<T> dataList = new ArrayList<>();
            EasyExcel.read(inputStream, clazz, new SimpleListener<>(dataList)).sheet().doRead();
            return dataList;
        } catch (IOException e) {
            throw new RuntimeException("读取上传文件失败", e);
        }
    }

    /**
     * 读取网络Excel文件
     *
     * @param fileUrl 文件URL
     * @param clazz   数据模型类
     * @return 数据列表
     */
    public static <T> List<T> readUrl(String fileUrl, Class<T> clazz) {
        try (InputStream inputStream = new URL(fileUrl).openStream()) {
            List<T> dataList = new ArrayList<>();
            EasyExcel.read(inputStream, clazz, new SimpleListener<>(dataList)).sheet().doRead();
            return dataList;
        } catch (IOException e) {
            throw new RuntimeException("读取网络文件失败", e);
        }
    }

    //------------------------ 批量读取方法 ------------------------

    /**
     * 批量读取本地文件
     *
     * @param filePaths 文件路径列表
     * @param clazz     数据模型类
     * @return Map<文件名, 数据列表>
     */
    public static <T> Map<String, List<T>> readFiles(List<String> filePaths, Class<T> clazz) {
        return filePaths.stream()
                .collect(Collectors.toMap(
                        filePath -> filePath,
                        filePath -> readFile(filePath, clazz)
                ));
    }


    //------------------------ 内部监听器 ------------------------

    private static class SimpleListener<T> implements ReadListener<T> {
        private final List<T> dataList;

        SimpleListener(List<T> dataList) {
            this.dataList = dataList;
        }

        @Override
        public void invoke(T data, AnalysisContext context) {
            Set<ConstraintViolation<T>> violations = validator.validate(data);
            if (!violations.isEmpty()) {
                String errors = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("; \n"));
                throw new RuntimeException(errors);
            }
            dataList.add(data);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 可在此处添加读取完成后的简单处理
        }

        @Override
        public void onException(Exception exception, AnalysisContext context) throws Exception {
            log.error("第{}行，解析异常", (context.readRowHolder().getRowIndex() + 1), exception);
            throw new RuntimeException("第" + (context.readRowHolder().getRowIndex() + 1) + "行数据解析错误: " + exception.getMessage());
        }

        private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}