package date;

import org.junit.Test;

/**
 * @Author Hanyu.Wang
 * @Date 2024/7/3 19:51
 * @Description
 * @Version 1.0
 **/
public class Instant {
    @Test
    public void testGetEpochSecond() {
        long HOUR = 60 * 60;
        long checkHours = 60 * 24;
        long now = java.time.Instant.now().getEpochSecond();
        for (long i = checkHours; i >= 0; i--) {
            long endTime = now - i * HOUR;
            long startTime = endTime - HOUR;

            System.out.println(startTime + " " + endTime);
        }
    }
}
