package com.itheima.core.service;

import com.itheima.core.pojo.seller.Seller;
import entity.PageResult;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/6 19:58:35
 * @Version: 1.0
 */
public interface SellerService {
    void add(Seller seller);

    PageResult search(Integer page, Integer rows, Seller seller);

    Seller findOne(String id);

    void updateStatus(String sellerId, String status);

    Seller findSellerByUsername(String username);
}
