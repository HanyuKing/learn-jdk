package other;

/**
 * @author wanghanyu
 * @create 2017-10-26 18:24
 */
public class LogTest {
    public static void main(String[] args) {
        doExTest();
        doExTest();
    }

    private static void doExTest() {
        long start = System.nanoTime();
        for (int i=0; i<100000; ++i) {
            try {
                throw new RuntimeException("" + Math.random());
            } catch (Exception e) {
            }
        }
        //System.out.println("time: " + (System.nanoTime() - start));
    }
}
