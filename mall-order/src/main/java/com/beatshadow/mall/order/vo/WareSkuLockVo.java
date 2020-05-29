package com.beatshadow.mall.order.vo;

import lombok.Data;

import java.util.List;

/**
 * 库存锁定
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/29 06:31
 */
@Data
public class WareSkuLockVo {
    /**
     * 订单号
     */
    private String orderSn ;

    /**
     * 需要锁定的所有库存信息
     */
    List<OrderItemVo> locks ;

}
