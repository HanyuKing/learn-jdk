package jvm.promo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2022/7/26 16:31
 * @Description
 * @Version 1.0
 **/
public class SkuService {

    public List<PromoSkuVO> getAllSku(ProductQuery productQuery) {

        Integer count = getSkuCountFromCenter(productQuery);
        if (count == 0) {
            return new ArrayList<PromoSkuVO>();
        }

        List<PromoSkuVO> skuList = new ArrayList<PromoSkuVO>();
        skuList.addAll(getSkuListFromCenter(productQuery));

        if ((productQuery.getPageIndex() + 1) <= getTotalPage(count)) {
            productQuery.setPageIndex(productQuery.getPageIndex() + 1);
            skuList.addAll(getAllSku(productQuery));
        }

        return skuList;
    }

    private Integer getTotalPage(Integer count) {
        return count / 10;
    }

    private List<PromoSkuVO> getSkuListFromCenter(ProductQuery productQuery) {
        List<PromoSkuVO> list = new ArrayList<>();
        for (int i = (productQuery.getPageIndex() - 1) * 10; i < productQuery.getPageIndex() * 10; i++) {
            list.add(PromoSkuVO.builder().id(i).build());
        }
        return list;
    }

    private Integer getSkuCountFromCenter(ProductQuery productQuery) {
        return 101;
    }
}
