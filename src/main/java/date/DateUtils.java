package date;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.time.LocalDateTime;

/**
 * @Author Hanyu.Wang
 * @Date 2023/10/23 16:44
 * @Description
 * @Version 1.0
 **/
public class DateUtils {

    /**
     * 计算两个时间段的交集
     *
     * @param time1 时间段1
     * @param time2 时间段2
     * @return 交集
     */
    public static ImmutablePair<LocalDateTime, LocalDateTime> intersection(ImmutablePair<LocalDateTime, LocalDateTime> time1,
                                                                           ImmutablePair<LocalDateTime, LocalDateTime> time2) {

        LocalDateTime s1 = time1.getLeft();
        LocalDateTime e1 = time1.getRight();
        LocalDateTime s2 = time2.getLeft();
        LocalDateTime e2 = time2.getRight();

        if (s1.isAfter(e1) || s2.isAfter(e2)) {
            // 时间有误，开始时间不能大于结束时间
            return null;
        }

        // 检查两个时间段是否重叠
        if (e1.isBefore(s2) || e2.isBefore(s1)) {
            // 时间段不重叠
            return null;
        }

        ImmutablePair<LocalDateTime, LocalDateTime> result = null;

        if (e1.isBefore(e2)) {
            if (s1.isBefore(s2)) {
                result = new ImmutablePair<>(s2, e1);
            } else {
                result = new ImmutablePair<>(s1, e1);
            }
        } else {
            if (s2.isBefore(s1)) {
                result = new ImmutablePair<>(s1, e2);
            } else {
                result = new ImmutablePair<>(s2, e2);
            }
        }
        return result;
    }
}
