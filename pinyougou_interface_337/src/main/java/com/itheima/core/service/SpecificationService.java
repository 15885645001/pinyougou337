package com.itheima.core.service;

import com.itheima.core.pojo.specification.Specification;
import entity.PageResult;
import vo.SpecificationVo;

import java.util.List;
import java.util.Map;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/5 15:51:28
 * @Version: 1.0
 */
public interface SpecificationService {
    PageResult search(Integer page, Integer rows, Specification specification);

    void add(SpecificationVo specificationVo);

    SpecificationVo findOne(Long id);

    void update(SpecificationVo specificationVo);

    void delete(Long[] ids);

    List<Map> selectOptionList();
}
