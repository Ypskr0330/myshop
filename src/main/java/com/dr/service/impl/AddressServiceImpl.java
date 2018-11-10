package com.dr.service.impl;

import com.dr.common.ServerResponse;
import com.dr.dao.ShippingMapper;
import com.dr.pojo.Shipping;
import com.dr.service.IAddressService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    ShippingMapper shippingMapper;

    /**
     * 添加地址
     * */
    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        //1.参数非空校验
        if (shipping == null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //2.添加地址
        shipping.setUserId(userId);
        shippingMapper.insert(shipping);
        //3.返回结果
        Map<String ,Integer> map = Maps.newHashMap();
        map.put("shoppingId", shipping.getId());
        return ServerResponse.serverResponseBySuccess(map);
    }

    /**
     * 删除地址
     * */
    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        //1.参数非空校验
        if (shippingId == null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //2.删除
        int result =  shippingMapper.deleteAddressByUserIdAndShoppingId(userId,shippingId);
        if (result >0){
            return ServerResponse.serverResponseBySuccess("删除成功");
        }
        //3.返回结果
        return ServerResponse.serverResponseByError("删除失败");
    }

    /**
     * 登录状态更新地址
     * */
    @Override
    public ServerResponse update(Shipping shipping, Integer userId) {
        //1.参数非空校验
        if (shipping == null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        shipping.setUserId(userId);
        //2.更新
         int result = shippingMapper.updateBySelectiveKey(shipping);
        if (result > 0){
            return ServerResponse.serverResponseBySuccess("更新地址成功");
        }
        //3.返回结果
        return ServerResponse.serverResponseByError("更新失败");
    }

    /**
     * 选中查看具体的地址
     * */
    @Override
    public ServerResponse select(Integer shippingId) {
        //1.参数非空校验
        if (shippingId == null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //2.查询
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if (shipping != null){
            return ServerResponse.serverResponseBySuccess(shipping);
        }
        //3.返回结果
        return ServerResponse.serverResponseByError("失败");
    }

    /**
     * 地址列表
     * */
    @Override
    public ServerResponse list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectShippingListByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

}
