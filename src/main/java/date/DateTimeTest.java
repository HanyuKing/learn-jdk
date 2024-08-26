package date;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author wanghanyu
 * @create 2018-01-16 11:55
 */
public class DateTimeTest {
    public static void main(String[] args) {
        Date date = new Date();
        JSONObject obj = new JSONObject();
        obj.put("date", "");
        System.out.println(obj.toString());
    }

    @Test
    public void testJdkLocalDateTime() {

        System.out.println(DateUtils.parse("2024-08-19 12:13:14", "yyyy-MM-dd HH:mm:ss").getTime());
        System.out.println(DateTimeUtils.parse("2024-08-19 12:13:14", "yyyy-MM-dd HH:mm:ss"));
    }
}
