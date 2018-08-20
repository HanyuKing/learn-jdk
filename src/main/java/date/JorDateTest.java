package date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Hanyu King
 * @since 2018-07-12 18:32
 */
public class JorDateTest {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for(int i = 0; i < 10000; i++) {
            Calendar calendar = Calendar.getInstance();
            new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        }
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        for(int i = 0; i < 10000; i++) {
            new DateTime().toString("yyyy-MM-dd");
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testZere() {
        String dateStr = DateTime.now().plusDays(1).toString("yyyy-MM-dd ") + "1:00:00";
        System.out.println(DateTime.parse(dateStr, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().getTime() - System.currentTimeMillis());
    }
}
