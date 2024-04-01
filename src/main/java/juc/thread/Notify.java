package juc.thread;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/1 20:39
 * @Description
 * @Version 1.0
 **/
public class Notify {
    public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();

        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            new Thread(() -> {
                synchronized (obj) {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("t" + finalI +"...");
            }).start();
        }

        Thread.sleep(1000);

        synchronized (obj) {
            obj.notifyAll(); // use notify
        }
    }
}
