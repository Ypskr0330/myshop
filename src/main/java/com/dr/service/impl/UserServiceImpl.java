package com.dr.service.impl;

import com.dr.common.Const;
import com.dr.common.ServerResponse;
import com.dr.dao.UserInfoMapper;
import com.dr.pojo.UserInfo;
import com.dr.service.IUserService;
import com.dr.utils.MD5Utils;
import com.dr.utils.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserInfoMapper userInfoMapper;
    /**
     * 登录
     * */
    @Override
    public ServerResponse login(String username, String password) {
        //1.参数非空校验
        if (username == null || username.equals("")){
            return ServerResponse.serverResponseByError("用户名不能为空");
        }
        if (password == null || password.equals("")){
            return  ServerResponse.serverResponseByError("密码不能为空");
        }
        //2.检查用户名是否存在
        int result = userInfoMapper.checkUsername(username);
        if (result == 0){
            return ServerResponse.serverResponseByError("用户名不存在");
        }
        //3.根据用户名和密码查找用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsernameAndPassword(username,MD5Utils.getMD5Code(password));
        //没查询出来，说明密码错误
        if (userInfo == null){
            return ServerResponse.serverResponseByError("密码错误");
        }
        //4.返回结果
        //返回结果之前应该将密码设为空
        userInfo.setPassword("");
        return ServerResponse.serverResponseBySuccess(userInfo);
    }

    /**
     * 注册
     * */
    @Override
    public ServerResponse register(UserInfo userInfo) {
        //1.参数非空校验
        if (userInfo == null){
            return ServerResponse.serverResponseByError("不能有空值");
        }
        //2.校验用户名唯一
        int result = userInfoMapper.checkUsername(userInfo.getUsername());
        if (result > 0) {
            return ServerResponse.serverResponseByError("用户名存在");
        }
        //3.邮箱唯一
        int result_email = userInfoMapper.checkEmail(userInfo.getEmail());
        if (result_email > 0){
            return ServerResponse.serverResponseByError("该邮箱已注册");
        }
        //4.注册
        userInfo.setRole(Const.RoleEnum.ROLE_CUSTOMER.getCode());
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
        int count = userInfoMapper.insert(userInfo);
        if (count > 0){
            return ServerResponse.serverResponseBySuccess("注册成功");
        }

        //5.返回结果
        return ServerResponse.serverResponseBySuccess("注册失败");
    }

    @Override
    public ServerResponse forget_get_question(String username) {
        //1.检验非空
        if (username == null || username.equals("")){
            return ServerResponse.serverResponseByError("用户名为空");
        }
        //2.检验用户民是否存在
        int result = userInfoMapper.checkUsername(username);
        if (result == 0){
            return ServerResponse.serverResponseByError("用户名不存在");
        }
        //3.根据用户名查询用户的密保问题
        String question = userInfoMapper.selectQuestionByUsername(username);
        //校验密保问题是否为空
        if (question == null || question.equals("")){
            return ServerResponse.serverResponseByError("密保问题为空");
        }
        return ServerResponse.serverResponseBySuccess(question);
    }


    /**
     * 提交问题答案
     * */
    @Override
    public ServerResponse forget_get_question(String username, String question, String answer) {
        //1.参数校验
        if (username == null || username.equals("")){
            return ServerResponse.serverResponseByError("用户名为空");
        }
        if (question == null || question.equals("")){
            return ServerResponse.serverResponseByError("问题为空");
        }
        if (answer == null || answer.equals("")){
            return ServerResponse.serverResponseByError("答案为空");
        }
        //2.根据用户名，问题，答案查询
        int result = userInfoMapper.selectByUsernameAndQuestionAndAnswer(username,question,answer);
        if (result == 0){
            return ServerResponse.serverResponseByError("答案不正确");
        }
        //3.服务端生成一个token保存，并将token返回客户端
        String forgetToken = UUID.randomUUID().toString();
        //把token保存在缓存中，用到 google guava Cache
        TokenCache.set(username,forgetToken);

        return ServerResponse.serverResponseBySuccess(forgetToken);
    }

    @Override
    public ServerResponse forget_reset_password(String username, String passwordNew, String forgetToken) {
        //1.参数校验
        if (username == null || username.equals("")){
            return ServerResponse.serverResponseByError("用户名为空");
        }
        if (passwordNew == null || passwordNew.equals("")){
            return ServerResponse.serverResponseByError("密码为空");
        }
        if (forgetToken == null || forgetToken.equals("")){
            return ServerResponse.serverResponseByError("token为空");
        }

        //2.token校验
        String token = TokenCache.get(username);
        if (token == null){
            return ServerResponse.serverResponseByError("token过期");
        }
        if (!token.equals(forgetToken)){
            return ServerResponse.serverResponseByError("token无效");
        }
        //3.修改密码

        int result = userInfoMapper.updateUserPassword(username,MD5Utils.getMD5Code(passwordNew));
        if (result > 0){
            return ServerResponse.serverResponseBySuccess("修改成功");
        }
        return ServerResponse.serverResponseByError("修改失败");
    }

    /**
     * 获取登录用户信息
     * */


    /**
     * 检查用户民和邮箱是否存在
     * */
    @Override
    public ServerResponse check_valid(String str, String type) {
        //1.非空校验
        if (str == null || str.equals("")) {
            return ServerResponse.serverResponseByError("用户名或邮箱不能为空");
        }
        if (type == null || type.equals("")) {
            return ServerResponse.serverResponseByError("校验的类型不能为空");
        }
        //2.类型判断
        if (type.equals("username")) {
            int result = userInfoMapper.checkUsername(str);
            if (result > 0) {
                return ServerResponse.serverResponseByError("用户名已存在");
            } else {
                return ServerResponse.serverResponseBySuccess();
            }
        } else if (type.equals("email")) {
            int result = userInfoMapper.checkEmail(str);
            if (result > 0) {
                return ServerResponse.serverResponseByError("邮箱已存在");
            } else {
                return ServerResponse.serverResponseBySuccess();
            }
        } else {
            //3.返回结果
            return ServerResponse.serverResponseByError("参数类型错误");
        }
    }

    /**
     * 登录状态下重置密码
     * */
    @Override
    public ServerResponse reset_password(String username, String passwordOld, String passwordNew) {
        //1.校验参数非空
        if (passwordOld == null || passwordOld.equals("")){
            return ServerResponse.serverResponseByError("旧密码不能为空");
        }
        if (passwordNew == null || passwordNew.equals("")){
            return ServerResponse.serverResponseByError("新密码不能为空");
        }
        //2.根据用户名和密码查询用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsernameAndPassword(username,MD5Utils.getMD5Code(passwordOld));
        if (userInfo == null){
            return ServerResponse.serverResponseByError("旧密码不正确");
        }
        userInfo.setPassword(MD5Utils.getMD5Code(passwordNew));
        int result = userInfoMapper.updatePasswordByPasswordOld(userInfo);
        if (result > 0){
            return ServerResponse.serverResponseBySuccess();
        }
        return ServerResponse.serverResponseByError("重置密码失败");
    }

    /**
     * 登录状态更新个人信息
     * */
    @Override
    public ServerResponse update_information(UserInfo userInfo) {
        //1.参数非空校验
        if (userInfo == null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //2.更新用户信息
        int result = userInfoMapper.updateUserBySelectActive(userInfo);
        if (result > 0){
            return ServerResponse.serverResponseBySuccess("更新成功");
        }
        return ServerResponse.serverResponseByError("更新失败");
    }

    @Override
    public UserInfo selectUserInfoByUserId(UserInfo userInfo) {
        UserInfo userInfo1 = userInfoMapper.selectByPrimaryKey(userInfo.getId());
        return userInfo1;
    }


}
