package com.itheima.core.listener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;


/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/25 13:27:08
 * @Version: 1.0
 */
public class ItemDeleteListener implements MessageListener {

    @Autowired
    private SolrTemplate solrTemplate;

    //接收消息
    @Override
    public void onMessage(Message message) {
        //商品id
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;

        try {
            String id = atm.getText();
            System.out.println("搜索项目要删除索引时接收到的ID:"+id);

            //2.将此商品信息从索引库中删除出去
            SolrDataQuery query = new SimpleQuery(new Criteria("item_goodsid").is(id));
            solrTemplate.delete(query);
            //手动提交
            solrTemplate.commit();


        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
