package com.beatshadow.mall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.beatshadow.common.to.MemberRespondVo;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.order.feign.CartFeignService;
import com.beatshadow.mall.order.feign.MemberFeignService;
import com.beatshadow.mall.order.feign.WareFeignService;
import com.beatshadow.mall.order.interceptor.LoginUserInterceptor;
import com.beatshadow.mall.order.vo.MemberAddressVo;
import com.beatshadow.mall.order.vo.OrderConfirmVo;
import com.beatshadow.mall.order.vo.OrderItemVo;
import com.beatshadow.mall.order.vo.SkuStockVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.order.dao.OrderDao;
import com.beatshadow.mall.order.entity.OrderEntity;
import com.beatshadow.mall.order.service.OrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


/**
 * @author gnehcgnaw
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private MemberFeignService memberFeignService ;

    private ThreadPoolExecutor threadPoolExecutor ;

    private CartFeignService cartFeignService ;

    private WareFeignService wareFeignService ;
    public OrderServiceImpl(MemberFeignService memberFeignService, ThreadPoolExecutor threadPoolExecutor, CartFeignService cartFeignService, WareFeignService wareFeignService) {
        this.memberFeignService = memberFeignService;
        this.threadPoolExecutor = threadPoolExecutor;
        this.cartFeignService = cartFeignService;
        this.wareFeignService = wareFeignService;
    }


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo() ;
        MemberRespondVo memberRespondVo = LoginUserInterceptor.userInfoToThreadLocal.get();
        log.debug("当前线程：{}" ,Thread.currentThread().getName());
        RequestAttributes mainRequestAttributes = RequestContextHolder.getRequestAttributes();
        //逻辑测试使用一下代码，后续需要使用异步编排进行改造
      /**  log.debug("当前线程：{}" ,Thread.currentThread().getName());
        //同步主线程的请求信息
        RequestContextHolder.setRequestAttributes(mainRequestAttributes);
        //获取收货地址列表
        R memberReceiveAddressListByMemberId = memberFeignService.getMemberReceiveAddressListByMemberId(memberRespondVo.getId());
        if (memberReceiveAddressListByMemberId.getCode()==0){
            List memberReceiveAddressEntityList = (List) memberReceiveAddressListByMemberId.get("memberReceiveAddressEntityList");
            if (memberReceiveAddressEntityList!=null&& memberReceiveAddressEntityList.size()>0){
                List<MemberAddressVo> memberAddressVoList = (List<MemberAddressVo>)memberReceiveAddressEntityList.stream().map((memberReceiveAddress) -> {
                    LinkedHashMap<String, Object> memberReceiveAddressHashMap = (LinkedHashMap<String, Object>) memberReceiveAddress;
                    String string = JSON.toJSONString(memberReceiveAddressHashMap);
                    MemberAddressVo memberAddressVo = JSON.parseObject(string, MemberAddressVo.class);
                    return memberAddressVo;
                }).collect(Collectors.toList());
                orderConfirmVo.setAddress(memberAddressVoList);
            }else {
                orderConfirmVo.setAddress(null);
            }
        }else {
            orderConfirmVo.setAddress(null);
        }

        //同步主线程的请求信息
        log.debug("当前线程：{}" ,Thread.currentThread().getName());
        RequestContextHolder.setRequestAttributes(mainRequestAttributes);
        R r  = cartFeignService.getCurrentUserCartItems();
        if (r.getCode()==0) {
            List cartItemListHashMap =  (List)r.get("cartItemList");
            List currentOrderItemVoList = (List<OrderItemVo>)(cartItemListHashMap.stream().map((orderItemVo) -> {
                LinkedHashMap<String, Object> memberReceiveAddressHashMap = (LinkedHashMap<String, Object>) orderItemVo;
                String string = JSON.toJSONString(memberReceiveAddressHashMap);
                OrderItemVo currentOrderItemVO = JSON.parseObject(string, OrderItemVo.class);
                return currentOrderItemVO;
            }).collect(Collectors.toList()));
            orderConfirmVo.setItems(currentOrderItemVoList);
        }else {
            orderConfirmVo.setItems(null);
        }

        //查询用户积分
        Integer integration = LoginUserInterceptor.userInfoToThreadLocal.get().getIntegration();
        //获取总价
        orderConfirmVo.setIntegration(integration);
        return orderConfirmVo ;
        */

        CompletableFuture<Void> memberAddressListCompletableFuture = CompletableFuture.runAsync(() -> {
            log.debug("当前线程：{}" ,Thread.currentThread().getName());
            //同步主线程的请求信息
            RequestContextHolder.setRequestAttributes(mainRequestAttributes);
            //获取收货地址列表
            R memberReceiveAddressListByMemberId = memberFeignService.getMemberReceiveAddressListByMemberId(memberRespondVo.getId());
            if (memberReceiveAddressListByMemberId.getCode()==0){
                List memberReceiveAddressEntityList = (List) memberReceiveAddressListByMemberId.get("memberReceiveAddressEntityList");
                if (memberReceiveAddressEntityList!=null&& memberReceiveAddressEntityList.size()>0){
                    List<MemberAddressVo> memberAddressVoList = (List<MemberAddressVo>)memberReceiveAddressEntityList.stream().map((memberReceiveAddress) -> {
                        LinkedHashMap<String, Object> memberReceiveAddressHashMap = (LinkedHashMap<String, Object>) memberReceiveAddress;
                        String string = JSON.toJSONString(memberReceiveAddressHashMap);
                        MemberAddressVo memberAddressVo = JSON.parseObject(string, MemberAddressVo.class);
                        return memberAddressVo;
                    }).collect(Collectors.toList());
                    orderConfirmVo.setAddress(memberAddressVoList);
                }else {
                    orderConfirmVo.setAddress(null);
                }
            }else {
                orderConfirmVo.setAddress(null);
            }
        }, threadPoolExecutor);

        //获取选中的购物项
        CompletableFuture<Void> orderItemVoListCompletableFuture = CompletableFuture.runAsync(() -> {
            //同步主线程的请求信息
            log.debug("当前线程：{}" ,Thread.currentThread().getName());
            RequestContextHolder.setRequestAttributes(mainRequestAttributes);
            R r  = cartFeignService.getCurrentUserCartItems();
            if (r.getCode()==0) {
                List cartItemListHashMap =  (List)r.get("cartItemList");
                List currentOrderItemVoList = (List<OrderItemVo>)(cartItemListHashMap.stream().map((orderItemVo) -> {
                    LinkedHashMap<String, Object> memberReceiveAddressHashMap = (LinkedHashMap<String, Object>) orderItemVo;
                    String string = JSON.toJSONString(memberReceiveAddressHashMap);
                    OrderItemVo currentOrderItemVO = JSON.parseObject(string, OrderItemVo.class);
                    return currentOrderItemVO;
                }).collect(Collectors.toList()));
                orderConfirmVo.setItems(currentOrderItemVoList);
            }else {
                orderConfirmVo.setItems(null);
            }

        }, threadPoolExecutor).thenRunAsync(()->{
            //org.springframework.web.util.NestedServletException: Request processing failed; nested exception is java.lang.IllegalStateException: Cannot create a session after the response has been committed
            //	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
            //	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898) ~[spring-webmvc-5.2.5.RELEASE.jar:5.2.5.RELEASE]
            List<OrderItemVo> items = orderConfirmVo.getItems();
            List<Long> skuList = items.stream().map((item) -> {
                Long skuId = item.getSkuId();
                return skuId;
            }).collect(Collectors.toList());

            R skuHasStock = wareFeignService.getSkuHasStock(skuList);
            //库存
            if (skuHasStock.getCode() == 0) {

                ArrayList skuHasStockVoList = (ArrayList<Object>) skuHasStock.get("skuHasStockVoList");
                Map<Long, Boolean> stocksMap = (Map<Long, Boolean>) (skuHasStockVoList.stream().map((stock) -> {
                    String string = JSON.toJSONString(stock);
                    SkuStockVo skuStockVo = JSON.parseObject(string, SkuStockVo.class);
                    return skuStockVo;
                }).collect(Collectors.toMap(SkuStockVo::getSkuId, SkuStockVo::getHasStock)));
                orderConfirmVo.setStocks(stocksMap);
                log.debug("stocksMap is {}",JSON.toJSONString(stocksMap));
            }

        },threadPoolExecutor);

        CompletableFuture.allOf(orderItemVoListCompletableFuture,memberAddressListCompletableFuture).get();
        //查询用户积分
        Integer integration = LoginUserInterceptor.userInfoToThreadLocal.get().getIntegration();
        //获取总价
        orderConfirmVo.setIntegration(integration);


        log.debug(JSON.toJSONString(orderConfirmVo));
        return orderConfirmVo ;
    }


}