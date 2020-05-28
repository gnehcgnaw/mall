package com.beatshadow.mall.ware.service.impl;

import com.alibaba.fastjson.JSON;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.ware.feign.MemberFeignService;
import com.beatshadow.mall.ware.vo.FareVo;
import com.beatshadow.mall.ware.vo.MemberAddressVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.ware.dao.WareInfoDao;
import com.beatshadow.mall.ware.entity.WareInfoEntity;
import com.beatshadow.mall.ware.service.WareInfoService;
import org.springframework.util.StringUtils;


/**
 * @author gnehcgnaw
 */
@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    private MemberFeignService memberFeignService ;

    public WareInfoServiceImpl(MemberFeignService memberFeignService) {
        this.memberFeignService = memberFeignService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<WareInfoEntity> wareInfoEntityQueryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wareInfoEntityQueryWrapper.eq("id",key).or()
                    .like("name",key)
                    .or().like("address",key)
                    .or().like("areacode",key);
        }

        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wareInfoEntityQueryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public FareVo getFare(Long addrId) {
        FareVo fareVo = new FareVo() ;
        //todo
        //查看收货地址的详细信息 ，远程调用mall-member，获取收货地址信息
        R info = memberFeignService.info(addrId);
        if (info.getCode()==0){
            String string = JSON.toJSONString(info.get("memberReceiveAddress"));
            MemberAddressVo memberAddressVo = JSON.parseObject(string, MemberAddressVo.class);
            fareVo.setMemberAddressVo(memberAddressVo);
            fareVo.setFare(new BigDecimal("1"));
        }
        //调用第三方的物流，计算费用

        return fareVo ;
    }


}