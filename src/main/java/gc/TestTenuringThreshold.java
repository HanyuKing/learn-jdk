package gc;

/**
 * -verbose:gc -Xms20M -Xmx20M -Xmn10M  -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1
 * -verbose:gc -Xms20M -Xmx20M -Xmn10M  -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15
 * @author wanghanyu
 * @create 2017-10-29 15:54
 */
public class TestTenuringThreshold {
    public static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] allocation1, allocation2, allocation3;
        allocation1 = new byte[_1MB / 4];
        allocation2 = new byte[4 * _1MB];

        // 什么时候进入老年代取决于 XX:MaxTenuringThreshold
       allocation3 = new byte[4 * _1MB];
        allocation3 = null;
        allocation3 = new byte[4 * _1MB];
    }
}
