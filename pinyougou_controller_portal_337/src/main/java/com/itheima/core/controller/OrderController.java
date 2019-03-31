package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.pojo.order.Order;
import com.itheima.core.service.OrderService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @描述:  订单管理
 * @Auther: yanlong
 * @Date: 2019/3/26 20:35:05
 * @Version: 1.0
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    /**
     *@描述:  提交订单
     *@Param: [order]
     *@Return: entity.Result
    */

    @RequestMapping("/add")
    public Result add(@RequestBody Order order){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            order.setSellerId(name);
            orderService.add(order);
            return new Result(true,"提交订单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"提交订单失败");
        }
    }
}
