package collection;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/28 14:57
 * @Description
 * @Version 1.0
 **/
public class QueueTest {

    @Test
    public void testArrayDequeAsQueue() {
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);

        deque.add(4);


        while (!deque.isEmpty()) {
            System.out.println(deque.removeFirst());
        }

    }

    @Test
    public void testLinkedListAsQueue() {

    }
}
