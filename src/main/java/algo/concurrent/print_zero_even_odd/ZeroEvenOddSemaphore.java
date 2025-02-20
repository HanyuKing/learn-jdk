package algo.concurrent.print_zero_even_odd;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

class ZeroEvenOddSemaphore {
    private int n;

    public ZeroEvenOddSemaphore(int n) {
        this.n = n;
    }

    private Semaphore s0 = new Semaphore(1);
    private Semaphore s1 = new Semaphore(0);
    private Semaphore s2 = new Semaphore(0);
    private int printNum = 0;
    int number = 1;

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            // condition wait
            s0.acquire();
            printNumber.accept(0);
            if ((number & 1) == 1) {
                s1.release();
            } else {
                s2.release();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        while (number <= n) {
            if ((n & 1) == 1 && number == n) {
                break;
            }
            s2.acquire();
            printNumber.accept(number++);
            s0.release();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        while (number <= n) {
            if ((n & 1) == 0 && number == n) {
                break;
            }
            s1.acquire();
            printNumber.accept(number++);
            s0.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ZeroEvenOddSemaphore zeroEvenOdd = new ZeroEvenOddSemaphore(1);
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