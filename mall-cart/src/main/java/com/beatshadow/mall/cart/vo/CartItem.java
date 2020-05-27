package com.beatshadow.mall.cart.vo;



import java.math.BigDecimal;
import java.util.List;

/**
 * 购物项
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/27 13:40
 */
public class CartItem {

    /**
     * sku
     */
    private Long skuId ;
    /**
     * 是否被选中
     */
    private Boolean check = true ;
    /**
     * 标题
     */
    private String title ;

    /**
     * 图片
     */
    private String image ;

    /**
     * 套餐信息
     */
    private List<String> skuAttr ;

    /**
     * 价格
     */
    private BigDecimal price ;
    /**
     * 数量
     */
    private Integer count ;
    /**
     * 小计
     */
    private BigDecimal totalPrice ;


    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getSkuAttr() {
        return skuAttr;
    }

    public void setSkuAttr(List<String> skuAttr) {
        this.skuAttr = skuAttr;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 购物项的总价
     * @return
     */
    public BigDecimal getTotalPrice() {
        return  this.price.multiply(new BigDecimal(""+this.count));
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
