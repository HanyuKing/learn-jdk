package guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheTest {
    public static void main(String[] args) throws ExecutionException {
        LoadingCache<Object, Object> loadingCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1000L, TimeUnit.MILLISECONDS)
                .refreshAfterWrite(1000L, TimeUnit.MILLISECONDS)
                .expireAfterAccess(1000L, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object o) throws Exception {
                return "";
            }
        });

        loadingCache.get("");
    }
}
