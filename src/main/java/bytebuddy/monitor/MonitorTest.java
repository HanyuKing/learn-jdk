package bytebuddy.monitor;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

/**
 * @Author Hanyu.Wang
 * @Date 2024/8/7 15:04
 * @Description
 * @Version 1.0
 **/
public class MonitorTest {
    @Test
    public void test_byteBuddy() throws Exception {
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(BizMethod.class)
                .method(ElementMatchers.named("queryUserInfo"))
                .intercept(MethodDelegation.to(MonitorDemo.class))
                .make();
        // 加载类
        Class<?> clazz = dynamicType.load(MonitorTest.class.getClassLoader())
                .getLoaded();
        // 反射调用
        clazz.getMethod("queryUserInfo", String.class, String.class).invoke(clazz.newInstance(), "10001", "abcde");
    }
}
