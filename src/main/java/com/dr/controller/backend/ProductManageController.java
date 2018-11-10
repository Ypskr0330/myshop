package com.dr.controller.backend;

import com.dr.common.Const;
import com.dr.common.ServerResponse;
import com.dr.pojo.Product;
import com.dr.pojo.UserInfo;
import com.dr.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/product")
public class ProductManageController {

    @Autowired
    IProductService productService;

    /**
     * 新增或更新产品
     * */
    @RequestMapping(value = "/save.do")
    public ServerResponse saveOrUpdate(HttpSession session, Product product){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null){
            return ServerResponse.serverResponseByError("未登录");
        }
        //判断权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError("该用户无权限");
        }
        return productService.saveOrUpdate(product);
    }

    /**
     * 产品上下架
     * */
    @RequestMapping(value = "/set_sale_status.do")
    public ServerResponse set_sale_status(HttpSession session, Integer productId, Integer status) {
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError("未登录");
        }
        //判断权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError("该用户无权限");
        }
        return productService.set_sale_status(productId,status);
    }

    /**
     * 后台-获取商品详情
     * */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session, Integer productId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError("未登录");
        }
        //判断权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError("该用户无权限");
        }
        return productService.detail(productId);
    }

    /**
     * 后台-分页查看商品列表
     * */
    @RequestMapping(value ="/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError("未登录");
        }
        //判断权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError("该用户无权限");
        }

        return productService.list(pageNum,pageSize);
    }

    /**
     * 后台-商品查询
     * */
    @RequestMapping(value ="/search.do")
    public ServerResponse search(HttpSession session,
                                 @RequestParam(value = "productId",required = false) Integer productId,
                                 @RequestParam(value = "productName",required = false)String productName,
                                 @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError("未登录");
        }
        //判断权限
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError("该用户无权限");
        }

        return productService.search(productId,productName,pageNum,pageSize);
    }

}
