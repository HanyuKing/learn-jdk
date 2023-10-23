package date;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/23 16:43
 * @Description
 * @Version 1.0
 **/
public class DateIntersectionTest {

    @Test
    public void test() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ImmutablePair<LocalDateTime, LocalDateTime> time1 = ImmutablePair.of(
                LocalDateTime.parse("2023-09-07 00:00:00", formatter),
                LocalDateTime.parse("2023-09-10 08:00:00", formatter));

        ImmutablePair<LocalDateTime, LocalDateTime> time2 = ImmutablePair.of(
                LocalDateTime.parse("2023-09-09 00:00:00", formatter),
                LocalDateTime.parse("2024-09-09 08:00:00", formatter));

        System.out.println(DateUtils.intersection(time1, time2));
    }
}
