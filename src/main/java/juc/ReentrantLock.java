package juc;

import org.junit.Test;

import java.util.concurrent.locks.Lock;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/2 15:50
 * @Description
 * @Version 1.0
 **/
public class ReentrantLock {
    @Test
    public void testReentrantLock() {

    }

    @Test
    public void testNoReentrantLock() {
        Lock lock = new java.util.concurrent.locks.ReentrantLock();
        lock.lock();
        lock.unlock();
    }
}
