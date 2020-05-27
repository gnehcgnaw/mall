package com.beatshadow.mall.cart.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车
 *      大部分都是需要计算的
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/27 13:40
 */
public class Cart {

    /**
     * 购物项集合
     */
    private List<CartItem>  items ;

    /**
     * 商品总数
     */
    private Integer countNum ;

    /**
     * 商品的种类
     *
     */
    private Integer countType ;

    /**
     * 购物车中的总价
     */
    private BigDecimal totalAmount ;

    /**
     * 优惠价格
     */
    private BigDecimal reduce = new BigDecimal("0.00") ;

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getCountNum() {
        countNum = 0 ;
        if (items!=null && items.size()>0){
            for (CartItem item : items) {
                countNum+=item.getCount();
            }
        }
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public Integer getCountType() {
        countType = 0 ;
        if (items!=null && items.size()>0){
            for (CartItem item : items) {
                countNum+=1;
            }
        }
        return countType;
    }

    public void setCountType(Integer countType) {
        this.countType = countType;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        //计算购物享总价
        if (items!=null && items.size()>0){
            for (CartItem item : items) {
                BigDecimal totalPrice = item.getTotalPrice();
                amount =  amount.add(totalPrice);
            }
        }
        //减去优惠总价
        BigDecimal totalAmount = amount.subtract(getReduce());
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }

}
