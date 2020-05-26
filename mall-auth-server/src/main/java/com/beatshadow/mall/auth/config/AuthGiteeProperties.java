package com.beatshadow.mall.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/26 14:02
 */
@ConfigurationProperties(prefix = "auth.gitee")
@Data
public class AuthGiteeProperties {
    private String clientId ;
    private String clientSecret ;
    private String redirectUri ;
}
