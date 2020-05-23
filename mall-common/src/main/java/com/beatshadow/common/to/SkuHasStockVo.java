package com.beatshadow.common.to;

import lombok.Builder;
import lombok.Data;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/21 02:21
 */
@Data
@Builder
public class SkuHasStockVo {
    private Long skuId ;
    private Boolean hasStock ;
}
