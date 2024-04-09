package juc.semaphore;

import java.util.concurrent.Semaphore;

public class TestSemaphore {

  // 这里将信号量设置成了0
  private static Semaphore sem = new Semaphore(0);

  private static class Thread1 extends Thread {
    
    public void run() {
      // 获取锁
      sem.acquireUninterruptibly();
    }
  }

  private static class Thread2 extends Thread {
    
    public void run() {
      // 释放锁
      sem.release();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Thread t1 = new Thread1();
    Thread t2 = new Thread1();
    Thread t3 = new Thread2();
    Thread t4 = new Thread2();

    t1.start();
    Thread.sleep(100);
    System.out.println(sem.availablePermits());

    t2.start();
    Thread.sleep(100);
    System.out.println(sem.availablePermits());

    t3.start();
    Thread.sleep(100);
    System.out.println(sem.availablePermits());

    t4.start();
    Thread.sleep(100);
    System.out.println(sem.availablePermits());

    t1.join();
    t2.join();
    t3.join();
    t4.join();

  }
}