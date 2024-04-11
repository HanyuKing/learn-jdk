package juc.phaser;

import java.util.concurrent.Phaser;
public class PhaserTest extends Phaser {

    @Override
    protected boolean onAdvance(int phase, int registeredParties) {    //在每个阶段执行完成后回调的方法

        switch (phase) {
            case 0:
                return daoqi();
            case 1:
                return yingping();
            case 2:
                return zhucai();
            case 3:
                return tiandian();
            default:
                return true;
        }

    }

    private boolean tiandian() {
        System.out.println("甜点点完了！人数：" + getRegisteredParties());
        return false;
    }

    private boolean zhucai() {
        System.out.println("主菜点完了！人数：" + getRegisteredParties());
        return false;
    }

    private boolean yingping() {
        System.out.println("饮料点完咯！人数：" + getRegisteredParties());
        return false;
    }

    private boolean daoqi() {
        System.out.println("人都到齐咯,开始点菜，人数：" + getRegisteredParties());
        return false;
    }

    static class Runner implements Runnable {

        private Phaser phaser;

        public Runner(Phaser phaser) {
            this.phaser = phaser;
        }

        @Override
        public void run() {

            System.out.println("同学-"+Thread.currentThread().getName()+":到达");
            /**
             * 执行这个方法的话会等所有的选手都完成了之后再继续下面的方法
             */
            phaser.arriveAndAwaitAdvance();

            System.out.println("同学-"+Thread.currentThread().getName()+":点了一杯杨枝甘露");
            phaser.arriveAndAwaitAdvance();

            System.out.println("同学-"+Thread.currentThread().getName()+":点了一个鱼香肉丝");
            phaser.arriveAndAwaitAdvance();

            System.out.println("同学-"+Thread.currentThread().getName()+":点了一个巧克力圣代");
            phaser.arriveAndAwaitAdvance();

        }
    }

    public static void main(String[] args) {
        int runnerNum = 4;

        PhaserTest phaser = new PhaserTest();
        /**
         * 注册一次表示phaser维护的线程个数
         */
        phaser.register();
        for (int i = 0; i < runnerNum;  i++ ) {
            /**
             * 注册一次表示phaser维护的线程个数
             */
            phaser.register();
            new Thread(new Runner(phaser)).start();

        }
        /**
         * 后续阶段主线程就不参加了
         */
        phaser.arriveAndDeregister();
    }
}

