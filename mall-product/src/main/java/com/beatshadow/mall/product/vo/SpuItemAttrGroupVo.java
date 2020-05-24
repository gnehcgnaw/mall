package com.beatshadow.mall.product.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 属性的分组信息
 * @author gnehcgnaw
 *  @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpuItemAttrGroupVo{
    private String groupName ;
    private List<Attr> attrs ;

}