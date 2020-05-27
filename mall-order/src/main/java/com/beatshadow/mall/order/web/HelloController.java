package com.beatshadow.mall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/28 01:41
 */
@Controller
public class HelloController {
    @GetMapping("/{page}.html")
    public String listPage(@PathVariable("page")String page){
        return page+".html" ;
    }
}
