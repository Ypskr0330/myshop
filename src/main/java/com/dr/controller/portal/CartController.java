package com.dr.controller.portal;


import com.dr.common.Const;
import com.dr.common.ServerResponse;
import com.dr.pojo.UserInfo;
import com.dr.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    ICartService cartService;
    /**
     * 向购物车添加商品
     * */
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session, Integer productId, Integer count){
       UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
       if (userInfo == null){
           return ServerResponse.serverResponseByError("请登录");
       }
        return cartService.add(userInfo.getId(),productId,count);
    }

    /**
     * 购物车列表
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return cartService.list(userInfo.getId());
    }

    /**
     * 更新购物车中某个产品的数量
     * */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session, Integer productId, Integer count){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return cartService.update(userInfo.getId(),productId,count);
    }

    /**
     *移除购物车某个产品
     **/
    @RequestMapping(value = "/delete_product.do")
    public ServerResponse delete_product(HttpSession session, String productIds){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return cartService.delete_product(userInfo.getId(),productIds);
    }

    /**
     *购物车中选中某个商品
     * */
    @RequestMapping(value = "/select.do")
    public ServerResponse select(HttpSession session, Integer productId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return cartService.select(userInfo.getId(),productId,Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
    }

    /**
     *购物车中取消选中某个商品
     * */
    @RequestMapping(value = "/un_select.do")
    public ServerResponse un_select(HttpSession session, Integer productId) {
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return cartService.select(userInfo.getId(),productId,Const.CartCheckedEnum.PRODUCT_UNCHECKED.getCode());
    }

    /**
     * 全选
     * */
    @RequestMapping(value = "/select_all.do")
    public ServerResponse select_all(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return cartService.select_all(userInfo.getId(),Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
    }

    /**
     * 取消全选
     * */
    @RequestMapping(value = "/un_select_all.do")
    public ServerResponse un_select_all(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("请登录");
        }
        return cartService.select_all(userInfo.getId(),Const.CartCheckedEnum.PRODUCT_UNCHECKED.getCode());
    }

    /**
     * 购物车中产品数量
     * */
    @RequestMapping(value = "/get_cart_product_count.do")
    public ServerResponse get_cart_product_count(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        int userId = userInfo.getId();
        return cartService.get_cart_product_count(userId);
    }

}
