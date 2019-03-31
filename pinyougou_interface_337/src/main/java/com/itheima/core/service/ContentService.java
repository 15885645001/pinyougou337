package com.itheima.core.service;

import com.itheima.core.pojo.ad.Content;
import entity.PageResult;

import java.util.List;

/**
 * @描述: 广告管理
 * @Auther: yanlong
 * @Date: 2019/3/14 16:04:35
 * @Version: 1.0
 */
public interface ContentService {
    public List<Content> findAll();
    public PageResult findPage(Content content, Integer pageNum, Integer pageSize);
    public void add(Content content);
    public void edit(Content content);
    public Content findOne(Long id);
    public void delAll(Long[] ids);

    List<Content> findByCategoryId(Long categoryId);
}
