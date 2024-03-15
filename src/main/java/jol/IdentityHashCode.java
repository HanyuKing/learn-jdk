package jol;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@Threads(Threads.MAX)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
public class IdentityHashCode {
    Object o = new Object();

    @Benchmark
    public void cold(Blackhole bh) {
        Object lo = new Object();
        bh.consume(lo);
        bh.consume(lo.hashCode());
    }

    @Benchmark
    public void warm(Blackhole bh) {
        Object lo = o;
        bh.consume(lo); // for symmetry
        bh.consume(lo.hashCode());
    }
}