package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.pojo.seller.Seller;
import com.itheima.core.service.SellerService;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @描述: 商家管理
 * @Auther: yanlong
 * @Date: 2019/3/6 19:54:10
 * @Version: 1.0
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    /**
     *@描述: 新增商家
     *@Param: [seller]
     *@Return: entity.Result
    */
    @RequestMapping("/add")
    public Result add(@RequestBody Seller seller){
        try {
            sellerService.add(seller);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }
}
