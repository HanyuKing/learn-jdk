package excel.dto;

import lombok.Data;

@Data
public class FrontBackCategoryMapping {
    private String frontendCategory;     // 前台类目
    private String frontendCategoryId;   // 前台类目ID
    private String backendCategoryName;  // 后台类目名称
    private String backendCategoryCode;  // 后台类目code
}