package date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author Hanyu.Wang
 * @Date 2024/8/19 14:51
 * @Description
 * @Version 1.0
 **/
public class DateTimeUtils {
    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
}
