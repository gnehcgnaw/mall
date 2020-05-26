package com.beatshadow.mall.auth.config;

import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/26 13:52
 */
@EnableConfigurationProperties(AuthGiteeProperties.class)
@Configuration
public class AuthGiteeConfig {

    @Bean
    public AuthRequest authRequest(AuthGiteeProperties authGiteeProperties){
        return new AuthGiteeRequest(AuthConfig.builder()
                .clientId(authGiteeProperties.getClientId())
                .clientSecret(authGiteeProperties.getClientSecret())
                .redirectUri(authGiteeProperties.getRedirectUri())
                .build());
    }
}
