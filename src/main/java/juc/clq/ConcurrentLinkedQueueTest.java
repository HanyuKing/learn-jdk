package juc.clq;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueTest {

    @Test
    public void test1() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>(list);

        System.out.println(queue);
    }

    @Test
    public void test2() {
        List<Integer> list = Arrays.asList();
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>(list);

        System.out.println(queue);
    }
}
