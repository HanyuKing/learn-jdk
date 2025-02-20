package algo.concurrent.print_foobar_alternately;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class FooBar {
    private int n;

    public FooBar(int n) {
        this.n = n;
    }

    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private boolean isPrintFoo = true;

    public void foo(Runnable printFoo) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            lock.lock();
            while (!isPrintFoo) {
                c1.await();
            }
        	// printFoo.run() outputs "foo". Do not change or remove this line.
        	printFoo.run();
            isPrintFoo = false;
            c2.signal();
            lock.unlock();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            lock.lock();
            while (isPrintFoo) {
                c2.await();
            }
            // printBar.run() outputs "bar". Do not change or remove this line.
        	printBar.run();
            isPrintFoo = true;
            c1.signal();
            lock.unlock();
        }
    }
}