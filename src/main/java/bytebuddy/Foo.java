package bytebuddy;

/**
 * @Author Hanyu.Wang
 * @Date 2023/3/6 19:21
 * @Description
 * @Version 1.0
 **/
public class Foo {
    public void println(String x) {
        System.out.println("Foo: " + x);
    }

    public void println(char x) {
        System.out.println("Foo: " + x);
    }

    public void println(int x) {
        System.out.println("Foo: " + x);
    }
}
