package com.dr.controller;

import com.dr.pojo.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class TestController {
    @RequestMapping(value = "/login.do1")
    public UserInfo userInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername("hudie");
        return userInfo;
    }
}
