package algo.concurrent.print;

import org.junit.Test;

import java.util.concurrent.Semaphore;

/**
 * @Author Hanyu.Wang
 * @Date 2025/2/18 19:49
 * @Description
 * @Version 1.0
 **/
public class PrintWithWaitNotify {
    private int printNumber = 1;
    private Object lockObj = new Object();
    private class Print1 implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);

                    synchronized (lockObj) {
                        while (printNumber != 1) {
                            lockObj.wait();
                        }
                        System.out.println(1);
                        printNumber = 2;
                        lockObj.notifyAll();
                    }
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

                    synchronized (lockObj) {
                        while (printNumber != 2) {
                            lockObj.wait();
                        }
                        System.out.println(2);
                        printNumber = 1;
                        lockObj.notifyAll();
                    }
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
