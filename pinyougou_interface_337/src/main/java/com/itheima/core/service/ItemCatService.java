package com.itheima.core.service;

import com.itheima.core.pojo.item.ItemCat;
import entity.PageResult;

import java.util.List;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/8 16:15:30
 * @Version: 1.0
 */
public interface ItemCatService {

    List<ItemCat> findByParentId(Long parentId);

    ItemCat findOne(Long id);

    List<ItemCat> findAll();
}
