package other;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * @author Hanyu King
 * @since 2018-04-24 18:36
 */
public class BoundedThreadPool extends ThreadPoolExecutor {
    public static ExecutorService pool;
    public static Semaphore semaphore;

    private static class MyThreadFactory implements ThreadFactory {

        private String poolName;

        public MyThreadFactory(String poolName) {
            this.poolName = poolName;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName(this.poolName);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }


    public BoundedThreadPool(int corePoolSize, int maxPoolSize, int blockQueueSize, String poolName) {
        super(5, 100, 1, TimeUnit.MINUTES, new SynchronousQueue<Runnable>(), new BasicThreadFactory.Builder().namingPattern(poolName + "-%d").build(), new CallerRunsPolicy());

        semaphore = new Semaphore(blockQueueSize + maxPoolSize);
    }

    public <T> Future<T> submit(Callable<T> task) {
        try {
            while(!semaphore.tryAcquire(10, TimeUnit.SECONDS)) {}
        } catch (InterruptedException e) {}

        return (Future<T>) pool.submit(new FutureTask(task) {
            @Override
            public void run() {
                super.run();
                semaphore.release();
            }
        });
    }

}

