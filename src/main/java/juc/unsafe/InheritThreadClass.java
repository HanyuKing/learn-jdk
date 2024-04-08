package juc.unsafe;

import java.util.concurrent.atomic.AtomicInteger;

public class InheritThreadClass extends Thread{
    // private static volatile int a = 0;
    private static MyAtomicInteger a = new MyAtomicInteger(0);
    @Override
    public void run() {
        for(int i = 0; i < 10000; i++){
            // a++;
            a.getAndUpdate(c -> c + 1);
        }
    }
    public static void main(String[] args) throws Exception {
        InheritThreadClass[] threads = new InheritThreadClass[200];
        for(int i=0; i < 100; i++){
            threads[i] = new InheritThreadClass();
            threads[i].start();
        }
        //等待所有子线程结束
        for (int i = 0; i < 100; i++) {
            threads[i].join();
        }
        
        //这段代码会在所有子线程运行完毕之后执行
        System.out.println(a.get());  //(1)
    }
}