package algo.hot100;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class H2O {

    private Lock lock = new ReentrantLock();
    private Object oObj = new Object();
    private Object hObj = new Object();
    private int oCount = 0;
    private int hCount = 0;
    public H2O() {
        
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
		synchronized (this) {
            while (hCount == 2) {
                this.wait();
            }
            hCount++;

            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();

            if (hCount == 2 && oCount == 1) {
                oCount = 0;
                hCount = 0;

                notifyAll();
            }
        }

    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        synchronized (this) {
            while (oCount == 1) {
                this.wait();
            }
            oCount = 1;
            // releaseOxygen.run() outputs "O". Do not change or remove this line.
            releaseOxygen.run();

            if (hCount == 2 && oCount == 1) {
                oCount = 0;
                hCount = 0;

                notifyAll();
            }
        }
    }

    public static void main(String[] args) {

    }
}