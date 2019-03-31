package com.itheima.core.service;

import com.itheima.core.pojo.template.TypeTemplate;
import entity.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/6 10:26:26
 * @Version: 1.0
 */
public interface TypeTemplateService {
    PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate);

    void add(TypeTemplate typeTemplate);

    TypeTemplate findOne(Long id);

    void update(TypeTemplate typeTemplate);

    List<Map> findBySpecList(Long id);
}
