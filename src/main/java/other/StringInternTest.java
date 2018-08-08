package other;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        s.intern();
        String s2 = "1";
        System.out.println(s == s2);

        String s3 = new String("1") + new String("1");
        s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4);
    }

    @Test
    public void testTimeConsuming() {
        int size = 10000000;
        List<String> lst = new ArrayList<String>();

        long start = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < size; ++i ) {
            String str = String.valueOf(i);
            lst.add(str);

            if ( i % 10000 == 0 ) {
                if(count >= 300) {
                    break;
                }
                count++;
                long end = System.currentTimeMillis();
                System.out.println(( end - start ) / 1000.0);
                start = System.currentTimeMillis();
            }
        }
    }

    @Test
    public void test2() {
        String s1 = new StringBuilder().append("String").append("Test").toString();
        System.out.println(s1.intern() == s1);
        String s2 = new StringBuilder().append("ja").append("va").toString();
        System.out.println(s2.intern() == s2);
    }
}
