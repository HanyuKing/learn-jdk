package other;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

/**
 * @author wanghanyu
 * @create 2017-10-26 18:24
 */
public class LogTest {
    public static void main(String[] args) {
        doExTest();
        doExTest();
    }

    private static void doExTest() {
        long start = System.nanoTime();
        for (int i=0; i<100000; ++i) {
            try {
                throw new RuntimeException("" + Math.random());
            } catch (Exception e) {
            }
        }
        //System.out.println("time: " + (System.nanoTime() - start));
    }


    @Test
    public void test() {
        String[] dates = new String[3];
        Long[] takeNum = new Long[dates.length];
        Long[] takeUserNum = new Long[dates.length];
        Long[] useNum = new Long[dates.length];
        Long[] useUserNum = new Long[dates.length];
        Long[] ordQty = new Long[dates.length];
        Long[] ordAmt = new Long[dates.length];
        Long[] couponSkuQty = new Long[dates.length];

        Map<String, Object[]> statisticsVOMap = Maps.newHashMap();
        statisticsVOMap.put("dates", dates);
        statisticsVOMap.put("takeNum", takeNum);
        statisticsVOMap.put("takeUserNum", takeUserNum);
        statisticsVOMap.put("useNum", useNum);
        statisticsVOMap.put("useUserNum", useUserNum);
        statisticsVOMap.put("ordQty", ordQty);
        statisticsVOMap.put("ordAmt", ordAmt);
        statisticsVOMap.put("couponSkuQty", couponSkuQty);
        takeNum[0] = 0L;
        takeNum[1] = 10L;

        System.out.println(JSON.toJSONString(statisticsVOMap));
    }
}
