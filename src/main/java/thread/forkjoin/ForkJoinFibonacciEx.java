package thread.forkjoin;

import java.time.LocalTime;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static java.time.temporal.ChronoUnit.MILLIS;

class Fibonacci extends RecursiveTask<Long>{

    long num;
    Fibonacci(long n){
        num = n;
    }

    @Override
    protected Long compute() {
        if(num <= 1)
            return num;

        Fibonacci fib1 = new Fibonacci(num - 1);
        fib1.fork();

        Fibonacci fib2 = new Fibonacci(num - 2);

        return fib2.compute() + fib1.join();
    }
}

public class ForkJoinFibonacciEx {

    public static void main(String[] arg){
        long n = 30;
        long t1 = System.currentTimeMillis();
        long res = fibonacci(n);
        long t2 = System.currentTimeMillis();
        System.out.println("==========Sequential===========");
        System.out.println("Time in sequential = " + (t2 - t1)+" ms. Result: " + res + "\n");

        System.out.println("==========ForkJoin===========");

        LocalTime before = LocalTime.now();
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available core: " + processors);

        ForkJoinPool pool = new ForkJoinPool(processors);
        Fibonacci fib = new Fibonacci(n);
        pool.invoke(fib);
        System.out.println("Inside ForkJoin for number 50:  " + fib.getRawResult());

        LocalTime after = LocalTime.now();
        System.out.println("Total time taken: " + MILLIS.between(before, after));
    }

    private static Long fibonacci(Long n){
        if (n <= 1){
            return n;
        }else{
            return fibonacci(n-2) + fibonacci(n-1);
        }
    }
}