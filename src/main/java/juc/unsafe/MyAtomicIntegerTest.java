package juc.unsafe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/3 15:37
 * @Description
 * @Version 1.0
 **/
public class MyAtomicIntegerTest {
    @Test
    /*
        Safe
     */
    public void testThreadSafe() throws Exception {
        MyAtomicInteger atomicInteger = new MyAtomicInteger(0);

        // AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executorService = Executors.newFixedThreadPool(200);

        for (int i = 0; i < 10000000; i++) {
            executorService.submit(() -> {
                atomicInteger.getAndUpdate(operand -> operand + 1);
            });
        }

        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);

        executorService.shutdownNow();

        System.out.println("total: " + atomicInteger.get() + ", count: " + atomicInteger.count.get());
    }

    @Test
    /*
        Unsafe
     */
    public void testThreadSafe2() throws Exception {
        Value value = new Value(0);

        // AtomicInteger atomicInteger = new AtomicInteger(0);

        ExecutorService executorService = Executors.newFixedThreadPool(20);

        for (int i = 0; i < 10000; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    value.value++;
                }
            });
        }

        executorService.shutdown();

        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);

        System.out.println(value);
    }

    @Test
    /*
        Safe
     */
    public void testThreadSafe3() throws Exception {
        MyAtomicInteger2 atomicInteger = new MyAtomicInteger2();

        ExecutorService executorService = Executors.newFixedThreadPool(200);

        int total = 10000000;
        CountDownLatch latch = new CountDownLatch(total);

        for (int i = 0; i < total; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    atomicInteger.incrementAndGet();
                    latch.countDown();
                }
            });
        }

        latch.await();

        System.out.println("total: " + atomicInteger.getValue() + ", count: " + atomicInteger.count.get());
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Value {
    int value;
}
