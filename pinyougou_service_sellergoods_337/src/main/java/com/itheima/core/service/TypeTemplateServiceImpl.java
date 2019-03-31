package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.core.dao.specification.SpecificationOptionDao;
import com.itheima.core.dao.template.TypeTemplateDao;
import com.itheima.core.pojo.specification.SpecificationOption;
import com.itheima.core.pojo.specification.SpecificationOptionQuery;
import com.itheima.core.pojo.template.TypeTemplate;
import com.itheima.core.pojo.template.TypeTemplateQuery;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/6 10:27:09
 * @Version: 1.0
 */
@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {
    @Autowired
    private TypeTemplateDao typeTemplateDao;

    @Autowired
    private SpecificationOptionDao specificationOptionDao;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate) {

        //1. 从数据库查询模板结果集  保存到缓存一份
        List<TypeTemplate> typeTemplates = typeTemplateDao.selectByExample(null);
        //2. 通过模板id查询
        for (TypeTemplate template : typeTemplates) {
            List<Map> brandList = JSON.parseArray(template.getBrandIds(), Map.class);
            redisTemplate.boundHashOps("brandList").put(template.getId(),brandList);

            List<Map> specList = findBySpecList(template.getId());
            redisTemplate.boundHashOps("specList").put(template.getId(),specList);
        }


        PageHelper.startPage(page,rows);
        TypeTemplateQuery typeTemplateQuery = new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = typeTemplateQuery.createCriteria();
        //判断分类模板名称是否有值
        if (typeTemplate.getName() != null && !"".equals(typeTemplate.getName().trim())){

            criteria.andNameLike("%"+typeTemplate.getName()+"%");
        }

        Page<TypeTemplate> typeTemplatePage = (Page<TypeTemplate>) typeTemplateDao.selectByExample(typeTemplateQuery);
        //System.out.println(typeTemplatePage.getTotal());
        //System.out.println(typeTemplatePage.getResult());
        return new PageResult(typeTemplatePage.getTotal(),typeTemplatePage.getResult());
    }

    @Override
    public void add(TypeTemplate typeTemplate) {
        typeTemplateDao.insertSelective(typeTemplate);
    }

    @Override
    public TypeTemplate findOne(Long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(TypeTemplate typeTemplate) {
        typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
    }

    @Override
    public List<Map> findBySpecList(Long id) {
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);

        String specIds = typeTemplate.getSpecIds();

        //将字符串转为对象
        List<Map> listMap = JSON.parseArray(specIds, Map.class);

        for (Map map : listMap) {

            SpecificationOptionQuery query = new SpecificationOptionQuery();
            //object可以先转成基本类型, 再强转成长整型
            query.createCriteria().andSpecIdEqualTo((long)(Integer)map.get("id"));
            List<SpecificationOption> options = specificationOptionDao.selectByExample(query);
            map.put("options",options);
        }
        return listMap;
    }
}
