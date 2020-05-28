package com.beatshadow.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/28 03:24
 */
@Data
public class OrderItemVo {
    /**
     * sku
     */
    private Long skuId ;
    /**
     * 是否被选中
     *//*
    private Boolean check = true ;*/
    /**
     * 标题
     */
    private String title ;

    /**
     * 图片
     */
    private String image ;

    /**
     * 套餐信息
     */
    private List<String> skuAttr ;

    /**
     * 价格
     */
    private BigDecimal price ;
    /**
     * 数量
     */
    private Integer count ;
    /**
     * 小计
     */
    private BigDecimal totalPrice ;

}
