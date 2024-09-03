package uml.recycle.management.activity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author Hanyu.Wang
 * @Date 2024/9/3 19:06
 * @Description
 * @Version 1.0
 **/
@Data
@Builder
public class RecycleActivityRule {
    /**
     * id
     */
    private Integer id;

    /**
     * 活动编号
     */
    private Integer activityId;

    /**
     * 回收旧机档位最小金额（分）
     */
    private Integer recycleMinPrice;

    /**
     * 回收旧机档位最大金额（分）
     */
    private Integer recycleMaxPrice;

    /**
     * 旧机回收优惠类型(1-固定比例,2-固定金额)
     */
    private Byte recycleDiscountType;

    /**
     * 旧机回收优惠金额(分)
     */
    private Integer recycleDiscountPrice;

    /**
     * 旧机回收优惠比例(整数*100)
     */
    private Integer recycleDiscountRatio;

    /**
     * 新机回收立减金额（分）
     */
    private Integer recycleNewDeviceDiscountPrice;

    /**
     * 新机回收数量限制（个）
     */
    private Integer recycleNewDeviceCountLimit;

    /**
     * 添加用户
     */
    private String addUser;

    /**
     * 更新用户
     */
    private String updateUser;

    /**
     * 添加时间
     */
    private Date addTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
