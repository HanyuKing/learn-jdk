package collection;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Hanyu King
 * @since 2018-04-25 11:04
 */
public class ListSubString {

    @Test
    public void testAsList() {
        String pinStr = "1, 2, 3, 4, 5";
        List<String> pinList = Arrays.asList(pinStr.split(","));
        System.out.println(pinList.size());

        System.out.println(pinList.subList(0, 2));
    }

    @Test
    public void testAsList2() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            sb.append("test_pop_hanyu_").append(i).append(",");
        }

        String pinStr = sb.toString();
        String[] pins = pinStr.split(",");
        int pinLen = pins.length;
        List<String> pinList = Arrays.asList(pins);
        int groupSize = 3;
        int groupTotal = pinLen / groupSize + 1;

        for(int i = 0; i < groupTotal; i++) {
            List<String> subPins = pinList.subList(i * groupSize, (i + 1) * groupSize > pinLen ? pinLen : (i + 1) * groupSize);
            System.out.println(subPins);
        }
    }
}
