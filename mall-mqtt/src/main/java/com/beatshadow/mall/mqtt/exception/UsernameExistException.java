package com.beatshadow.mall.mqtt.exception;

/**
 * 异常处理
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/19 16:48
 */
public class UsernameExistException extends RuntimeException{
    public UsernameExistException() {
        super("用户名已存在");
    }
}