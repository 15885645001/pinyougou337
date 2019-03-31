package com.itheima.core.service;

import com.itheima.core.pojo.user.User; /**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/22 14:17:22
 * @Version: 1.0
 */
public interface UserService {
    void sendCode(String phone);

    void add(String smscode, User user);
}
