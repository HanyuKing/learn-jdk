package juc;

public class VolatileTest {

    static int i = 0;
    static volatile boolean flag = false;

    //Thread A
    public static void write(){
        i = 2;              //1
        flag = true;        //2
    }

    //Thread B
    public static void read(){
        if(flag){                                   //3
            System.out.println("---i = " + i);      //4
        }
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                read();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                write();
            }
        }).start();
    }
}