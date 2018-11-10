package com.dr.service;

import com.dr.common.ServerResponse;
import com.dr.pojo.UserInfo;

public interface IUserService {

    /**
     * 登录接口
     * */
    ServerResponse login(String username, String password);

    /**
     * 注册接口
     * */
    ServerResponse register(UserInfo userInfo);

    /**
     * 根据用户名返回密保问题，忘记密码
     * */
    ServerResponse forget_get_question(String username);

    /**
     * 提交问题答案
     * 根据用户名，问题，答案，生成一个token
     * */
    ServerResponse forget_get_question(String username, String question, String answer);

    /**
     *忘记密码的重置密码
     * */
    ServerResponse forget_reset_password(String username, String passwordNew, String forgetToken);

    /**
     * 检查用户名或邮箱是否存在
     * */
    ServerResponse check_valid(String str, String type);

    /**
     * 获取登录用户信息
     * */


    /**
     * 登录状态修改密码
     */
    ServerResponse reset_password(String username, String passwordOld, String passwordNew);


    /**
     *登录状态更新个人信息
     * */
    ServerResponse update_information(UserInfo userInfo);

    /**
     * 根据用户Id查询用户信息
     * */
    UserInfo selectUserInfoByUserId(UserInfo userInfo);

}
