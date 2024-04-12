package juc.threadlocal;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ThreadLocalUnsafe implements Runnable {

    public static Number number = new Number(0);

    public void run() {
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {

        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        //每个线程计数加一
        number = new Number((number.getNum() + 1)); // number.setNum(number.getNum() + 1);
        //将其存储到ThreadLocal中
        value.set(number);
        //输出num值
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "=" + value.get().getNum() + "\n");
    }

    public static ThreadLocal<Number> value = new ThreadLocal<Number>() {
    };

    static CyclicBarrier cyclicBarrier = new CyclicBarrier(50, new Runnable() {
        @Override
        public void run() {
            System.out.println("开始。。。。。。。。");
        }
    });

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

        for (int i = 0; i < 50; i++) {
            new Thread(new ThreadLocalUnsafe()).start();
        }
    }

}


@Data
@AllArgsConstructor
class Number {

    private int num;

}