package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.core.dao.good.GoodsDao;
import com.itheima.core.dao.good.GoodsDescDao;
import com.itheima.core.dao.item.ItemCatDao;
import com.itheima.core.dao.item.ItemDao;
import com.itheima.core.pojo.good.Goods;
import com.itheima.core.pojo.good.GoodsDesc;
import com.itheima.core.pojo.item.Item;
import com.itheima.core.pojo.item.ItemQuery;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述:  静态化处理实现类
 * @Auther: yanlong
 * @Date: 2019/3/18 10:23:11
 * @Version: 1.0
 */
@Service
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private GoodsDescDao goodsDescDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private ItemCatDao itemCatDao;

    //定义静态化商品详情页面的方法
    @Override
    public void index(Long id){

        //1. 对freemarker进行实例化
        Configuration configuration = freeMarkerConfigurer.getConfiguration();

        Writer out = null;

        //定义输出路径  即全路径
        String path = getPath("/"+id+".html");



        //2. 在配置文件中已配置

        //3. 加载指定的模板, 返回值为模板对象
        try {
            Template template = configuration.getTemplate("item.ftl");

            //创建输出流对象, 并且指定生成的文件名, 指定编码集
            out = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");

            //创建模板使用的数据集
            Map<String,Object> root = new HashMap<>();

               //数据1. 根据商品id查询库存结果集
               ItemQuery itemQuery = new ItemQuery();
               itemQuery.createCriteria().andGoodsIdEqualTo(id);
               List<Item> itemList = itemDao.selectByExample(itemQuery);
               root.put("itemList",itemList);

               //数据2. 查询商品详情表
               GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
               root.put("goodsDesc",goodsDesc);

               //数据3. 查询商品表
               Goods goods = goodsDao.selectByPrimaryKey(id);
               root.put("goods",goods);

               //数据4. 查询商品分类名称  1级分类  2级分类  3级分类
               root.put("itemCat1",itemCatDao.selectByPrimaryKey(goods.getCategory1Id()).getName());
               root.put("itemCat2",itemCatDao.selectByPrimaryKey(goods.getCategory2Id()).getName());
               root.put("itemCat3",itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());

            //4. 调用模板对象的process方法输出文件
            template.process(root,out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //定义方法获取全路径
    public String getPath(String path){
        return servletContext.getRealPath(path);
    }

    @Autowired
    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {

        this.servletContext = servletContext;
    }
}
