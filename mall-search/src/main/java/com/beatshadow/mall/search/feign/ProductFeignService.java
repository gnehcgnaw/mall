package com.beatshadow.mall.search.feign;

import com.beatshadow.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/24 17:21
 */
@FeignClient(value = "mall-product")
public interface ProductFeignService {
    @RequestMapping("product/attr/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId);
}
