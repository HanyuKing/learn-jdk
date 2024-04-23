package thread.threadpool;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/22 16:05
 * @Description
 * @Version 1.0
 **/
public class ShutdownThreadPoolTest {
    @Test
    public void testShutdown() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        executorService.shutdown();
    }
    @Test
    public void testShutdownNow() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        executorService.shutdownNow();
    }
}
