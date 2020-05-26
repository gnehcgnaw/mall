package com.beatshadow.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.beatshadow.common.exception.BizCodeEnume;
import com.beatshadow.mall.member.exception.PhoneExistException;
import com.beatshadow.mall.member.exception.UsernameExistException;
import com.beatshadow.mall.member.feign.CouponFeignService;
import com.beatshadow.mall.member.vo.MemberLoginVo;
import com.beatshadow.mall.member.vo.MemberRegisterVo;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/oauth/login/gitee")
    public R oauthLoginByGitee (@RequestBody AuthUser authUser) throws Exception{
        MemberEntity memberEntity = memberService.login(authUser);
        if (memberEntity!=null){

            return  R.ok().put("data", JSON.toJSONString(memberEntity)) ;
        }else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVAILD__EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSWORD_INVAILD__EXCEPTION.getMsg());
        }

    }

    @PostMapping("/login")
    public R login (@RequestBody MemberLoginVo memberLoginVo){
        MemberEntity memberEntity = memberService.login(memberLoginVo);
        if (memberEntity!=null){
            return  R.ok().put("data", JSON.toJSONString(memberEntity)) ;
        }else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVAILD__EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSWORD_INVAILD__EXCEPTION.getMsg());
        }

    }

    @PostMapping("/register")
    public R register(@RequestBody MemberRegisterVo memberRegisterVo) {
        try{
            memberService.register(memberRegisterVo);
        }catch (PhoneExistException phoneExistException){
            return R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION.getCode(),BizCodeEnume.PHONE_EXIST_EXCEPTION.getMsg());
        }catch (UsernameExistException usernameExistException){
            return R.error(BizCodeEnume.USER_EXIST_EXCEPTION.getCode(),BizCodeEnume.USER_EXIST_EXCEPTION.getMsg());
        }

        return R.ok() ;
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
