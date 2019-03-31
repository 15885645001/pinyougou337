package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.itheima.core.utils.HttpClient;
import com.itheima.core.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/27 09:47:31
 * @Version: 1.0
 */
@Service
@Transactional
public class PayServiceImpl implements PayService {

    @Value("${appid}")
    private String appid;

    @Value("${partner}")
    private String partner;

    @Value("${partnerkey}")
    private String partnerkey;

    @Autowired
    private IdWorker idWorker;

    //连接微信服务器, 走http协议进行远程调用
    @Override
    public Map<String, String> createNative() {

        //订单号
        String out_trade_no = String.valueOf(idWorker.nextId());

        //url  统一下单地址
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        //httpClient相当于浏览器
        HttpClient httpClient = new HttpClient(url);

        //是https协议
        httpClient.setHttps(true);

        //入参或请求参数
        Map<String,String> param = new HashMap<>();

        param.put("appid",appid);
        param.put("mch_id",partner);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("body", "在品优购上买东西不要钱啊?");
        //订单号
        param.put("out_trade_no",out_trade_no);
        param.put("total_fee", "1");
        param.put("spbill_create_ip", "127.0.0.1");
        param.put("notify_url", "http://www.itcast.cn");
        param.put("trade_type", "NATIVE");

        //签名    统一进行加密处理
        try {
            String xml = WXPayUtil.generateSignedXml(param, partnerkey);  //自动添加签名

            //设置入参
            httpClient.setXmlParam(xml);

            //发送请求
            httpClient.post();

            //响应
            String content = httpClient.getContent();

            //转成map
            Map<String, String> map = WXPayUtil.xmlToMap(content);

            //判断
            if ("SUCCESS".equals(map.get("return_code"))){
                //订单号
                map.put("out_trade_no",out_trade_no);
                //金额
                map.put("total_fee","1");

                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) {

        //查询订单的url
        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        HttpClient httpClient = new HttpClient(url);
        httpClient.setHttps(true);

        //请求参数
        Map<String,String> param = new HashMap<>();
        param.put("appid",appid);
        param.put("mch_id",partner);
        param.put("out_trade_no",out_trade_no);
        param.put("nonce_str",WXPayUtil.generateNonceStr());
        try {
            String xml = WXPayUtil.generateSignedXml(param, partnerkey);
            //设置入参
            httpClient.setXmlParam(xml);

            //发送请求
            httpClient.post();

            //响应
            String content = httpClient.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(content);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
