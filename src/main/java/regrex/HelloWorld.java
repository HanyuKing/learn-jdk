package regrex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wanghanyu
 * @create 2017-11-10 15:47
 */
public class HelloWorld {
    public static void main(String[] args) {
        String value = "-15";
        Pattern pattern = Pattern.compile("1|2|3|4|-1|5");
        Matcher m = pattern.matcher( value );

        System.out.println(m.matches());
    }
}
