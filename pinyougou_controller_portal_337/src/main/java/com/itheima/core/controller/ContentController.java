package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.pojo.ad.Content;
import com.itheima.core.service.ContentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @描述: 广告管理
 * @Auther: yanlong
 * @Date: 2019/3/14 17:23:45
 * @Version: 1.0
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference  //远程调用
    private ContentService contentService;

    /**
     *@描述: 根据广告分类id查询此分类下的所有广告集合
     *
     *       网站前台之首页轮播图
     *@Param: [categoryId]
     *@Return: java.util.List<com.itheima.core.pojo.ad.Content>
    */
    @RequestMapping("/findByCategoryId")
    public List<Content> findByCategoryId(Long categoryId){
        //System.out.println(categoryId);

        return contentService.findByCategoryId(categoryId);
    }
}
