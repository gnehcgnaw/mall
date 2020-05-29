package com.beatshadow.mall.ware.service.impl;

import com.beatshadow.common.exception.NoStockException;
import com.beatshadow.common.to.SkuHasStockVo;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.ware.feign.ProductFeignService;
import com.beatshadow.mall.ware.vo.OrderItemVo;
import com.beatshadow.mall.ware.vo.WareSkuLockVo;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.ware.dao.WareSkuDao;
import com.beatshadow.mall.ware.entity.WareSkuEntity;
import com.beatshadow.mall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    private WareSkuDao wareSkuDao ;

    private ProductFeignService productFeignService ;

    public WareSkuServiceImpl(WareSkuDao wareSkuDao, ProductFeignService productFeignService) {
        this.wareSkuDao = wareSkuDao;
        this.productFeignService = productFeignService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         * skuId: 1
         * wareId: 2
         */
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if(!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }

        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }


        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1、判断如果还没有这个库存记录新增
        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if(entities == null || entities.size() == 0){
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(skuId);
            skuEntity.setStock(skuNum);
            skuEntity.setWareId(wareId);
            skuEntity.setStockLocked(0);
            //TODO 远程查询sku的名字，如果失败，整个事务无需回滚
            //1、自己catch异常
            //TODO 还可以用什么办法让异常出现以后不回滚？高级
            try {
                R info = productFeignService.info(skuId);
                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");

                if(info.getCode() == 0){
                    skuEntity.setSkuName((String) data.get("skuName"));
                }
            }catch (Exception e){

            }

            wareSkuDao.insert(skuEntity);
        }else{
            wareSkuDao.addStock(skuId,wareId,skuNum);
        }

    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> collect = skuIds.stream().map((skuId) -> {
            SkuHasStockVo skuHasStockVo = SkuHasStockVo.builder().skuId(skuId).build();
            //查询SKU的总库存量 [多个仓库的总和  ]
            //库存的数量-锁定的库存 =真实库存量
            Long count =  baseMapper.getSkuStock(skuId) ;
            skuHasStockVo.setHasStock(count==null?false:count>0);
            return skuHasStockVo;
        }).collect(Collectors.toList());
        return collect ;

    }

    @Transactional(rollbackFor = {NoStockException.class})      //默认只要是运行时异常都会回滚
    @Override
    public Boolean orderLockStock(WareSkuLockVo wareSkuLockVo) {
        //1、按照下单的地址，找到一个最近的仓库，锁定库存。

        List<OrderItemVo> locks = wareSkuLockVo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map((item) -> {
            SkuWareHasStock skuWareHasStock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            //现在找到每个商品在哪里有库存
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(skuId);
            skuWareHasStock.setWareId(wareIds);
            skuWareHasStock.setSkuId(skuId);
            skuWareHasStock.setNum(item.getCount());
            return skuWareHasStock ;
        }).collect(Collectors.toList());

        //锁定库存
        for (SkuWareHasStock skuWareHasStock : collect) {
            Boolean skuStocked = false ;
            Long skuId = skuWareHasStock.getSkuId();
            List<Long> wareIds = skuWareHasStock.getWareId();
            //挨个扣除，如果扣除成功了，就挑出
            if (wareIds==null||wareIds.size()==0){
                throw  new NoStockException(skuId);
            }
            for (Long wareId : wareIds) {
                int row = wareSkuDao.lockSkuStock(skuId,wareId,skuWareHasStock.getNum());
                if (row==1){
                    //成功
                    skuStocked = true ;
                    break;
                }else{
                    //当前仓库失败，进行下一个仓库
                }
            }
            if (!skuStocked){
                //当前商品所有仓库都没有锁住
                throw  new NoStockException(skuId);
            }

        }
        return true ;
    }

    @Data
    class SkuWareHasStock{
        private Long skuId ;
        private List<Long> wareId ;
        private Integer num ;
    }

}