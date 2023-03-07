package bytebuddy;

/**
 * @Author Hanyu.Wang
 * @Date 2023/3/6 19:21
 * @Description
 * @Version 1.0
 **/
public class Bar {
    public static void println(String x) {
        System.out.println("Bar: " + x);
    }

    @Override
    public String toString() {
        return null;
    }

    public String get(String key) {
        return "adas";
    }
}
