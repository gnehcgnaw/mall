package com.beatshadow.mall.order.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/28 03:20
 */
public class OrderConfirmVo {


    public OrderConfirmVo() {
    }
    /**
     * 选中的购物项目
     *      mall-cart
     */
    @Getter
    @Setter
    private List<OrderItemVo> items ;
    @Getter

    /**
     * 收货地址列表   ums_member_receive_address
     *      mall-member
     */
    @Setter
    private List<MemberAddressVo> address;
    /**
     * 发票信息
     */

    /**
     * 优惠价信息
     *  mall-coupon
     *
     *  会员积分
     *  mall-member
     */
    @Getter
    @Setter
    private Integer integration ;
    /**
     * 订单总额
     */

   // private BigDecimal total ;

    /**
     * 应付价格
     */
    //private BigDecimal payPrice ;

    /**
     * 有可能有网络延迟等情况，会多次被下单，那么这是我们需要一个令牌【防止重复提交】
     * //todo 幂等性
     * @return
     */
    @Getter
    @Setter
    private String orderToken ;

    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal("0");
        List<OrderItemVo> items = getItems();
        if (items!=null){
            for (OrderItemVo item : items) {
                //为什么使用item的price吗，因为此时price是最新值
                total =total.add(item.getPrice().multiply(new BigDecimal(item.getCount())));
            }
        }
        return total;
    }

    public BigDecimal getPayPrice() {
     /*   BigDecimal total = new BigDecimal("0");
        List<OrderItemVo> items = getItems();
        if (items!=null){
            for (OrderItemVo item : items) {
                //为什么使用item的price吗，因为此时price是最新值
                total =total.add(item.getPrice().multiply(new BigDecimal(item.getCount())));
            }
        }*/
        return getTotal();
    }


}
