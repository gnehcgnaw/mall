package com.beatshadow.mall.product.feign;

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
    //刚开始我使用的是 R<List<SkuHasStockVo>> 接收参数，但是发现feign的提供着数据是正确的，但是调用放获取到数据是null ,
    //todo 后续去解决问题的原因
    @PostMapping("/ware/waresku/hasstock")
    public  R getSkuHasStock (@RequestBody List<Long> skuIds);

}
