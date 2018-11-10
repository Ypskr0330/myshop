package com.dr.service;

import com.dr.common.ServerResponse;
import com.dr.pojo.Shipping;

public interface IAddressService {
    /**
     * 添加地址
     * */
    ServerResponse add(Integer userId, Shipping shipping);

    /**
     * 删除地址
     * */
    ServerResponse del(Integer userId, Integer shippingId);

    /**
     * 登录状态更新地址
     * */
    ServerResponse update(Shipping shipping, Integer userId);

    /**
     * 选中查看具体的地址
     * */
    ServerResponse select(Integer shippingId);

    /**
     * 地址列表
     * */
    ServerResponse list(Integer userId, Integer pageNum, Integer pageSize);
}
