package gc;

/**
 * @author wanghanyu
 * @create 2017-10-29 17:26
 */
public class DeadLockTest {
    public static Object lock1 = new Object();
    public static Object lock2 = new Object();

    public static void main(String[] args) throws Exception {
        new Thread(new Runnable() {
            public void run() {
                synchronized (lock1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lock2) {
                        System.out.println("Thread-1");
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                synchronized (lock2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lock1) {
                        System.out.println("Thread-2");
                    }
                }
            }
        }).start();
    }
}
