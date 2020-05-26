package com.beatshadow.mall.auth.feign;

import com.beatshadow.common.utils.R;
import com.beatshadow.mall.auth.vo.UserLoginVo;
import com.beatshadow.mall.auth.vo.UserRegisterVo;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/26 02:42
 */
@FeignClient("mall-member")
public interface MemberFeignService {
    @PostMapping("/member/member/register")
    public R register(@RequestBody UserRegisterVo userRegisterVo);

    @PostMapping("/member/member/login")
    public R login (@RequestBody UserLoginVo userLoginVo);

    @PostMapping("/member/member/oauth/login/gitee")
    public R oauthLoginByGitee (@RequestBody AuthUser authUser);
}
