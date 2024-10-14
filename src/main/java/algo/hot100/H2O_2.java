package algo.hot100;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class H2O_2 {

    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private int oCount = 0;
    private int hCount = 0;
    public H2O_2() {
        
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        lock.lock();
		try {
            while (hCount == 2) {
                c1.await();
            }
            hCount++;

            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();

            if (hCount == 2 && oCount == 1) {
                oCount = 0;
                hCount = 0;

            }
            c2.signal();
        } finally {
            lock.unlock();
        }

    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        lock.lock();
        try{
            while (oCount == 1) {
                c2.await();
            }
            oCount = 1;
            // releaseOxygen.run() outputs "O". Do not change or remove this line.
            releaseOxygen.run();

            if (hCount == 2 && oCount == 1) {
                oCount = 0;
                hCount = 0;
            }
            c1.signal();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {

    }
}