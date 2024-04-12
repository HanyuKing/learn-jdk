package date;

import org.junit.Test;
import util.BenchUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Hanyu.Wang
 * @Date 2024/4/11 16:57
 * @Description
 * @Version 1.0
 **/
public class SimpleDateFormatThreadUnSafeTest {
    @Test
    public void test1() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        BenchUtil.concurrentRun(2, () -> {
            try {
                System.out.println(sdf.parse("2024-04-11"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void test2() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        BenchUtil.concurrentRun(2000, () -> {
            Date date = new Date();
            System.out.println(sdf.format(date));
        });
    }
}
