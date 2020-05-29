package com.beatshadow.mall.ware.vo;

import lombok.Data;

/**
 * 锁定结果
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/29 06:37
 */
@Data
public class LockStockResult {
    /**
     * 锁定的skuid
     */
    private Long skuId ;
    /**
     * 锁定的数量
     */
    private Integer num ;
    /**
     * 是否被锁定成功
     */
    private Boolean locked;
}
