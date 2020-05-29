package com.beatshadow.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单提交的数据
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/29 01:34
 */
@Data
public class OrderSubmitVo {
    /**
     * 收货地址ID
     */
    private Long addrId ;

    /**
     * 支付方式
     */
    private Integer payType ;

    /**
     * 送货清单
     *
     */

    /**
     * 无需提交购买的商品，去购物车再获取一遍，参考jd
     */

    /**
     * 优惠
     */

    /**
     * 发票
     */

    /**
     * 防重令牌
     */

    private String orderToken ;

    /**
     * 应付总额
     *
     * 自己订单再算一个金额，
     *
     * 要进行验价， 如果相等：直接购买，如果不同提示用户确认
     *      jd没有做
     */
    private BigDecimal payPrice ;

    /**
     *用户的相关信息都在session中，所以不需要在这里定义
     */

    /**
     * 订单的备注
     */
    private String note ;
}
