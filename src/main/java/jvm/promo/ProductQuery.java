package jvm.promo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductQuery {
    private Integer pageIndex;
    private Integer pageIndexTemp;
}