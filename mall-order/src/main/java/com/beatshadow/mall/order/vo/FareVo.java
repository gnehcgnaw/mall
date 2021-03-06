package com.beatshadow.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 地址加运费
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/28 22:53
 */
@Data
public class FareVo {
    private MemberAddressVo memberAddressVo ;
    private BigDecimal fare ;
}
