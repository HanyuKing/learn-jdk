package easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ProductImportDTO {
    @ExcelProperty("商品ID")
    private String productId;
    
    // 销售属性列 - 使用Map接收
    private Map<String, List<String>> skuPropMap = new HashMap<>();

    // 类目关键属性列 - 使用Map接收
    private Map<String, List<String>> categoryKeyPropMap = new HashMap<>();

    // 类目其它属性列 - 使用Map接收
    private Map<String, List<String>> categoryOtherPropMap = new HashMap<>();
}