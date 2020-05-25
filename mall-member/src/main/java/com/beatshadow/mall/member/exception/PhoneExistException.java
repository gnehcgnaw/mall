package com.beatshadow.mall.member.exception;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/26 01:22
 */
public class PhoneExistException extends RuntimeException{
    public PhoneExistException() {
        super("手机账号已存在");
    }
}
