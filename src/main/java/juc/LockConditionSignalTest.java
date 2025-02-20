package juc;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author Hanyu.Wang
 * @Date 2025/2/19 16:36
 * @Description
 * @Version 1.0
 **/
public class LockConditionSignalTest {

    @Test
    public void testSignalBeforeAwait() throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition c1 = lock.newCondition();
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    Thread.sleep(1000);
                    System.out.println(System.currentTimeMillis() + " await");
                    c1.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
                System.out.println("hhhhhh");
            }
        }).start();

        lock.lock();
        c1.signal();
        System.out.println(System.currentTimeMillis() + " signal");
        lock.unlock();

        Thread.sleep(2000);

        System.out.println(System.currentTimeMillis() + " end");

    }
}
