package stream.priority;

import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BinaryOperator;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * @Author Hanyu.Wang
 * @Date 2024/8/1 19:41
 * @Description
 * @Version 1.0
 **/
public class StreamTest {

    @Test
    public void testCompleteFutureAllCompleted() {
        final int MAX_CPU = Runtime.getRuntime().availableProcessors();

        ThreadPoolExecutor asyncThreadPool = new ThreadPoolExecutor(10,
                    10,
                    60L,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(MAX_CPU * 120));

        int total = 227;
        int pageSize = 10;

        CompletableFuture<Boolean>[] completableFutures = IntStream.rangeClosed(1, total / pageSize + 1)
                .mapToObj(index -> CompletableFuture
                        .supplyAsync(() -> {
                            try {
                                doFlow(index, pageSize);
                            } catch (Exception e) {
                                return Boolean.FALSE;
                            }
                            return Boolean.TRUE;
                        }, asyncThreadPool)
                        .whenComplete((res, throwable) -> Optional.ofNullable(res).orElse(Boolean.FALSE)))
                .toArray((IntFunction<CompletableFuture<Boolean>[]>) CompletableFuture[]::new);
        CompletableFuture.allOf(completableFutures).join();
        Boolean success = Arrays.stream(completableFutures).map(CompletableFuture::join).reduce(new BinaryOperator<Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean, Boolean aBoolean2) {
                        return aBoolean || aBoolean2;
                    }
                })
                .orElse(false);

        if (Boolean.TRUE.equals(success)) {
            System.out.println("testCompleteFuture all completed");
        } else {
            System.out.println("testCompleteFuture not all completed");
        }
    }

    private Boolean doFlow(int index, int pageSize) {
        System.out.println(Thread.currentThread().getId() + " -> " + "offset: " + ((index - 1) * pageSize) + ", pageSize: " + pageSize);
        if (index > 1) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
