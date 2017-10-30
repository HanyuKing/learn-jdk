package gc;

import java.util.WeakHashMap;

/**
 * @author wanghanyu
 * @create 2017-10-30 22:12
 */
public class WeakHashMapTest {
    public static void main(String[] args) {
        WeakHashMap<Object, Object> weakHashMap = new WeakHashMap<Object, Object>();
        weakHashMap.put(new Object(), new Object());
       // System.out.println(weakHashMap.keySet());
        System.gc();
        System.out.println(weakHashMap.entrySet());
        System.out.println(weakHashMap.size());
    }
}
