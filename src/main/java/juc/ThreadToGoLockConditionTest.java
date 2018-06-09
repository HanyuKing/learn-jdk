package juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Hanyu King
 * @since 2018-06-09 22:37
 */
public class ThreadToGoLockConditionTest {

    private static AtomicInteger i = new AtomicInteger(0);
    private volatile static int currThread = 1;
    private static Object condition = new Object();

    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    synchronized (condition) {
                        while(currThread != 1) {
                            try {
                                condition.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        print();
                        currThread = 2;
                        condition.notifyAll();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    synchronized (condition) {
                        while(currThread != 2) {
                            try {
                                condition.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        print();
                        currThread = 3;
                        condition.notifyAll();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    synchronized (condition) {
                        while(currThread != 3) {
                            try {
                                condition.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        print();
                        currThread = 1;
                        condition.notifyAll();
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private static void print() {
        System.out.println(Thread.currentThread() + " -> " + i.incrementAndGet());
    }
}
