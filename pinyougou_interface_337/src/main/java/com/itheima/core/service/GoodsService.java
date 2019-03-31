package com.itheima.core.service;

import com.itheima.core.pojo.good.Goods;
import entity.PageResult;
import vo.GoodsVo; /**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/8 21:05:42
 * @Version: 1.0
 */
public interface GoodsService {
    void add(GoodsVo goodsVo);

    PageResult search(Integer page, Integer rows, Goods goods);

    GoodsVo findOne(Long id);

    void update(GoodsVo goodsVo);

    void updateStatus(Long[] ids, String status);

    void delete(Long[] ids);
}
