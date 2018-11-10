package com.dr.controller.backend;

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
@RequestMapping(value = "/manage/order")
public class OrderManageController {

    @Autowired
    IOrderService orderService;

    /**
     * 订单list，分页查看订单
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("未登录");
        }
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError("无权限登录");
        }

        return orderService.list(null,pageNum,pageSize);
    }

    /**
     * 按订单号查询
     * */
    @RequestMapping(value = "/search.do")
    public ServerResponse search(HttpSession session, Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("未登录");
        }
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError("无权限登录");
        }
        return orderService.selectDetail(orderNo);
    }

    /**
     * 订单详情
     * */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session, Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("未登录");
        }
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError("无权限登录");
        }
        return orderService.selectDetail(orderNo);
    }

    /**
     * 订单发货
     * */
    @RequestMapping(value = "/send_goods.do")
    public ServerResponse send_goods(HttpSession session, Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("未登录");
        }
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError("无权限登录");
        }
        return orderService.send_goods(orderNo);
    }
}
