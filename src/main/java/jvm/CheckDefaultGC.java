package jvm;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

/**
 * https://stackoverflow.com/questions/39929758/ps-marksweep-is-which-garbage-collector
 */
public class CheckDefaultGC {
    public static void main(String[] args) {
        System.out.println("Get JDK Default GC for jdk"
                + System.getProperty("java.version") + " - "
                + System.getProperty("java.vm.name") + ":");
        for (GarbageCollectorMXBean gcBean : ManagementFactory.getGarbageCollectorMXBeans()) {
            System.out.println(gcBean.getName());
        }
    }
}