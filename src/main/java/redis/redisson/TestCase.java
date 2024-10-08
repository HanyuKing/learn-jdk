package redis.redisson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.util.concurrent.ExecutionException;

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

    @After
    public void after() {
        redissonClient.shutdown();
    }

    /**
     * 本质是client发送批量格式的命令
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testPipeline() throws ExecutionException, InterruptedException {
        RBatch rBatch = redissonClient.createBatch();
        // "*2\r\n$3\r\nGET\r\n$1\r\na\r\n*2\r\n$3\r\nGET\r\n$1\r\nb\r\n*2\r\n$3\r\nGET\r\n$1\r\nc\r\n"
        RFuture rFutureA = rBatch.getBucket("a", StringCodec.INSTANCE).getAsync();
        RFuture rFutureB = rBatch.getBucket("b", StringCodec.INSTANCE).getAsync();
        RFuture rFutureC = rBatch.getBucket("c", StringCodec.INSTANCE).getAsync();

        BatchResult batchResult = rBatch.execute();

        System.out.println(rFutureA.get());
        System.out.println(rFutureB.get());
        System.out.println(rFutureC.get());
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
