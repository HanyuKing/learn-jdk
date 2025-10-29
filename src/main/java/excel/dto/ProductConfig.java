package excel.dto;

import lombok.Data;


@Data
public class ProductConfig {
    private String spuName;
    private String productId;
    private String tag;
    private String resolution;
    private String expandMargin;
    private String punch;
    private String bleedLine;
    private String hasCutLine;
    private String aspectRatio;
    private String fileType;
    private String hasBorder;
}