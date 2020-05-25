package com.beatshadow.mall.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/25 21:11
 */
@Data
public class UserRegisterVo {
    @NotEmpty(message = "用户名必须提交")
    @Length(min = 6,max = 18,message = "用户名必须是6-18位字符")
    private String userName ;

    @NotEmpty(message = "密码必须提交")
    @Length(min = 6,max = 18,message = "密码必须是6-18位字符")
    private String password ;

    @NotEmpty(message = "验证码必须填写")
    @Pattern(regexp = "^1[3-9]\\d{9}$" ,message = "手机号格式不正确")
    private String phone ;
    @NotEmpty(message = "验证码必须填写")
    private String code ;
}
