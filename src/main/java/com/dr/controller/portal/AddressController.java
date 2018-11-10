package com.dr.controller.portal;

import com.dr.common.Const;
import com.dr.common.ServerResponse;
import com.dr.pojo.Shipping;
import com.dr.pojo.UserInfo;
import com.dr.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/shopping")
public class AddressController {

    @Autowired
    IAddressService addressService;
    /**
     * 添加地址
     * */
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session, Shipping shipping){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null)
        {
            return ServerResponse.serverResponseByError("请登录");
        }
        return addressService.add(userInfo.getId(), shipping);
    }

    /**
     * 删除地址
     * */
    @RequestMapping(value = "/del.do")
    public ServerResponse del(HttpSession session, Integer shippingId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null)
        {
            return ServerResponse.serverResponseByError("请登录");
        }
        return addressService.del(userInfo.getId(),shippingId);
    }


    /**
     * 登录状态更新地址
     * */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session, Shipping shipping){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null)
        {
            return ServerResponse.serverResponseByError("请登录");
        }
        return addressService.update(shipping,userInfo.getId());
    }


    /**
     * 选中查看具体的地址
     * */
    @RequestMapping(value = "/select.do")
    public ServerResponse select(HttpSession session, Integer shippingId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null)
        {
            return ServerResponse.serverResponseByError("请登录");
        }
        return addressService.select(shippingId);
    }
    /**
     * 地址列表,分页查询
     **/
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null)
        {
            return ServerResponse.serverResponseByError("请登录");
        }
        return addressService.list(userInfo.getId(),pageNum,pageSize);
    }

}
