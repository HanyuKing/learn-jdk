package jvm;

public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader c1 = new MyClassLoader1();
        ClassLoader c2 = new MyClassLoader2();

        Class klass1 = Class.forName("java.lang.Integer", false, c1);
        Class klass2 = Class.forName("java.lang.Integer", false, c2);
        System.out.println(klass1.getName());
        System.out.println(klass2.getName());
        System.out.println(klass1 == klass2);
    }

    static class MyClassLoader1 extends ClassLoader {
        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            return super.loadClass(name, resolve);
        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            return super.loadClass(name);
        }
    }

    static class MyClassLoader2 extends ClassLoader {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            return super.loadClass(name);
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            return super.loadClass(name, resolve);
        }
    }
}
