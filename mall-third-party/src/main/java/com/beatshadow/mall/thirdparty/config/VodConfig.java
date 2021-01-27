package com.beatshadow.mall.thirdparty.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/25 17:50
 */
@RefreshScope
@Configuration
public class VodConfig {
    /**
     * 点播服务接入区域
     */
    @Value("${vod.region-id}")
    private String vodRegionId ;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessKeyId;

    @Value("${spring.cloud.alicloud.secret-key}")
    private String accessKeySecret ;

    @Bean
    public DefaultAcsClient defaultAcsClient(){
        DefaultProfile profile = DefaultProfile.getProfile(vodRegionId, accessKeyId, accessKeySecret);
        return new DefaultAcsClient(profile);
    }
}
