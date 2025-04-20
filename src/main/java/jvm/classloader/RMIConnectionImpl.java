package jvm.classloader;

import sun.reflect.misc.ReflectUtil;

public class RMIConnectionImpl {
    public static final class CombinedClassLoader extends ClassLoader {

        private final static class ClassLoaderWrapper extends ClassLoader {
            ClassLoaderWrapper(ClassLoader cl) {
                super(cl);
            }

            @Override
            protected Class<?> loadClass(String name, boolean resolve)
                    throws ClassNotFoundException {
                return super.loadClass(name, resolve);
            }
        };

        final CombinedClassLoader.ClassLoaderWrapper defaultCL;

        CombinedClassLoader(ClassLoader parent, ClassLoader defaultCL) {
            super(parent);
            this.defaultCL = new CombinedClassLoader.ClassLoaderWrapper(defaultCL);
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve)
                throws ClassNotFoundException {
            ReflectUtil.checkPackageAccess(name);
            try {
                super.loadClass(name, resolve);
            } catch(Exception e) {
                for(Throwable t = e; t != null; t = t.getCause()) {
                    if(t instanceof SecurityException) {
                        throw t==e?(SecurityException)t:new SecurityException(t.getMessage(), e);
                    }
                }
            }
            final Class<?> cl = defaultCL.loadClass(name, resolve);
            return cl;
        }

    }
}