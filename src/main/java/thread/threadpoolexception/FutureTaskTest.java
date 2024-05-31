package thread.threadpoolexception;

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

    @Test
    public void testMultiConsumerOneFutureTask() throws Exception {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            Thread.sleep(2000);
            return "hello";
        });

        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(futureTask);

        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                try {
                    String value = futureTask.get();
                    System.out.println(System.currentTimeMillis() + " -> " + value);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
            threadList.add(t);
            t.start();
        }

        for (Thread t : threadList) {
            t.join();
        }
    }

    @Test
    public void printException2() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();

        pool.execute(() -> {
            throw new RuntimeException("execute异常能打印出来");
        });

        pool.awaitTermination(1000, TimeUnit.MICROSECONDS);
        pool.shutdown();
    }

    @Test
    public void printException() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();

        pool.submit((Runnable) () -> {
            throw new RuntimeException("submit异常打印不出来");
        });

        pool.awaitTermination(1000, TimeUnit.MICROSECONDS);
        pool.shutdown();
    }

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
                    //return iTemp;
                    throw new RuntimeException("pool.submit futureTask 异常打印不出来");
                }
            };

            FutureTask<Integer> futureTask = new FutureTask<Integer>(callable);
            futures.add(futureTask);
            pool.execute(futureTask);
        }

        for (int i = 0; i < 1; i++) {
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
