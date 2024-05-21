package juc;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class LockSupportDTest {

    @Test
    public void testConditionAwait() throws InterruptedException {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        condition.await();
    }

    @Test
    public void testParkThenInterrupt() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                System.out.println("t1.... execute");
                LockSupport.park(); // 中断之后唤起park，后面再park不会等待
                System.out.println("t1... unpark");
                System.out.println("t1 is interrupted: " + Thread.currentThread().isInterrupted());
            }

        });
        t1.start();

        LockSupport.parkNanos(1000);
        t1.interrupt();
        t1.join();

        if (t1.isInterrupted()) {
            Thread.currentThread().interrupt();
        }

        System.out.println("main end...");
    }

    @Test
    public void testUnParkBeforePark() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("t1.... execute");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LockSupport.park();
            System.out.println("t1... park");
        });
        t1.start();

        System.out.println("main... unpark");
        LockSupport.unpark(t1);

        t1.join();
    }

    public static void main(String[] args) {
        // jstack

        new LockSupportDTest().park2();
        // new LockSupportDTest().park();

    }

    /*
    "main" #1 prio=5 os_prio=31 tid=0x0000000160009000 nid=0x1803 waiting on condition [0x000000016b12a000]
       java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000007002a3180> (a juc.LockSupportDTest)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at juc.LockSupportDTest.park(LockSupportDTest.java:11)
        at juc.LockSupportDTest.main(LockSupportDTest.java:7)
     */
    public void park() {
        LockSupport.park(this);
    }
    /*
    "main" #1 prio=5 os_prio=31 tid=0x0000000126009000 nid=0x1503 waiting on condition [0x000000016f1a2000]
       java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:304)
        at juc.LockSupportDTest.park2(LockSupportDTest.java:26)
        at juc.LockSupportDTest.main(LockSupportDTest.java:7)
     */
    public void park2() {
        LockSupport.park();
    }
}
