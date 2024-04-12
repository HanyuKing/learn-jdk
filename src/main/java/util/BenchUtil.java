package util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/11 16:58
 * @Description
 * @Version 1.0
 **/
public class BenchUtil {
    public static void concurrentRun(int threadNum, Runnable runnable) throws Exception {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNum);
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);

        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                try {
                    cyclicBarrier.await();
                    runnable.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();

    }
}
