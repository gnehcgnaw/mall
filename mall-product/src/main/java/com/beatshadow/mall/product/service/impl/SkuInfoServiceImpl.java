package com.beatshadow.mall.product.service.impl;

import com.beatshadow.mall.product.entity.SkuImagesEntity;
import com.beatshadow.mall.product.entity.SpuInfoDescEntity;
import com.beatshadow.mall.product.service.*;
import com.beatshadow.mall.product.vo.SkuItemSaleAttrVo;
import com.beatshadow.mall.product.vo.SkuItemVo;
import com.beatshadow.mall.product.vo.SpuItemAttrGroupVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.product.dao.SkuInfoDao;
import com.beatshadow.mall.product.entity.SkuInfoEntity;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    private SkuImagesService skuImagesService ;

    private SpuInfoDescService spuInfoDescService ;

    private AttrGroupService attrGroupService ;

    private SkuSaleAttrValueService skuSaleAttrValueService ;

    public SkuInfoServiceImpl(SkuImagesService skuImagesService, SpuInfoDescService spuInfoDescService, AttrGroupService attrGroupService, SkuSaleAttrValueService skuSaleAttrValueService) {
        this.skuImagesService = skuImagesService;
        this.spuInfoDescService = spuInfoDescService;
        this.attrGroupService = attrGroupService;
        this.skuSaleAttrValueService = skuSaleAttrValueService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        /**
         * key:
         * catelogId: 0
         * brandId: 0
         * min: 0
         * max: 0
         */
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("sku_id",key).or().like("sku_name",key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){

            queryWrapper.eq("catalog_id",catelogId);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(catelogId)){
            queryWrapper.eq("brand_id",brandId);
        }

        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min)){
            queryWrapper.ge("price",min);
        }

        String max = (String) params.get("max");

        if(!StringUtils.isEmpty(max)  ){
            try{
                BigDecimal bigDecimal = new BigDecimal(max);

                if(bigDecimal.compareTo(new BigDecimal("0"))==1){
                    queryWrapper.le("price",max);
                }
            }catch (Exception e){

            }

        }


        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public SkuItemVo item(Long skuId) {
        //sku基本信息获取 sku_info表 //标题、副标题、价格
        SkuInfoEntity skuInfoEntity = getById(skuId);
        Long catalogId = skuInfoEntity.getCatalogId();
        //sku图片信息， sku_imgs表
        List<SkuImagesEntity> skuImagesEntityList = skuImagesService.getImagesBySkuId(skuId);


        //商品介绍【共享spu的属性】
        Long spuId = skuInfoEntity.getSpuId();
        SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(skuId);

        //当前sku对应的sku的组合信息
        List<SpuItemAttrGroupVo> spuItemAttrGroupVoList = attrGroupService.getAttrGroupWithAttrsBySpuId(spuId,catalogId);
        //规格属性与包装——规格参数信息[销售属性]
        List<SkuItemSaleAttrVo> skuItemSaleAttrVos = skuSaleAttrValueService.getSaleAttrBySpuId(spuId);

        SkuItemVo skuItemVo = SkuItemVo.builder()
                .info(skuInfoEntity)
                .desp(spuInfoDescEntity)
                .groupAttrs(spuItemAttrGroupVoList)
                .saleAttr(skuItemSaleAttrVos)
                .images(skuImagesEntityList)
                .build();
        return skuItemVo;
    }


}