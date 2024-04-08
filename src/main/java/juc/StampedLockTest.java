package juc;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * @Author Hanyu.Wang
 * @Date 2024/2/28 10:46
 * @Description
 * @Version 1.0
 **/
public class StampedLockTest {
    public static void main(String[] args) throws InterruptedException {
        Point point = new Point();
        point.moveIfAtOrigin(4, 3);

        new Thread(() -> {
            point.move(1, 2);
        }).start();

        Thread.sleep(100);

        System.out.println(point.distanceFromOrigin());

    }

    @Test
    public void testStampedLock() throws Exception{
        final StampedLock lock = new StampedLock();
        Thread thread01 = new Thread(()->{
            // 获取写锁
            lock.writeLock();
            // 永远阻塞在此处，不释放写锁
            LockSupport.park();
        });
        thread01.start();
        // 保证thread01获取写锁
        Thread.sleep(100);
        Thread thread02 = new Thread(()->
                //阻塞在悲观读锁
                lock.readLock()
        );
        thread02.start();
        // 保证T2阻塞在读锁
        Thread.sleep(100);
        //中断线程thread02
        //会导致线程thread02所在CPU飙升
        thread02.interrupt();
        thread02.join();
    }

    @Test
    public void testReentrant() {
        readWriteLockReentrant(3);
        stampedLockReentrant(3); // no
    }

    ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    public void readWriteLockReentrant(int reentrantCount) {
        if (reentrantCount == 0) {
            return;
        }
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            System.out.println("readWriteLockReentrant count: " + reentrantCount);
            readWriteLockReentrant(--reentrantCount);
        } finally {
            writeLock.unlock();
        }
    }


    StampedLock stampedLock = new StampedLock();
    public void stampedLockReentrant(int reentrantCount) {
        if (reentrantCount == 0) {
            return;
        }
        long stamp = stampedLock.writeLock();
        try {
            System.out.println("stampedLockReentrant count: " + reentrantCount);
            stampedLockReentrant(--reentrantCount);
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Test
    public void testBit() {
        long LG_READERS = 7;
        long RUNIT = 1L;
        long WBIT  = 1L << LG_READERS;
        long RBITS = WBIT - 1L;
        long RFULL = RBITS - 1L;
        long ABITS = RBITS | WBIT;
        final long SBITS = ~RBITS; // note overlap with ABITS
        long ORIGIN = WBIT << 1;

        long OA = ORIGIN & ABITS; // 0
        long OS = ORIGIN & SBITS; // 100000000

        /*
            LG_READERS: 111
            WBIT:  10000000
            RBITS: 1111111
            RFULL: 1111110
            ABITS: 11111111
            SBITS: 1111111111111111111111111111111111111111111111111111111110000000
           ORIGIN: 100000000
   ORIGIN & SBITS: 100000000
         */

        System.out.println("LG_READERS: " + Long.toBinaryString(LG_READERS));
        System.out.println("WBIT: " + Long.toBinaryString(WBIT));
        System.out.println("RBITS: " + Long.toBinaryString(RBITS));
        System.out.println("RFULL: " + Long.toBinaryString(RFULL));
        System.out.println("ABITS: " + Long.toBinaryString(ABITS));
        System.out.println("SBITS: " + Long.toBinaryString(SBITS));
        System.out.println("ORIGIN: " + Long.toBinaryString(ORIGIN));

        System.out.println("ORIGIN & ABITS: " + OA);
        System.out.println("ORIGIN & SBITS: " + Long.toBinaryString(OS));

        System.out.println("ORIGIN + WBIT: " + Long.toBinaryString(ORIGIN + WBIT) + ", value: " + (ORIGIN + WBIT));
    }
}

class Point {
    private double x, y;
    private final StampedLock sl = new StampedLock();

    void move(double deltaX, double deltaY) { // an exclusively locked method
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
            Thread.sleep(1000);
            System.out.println(Long.toBinaryString(stamp) + " -> " + stamp);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    double distanceFromOrigin() { // A read-only method
        long stamp = sl.tryOptimisticRead();

        System.out.println("distanceFromOrigin stamp: " + Long.toBinaryString(stamp) + " -> " + stamp);

        double currentX = x, currentY = y;
        if (!sl.validate(stamp)) {
            stamp = sl.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    void moveIfAtOrigin(double newX, double newY) { // upgrade
        // Could instead start with optimistic, not read mode
        long stamp = sl.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                long ws = sl.tryConvertToWriteLock(stamp);
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                }
                else {
                    sl.unlockRead(stamp);
                    stamp = sl.writeLock();
                }
            }
        } finally {
            sl.unlock(stamp);
        }
    }

    void moveIfAtOrigin2(double newX, double newY) { // upgrade
        // Could instead start with optimistic, not read mode
        long stamp = sl.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                long ws = sl.tryConvertToWriteLock(stamp);
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                }
                else {
                    sl.unlockRead(stamp);
                    stamp = sl.writeLock();
                }
            }
        } finally {
            sl.unlock(stamp);
        }
    }
}

