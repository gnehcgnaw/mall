package com.beatshadow.mall.product.feign;

import com.beatshadow.common.to.es.SkuEsModel;
import com.beatshadow.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/21 03:43
 */
@FeignClient(value = "mall-search")
public interface SearchFeignService {
    @PostMapping("/search/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) ;

}
