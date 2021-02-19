package com.beatshadow.mall.thirdparty.config.auth;


/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/2/19 15:53
 */
public enum AuthorizationGrantType {

    /**
     * 授权码模式(即先登录获取code,再获取token)
     */
    AUTHORIZATION_CODE("authorization_code"),
    /**
     * 密码模式(将用户名,密码传过去,直接获取token)
     */
    PASSWORD("password"),
    /**
     *  客户端模式(无用户,用户向客户端注册,然后客户端以自己的名义向’服务端’获取资源)
     */
    CLIENT_CREDENTIALS("client_credentials"),
    /**
     * 简化模式(在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash)
     */
    IMPLICIT("implicit"),
    /**
     * 刷新access_token
     */
    REFRESH_TOKEN("refresh_token");

    private String value ;


    AuthorizationGrantType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
