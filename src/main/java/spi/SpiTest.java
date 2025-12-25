package spi;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ServiceLoader;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/18 01:04
 * @Description
 * @Version 1.0
 **/
public class SpiTest {
    public static void main(String[] args) throws IOException {
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        Enumeration<URL> enumeration = classLoader.getResources("META-INF/services");
//        while (enumeration.hasMoreElements()) {
//            System.out.println(enumeration.nextElement());
//        }

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        ServiceLoader<SpiTestI> spiTests = ServiceLoader.load(SpiTestI.class, null);
        Iterator<SpiTestI> it = spiTests.iterator();

        while (it.hasNext()) {
            it.next().print();
        }
    }
}

