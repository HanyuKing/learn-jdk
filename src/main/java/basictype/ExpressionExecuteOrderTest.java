package basictype;

import org.junit.Test;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/15 10:14
 * @Description
 * @Version 1.0
 **/
public class ExpressionExecuteOrderTest {

    @Test
    public void test1() {
        // (t != (t = tail))

        boolean t = func1();

        if (t != (t = func2())) {
            System.out.println(t);
        }
    }

    private boolean func1() {
        System.out.println("func1");
        return false;
    }

    private boolean func2() {
        System.out.println("func2");
        return true;
    }
}
