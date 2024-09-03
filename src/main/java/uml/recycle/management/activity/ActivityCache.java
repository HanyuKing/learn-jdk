package uml.recycle.management.activity;

import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ActivityCache {

    /**
     * 记录缓存更新时间（更新时提供查看接口）
     */
    private Long updateTime = 0L;
    /**
     * 新机与活动对应关系（1 V n）
     */
    private Map<Integer, List<Integer>> skuActivityMap = null;

    /**
     * 门店与活动对应关系（1 V n）
     */
    private Map<String, List<Integer>> orgActivityMap = null;

    /**
     * 旧机与活动对应关系（1 V n）
     */
    private Map<Long, List<Integer>> productActivityMap = null;

    /**
     * 活动id与规则列表对应关系的缓存
     * 注意：RecycleActivityRule对象未做只读保护，使用时请勿修改对象内的值
     */
    private Map<Integer, List<RecycleActivityRule>> activityRulesMap = null;

    /**
     * 活动id与活动对象对应关系缓存
     * 注意：RecycleActivityInfo对象未做只读保护，使用时请勿修改对象内的值
     */
    private Map<Integer, RecycleActivityInfo> activityMap = null;

    protected void updateCache(Map<Integer, List<Integer>> skuActivityMap,
                               Map<String, List<Integer>> orgActivityMap,
                               Map<Long, List<Integer>> productActivityMap,
                               Map<Integer, List<RecycleActivityRule>> activityRulesMap,
                               Map<Integer, RecycleActivityInfo> activityMap) {
        this.skuActivityMap = transUnmodifiableMap(skuActivityMap);
        this.orgActivityMap = transUnmodifiableMap(orgActivityMap);
        this.productActivityMap = transUnmodifiableMap(productActivityMap);
        this.activityRulesMap = transUnmodifiableMap(activityRulesMap);
        this.activityMap = Collections.unmodifiableMap(activityMap);
        this.updateTime = System.currentTimeMillis();
    }

    private <T, E> Map<T, List<E>> transUnmodifiableMap(Map<T, List<E>> objActivityMap) {
        Map<T, List<E>> tmpActivityMap = new HashMap<>();
        for (T key : objActivityMap.keySet()) {
            tmpActivityMap.put(key, Collections.unmodifiableList(objActivityMap.get(key)));
        }
        return Collections.unmodifiableMap(tmpActivityMap);
    }
}