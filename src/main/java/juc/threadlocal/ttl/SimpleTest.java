package juc.threadlocal.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import org.junit.Test;

/**
 * @author hanyu.wang
 * @version 1.0
 * @date 2025/7/25
 */
public class SimpleTest {
    @Test
    public void testInheritableThreadLocal() {
        InheritableThreadLocal<String> context = new InheritableThreadLocal<>();

        // =====================================================

        // 在父线程中设置
        context.set("value-set-in-parent");

        // =====================================================

        // 在子线程中可以读取，值是"value-set-in-parent"
        String value = context.get();
        System.out.println("parent: " + value);

        new Thread(() -> {
            while (true) {
                // 在子线程中可以读取，值是"value-set-in-parent"
                System.out.println("child: " + context.get());
                context.set("value-set-in-child");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        int i = 1;
        while (true) {
            context.set("value-set-in-parent: " + i++);
            System.out.println("parent: " + context.get());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testTransmittableThreadLocal() {
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

        // =====================================================

        // 在父线程中设置
        context.set("value-set-in-parent");

        // =====================================================

        // 在子线程中可以读取，值是"value-set-in-parent"
        String value = context.get();
        System.out.println("parent: " + value);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // 在子线程中可以读取，值是"value-set-in-parent"
                    System.out.println("child: " + context.get());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        // 额外的处理，生成修饰了的对象ttlRunnable
        Runnable ttlRunnable = TtlRunnable.get(task);
        new Thread(ttlRunnable).start();

        int i = 1;
        while (true) {
            context.set("value-set-in-parent: " + i++);
            System.out.println("parent: " + context.get());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
