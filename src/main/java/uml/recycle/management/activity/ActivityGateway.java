package uml.recycle.management.activity;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/9/3 19:24
 * @Description
 * @Version 1.0
 **/
public class ActivityGateway {
    public List<RecycleActivityInfo> selectActivityInfoByIds(List<Integer> activityIds) {
        return Lists.newArrayList(
                RecycleActivityInfo.builder().activityId(1).activityName("测试1").build(),
                RecycleActivityInfo.builder().activityId(2).activityName("测试2").build()
//                RecycleActivityInfo.builder().activityId(3).activityName("测试3").build()
        );
    }

    public List<Integer> selectActivityIds(Integer isOnline, Integer pageSize, Integer pageNo) {
        if (pageNo != 1) {
            return null;
        }
        return Lists.newArrayList(1, 2);
    }
}
