package BasicType;

import org.junit.Test;

/**
 * @author Hanyu King
 * @since 2018-05-29 10:59
 */
public class IntegerTest {
    public static void main(String[] args) {
        Integer i = 14;
        Integer b = 199;
        Integer c = 200;

        System.out.println(b >= c);
        System.out.println(i >= 5);
    }

    @Test
    public void testByteLength() {
        char c = 'c';
        System.out.println(c);
    }

    @Test
    public void testAnd() {
        for(int i = 0; i < 10; i++) {
            System.out.println(i & 1);
        }
    }

    @Test
    public void testObjectSize() {
        Sku sku = new Sku();
        System.out.println(sku);
    }

    @Test
    public void testStrLen() {
        System.out.println("Hanyu King".length());
        System.out.println("王".length());
        char a = '王';
        System.out.println(a);
    }
}

class Sku {

}
