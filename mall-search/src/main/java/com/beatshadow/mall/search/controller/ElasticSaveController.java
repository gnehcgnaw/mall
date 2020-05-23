package com.beatshadow.mall.search.controller;

import com.beatshadow.common.exception.BizCodeEnume;
import com.beatshadow.common.to.es.SkuEsModel;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.search.service.ProductSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/21 03:15
 */
@Slf4j
@RequestMapping("/search")
@RestController
public class ElasticSaveController {
    private ProductSearchService productSearchService ;

    public ElasticSaveController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    /**
     * 上架商品
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody  List<SkuEsModel> skuEsModels)  {
        boolean b = false;
        try {
           b = productSearchService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            log.error("ElasticSaveController 商品上架错误 ：{}",b);
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (b){
            return R.ok() ;
        }else {
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}
