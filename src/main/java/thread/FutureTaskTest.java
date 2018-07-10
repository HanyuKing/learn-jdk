package thread;

import com.google.caliper.model.Run;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * callable future futuretask
 *
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
//                    if(iTemp >= 5) {
//                        Thread.sleep(6 * 1000);
//                    }
                    System.out.println("temp: " + iTemp);
                    return iTemp;
                    //throw new RuntimeException("pool.submit futureTask 异常打印不出来");
                }
            };

            FutureTask<Integer> futureTask = new FutureTask<Integer>(callable);
            futures.add(futureTask);
            pool.submit(futureTask);
        }

        for (int i = 0; i < 10; i++) {
            try {
                Integer integer = (Integer) futures.get(i).get();
                System.out.println(integer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pool.shutdown();
    }
}
