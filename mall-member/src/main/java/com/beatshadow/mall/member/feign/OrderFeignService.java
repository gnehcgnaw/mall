package com.beatshadow.mall.member.feign;

import com.beatshadow.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/30 14:15
 */
@FeignClient(value = "mall-order")
public interface OrderFeignService {
    @PostMapping("/order/order/listWithItem")
    public R listWithItem(@RequestBody Map<String,Object> params);
}
