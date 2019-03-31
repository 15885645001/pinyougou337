package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.pojo.address.Address;
import com.itheima.core.service.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @描述:  收获地址管理
 * @Auther: yanlong
 * @Date: 2019/3/26 19:22:32
 * @Version: 1.0
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    /**
     *@描述: 通过用户id查询收获地址结果集
     *@Param: []
     *@Return: java.util.List<com.itheima.core.pojo.address.Address>
    */

    @RequestMapping("/findListByLoginUser")
    public List<Address> findListByLoginUser(){

        //获取当前登陆人
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return addressService.findListByLoginUser(name);
    }

}
