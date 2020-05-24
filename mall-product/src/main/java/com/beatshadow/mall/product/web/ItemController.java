package com.beatshadow.mall.product.web;

import com.beatshadow.common.utils.R;
import com.beatshadow.mall.product.service.SkuInfoService;
import com.beatshadow.mall.product.vo.SkuItemVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/25 01:10
 */
@Controller
public class ItemController {


    private SkuInfoService skuInfoService ;

    public ItemController(SkuInfoService skuInfoService) {
        this.skuInfoService = skuInfoService;
    }

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model){
                          //路径变量使用@PathVariabl
        SkuItemVo skuItemVo = skuInfoService.item(skuId);
        model.addAttribute("item",skuItemVo);
        return "item" ;
    }
}
