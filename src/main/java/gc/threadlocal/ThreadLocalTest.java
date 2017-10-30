package gc.threadlocal;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * ThreadLocal 变量如果不及时remove会导致GC频繁
 *
 * @author wanghanyu
 * @create 2017-10-30 22:56
 */
public class ThreadLocalTest {
    public static void main(String[] args) {
        String str = new String("Hanyu");
        //ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
        //WeakReference<String> weakReference = new WeakReference<String>(str, refQueue);
        ThreadLocal<String> local = new ThreadLocal<String>();
        //MyThreadLocal myThreadLocal = new MyThreadLocal();
        local.set(str);
        str = null;
        System.gc();
        //refQueue.poll();
        System.out.println(local.get());
        //System.out.println(myThreadLocal.weakReference.get());
       // System.out.println(weakReference.get());
    }
}

class MyThreadLocal {
    WeakReference<String> weakReference = new WeakReference<String>("King");
}

