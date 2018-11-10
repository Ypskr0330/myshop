package com.dr.service;

import com.dr.common.ServerResponse;

public interface ICartService {
    /**
     * 向购物车添加商品
     * */
    ServerResponse add(Integer userId, Integer productId, Integer count);

    /**
     * 购物车列表
     * */
    ServerResponse list(Integer userId);

    /**
     * 更新购物车中某个产品的数量
     * */
    ServerResponse update(Integer userId, Integer productId, Integer count);

    /**
     *移除购物车某个产品
     **/
    ServerResponse delete_product(Integer userId, String productIds);

    /**
     *购物车中选中某个商品
     * */
    ServerResponse select(Integer userId, Integer productId, Integer checked);//选择商品的4个接口复用这一个方法

    /**
     * 取消选中某个商品
     * */
    //ServerResponse un_select(Integer userId,Integer productId);

    /**
     * 全选商品
     * */
    ServerResponse select_all(Integer userId, Integer checked);

    /**
     * 取消全选
     * */
    //ServerResponse un_select_all(Integer userId);

    /**
     * 购物车中产品数量
     * */
    ServerResponse get_cart_product_count(Integer userId);

}
