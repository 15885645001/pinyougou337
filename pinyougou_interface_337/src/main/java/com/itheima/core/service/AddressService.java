package com.itheima.core.service;

import com.itheima.core.pojo.address.Address;

import java.util.List;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/26 19:26:46
 * @Version: 1.0
 */
public interface AddressService {

    List<Address> findListByLoginUser(String name);
}
