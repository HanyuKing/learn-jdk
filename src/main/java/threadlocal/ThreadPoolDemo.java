package threadlocal;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDemo {
    private static final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(30 * 1000);
        for (int i = 0; i < 100; ++i) {
            System.out.println(i);
            poolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    ThreadLocal<BigObject> threadLocal = new ThreadLocal<>(); // move up
                    threadLocal.set(new BigObject());

                    // 其他业务代码
                    threadLocal.remove(); // comment
                }
            });
            Thread.sleep(1000);
        }
        Semaphore semaphore = new Semaphore(0);
        semaphore.acquire();
    }
    static class BigObject {
        // 10M
        private byte[] bytes = new byte[100 * 1024 * 1024];
    }
}