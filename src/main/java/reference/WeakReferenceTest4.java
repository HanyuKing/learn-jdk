package reference;

import java.lang.ref.WeakReference;

/**
 * @author wanghanyu
 * @create 2017-10-31 18:37
 */
public class WeakReferenceTest4 {
    public static void main(String[] args) {
        A a = new A();
        //WeakReference<A> reference = new WeakReference<A>(a);
        AA reference = new AA(a);
        a = null;
        System.gc();

        System.out.println(reference.get());
    }
}

class AA extends WeakReference<A> {

    public AA(A a) {
        super(a);
    }

    public A get(A a) {
        return super.get();
    }
}

class A {

}
