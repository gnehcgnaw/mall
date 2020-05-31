package com.beatshadow.mall.coupon.service.impl;

import com.beatshadow.mall.coupon.entity.SeckillSkuRelationEntity;
import com.beatshadow.mall.coupon.service.SeckillSkuRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.coupon.dao.SeckillSessionDao;
import com.beatshadow.mall.coupon.entity.SeckillSessionEntity;
import com.beatshadow.mall.coupon.service.SeckillSessionService;


/**
 * @author gnehcgnaw
 */
@Slf4j
@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {

    private SeckillSkuRelationService seckillSkuRelationService;

    public SeckillSessionServiceImpl(SeckillSkuRelationService seckillSkuRelationService) {
        this.seckillSkuRelationService = seckillSkuRelationService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                new QueryWrapper<SeckillSessionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SeckillSessionEntity> getLatest3DaySession() {
        log.debug("最近三天的时间间隔是：{}——{},当前时间为：{}",startTime(),endTime(),currentTime());
        //操作数据库时间相差时区解决方案：https://www.cnblogs.com/bignode/p/9310893.html
        List<SeckillSessionEntity> seckillSessionEntities = this.baseMapper.selectList(new QueryWrapper<SeckillSessionEntity>().between("start_time", startTime(), endTime()));
        if (seckillSessionEntities!=null&& seckillSessionEntities.size()>0){
            List<SeckillSessionEntity> seckillSessionDetailEntities = seckillSessionEntities.stream().map(session -> {
                List<SeckillSkuRelationEntity> relationSkus = seckillSkuRelationService.list(new QueryWrapper<SeckillSkuRelationEntity>().eq("promotion_session_id", session.getId()));
                session.setRelationSkus(relationSkus);
                return session;
            }).collect(Collectors.toList());
            return seckillSessionDetailEntities;
        }else {
            return null ;
        }
    }


    private String startTime(){
        LocalDate now = LocalDate.now();
        LocalTime min = LocalTime.MIN;
        LocalDateTime startTime = LocalDateTime.of(now,min);

        return startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
    }

    private String  endTime(){
        LocalDate localDate = LocalDate.now().plusDays(2);
        LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
        return endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
    }


    private String currentTime(){
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
    }

}