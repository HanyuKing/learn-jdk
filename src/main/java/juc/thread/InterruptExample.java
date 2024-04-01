package juc.thread;

public class InterruptExample {

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
