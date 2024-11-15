package concurrent;

import java.lang.annotation.ElementType;

/**
 * @Author Hanyu.Wang
 * @Date 2024/11/15 10:26
 * @Description
 * @Version 1.0
 **/
@java.lang.annotation.Documented
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.CLASS)
public @interface NotThreadSafe {
}
