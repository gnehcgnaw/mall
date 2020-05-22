package com.beatshadow.mall.product.web;

import com.beatshadow.mall.product.entity.CategoryEntity;
import com.beatshadow.mall.product.service.CategoryService;
import com.beatshadow.mall.product.vo.Catalog2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/21 13:30
 */
@Controller
public class IndexController {

    private CategoryService categoryService ;

    public IndexController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 一级分类： 修改index 页面
     * 二级分类： catalogLoader.js --->catalog.json
     * @param model
     * @return
     */
    @GetMapping({"/" ,"index.html"})
    public String indexPage(Model model){

        List<CategoryEntity> categoryEntityList = categoryService. getLevel1Categorys();
        model.addAttribute("categorys",categoryEntityList);
        //试图解析器拼串：spring.thymeleaf.prefix=classpath:/templates/   spring.thymeleaf.suffix=.html
        return "index" ;
    }

   // index/json/catalog.json
    @ResponseBody
    @GetMapping("index/catalog.json")
    public Map<String, List<Catalog2Vo>> getCatalogJson(){
        Map<String, List<Catalog2Vo>> map =  categoryService.getCatalogJson();
        return  map ;
    }
}
