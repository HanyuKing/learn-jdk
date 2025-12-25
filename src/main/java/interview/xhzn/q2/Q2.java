package interview.xhzn.q2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Q2 {
    /*
        使用多线程实现一个简单的生产者消费者模型。
        创建 1 个主进程及 5 个线程，同时初始化 N 个（N <= 100）执行时间随机的任务。
        将任务随机分配给空闲的子线程进行处理，每个任务处理结束后通知主进程实时打印执行状态。
        所有任务执行完成后，所有子线程需正常退出后，程序再退出。
     */
    private BlockingQueue<Task> blockQueue = null;
    private AtomicInteger atomicInteger = new AtomicInteger();
    private volatile boolean isRunning = true;

    private int taskSize;
    public static void main(String[] args) throws Exception {
        Q2 q2 = new Q2();
        q2.start(5, 100);

        System.out.println("任务完成总数: " + q2.atomicInteger.get());
    }

    public void start(int threadCount, int taskSize) throws Exception {
        blockQueue = new LinkedBlockingQueue<>(taskSize);
        this.taskSize = taskSize;

        List<Consumer> consumers = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Consumer consumer = new Consumer();
            consumer.start();
            consumers.add(consumer);
        }


        for (int i = 0; i < taskSize; i++) {
            blockQueue.offer(new Task(i));
        }

        for (Consumer consumer : consumers) {
            consumer.join();
        }
    }

    public class Task {
        private int id;
        public Task(int id) {
            this.id = id;
        }
        public void doTask () {
            System.out.println("ThreadId: " + Thread.currentThread().getId() + ", TaskId: " + id + ", random time: " + ThreadLocalRandom.current().nextInt());
        }
    }

    public class Consumer extends Thread {
        @Override
        public void run() {
            while (isRunning || !blockQueue.isEmpty()) {
                Task task = null;
                try {
                    task = blockQueue.poll(1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (task != null) {
                    task.doTask();
                    int currTaskSize = atomicInteger.incrementAndGet();
                    if (currTaskSize >= taskSize) {
                        isRunning = false;
                    }
                }
            }
        }
    }
}
