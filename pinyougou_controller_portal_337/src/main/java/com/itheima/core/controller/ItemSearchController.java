package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @描述:  搜索管理
 * @Auther: yanlong
 * @Date: 2019/3/16 09:12:17
 * @Version: 1.0
 */
@RestController
@RequestMapping("/itemsearch")
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    /**
     *@描述: 搜索
     *       入参是简单类型,不用@RequestBody注解
     *       入参是对象(pojo)类型 或 Map, 或 List ..., 必须使用@RequestBody注解
     *@Param: [searchMap]
     *@Return: java.util.Map<java.lang.String,java.lang.Object>
    */
    @RequestMapping("/search")
    public Map<String,Object> search(@RequestBody Map<String,String> searchMap){

        //System.out.println(searchMap);
        return itemSearchService.search(searchMap);
    }
}
