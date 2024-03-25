package jol;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class CompressedRefs {

    static class MyClass {
        int x;
        public MyClass(int x) { this.x = x; }
        public int x() { return x; }
    }

    private MyClass o = new MyClass(42);

    @Benchmark
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public int access() {
        return o.x();
    }

    public static void main(String[] args) {
        out.println(VM.current().details());

        MyClass myClass = new MyClass(1);
        ClassLayout layout = ClassLayout.parseInstance(myClass);

        synchronized (myClass) {
            out.println(layout.toPrintable());
        }
    }
}