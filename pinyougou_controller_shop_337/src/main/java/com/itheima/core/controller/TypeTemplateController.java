package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.pojo.template.TypeTemplate;
import com.itheima.core.service.TypeTemplateService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @描述:  模板管理
 * @Auther: yanlong
 * @Date: 2019/3/6 10:19:16
 * @Version: 1.0
 */
@SuppressWarnings("all")//该注解的作用是:出现重复代码不要提示
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;

    /**
     *@描述: 根据条件查询模板分页对象
     *@Param: [page, rows, typeTemplate]
     *@Return: entity.PageResult
    */
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody(required = false)TypeTemplate typeTemplate){
        return typeTemplateService.search(page,rows,typeTemplate);
    }

    /**
     *@描述: 新增模板
     *@Param: [typeTemplate]
     *@Return: entity.Result
    */
    @RequestMapping("/add")
    public Result add(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.add(typeTemplate);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败了");
        }
    }

    /**
     *@描述: 根据id查询一个模板对象
     *@Param: [id]
     *@Return: com.itheima.core.pojo.template.TypeTemplate
    */
    @RequestMapping("/findOne")
    public TypeTemplate findOne(Long id){

        return typeTemplateService.findOne(id);
    }

    /**
     *@描述: 修改一个模板对象
     *@Param: [typeTemplate]
     *@Return: entity.Result
    */
    @RequestMapping("/update")
    public Result update(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.update(typeTemplate);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败了");
        }
    }

    /**
     *@描述: 通过模板id 查询模板列表数据
     *@Param: [id]
     *@Return: java.util.List<java.util.Map>
    */
    @RequestMapping("/findBySpecList")
    public List<Map> findBySpecList(Long id){
        return typeTemplateService.findBySpecList(id);
    }
}
