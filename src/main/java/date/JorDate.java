package date;

import org.joda.time.DateTime;
import org.junit.Test;

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
}
