package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.core.dao.ad.ContentDao;
import com.itheima.core.pojo.ad.Content;
import com.itheima.core.pojo.ad.ContentQuery;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/14 16:09:58
 * @Version: 1.0
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentDao contentDao;

    @Override
    public List<Content> findAll() {
        List<Content> list = contentDao.selectByExample(null);
        return list;
    }
    @Override
    public PageResult findPage(Content content, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Content> page = (Page<Content>)contentDao.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     *@描述: 广告管理之新增
     *@Param: [content]
     *@Return: void
    */
    @Override
    public void add(Content content) {

        //清除此广告分类id下的所有广告
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());

        contentDao.insertSelective(content);
    }

    /**
     *@描述: 广告管理之修改
     *@Param: [content]
     *@Return: void
    */
    @Override
    public void edit(Content content) {

        Content c = contentDao.selectByPrimaryKey(content.getId());

        if (!c.getCategoryId().equals(content.getCategoryId())){
            redisTemplate.boundHashOps("content").delete(c.getCategoryId());
        }

        // 清除此广告分类id下的所有广告
        redisTemplate.boundHashOps("content").delete(content.getCategoryId());

        contentDao.updateByPrimaryKeySelective(content);
    }
    @Override
    public Content findOne(Long id) {
        Content content = contentDao.selectByPrimaryKey(id);
        return content;
    }

    /**
     *@描述: 广告管理之删除
     *@Param: [ids]
     *@Return: void
    */
    @Override
    public void delAll(Long[] ids) {
        if(ids != null){
            for(Long id : ids){

                Content content = contentDao.selectByPrimaryKey(id);
                // 清除此广告分类id下的所有广告的缓存
                redisTemplate.boundHashOps("content").delete(content.getCategoryId());

                contentDao.deleteByPrimaryKey(id);
            }
        }
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<Content> findByCategoryId(Long categoryId) {

        //1. 通过分类id从缓存中查询轮播图位置上的广告结果集
        List<Content> contentList = (List<Content>) redisTemplate.boundHashOps("content").get(categoryId);//从缓存中取数据
        /*//2. 判断
        if (contentList != null && contentList.size()>0){
            //缓存中有, 直接返回
            return contentList;

        }else {

            //缓存中没有,去数据库查询

            ContentQuery contentQuery = new ContentQuery();
            contentQuery.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo("1");//状态必须设置为1
            //设置排序
            contentQuery.setOrderByClause("sort_order desc");//查看dao(配置文件)的源代码

            contentList = contentDao.selectByExample(contentQuery);

            //查询完放到缓存中一份
            redisTemplate.boundHashOps("content").put(categoryId,contentList);

            return contentList;
        }*/


        //代码优化
        //2. 判断
        if (contentList == null || contentList.size()==0){

            //缓存中没有,去数据库查询

            ContentQuery contentQuery = new ContentQuery();
            contentQuery.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo("1");//状态必须设置为1
            //设置排序
            contentQuery.setOrderByClause("sort_order desc");//查看dao(配置文件)的源代码

            contentList = contentDao.selectByExample(contentQuery);

            //查询完放到缓存中一份
            redisTemplate.boundHashOps("content").put(categoryId,contentList);

            //设置存活时间
            redisTemplate.boundHashOps("content").expire(8, TimeUnit.HOURS);

        }

        //返回
        return contentList;
    }
}
