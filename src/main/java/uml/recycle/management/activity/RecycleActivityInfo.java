package uml.recycle.management.activity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/9/3 19:05
 * @Description
 * @Version 1.0
 **/
@Data
@Builder
public class RecycleActivityInfo {
    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动简称
     */
    private String activityNameSimple;

    /**
     * 活动状态（1活动生效，2活动失效）
     */
    private Byte activityStatus;

    /**
     * 活动优先级
     */
    private Byte priority;

    /**
     * 活动开始时间
     */
    private String startTime;

    /**
     * 活动结束时间
     */
    private String endTime;

    /**
     * 活动类型
     */
    private Byte activityType;

    /**
     * 影响渠道
     */
    private List<Integer> channel;

    /**
     * 活动模式
     */
    private Byte activityMode;

    /**
     * 活动系列名称描述
     */
    private String activitySeriesDesc;

    /**
     * 描述
     */
    private String activityDesc;

    /**
     * 活动规则标题
     */
    private String activityRuleTitle;

    /**
     * 活动规则描述
     */
    private String activityRuleDesc;

    /**
     * 活动范围类型
     */
    private Byte scopeRelationType;

    /**
     * 审核流程
     */
    private String auditProcess;

    /**
     * 审核品类
     */
    private Integer bpmType;

    /**
     * 审核id
     */
    private String auditTaskId;

    /**
     * 审核状态（1待提交审核，2审核中，3审核通过，4审核不通过）
     */
    private Byte auditStatus;

    /**
     * 上下线
     */
    private Byte isOnline;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 添加用户
     */
    private String addUser;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 更新用户
     */
    private String updateUser;

    /**
     * 参与上限
     */
    private Short participationGap;

    /**
     * 是否已同步
     */
    private Byte isSync;

    /**
     * 促销换新活动id
     */
    private Long promotionActivityId;

    /**
     * 同步次数
     */
    private Byte retryCount;

    /**
     * 活动档位规则信息列表
     */
    private List<RecycleActivityRule> activityRuleList;
}
