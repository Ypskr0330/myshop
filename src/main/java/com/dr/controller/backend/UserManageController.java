package com.dr.controller.backend;

import com.dr.common.Const;
import com.dr.common.ServerResponse;
import com.dr.pojo.UserInfo;
import com.dr.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/user")
public class UserManageController {

    @Autowired
    IUserService userService;

    /**
     * 管理员登录
     * */
    @RequestMapping(value = "/login.do")
    public ServerResponse login(HttpSession session, String username, String password){
        ServerResponse serverResponse = userService.login(username,password);
        if (serverResponse.isSuccess()){
            UserInfo userInfo = (UserInfo) serverResponse.getData();
            if (userInfo.getRole() == Const.RoleEnum.ROLE_CUSTOMER.getCode()){
                return ServerResponse.serverResponseByError("无权限登录");
            }
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }
        return ServerResponse.serverResponseBySuccess();
    }

}
