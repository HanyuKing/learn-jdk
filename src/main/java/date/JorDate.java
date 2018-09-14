package date;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Hanyu King
 * @since 2018-06-27 16:22
 */
public class JorDate {
    public static void main(String[] args) {
        DateTime dateTime = DateTime.parse("2018-06-27");
        System.out.println(dateTime.plusDays(1).toString("yyyy-MM-dd"));
    }

    @Test
    public void testFormat000000() {
        System.out.println(DateTime.parse("2018-07-20 01:00:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
        System.out.println(new DateTime(1536138590000L).toDate());
    }

    @Test
    public void testDayCompare() {
        String date1 = "2018-07-20 01:00:00";
        String date2 = "2018-07-20 23:59:59";
        System.out.println(new DateTime(date1));
        //System.out.println(new Period(new DateTime(date1).getMillis(), new DateTime(date2).getMillis(), PeriodType.days()));
    }

    @Test
    public void testFormat() {
        DateTime begin = new DateTime("2018-06-27");
        DateTime end = new DateTime("2018-06-28");

        int diffDay = (int) ((end.toDate().getTime() - begin.toDate().getTime()) / (24 * 60 * 60 * 1000));

        System.out.println(diffDay);
    }

    /**
     * 日期比较
     */
    @Test
    public void testCompare() {
        DateTime begin = new DateTime("2018-06-27");
        DateTime end = new DateTime("2018-06-28");
        DateTime calDate;

        DateTime yesterday = DateTime.now().plusDays(-1);

        boolean isEndBeforeToday = yesterday.isAfter(end);
        if(isEndBeforeToday) {
            calDate = end;
        } else {
            calDate = yesterday;
        }

        System.out.println(calDate.toString("yyyy-MM-dd"));
    }

    /**
     * 计算区间天数
     */
    @Test
    public void testPeriodDay() {
        DateTime begin = new DateTime("2018-06-27");
        DateTime end = new DateTime("2018-06-28");
        Period p = new Period(begin, end, PeriodType.days());
        int days = p.getDays();

        System.out.println(days);
    }

    @Test
    public void testGetDateStrings() {
        String[] dates = getDateStrings("2018-07-22", "2018-07-22");
        for (int i = 0; i < dates.length; i++) {
            System.out.println(dates[i]);
        }
    }

    @Test
    public void testLong() {
        System.out.println(Long.valueOf("5.555"));
    }

    @Test
    public void testReverse() {
        String[] dates = new String[]{"2018-07-11", "2018-07-10", "2018-07-09"};
        String temp;
        for(int i = 0; i < dates.length >> 1; i++) {
            temp = dates[i];
            dates[i] = dates[dates.length - i - 1];
            dates[dates.length - i - 1] = temp;
        }

        for(int i = 0; i < dates.length; i++) {
            System.out.println(dates[i]);
        }
    }

    public static String[] getDateStrings(String startTime, String endTime) {
        DateTime begin = new DateTime();
        DateTime end = new DateTime();

        try {
            begin = DateTime.parse(startTime);
            end = DateTime.parse(endTime);
        } catch (Exception e) {
            //log.error("时间格式错误: startTime[{}]endTime[{}]", startTime, endTime, e);
           // throw ExceptionsUtil.Http400("时间格式错误");
        }

        DateTime yesterday = DateTime.now().plusDays(-1);
        end = yesterday.isAfter(end) ? end : yesterday;

        if(end.isBefore(begin)) {
            return new String[]{};
        }

        int diffDay = new Period(begin, end, PeriodType.days()).getDays() + 1;
        diffDay = diffDay > 90 ? 90 : diffDay;

        String[] dates = new String[diffDay];
        for(int i = 0; i < diffDay; i++) {
            dates[i] = end.plusDays(-i).toString("yyyy-MM-dd");
        }

        String temp;
        for(int i = 0; i < dates.length >> 1; i++) {
            temp = dates[i];
            dates[i] = dates[dates.length - i - 1];
            dates[dates.length - i - 1] = temp;
        }

        return dates;
    }
}
