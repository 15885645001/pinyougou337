package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.core.pojo.seller.Seller;
import com.itheima.core.service.SellerService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @描述: 商家管理
 * @Auther: yanlong
 * @Date: 2019/3/7 15:44:28
 * @Version: 1.0
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;
    /**
     *@描述: 根据条件查询分页对象
     *@Param: [page, rows, seller]
     *@Return: entity.Result
    */
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Seller seller){

        //System.out.println("当前页:"+page);
        return sellerService.search(page,rows,seller);
    }


    /**
     *@描述: 通过商家id查询一个并回显
     *@Param: [id]
     *@Return: com.itheima.core.pojo.seller.Seller
    */
    @RequestMapping("/findOne")
    public Seller findOne(String id){
        return sellerService.findOne(id);
    }


    /**
     *@描述: 运营商根据商家的id和状态 对其进行审核
     *@Param: [sellerId, status]
     *@Return: entity.Result
    */
    @RequestMapping("/updateStatus")
    public Result updateStatus(String sellerId,String status){
        try {
            sellerService.updateStatus(sellerId,status);
            return new Result(true,"审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"审核失败");
        }
    }
}
