package algo.concurrent.fizz_buzz_multithread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

class FizzBuzz {
    private int n;
    private Lock lock = new ReentrantLock();
    private Condition fizzCondition = lock.newCondition();
    private Condition buzzCondition = lock.newCondition();
    private Condition fizzbuzzCondition = lock.newCondition();
    private Condition numberCondition = lock.newCondition();
    private boolean isEnd = false;
    private int flag = 1;

    public FizzBuzz(int n) {
        this.n = n;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        while (true) {
            lock.lock();
            while (flag != 2 && !isEnd) {
                fizzCondition.await();
            }
            if (isEnd) {
                lock.unlock();
                break;
            }
            printFizz.run();
            flag = 1;
            numberCondition.signal();
            lock.unlock();
        }
        //System.out.println("fizz end ");
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        while (true) {
            lock.lock();
            while (flag != 3 && !isEnd) {
                buzzCondition.await();
            }
            if (isEnd) {
                lock.unlock();
                break;
            }
            printBuzz.run();
            flag = 1;
            numberCondition.signal();
            lock.unlock();
        }
        //System.out.println("buzz end ");
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        while (true) {
            lock.lock();
            while (flag != 4 && !isEnd) {
                fizzbuzzCondition.await();
            }
            if (isEnd) {
                lock.unlock();
                break;
            }
            printFizzBuzz.run();
            flag = 1;
            numberCondition.signal();
            lock.unlock();
        }
        //System.out.println("fizzbuzz end ");
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n + 1; i++) {
            lock.lock();
            while (flag != 1) {
                numberCondition.await();
            }
            if (i == n + 1) {
                isEnd = true;
                fizzbuzzCondition.signal();
                fizzCondition.signal();
                buzzCondition.signal();
                lock.unlock();
                //System.out.println("number end ");
                return;
            }

            if ((i % 3 == 0) && (i % 5 == 0)) {
                fizzbuzzCondition.signal();
                flag = 4;
            } else if (i % 3 == 0) {
                fizzCondition.signal();
                flag = 2;
            } else if (i % 5 == 0) {
                buzzCondition.signal();
                flag = 3;
            } else {
                printNumber.accept(i);
            }

            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        FizzBuzz fizzBuzz = new FizzBuzz(15);

        new Thread(() -> {
            try {
                fizzBuzz.number(System.out::println);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                fizzBuzz.fizz(() -> System.out.println("fizz"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                fizzBuzz.buzz(() -> System.out.println("buzz"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                fizzBuzz.fizzbuzz(() -> System.out.println("fizzbuzz"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}