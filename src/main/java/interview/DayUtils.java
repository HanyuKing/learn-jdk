package interview;

public class DayUtils {
    // 每个月的天数（非闰年）
    private static final int[] DAYS_IN_MONTH_NORMAL =   {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    // 每个月的天数（闰年）
    private static final int[] DAYS_IN_MONTH_LEAP =     {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    // 到当月的累积天数（非闰年）
    private static final int[] CUMULATIVE_DAYS_NORMAL = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};

    // 到当月的累积天数（闰年）
    private static final int[] CUMULATIVE_DAYS_LEAP =   {0, 31, 60 ,91, 121 ,152, 182, 213, 244, 274, 305, 335};

    public static int dayOfYear(int year, int month, int day) {
        // 初步校验年月日是否合法
        if (year < 1
                || month < 1 || month > 12
                || day < 1 || day > 31) {
            return -1;
        }

        // 判断是否为闰年
        boolean isLeap = isLeapYear(year);

        // 根据是否闰年选取月份数组
        int[] daysInMonth = isLeap ? DAYS_IN_MONTH_LEAP : DAYS_IN_MONTH_NORMAL;
        // 验证day是否有效
        if (day > daysInMonth[month - 1]) {
            return -1;
        }

        // 根据是否闰年选取累计天数数组
        int[] cumulativeDays = isLeap ? CUMULATIVE_DAYS_LEAP : CUMULATIVE_DAYS_NORMAL;

        return cumulativeDays[month - 1] + day;
    }

    // 判断是否是闰年
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static void main(String[] args) {
        System.out.println(dayOfYear(2016, 1, 3));  // 3
        System.out.println(dayOfYear(2016, 2, 1));  // 32
        System.out.println(dayOfYear(2025, 2, 29)); // -1
        System.out.println(dayOfYear(2025, 4, 32)); // -1
        System.out.println(dayOfYear(-1, 4, 32)); // -1
    }
}
