package uml.recycle.management.activity;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/9/3 19:28
 * @Description
 * @Version 1.0
 **/
public class ActivityRuleGateway {
    public List<RecycleActivityRule> selectRulesByActivityIds(List<Integer> activityIds) {
        return Lists.newArrayList(
                RecycleActivityRule.builder()
                        .activityId(1)
                        .recycleMinPrice(0)
                        .recycleMaxPrice(100)
                        .recycleDiscountType((byte)1)
                        .recycleNewDeviceCountLimit(1222)
                        .recycleNewDeviceDiscountPrice(23)
                        .build()
        );
    }
}
