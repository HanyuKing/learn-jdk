package queue;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.PriorityQueue;

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
}
