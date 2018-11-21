package xstream;

import com.thoughtworks.xstream.XStream;
import sun.misc.VM;

import java.lang.management.ManagementFactory;

/**
 * @author Hanyu King
 * @since 2018-08-08 15:51
 */
public class XStreamTest {
    public static void main(String[] args) {
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
        while (true) {
            XStream xStream = new XStream();
            xStream.toString();
            xStream = null;
        }

    }
}
