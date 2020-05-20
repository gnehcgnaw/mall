package com.beatshadow.mall.search.service;

import com.beatshadow.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/21 03:18
 */
public interface ProductSearchService {

    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
