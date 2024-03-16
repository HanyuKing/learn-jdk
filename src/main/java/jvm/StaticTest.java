package jvm;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/16 16:53
 * @Description
 * @Version 1.0
 **/
public class StaticTest {
    static class Parent {
        public static int A = 1;
        static {
            A = 2;
        }
    }
    static class Sub extends Parent {
        public static int B = A;
    }

    public static void main(String[] args) {
        System.out.println(Sub.B);
    }
}
