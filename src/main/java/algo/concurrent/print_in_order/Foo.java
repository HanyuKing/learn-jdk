package algo.concurrent.print_in_order;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Foo {
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();
    private int num = 1;
    public Foo() {
        
    }

    public void first(Runnable printFirst) throws InterruptedException {
        lock.lock();
        while (num != 1) {
            c1.await();
        }
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        num = 2;
        c2.signal();
        lock.unlock();

    }

    public void second(Runnable printSecond) throws InterruptedException {
        lock.lock();
        while (num != 2) {
            c2.await();
        }
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        num = 3;
        c3.signal();
        lock.unlock();
    }

    public void third(Runnable printThird) throws InterruptedException {
        lock.lock();
        while (num != 3) {
            c3.await();
        }
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
        num = 1;
        c1.signal();
        lock.unlock();
    }
}