package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.core.dao.ad.ContentCategoryDao;
import com.itheima.core.pojo.ad.ContentCategory;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/14 16:12:02
 * @Version: 1.0
 */
@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private ContentCategoryDao contentCategoryDao;

    @Override
    public List<ContentCategory> findAll() {
        List<ContentCategory> list = contentCategoryDao.selectByExample(null);
        return list;
    }
    @Override
    public PageResult findPage(ContentCategory contentCategory, Integer pageNum,
                               Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<ContentCategory> page =
                (Page<ContentCategory>)contentCategoryDao.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }
    @Override
    public void add(ContentCategory contentCategory) {
        contentCategoryDao.insertSelective(contentCategory);
    }
    @Override
    public void edit(ContentCategory contentCategory) {
        contentCategoryDao.updateByPrimaryKeySelective(contentCategory);
    }
    @Override
    public ContentCategory findOne(Long id) {
        ContentCategory category = contentCategoryDao.selectByPrimaryKey(id);
        return category;
    }
    @Override
    public void delAll(Long[] ids) {
        if(ids != null){
            for(Long id : ids){
                contentCategoryDao.deleteByPrimaryKey(id);
            }
        }
    }
}
