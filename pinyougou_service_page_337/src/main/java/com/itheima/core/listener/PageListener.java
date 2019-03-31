package com.itheima.core.listener;

import com.itheima.core.service.StaticPageService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/24 13:40:02
 * @Version: 1.0
 */
public class PageListener implements MessageListener {

    @Autowired
    private StaticPageService staticPageService; //静态页面服务层(静态页面实现类)


    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;

        try {
            String id = atm.getText();
            System.out.println("静态化项目接收到的ID:"+id);

            //3.静态化:  模板 + 数据 = 输出
            staticPageService.index(Long.parseLong(id));

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
