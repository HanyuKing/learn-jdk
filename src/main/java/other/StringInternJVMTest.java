package other;

import java.lang.management.ManagementFactory;

public class StringInternJVMTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
        String s = "HanyuKing";
        String s2 = new String("Test");
    }
}
