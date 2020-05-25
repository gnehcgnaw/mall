package com.beatshadow.mall.auth.controller;

import com.beatshadow.common.constant.AuthServerConstant;
import com.beatshadow.common.exception.BizCodeEnume;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.auth.feign.MemberFeignService;
import com.beatshadow.mall.auth.feign.SmsFeignService;
import com.beatshadow.mall.auth.util.CaptchaGenerator;
import com.beatshadow.mall.auth.vo.UserLoginVo;
import com.beatshadow.mall.auth.vo.UserRegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/25 18:17
 */
@Slf4j
@Controller
public class LoginController {

    private MemberFeignService memberFeignService ;
    private StringRedisTemplate stringRedisTemplate ;

    private SmsFeignService smsFeignService ;

    public LoginController(MemberFeignService memberFeignService, StringRedisTemplate stringRedisTemplate, SmsFeignService smsFeignService) {
        this.memberFeignService = memberFeignService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.smsFeignService = smsFeignService;
    }


    /**
  * {@link com.beatshadow.mall.auth.config.MallWebConfig}
  * @GetMapping("/login.html")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/reg.html")
    public String regPage(){
        return "reg";
    }*/
    /**
     * 验证：http://localhost:20000/sms/sendcode?phone=xxx&code=117710_1590411693800
     * @param phone
     * @return
     */
    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone") String phone ){
        String redisCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_PREFIX + phone);

        if (!StringUtils.isEmpty(redisCode)){
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis()-l<60000){
                return R.error(BizCodeEnume.VAILD_SMS_EXCEPTION.getCode(),BizCodeEnume.VAILD_SMS_EXCEPTION.getMsg());
            }
        }

        //1.接口防刷 //todo
        //2.验证码的再次校验 redis
        String nonce_str = CaptchaGenerator.getNonce_str();
        String code = nonce_str+"_"+System.currentTimeMillis();
        //缓存验证码
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_PREFIX+phone,code,10, TimeUnit.MINUTES);
        smsFeignService.sendCode(phone,nonce_str);
        log.debug("当前手机号为：{}，验证码为：{}",phone,nonce_str);
        return R.ok() ;
    }

    /**
     *
     *
     *  Request method 'POST' not supported]
     *              register --->post   ----->转发reg.html【默认get】
     *
     *              RedirectAttributes redirectAttributes :模拟重定向
     *              重定向是携带session的方式解决的，只要跳到下一个页面session的数据就会被删掉，但是分布式的session会有问题。
     *
     * @param userRegisterVo
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterVo userRegisterVo , BindingResult bindingResult ,
                           //Model model ,
                           RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(Collectors.toMap((fieldError -> {
                return fieldError.getField();
            }), (fieldError) -> {
                return fieldError.getDefaultMessage();
            }));
            //model.addAttribute("errors",errors);
            redirectAttributes.addFlashAttribute("errors",errors);
            //检验出错转发
            //return "forward:/reg.html" ;
            //http://192.168.1.7:20000/reg.html;jsessionid=C64C0A1AD3D14D669C076D3AC0FD0726
            //return "redirect:/reg.html" ;
            //必须使用域名全写
            return "redirect:http://auth.mall.com/reg.html" ;
        }

        //1、校验验证吗
        String code = userRegisterVo.getCode();
        String s = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_PREFIX + userRegisterVo.getPhone());
        if (!StringUtils.isEmpty(s)){
            if (code.equals(s.split("_")[0])){
                //验证码通过
                     //删除验证码,令牌机制
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_PREFIX + userRegisterVo.getPhone());
                //调用远程服务，进行注册
                R register = memberFeignService.register(userRegisterVo);
                //成功
                if (register.getCode()==0){
                    return "redirect:http://auth.mall.com/login.html" ;
                }else {
                    HashMap<String, String> errors = new HashMap<>();
                    errors.put("msg",register.get("msg").toString());
                    redirectAttributes.addFlashAttribute("errors",errors);
                    return "redirect:http://auth.mall.com/reg.html" ;
                }
            }else{
                HashMap<String, String> errors = new HashMap<>();
                errors.put("code","验证码错误");
                redirectAttributes.addFlashAttribute("errors",errors);
                return "redirect:http://auth.mall.com/reg.html" ;
            }
        }else {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("code","验证码错误");
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.mall.com/reg.html" ;
        }

    }


    @PostMapping("/login")
    public String login(UserLoginVo userLoginVo,RedirectAttributes redirectAttributes){
        //远程登录
        R login = memberFeignService.login(userLoginVo);
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
