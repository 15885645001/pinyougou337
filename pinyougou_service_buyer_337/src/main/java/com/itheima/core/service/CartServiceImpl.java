package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.core.dao.item.ItemDao;
import com.itheima.core.pojo.item.Item;
import com.itheima.core.pojo.order.OrderItem;
import entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/25 20:09:41
 * @Version: 1.0
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private ItemDao itemDao;
    @Override
    public Item findItemByItemId(Long itemId) {
        return itemDao.selectByPrimaryKey(itemId);
    }

    @Override
    public List<Cart> findCartList(List<Cart> cartList) {
        for (Cart cart : cartList) {
            //商品结果集
            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                //有的数据是： 库存ID 数量
                Item item = findItemByItemId(orderItem.getItemId());
                //图片
                orderItem.setPicPath(item.getImage());
                //标题
                orderItem.setTitle(item.getTitle());
                //价格
                orderItem.setPrice(item.getPrice());
                //小计
                orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*orderItem.getNum()));

                //商家名称
                cart.setSellerName(item.getSeller());
            }
        }
        return cartList;
    }

}
