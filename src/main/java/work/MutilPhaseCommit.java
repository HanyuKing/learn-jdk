package work;

import java.util.Arrays;
import java.util.List;

/**
 * @author Hanyu King
 * @since 2018-04-26 18:25
 */
public class MutilPhaseCommit {

    private static final int pinTotal = 10;
    private static final int groupSize = 4;

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < pinTotal; i++) {
            sb.append("test_pop_hanyu_").append(i).append(",");
        }
        String pinStrs = sb.toString();
        String[] pins = pinStrs.split(",");

        int goodCustomerTotal = 0;

        StringBuilder pinStr = new StringBuilder();
        for(int i = 1; i <= pinTotal; i++) {
            String pin = pins[i - 1];
            if(pin.contains("2") || pin.contains("4") || pin.contains("9")) {
                continue;
            }

            goodCustomerTotal++;
            pinStr.append(pin).append(",");

            if(goodCustomerTotal % groupSize == 0) {
                System.out.println(pinStr.toString());
                pinStr = new StringBuilder();
            }
        }

        if(pinStr.length() > 0) {
            System.out.println(pinStr);
        }

    }
}
