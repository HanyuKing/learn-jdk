package juc.thread;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class InterruptExample {


    @Test
    public void testInterruptSelf() throws Exception {
        Thread t = new Thread(() -> {
            Thread ct = Thread.currentThread();
            ct.interrupt();
            System.out.println(System.currentTimeMillis() + "->isAlive: " + ct.isAlive() + ", interrupted: " + ct.isInterrupted());

            long now = System.currentTimeMillis();
            while (System.currentTimeMillis() - now < 1000) {}

            System.out.println(System.currentTimeMillis() + "->end");
        });
        t.start();
        Thread.sleep(100);
        // t.join();
        System.out.println("isAlive2: " + t.isAlive() + ", interrupted2: " + t.isInterrupted());
        t.join();
    }

    private static class MyThread1 extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                System.out.println("Thread run");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class MyThread2 extends Thread {

        Object obj = new Object();

        @Override
        public void run() {
            System.out.println("Thread2 run");

            synchronized (obj) {
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    private static class MyThread3 extends Thread {

        public void run() {
            System.out.println("Thread3 run");
            long start = System.currentTimeMillis();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Thread3 sleep " + (System.currentTimeMillis() - start) + " ms");
                throw new RuntimeException(e);
            }

        }
    }

    @Test
    public void testInterruptSleepThread() throws InterruptedException {
        MyThread3 t = new MyThread3();
        t.start();
        long sleep = ThreadLocalRandom.current().nextInt(1000);
        System.out.println("sleep: " + sleep + " ms");
        Thread.sleep(sleep);
        t.interrupt();
    }


    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new MyThread1();
        thread1.start();
        thread1.interrupt();

        Thread thread2 = new MyThread2();
        thread2.start();
        thread2.interrupt();
        System.out.println("Main run");

        thread2.interrupt();


    }
}
