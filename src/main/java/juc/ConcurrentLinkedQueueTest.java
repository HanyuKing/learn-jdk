package juc;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author Hanyu.Wang
 * @Date 2024/2/4 14:18
 * @Description
 *  https://bugs.java.com/bugdatabase/view_bug?bug_id=8054446
 *
 * @Version 1.0
 **/
public class ConcurrentLinkedQueueTest {
    public static void main(String[] args) {
        ConcurrentLinkedQueueTest test = new ConcurrentLinkedQueueTest();
        test.testOutOfMemory();
    }

    public void testOutOfMemory(){
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
        String a="a";
        String b="b";
        queue.offer(a);
        for(int i=0;;i++){
            if(i % 1024 == 0) {
                System.out.println("i = "+i);
            }
            queue.offer(b);
            queue.remove(b);

        }
    }
/***********************before*********************************/
    /*
    public boolean remove(Object o) {
        if (o != null) {
            Node < E > pred = null;
             for (Node<E> p = first(); p != null; p = succ(p)) {
                    E item = p.item;
                     if (item != null &&
                            o.equals(item) &&
                            casItem(p, item, null)) {
                        Node < E > next = succ(p);
                         if (pred != null && next != null)
                            casNext(pred, p, next);
                            return true;
                        pred = p;
                    }
                }
                return false;
            }
        }
    }
    */
/**********************************after*******************************/
    /*
    public boolean remove(Object o) {
        if (o != null) {
                Node < E > next, pred = null;
                 for (Node<E> p = first(); p != null; pred = p, p = next) {
                         boolean removed = false;
                         E item = p.item;
                         if (item != null) {
                             if (!o.equals(item)) {
                                next = succ(p);
                                 continue;
                            }
                            removed = casItem(p, item, null);
                         }
                         next = succ(p);
                         if (pred != null && next != null) // unlink
                            casNext(pred, p, next);
                         if (removed)
                             return true;
                    }
                }
                return false;
            }
        }
    }
    */
}
