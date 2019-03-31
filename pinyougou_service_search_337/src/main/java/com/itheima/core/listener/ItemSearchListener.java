package com.itheima.core.listener;

import com.itheima.core.dao.item.ItemDao;
import com.itheima.core.pojo.item.Item;
import com.itheima.core.pojo.item.ItemQuery;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;

/**
 * @描述:  自定义的消息处理类
 * @Auther: yanlong
 * @Date: 2019/3/24 13:19:19
 * @Version: 1.0
 */
public class ItemSearchListener implements MessageListener {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private SolrTemplate solrTemplate;

    //接收消息
    @Override
    public void onMessage(Message message) {
        //商品id
        ActiveMQTextMessage atm = (ActiveMQTextMessage) message;

        try {
            String id = atm.getText();
            System.out.println("搜索项目接收到的ID:"+id);

            //2.将此商品信息保存到索引库
            ItemQuery itemQuery = new ItemQuery();
            itemQuery.createCriteria().andGoodsIdEqualTo(Long.parseLong(id)).andIsDefaultEqualTo("1");
            List<Item> itemList = itemDao.selectByExample(itemQuery);
            solrTemplate.saveBeans(itemList,1000);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
