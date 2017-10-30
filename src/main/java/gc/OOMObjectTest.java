package gc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanghanyu
 * @create 2017-10-29 17:15
 */
public class OOMObjectTest {
    public static void main(String[] args) throws Exception {
        fillHeap(1000);
        System.gc();
    }

    private static class OOMObject {
        public byte[] placeholder = new byte[64 * 1024];
    }

    private static void fillHeap(int num) throws Exception {
        List<OOMObject> list = new ArrayList<OOMObject>();
        for(int i = 0; i < num; i++) {
            Thread.sleep(50);
            list.add(new OOMObject());
        }

    }
}



