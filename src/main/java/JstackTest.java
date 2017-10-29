import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wanghanyu
 * @create 2017-10-23 16:06
 */
public class JstackTest {
    public static Executor executor = Executors.newFixedThreadPool(5);
    //public static Object lock = new Object();
    public static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        Task task1 = new Task();
        Task task2 = new Task();

        executor.execute(task1);
        executor.execute(task2);
    }

    static class Task implements Runnable {

        public void run() {
            lock.lock();
            calculate();
        }

        public void calculate() {
            int i = 0;
            while(true) {
                i++;
            }
        }
    }

}

