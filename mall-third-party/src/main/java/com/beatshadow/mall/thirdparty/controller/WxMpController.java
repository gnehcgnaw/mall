package com.beatshadow.mall.thirdparty.controller;

import com.beatshadow.common.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gnehcgnaw
 * @date 2021-10-13 10:10
 */
@RestController
@RequestMapping("/wx-mp")
public class WxMpController {
    @GetMapping("/token")
    public R token(){
        return null ;
    }
}
