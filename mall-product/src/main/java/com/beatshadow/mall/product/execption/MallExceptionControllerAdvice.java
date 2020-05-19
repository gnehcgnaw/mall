package com.beatshadow.mall.product.execption;

import com.beatshadow.common.exception.BizCodeEnume;
import com.beatshadow.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * 统一异常处理
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/19 12:11
 */
@Slf4j
@ResponseBody
@ControllerAdvice("com.beatshadow.mall.product.controller")
public class MallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException methodArgumentNotValidException){
        log.error("数据校验出现问题{}，异常类型：{}",methodArgumentNotValidException.getMessage(),methodArgumentNotValidException.getClass());
        //获取到BindingResult
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        HashMap<String, String> bindingResultHashMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError)->{
            bindingResultHashMap.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data",bindingResultHashMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable){
        log.error("错误：",throwable);
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }

}
