package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.core.dao.address.AddressDao;
import com.itheima.core.pojo.address.Address;
import com.itheima.core.pojo.address.AddressQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/26 19:27:06
 * @Version: 1.0
 */
@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Override
    public List<Address> findListByLoginUser(String name) {
        AddressQuery addressQuery = new AddressQuery();
        addressQuery.createCriteria().andUserIdEqualTo(name);
        return addressDao.selectByExample(addressQuery);
    }
}
