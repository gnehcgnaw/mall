package com.beatshadow.mall.product.service.impl;

import com.beatshadow.common.constant.ProductConstant;
import com.beatshadow.common.to.SkuHasStockVo;
import com.beatshadow.common.to.SkuReductionTo;
import com.beatshadow.common.to.SpuBoundTo;
import com.beatshadow.common.to.es.SkuEsModel;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.product.entity.*;
import com.beatshadow.mall.product.feign.CouponFeignService;
import com.beatshadow.mall.product.feign.SearchFeignService;
import com.beatshadow.mall.product.feign.WareFeignService;
import com.beatshadow.mall.product.service.*;
import com.beatshadow.mall.product.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    final
    SpuInfoDescService spuInfoDescService;

    final
    SpuImagesService imagesService;

    final
    AttrService attrService;

    final
    ProductAttrValueService attrValueService;

    final
    SkuInfoService skuInfoService;
    final
    SkuImagesService skuImagesService;

    final
    SkuSaleAttrValueService skuSaleAttrValueService;

    final
    CouponFeignService couponFeignService;

    final
    BrandService brandService ;

    final
    CategoryService categoryService ;

    final
    WareFeignService wareFeignService ;

    final
    SearchFeignService searchFeignService ;


    public SpuInfoServiceImpl(SpuInfoDescService spuInfoDescService, SpuImagesService imagesService, AttrService attrService, ProductAttrValueService attrValueService, SkuInfoService skuInfoService, SkuImagesService skuImagesService, SkuSaleAttrValueService skuSaleAttrValueService, CouponFeignService couponFeignService, BrandService brandService, CategoryService categoryService, WareFeignService wareFeignService, SearchFeignService searchFeignService) {
        this.spuInfoDescService = spuInfoDescService;
        this.imagesService = imagesService;
        this.attrService = attrService;
        this.attrValueService = attrValueService;
        this.skuInfoService = skuInfoService;
        this.skuImagesService = skuImagesService;
        this.skuSaleAttrValueService = skuSaleAttrValueService;
        this.couponFeignService = couponFeignService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.wareFeignService = wareFeignService;
        this.searchFeignService = searchFeignService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * //TODO 高级部分完善
     *
     * 调试的时候设置事务的隔离级别       设置为读未提交
     * @param vo
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {

        //1、保存spu基本信息 pms_spu_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo,infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);

        //2、保存Spu的描述图片 pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);


        //3、保存spu的图片集 pms_spu_images
        List<String> images = vo.getImages();
        imagesService.saveImages(infoEntity.getId(),images);


        //4、保存spu的规格参数;pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            AttrEntity id = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(id.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(infoEntity.getId());

            return valueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveProductAttr(collect);


        //5、保存spu的积分信息；gulimall_sms->sms_spu_bounds
        Bounds bounds = vo.getBounds();
        //to模型
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(infoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if(r.getCode() != 0){
            log.error("远程保存spu积分信息失败");
        }


        //5、保存当前spu对应的所有sku信息；

        List<Skus> skus = vo.getSkus();
        if(skus!=null && skus.size()>0){
            skus.forEach(item->{
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if(image.getDefaultImg() == 1){
                        defaultImg = image.getImgUrl();
                    }
                }
                //    private String skuName;
                //    private BigDecimal price;
                //    private String skuTitle;
                //    private String skuSubtitle;
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                //5.1）、sku的基本信息；pms_sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();

                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity->{
                    //返回true就是需要，false就是剔除
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                //5.2）、sku的图片信息；pms_sku_image
                skuImagesService.saveBatch(imagesEntities);

                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);

                    return attrValueEntity;
                }).collect(Collectors.toList());
                //5.3）、sku的销售属性信息：pms_sku_sale_attr_value
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                // //5.4）、sku的优惠、满减等信息；gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if(skuReductionTo.getFullCount() >0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if(r1.getCode() != 0){
                        log.error("远程保存sku优惠信息失败");
                    }
                }

            });
        }

    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        // status=1 and (id=1 or spu_name like xxx)
        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }

        /**
         * status: 2
         * key:
         * brandId: 9
         * catelogId: 225
         */

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {

        //1、查出当前spuId对应的所有的sku信息,品牌名字
        List<SkuInfoEntity> skuInfoEntityList = skuInfoService.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        //5、查询当前sku的所有可以被用来检索的规格属性 【pms_attr的search_type = 1】
        //spu 的 attr
        List<ProductAttrValueEntity> productAttrValueEntities = attrValueService.baseAttrlistforspu(spuId);
        // attrIds
        List<Long> attrIds = productAttrValueEntities.stream().map((attr) -> {
            Long attrId = attr.getAttrId();
            return attrId;
        }).collect(Collectors.toList());
        //指定的所有属性集合中挑出可搜索的所有属性
        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        Set<Long> attrIdsSet = new HashSet<>(searchAttrIds);

        List<SkuEsModel.Attrs> attrsList = productAttrValueEntities.stream().filter((item) -> {
            return attrIdsSet.contains(item.getAttrId());
        }).map((attrValueEntity) -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(attrValueEntity, attrs);
            return attrs;
        }).collect(Collectors.toList());

        Map<Long, Boolean> stockMap = null ;
        try {
            List<Long> skuIdList = skuInfoEntityList.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
            // 6、 发送远程调用、库存系统查询是否有库存
            R wareFeignServiceSkuHasStock = wareFeignService.getSkuHasStock(skuIdList);
            List<Map> skuHasStockVoList = ( List<Map>)wareFeignServiceSkuHasStock.get("skuHasStockVoList");
            List<SkuHasStockVo> hasStockVoList = skuHasStockVoList.stream().map((map) -> {
                Integer skuId =  (Integer) map.get("skuId");
                Boolean hasStock = (Boolean) map.get("hasStock");
                SkuHasStockVo skuHasStockVo = SkuHasStockVo.builder().hasStock(hasStock).skuId( Long.parseLong(String.valueOf(skuId))).build();
                return skuHasStockVo;
            }).collect(Collectors.toList());

            //List<SkuHasStockVo> skuHasStockRData = wareFeignServiceSkuHasStock.getData();
            //将skuid做为key ,stock 作为 value  【技巧操作】
            stockMap = hasStockVoList.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, (item) -> {
                return item.getHasStock();
            }));
        } catch (Exception e) {
            log.error("库存服务调用异常，原因：{}",e);
        }

        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> uoProducts = skuInfoEntityList.stream().map((sku)->{

            //2、组装需要的数据
            SkuEsModel esModel = new SkuEsModel() ;
            BeanUtils.copyProperties(sku,esModel);

            //skuPrice 、skuImg
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());

            //hasStock 、hotScore 、
            //如果远程服务调用异常，那么默认库存是存在的
            if (finalStockMap ==null) {
                esModel.setHasStock(true);
            }else {
                esModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }
            //esModel.setHasStock();
            //todo 热度评分
            //展示设置为0
            esModel.setHotScore(0L);

            //3、查询品牌
            BrandEntity brandEntity = brandService.getById(esModel.getBrandId());
            esModel.setBrandName(brandEntity.getName());
            esModel.setBrandImg(brandEntity.getLogo());

            //4、查询分类的名称信息
            CategoryEntity categoryEntity = categoryService.getById(esModel.getCatalogId());
            esModel.setCatalogName(categoryEntity.getName());

            esModel.setAttrs(attrsList);

            return esModel ;

        }).collect(Collectors.toList());

        //将数据发送给ES服务生成索引并保存
        R r = searchFeignService.productStatusUp(uoProducts);
        if (r.getCode()==0){
            //远程调用成功
            //todo
            //改掉发布状态为已发布
            this.baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }else{
            //远程调用失败
            //接口幂等性
            //重试机制 【feign的重试机制】
        }
    }


}