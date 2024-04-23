package thread.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/23 16:43
 * @Description https://www.panziye.com/java/3575.html
 *
 *  1、无返回值情况
 *
 * @Version 1.0
 **/
public class ForkJoinExample1 {
    public static void main(String[] args) throws Exception {
        // 创建ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();
        // 提交任务，这里打印1到20
        pool.submit(new PrintTask(1, 20));
        // 开始2秒时如果任务未完成一直阻塞让submit的任务先执行完
        pool.awaitTermination(2, TimeUnit.SECONDS);
        // 关闭任务
        pool.shutdown();
    }
}

class PrintTask extends RecursiveAction {

    private static final long serialVersionUID = 1L;
    // 拆分阈值
    private static final int THRESHOLD = 5;
    // 开始值
    private int start;
    // 结束值
    private int end;

    // 构造
    public PrintTask(int start, int end) {
        super();
        this.start = start;
        this.end = end;
    }

    // 重写compute核心方法
    @Override
    protected void compute() {
        //当结束值比起始值小于阈值时，直接打印该区间的值
        if (end - start < THRESHOLD) {
            for (int i = start; i <= end; i++) {
                System.out.println(Thread.currentThread().getName() + ", i = " + i);
            }
        } else {
            // 大于阈值时，按数值区间平均拆分为两个子任务
            int middle = (start + end) / 2;
            PrintTask leftTask = new PrintTask(start, middle);
            PrintTask rightTask= new PrintTask(middle + 1, end);
            leftTask.fork();
            rightTask.fork();
        }
    }
}
