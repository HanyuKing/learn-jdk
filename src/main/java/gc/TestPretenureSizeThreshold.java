package gc;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/15 14:36
 * @Description
 * @Version 1.0
 **/
public class TestPretenureSizeThreshold {
    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     * -XX:PretenureSizeThreshold=3145728
     */

    /**
     * 注意 -XX：PretenureSizeThreshold参数只对Serial和ParNew两款新生代收集器有效，HotSpot
     * 的其他新生代收集器，如Parallel Scavenge并不支持这个参数。如果必须使用此参数进行调优，可考虑
     * ParNew加CMS的收集器组合
     */
    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] allocation;
        allocation = new byte[4 * _1MB]; //直接分配在老年代中

    }
}
