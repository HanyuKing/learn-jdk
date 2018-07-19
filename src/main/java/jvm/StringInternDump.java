package jvm;

import java.lang.management.ManagementFactory;
import java.util.Random;

/**
 * @author Hanyu King
 * @since 2018-07-13 15:59
 */
public class StringInternDump {
    static final int MAX = 1000 * 10000;
    static final String[] arr = new String[MAX];

    public static void main(String[] args) throws Exception {
        Integer[] DB_DATA = new Integer[10];
        Random random = new Random(10 * 10000);
        for (int i = 0; i < DB_DATA.length; i++) {
            DB_DATA[i] = random.nextInt();
        }
        long t = System.currentTimeMillis();
        for (int i = 0; i < MAX; i++) {
            //arr[i] = new String(String.valueOf(DB_DATA[i % DB_DATA.length]));
            arr[i] = new String(String.valueOf(DB_DATA[i % DB_DATA.length])).intern();
        }


        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);

        System.out.println((System.currentTimeMillis() - t) + "ms");
        Thread.sleep(300000);
        System.gc();
    }
}
