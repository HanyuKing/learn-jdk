package juc.thread;

import jdk.nashorn.internal.runtime.options.Options;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations=3, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations=3,time = 5)
@Threads(4)
@Fork(1)
@State(Scope.Benchmark)
public class Myclass {
   Random random = new Random();
   ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

   @Benchmark
   public int measureRandom(){
       return random.nextInt();
   }
   @Benchmark
   public int threadLocalmeasureRandom(){
       return threadLocalRandom.nextInt();
   }
    public static void main(String[] args) throws RunnerException {
        org.openjdk.jmh.runner.options.Options opt = new OptionsBuilder()
                .include(Myclass.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .measurementIterations(2)
                .build(); // Changed from .build() to .build()

        new Runner(opt).run();
    }
}
