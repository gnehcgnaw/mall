package com.beatshadow.mall.order.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/28 19:58
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuStockVo {
    private Long skuId ;
    private Boolean hasStock ;
}