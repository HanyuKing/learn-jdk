package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hanyu King
 * @since 2018-04-14 14:32
 */
public class PhoneNumberTest {
    public static void main(String[] args) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[0-9]{6}$");
        m = p.matcher("190743");
        b = m.matches();

        System.out.println(b);
    }
}
