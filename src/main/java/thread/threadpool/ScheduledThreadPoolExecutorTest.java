package thread.threadpool;

import org.junit.Test;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/22 17:25
 * @Description
 * @Version 1.0
 **/
public class ScheduledThreadPoolExecutorTest {

    @Test
    public void testSchedule() throws InterruptedException {
        ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("test-scheduled-pool-Hello");
                return t;
            }
        });

        // 无界队列，MaximumPoolSize 没有意义

        // scheduledPool.setMaximumPoolSize();

        scheduledPool.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("scheduleWithFixedDelay...");
            }
        }, 1, TimeUnit.SECONDS);

        Semaphore semaphore = new Semaphore(0);
        semaphore.acquire();
    }

    @Test
    public void testScheduleWithFixedDelay() throws InterruptedException {
        ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("test-scheduled-pool-Hello");
                return t;
            }
        });

        scheduledPool.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println("scheduleWithFixedDelay...");
            }
        }, 1, 1, TimeUnit.SECONDS);

        Semaphore semaphore = new Semaphore(0);
        semaphore.acquire();
    }

    @Test
    public void testScheduleAtFixedRate() throws InterruptedException {
        ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("test-scheduled-pool-Hello");
                return t;
            }
        });

        scheduledPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("scheduleAtFixedRate...");
            }
        }, 1, 1, TimeUnit.SECONDS);

        Semaphore semaphore = new Semaphore(0);
        semaphore.acquire();
    }
}
