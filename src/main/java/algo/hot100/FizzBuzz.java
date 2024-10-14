package algo.hot100;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

class FizzBuzz {
    private int n;

    private Semaphore sn = new Semaphore(1);
    private Semaphore s3 = new Semaphore(0);
    private Semaphore s5 = new Semaphore(0);
    private Semaphore s35 = new Semaphore(0);

    private volatile int val;
    private volatile boolean isEnd = false;

    public FizzBuzz(int n) {
        this.n = n;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        for (int i = 3; i <= n; i += 3) {
            if (!condition5(i)) {
                s3.acquire();
                printFizz.run();
                sn.release();
            }
        }
        System.out.println("fizz");
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        for (int i = 5; i <= n; i += 5) {
            if (!condition3(i)) {
                s5.acquire();
                printBuzz.run();
                sn.release();
            }
        }
        System.out.println("buzz");
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        for (int i = 15; i <= n; i += 15) {
            if (condition3(i) && condition5(i)) {
                s35.acquire();
                printFizzBuzz.run();
                sn.release();
            }
        }
        System.out.println("fizzbuzz");
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            sn.acquire();

            System.out.println(i);

            val = i;

            if (i % 3 == 0 && i % 5 == 0) {
                s35.release();
            } else if (i % 3 == 0) {
                s3.release();
            } else if (i % 5 == 0) {
                s5.release();
            } else {
                printNumber.accept(i);
                sn.release();
            }
        }
        System.out.println("number");
    }

    private boolean condition5(int val) {
        return val % 5 == 0;
    }
    private boolean condition3(int val) {
        return val % 3 == 0;
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> result = new ArrayList<>();
        FizzBuzz fizzBuzz = new FizzBuzz(15);
        new Thread(() -> {
            try {
                fizzBuzz.buzz(() -> result.add("buzz,"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                fizzBuzz.fizz(() -> result.add("fizz,"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                fizzBuzz.number(value -> result.add(value + ","));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                fizzBuzz.fizzbuzz(() -> result.add("fizzbuzz,"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread.sleep(1000);
        System.out.println(result);
    }
}