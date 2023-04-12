package jvm;

import org.junit.Test;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Author Hanyu.Wang
 * @Date 2023/4/11 19:43
 * @Description
 * @Version 1.0
 **/
public class JVMConnector {

    private Set<String> youngGcAlgorithm = new LinkedHashSet<String>() {
        private static final long serialVersionUID = -2953196532584721351L;

        {
            add("Copy");
            add("ParNew");
            add("PS Scavenge");
            add("G1 Young Generation");
        }
    };

    private Set<String> oldGcAlgorithm = new LinkedHashSet<String>() {
        private static final long serialVersionUID = -8267829533109860610L;

        {
            add("MarkSweepCompact");
            add("PS MarkSweep");
            add("ConcurrentMarkSweep");
            add("G1 Old Generation");
        }
    };

    @Test
    public void printGcCollect() {
        doPrintGcCollect();

        System.gc();

        doPrintGcCollect();
    }

    public void doPrintGcCollect() {
        long gcCount = 0;
        long gcTime = 0;

        for (final GarbageCollectorMXBean garbageCollector : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcTime += garbageCollector.getCollectionTime();
            gcCount += garbageCollector.getCollectionCount();
            String gcAlgorithm = garbageCollector.getName();

            System.out.println("gcTime: " + gcTime + ", gcCount: " + gcCount + ", gcAlgorithm: " + gcAlgorithm);
        }
    }
}
