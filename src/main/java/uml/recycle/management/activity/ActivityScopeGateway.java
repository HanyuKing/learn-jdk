package uml.recycle.management.activity;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/9/3 19:25
 * @Description
 * @Version 1.0
 **/
public class ActivityScopeGateway {
    public List<RecycleActivityScope> selectRecycleScopes(List<Integer> activityIds) {
        Integer activityId = 1;
        return Lists.newArrayList(
                RecycleActivityScope.builder()
                        .relationId(activityId)
                        .scopeType(ScopeTypeEnum.SKU_TYPE.getCode())
                        .scopeIds(Lists.newArrayList("1", "2", "3", "4", "36128")) // 5
                        .build(),
                RecycleActivityScope.builder()
                        .relationId(activityId)
                        .scopeType(ScopeTypeEnum.ORG_TYPE.getCode())
                        .scopeIds(Lists.newArrayList("MI51072","MI51193","MIYP31000","MI51071","MI51192","MI51070","MI51191","MI51190","MI51076","MI51197")) // 10
                        .build(),
                RecycleActivityScope.builder()
                        .relationId(1)
                        .scopeType(ScopeTypeEnum.PRODUCT_TYPE.getCode())
                        .scopeIds(Lists.newArrayList("20599000000040","20599000000070","20599000000160","20599000000190","20599000000250","20599000006562","20599000006565","20599000006568","20599000006571","20599000006574")) // 10
                        .build(),

                RecycleActivityScope.builder()
                        .relationId(2)
                        .scopeType(ScopeTypeEnum.SKU_TYPE.getCode())
                        .scopeIds(Lists.newArrayList("1", "2222", "36128")) // 3
                        .build(),
                RecycleActivityScope.builder()
                        .relationId(2)
                        .scopeType(ScopeTypeEnum.ORG_TYPE.getCode())
                        .scopeIds(Lists.newArrayList("MI51072","MI51193","MIYP31000","MI51071","MI51192","MI51070","MI51191","MI51190","MI51076","MI51197")) // 10
                        .build(),
                RecycleActivityScope.builder()
                        .relationId(2)
                        .scopeType(ScopeTypeEnum.PRODUCT_TYPE.getCode())
                        .scopeIds(Lists.newArrayList("20599000000040","20599000000070","20599000000160","20599000000190","20599000000250","20599000006562","20599000006565","20599000006568","20599000006571","20599000006574")) // 10
                        .build()
        );
    }
}
