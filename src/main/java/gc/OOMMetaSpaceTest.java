package gc;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author Hanyu.Wang
 * @Date 2025/3/18 02:04
 * @Description
 * @Version 1.0
 **/
public class OOMMetaSpaceTest {

    public static void main(String[] args) {
        int i = 0;//模拟计数多少次以后发生异常
        try {
            while (true){
                i++;
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(OOM.class);
                enhancer.setUseCache(false);
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects,
                                            MethodProxy methodProxy) throws Throwable {
                        return methodProxy.invokeSuper(o,args);
                    }
                });
                Object obj = enhancer.create();
                System.out.println(obj.getClass().getClassLoader());
            }
        } catch (Throwable e) {
            System.out.println("=================多少次后发生异常："+i);
            e.printStackTrace();
        }
    }
}
