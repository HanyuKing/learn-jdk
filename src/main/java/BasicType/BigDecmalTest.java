package BasicType;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author Hanyu King
 * @since 2018-07-19 20:41
 */
public class BigDecmalTest {

    @Test
    public void test() {
        double a = new BigDecimal(1)
                .divide(new BigDecimal(3), 5, BigDecimal.ROUND_HALF_EVEN)
                .doubleValue();
        System.out.println(a);
    }
}
