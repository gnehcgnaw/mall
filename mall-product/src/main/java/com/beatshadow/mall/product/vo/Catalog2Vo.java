package com.beatshadow.mall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 二级分类
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/21 13:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Catalog2Vo {
    private String catalog1Id ; //1级分类ID
    private List<Catalog2Vo.Catalog3Vo> catalog3List ; //3级分类ID
    private String id ;
    private String name ;

    /**
     * 三级分类
     */
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public  static  class  Catalog3Vo{
        private String catalog2Id ;
        private String id ;
        private String name ;


    }
}
