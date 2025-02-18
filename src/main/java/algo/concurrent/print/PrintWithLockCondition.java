package algo.concurrent.print;

import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author Hanyu.Wang
 * @Date 2025/2/18 19:49
 * @Description
 * @Version 1.0
 **/
public class PrintWithLockCondition {
    private int printNumber = 1;
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private class Print1 implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    lock.lock();
                    while (printNumber != 1) {
                        c1.await();
                    }
                    System.out.println(1);
                    printNumber = 2;
                    c2.signal();
                    lock.unlock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class Print2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    lock.lock();
                    while (printNumber != 2) {
                        c2.await();
                    }
                    System.out.println(2);
                    printNumber = 1;
                    c1.signal();
                    lock.unlock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Test
    public void testPrint() throws InterruptedException {
        new Thread(new Print1()).start();
        new Thread(new Print2()).start();
        Semaphore semaphore = new Semaphore(0);
        semaphore.acquire();
    }
}
