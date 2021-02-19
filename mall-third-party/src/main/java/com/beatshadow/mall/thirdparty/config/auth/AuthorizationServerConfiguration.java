package com.beatshadow.mall.thirdparty.config.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/2/19 11:20
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


    /**
     * 注入 WebSecurityConfiguration 中配置的 BCryptPasswordEncoder
     */
    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager ;

    private final AuthorizationCodeServices authorizationCodeServices ;

    private final AuthorizationServerTokenServices authorizationServerTokenServices ;

    /**
     * 认证管理器
     * @param passwordEncoder
     * @param clientDetailsService
     * @param authenticationManager
     * @param authorizationCodeServices
     * @param authorizationServerTokenServices
     */

    public AuthorizationServerConfiguration(BCryptPasswordEncoder passwordEncoder, ClientDetailsService clientDetailsService, AuthenticationManager authenticationManager, AuthorizationCodeServices authorizationCodeServices, AuthorizationServerTokenServices authorizationServerTokenServices) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.authorizationCodeServices = authorizationCodeServices;
        this.authorizationServerTokenServices = authorizationServerTokenServices;
    }

    /**
     * 配置客户端相关信息
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置客户端
        clients
                // 使用内存设置
                .inMemory()
                //client_id
                .withClient("client")
                // client_secret
                .secret(passwordEncoder.encode("secret"))
                // 授权类型
                //AuthorizationGrantType
                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")
                // 授权范围
                .scopes("all")
                //跳转到授权页面，如果是true表示不需要跳转，直接给授权码
                .autoApprove(false)
                //注册回调地址
                .redirectUris("http://www.baidu.com");

    }

    /**
     * 认证服务配置
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 认证端点配置
     * @param endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices)
                .tokenServices(authorizationServerTokenServices)
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

}
