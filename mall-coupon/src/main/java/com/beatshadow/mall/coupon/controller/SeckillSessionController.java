package com.beatshadow.mall.coupon.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.beatshadow.mall.coupon.entity.SeckillSessionEntity;
import com.beatshadow.mall.coupon.service.SeckillSessionService;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.R;



/**
 * 秒杀活动场次
 *
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 06:50:28
 */
@RestController
@RequestMapping("coupon/seckillsession")
public class SeckillSessionController {
    private final SeckillSessionService seckillSessionService;

    public SeckillSessionController(SeckillSessionService seckillSessionService) {
        this.seckillSessionService = seckillSessionService;
    }


    /**
     * 获取最近三天的秒杀数据
     * @return
     */
    @GetMapping("/getLatest3DaySession")
    public R getLatest3DaySession(){
        List<SeckillSessionEntity> seckillSessionEntities = seckillSessionService.getLatest3DaySession();
        return R.ok().put("seckillSessionEntities", seckillSessionEntities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("coupon:seckillsession:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillSessionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("coupon:seckillsession:info")
    public R info(@PathVariable("id") Long id){
		SeckillSessionEntity seckillSession = seckillSessionService.getById(id);

        return R.ok().put("seckillSession", seckillSession);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("coupon:seckillsession:save")
    public R save(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.save(seckillSession);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("coupon:seckillsession:update")
    public R update(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.updateById(seckillSession);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:seckillsession:delete")
    public R delete(@RequestBody Long[] ids){
		seckillSessionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
