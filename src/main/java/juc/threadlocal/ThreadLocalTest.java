package juc.threadlocal;

import org.junit.Test;
import reflect.ReflectUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadLocal 变量如果不及时remove会导致GC频繁
 *
 * @author wanghanyu
 * @create 2017-10-30 22:56
 */
public class ThreadLocalTest {
    private static ThreadLocal<String> threadLocalStatic = new ThreadLocal<>();
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        String str = "Hanyu";
        threadLocalStatic.set(str);
        str = null;
        //threadLocalStatic = null;
        System.gc();

        // System.out.println(local.get());

        Object map = ReflectUtil.getFieldValue(Thread.currentThread(), "threadLocals");

        Object table = ReflectUtil.getFieldValue(map, "table");

        System.out.println(table); // key == null

        threadLocalStatic.set("Hanyu2");

        System.out.println(table); // key == null removed;

    }

//    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//        String str = "Hanyu";
//
//        ThreadLocal<String> local = new ThreadLocal<String>();
//
//        local.set(str);
//        str = null;
//        local = null;
//        System.gc();
//
//        // System.out.println(local.get());
//
//        Object map = ReflectUtil.getFieldValue(Thread.currentThread(), "threadLocals");
//
//        Object table = ReflectUtil.getFieldValue(map, "table");
//
//        System.out.println(table); // key == null
//
//        ThreadLocal<String> local2 = new ThreadLocal<String>();
//        local2.set("Hanyu");
//
//        System.out.println(table); // key == null removed;
//
//    }

    @Test
    public void testNullKey() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        final Thread[] t = {null};
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                ThreadLocal<ThreadLocalTest> threadLocal = new ThreadLocal<>();
                ThreadLocalTest object = new ThreadLocalTest();
                threadLocal.set(object);
                t[0] = Thread.currentThread();
            }
        });

        Thread.sleep(100);

        Object map = ReflectUtil.getFieldValue(t[0], "threadLocals");
        Object table = ReflectUtil.getFieldValue(map, "table");


        System.gc();

        map = ReflectUtil.getFieldValue(t[0], "threadLocals");
        table = ReflectUtil.getFieldValue(map, "table");
    }
}
