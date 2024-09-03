package uml.recycle.management.activity;

import lombok.Getter;

@Getter
public enum ScopeTypeEnum {

    SKU_TYPE(1, "sku类型"),
    ORG_TYPE(2, "门店id类型"),
    PRODUCT_TYPE(3, "线下旧机id类型"),
    ONLINE_PRODUCT_TYPE(4, "线上旧机id类型"),
    PACKAGE_TYPE(5, "套装类型"),
    ;


    /**
     * 状态描述
     */
    private Integer code;

    /**
     * 状态描述
     */
    private String desc;

    ScopeTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
