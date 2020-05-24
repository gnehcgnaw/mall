package com.beatshadow.mall.search.controller;

import com.beatshadow.mall.search.service.MallSearchService;
import com.beatshadow.mall.search.vo.SearchParam;
import com.beatshadow.mall.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/23 12:26
 */
@Controller
public class SearchController {

    private MallSearchService mallSearchService ;

    public SearchController(MallSearchService mallSearchService) {
        this.mallSearchService = mallSearchService;
    }

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam , Model model , HttpServletRequest httpServletRequest){
        String queryString = httpServletRequest.getQueryString();
        searchParam.setQueryString(queryString);
        SearchResult searchResult = mallSearchService.search(searchParam);
        model.addAttribute("result",searchResult);
        return "list" ;
    }

}
