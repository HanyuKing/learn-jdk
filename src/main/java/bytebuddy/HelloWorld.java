package bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @Author Hanyu.Wang
 * @Date 2023/3/6 19:07
 * @Description
 * @Version 1.0
 **/
public class HelloWorld {
    public static void main(String[] args) throws Exception {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(Thread.currentThread().getContextClassLoader())
                .getLoaded();

        System.out.println(dynamicType.newInstance());
    }
}
