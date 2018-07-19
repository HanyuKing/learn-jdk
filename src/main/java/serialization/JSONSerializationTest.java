package serialization;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * @author Hanyu King
 * @since 2018-07-19 11:01
 */
public class JSONSerializationTest {

    /**
     * fastjson 优先使用 get，再使用 is
     */
    @Test
    public void testIsField() {
        IsClass isClass = new IsClass();
        isClass.setTrue(true);

        System.out.println(JSON.toJSONString(isClass));

        System.out.println(JSON.parseObject("{\"true\":true}", IsClass.class).isTrue);
    }
    private static class IsClass {
        private boolean isTrue;

        public boolean isTrue() {
            return isTrue;
        }

        public void setTrue(boolean aTrue) {
            isTrue = aTrue;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
