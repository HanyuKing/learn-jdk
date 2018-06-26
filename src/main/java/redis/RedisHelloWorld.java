package redis;

import redis.clients.jedis.Jedis;

/**
 * @author Hanyu King
 * @since 2018-06-25 17:43
 */
public class RedisHelloWorld {
    private static Jedis jedis = new Jedis("47.254.28.50", 6379);
    static {
        jedis.auth("hanyuking_redis");
    }
    public static void main(String[] args) {
        jedis.set("name", "hanyu");

        System.out.println(jedis.get("name"));
    }
}
