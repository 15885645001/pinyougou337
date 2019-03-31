package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.core.dao.specification.SpecificationDao;
import com.itheima.core.dao.specification.SpecificationOptionDao;
import com.itheima.core.pojo.specification.Specification;
import com.itheima.core.pojo.specification.SpecificationOption;
import com.itheima.core.pojo.specification.SpecificationOptionQuery;
import com.itheima.core.pojo.specification.SpecificationQuery;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vo.SpecificationVo;

import java.util.List;
import java.util.Map;

/**
 * @描述: 根据条件查询分页对象
 * @Auther: yanlong
 * @Date: 2019/3/5 15:52:52
 * @Version: 1.0
 */
@Service
@Transactional  //开启事务的注解
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecificationDao specificationDao;

    @Autowired
    private SpecificationOptionDao specificationOptionDao;

    @Override
    public PageResult search(Integer page, Integer rows, Specification specification) {
        //使用分页插件, 分页的初始化参数
        PageHelper.startPage(page, rows);

        //创建条件对象
        SpecificationQuery specificationQuery = new SpecificationQuery();

        //创建内部条件对象
        SpecificationQuery.Criteria criteria = specificationQuery.createCriteria();

        //判断规格名称是否有值
        if (specification.getSpecName() != null && !"".equals(specification.getSpecName().trim())) {
            //模糊条件查询
            criteria.andSpecNameLike("%" + specification.getSpecName() + "%");
        }

        //查询
        Page<Specification> specificationPage = (Page<Specification>) specificationDao.selectByExample(specificationQuery);

        return new PageResult(specificationPage.getTotal(), specificationPage.getResult());
    }

    @Override
    public void add(SpecificationVo specificationVo) {
        //新增规格表  并返回id  修改dao的配置文件即可
        specificationDao.insertSelective(specificationVo.getSpecification());

        //新增规格选项表
        List<SpecificationOption> specificationOptionList = specificationVo.getSpecificationOptionList();
        for (SpecificationOption specificationOption : specificationOptionList) {
            //设置外键
            specificationOption.setSpecId(specificationVo.getSpecification().getId());
            //保存
            specificationOptionDao.insertSelective(specificationOption);
        }
    }

    //查询一个vo对象
    @Override
    public SpecificationVo findOne(Long id) {
        SpecificationVo specificationVo = new SpecificationVo();
        //规格对象
        specificationVo.setSpecification(specificationDao.selectByPrimaryKey(id));

        //规格选项对象结果集
        //创建条件对象
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        specificationOptionQuery.createCriteria().andSpecIdEqualTo(id);
        specificationVo.setSpecificationOptionList(specificationOptionDao.selectByExample(specificationOptionQuery));

        return specificationVo;
    }

    @Override
    public void update(SpecificationVo specificationVo) {
        //修改规格表
        specificationDao.updateByPrimaryKeySelective(specificationVo.getSpecification());

        //修改规格选项表
        //1. 先删除即全部清空
        SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
        specificationOptionQuery.createCriteria().andSpecIdEqualTo(specificationVo.getSpecification().getId());
        specificationOptionDao.deleteByExample(specificationOptionQuery);

        //2. 后添加
        List<SpecificationOption> specificationOptionList = specificationVo.getSpecificationOptionList();
        for (SpecificationOption specificationOption : specificationOptionList) {
            //设置外键
            specificationOption.setSpecId(specificationVo.getSpecification().getId());
            //保存
            specificationOptionDao.insertSelective(specificationOption);
        }
    }

    @Override
    public void delete(Long[] ids) {
        //删除规格表  并删除与之关联的规格选项表
        for (Long id : ids) {
            specificationDao.deleteByPrimaryKey(id);

            //删除规格选项表
            SpecificationOptionQuery specificationOptionQuery = new SpecificationOptionQuery();
            specificationOptionQuery.createCriteria().andSpecIdEqualTo(id);
            specificationOptionDao.deleteByExample(specificationOptionQuery);
        }

    }

    @Override
    public List<Map> selectOptionList() {
        return specificationDao.selectOptionList();
    }
}
