package gc;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * http://www.iteye.com/topic/587995
 *
 * @author wanghanyu
 * @create 2017-10-30 20:53
 */
public class WeakReferenceTest3 {
    public static void main(String[] args) {
        List<WeakHashMap<Object, Object>> maps = new ArrayList<WeakHashMap<Object, Object>>();
        Object o = new Object();
        for (int i = 0; i < 1000; i++) {
            WeakHashMap<Object, Object> d = new WeakHashMap<Object, Object>();
            d.put(new byte[4000][4000], new Object()); // 退化为链表,GC不一定会回收
            maps.add(d);
            System.gc();
            System.err.println(d.size());

//            for (int j = 0; j < i; j++) {
//                System.err.println(j+  " size  " + maps.get(j).size());
//           }
        }

    }
}
