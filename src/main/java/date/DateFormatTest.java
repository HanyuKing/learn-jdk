package date;

import java.text.SimpleDateFormat;

/**
 * @author wanghanyu
 * @create 2017-12-14 20:30
 */
public class DateFormatTest {
    public static final String pattern1 = "yyyy-MM-dd hh:mm:ss";
    public static final String pattern2 = "yyyy-MM-dd hh:mm";
    public static final String pattern3 = "yyyy-MM-dd hh";
    public static final String pattern4 = "yyyy-MM-dd";
    public static final String pattern5 = "yyyy-MM";
    public static final String pattern6 = "yyyy";

    public static void main(String[] args) {
        String time = "2017-10-11 9:13";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern1);
            System.out.println("1->" + sdf.format(sdf.parse(time)));
            System.out.println("1->" + sdf.parse(time).getTime());
        } catch (Exception e) {}
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern2);
            SimpleDateFormat sdf2 = new SimpleDateFormat(pattern1);
            System.out.println("2->" + sdf.parse(time).getTime());
        } catch (Exception e) {}
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern3);
            SimpleDateFormat sdf2 = new SimpleDateFormat(pattern1);
            System.out.println("3->" + sdf2.format(sdf.parse(time)));
        } catch (Exception e) {}
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern4);
            SimpleDateFormat sdf2 = new SimpleDateFormat(pattern1);
            System.out.println("4->" + sdf.parse(time).getTime());
            System.out.println("4->" + sdf2.format(sdf.parse(time)));
        } catch (Exception e) {}
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern5);
            SimpleDateFormat sdf2 = new SimpleDateFormat(pattern1);
            System.out.println("5->" + sdf.parse(time).getTime());
            System.out.println("5->" + sdf2.format(sdf.parse(time)));
        } catch (Exception e) {}
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern6);
            SimpleDateFormat sdf2 = new SimpleDateFormat(pattern1);
            System.out.println("6->" + sdf.parse(time).getTime());
            System.out.println("6->" + sdf2.format(sdf.parse(time)));
        } catch (Exception e) {}
    }
}
