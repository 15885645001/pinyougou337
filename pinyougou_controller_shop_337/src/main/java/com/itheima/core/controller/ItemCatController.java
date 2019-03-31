package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.pojo.item.ItemCat;
import com.itheima.core.service.ItemCatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @描述: 商品分类管理
 * @Auther: yanlong
 * @Date: 2019/3/8 16:05:59
 * @Version: 1.0
 */
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

  /**
     *@描述: 根据父id(上级id)查询商品分类列表
     *@Param: [parentId]
     *@Return: entity.PageResult
    */
    @RequestMapping("/findByParentId")
    public List<ItemCat> findByParentId(Long parentId){
        System.out.println(parentId);

        //Long parentId=0 查询所有一级分类
        //Long parentId=1 查询父id为1的所有二级分类
        //Long parentId=2 查询父id为2的所有三级分类

        return itemCatService.findByParentId(parentId);
    }

    /**
     *@描述: 查询一个商品分类
     *@Param: [id]
     *@Return: com.itheima.core.pojo.item.ItemCat
    */
    @RequestMapping("/findOne")
    public ItemCat findOne(Long id){

        return itemCatService.findOne(id);

    }

    /**
     *@描述: 查询所有商品分类
     *@Param: []
     *@Return: java.util.List<com.itheima.core.pojo.item.ItemCat>
    */
    @RequestMapping("/findAll")
    public List<ItemCat> findAll(){
        return itemCatService.findAll();
    }
}
