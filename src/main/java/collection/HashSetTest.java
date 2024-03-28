package collection;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/28 16:33
 * @Description
 * @Version 1.0
 **/
public class HashSetTest {
    public static void main(String[] args) {
        A a = new A(100);
        A aa = new A(100);

        System.out.println(a == aa); // false
        System.out.println(a.equals(aa)); // true

        Set<A> aSets = new HashSet<>();
        aSets.add(a);
        aSets.add(aa);

        // 要覆写hashCode
        System.out.println(aSets); // [value = 100, value = 100]
    }

    private static class A {
        private int value;

        public A(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return this.value == ((A) obj).value;
        }

        @Override
        public String toString() {
            return "value = " + this.value;
        }
    }
}
