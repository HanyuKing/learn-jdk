package cache.caffine;

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

    @Test
    public void testConcurrentLoadingCache() throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger();

        LoadingCache<String, String> loadingCache = Caffeine.newBuilder()
                .expireAfterWrite(2000L, TimeUnit.MILLISECONDS)
                .refreshAfterWrite(1000L, TimeUnit.MILLISECONDS)
                .expireAfterAccess(5000L, TimeUnit.MILLISECONDS)
                .build(key -> {
                    Thread.sleep(1000);
                    atomicInteger.incrementAndGet();
                    return "newValue";
                });

        // loadingCache.put("key", "oldValue");
        // Thread.sleep(1995);

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

        System.out.println(atomicInteger.get());
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
}
