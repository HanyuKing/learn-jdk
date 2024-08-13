package cache.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class CacheTest {
    /**
     *  refreshAfterWrite < now < expireAfterWrite 时
     *
     * 请求线程的其中一个线程load，其它线程返回旧值
     */
    public static void main(String[] args) throws ExecutionException, BrokenBarrierException, InterruptedException {

        AtomicInteger atomicInteger = new AtomicInteger();

        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .expireAfterWrite(2000L, TimeUnit.MILLISECONDS)
                .refreshAfterWrite(1000L, TimeUnit.MILLISECONDS)
                .concurrencyLevel(8)
                //.expireAfterAccess(5000L, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String o) throws Exception {
                        Thread.sleep(2000);
                        System.out.println(Thread.currentThread() + " load new Value");
                        atomicInteger.incrementAndGet();
                        return "newValue";
                    }
                });

        loadingCache.put("key", "oldValue");
        Thread.sleep(1500);

        int partis = 100;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(partis, () -> {
            System.out.println(System.currentTimeMillis() + "->start...");
        });

        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < partis; i++) {
            Thread t = new Thread(() -> {
                try {
                    cyclicBarrier.await();
                    String value = loadingCache.get("key");
                    System.out.println(System.currentTimeMillis() + "->" + value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            threadList.add(t);
            t.start();
        }

        for (Thread t : threadList) {
            t.join();
        }

        System.out.println("end: " + atomicInteger.get());
        System.out.println("end: " + loadingCache.get("key"));
    }
}
