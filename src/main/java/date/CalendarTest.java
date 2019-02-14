package date;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Hanyu King
 * @since 2018-12-21 11:08
 */
public class CalendarTest {
    public static void main(String[] args) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String monthfirst = format.format(c.getTime());
        System.out.println("===============nowfirst:" + monthfirst);

//获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        String monthlast = format.format(ca.getTime());
        System.out.println("===============last:" + ca.getTime());

        ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        ca.add(Calendar.DAY_OF_MONTH, 1);
        System.out.println("===============curr:" + format.format(ca.getTime()));


        System.out.println(Boolean.FALSE.equals(null));

    }
}
