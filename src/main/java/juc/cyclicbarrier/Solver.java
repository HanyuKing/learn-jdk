package juc.cyclicbarrier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.LockSupport;

public class Solver {
 final int N;
 final float[][] data;
 final CyclicBarrier barrier;

 class Worker implements Runnable {
  int myRow;
  Worker(int row) { myRow = row; }
  public void run() {
   while (!done()) {
    try {
     barrier.await();
    } catch (InterruptedException ex) {
     return;
    } catch (BrokenBarrierException ex) {
     return;
    }
    processRow(myRow);
   }
  }

  private boolean done() {
   LockSupport.parkNanos(5_000_000_000L);
   return false;
  }

  private void processRow(int row) {
   System.out.println("processRow: " + row);
  }
 }

 public Solver(float[][] matrix) throws InterruptedException {
  data = matrix;
  N = matrix.length;
  Runnable barrierAction = () -> System.out.println("mergeRows(...);");
  barrier = new CyclicBarrier(N, barrierAction);

  List<Thread> threads = new ArrayList<Thread>(N);
  for (int i = 0; i < N; i++) {
   Thread thread = new Thread(new Worker(i));
   threads.add(thread);
   thread.start();
  }

  // wait until done
  for (Thread thread : threads)
   thread.join();
 }

 public static void main(String[] args) throws InterruptedException {
  float[][] matrix = new float[][] {
          {1, 2, 3, 4},
          {5, 6, 7, 8},
          {9, 10, 11, 12},
  };
  new Solver(matrix);
 }
}