package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.pojo.specification.Specification;
import com.itheima.core.service.SpecificationService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.SpecificationVo;

import java.util.List;
import java.util.Map;

/**
 * @描述: 规格管理: 即规格的增删改查
 * @Auther: yanlong
 * @Date: 2019/3/5 15:22:13
 * @Version: 1.0
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;

    /**
     *@描述:  根据条件查询分页对象  传入的参数: 当前页  每页的条数  条件对象
     *@Param: [page, rows, specification]
     *@Return: entity.PageResult
    */
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody(required = false) Specification specification){

        return specificationService.search(page,rows,specification);
    }

    /**
     *@描述: 新增
     *@Param: [specificationVo]
     *@Return: entity.Result
    */
    @RequestMapping("/add")
    public Result add(@RequestBody SpecificationVo specificationVo){

        try {
            specificationService.add(specificationVo);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }

    /**
     *@描述: 修改
     *@Param: [specificationVo]
     *@Return: entity.Result
    */
    @RequestMapping("/update")
    public Result update(@RequestBody SpecificationVo specificationVo){

        try {
            specificationService.update(specificationVo);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败了");
        }
    }

    /**
     *@描述: 根据规格id查询一个
     *@Param: [id]
     *@Return: vo.SpecificationVo
    */
    @RequestMapping("/findOne")
    public SpecificationVo findOne(Long id){

        return specificationService.findOne(id);
    }

    /**
     *@描述: 批量删除
     *@Param: [ids]
     *@Return: entity.Result
    */
    @RequestMapping("/delete")
    public Result delete(Long[] ids){

        try {
            specificationService.delete(ids);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }

    /**
     *@描述: 查询所有规格 并返回List<Map>
     *@Param: []
     *@Return: java.util.List<java.util.Map>
    */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList(){
        return specificationService.selectOptionList();
    }
}
