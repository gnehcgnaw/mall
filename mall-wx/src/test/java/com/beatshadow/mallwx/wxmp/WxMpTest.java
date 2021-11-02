package com.beatshadow.mall.thirdparty.wxmp;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

/**
 * @author gnehcgnaw
 * @date 10:04
 */
public class WxMpTest {
    //http://beatshadow.natapp1.cc/wx-mp/token
    //mytoken
    private final static String appId = "wx9d2319eccc44338d";
    private final static String appSecret = "cabbb4d3e9451a0d6f9af000b65812e4";
    public void test1(){
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(appId); // 设置微信公众号的appid
        wxMpDefaultConfig.setSecret(appSecret); // 设置微信公众号的app corpSecret
        wxMpDefaultConfig.setToken("..."); // 设置微信公众号的token
        wxMpDefaultConfig.setAesKey("..."); // 设置微信公众号的EncodingAESKey

        WxMpService wxService = new WxMpServiceImpl();// 实际项目中请注意要保持单例，不要在每次请求时构造实例，具体可以参考demo项目
        wxService.setWxMpConfigStorage(wxMpDefaultConfig);


    }
}
