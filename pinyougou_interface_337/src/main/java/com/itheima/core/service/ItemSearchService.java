package com.itheima.core.service;

import java.util.Map; /**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/16 09:18:15
 * @Version: 1.0
 */
public interface ItemSearchService {
    Map<String,Object> search(Map<String, String> searchMap);
}
