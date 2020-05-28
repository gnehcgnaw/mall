package com.beatshadow.mall.cart.services.impl;

import com.alibaba.fastjson.JSON;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.cart.feign.ProductFeignService;
import com.beatshadow.mall.cart.interceptor.CartInterceptor;
import com.beatshadow.mall.cart.services.CartService;
import com.beatshadow.mall.cart.vo.Cart;
import com.beatshadow.mall.cart.vo.CartItem;
import com.beatshadow.mall.cart.vo.SkuInfoVo;
import com.beatshadow.mall.cart.vo.UserInfoTo;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/27 14:45
 */
@Service("cartService")
public class CartServiceImpl implements CartService {

    private final String CART_PREFIX = "mall.cart:" ;

    private StringRedisTemplate stringRedisTemplate ;

    private ProductFeignService productFeignService ;

    private ThreadPoolExecutor threadPoolExecutor ;


    public CartServiceImpl(StringRedisTemplate stringRedisTemplate, ProductFeignService productFeignService, ThreadPoolExecutor threadPoolExecutor) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.productFeignService = productFeignService;
        this.threadPoolExecutor = threadPoolExecutor;
    }


    @Override
    public CartItem addToCart(Long skuId, Integer num) {
        CartItem cartItem = new CartItem() ;
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //判断是否是第一次添加
        String string  = (String)cartOps.get(skuId.toString());
        if (StringUtils.isEmpty(string)){
            return createNewProduct(skuId, num, cartItem, cartOps);
        }else {
            CartItem cartItemDb = JSON.parseObject(string, CartItem.class);
            cartItemDb.setCount(cartItemDb.getCount()+num);
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItemDb));
            return cartItemDb ;
        }
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String string  = (String)cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(string, CartItem.class);
        return cartItem;
    }

    @Override
    public Cart getCart() {
        Cart cart = new Cart() ;
        UserInfoTo userInfoTo = CartInterceptor.userInfoToThreadLocal.get();
        //登录了
        if (userInfoTo.getUserId()!=null){
            String caryKey  = CART_PREFIX + userInfoTo.getUserId();
            //1、看看购物车中是否有临时购物项
            String tempCartKey = userInfoTo.getUserKey();
            List<CartItem> tempCartItems = getCartItems(CART_PREFIX+tempCartKey);
            if (tempCartItems!=null){
                //临时购物车有数据，要进行合并
                for (CartItem tempCartItem : tempCartItems) {
                    addToCart(tempCartItem.getSkuId(), tempCartItem.getCount());
                }
                //清理临时购物车
                clearCart(CART_PREFIX+tempCartKey);
            }

            //获取登录后的购物车
            List<CartItem> cartItems = getCartItems(caryKey);
            cart.setItems(cartItems);
        }else{
            //未登录
            String caryKey  = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> cartItems = getCartItems(caryKey);
            cart.setItems(cartItems);

        }
        return cart ;

    }

    /**
     * 清空购物车
     * @param cartKey
     */
    @Override
    public void clearCart(String cartKey) {
       stringRedisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check==1?true:false);
        String string = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),string);

    }

    @Override
    public void countItem(Long skuId, Integer num) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        String string = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),string);
    }

    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    @Override
    public List<CartItem> getCurrentUserCartItems() {
        Long userId = CartInterceptor.userInfoToThreadLocal.get().getUserId();
        if (userId==null){
            return null ;
        }else {
            String cartKey =  CART_PREFIX+userId ;
            List<CartItem> cartItems = getCartItems(cartKey);
            List<CartItem> collect = cartItems.stream().filter(cartItem -> cartItem.getCheck() == true)
                    .map((item)->{
                        //查询新的价格
                        //调用mall-product
                        R r = productFeignService.getPrice(item.getSkuId());
                        if (r.getCode()==0){
                            Double currentPrice = (Double) r.get("price");
                            item.setPrice(BigDecimal.valueOf(currentPrice)==null?new BigDecimal("0"):BigDecimal.valueOf(currentPrice));
                        }
                        return item ;
                    })
                    .collect(Collectors.toList());
            return collect ;
        }
    }

    /**
     * 添加新商品
     * @param skuId
     * @param num
     * @param cartItem
     * @param cartOps
     * @return
     */
    private CartItem createNewProduct(Long skuId, Integer num, CartItem cartItem, BoundHashOperations<String, Object, Object> cartOps) {
        CompletableFuture<SkuInfoVo> skuCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //远程查询当前要添加的商品信息
            R info = productFeignService.info(skuId);
            if (info.getCode()==0){
                LinkedHashMap skuInfoLinkedHashMap = (LinkedHashMap<String,Object>)info.get("skuInfo");
                String string = JSON.toJSONString(skuInfoLinkedHashMap);
                SkuInfoVo skuInfoVo = JSON.parseObject(string, SkuInfoVo.class);
                return skuInfoVo ;
            }else {
                return  null ;
            }
        }, threadPoolExecutor);

        //远程调用获取sku组合
        CompletableFuture<List<String>> skuAttrsCompletableFuture = CompletableFuture.supplyAsync(() -> {
            R skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);
            if (skuSaleAttrValues.getCode()==0){
                List skuSaleAttrValueEntities = (List) skuSaleAttrValues.get("skuSaleAttrValueEntities");
                String string = JSON.toJSONString(skuSaleAttrValueEntities);
                List<String> stringList = JSON.parseArray(string,String.class);
                return stringList ;
            }else {
                return null ;
            }

        }, threadPoolExecutor);
        try {
            List<String> stringList = skuAttrsCompletableFuture.get();
            SkuInfoVo skuInfoVo = skuCompletableFuture.get();
            if (skuInfoVo!=null&&stringList!=null){
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(skuInfoVo.getSkuDefaultImg());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(skuInfoVo.getPrice());
                cartItem.setSkuAttr(stringList);
                cartItem.setTitle(skuInfoVo.getSkuTitle());
                String string = JSON.toJSONString(cartItem);
                cartOps.put(skuId.toString(),string);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return cartItem ;
    }

    private BoundHashOperations<String,Object,Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.userInfoToThreadLocal.get();
        String cartKey = "" ;
        if (userInfoTo.getUserId()!=null){
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        }else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        //购物车结构是一个hash，
        //stringRedisTemplate.opsForHash().get(cartKey,)
        //一下操作更方便
        BoundHashOperations<String,Object,Object> stringStringBoundGeoOperations = stringRedisTemplate.boundHashOps(cartKey);
        return stringStringBoundGeoOperations ;
    }

    private List<CartItem> getCartItems(String cartKey){
        BoundHashOperations<String, Object, Object> stringObjectObjectBoundHashOperations = stringRedisTemplate.boundHashOps(cartKey);
        List<Object> values = stringObjectObjectBoundHashOperations.values();
        List<CartItem> cartItems = null ;
        if (values!=null&&values.size()>0){
            cartItems  = values.stream().map((obj) -> {
                String string = (String) obj ;
                CartItem cartItem = JSON.parseObject(string, CartItem.class);
                return  cartItem ;

            }).collect(Collectors.toList());
        }
        return cartItems ;
    }


}
