package com.beatshadow.mall.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/23 12:26
 */
@Controller
public class SearchController {

    @GetMapping("/list.html")
    public String listPage(){
        return "list" ;
    }

}
