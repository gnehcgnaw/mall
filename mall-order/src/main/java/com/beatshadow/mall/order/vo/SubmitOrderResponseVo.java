package com.beatshadow.mall.order.vo;

import com.beatshadow.mall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/29 01:53
 */
@Data
public class SubmitOrderResponseVo {
    /**
     * 订单信息
     */
    private OrderEntity order ;

    /**
     * 错误状态吗
     *      0 成功
     *      不是0 ，就是各有各的错误
     */
    private Integer code ;

}
