package com.beatshadow.mall.product.vo;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * SKu的销售属性vo
 * @author gnehcgnaw
 * @see AttrVo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuItemSaleAttrVo{
    private Long attrId ;
    private String attrName ;
    private String attrValue ;
}
