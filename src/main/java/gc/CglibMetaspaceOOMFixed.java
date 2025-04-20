package gc;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CglibMetaspaceOOMFixed {
    public static void main(String[] args) {
        List<ClassLoader> classLoaders = new ArrayList<>();

        try {
            while (true) {
                // 创建一个新的 ClassLoader
                ClassLoader customClassLoader = new CustomClassLoader();
                classLoaders.add(customClassLoader); // 保存引用，避免立即被 GC

                Enhancer enhancer = getEnhancer(customClassLoader);

                Object proxy = enhancer.create();
                System.out.println("Created proxy class: " + proxy.getClass().getClassLoader());

                // 释放 ClassLoader 引用，让 GC 可以回收它
                if (classLoaders.size() > 50) {
                    classLoaders.remove(0);
                }

                // 手动触发 GC（仅作提示，具体回收由 JVM 决定）
                System.gc();
                Thread.sleep(10); // 避免 CPU 过载
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static Enhancer getEnhancer(ClassLoader customClassLoader) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(OOM.class);
        enhancer.setUseCache(false); // 禁用缓存，确保生成新类
        enhancer.setClassLoader(customClassLoader); // 使用自定义 ClassLoader
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return proxy.invokeSuper(obj, args);
            }
        });
        return enhancer;
    }

    // 自定义 ClassLoader，确保可以被回收
    static class CustomClassLoader extends ClassLoader {
    }
}
