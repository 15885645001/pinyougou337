package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.pojo.good.Goods;
import com.itheima.core.service.GoodsService;
import entity.PageResult;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.GoodsVo;

/**
 * @描述:  商品管理
 * @Auther: yanlong
 * @Date: 2019/3/8 20:53:38
 * @Version: 1.0
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     *@描述: 新增商品
     *@Param: [goods]
     *@Return: entity.Result
    */
    @RequestMapping("/add")
    public Result add(@RequestBody GoodsVo goodsVo){
        try {
            //获取当前登录人
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsVo.getGoods().setSellerId(name);//设置商家id

            goodsService.add(goodsVo);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }


    /**
     *@描述: 带条件的分页查询
     *       查询商品列表分页对象
     *
     *       运营商是对所有商家的商品进行审核
     *@Param: [page, rows, goods]
     *@Return: entity.PageResult
    */
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Goods goods){

        //运营商  不设置当前登录人
        return goodsService.search(page,rows,goods);

    }

    /**
     *@描述: 根据商品id查询一个商品包装对象
     *@Param: [id]
     *@Return: vo.GoodsVo
    */
    @RequestMapping("/findOne")
    public GoodsVo findOne(Long id){
        return goodsService.findOne(id);
    }


    /**
     *@描述: 商品修改
     *@Param: [goodsVo]
     *@Return: entity.Result
    */
    @RequestMapping("/update")
    public Result update(@RequestBody GoodsVo goodsVo){
        try {

            goodsService.update(goodsVo);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }


    /**
     *@描述: 审核,修改状态:  审核通过或驳回
     *@Param: [ids, status]
     *@Return: entity.Result
    */
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids,String status){

        try {
            goodsService.updateStatus(ids,status);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }


    /**
     *@描述: 批量删除
     *@Param: [ids]
     *@Return: entity.Result
    */
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        System.out.println(111111111);
        try {
            goodsService.delete(ids);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"失败");
        }
    }
}
