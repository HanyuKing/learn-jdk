package BasicType;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hanyu King
 * @since 2018-03-26 15:39
 */
public class LongTest {

    @Test
    public void testEquals() {
        System.out.println(Long.valueOf(1L).equals(1L));
    }

    @Test
    public void test() {
        System.out.println((int) (1 / 0.0));
    }

    @Test
    public void testConvert() {
        Map map = new HashMap();
        map.put("key", 123);
        System.out.println(Long.valueOf((String) map.get("key")));
    }
}
