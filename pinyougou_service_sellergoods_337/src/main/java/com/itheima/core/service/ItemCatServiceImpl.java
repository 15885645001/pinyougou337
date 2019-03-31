package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.itheima.core.dao.item.ItemCatDao;
import com.itheima.core.pojo.item.ItemCat;
import com.itheima.core.pojo.item.ItemCatQuery;
import com.itheima.core.pojo.item.ItemQuery;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/8 16:15:46
 * @Version: 1.0
 */
@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ItemCat> findByParentId(Long parentId) {

        //1. 从数据库将所有数据查询出来
        List<ItemCat> itemCats = itemCatDao.selectByExample(null);
        //2. 保存到缓存中
        for (ItemCat itemCat : itemCats) {

            redisTemplate.boundHashOps("itemCat").put(itemCat.getName(),itemCat.getTypeId());
        }


        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);
        List<ItemCat> itemCatList = itemCatDao.selectByExample(itemCatQuery);

        return itemCatList;
    }

    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }
}
