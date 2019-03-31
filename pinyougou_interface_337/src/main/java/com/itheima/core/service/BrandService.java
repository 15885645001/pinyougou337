package com.itheima.core.service;

import com.itheima.core.pojo.good.Brand;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/1 19:07:32
 * @Version: 1.0
 */
public interface BrandService {

    /**
     *@描述: 查询所有品牌
     *@Param: []
     *@Return: java.util.List<com.itheima.core.pojo.good.Brand>
    */
    List<Brand> findAll();

    /**
     *@描述: 根据分页查询所有品牌
     *@Param: [pageNum, pageSize]
     *@Return: entity.PageResult
    */
    PageResult findAllByPage(Integer pageNum, Integer pageSize);

    /**
     *@描述: 保存品牌
     *@Param: [brand]
     *@Return: void
    */
    void save(Brand brand);

    /**
     *@描述: 根据id查询一个品牌并回显
     *@Param: [id]
     *@Return: com.itheima.core.pojo.good.Brand
    */
    Brand findById(Long id);

    /**
     *@描述: 修改品牌
     *@Param: [brand]
     *@Return: void
    */
    void update(Brand brand);

    /**
     *@描述: 删除品牌 或 批量删除
     *@Param: [ids]
     *@Return: void
    */
    void deletes(Long[] ids);

    /**
     *@描述: 根据条件查询分页对象
     *@Param: [pageNum, pageSize, brand]
     *@Return: entity.PageResult
    */
    PageResult search(Integer pageNum, Integer pageSize, Brand brand);

    List<Map> selectOptionList();
}
