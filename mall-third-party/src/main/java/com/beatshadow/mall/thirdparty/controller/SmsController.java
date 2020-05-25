package com.beatshadow.mall.thirdparty.controller;

import com.beatshadow.common.utils.R;
import com.beatshadow.mall.thirdparty.component.SmsComponent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送短信
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/25 20:15
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    private SmsComponent smsComponent ;

    public SmsController(SmsComponent smsComponent) {
        this.smsComponent = smsComponent;
    }

    /**
     * 提供给别的服务进行调用的
     * @param phone
     * @param code
     * @return
     */
    @GetMapping("/sendcode")
    public R sendCode(@RequestParam("phone") String phone , @RequestParam("code")String code ){
        smsComponent.sendSmsCode(phone,code);
        return R.ok() ;
    }
}
