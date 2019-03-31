package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.service.PayService;
import entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @描述: 支付管理
 * @Auther: yanlong
 * @Date: 2019/3/27 09:41:13
 * @Version: 1.0
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference
    private PayService payService;

    /**
     * @描述: 订单号和金额以及二维码的生成
     * @Param: []
     * @Return: java.util.Map<java.lang.String   ,   java.lang.String>
     */

    @RequestMapping("/createNative")
    public Map<String, String> createNative() {
        return payService.createNative();
    }


    /**
     * @描述: 查询此订单的支付状态
     * @Param: [out_trade_no]
     * @Return: entity.Result
     */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) {
        try {
            int i = 0;
            while (true) {
                Map<String, String> map = payService.queryPayStatus(out_trade_no);

                if ("NOTPAY".equals(map.get("trade_state"))) {

                    Thread.sleep(5000);
                    i++;
                    if (i >= 60) {
                        return new Result(false, "支付超时");
                    }
                } else {
                    return new Result(true, "支付成功");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "支付失败");
        }
    }
}
