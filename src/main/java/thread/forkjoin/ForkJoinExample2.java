package thread.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/23 16:43
 * @Description https://www.panziye.com/java/3575.html
 *
 *  2、有返回值情况
 *
 * @Version 1.0
 **/
public class ForkJoinExample2 {
    public static void main(String[] args) throws Exception {
        // 创建ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();
        // 提交任务，求和 1到20
        ForkJoinTask<Integer> task = pool.submit(new SumTask(1, 20));
        // 获取计算结果
        int result = task.get();
        System.out.println("并行计算 1-20 累加值：" + result);
        pool.awaitTermination(2, TimeUnit.SECONDS);
        pool.shutdown();
    }
}

class SumTask extends RecursiveTask<Integer> {
    private static final long serialVersionUID = 1L;
    // 拆分阈值
    private static final int THRESHOLD = 5;
    // 开始值
    private int start;
    // 结束值
    private int end;

    public SumTask(int start, int end) {
        super();
        this.start = start;
        this.end = end;
    }

    // 重写compute核心方法
    @Override
    protected Integer compute() {
        // 当结束值比起始值小于阈值时，直接计算累加值
        if (end - start <= THRESHOLD) {
            int result = 0;
            for (int i = start; i <= end; i++) {
                result += i;
            }
            return result;
        } else {
            // 大于阈值时，按数值区间平均拆分为两个子任务，进行两个任务的累加值汇总
            int middle = (start + end) / 2;
            SumTask leftTask = new SumTask(start, middle);
            SumTask rightTask= new SumTask(middle + 1, end);
            leftTask.fork();
            rightTask.fork();
            // 合并子任务结果
            int sum1 = leftTask.join();
            int sum2 = rightTask.join();
            System.out.println("sum1="+sum1+",sum2="+sum2);
            return sum1 + sum2;
        }
    }
}