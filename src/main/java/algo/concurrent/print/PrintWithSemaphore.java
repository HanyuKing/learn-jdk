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
public class PrintWithSemaphore {
    private Semaphore semaphore = new Semaphore(1);
    private Semaphore semaphore2 = new Semaphore(0);
    private class Print1 implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    semaphore.acquire();
                    System.out.println(1);
                    semaphore2.release();
                    Thread.sleep(1000);
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
                    semaphore2.acquire();
                    System.out.println(2);
                    semaphore.release();
                    Thread.sleep(1000);
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
