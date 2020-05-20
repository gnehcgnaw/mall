package com.beatshadow.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.beatshadow.common.to.es.SkuEsModel;
import com.beatshadow.mall.search.constant.EsConstant;
import com.beatshadow.mall.search.service.ProductSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/21 03:19
 */
@Slf4j
@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    private final RestHighLevelClient restHighLevelClient ;

    public ProductSearchServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }


    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        //保存到es
        // 给es中创建索引，product ，建立好映射关系
        BulkRequest bulkRequest = new BulkRequest() ;

        RequestOptions options = RequestOptions.DEFAULT;

        for (SkuEsModel skuEsModel: skuEsModels){
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            indexRequest.source(JSON.toJSONString(skuEsModel), XContentType.JSON);
            bulkRequest.add(indexRequest);

        }
        BulkResponse bulkItemResponses = restHighLevelClient.bulk(bulkRequest, options);
        //todo 如果批量错误
        boolean hasFailures = bulkItemResponses.hasFailures();
        List<String> collect = Arrays.stream(bulkItemResponses.getItems()).map((itemResponse) -> {
            return itemResponse.getId();
        }).collect(Collectors.toList());

        log.debug("商品上架成功：{}",collect);

        return hasFailures ;

    }
}
