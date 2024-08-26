package cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Hanyu.Wang
 * @Date 2024/5/22 17:08
 * @Description
 * @Version 1.0
 **/
public class CacheTest {

    /**
     * refreshAfterWrite < now < expireAfterWrite 时
     * ForkJoin#commonPoll 的一个线程去异步load，所有请求线程都返回老数据
     * @throws Exception
     */
    @Test
    public void testConcurrentLoadingCache() throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger();

        LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
                .expireAfterWrite(2000L, TimeUnit.MILLISECONDS)
                .refreshAfterWrite(1000L, TimeUnit.MILLISECONDS)
                //.expireAfterAccess(5000L, TimeUnit.MILLISECONDS)
                .build(key -> {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread() + " load new Value");
                    atomicInteger.incrementAndGet();
                    return "newValue";
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
                    System.out.println(Thread.currentThread() + ":" + System.currentTimeMillis() + "->" + value);
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

        System.out.println(atomicInteger.get());

        // waiting load
        Thread.sleep(2000);
    }

    /**
     * refreshAfterWrite < expireAfterWrite < now 时
     * 主线程的一个线程去异步load，所有线程都等待这个线程加载完，然后返回新数据
     * @throws Exception
     */
    @Test
    public void testConcurrentLoadingCache2() throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger();

        LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
                .expireAfterWrite(2000L, TimeUnit.MILLISECONDS)
                .refreshAfterWrite(1000L, TimeUnit.MILLISECONDS)
                //.expireAfterAccess(5000L, TimeUnit.MILLISECONDS)
                .build(key -> {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread() + ":" + System.currentTimeMillis() + " load new Value");
                    atomicInteger.incrementAndGet();
                    return "newValue";
                });
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
                    System.out.println(Thread.currentThread() + ":" + System.currentTimeMillis() + "->" + value);
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

        System.out.println(atomicInteger.get());

        // waiting load
        Thread.sleep(2000);
    }

    @Test
    public void testLoadingCache() {
        LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
                //创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存；refreshAfterWrite仅支持LoadingCache
                .refreshAfterWrite(10, TimeUnit.SECONDS)
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .expireAfterAccess(10, TimeUnit.SECONDS)
                .maximumSize(10)
                //根据key查询数据库里面的值，这里是个lamba表达式
                .build(key -> new Date().toString());

        String value = loadingCache.get("key");

        System.out.println("value->" + value);
    }

    @Test
    public void testRefreshAfterWrite() throws InterruptedException {
        LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
                //创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存；refreshAfterWrite仅支持LoadingCache
                .refreshAfterWrite(2, TimeUnit.SECONDS)
                .maximumSize(10)
                //根据key查询数据库里面的值，这里是个lamba表达式
                .build(key -> new Date().toString());

        System.out.println("value->" + loadingCache.get("key"));

        Thread.sleep(2500);

        System.out.println("value->" + loadingCache.get("key"));
    }

    @Test
    public void testRefreshAfterWriteAndExpireAfterWrite() throws InterruptedException {
        LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
                //创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存；refreshAfterWrite仅支持LoadingCache
                .refreshAfterWrite(2, TimeUnit.SECONDS)
                .expireAfterWrite(4, TimeUnit.SECONDS)
                .maximumSize(10)
                //根据key查询数据库里面的值，这里是个lamba表达式
                .build(key -> new Date().toString());

        System.out.println("value->" + loadingCache.get("key"));

        Thread.sleep(2500);

        System.out.println("value->" + loadingCache.get("key"));

        Thread.sleep(2000);

        System.out.println("value->" + loadingCache.get("key"));
    }

    @Test
    public void testExpireAfterWrite() throws InterruptedException {
        LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
                //创建缓存或者最近一次更新缓存后经过指定时间间隔，刷新缓存；refreshAfterWrite仅支持LoadingCache
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .maximumSize(10)
                //根据key查询数据库里面的值，这里是个lamba表达式
                .build(key -> new Date().toString());

        System.out.println("value->" + loadingCache.get("key"));

        Thread.sleep(2500);

        System.out.println("value->" + loadingCache.get("key"));
    }
}
