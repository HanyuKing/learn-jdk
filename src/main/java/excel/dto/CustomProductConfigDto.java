package excel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomProductConfigDto {
    private String productId;
    private String customType;
    private int dpi;
    private boolean enableTopCircle;
    private boolean enableExpand;
    private String bleedingLine;
    private String knifeLine;
    private int productFileType;

    private float ratio;
    private boolean hasBorder;
}