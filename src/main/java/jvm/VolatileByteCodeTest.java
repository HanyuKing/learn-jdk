package jvm;

/**
 * @author wanghanyu
 * @create 2018-01-04 10:44
 *
 *  -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -Xcomp -XX:CompileCommand=compileonly,*VolatileByteCodeTest.increment
 */
public class VolatileByteCodeTest {

    int a = 2;
    volatile int b = 4;

    public int increment() {
        b = b + 5;
        a = a + 6;

        return a + b;
    }

    public synchronized int increment2() {
        synchronized (VolatileByteCodeTest.class) {
            a = a + 1;
            return a;
        }
    }

    public static void main(String[] args) {
        new VolatileByteCodeTest().increment2();
    }
}
