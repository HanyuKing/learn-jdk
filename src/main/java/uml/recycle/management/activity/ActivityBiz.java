package uml.recycle.management.activity;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.json.GsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Hanyu.Wang
 * @Date 2024/9/3 19:02
 * @Description
 * @Version 1.0
 **/
@Slf4j
public class ActivityBiz {
    
    private ActivityCache activityCache = new ActivityCache();
    private ActivityGateway activityGateway=  new ActivityGateway();
    private ActivityScopeGateway activityScopeGateway = new ActivityScopeGateway();
    private ActivityRuleGateway activityRuleGateway = new ActivityRuleGateway();
    
    @Before
    public void init() {
        // initCache();
    }

    @Test
    public void initCache() {
        int pageNo = 1, pageSize = 300;
        Map<Integer, List<Integer>> skuActivityMap = new HashMap<>();
        Map<String, List<Integer>> orgActivityMap = new HashMap<>();
        Map<Long, List<Integer>> productActivityMap = new HashMap<>();
        Map<Integer, List<RecycleActivityRule>> activityRulesMap = new HashMap<>();
        Map<Integer, RecycleActivityInfo> activityMap = new HashMap<>();
        List<Integer> activityIds = null;
        do {
            //按次缓存，报错则跳过本次查询
            try {
                //获取活动id集合(查询全量活动，上线，活动进行中)
                activityIds = activityGateway.selectActivityIds(1, pageSize, pageNo++);
                //填充对应关系
                fillRefMaps(skuActivityMap, orgActivityMap, productActivityMap, activityIds);
                fillBeanMaps(activityRulesMap, activityMap, activityIds);
            } catch (Exception e) {
                log.error("本次缓存失败，第{}页活动数据", pageNo-1);
                
            }
        } while (!CollectionUtils.isEmpty(activityIds));
        // 更新本地缓存
        activityCache.updateCache(skuActivityMap, orgActivityMap, productActivityMap, activityRulesMap, activityMap);

        System.out.println(JSON.toJSONString(activityCache));
    }

    private void fillBeanMaps(Map<Integer, List<RecycleActivityRule>> activityRulesMap,
                              Map<Integer, RecycleActivityInfo> activityMap, List<Integer> activityIds) {
        List<RecycleActivityInfo> recycleActivityInfos = activityGateway.selectActivityInfoByIds(activityIds);
        for (RecycleActivityInfo info : recycleActivityInfos) {
            activityMap.put(IdObjectCache.getId(info.getActivityId()), info);
        }
        List<RecycleActivityRule> recycleActivityRules = activityRuleGateway.selectRulesByActivityIds(activityIds);
        for (RecycleActivityRule rule : recycleActivityRules) {
            List<RecycleActivityRule> rules = activityRulesMap.computeIfAbsent(IdObjectCache.getId(rule.getActivityId()),
                    v -> new ArrayList<>());
            rules.add(rule);
        }
    }

    private void fillRefMaps(Map<Integer, List<Integer>> skuActivityMap,
                             Map<String, List<Integer>> orgActivityMap,
                             Map<Long, List<Integer>> productActivityMap,
                             List<Integer> activityIds) {
        //查询活动范围信息
        List<RecycleActivityScope> RecycleActivityScopes = activityScopeGateway.selectRecycleScopes(activityIds);
        Map<Integer, List<RecycleActivityScope>> relationScopesMap = new HashMap<>();
        for (RecycleActivityScope scope : RecycleActivityScopes) {
            List<RecycleActivityScope> tmpScopes = relationScopesMap.computeIfAbsent(scope.getRelationId(),
                    v -> new ArrayList<>());
            tmpScopes.add(scope);
        }
        for (Integer activityId : activityIds) {
            List<RecycleActivityScope> scopes = relationScopesMap.get(activityId);
            if (CollectionUtils.isEmpty(scopes)) {
                continue;
            }
            Integer cachedActivityId = IdObjectCache.getId(activityId);
            //放入规则map
            for (RecycleActivityScope scope : scopes) {
                if (scope.getScopeType().equals(ScopeTypeEnum.SKU_TYPE.getCode())) {
                    //填充新机与活动的对应关系
                    fillSkuActivityMap(skuActivityMap, cachedActivityId, scope);
                } else if (scope.getScopeType().equals(ScopeTypeEnum.ORG_TYPE.getCode())) {
                    //填充门店与活动的对应关系
                    fillOrgActivityMap(orgActivityMap, cachedActivityId, scope);
                } else if (scope.getScopeType().equals(ScopeTypeEnum.PRODUCT_TYPE.getCode())) {
                    //填充旧机与活动的对应关系
                    fillProductActivityMap(productActivityMap, cachedActivityId, scope);
                }
            }
        }
    }

    private void fillProductActivityMap(Map<Long, List<Integer>> productActivityMap, Integer cachedActivityId,
                                        RecycleActivityScope scope) {
        for (String skuId : scope.getScopeIds()) {
            List<Integer> actList = productActivityMap.computeIfAbsent(IdObjectCache.getId(Long.parseLong(skuId)), v -> new ArrayList<>());
            actList.add(cachedActivityId);
        }
    }

    private void fillOrgActivityMap(Map<String, List<Integer>> orgActivityMap, Integer cachedActivityId,
                                    RecycleActivityScope scope) {
        if (CollectionUtils.isEmpty(scope.getScopeIds())) {
            return;
        }
        List<String> orgIdList = scope.getScopeIds();
        for (String orgId : orgIdList) {
            if (orgId.trim().length() == 0) {
                continue;
            }
            List<Integer> actList = orgActivityMap.computeIfAbsent(IdObjectCache.getId(orgId), v -> new ArrayList<>());
            actList.add(cachedActivityId);
        }
    }

    private void fillSkuActivityMap(Map<Integer, List<Integer>> skuActivityMap, Integer cachedActivityId,
                                    RecycleActivityScope scope) {
        for (String skuId : scope.getScopeIds()) {
            List<Integer> actList = skuActivityMap.computeIfAbsent(IdObjectCache.getId(Integer.parseInt(skuId)), v -> new ArrayList<>());
            actList.add(cachedActivityId);
        }
    }

}
