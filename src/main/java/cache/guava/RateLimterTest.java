package cache.guava;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class RateLimterTest {
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(1);
        for(int i = 0; i < 10; i++) {
            boolean result = rateLimiter.tryAcquire(1, 10, TimeUnit.SECONDS);
            System.out.println(result);
        }
    }
}
