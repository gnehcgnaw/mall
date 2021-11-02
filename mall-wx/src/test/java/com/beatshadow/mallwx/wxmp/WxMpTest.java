package com.beatshadow.mallwx.wxmp;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * @author gnehcgnaw
 * @date 10:04
 */
public class WxMpTest {
    private final static String appId = "wx9d2319eccc44338d";
    private final static String appSecret = "cabbb4d3e9451a0d6f9af000b65812e4";
    private  WxMpService wxService ;

    @Before
    public void init(){
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(appId); // 设置微信公众号的appid
        wxMpDefaultConfig.setSecret(appSecret); // 设置微信公众号的app corpSecret
        wxMpDefaultConfig.setToken("mytoken"); // 设置微信公众号的token
        wxMpDefaultConfig.setAesKey("..."); // 设置微信公众号的EncodingAESKey
        wxService = new WxMpServiceImpl();// 实际项目中请注意要保持单例，不要在每次请求时构造实例，具体可以参考demo项目
        wxService.setWxMpConfigStorage(wxMpDefaultConfig);
    }

    /**
     * 获取公众号用户列表，即所有用户的公众号openid
     * @throws WxErrorException
     */
    @Test
    public void userList() throws WxErrorException {
        WxMpUserList wxMpUserList = wxService.getUserService().userList(null);
        System.out.println(wxMpUserList);
    }


    /**
     * 通过公众号openid获取用户的unionid
     */
    @Test
    public void userInfo() throws WxErrorException {
        WxMpUser wxMpUser = wxService.getUserService().userInfo("ogA155s92NQoiJWi7XvLbsNW8cq0");
        System.out.println(wxMpUser);
    }

    /**
     * 推送服务号消息
     * @throws WxErrorException
     */
    @Test
    public void sendTemplateMsg() throws WxErrorException {
        WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage.builder().toUser("ogA155s92NQoiJWi7XvLbsNW8cq0").templateId("0uoAUqcg5koYGo-oOG3oF81Fe-ebFVFc1ycaLDjUqY4").build();
        wxMpTemplateMessage.addData(new WxMpTemplateData("name", "张三", "#191970"));
        wxMpTemplateMessage.addData(new WxMpTemplateData("sex", "男", "#191970"));
        wxService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
    }
}
