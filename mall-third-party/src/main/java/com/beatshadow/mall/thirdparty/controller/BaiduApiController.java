package com.beatshadow.mall.thirdparty.controller;

import com.alibaba.fastjson.JSON;
import com.beatshadow.mall.thirdparty.component.BaiduAuthComponent;
import com.beatshadow.mall.thirdparty.entity.facematch.BaiduFaceMatchRequestEntity;
import com.beatshadow.mall.thirdparty.util.baiduapi.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/12/18 14:53
 */
@RefreshScope
@RestController
public class BaiduApiController {

    @Value("${baidu.api.ai.face-match-url}")
    private String faceMatchUrl ;

    private final BaiduAuthComponent baiduAuthComponent ;

    public BaiduApiController(BaiduAuthComponent baiduAuthComponent) {
        this.baiduAuthComponent = baiduAuthComponent;
    }

    /**
     * 人脸对比，参见文档地址：https://ai.baidu.com/ai-doc/FACE/Lk37c1tpf
     *
     */
    @PostMapping("/face/match")
    public String faceMatch(@RequestBody List<BaiduFaceMatchRequestEntity> baiduFaceMatchRequestEntities ){
        // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
        String accessToken = baiduAuthComponent.getAuth();
        try {
            String result = HttpUtil.post(faceMatchUrl, accessToken, "application/json", JSON.toJSONString(baiduFaceMatchRequestEntities));
            return result ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
