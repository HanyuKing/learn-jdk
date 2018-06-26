package juc;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Hanyu King
 * @since 2018-06-09 22:37
 */
public class ThreadToGoLockConditionTest {

    private static AtomicInteger i = new AtomicInteger(0);
    private static Lock lock = new ReentrantLock(false);
    private static Condition condition1 = lock.newCondition();
    private static Condition condition2 = lock.newCondition();
    private static Condition condition3 = lock.newCondition();

    private static int currThread = 1;

    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        lock.lock();
                        while (currThread != 1) {
                            condition1.await();
                        }
                        print();
                        currThread = 2;
                        condition2.signal();
                    } catch (Exception e) {

                    } finally {
                        lock.unlock();
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
                    try {
                        lock.lock();
                        while (currThread != 2) {
                            condition2.await();
                        }
                        print();
                        currThread = 3;
                        condition3.signal();
                    } catch (Exception e) {

                    } finally {
                        lock.unlock();
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
                    try {
                        lock.lock();
                        while (currThread != 3) {
                            condition3.await();
                        }
                        print();
                        currThread = 1;
                        condition1.signal();
                    } catch (Exception e) {

                    } finally {
                        lock.unlock();
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
