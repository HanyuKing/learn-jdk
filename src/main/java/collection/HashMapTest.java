package collection;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/28 15:46
 * @Description
 * @Version 1.0
 **/
public class HashMapTest {
    public static void main(String[] args) {

        Map<Object, Object> map = new HashMap<>();
        A a = new A(100);
        B b = new B(101);

        map.put(a, 100);

        System.out.println(map.get(b));
    }

    private static class A {
        private int value;

        public A(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            return true;
        }

        @Override
        public String toString() {
            return "value = " + this.value;
        }
    }

    private static class B {
        private int value;

        public B(int value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            return true;
        }

        @Override
        public String toString() {
            return "value = " + this.value;
        }
    }
}
