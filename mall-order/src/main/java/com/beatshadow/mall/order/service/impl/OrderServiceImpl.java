package com.beatshadow.mall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.beatshadow.common.to.MemberRespondVo;
import com.beatshadow.common.utils.R;
import com.beatshadow.mall.order.constant.OrderConstant;
import com.beatshadow.mall.order.entity.OrderItemEntity;
import com.beatshadow.mall.order.enume.OrderStatusEnum;
import com.beatshadow.mall.order.feign.CartFeignService;
import com.beatshadow.mall.order.feign.MemberFeignService;
import com.beatshadow.mall.order.feign.ProductFeignService;
import com.beatshadow.mall.order.feign.WareFeignService;
import com.beatshadow.mall.order.interceptor.LoginUserInterceptor;
import com.beatshadow.mall.order.service.OrderItemService;
import com.beatshadow.mall.order.to.OrderCreateTo;
import com.beatshadow.mall.order.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.order.dao.OrderDao;
import com.beatshadow.mall.order.entity.OrderEntity;
import com.beatshadow.mall.order.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


/**
 * @author gnehcgnaw
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    private ThreadLocal<OrderSubmitVo> orderSubmitVoThreadLocal = new ThreadLocal<>();

    private MemberFeignService memberFeignService ;

    private ThreadPoolExecutor threadPoolExecutor ;

    private CartFeignService cartFeignService ;

    private WareFeignService wareFeignService ;

    private StringRedisTemplate stringRedisTemplate ;

    private ProductFeignService productFeignService ;

    private OrderItemService orderItemService ;

    public OrderServiceImpl(MemberFeignService memberFeignService, ThreadPoolExecutor threadPoolExecutor, CartFeignService cartFeignService, WareFeignService wareFeignService, StringRedisTemplate stringRedisTemplate, ProductFeignService productFeignService, OrderItemService orderItemService) {
        this.memberFeignService = memberFeignService;
        this.threadPoolExecutor = threadPoolExecutor;
        this.cartFeignService = cartFeignService;
        this.wareFeignService = wareFeignService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.productFeignService = productFeignService;

        this.orderItemService = orderItemService;
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

        //todo 防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        orderConfirmVo.setOrderToken(token);
        stringRedisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN+memberRespondVo.getId(),token,30, TimeUnit.MINUTES);
        log.debug(JSON.toJSONString(orderConfirmVo));
        return orderConfirmVo ;
    }

    /**
     * 下单功能
     *      *      去创建订单
     *      *      验证令牌
     *      *      验证价格
     *      *      锁库存
     *      *   下单成功：来到支付页面
     *      *   下单失败：回到订单确认页，提示重新确认订单信息
     *
     * 事务：
     * 出异常保证回滚，这种是不是有问题：
     *      1、 远程采用的是异常回滚——————假失败，库存扣件了，但是read time，订单都会回滚，但是库存没有回滚
     *      2、 假设远程调用库存成功le， 但是后续出现了问题，订单可以回滚，但是库存不能回滚
     *
     *      本地事务，在分布式系统，只能控制自己的回滚，控制不了其他服务的回滚
     *      分布式事务：最大的原因是网络问题。
     *
     * @param orderSubmitVo
     * @return
     */
    @Transactional
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo) {
        orderSubmitVoThreadLocal.set(orderSubmitVo);
        SubmitOrderResponseVo submitOrderResponseVo = new SubmitOrderResponseVo() ;
        MemberRespondVo memberRespondVo = LoginUserInterceptor.userInfoToThreadLocal.get();
        submitOrderResponseVo.setCode(0);
        //令牌的对比和删除必须保证原子性
        //lua脚本 0失败 ,1删除成功
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else  return 0 end";
        String orderToken = orderSubmitVo.getOrderToken();
        //String redisToken = stringRedisTemplate.opsForValue().get(OrderConstant.USER_ORDER_TOKEN + memberRespondVo.getId());
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(OrderConstant.USER_ORDER_TOKEN + memberRespondVo.getId()), orderToken);
        if (result==0){
            //验证失败
            submitOrderResponseVo.setCode(1);
        }else {
            //令牌验证成功，删除成功
            //进行完整的下单逻辑
            //创建订单号
            //获取购物车中的所选订单
            //创建订单项
            //获取收货地址信息，填充订单
            //计算订单金额

            OrderCreateTo orderCreateTo = orderCreate();
            //验证价格
            BigDecimal payAmount = orderCreateTo.getOrder().getPayAmount();
            BigDecimal payPrice = orderSubmitVo.getPayPrice();
            if (Math.abs(payAmount.subtract(payPrice).doubleValue())<0.01) {
                //金额对比成功
                //保存订单【即生成订单】
                saveOrder(orderCreateTo);
                //锁定库存 ---》只要有异常回滚数据
                //wms_ware_sku
                //订单号，订单项、sku 、skuName count
                WareSkuLockVo wareSkuLockVo = new WareSkuLockVo();
                wareSkuLockVo.setOrderSn(orderCreateTo.getOrder().getOrderSn());
                List<OrderItemEntity> orderItems = orderCreateTo.getOrderItems();
                List<OrderItemVo> orderItemVos = orderItems.stream().map((item) -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setSkuId(item.getSkuId());
                    orderItemVo.setCount(item.getSkuQuantity());
                    orderItemVo.setTitle(item.getSkuName());
                    return orderItemVo;
                }).collect(Collectors.toList());
                wareSkuLockVo.setLocks(orderItemVos);
                //远程采用的是异常回滚——————假失败，库存扣件了，但是read time，订单都会回滚，但是库存没有回滚
                R r = wareFeignService.orderLockStock(wareSkuLockVo);
                if (r.getCode()==0){
                    //锁定成功
                    submitOrderResponseVo.setOrder(orderCreateTo.getOrder());
                }else{
                    //锁定失败
                    submitOrderResponseVo.setCode(3);
                }
            }else {
                //对比失败
                submitOrderResponseVo.setCode(2);
            }
        }
        return submitOrderResponseVo ;
    }

    /**
     * 保存订单数据
     * @param orderCreateTo
     */
    private void saveOrder(OrderCreateTo orderCreateTo) {
        OrderEntity orderEntity = orderCreateTo.getOrder();
        orderEntity.setCreateTime(new Date());
        orderEntity.setModifyTime(new Date());
        this.save(orderEntity);
        List<OrderItemEntity> orderItems = orderCreateTo.getOrderItems();
        orderItemService.saveBatch(orderItems);
    }


    /**
     * 创建订单
     */

    private OrderCreateTo orderCreate(){

        OrderCreateTo orderCreateTo = new OrderCreateTo();

        //1、生产订单号
        String orderSn = IdWorker.getTimeId();

        OrderEntity orderEntity = buildOrder(orderSn);
        orderCreateTo.setOrder(orderEntity);
        //获取到所有的订单项目[如果是异步的话：使用 RequestContextHolder.setRequestAttributes(mainRequestAttributes)]
        List<OrderItemEntity> orderItemEntityList = buildOrderItems(orderSn);
        orderCreateTo.setOrderItems(orderItemEntityList);
        //验价
        computePrice(orderEntity,orderItemEntityList);


        return orderCreateTo ;
    }

    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntityList) {
        //订单价格相关的计算
        BigDecimal total = new BigDecimal("0");
        BigDecimal integrationAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        BigDecimal couponAmount = new BigDecimal("0");
        BigDecimal giftIntegration = new BigDecimal("0");
        BigDecimal giftGrowth = new BigDecimal("0");

        for (OrderItemEntity orderItemEntity : orderItemEntityList) {
            integrationAmount = integrationAmount.add(orderItemEntity.getIntegrationAmount());
            promotionAmount = promotionAmount.add(orderItemEntity.getPromotionAmount());
            couponAmount = couponAmount.add(orderItemEntity.getCouponAmount());
            total = total.add(orderItemEntity.getRealAmount());
            giftIntegration = giftIntegration.add(new BigDecimal(giftIntegration+orderItemEntity.getGiftIntegration().toString()));
            giftGrowth = giftGrowth.add(new BigDecimal(orderItemEntity.getGiftGrowth().toString())) ;
        }
        //订单总额
        orderEntity.setTotalAmount(total);
        //应付总额
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        orderEntity.setPromotionAmount(promotionAmount);
        orderEntity.setCouponAmount(couponAmount);
        orderEntity.setIntegrationAmount(integrationAmount);

        orderEntity.setGrowth(giftGrowth.intValue());
        orderEntity.setIntegration(giftIntegration.intValue());

        orderEntity.setDeleteStatus(0);

    }

    /**
     * 构建订单信息
     * @param orderId
     * @return
     */
    private OrderEntity buildOrder(String orderId) {
        OrderSubmitVo orderSubmitVo = orderSubmitVoThreadLocal.get();
        MemberRespondVo memberRespondVo = LoginUserInterceptor.userInfoToThreadLocal.get();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(orderId);

        //2、计算运费
        R fareR = wareFeignService.getFare(orderSubmitVo.getAddrId());
        if (fareR.getCode()==0){
            LinkedHashMap<String,Object> fareInfoMap = (LinkedHashMap<String, Object>) fareR.get("fare");
            String fareInfoString = JSON.toJSONString(fareInfoMap);
            FareVo fareVo = JSON.parseObject(fareInfoString, FareVo.class);
            BigDecimal fare = fareVo.getFare();

            orderEntity.setFreightAmount(fare);

            MemberAddressVo memberAddressVo = fareVo.getMemberAddressVo();
            orderEntity.setReceiverDetailAddress(memberAddressVo.getDetailAddress());
            orderEntity.setReceiverCity(memberAddressVo.getCity());
            orderEntity.setReceiverName(memberAddressVo.getName());
            orderEntity.setReceiverPhone(memberAddressVo.getPhone());
            orderEntity.setReceiverProvince(memberAddressVo.getProvince());
            orderEntity.setReceiverPostCode(memberAddressVo.getPostCode());
            orderEntity.setReceiverRegion(memberAddressVo.getRegion());

            //设置订单的状态
            orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
            orderEntity.setAutoConfirmDay(7);

            orderEntity.setMemberId(memberRespondVo.getId());
        }
        return orderEntity;
    }

    /**
     * 构建订单项列表
     * @return
     */
    private  List<OrderItemEntity> buildOrderItems(String orderSn) {
        //最后确定购物项的价格
        List<OrderItemEntity> orderItemEntityList = null ;
        R r = cartFeignService.getCurrentUserCartItems();
        if (r.getCode()==0) {
            List cartItemListHashMap =  (List)r.get("cartItemList");
            List currentOrderItemVoList = (List<OrderItemVo>)(cartItemListHashMap.stream().map((orderItemVo) -> {
                LinkedHashMap<String, Object> memberReceiveAddressHashMap = (LinkedHashMap<String, Object>) orderItemVo;
                String string = JSON.toJSONString(memberReceiveAddressHashMap);
                OrderItemVo currentOrderItemVO = JSON.parseObject(string, OrderItemVo.class);
                return currentOrderItemVO;
            }).collect(Collectors.toList()));

            orderItemEntityList = (List<OrderItemEntity> )(currentOrderItemVoList.stream().map((orderItemVo) -> {
                OrderItemEntity orderItemEntity = buildOrderItem((OrderItemVo) orderItemVo);
                orderItemEntity.setOrderSn(orderSn);
                return orderItemEntity;
            }).collect(Collectors.toList()));

        }
        return orderItemEntityList ;
    }

    /**
     * 构建订单项
     * @param orderItemVo
     * @return
     */
    private OrderItemEntity buildOrderItem(OrderItemVo orderItemVo) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        //spu信息
        Long skuId = orderItemVo.getSkuId();
        R spuInfoBySkuId = productFeignService.getSpuInfoBySkuId(skuId);
        if (spuInfoBySkuId.getCode()==0){
            LinkedHashMap<String,Object> skuInfoMap = (LinkedHashMap<String,Object>)spuInfoBySkuId.get("spuInfoEntity");
            String string = JSON.toJSONString(skuInfoMap);
            SpuInfoVo spuInfoVo = JSON.parseObject(string, SpuInfoVo.class);
            orderItemEntity.setSpuId(spuInfoVo.getId());
            orderItemEntity.setSpuBrand(spuInfoVo.getBrandId().toString());
            orderItemEntity.setSpuName(spuInfoVo.getSpuName());
            orderItemEntity.setCategoryId(spuInfoVo.getCatalogId());
        }
        //spu信息
        orderItemEntity.setSkuId(orderItemVo.getSkuId());
        orderItemEntity.setSkuName(orderItemVo.getTitle());
        orderItemEntity.setSkuPic(orderItemVo.getImage());
        orderItemEntity.setSkuPrice(orderItemVo.getPrice());
        orderItemEntity.setSkuAttrsVals(StringUtils.collectionToDelimitedString(orderItemVo.getSkuAttr(),";"));
        orderItemEntity.setSkuQuantity(orderItemVo.getCount());
        //优惠信息【不做】

        //积分成长值信息
        orderItemEntity.setGiftGrowth(orderItemVo.getPrice().multiply(new BigDecimal(orderItemVo.getCount().toString())).intValue());
        orderItemEntity.setGiftIntegration(orderItemVo.getPrice().multiply(new BigDecimal(orderItemVo.getCount().toString())).intValue());

        orderItemEntity.setPromotionAmount(new BigDecimal("0"));
        orderItemEntity.setCouponAmount(new BigDecimal("0"));
        orderItemEntity.setIntegrationAmount(new BigDecimal("0"));

        BigDecimal subtract = orderItemEntity.getSkuPrice()
                .multiply(new BigDecimal(orderItemEntity.getSkuQuantity()))
                .subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getPromotionAmount())
                .subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(subtract);

        return orderItemEntity ;
    }


}