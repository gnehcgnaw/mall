package com.beatshadow.mall.product.feign;

import com.beatshadow.common.to.SkuHasStockVo;
import com.beatshadow.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/21 02:45
 */
@FeignClient(value = "mall-ware")
public interface WareFeignService {
    @PostMapping("/ware/waresku/hasstock")
    public  R getSkuHasStock (@RequestBody List<Long> skuIds);

}
