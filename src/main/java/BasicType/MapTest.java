package BasicType;

import com.google.gson.Gson;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Hanyu.Wang
 * @Date 2022/10/17 11:37
 * @Description
 * @Version 1.0
 **/
public class MapTest {

    @Test
    public void testNullValue() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, null);
        map.put(2, null);
        System.out.println(map); // {1=null, 2=null}
        System.out.println(map.get(1)); // null
        System.out.println(map.get(111)); // null


        map = new ConcurrentHashMap<>();
        map.put(1, null); // NullPointerException
        map.put(2, null);
        System.out.println(map);
        System.out.println(map.get(1));
        System.out.println(map.get(111));
    }

    @Test
    public void testNullKey() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(null, 1);
        map.put(null, 2);
        System.out.println(map); // {null=2}
        System.out.println(map.get(null)); // 2
        System.out.println(map.get(123)); // null

        map = new ConcurrentHashMap<>();
        map.put(null, 1); // NullPointerException
        map.put(null, 2);
        System.out.println(map);
        System.out.println(map.get(null));
        System.out.println(map.get(123));
    }

    @Test
    public void testTypeConvert() {

        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson("{\"type\":1}", HashMap.class);

        System.out.println("number->" + NumberUtils.createNumber(data.get("type").toString()).intValue());

        Integer type = (Integer) data.get("type");

        System.out.println(type);
    }
}
