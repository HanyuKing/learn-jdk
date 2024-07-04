package zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @Author Hanyu.Wang
 * @Date 2024/7/4 16:06
 * @Description
 * @Version 1.0
 **/
public class CuratorClientTest {
    private CuratorFramework client;

    @Before
    public void before () {
        //重试策略，初试时间 3 秒，重试 3 次
        RetryPolicy policy = new ExponentialBackoffRetry(3000, 3);
        //通过工厂创建 Curator
        client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .connectionTimeoutMs(2000)
                .sessionTimeoutMs(2000)
                .retryPolicy(policy).build();
        //开启连接
        client.start();
        System.out.println("zk 初始化完成...");
    }

    @After
    public void after() {
        client.close();
        System.out.println("zk 已关闭...");
    }

    @Test
    public void testDistributeLock() throws Exception {

        InterProcessSemaphoreMutex lock = new InterProcessSemaphoreMutex(client, "/test/lock");

        if(lock.acquire(10, TimeUnit.SECONDS)) {
            try {
                /*do something*/
                System.out.println(Thread.currentThread().getId() + ":" + "I get the lock.");
            } finally {
                lock.release();
            }
        }
    }
}
