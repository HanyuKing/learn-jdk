package juc.atomic;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/3 14:28
 * @Description
 * @Version 1.0
 **/


import java.util.concurrent.atomic.AtomicInteger;

/**
 * -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -Xcomp -XX:CompileCommand=compileonly,*VolatileByteCodeTest.increment
 */
public class AtomicByteCodeTest {

    private AtomicInteger atomicInteger = new AtomicInteger();

    public void increment() {
        atomicInteger.incrementAndGet();
    }

    public static void main(String[] args) {
        AtomicByteCodeTest atomicByteCodeTest = new AtomicByteCodeTest();
        atomicByteCodeTest.increment();

        System.out.println(atomicByteCodeTest.atomicInteger.get());
    }
}
