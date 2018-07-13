package date;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Test;

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
        String[] dates = getDateStrings("2018-07-03", "2018-07-03");
        for (int i = 0; i < dates.length; i++) {
            System.out.println(dates[i]);
        }
    }

    public String[] getDateStrings(String startTime, String endTime) {
        DateTime begin = DateTime.now();
        DateTime end = DateTime.now();

        try {
            begin = DateTime.parse(startTime);
            end = DateTime.parse(endTime);
        } catch (Exception e) {
        }

        DateTime yesterday = DateTime.now().plusDays(-1);
        end = yesterday.isAfter(end) ? end : yesterday;

        int diffDay = new Period(begin, end, PeriodType.days()).getDays() + 1;
        diffDay = diffDay > 4 ? 4 : diffDay;

        String[] dates = new String[diffDay];
        for(int i = 0; i < diffDay; i++) {
            dates[i] = end.plusDays(-i).toString("yyyy-MM-dd");
        }
        return dates;
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
}
