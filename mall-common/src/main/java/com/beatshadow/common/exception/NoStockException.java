package com.beatshadow.common.exception;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/29 11:46
 */
public class NoStockException extends  RuntimeException{
    private Long skuId ;

    public NoStockException(Long skuId) {
        super("商品ID："+skuId+"没有足够的库存了");
    }
}
