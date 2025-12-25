package thread.threadpoolexception;

import java.util.concurrent.*;

/**
 * 线程死锁解决方案
 *
 * 线程池不使用阻塞队列，使用同步队列（这样可能会造成任务串行执行，达不到并发的效果）
 * 父任务和子任务使用不同的线程池（采用的是此种解决方案）
 * 控制父任务并发数低于核心线程数
 */
public class ThreadPoolDeadLock {
    public static void main(String[] args) {
        // 创建单线程的线程池
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                10,
                10,
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(100),
                r -> {
                    Thread t = new Thread(r);
                    t.setName("Custom-Thread-Pool");
                    return t;
                },
                new ThreadPoolExecutor.AbortPolicy()
        );

        for (int i = 0; i < 5; i++) {
            pool.submit(() -> {
                try {
                    // 输出日志信息，表示第一个任务开始执行
                    System.out.println("First");
                    // 向线程池提交第二个任务，并等待第二个任务执行完成
                    pool.submit(() -> System.out.println("Second")).get();

                    // 输出日志信息，表示第一个任务后续操作继续执行
                    System.out.println("Third");
                } catch (Exception e) {
                    // 若出现异常，记录错误日志
                    e.printStackTrace();
                }
            });
            System.out.println(i + "->" + i);
        }
        System.out.println("end");
    }
}
