package gc;

import java.util.ArrayList;
import java.util.List;

public class StackOOMTest {
    private static final List<Thread> threadList = new ArrayList<>();

    /**
     * 384k
     * @param args
     */
    public static void main(String[] args) {
        while (true) {
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000000); // 让线程保持运行状态
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            thread.start();
            threadList.add(thread);
            System.out.println("Created thread count: " + threadList.size());
        }
    }
}
