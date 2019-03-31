package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.pojo.user.User;
import com.itheima.core.service.UserService;
import com.itheima.core.utils.PhoneFormatCheckUtils;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @描述: 用户管理包括:
 *        用户注册
 *        用户登录
 *        用户个人中心
 * @Auther: yanlong
 * @Date: 2019/3/22 14:11:16
 * @Version: 1.0
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    /**
     *@描述: 发送验证码
     *@Param: [phone]
     *@Return: entity.Result
    */

    @RequestMapping("/sendCode")
    public Result sendCode(String phone){
        //判断手机号是否合法
        if (PhoneFormatCheckUtils.isPhoneLegal(phone)){

            try {
                userService.sendCode(phone);
                return new Result(true,"发送成功");
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,"发送失败");
            }
        }else {
            return new Result(false,"手机号不合法");
        }

    }


    /**
     *@描述: 新增用户
     *@Param: [smscode, user]
     *@Return: entity.Result
    */
    @RequestMapping("/add")
    public Result add(String smscode, @RequestBody User user){

            try {
                userService.add(smscode,user);
                return new Result(true,"注册成功");
            } catch (RuntimeException e) {
                return new Result(false,e.getMessage());//RuntimeException异常是从后面抛过来的,动态获取错误信息
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,"注册失败");
            }
    }
}
