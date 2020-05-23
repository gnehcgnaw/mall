package com.beatshadow.mall.search.vo;

import com.beatshadow.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * 查询的返回结果VO
 * @see SearchParam「检索条件」
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/23 15:10
 */
@Data
public class SearchResult {
    //查询到的商品信息
    private List<SkuEsModel> products ;

    //返回分页信息
    //当前页码
    private Integer pageNum ;
    //总记录数
    private Long total ;
    //总页码
    private Integer totalPages ;

    //<---------品牌-------
    private List<BrandVo> brands ;

    //品牌信息
    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName ;
        private String brandImg ;
    }
    //-------end ----------->


    //<---------属性--------
    private List<AttrVo> attrs ;

    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName ;
        private List<String> attrValue ;
    }
    //--------end--------->

    //<-------分类---------
    private List<CatalogVo> catalogs;

    @Data
    public static class CatalogVo{
        private Long catalogId ;
        private String catalogName ;
    }
    //--------end---------->
}
