package com.itheima.core.service;

import java.util.Map;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/27 09:47:17
 * @Version: 1.0
 */
public interface PayService {
    Map<String,String> createNative();

    Map<String,String> queryPayStatus(String out_trade_no);
}
