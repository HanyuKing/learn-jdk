package thread.threadpool;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Hanyu King
 * @since 2018-07-09 17:22
 */
public class ThreadPoolTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(Runtime.getRuntime().availableProcessors());
        ThreadPoolExecutor addDbFileToESExcutor = new ThreadPoolExecutor(
                5,
                5,
                1, TimeUnit.HOURS,
                new LinkedBlockingQueue<Runnable>(200),
                new BasicThreadFactory.Builder().namingPattern("AddDbFileToESExcutor-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        List<Future> futures = new ArrayList<Future>();

        for(int i = 0; i < 10; i++) {
            final int j = i;
            Future future = addDbFileToESExcutor.submit(new Callable() {
                public Object call() throws Exception {
                    doAddDBFieldToES(j);
                    return null;
                }
            });
            futures.add(future);
        }

        for(int i = 0; i < futures.size(); i++) {
            futures.get(i).get();
        }

        addDbFileToESExcutor.shutdown();
    }

    private static void doAddDBFieldToES(int i) throws InterruptedException {
        System.out.println("start " + i);
        Thread.sleep(2000);
        System.out.println(i);
    }
}
