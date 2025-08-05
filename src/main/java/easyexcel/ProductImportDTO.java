package easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ProductImportDTO {
    @ExcelProperty("商品ID")
    private String productId;
    
    // 动态属性列 - 使用Map接收
    private Map<String, String> skuPropMap = new HashMap<>();
}