package date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

        Date date = sdf.parse("2018-06-27");
        Date date2 = sdf.parse("2018-07-01");

        System.out.println("diff day: " + (date2.getTime() - date.getTime()) / (24 * 60 * 60 * 1000));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 4);
        System.out.println(sdf.format(calendar.getTime()));
    }
}
