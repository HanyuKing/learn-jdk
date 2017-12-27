package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author wanghanyu
 * @create 2017-12-24 16:42
 */
public class FutureTaskTest {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        List<Future> futures = new ArrayList<Future>();

        for (int i = 0; i < 10; i++) {
            final int iTemp = i;
            Callable<Integer> callable = new Callable<Integer>() {
                public Integer call() throws Exception {
                    if(iTemp >= 5) {
                        Thread.sleep(6 * 1000);
                    }
                    return iTemp;
                }
            };

            futures.add(pool.submit(callable));
        }

        for (int i = 0; i < 10; i++) {
            try {
                Integer integer = (Integer) futures.get(i).get(5, TimeUnit.SECONDS);
                System.out.println(integer);
            } catch (Exception e) {}
        }

        pool.shutdown();
    }
}
