package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.core.dao.item.ItemDao;
import com.itheima.core.dao.order.OrderDao;
import com.itheima.core.dao.order.OrderItemDao;
import com.itheima.core.pojo.item.Item;
import com.itheima.core.pojo.order.Order;
import com.itheima.core.pojo.order.OrderItem;
import com.itheima.core.utils.IdWorker;
import entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/26 20:37:19
 * @Version: 1.0
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ItemDao itemDao;

    /**
     *@描述: 提交订单(保存订单)
     *@Param: [order]
     *@Return: void
    */
    @Override
    public void add(Order order) {
        //根据用户名查询此用户下的购物车集合(缓存来查)
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cart").get(order.getSellerId());
        if (cartList != null && cartList.size() > 0){
            for (Cart cart : cartList) {
                //订单id (通过分布式id获取)
                Long id = idWorker.nextId();
                order.setOrderId(id);

                //实付金额 = 小计之和
                double total = 0f;

                //支付状态
                order.setStatus("1");

                //订单创建时间
                order.setCreateTime(new Date());

                //订单更新时间
                order.setUpdateTime(new Date());

                //订单来源
                order.setSourceType("2");

                //商家id
                order.setSellerId(cart.getSellerId());



                //一个订单对应多个订单详情表
                List<OrderItem> orderItemList = cart.getOrderItemList();
                for (OrderItem orderItem : orderItemList) {

                    Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                    //订单详情表id
                    long orderItemId = idWorker.nextId();
                    orderItem.setId(orderItemId);

                    //商品id
                    orderItem.setGoodsId(item.getGoodsId());

                    //订单id
                    orderItem.setOrderId(id);

                    //标题
                    orderItem.setTitle(item.getTitle());

                    //价格
                    orderItem.setPrice(item.getPrice());

                    //总金额(即小计)
                    orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));

                    //追加总金额
                    total += orderItem.getTotalFee().doubleValue();

                    //图片
                    orderItem.setPicPath(item.getImage());

                    //商家id
                    orderItem.setSellerId(item.getSellerId());

                    //保存订单详情表
                    orderItemDao.insertSelective(orderItem);
                }

                //设置订单的实付金额
                order.setPayment(new BigDecimal(total));

                //保存订单
                orderDao.insertSelective(order);
            }
        }else {
            return ;
        }

    }
}
