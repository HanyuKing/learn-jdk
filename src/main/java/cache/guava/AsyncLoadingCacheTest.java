package cache.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncLoadingCacheTest {
    /**
     * refreshAfterWrite < now < expireAfterWrite，并且设置了线程池 Executor 时
     *
     * 线程池中一个线程异步load，请求线程全部返回旧值
     *
     * 对比caffeine
     * @see cache.caffeine.CacheTest#testConcurrentLoadingCache()
     */
    @Test
    public void testConcurrentLoadingCache() throws Exception {

        AtomicInteger atomicInteger = new AtomicInteger();

        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(2000L, TimeUnit.MILLISECONDS)
                .refreshAfterWrite(1000L, TimeUnit.MILLISECONDS)
                .concurrencyLevel(8)
                //.expireAfterAccess(5000L, TimeUnit.MILLISECONDS)
                .build(CacheLoader.asyncReloading(new CacheLoader<String, String>() {
                    @Override
                    public String load(String o) throws Exception {
                        Thread.sleep(2000);
                        System.out.println(Thread.currentThread() + " load new Value");
                        atomicInteger.incrementAndGet();
                        return "newValue";
                    }
                }, ForkJoinPool.commonPool()));

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

        Thread.sleep(3000);

        System.out.println("end: " + atomicInteger.get());
        System.out.println("end: " + loadingCache.get("key"));
    }

    /**
     * refreshAfterWrite < expireAfterWrite < now，并且设置了线程池 Executor 时
     *
     * 主线程的一个线程去异步load，所有线程都等待这个线程加载完，然后返回新数据
     *
     *
     * 对比caffeine
     * @see cache.caffeine.CacheTest#testConcurrentLoadingCache2()
     */
    @Test
    public void testConcurrentLoadingCache2() throws Exception {

        AtomicInteger atomicInteger = new AtomicInteger();

        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(2000L, TimeUnit.MILLISECONDS)
                .refreshAfterWrite(1000L, TimeUnit.MILLISECONDS)
                .concurrencyLevel(8)
                //.expireAfterAccess(5000L, TimeUnit.MILLISECONDS)
                .build(CacheLoader.asyncReloading(new CacheLoader<String, String>() {
                    @Override
                    public String load(String o) throws Exception {
                        Thread.sleep(2000);
                        System.out.println(Thread.currentThread() + " load new Value");
                        atomicInteger.incrementAndGet();
                        return "newValue";
                    }
                }, ForkJoinPool.commonPool()));

        loadingCache.put("key", "oldValue");
        Thread.sleep(2500);

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

        Thread.sleep(3000);

        System.out.println("end: " + atomicInteger.get());
        System.out.println("end: " + loadingCache.get("key"));
    }
}
