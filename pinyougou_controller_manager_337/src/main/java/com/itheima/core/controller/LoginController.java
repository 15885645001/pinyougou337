package com.itheima.core.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @描述: 登录管理
 * @Auther: yanlong
 * @Date: 2019/3/6 18:47:26
 * @Version: 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    /**
     *@描述: 获取当前登录人的名字
     *@Param: []
     *@Return: java.util.Map
    */
    @RequestMapping("/showName")
    public Map<Object,Object> showName(){

        //使用spring-security中的工具类---securitycontextholder 获取用户名  这是从当前线程中获取的
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        HashMap<Object, Object> map = new HashMap<>();

        map.put("username",username);
        map.put("currentTime",new Date());

        return map;
    }
}
