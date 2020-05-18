package com.beatshadow.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.beatshadow.mall.member.feign.CouponFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beatshadow.mall.member.entity.MemberEntity;
import com.beatshadow.mall.member.service.MemberService;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.R;



/**
 * 会员
 *
 * @author gnehcgnaw
 * @email gnehcgnaw@gmail.com
 * @date 2020-05-18 06:58:09
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    private final MemberService memberService;

    private final CouponFeignService couponFeignService ;

    public MemberController(MemberService memberService, CouponFeignService couponFeignService) {
        this.memberService = memberService;
        this.couponFeignService = couponFeignService;
    }

    /**
     * 测试从 coupon 插叙优惠卷
     */
    @RequestMapping("/list-coupon")
    public R listCoupon(){
        return couponFeignService.memberCoupons();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
   // @RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
