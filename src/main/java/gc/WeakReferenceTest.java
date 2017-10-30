package gc;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author wanghanyu
 * @create 2017-10-30 19:51
 */
public class WeakReferenceTest {

    public static void main(String[] args) {
        SocketManager socketManager = new SocketManager();
        for (int i = 0; i < 1; i++) {
            socketManager.setUser("KEY-" + (i + 1), i + 1);
        }

        System.out.println("size-1->" + socketManager.getSize());
        System.gc();
        System.out.println("size-2->" + socketManager.getSize());
        socketManager.getSize();
        socketManager.getSize();
        socketManager.getSize();
        socketManager.getSize();
        System.out.println("KEY-1->" + socketManager.getUser("KEY-1"));
        System.out.println("size-3->" + socketManager.m.size());
        System.out.println();

    }

    public static class SocketManager {
        public WeakHashMap<String, Object> m = new WeakHashMap<String, Object>();

        public void setUser(String s, Object u) {
            m.put(s, u);
        }
        public Object getUser(String s) {
            return m.get(s);
        }

        public void remove() {
            m.remove("KEY-1");
        }

        public int getSize() {
            if(!m.isEmpty()) {
                return m.size();
            } else {
                return 0;
            }
        }
    }
}
