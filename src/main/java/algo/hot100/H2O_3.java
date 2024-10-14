package algo.hot100;

import java.util.concurrent.Semaphore;

class H2O_3 {

    private Semaphore hSem = new Semaphore(2);
    private int hCount = 0;

    private Semaphore oSem = new Semaphore(0);
    public H2O_3() {
        
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hSem.acquire();
        hCount++;
        releaseHydrogen.run();
        if (hCount == 2) {
            oSem.release();
            hCount = 0;
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oSem.acquire();
        releaseOxygen.run();
        hSem.release(2);
    }

    public static void main(String[] args) {

    }
}