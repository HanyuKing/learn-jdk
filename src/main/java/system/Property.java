package system;

/**
 * @author wanghanyu
 * @create 2017-12-28 15:31
 */
public class Property {
    public static void main(String[] args) {
        System.setProperty("aa", "a_a");
        System.setProperty("bb", "a_bbba");
        System.out.println(System.getProperty("aa"));
        System.out.println(System.getProperty("bb"));
    }
}
