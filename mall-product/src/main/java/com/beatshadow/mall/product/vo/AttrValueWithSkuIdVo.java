package com.beatshadow.mall.product.vo;

import lombok.*;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/25 14:36
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttrValueWithSkuIdVo {
    private String attrValue ;
    private String skuIds;
}
