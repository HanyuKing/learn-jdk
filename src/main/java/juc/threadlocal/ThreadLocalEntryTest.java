package juc.threadlocal;

import java.lang.ref.WeakReference;

/**
 * @author wanghanyu
 * @create 2017-10-30 23:30
 */
public class ThreadLocalEntryTest {

    public static void main(String[] args) {
        ThreadLocal<Object> threadLocal = new ThreadLocal<Object>();
        Object obj = new Object();

        ThreadLocalEntryTest.Entry entry = new Entry(threadLocal, obj);
        obj = null;
       // threadLocal = null;
        System.gc();
        System.out.println(entry.get());
    }

    static class Entry extends WeakReference<ThreadLocal<?>> {
        /** The value associated with this ThreadLocal. */
        Object value;
        //ThreadLocal<?> key;


        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            //key = k;
            value = v;
        }
    }
}
