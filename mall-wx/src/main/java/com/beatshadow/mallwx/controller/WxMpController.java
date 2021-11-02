package com.beatshadow.mallwx.controller;

import com.beatshadow.common.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gnehcgnaw
 * @date 10:41
 */
@RequestMapping("wx-mp")
@RestController
public class WxMpController {
    @GetMapping("/token")
    public String token(@RequestParam(value = "signature") String signature,
                   @RequestParam(value = "timestamp") String timestamp,
                   @RequestParam(value = "nonce") String nonce,
                   @RequestParam(value = "echostr") String echostr){
        return echostr ;
    }
}
