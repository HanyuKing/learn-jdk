package BasicType;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * @Author Hanyu.Wang
 * @Date 2022/5/28 12:12
 * @Description
 * @Version 1.0
 **/
public class FloatTest {
    @Test
    public void test() {
        float a = 1.125f;
        System.out.println(a * 1d);
    }

    @Test
    public void test2() {
        String a = "0.33";
        double aa = Float.parseFloat(a) * 100d;
        System.out.println(aa / 100);
    }

    @Test
    public void test3() {
        String a = "0.33";
        System.out.println(new BigDecimal(a)
                .multiply(new BigDecimal(100))
                .divide(new BigDecimal(100))
                .floatValue());
    }
}
