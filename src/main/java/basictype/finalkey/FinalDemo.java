package basictype.finalkey;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

public class FinalDemo {
    private int a;  //普通域
    private final int b; //final域
    private FinalDemo finalDemo;

    public FinalDemo() {
        a = 1; // 1. 写普通域
        b = 2; // 2. 写final域
    }

    public void writer() {
        //LockSupport.parkNanos(10000);
        finalDemo = new FinalDemo();
    }

    public void reader() {
        try {
            FinalDemo demo = finalDemo; // 3.读对象引用
            int a = demo.a;    //4.读普通域
            int b = demo.b;    //5.读final域

            if (a == 0) {
                System.out.println(a + ", " + b);
            }
        } catch (NullPointerException e) {
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10000000; i++) {
            FinalDemo demo = new FinalDemo();
            Thread t1 = new Thread(demo::writer);
            Thread t2 = new Thread(demo::reader);
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        }
    }
}
