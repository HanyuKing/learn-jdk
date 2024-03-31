package reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/31 23:19
 * @Description
 * @Version 1.0
 **/
public class PhantomReferenceTest {
    public static void main(String[] args) {
        String str = "aaa";
        A<String> a = new A<String>(str, new ReferenceQueue<>());
        System.out.println(a.get());

        a = null;

        System.gc();

    }

    static class A<T> extends PhantomReference<T> {

        /**
         * Creates a new phantom reference that refers to the given object and
         * is registered with the given queue.
         *
         * <p> It is possible to create a phantom reference with a <tt>null</tt>
         * queue, but such a reference is completely useless: Its <tt>get</tt>
         * method will always return null and, since it does not have a queue, it
         * will never be enqueued.
         *
         * @param referent the object the new phantom reference will refer to
         * @param q        the queue with which the reference is to be registered,
         *                 or <tt>null</tt> if registration is not required
         */
        public A(T referent, ReferenceQueue q) {
            super(referent, q);
        }

        @Override
        protected void finalize() throws Throwable {
            System.out.println("finalize....");
            super.finalize();
        }
    }
}
