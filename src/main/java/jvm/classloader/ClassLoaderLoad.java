package jvm.classloader;

import jvm.ClassLoaderTest;
import sun.reflect.misc.ReflectUtil;

/**
 * @Author Hanyu.Wang
 * @Date 2025/4/13 12:59
 * @Description
 * @Version 1.0
 **/
public class ClassLoaderLoad {
    public static void main(String[] args) throws Exception {
        while (true) {
            RMIConnectionImpl.CombinedClassLoader c1 = new RMIConnectionImpl.CombinedClassLoader(null, Thread.currentThread().getContextClassLoader());

            Class klass1 = Class.forName("java.lang.Integer", false, c1);

            //System.out.println(klass1);

            //Thread.sleep(100);
        }
    }


}
