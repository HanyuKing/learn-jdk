package algo.concurrent.print_zero_even_odd;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

class ZeroEvenOdd {
    private int n;
    
    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    private Lock lock = new ReentrantLock();
    private Condition zeroCondition = lock.newCondition();
    private Condition evenCondition = lock.newCondition();
    private Condition oddCondition = lock.newCondition();
    private int printNum = 0;
    int number = 1;

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            // condition wait
            lock.lock();
            while (printNum != 0) {
                zeroCondition.await();
            }
            printNumber.accept(0);
            if ((number & 1) == 1) {
                oddCondition.signal();
                printNum = 1;
            } else {
                evenCondition.signal();
                printNum = 2;
            }
            lock.unlock();
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            lock.lock();
            while (printNum != 2) {
                System.out.println("even await");
                evenCondition.await();
            }
            printNumber.accept(number++);
            zeroCondition.signal();
            printNum = 0;
            lock.unlock();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            lock.lock();
            while (printNum != 1) {
                System.out.println("odd await");
                oddCondition.await();
            }
            printNumber.accept(number++);
            printNum = 0;
            zeroCondition.signal();
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(5);
        IntConsumer intConsumer = value -> System.out.println(value + " ");
        new Thread(() -> {
            try {
                zeroEvenOdd.zero(intConsumer);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                zeroEvenOdd.even(intConsumer);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                zeroEvenOdd.odd(intConsumer);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}