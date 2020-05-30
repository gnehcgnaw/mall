package com.beatshadow.mall.order.controller;

import java.util.Arrays;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.beatshadow.mall.order.entity.OrderEntity;
import com.beatshadow.mall.order.service.OrderService;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.R;



/**
 * 订单
 *
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 07:06:14
 */
@Slf4j
@RestController
@RequestMapping("order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("order:order:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("order:order:info")
    public R info(@PathVariable("id") Long id){
		OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("order:order:save")
    public R save(@RequestBody OrderEntity order){
		orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("order:order:update")
    public R update(@RequestBody OrderEntity order){
		orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("order:order:delete")
    public R delete(@RequestBody Long[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 查询到当前用户的订单数据
     * @param params
     * @return
     */
    @PostMapping("/listWithItem")
    public R listWithItem(@RequestBody Map<String,Object> params){
        PageUtils page = orderService.queryPageWithItem(params);
        log.debug("page : {}", JSON.toJSONString(page));
        return R.ok().put("page",page);
    }


}
