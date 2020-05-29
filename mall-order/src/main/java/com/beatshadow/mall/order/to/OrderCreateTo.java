package com.beatshadow.mall.order.to;

import com.beatshadow.mall.order.entity.OrderEntity;
import com.beatshadow.mall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建好的订单
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/29 02:12
 */
@Data
public class OrderCreateTo {
    private OrderEntity order ;

    private List<OrderItemEntity> orderItems ;

    /**
     * 订单计算的应付价格
     */
    private BigDecimal payPrice ;

    /**
     * 运费
     */
    private BigDecimal fare ;
}
