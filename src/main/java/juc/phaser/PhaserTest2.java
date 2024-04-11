package juc.phaser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Phaser;

public class PhaserTest2 {

    public static void main(String[] args) throws IOException {
        Phaser phaser = new Phaser(1);       // 注册主线程,当外部条件满足时,由主线程打开开关
        for (int i = 0; i < 10; i++) {
            phaser.register();                      // 注册各个参与者线程
            new Thread(new Task2(phaser), "Thread-" + i).start();
        }

        // 外部条件:等待用户输入命令
        System.out.println("Press ENTER to continue");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();

        // 打开开关
        phaser.arriveAndDeregister();
        System.out.println("主线程打开了开关");
    }
}

class Task2 implements Runnable {
    private final Phaser phaser;

    Task2(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        int i = phaser.arriveAndAwaitAdvance();     // 等待其它参与者线程到达

        // do something
        System.out.println(Thread.currentThread().getName() + ": 执行完任务，当前phase =" + i + "");
    }
}