package other;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Hanyu King
 * @since 2018-04-24 17:02
 */
public class TimeOut {

    public static ExecutorService threadPoolExecutor = new ThreadPoolExecutor(2, 2, 1, TimeUnit.MINUTES, new SynchronousQueue<Runnable>());
    public static final Semaphore semaphore = new Semaphore(2000);

    public static void main(String[] args) throws Exception {
        final int total = 0;
        final long start = System.currentTimeMillis();
        List<FutureTask<String>> futureTaskList = new ArrayList<FutureTask<String>>();
        for(int i = 0; i < 4; i++) {
            FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
                public String call() throws Exception {
                    return Thread.currentThread().getName() + "-" + Thread.currentThread().getId();
                }
            });

            System.out.println("end: " + i);
            threadPoolExecutor.submit(futureTask);
            System.out.println("end: " + i);
            futureTaskList.add(futureTask);
        }

        System.out.println("end");

        for(int i = 0; i < futureTaskList.size(); i++) {
            System.out.println(futureTaskList.get(i).get());
        }

        threadPoolExecutor.shutdown();
    }

    @Test
    public void test() {
        System.out.println("200".equals(null));
    }
}
