package com.beatshadow.mall.order.feign;

import com.beatshadow.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/28 13:39
 */
@FeignClient("mall-cart")
public interface CartFeignService {
    @GetMapping("/getCurrentUserCartItems")
    public R getCurrentUserCartItems() ;
}
