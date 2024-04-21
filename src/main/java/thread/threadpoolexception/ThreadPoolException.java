package thread.threadpoolexception;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class ThreadPoolException {
    public static void main(String[] args) throws InterruptedException {


        //1.实现一个自己的线程池工厂
        ThreadFactory factory = (Runnable r) -> {
            //创建一个线程
            Thread t = new Thread(r);
            //给创建的线程设置UncaughtExceptionHandler对象 里面实现异常的默认逻辑
            t.setDefaultUncaughtExceptionHandler((Thread thread1, Throwable e) -> {
                e.printStackTrace();
            });
            return t;
        };

        //2.创建一个自己定义的线程池，使用自己定义的线程工厂
        ExecutorService executorService = new ThreadPoolExecutor(
                1,
                1,
                0,
                MILLISECONDS,
                new LinkedBlockingQueue(10),
                factory);

        // submit无提示
        executorService.submit(new task());

        Thread.sleep(1000);
        System.out.println("==================为检验打印结果，1秒后执行execute方法");

        // execute 方法被线程工厂factory 的UncaughtExceptionHandler捕捉到异常
        executorService.execute(new task());


    }


}

class task implements Runnable {
    @Override
    public void run() {
        System.out.println("进入了task方法！！！");
        int i = 1 / 0;
    }
}