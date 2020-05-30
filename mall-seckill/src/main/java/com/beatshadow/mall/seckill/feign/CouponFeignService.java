package com.beatshadow.mall.seckill.feign;

import com.beatshadow.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/30 21:23
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {
    @GetMapping("/coupon/seckillsession/getLatest3DaySession")
    public R getLatest3DaySession();
}
