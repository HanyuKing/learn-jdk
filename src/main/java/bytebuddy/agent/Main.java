package bytebuddy.agent;

import bytebuddy.Foo;

/**
 * @Author Hanyu.Wang
 * @Date 2024/8/7 15:58
 * @Description
 * @Version 1.0
 **/
public class Main {
    public static void main(String[] args) {
        Foo foo = new Foo();
        foo.println("foo");
    }
}
