package com.dr.controller.backend;

import com.dr.common.Const;
import com.dr.common.ServerResponse;
import com.dr.pojo.UserInfo;
import com.dr.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/category")
public class CategoryManageController {

    @Autowired
    ICategoryService categoryService;
    /**
     * 获取品类子节点
     * */
    @RequestMapping(value = "/get_category.do")
    public ServerResponse get_category(HttpSession session, Integer categoryId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        //判断是否登录
        if (userInfo == null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),
                                                        Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断是否为管理员
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),
                                                        Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        ServerResponse serverResponse =  categoryService.get_category(categoryId);
        return serverResponse;
    }

    /**
     * 增加节点
     * */
    @RequestMapping(value = "/add_category.do")
    public ServerResponse add_category(HttpSession session,
                                       @RequestParam(required = false,defaultValue = "0") Integer parentId,
                                       String categoryName){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        //判断是否登录
        if (userInfo == null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),
                    Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断是否为管理员
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),
                    Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        ServerResponse serverResponse = categoryService.add_category(parentId,categoryName);
        return serverResponse;

    }

    /**
     * 修改节点
     * */
    @RequestMapping(value = "/set_category_name.do")
    public ServerResponse set_category_name(HttpSession session, Integer categoryId, String categoryName){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        //判断是否登录
        if (userInfo == null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),
                    Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断是否为管理员
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),
                    Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }

        ServerResponse serverResponse = categoryService.set_category_name(categoryId,categoryName);
        return serverResponse;
    }

    /**
     * 获取当前分类id及递归子节点categoryId
     * */
    @RequestMapping(value = "/get_deep_category.do")
    public ServerResponse get_deep_category(HttpSession session, Integer categoryId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        //判断是否登录
        if (userInfo == null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),
                    Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断是否为管理员
        if (userInfo.getRole() != Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),
                    Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return categoryService.get_deep_category(categoryId);
    }

}
