package jol;

import org.junit.Test;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/5 14:14
 * @Description
 * @Version 1.0
 **/
public class HashCode {
    class A {

        @Override
        public int hashCode() {
            return 123;
        }
    }

    @Test
    public void testSystemIdentityHashCode() {
        A a = new A();

        System.out.println(a.hashCode());

        System.out.println(System.identityHashCode(a));
    }
    public static void main(String[] args) {
        Object o = new Object();

        synchronized (o) {
            System.out.println(o.hashCode());
        }
    }
}
