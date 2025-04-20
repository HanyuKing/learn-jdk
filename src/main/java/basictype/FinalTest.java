package basictype;

import org.junit.Test;

/**
 * @Author Hanyu.Wang
 * @Date 2025/3/5 11:02
 * @Description
 * @Version 1.0
 **/
public class FinalTest {
    @Test
    public void testFinallyReturn() {
        System.out.println(finallyReturn());
    }

    public int finallyReturn() {
        try {
            System.out.println("exec");
            return 0;
        } catch (Throwable e) {

//        } catch (Exception e1) {
//
//        } catch (NullPointerException e3) {

        } finally {
            return 1;
        }

    }
}
