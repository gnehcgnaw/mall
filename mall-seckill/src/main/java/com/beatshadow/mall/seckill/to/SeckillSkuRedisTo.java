package com.beatshadow.mall.seckill.to;

import com.beatshadow.mall.seckill.vo.SkuInfoVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/31 00:22
 */
@Data
public class SeckillSkuRedisTo {

    /**
     * 活动id
     */
    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    private BigDecimal seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

    /**
     * sku的详细信息
     */
     private SkuInfoVo skuInfo ;

    /**
     * 当前商品秒杀的开始时间
     */
    private Long startTime ;
    /**
     * 当前商品秒杀的结束时间
     */
     private Long endTime ;

    /**
     * 商品秒杀随机码
     */
    private String randomCode ;
}
