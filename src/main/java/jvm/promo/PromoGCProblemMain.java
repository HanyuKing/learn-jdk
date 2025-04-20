package jvm.promo;

import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2022/7/26 15:52
 * @Description
 * @Version 1.0
 **/
public class PromoGCProblemMain {
    public static void main(String[] args)  throws Exception {
        Thread.sleep(10000);
        ProductQuery productQuery = ProductQuery.builder().pageIndex(1).pageIndexTemp(1).build();
        List<PromoSkuVO> list = new SkuService().getAllSku(productQuery);

        System.out.println("------------pause-----------");

        Thread.sleep(10000000);

        System.out.println(list);
    }

}
