package other;

import org.junit.Test;

/**
 * refer  https://tech.meituan.com/in_depth_understanding_string_intern.html?utm_source=tool.lu
 *
 * String.intern method test, different with jdk6 and jdk7
 *
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

    @Test
    public void testStringIntern2() {
        String s = new String("1");
        s.intern();
        String s2 = "1";
        System.out.println(s == s2);

        String s3 = new String("1") + new String("1");
        s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4);
    }

    @Test
    public void testStringIntern() {
        String s = new String("1");
        String s2 = "1";
        s.intern();
        System.out.println(s == s2);

        String s3 = new String("1") + new String("1");
        String s4 = "11";
        s3.intern();
        System.out.println(s3 == s4);
    }

    @Test
    public void testTimeConsuming() {
        String[] keys = new String[50000];
        for(int i = 0; i < keys.length; i++) {
            keys[i] = i + "";
        }

        long start = System.currentTimeMillis();
        for(int i = 0; i < 10000000; i++) {
            String s = keys[i % 50000];
        }
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        for(int i = 0; i < 10000000; i++) {
            String s = keys[i % 50000];
            synchronized (s) {
                String str = s;
            }
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
