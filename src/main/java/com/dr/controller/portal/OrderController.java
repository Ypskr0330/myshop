package com.dr.controller.portal;

import com.dr.common.Const;
import com.dr.common.ServerResponse;
import com.dr.pojo.UserInfo;
import com.dr.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    IOrderService orderService;
    /**
     * 创建订单
     * */
    @RequestMapping(value = "/create.do")
    public ServerResponse createOrder(HttpSession session, Integer shippingId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return orderService.createOrder(userInfo.getId(),shippingId);
    }

    /**
     * 取消订单
     * **/
    @RequestMapping(value = "/cancel.do")
    public ServerResponse cancel(HttpSession session, Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return orderService.cancel(userInfo.getId(),orderNo);
    }

    /**
     * 获取订单的商品信息
     * **/
    @RequestMapping(value = "/get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return orderService.get_order_cart_product(userInfo.getId());
    }


    /**
     * 订单List,订单分页查询
     * **/
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session, @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return orderService.list(userInfo.getId(),pageNum,pageSize);
    }

    /**
     * 订单详情detail
     * **/
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session, Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return orderService.selectDetail(orderNo);
    }

}
