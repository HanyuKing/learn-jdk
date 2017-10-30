package gc;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author wanghanyu
 * @create 2017-10-30 20:25
 */
public class WeakReferenceTest2 {
    public static void main(String[] args) {
        User user = new User(1, "King");
        User user2 = new User(2, "Hanyu");

        Map<String, Object> map = new WeakHashMap<String, Object>();
        map.put("1", user);
        map.put("2", user2);

        System.gc();

        System.out.println(map.get("1"));
    }

    public static class User extends WeakReference<Integer> {
        private String name;

        public User(Integer id, String name) {
            super(id);
            this.name = name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "[id: " + super.get() + " name: " + this.getName() + "]\n";
        }
    }
}
