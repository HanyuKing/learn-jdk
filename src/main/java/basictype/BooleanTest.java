package basictype;

import org.junit.Test;

/**
 * @author Hanyu King
 * @since 2018-09-19 16:20
 */
public class BooleanTest {

    @Test
    public void testFloat() {
        float a = +0.0F;
        float b = -0.0F;
        System.out.println(a == b);

        System.out.println(b);
    }

    @Test
    public void testEquals() {


        boolean flag = true;
        if(flag) {
            System.out.println("Hello Java");
        }
        if(flag == true) {
            System.out.println("asdasasd");
        }
    }
}
