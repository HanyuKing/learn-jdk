package reference;

import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Finalizer 线程去扫描 reference queue，把 reference queue 中的 Entry poll 下来，与table比较，并删除table指定节点
 * Reference Handler 线程enqueue
 *
 * @author wanghanyu
 * @create 2017-10-30 22:12
 */
public class WeakHashMapTest {
    public static void main(String[] args) throws Exception {
        Map<String, String> weakHashMap = new WeakHashMap<String, String>(); // HashMap
        String key1 = new String("Hanyu1");
        String val1 = new String("King1");
        String key2 = new String("Hanyu2");
        String val2 = new String("King2");
        weakHashMap.put(key1, val1);
        weakHashMap.put(key2, val2);
       // System.out.println(weakHashMap.keySet());
        key1 = null;
        //key2 = null;
        System.gc();
        System.out.println(weakHashMap.keySet());
        System.out.println(weakHashMap.entrySet());

//        Field tableField = weakHashMap.getClass().getDeclaredField("table");
//        Field sizeField = weakHashMap.getClass().getDeclaredField("size");
//        tableField.setAccessible(true);
//        sizeField.setAccessible(true);
//        Object[] table = (Object[]) tableField.get(weakHashMap);
//        int size = sizeField.getInt(weakHashMap);
//
//        System.out.println("size->" + size);

        //System.out.println(weakHashMap.entrySet());
//        for(int i = 0; i < table.length; i++) {
//            Object entry = table[i];
//            if(entry != null) {
//                Field valField = entry.getClass().getDeclaredField("value");
//                Field keyField = entry.getClass().getSuperclass().getSuperclass().getDeclaredField("referent");
//                Field queueField = entry.getClass().getSuperclass().getSuperclass().getDeclaredField("queue");
//                valField.setAccessible(true);
//                keyField.setAccessible(true);
//                queueField.setAccessible(true);
//
//                System.out.println("table[" + i + "] " + keyField.get(entry) + "=" + valField.get(entry) + "   queue->" + ((ReferenceQueue) queueField.get(entry)).poll());
//            }
//        }

//        System.out.println(weakHashMap.entrySet());
    }
}
