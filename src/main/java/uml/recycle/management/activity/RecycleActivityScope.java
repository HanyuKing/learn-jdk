package uml.recycle.management.activity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Hanyu.Wang
 * @Date 2024/9/3 19:22
 * @Description
 * @Version 1.0
 **/
@Data
@Builder
public class RecycleActivityScope {
    /**
     * 活动id
     */
    private Integer id;

    private Integer relationId;
    // 1,2,3
    private Integer scopeType;

    /**
     * 规则ID集合
     */
    private List<String> scopeIds;

    /**
     * 是否全量
     */
    private Integer isFull;

    private Integer status;

    /**
     * 创建人
     */
    private String addUser;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 创建时间
     */
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
