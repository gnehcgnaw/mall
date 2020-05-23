package com.beatshadow.mall.search.service;

import com.beatshadow.mall.search.vo.SearchParam;
import com.beatshadow.mall.search.vo.SearchResult;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/23 14:51
 */
public interface MallSearchService {
    SearchResult search(SearchParam searchParam);
}
