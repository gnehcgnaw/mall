package com.beatshadow.mall.product.vo;

import com.beatshadow.mall.product.entity.SkuImagesEntity;
import com.beatshadow.mall.product.entity.SkuInfoEntity;
import com.beatshadow.mall.product.entity.SpuInfoDescEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/25 01:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuItemVo {
    /**
     *  //sku基本信息获取 sku_info表 //标题、副标题、价格
     *     //sku图片信息， sku_imgs表
     *     //当前sku对应的sku的组合信息
     *     //商品介绍【共享spu的属性】
     *     //规格属性与包装——规格参数信息
     */
    private SkuInfoEntity info ;

    private List<SkuImagesEntity> images ;

    private SpuInfoDescEntity desp ;

    private List<SkuItemSaleAttrVo> saleAttr ;

    private List<SpuItemAttrGroupVo> groupAttrs ;

}
