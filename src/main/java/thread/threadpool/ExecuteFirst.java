package thread.threadpool;

/**
 * @Author Hanyu.Wang
 * @Date 2025/5/7 02:35
 * @Description
 * @Version 1.0
 **/

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * 假如有一个线程池，并行执行3个任务返回future，其中一个任务结束就表明所有任务结束，请举出所有可能的方案
 */
public class ExecuteFirst {
    Callable<String> task1 = () -> "1";
    Callable<String> task2 = () -> "2";
    Callable<String> task3 = () -> "3";

    /**
     * 方案一：ExecutorService.invokeAny()
     */
    @Test
    public void test1() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Callable<String>> tasks = Arrays.asList(
                task1, task2, task3
        );
        try {
            String result = executor.invokeAny(tasks); // 返回第一个成功完成的任务结果
            System.out.println("First result: " + result);
        } finally {
            executor.shutdown();
        }
    }
    /**
     * 方案二：使用 Future + CompletionService（推荐）
     */
    @Test
    public void test2() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

        Future<?> f1 = completionService.submit(task1);
        Future<?> f2 = completionService.submit(task2);
        Future<?> f3 = completionService.submit(task3);

        // 获取第一个完成的任务结果
        try {
            Future<String> completed = completionService.take(); // 阻塞直到有一个完成
            String result = completed.get();
            System.out.println("First result: " + result);

            // 取消其他任务
            f1.cancel(true);
            f2.cancel(true);
            f3.cancel(true);
        } finally {
            executor.shutdown();
        }
    }

    /**
     * 方案三：手动轮询 Future 状态（不推荐）
     */
    @Test
    public void test3() {

    }

    /**
     * 方案四：使用 CompletableFuture.anyOf()（Java 8+）
     */
    @Test
    public void test4() throws Exception {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                return task1.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                return task2.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            try {
                return task3.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture<Object> any = CompletableFuture.anyOf(f1, f2, f3);

        any.thenAccept(result -> {
            System.out.println("First completed: " + result);
            // 可手动取消其他任务（不能自动取消）
        });

        Thread.sleep(1000);


    }
}
