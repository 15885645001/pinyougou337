package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.core.dao.user.UserDao;
import com.itheima.core.pojo.user.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/22 14:22:39
 * @Version: 1.0
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    //注: 在这里不是连接数据库, 而是连接activemq

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Destination smsDestination;

    //发消息
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private UserDao userDao;

    @Override
    public void sendCode(String phone) {
        //随机生成6为验证码
        String randomNumeric = RandomStringUtils.randomNumeric(6);

        //保存验证码到缓存中, 是为了进行比对验证码正确与否
        //验证码与手机绑定在一起
        redisTemplate.boundValueOps(phone).set(randomNumeric);
        //设置验证码存活时间
        //redisTemplate.boundValueOps(phone).expire(1, TimeUnit.MINUTES);

        //发消息
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                //手机号
                mapMessage.setString("phoneNumbers",phone);
                //验证码
                mapMessage.setString("templateParam","{'code':'"+randomNumeric+"'}");
                //签名
                mapMessage.setString("signName","品美优购");
                //模板
                mapMessage.setString("templateCode","SMS_161595641");

                return mapMessage;
            }
        });

    }

    @Override
    public void add(String smscode, User user) {
        //首先从缓存中取出验证码
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();

        //1. 判断验证码是否失效或存在
        if (code == null){
            throw new RuntimeException("验证码失效");//抛出异常在页面上显示
        }

        //2. 如果没失效,进行比对,判断验证码是否正确
        if (code.equals(smscode)){
            //保存用户
            //设置创建时间
            user.setCreated(new Date());
            //设置更新时间
            user.setUpdated(new Date());

            //密码加密....

            userDao.insertSelective(user);
        }else {
            throw new RuntimeException("验证码不正确");
        }
    }
}
