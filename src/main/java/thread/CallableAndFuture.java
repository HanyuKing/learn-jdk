package thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CallableAndFuture {
    public static void main(String[] args) {
        Callable<Integer> callable = new Callable<Integer>() {
            public Integer call() throws Exception {
                return new Random().nextInt(100);
            }
        };

        List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
        for(int i = 0; i < 10; i++) {
            FutureTask<Integer> future = new FutureTask<Integer>(callable);

            futureList.add(future);

            new Thread(future).start();
        }

        try {
            for(int i = 0; i < 10; i++) {
                System.out.println(futureList.get(i).get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}