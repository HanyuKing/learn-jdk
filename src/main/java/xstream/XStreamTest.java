package xstream;

import com.thoughtworks.xstream.XStream;
import sun.misc.VM;

/**
 * @author Hanyu King
 * @since 2018-08-08 15:51
 */
public class XStreamTest {
    public static void main(String[] args) {

        while (true) {
            XStream xStream = new XStream();
            xStream.toString();
            xStream = null;
        }

    }
}
