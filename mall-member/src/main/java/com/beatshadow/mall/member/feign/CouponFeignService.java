package com.beatshadow.mall.member.feign;

import com.beatshadow.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/18 14:26
 */
@FeignClient(value = "mall-coupon")
public interface CouponFeignService {
    @RequestMapping("coupon/coupon/member-coupons")
    public R memberCoupons();
}
