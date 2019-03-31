package com.itheima.core.service;

import com.itheima.core.pojo.seller.Seller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

/**
 * @描述:  该实现类属于安全框架中加载数据库中用户信息的实现类
 *         也是授权类
 * @Auther: yanlong
 * @Date: 2019/3/8 10:56:59
 * @Version: 1.0
 */
public class UserDetailServiceImpl implements UserDetailsService {

    //配置文件中是property属性的, 使用set方法注入
    //将sellerService 远程调用
    private SellerService sellerService;
    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    //此方法就是加载数据库中用户信息的实现类  并且授权
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过用户名查询数据库
        Seller seller = sellerService.findSellerByUsername(username);

        //判断是否有此用户
        if (seller != null){
            //有用户   判断状态: 只有为1的状态时,审核通过, 才让使用
            if ("1".equals(seller.getStatus())){
                //添加权限
                Set<GrantedAuthority> authorities = new HashSet<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
                return new User(seller.getSellerId(),seller.getPassword(),authorities);
            }

        }

        //无此用户  返回null
        return null;
    }
}
