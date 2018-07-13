package date;

import org.joda.time.DateTime;

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
}
