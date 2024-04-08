package juc.unsafe;

import lombok.Getter;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/3 15:26
 * @Description
 * @Version 1.0
 **/
public class UnsafeUtil {
    @Getter
    private static final sun.misc.Unsafe unsafe;

    static {
        sun.misc.Unsafe value = null;
        try {
            Class<?> clazz = Class.forName("sun.misc.Unsafe");
            Field field = clazz.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            value = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException("error to get theUnsafe", e);
        }
        unsafe = value;
    }
}
