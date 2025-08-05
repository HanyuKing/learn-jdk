package easyexcel;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.List;

/**
 * @author hanyu.wang
 * @version 1.0
 * @date 2025/8/5
 */
public class ExcelTest {
    @Test
    public void testRead() {
        String filePath = "/Users/rogerswang/my/source_code/learn-jdk/src/main/java/easyexcel/t221754378172059.xlsx";
        List<DemoData> users = ExcelReader.readFile(filePath, DemoData.class);
        System.out.println(JSON.toJSONString(users));
    }

    @Test
    public void testReadFromUrl() {
        String fileUrl = "https://mm-jjewelry-test-bj.tos-cn-beijing.volces.com/jjewelry/web/resources/trade/product/20250805/79c6d8703c3f4a4fbfa6a4cee57f7c61/resource/33c8266117d64f47855075e03e2691a1/c8c366f80f6e6836fcc49ed4cd53f6f5/original/c8c366f80f6e6836fcc49ed4cd53f6f5.xlsx";
        List<DemoData> users = ExcelReader.readUrl(fileUrl, DemoData.class);
        System.out.println(JSON.toJSONString(users));
    }
}
