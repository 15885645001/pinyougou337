package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.core.dao.seller.SellerDao;
import com.itheima.core.pojo.seller.Seller;
import com.itheima.core.pojo.seller.SellerQuery;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/6 20:00:01
 * @Version: 1.0
 */
@Service
@Transactional  //事务的注解
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerDao sellerDao;

    @Override
    public void add(Seller seller) {
        //商家状态---未审核
        seller.setStatus("0");

        //密码---加盐加密的
        seller.setPassword(new BCryptPasswordEncoder().encode(seller.getPassword()));

        //商家注册的时间
        seller.setCreateTime(new Date());

        sellerDao.insertSelective(seller);
    }

    @Override
    public PageResult search(Integer page, Integer rows, Seller seller) {
        PageHelper.startPage(page,rows);
       /* SellerQuery sellerQuery = new SellerQuery();
        //查询未审核状态
        SellerQuery.Criteria criteria = sellerQuery.createCriteria();
        criteria.andStatusEqualTo(seller.getStatus());
        //判断公司名称是否有值
        if (seller.getName() != null && !"".equals(seller.getName().trim())){
            //模糊条件查询
            criteria.andNameLike("%"+seller.getName().trim()+"%");
        }
        //判断店铺名称是否有值
        if (seller.getNickName() != null && !"".equals(seller.getNickName().trim())){
            //模糊条件查询
            criteria.andNickNameLike("%"+seller.getNickName().trim()+"%");
        }

        Page<Seller> sellerPage = (Page<Seller>) sellerDao.selectByExample(sellerQuery);*/

        Page<Seller> sellerPage = (Page<Seller>) sellerDao.selectByExample(null);


        return new PageResult(sellerPage.getTotal(),sellerPage.getResult());
    }

    @Override
    public Seller findOne(String id) {
        return sellerDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setSellerId(sellerId);
        seller.setStatus(status);
        sellerDao.updateByPrimaryKeySelective(seller);
    }

    /**
     *@描述: 通过用户名查询商家  这里的用户名就是sellerId
     *@Param: [username]
     *@Return: com.itheima.core.pojo.seller.Seller
    */
    @Override
    public Seller findSellerByUsername(String username) {

        return sellerDao.selectByPrimaryKey(username);
    }
}
