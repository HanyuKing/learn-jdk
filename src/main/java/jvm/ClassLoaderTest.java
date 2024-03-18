package jvm;

import java.io.IOException;
import java.io.InputStream;

public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> klass = Class.forName("java.sql.Driver");
        System.out.println("java.sql.Driver classloader: " + klass.getClassLoader());

        ClassLoader c1 = new MyClassLoader1();
        ClassLoader c2 = new MyClassLoader2();

        Class klass1 = Class.forName("java.lang.Integer", false, c1);
        Class klass2 = Class.forName("java.lang.Integer", false, c2);
        System.out.println(klass1.getName());
        System.out.println(klass2.getName());
        System.out.println(klass1 == klass2);

        System.out.println("klass1 classloader: " + klass1.getClassLoader());
        System.out.println("klass2 classloader: " + klass2.getClassLoader());

        MyClassLoader3 c3 = new MyClassLoader3(); // change to MyClassLoader1

        Class klass3 = Class.forName("jvm.ClassLoaderTest", false, c3);
        Class klass4 = Class.forName("jvm.ClassLoaderTest", false, c1);
        System.out.println(klass3.getName());
        System.out.println(klass4.getName());
        System.out.println(klass3.equals(klass4));

        System.out.println("klass3 classloader: " + klass3.getClassLoader());
        System.out.println("klass4 classloader: " + klass4.getClassLoader());

    }

    static class MyClassLoader3 extends ClassLoader {
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            try {
                String clazzName = name.substring(name.lastIndexOf(".") + 1) + ".class";

                InputStream is = getClass().getResourceAsStream(clazzName);
                if (is == null) {
                    return super.loadClass(name);
                }
                byte[] b = new byte[is.available()];
                is.read(b);
                return defineClass(name, b, 0, b.length);
            } catch (IOException e) {
                throw new ClassNotFoundException(name);
            }
        }
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
