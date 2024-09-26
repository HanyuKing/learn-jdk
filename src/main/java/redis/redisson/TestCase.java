package redis.redisson;

import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RBitSet;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @Author Hanyu.Wang
 * @Date 2024/9/23 16:54
 * @Description
 * @Version 1.0
 **/
public class TestCase {

    private RedissonClient redissonClient;

    @Before
    public void init() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        redissonClient = Redisson.create(config);
    }

    @Test
    public void testBitSetSet() {
        RBitSet rBitSet = redissonClient.getBitSet("bitset");
        rBitSet.set(10, true);
        assert rBitSet.get(10);

        rBitSet.set(10, false);
        assert !rBitSet.get(10);
    }

    @Test
    public void testBloomFilter() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("phoneList");

        //初始化布隆过滤器：预计元素为100000000L,误差率为3%
        bloomFilter.tryInit(100000000L,0.03);

        //将号码10086插入到布隆过滤器中
        bloomFilter.add("10086");

        //判断下面号码是否在布隆过滤器中
        System.out.println(bloomFilter.contains("123456")); //false
        System.out.println(bloomFilter.contains("10086")); //true
    }
}
