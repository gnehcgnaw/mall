package com.beatshadow.mall.order.feign;

import com.beatshadow.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/29 03:19
 */
@FeignClient(value = "mall-product")
public interface ProductFeignService {
    @GetMapping("/product/spuinfo/skuId/{id}")
    public R getSpuInfoBySkuId(@PathVariable("id")Long skuId);
}
