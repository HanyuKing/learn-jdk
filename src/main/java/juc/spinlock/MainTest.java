package juc.spinlock;

/**
 * @Author Hanyu.Wang
 * @Date 2022/8/1 12:52
 * @Description
 * @Version 1.0
 **/
public class MainTest {
    public static void main(String[] args) {
        MCSLock lock = new MCSLock();
        lock.lock();
    }
}
