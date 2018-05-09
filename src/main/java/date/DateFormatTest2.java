package date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wanghanyu
 * @create 2017-12-14 20:30
 */
public class DateFormatTest2 {
    public static final String pattern1 = "yyyy/MM/dd HH:mm:ss";
    public static final String pattern2 = "yyyy-MM-dd hh:mm";
    public static final String pattern3 = "yyyy-MM-dd hh";
    public static final String pattern4 = "yyyy-MM-dd";
    public static final String pattern5 = "yyyy-MM";
    public static final String pattern6 = "yyyy";

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern4);
        System.out.println("1->" + sdf.parse("2018-04-21"));
    }
}
