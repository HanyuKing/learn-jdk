package juc.thread;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/1 20:05
 * @Description
 * @Version 1.0
 **/
public class DaemonThread {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                System.out.println("I'm running...");


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.setDaemon(true); // comment this line
        thread.start();

        System.out.println("main exit...");
    }
}
