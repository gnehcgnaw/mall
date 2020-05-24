package com.beatshadow.mall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 检索条件
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/23 14:50
 */
@Data
public class SearchParam {

    //keyword【从搜索框进入】
    private String keyword; //全文匹配关键字       skuTitle

    //从三级分类过来的
    private  Long catalog3Id ;


    //排序条件  销量、热度评分、价格

    //sort = saleCount_asc/desc [销量]
    //sort = salePrice_asc/desc [价格]
    //sort = hotScore_asc/desc  [热度]
    private String sort ;

    //过滤条件
    //hasStock=1/0[是否有货]
    //sukPrice=1_500[价格区间] _500[五百以下] 500_[500以上]
    //brandId [品牌ID]
    //
    private Integer hasStock =1;
    private String skuPrice ;
    //brandId=1&brandId=2
    private List<Long> brandId ;  //多选
    //属性
    //attrs=1_其他
    //多选之间使用冒号进行分割
    // attrs=1_其他:安卓&attr=2_5寸:6寸
    private List<String> attrs ;

    //页码 [默认第一页]
    private Integer pageNum = 1 ;

    //原生查询条件
    private String queryString ;

}
