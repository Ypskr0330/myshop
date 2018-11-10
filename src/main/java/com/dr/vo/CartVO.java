package com.dr.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVO {
    //购买商品信息集合
    private List<CartProductVO> cartProductVOList;
    //购物车是否全选
    private boolean allChecked;
    //总价格
    private BigDecimal cartTotalPrice;

    public List<CartProductVO> getCartProductVOList() {
        return cartProductVOList;
    }

    public void setCartProductVOList(List<CartProductVO> cartProductVOList) {
        this.cartProductVOList = cartProductVOList;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }
}
