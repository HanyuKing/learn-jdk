package reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @author wanghanyu
 * @create 2017-10-30 20:43
 */
public class SoftReferenceTest {
    public static void main(String[] args) throws Exception {
        Object obj = new Object();
        ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
        WeakReference<Object> softRef = new WeakReference<Object>(obj, refQueue);
        System.out.println("1-> " + softRef.get()); // java.lang.Object@f9f9d8
        System.out.println("2-> " + refQueue.poll());// null

        // 清除强引用,触发GC
        obj = null;
        System.gc();

        System.out.println("3-> " + softRef.get());

        Thread.sleep(2000);
        System.out.println("4-> " + refQueue.poll());
    }
}
