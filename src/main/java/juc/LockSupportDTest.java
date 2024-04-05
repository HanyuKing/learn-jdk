package juc;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDTest {
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
