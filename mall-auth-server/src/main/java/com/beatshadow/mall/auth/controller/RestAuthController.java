package com.beatshadow.mall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.auth.feign.MemberFeignService;
import com.beatshadow.mall.auth.vo.MemberRespondVo;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;


/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/26 13:47
 */
@Slf4j
@Controller
@RequestMapping("/oauth")
public class RestAuthController {

    private AuthRequest authRequest ;

    private MemberFeignService memberFeignService ;

    public RestAuthController(AuthRequest authRequest, MemberFeignService memberFeignService) {
        this.authRequest = authRequest;
        this.memberFeignService = memberFeignService;
    }

    /**
     * 第三方登录请求地址
     * @param response
     * @throws IOException
     */
    @RequestMapping("/render")
    public void renderAuth(HttpServletResponse response) throws IOException {
        //生成授权地址,跳转访问
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    /**
     * 回调
     * @param callback
     * @return
     */
    @GetMapping("/callback")
    public String login(AuthCallback callback , RedirectAttributes redirectAttributes , HttpSession httpSession)  {
        AuthResponse authResponse = authRequest.login(callback);
        log.debug("authResponse is {}", JSON.toJSONString(authResponse));

        if (authResponse.ok()){
            //知道是那个社交用户了，如果是第一次社交登录，首先要注册，以后就用当前的用户信息
            //所以有两种情况：注册，或 登录
            AuthUser authUser = (AuthUser) authResponse.getData();
            R login = memberFeignService.oauthLoginByGitee(authUser);
            MemberRespondVo memberRespondVo = JSON.parseObject((String) login.get("data"), MemberRespondVo.class);
            httpSession.setAttribute("loginUser",memberRespondVo);

            return getString(redirectAttributes, login);
        }else {
            return "redirect:http://auth.mall.com/login.html";
        }

    }

    static String getString(RedirectAttributes redirectAttributes, R login) {
        if (login.getCode()==0){
            return "redirect:http://mall.com" ;
        }else {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("msg",login.get("msg").toString());
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.mall.com/login.html" ;
        }
    }
}
