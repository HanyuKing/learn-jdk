package jvm;

/**
 * @author Hanyu King
 * @since 2018-05-31 11:37
 */
public class DeadLock {
    private static Object SOURCE1 = new Object();
    private static Object SOURCE2 = new Object();
    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                synchronized (SOURCE1) {
                    System.out.println("acquired source1");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (SOURCE2) {
                        System.out.println("acquired source2");
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                synchronized (SOURCE2) {
                    System.out.println("acquired source2");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (SOURCE1) {
                        System.out.println("acquired source2");
                    }
                }
            }
        }).start();
    }
}
