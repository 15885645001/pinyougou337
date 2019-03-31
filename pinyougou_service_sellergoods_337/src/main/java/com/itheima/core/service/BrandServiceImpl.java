package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.core.dao.good.BrandDao;
import com.itheima.core.pojo.good.Brand;
import com.itheima.core.pojo.good.BrandQuery;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/1 18:57:39
 * @Version: 1.0
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandDao brandDao;

    /**
     *@描述: 查询所有品牌
     *@Param: []
     *@Return: java.util.List<com.itheima.core.pojo.good.Brand>
    */
    @Override
    public List<Brand> findAll() {
    return brandDao.selectByExample(null);
}

    /**
     *@描述: 根据分页查询所有品牌
     *@Param: [pageNum, pageSize]
     *@Return: entity.PageResult
    */
    @Override
    public PageResult findAllByPage(Integer pageNum, Integer pageSize) {
        //使用分页插件, 分页的初始化参数
        PageHelper.startPage(pageNum,pageSize);

        //查询结果
        Page<Brand> brandPage = (Page<Brand>) brandDao.selectByExample(null);

        //返回总条数 结果集
        return new PageResult(brandPage.getTotal(),brandPage.getResult());
    }

    @Override
    public void save(Brand brand) {
        brandDao.insertSelective(brand);
    }

    /**
     *@描述: 根据id查询一个品牌并回显
     *@Param: [id]
     *@Return: com.itheima.core.pojo.good.Brand
    */
    @Override
    public Brand findById(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    /**
     *@描述: 修改品牌
     *@Param: [brand]
     *@Return: void
    */
    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    /**
     *@描述:  删除品牌 或 批量删除
     *@Param: [ids]
     *@Return: void
    */
    @Override
    public void deletes(Long[] ids) {
        //遍历数组获取数组中的值
        //判断
        if (ids != null && ids.length > 0 ){
            for (Long id : ids) {
                brandDao.deleteByPrimaryKey(id);
            }
        }

        //批量删除
        //创建branddquery对象
        BrandQuery brandQuery = new BrandQuery();
        //brandQuery.createCriteria()固定用法   数组转换为集合 Arrays.asList
        brandQuery.createCriteria().andIdIn(Arrays.asList(ids));
        brandDao.deleteByExample(brandQuery);
    }

    /**
     *@描述: 根据条件查询分页对象
     *@Param: [pageNum, pageSize, brand]
     *@Return: entity.PageResult
    */
    @Override
    public PageResult search(Integer pageNum, Integer pageSize, Brand brand) {
        //使用分页插件, 分页的初始化参数
        PageHelper.startPage(pageNum,pageSize);

        //创建条件对象
        BrandQuery brandQuery = new BrandQuery();
        //创建内部条件对象
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        //判断品牌名称是否有值
        if (brand.getName() != null && !"".equals(brand.getName().trim())){
            //模糊条件查询
            criteria.andNameLike("%"+brand.getName()+"%");
        }
        //判断品牌首字母是否有值
        if (brand.getFirstChar() != null && !"".equals(brand.getFirstChar().trim())){
            //等于某个条件进行查询
            criteria.andFirstCharEqualTo(brand.getFirstChar().trim());
        }

        //查询结果
        Page<Brand> brandPage = (Page<Brand>) brandDao.selectByExample(brandQuery);

        //返回总条数 结果集
        return new PageResult(brandPage.getTotal(),brandPage.getResult());
    }

    @Override
    public List<Map> selectOptionList() {
        return brandDao.selectOptionList();
    }
}
