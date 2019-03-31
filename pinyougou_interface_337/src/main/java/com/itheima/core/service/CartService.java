package com.itheima.core.service;

import com.itheima.core.pojo.item.Item;
import entity.Cart;

import java.util.List;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/25 20:09:23
 * @Version: 1.0
 */
public interface CartService {
    Item findItemByItemId(Long itemId);

    List<Cart> findCartList(List<Cart> cartList);

}
