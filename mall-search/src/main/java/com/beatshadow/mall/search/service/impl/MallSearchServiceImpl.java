package com.beatshadow.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.beatshadow.common.to.es.SkuEsModel;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.search.constant.EsConstant;
import com.beatshadow.mall.search.feign.ProductFeignService;
import com.beatshadow.mall.search.service.MallSearchService;
import com.beatshadow.mall.search.vo.AttrResponseVo;
import com.beatshadow.mall.search.vo.SearchParam;
import com.beatshadow.mall.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/23 14:51
 */
@Slf4j
@Service
public class MallSearchServiceImpl implements MallSearchService {

    private RestHighLevelClient restHighLevelClient ;

    private ProductFeignService productFeignService ;

    public MallSearchServiceImpl(RestHighLevelClient restHighLevelClient, ProductFeignService productFeignService) {
        this.restHighLevelClient = restHighLevelClient;
        this.productFeignService = productFeignService;
    }

    @Override
    public SearchResult search(SearchParam searchParam) {
        //准备检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParam);
        try {
            //执行检索请求
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //分析响应数据封装成需要的格式
            return buildSearchResult(searchResponse,searchParam);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

    /**
     * 分析响应数据封装成需要的格式
     * @param searchResponse
     */
    private SearchResult buildSearchResult(SearchResponse searchResponse ,SearchParam searchParam) {
        //命中记录
        SearchHits searchHits = searchResponse.getHits();
        //总记录数
        long value = searchHits.getTotalHits().value;
        //1.查询到的商品  [命中记录中获取]
        List<SkuEsModel> skuEsModelList = new ArrayList<>();
        if (searchHits.getHits()!=null&&searchHits.getHits().length>0){
            for (SearchHit hit : searchHits.getHits()) {
                //source是原本的信息
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if (!StringUtils.isEmpty(searchParam.getKeyword())){
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    skuEsModel.setSkuTitle(skuTitle.getFragments()[0].string());
                }
                skuEsModelList.add(skuEsModel);
            }
        }
        //2、属性信息 [聚合中获取]
        List<SearchResult.AttrVo> attrVos = new ArrayList<>() ;
        ParsedNested  attr_agg = searchResponse.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            ParsedStringTerms attr_name_agg = bucket.getAggregations().get("attr_name_agg");
            String attr_name = attr_name_agg.getBuckets().get(0).getKeyAsString();
            ParsedStringTerms attr_value_agg = bucket.getAggregations().get("attr_value_agg");
            List<String> attr_values = attr_value_agg.getBuckets().stream().map((currentBucket) -> {
                String attr_value = currentBucket.getKeyAsString();
                return attr_value;
            }).collect(Collectors.toList());
            SearchResult.AttrVo attrVo = SearchResult.AttrVo.builder()
                    .attrId(bucket.getKeyAsNumber().longValue())
                    .attrName(attr_name)
                    .attrValue(attr_values)
                    .build();
            attrVos.add(attrVo);
        }

        //3、品牌信息 [聚合中获取]
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brand_agg = searchResponse.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            long brand_id = bucket.getKeyAsNumber().longValue();

            ParsedStringTerms brand_img_agg = bucket.getAggregations().get("brand_img_agg");
            String brand_img = brand_img_agg.getBuckets().get(0).getKeyAsString();
            bucket.getAggregations();

            ParsedStringTerms brand_name_agg = bucket.getAggregations().get("brand_name_agg");
            String brand_name = brand_name_agg.getBuckets().get(0).getKeyAsString();
            SearchResult.BrandVo brandVo = SearchResult.BrandVo.builder()
                    .brandId(brand_id)
                    .brandName(brand_name)
                    .brandImg(brand_img)
                    .build();
            brandVos.add(brandVo);
        }


        //4、分类信息 [聚合中获取]
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>() ;
        //通过查看返回值确定类型
        ParsedLongTerms catalog_agg = searchResponse.getAggregations().get("catalog_agg");
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            SearchResult.CatalogVo catalogVo = SearchResult.CatalogVo.builder()
                    .catalogId(Long.parseLong(bucket.getKeyAsString()))
                    .catalogName(catalog_name)
                    .build();
            catalogVos.add(catalogVo);
        }
        //分页信息、页码，总记录数
        List<Integer> pageNaves = new ArrayList<>() ;
        long totalPages = value % EsConstant.PRODUCT_PAGESIZE == 0 ? value / EsConstant.PRODUCT_PAGESIZE : (value / EsConstant.PRODUCT_PAGESIZE + 1);
        for (int i = 0; i <=totalPages ; i++) {
            pageNaves.add(i);
        }
        //构建面包屑导航功能
        List<SearchResult.NavVo> navVos = null ;
        if (searchParam.getAttrs()!=null&& searchParam.getAttrs().size()>0){
            navVos = searchParam.getAttrs().stream().map((attr) -> {
                String[] split = attr.split("_");
                R info = productFeignService.info(Long.parseLong(split[0]));
                String navName = null;
                if (info.getCode() == 0) {
                    LinkedHashMap<Object, Object> attrResponseList = (LinkedHashMap<Object, Object>) info.get("attr");
                    String s = JSON.toJSONString(attrResponseList);
                    TypeReference<AttrResponseVo> typeReference = new TypeReference<AttrResponseVo>() {
                    };
                    AttrResponseVo attrResponseVo = JSON.parseObject(s, typeReference);
                    navName = attrResponseVo.getAttrName();
                }
                //转义问题
                String encode =null ;
                try {
                    encode = URLEncoder.encode(attr, "UTF-8");
                    //浏览器对于空格编码和Java的不同
                    encode= encode.replace("+", "%20");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String queryString = searchParam.getQueryString();
                String replace = queryString.replace("&attrs=" + encode, "");
                String link = "http://search.mall.com/list.html?"+replace;
                SearchResult.NavVo navVo = SearchResult.NavVo.builder()
                        .navValue(split[1])
                        .navName(navName!=null?navName:split[1])
                        .link(link)
                        .build();
                return navVo;
            }).collect(Collectors.toList());
        }else {
            navVos = null ;
        }
        //todo 品牌和分类封装面包屑导航，参照属性实现方式
        SearchResult searchResult = SearchResult.builder()
                //总记录
                .total(value)
                //总页码
                .totalPages((int) totalPages)
                //当前页码
                .pageNum(searchParam.getPageNum())
                //导航页面页码
                .pageNavs(pageNaves)
                //面包屑
                .navs(navVos)
                //所有商品
                .products(skuEsModelList)
                //品牌
                .brands(brandVos)
                //参数
                .attrs(attrVos)
                //分类
                .catalogs(catalogVos)
                //构建
                .build();
        return searchResult ;
    }

    /**
     * 准备检索请求
     * @return
     */
    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        //构建DSL语句
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, searchSourceBuilder);

        /*
         *  模糊匹配，过滤（按照属性、分类、品牌、价格区间、库存）
         */
        //构建boolQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // must 模糊匹配
        if (!StringUtils.isEmpty(searchParam.getKeyword())){
            //1、针对的是title
            boolQuery.must(QueryBuilders.matchQuery("skuTitle",searchParam.getKeyword()));
        }
        //2、filter
        //2、1、 是否携带三级分类ID
        if (searchParam.getCatalog3Id()!=null){
            boolQuery.filter(QueryBuilders.termQuery("catalogId",searchParam.getCatalog3Id()));
        }
        //2.2、品牌参数
        if (searchParam.getBrandId()!=null&&searchParam.getBrandId().size()>0) {
            boolQuery.filter(QueryBuilders.termQuery("brandId",searchParam.getBrandId()));
        }
        //2.3.属性
        if (searchParam.getAttrs()!=null&& searchParam.getAttrs().size()>0){
            for (String attrStr : searchParam.getAttrs()) {
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                String[] s = attrStr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");

                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue" ,attrValues));
                //嵌入式的query
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery) ;
            }
        }
        //2.4.是否拥有库存
        boolQuery.filter(QueryBuilders.termQuery("hasStock",searchParam.getHasStock()==1));

        //2.5. 价格区间
        if (!StringUtils.isEmpty(searchParam.getSkuPrice())) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            //分割【如果两个值就是一个区间】
            String[] split = searchParam.getSkuPrice().split("_");
            if (split.length==2){
                rangeQuery.gte(split[0]).lte(split[1]);
            }else if (split.length ==1 ){
                if (searchParam.getSkuPrice().startsWith("_")){
                    rangeQuery.lte(split[0]);
                }
                if (searchParam.getSkuPrice().endsWith("_")){
                    rangeQuery.gte(split[0]);
                }
            }
            boolQuery.filter(rangeQuery);
        }

        /*
         *  排序、分页、高亮
         */
        //1、 排序
        if (!StringUtils.isEmpty(searchParam.getSort())) {
            String[] split = searchParam.getSort().split("_");
            SortOrder sortOrder = split[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            searchSourceBuilder.sort(split[0],sortOrder);
        }
        //2、分页 【from ,size】 //from = (pageNumber -1)/pageSize
        searchSourceBuilder.from((searchParam.getPageNum()-1)*EsConstant.PRODUCT_PAGESIZE);
        searchSourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);

        //高亮 【只有有模糊匹配的时候才使用高亮】
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        /*
        * 聚合
         */
        //品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        //子聚合 根据ID，得到其他结果
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        searchSourceBuilder.aggregation(brand_agg);
        //分类聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        searchSourceBuilder.aggregation(catalog_agg);
        // 属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName")).size(1);
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue")).size(50);
        attr_agg.subAggregation(attr_id_agg);
        searchSourceBuilder.aggregation(attr_agg);



        //封装所有boolQuery
        searchSourceBuilder.query(boolQuery);
        log.debug("构建的dsl语句{}",searchSourceBuilder.toString());
        return searchRequest ;
    }


}
