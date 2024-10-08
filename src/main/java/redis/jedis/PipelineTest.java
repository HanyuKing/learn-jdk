package redis.jedis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/8 16:47
 * @Description
 * @Version 1.0
 **/
public class PipelineTest {
    private Jedis jedis = null;

    @Before
    public void init() {
        this.jedis = new Jedis("localhost", 6379);
    }

    @After
    public void after() {
        jedis.shutdown();
    }

    @Test
    public void testPipeline() {
        Pipeline pipeline = jedis.pipelined();

        pipeline.set("key1", "value1");
        pipeline.set("key2", "value2");
        pipeline.set("key3", "value3");

        pipeline.sync();

        System.out.println(jedis.get("key1"));
        System.out.println(jedis.get("key2"));
        System.out.println(jedis.get("key3"));
    }
}
