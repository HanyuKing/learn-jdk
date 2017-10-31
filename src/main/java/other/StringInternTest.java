package other;

/**
 * @author wanghanyu
 * @create 2017-10-28 16:33
 */
public class StringInternTest {
    public static void main(String[] args) {
        String str1 = new StringBuilder("ja").append("xy").toString();
        System.out.println(str1.intern() == str1);

        String a = new String("ja") + "va";
        System.out.println(a.intern() == a);
    }
}
