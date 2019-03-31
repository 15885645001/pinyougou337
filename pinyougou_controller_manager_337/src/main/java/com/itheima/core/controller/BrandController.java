package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.pojo.good.Brand;
import com.itheima.core.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @描述: 品牌管理
 * @Auther: yanlong
 * @Date: 2019/3/1 18:05:28
 * @Version: 1.0
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     *@描述: 查询所有品牌
     *@Param: []
     *@Return: java.util.List<com.itheima.core.pojo.good.Brand>
    */
    @RequestMapping("/findAll")
    public List<Brand> findAll(){
        return brandService.findAll();
    }

    /**
     *@描述: 根据条件查询分页对象  传入的参数: 当前页  每页的条数  条件对象
     *@Param: []
     *@Return: java.util.List<com.itheima.core.pojo.good.Brand>
    */
    @RequestMapping("/search")
    public PageResult search(Integer pageNum,Integer pageSize,@RequestBody Brand brand){
        System.out.println(pageNum);
        return brandService.search(pageNum,pageSize,brand);
    }

    /**
     *@描述: 根据分页查询所有品牌
     *@Param: []
     *@Return: java.util.List<com.itheima.core.pojo.good.Brand>
     */
    @RequestMapping("/findAllByPage")
    public PageResult findAllByPage(Integer pageNum,Integer pageSize){
        return brandService.findAllByPage(pageNum,pageSize);
    }


    /**
     *@描述: 保存品牌
     *@Param: [brand]
     *@Return: Result
    */
    @RequestMapping("/save")
    public Result save(@RequestBody Brand brand){

        //判断是否保存成功
        //快捷键 : ctrl+alt+T
        try {
            brandService.save(brand);
            return new Result(true,"保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"保存失败");
        }

    }

    /**
     *@描述: 修改品牌
     *@Param: [brand]
     *@Return: entity.Result
    */
    @RequestMapping("/update")
    public Result update(@RequestBody Brand brand){
        //判断是否修改成功
        try {
            brandService.update(brand);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }

    /**
     *@描述: 删除品牌
     *@Param: [brand]
     *@Return: entity.Result
     */
    @RequestMapping("/deletes")
    public Result deletes(Long[] ids){
        //判断是否删除成功
        try {
            brandService.deletes(ids);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }


    /**
     *@描述: 根据id查询一个品牌并回显
     *@Param: [id]
     *@Return: com.itheima.core.pojo.good.Brand
    */
    @RequestMapping("/findById")
    public Brand findById(Long id){
        return brandService.findById(id);
    }

    /**
     *@描述: 查询所有品牌  并返回 List<Map>
     *@Param: []
     *@Return: java.util.List<java.util.Map>
    */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList(){

        return brandService.selectOptionList();
    }
}
