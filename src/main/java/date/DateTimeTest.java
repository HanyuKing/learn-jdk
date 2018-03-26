package date;

import com.alibaba.fastjson.JSONObject;

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
}
