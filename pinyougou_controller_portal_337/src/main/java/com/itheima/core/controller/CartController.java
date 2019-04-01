package com.itheima.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.core.pojo.item.Item;
import com.itheima.core.pojo.order.OrderItem;
import com.itheima.core.service.CartService;
import entity.Cart;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @描述: 购物车管理
 * @Auther: yanlong
 * @Date: 2019/3/25 16:07:37
 * @Version: 1.0
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;


    /**
     *@描述: 添加商品到购物车
     *@Param: [itemId, num] : 库存id  数量
     *@Return: entity.Result
    */
    @RequestMapping("/addGoodsToCartList")
    //@CrossOrigin(origins="http://localhost:9003",allowCredentials="true")
    @CrossOrigin(origins={"http://localhost:9003"})
    public Result addGoodsToCartList(Long itemId, Integer num, HttpServletRequest request,HttpServletResponse response){

        //System.out.println(11111111);

        response.setContentType("text/html;charset=utf-8");

        try {
            List<Cart> cartList = null;
            //1:获取Cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0){
                for (Cookie cookie : cookies) {
                    //判断cookies中的数据是否是购物车
                    if ("CART".equals(cookie.getName())){
                        //是
                        //2：获取Cookie中购物车
                        //购物车对象  Cookie只能保存String类型, 不能保存对象, 将对象转成JSon格式字符串, 取出串转回对象
                        String value = URLDecoder.decode(cookie.getValue(), "utf-8");//字符串
                        cartList = JSON.parseArray(value, Cart.class);
                    }
                }

            }
            //3:没有 创建购物车
            if (cartList == null){
                cartList = new ArrayList<>();
            }

            //4: 不论有没有购物车, 都要追加当前款
            //创建新购物车对象
            Cart newCart = new Cart();
            //根据库存id查询商家id
            Item item = cartService.findItemByItemId(itemId);
            newCart.setSellerId(item.getSellerId());
            //商家里有商品结果集
            OrderItem newOrderItem = new OrderItem();
            //库存id
            newOrderItem.setItemId(itemId);
            //数量
            newOrderItem.setNum(num);
            //创建集合
            List<OrderItem> newOrderItemList = new ArrayList<>();
            newOrderItemList.add(newOrderItem);
            newCart.setOrderItemList(newOrderItemList);
            //判断当前款商品的商家是否在购物车集合中存在
            int newIndexOf = cartList.indexOf(newCart);
            if (newIndexOf != -1){
                //商家存在, 判断当前款的商品是否在该商家中已存在
                Cart oldCart = cartList.get(newIndexOf);
                List<OrderItem> oldOrderItemList = oldCart.getOrderItemList();
                int indexOf = oldOrderItemList.indexOf(newOrderItem);
                if (indexOf != -1){
                    //商品存在,追加数量
                    OrderItem oldOrderItem = oldOrderItemList.get(indexOf);
                    oldOrderItem.setNum(oldOrderItem.getNum() + newOrderItem.getNum());

                }else {
                    //商品不存在,直接追加,即新建一个商品并放到此商家下
                    oldOrderItemList.add(newOrderItem);
                }

            }else {

                //商家不存在, 直接创建新的购物车（因为一个购物车对应一个商家，并在此商家下创建新商品）
                cartList.add(newCart);
            }


                //未登陆
                //5:创建Cookie, 保存购物车到Cookie,  回写Cookie到浏览器
                Cookie cookie = new Cookie("CART", URLEncoder.encode(JSON.toJSONString(cartList),"utf-8"));
                //设置cookie存活时间
                cookie.setMaxAge(7*24*60*60);
                //设置cookie的有效作用范围
                cookie.setPath("/");
                //回写Cookie到浏览器
                response.addCookie(cookie);

            //6：返回信息: new Result(true,加入购物车成功)...
            return new Result(true,"加入购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"加入购物车失败");
        }
    }


    /**
     *@描述: 查询购物车结果集
     *@Param: []
     *@Return: java.util.List<entity.Cart>
    */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList(HttpServletRequest request,HttpServletResponse response) {

        List<Cart> cartList = null;
        //1：获取Cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if ("CART".equals(cookie.getName())){
                    try {
                        //2：获取Cookie中购物车
                        String value = URLDecoder.decode(cookie.getValue(),"utf-8");
                        cartList = JSON.parseArray(value, Cart.class);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

        }


        //5：如果购物车中有商品  将商品的信息装满
        if(cartList != null){
            cartList = cartService.findCartList(cartList);
        }
        //6：回显
        return cartList;
    }
}
