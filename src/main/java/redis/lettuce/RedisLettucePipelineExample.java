package redis.lettuce;

import com.google.common.collect.Lists;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * https://github.com/redis/lettuce/wiki/Pipelining-and-command-flushing
 *
 */
public class RedisLettucePipelineExample {

    public static void main(String[] args) {
        // Create a Redis client
        RedisClient redisClient = RedisClient.create("redis://localhost:6379");

        // Establish a connection
        StatefulRedisConnection<String, String> connection = redisClient.connect();

        RedisAsyncCommands<String, String> commands = connection.async();

        // disable auto-flushing
        commands.setAutoFlushCommands(false);

        // perform a series of independent calls
        List<RedisFuture<?>> futures = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            futures.add(commands.set("key-" + i, "value-" + i));
            futures.add(commands.expire("key-" + i, 3600));
        }

        // write all commands to the transport layer
        commands.flushCommands();

        // synchronization example: Wait until all futures complete
        boolean result = LettuceFutures.awaitAll(5, TimeUnit.SECONDS,
                futures.toArray(new RedisFuture[futures.size()]));

        // later
        connection.close();

        // Close the connection and client
        connection.close();
        redisClient.shutdown();
    }
}
