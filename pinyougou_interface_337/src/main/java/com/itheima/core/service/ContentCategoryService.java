package com.itheima.core.service;

import com.itheima.core.pojo.ad.ContentCategory;
import entity.PageResult;

import java.util.List;

/**
 * @描述:  广告分类管理
 * @Auther: yanlong
 * @Date: 2019/3/14 16:07:38
 * @Version: 1.0
 */
public interface ContentCategoryService {
    public List<ContentCategory> findAll();
    public PageResult findPage(ContentCategory contentCategory, Integer pageNum, Integer pageSize);
    public void add(ContentCategory contentCategory);
    public void edit(ContentCategory contentCategory);
    public ContentCategory findOne(Long id);
    public void delAll(Long[] ids);
}
