package juc;

import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class SingletonReorderTest {
    /**
     * 没复现出来 ):
     *
     * @throws InterruptedException
     */
    @Test
    public void testReorder() throws InterruptedException {
        int parties = 200;

        CyclicBarrier cyclicBarrier = new CyclicBarrier(parties);
        CountDownLatch countDownLatch = new CountDownLatch(parties);

        for (int i = 0; i < parties; i++) {
            new Thread(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

                SingletonNoVolatile singleton = SingletonNoVolatile.getInstance();

                System.out.printf("time: %d, thread: %d, singleton.num: %d \n", System.currentTimeMillis(), Thread.currentThread().getId(), singleton.getNum());

                countDownLatch.countDown();
            }).start();
        }


        countDownLatch.await();
    }
}

class SingletonNoVolatile {
    private static SingletonNoVolatile instance; // not use volatile

    private int num = 0;

    public int getNum() {
        return num;
    }

    private SingletonNoVolatile() {
        this.num = 11;
    }
    public static SingletonNoVolatile getInstance() {
        if (instance == null) {
            synchronized (SingletonReorderTest.class) {
                if (instance == null) {
                    instance = new SingletonNoVolatile();
                }
            }
        }
        return instance;
    }
}


