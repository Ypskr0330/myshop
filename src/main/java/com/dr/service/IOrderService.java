package com.dr.service;

import com.dr.common.ServerResponse;

public interface IOrderService {

    /**
     * 创建订单
     * */
    ServerResponse createOrder(Integer userId, Integer shippingId);

    /**
     * 取消订单
     * */
    ServerResponse cancel(Integer userId, Long orderNo);

    /**
     * 获取订单的商品信息
     * **/
    ServerResponse get_order_cart_product(Integer userId);
    /**
     * 订单List，分页查询订单
     * **/
    ServerResponse list(Integer userId, Integer pageNum, Integer pageSize);

    /**
     *查询订单详情
     * */
    ServerResponse selectDetail(Long orderNo);

    /**
     * 根据订单号查询订单信息
     * */
    ServerResponse search(Long orderNo);

    /**
     * 订单发货
     * */
    ServerResponse send_goods(Long orderNo);
}
