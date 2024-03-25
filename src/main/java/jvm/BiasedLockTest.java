package jvm;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/25 11:03
 * @Description
 * @Version 1.0
 **/
public class BiasedLockTest {

    /**
     * -XX:BiasedLockingStartupDelay=0
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // Thread.sleep(5000);
        new BiasedLockFoo().incr();
    }
}

class BiasedLockFoo {

    private int i;

    public synchronized void incr(){
        i++;
        System.out.println(Thread.currentThread().getName()+"-"+ ClassLayout.parseInstance(this).toPrintable());
    }
}