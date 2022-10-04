package queue;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Author Hanyu.Wang
 * @Date 2022/9/4 17:32
 * @Description
 * @Version 1.0
 **/
public class TestPriorityQueue {

    @Test
    public void testAdd() {
        PriorityQueue pq = new PriorityQueue();
        pq.add(1);
        pq.add(2);
        pq.add(3);
        pq.add(4);
        pq.add(1);
        System.out.println(Lists.newArrayList(pq.toArray()));


    }

    @Test
    public void testTimer() throws InterruptedException {
        System.out.println(System.currentTimeMillis());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
            }
        }, 1500);
        timer.cancel();
        Thread.sleep(2000);
    }

    @Test
    public void testScheduledExecutorService() throws Exception {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);
        System.out.println(System.currentTimeMillis());
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
            }
        }, 1000, TimeUnit.MILLISECONDS);

        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
            }
        }, 1100, TimeUnit.MILLISECONDS);

        Thread.sleep(2000);
    }
}
