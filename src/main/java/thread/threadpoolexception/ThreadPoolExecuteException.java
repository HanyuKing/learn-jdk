package thread.threadpoolexception;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @author Hanyu King
 * @since 2018-07-06 12:25
 */
public class ThreadPoolExecuteException {
    public static void main(String[] args) {
        MyThreadPoolExecutor threadPool = new MyThreadPoolExecutor(1, 1, 1000, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>());
        threadPool.submit(new Runnable() { // execute also ok
            public void run() {
                Object obj = null;
                System.out.println(obj.toString());
            }
        });

//        threadPool.execute(new Runnable() {
//            public void run() {
//                Object obj = null;
//                System.out.println(obj.toString());
//            }
//        });

        threadPool.shutdown();
    }

    static class MyThreadPoolExecutor extends ThreadPoolExecutor {

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            System.out.println(t.toString());
        }
    }
}
