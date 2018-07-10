package thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Hanyu King
 * @since 2018-07-06 12:25
 */
public class ThreadPoolExecuteException {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("开始打印异常");
                throw new RuntimeException("pool.execute 异常打印出来");
            }
        });
        System.out.println("shutdown...");
        pool.shutdown();
    }
}
