package reflect;

import java.lang.reflect.Field;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/31 22:00
 * @Description
 * @Version 1.0
 **/
public class ReflectUtil {
    public static <T> T getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class klass = obj.getClass();
        Field field = klass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(obj);
    }
}
