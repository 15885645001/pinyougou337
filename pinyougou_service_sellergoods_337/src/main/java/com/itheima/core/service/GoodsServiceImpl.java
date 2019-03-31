package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.core.dao.good.BrandDao;
import com.itheima.core.dao.good.GoodsDao;
import com.itheima.core.dao.good.GoodsDescDao;
import com.itheima.core.dao.item.ItemCatDao;
import com.itheima.core.dao.item.ItemDao;
import com.itheima.core.dao.seller.SellerDao;
import com.itheima.core.pojo.good.Goods;
import com.itheima.core.pojo.good.GoodsQuery;
import com.itheima.core.pojo.item.Item;
import com.itheima.core.pojo.item.ItemQuery;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;
import vo.GoodsVo;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @描述:
 * @Auther: yanlong
 * @Date: 2019/3/8 21:06:04
 * @Version: 1.0
 */
@SuppressWarnings("all")
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsDescDao goodsDescDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private SellerDao sellerDao;

    @Autowired
    private BrandDao brandDao;

    //添加三张表
    @Override
    public void add(GoodsVo goodsVo) {
        //商品表
           //1. 页面传递过来的
           //2. 手动写的
        //状态  未审核
        goodsVo.getGoods().setAuditStatus("0");
        //保存  回显商品id----在goodsdao.xml中修改
        goodsDao.insertSelective(goodsVo.getGoods());

        //商品详情表  商品id: 使用上面商品表生成的主键
        goodsVo.getGoodsDesc().setGoodsId(goodsVo.getGoods().getId());
        goodsDescDao.insertSelective(goodsVo.getGoodsDesc());

        //判断是否启用规格
        if ("1".equals(goodsVo.getGoods().getIsEnableSpec())){

            //库存表 或 库存结果集对象
            List<Item> itemList = goodsVo.getItemList();
            for (Item item : itemList) {
                String spec = item.getSpec();//{"机身内存":"16G","网络":"联通3G"}---->转换为对象map

                //标题: 商品名称+" "+ 规格1+" " +规格2+" " +...
                String title = goodsVo.getGoods().getGoodsName();

                Map<String,String> map = JSON.parseObject(spec, Map.class);

                Set<Map.Entry<String, String>> entrySet = map.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {

                    title += " "+entry.getValue();

                }

                item.setTitle(title);

                //获取库存表的图片  从商品表一堆中取第一张图片即可
                //[{"color":"粉色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXq2AFIs5AAgawLS1G5Y004.jpg"},{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXrWAcIsOAAETwD7A1Is874.jpg"}]
                String itemImages = goodsVo.getGoodsDesc().getItemImages();
                List<Map> imageList = JSON.parseArray(itemImages, Map.class);
                //判断图片是否为空
                if (imageList != null && imageList.size() > 0){
                    //object类型可以强转成string类型
                    item.setImage((String) imageList.get(0).get("url"));

                }

                //获取商品分类中三级商品分类的id
                item.setCategoryid(goodsVo.getGoods().getCategory3Id());

                //获取三级分类的名称
                item.setCategory(itemCatDao.selectByPrimaryKey(goodsVo.getGoods().getCategory3Id()).getName());

                //添加时间(创建时间)
                item.setCreateTime(new Date());

                //更新时间
                item.setUpdateTime(new Date());

                //获取商品id
                item.setGoodsId(goodsVo.getGoods().getId());

                //获取商家id
                item.setSellerId(goodsVo.getGoods().getSellerId());

                //获取商家名称
                item.setSeller(sellerDao.selectByPrimaryKey(goodsVo.getGoods().getSellerId()).getNickName());

                //获取品牌名称
                item.setBrand(brandDao.selectByPrimaryKey(goodsVo.getGoods().getBrandId()).getName());

                //保存一条库存表数据
                itemDao.insertSelective(item);
            }
        }else {
            //不写
        }



    }

    @Override
    public PageResult search(Integer page, Integer rows, Goods goods) {
        PageHelper.startPage(page,rows);
        GoodsQuery query = new GoodsQuery();
        GoodsQuery.Criteria criteria = query.createCriteria();

        //判断审核状态  下拉框不用去空格
        if (goods.getAuditStatus() != null && !"".equals(goods.getAuditStatus())){
            criteria.andAuditStatusEqualTo(goods.getAuditStatus());
        }

        //判断商品名称  文本框去空格[注: 空格去的只是前后两端的空格,不能去掉文本之间的空格]
        if (goods.getGoodsName() != null && !"".equals(goods.getGoodsName().trim())){
            //模糊查询
            criteria.andGoodsNameLike("%"+goods.getGoodsName().trim()+"%");
        }

        //运营商与商家都应该只显示不删除的商品  是null才是不删除的商品, 不是null是删除了的商品
        criteria.andIsDeleteIsNull();

        //判断 如果是运营商, 就没有当前登录人; 如果是商家后台调用,goods里面就会有当前登录人
        if (goods.getSellerId() != null){
            //只能查询当前登录人的商品
            criteria.andSellerIdEqualTo(goods.getSellerId());

        }

        //根据条件查询
        Page<Goods> goodsPage = (Page<Goods>) goodsDao.selectByExample(query);
        return new PageResult(goodsPage.getTotal(),goodsPage.getResult());
    }

    @Override
    public GoodsVo findOne(Long id) {
        GoodsVo goodsVo = new GoodsVo();
        //商品对象
        goodsVo.setGoods(goodsDao.selectByPrimaryKey(id));

        //商品详情对象
        goodsVo.setGoodsDesc(goodsDescDao.selectByPrimaryKey(id));

        //库存结果集对象
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        goodsVo.setItemList(itemDao.selectByExample(itemQuery));
        return goodsVo;
    }

    @Override
    public void update(GoodsVo goodsVo) {
        //商品表
        goodsDao.updateByPrimaryKeySelective(goodsVo.getGoods());

        //商品详情表
        goodsDescDao.updateByPrimaryKeySelective(goodsVo.getGoodsDesc());

        //库存结果集表
          //1. 先通过商品id的外键 进行整体删除
          ItemQuery itemQuery = new ItemQuery();
          itemQuery.createCriteria().andGoodsIdEqualTo(goodsVo.getGoods().getId());
          itemDao.deleteByExample(itemQuery);

          //2. 添加
        //判断是否启用规格
        if ("1".equals(goodsVo.getGoods().getIsEnableSpec())){

            //库存表 或 库存结果集对象
            List<Item> itemList = goodsVo.getItemList();
            for (Item item : itemList) {
                String spec = item.getSpec();//{"机身内存":"16G","网络":"联通3G"}---->转换为对象map

                //标题: 商品名称+" "+ 规格1+" " +规格2+" " +...
                String title = goodsVo.getGoods().getGoodsName();

                Map<String,String> map = JSON.parseObject(spec, Map.class);

                Set<Map.Entry<String, String>> entrySet = map.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {

                    title += " "+entry.getValue();

                }

                item.setTitle(title);

                //获取库存表的图片  从商品表一堆中取第一张图片即可
                //[{"color":"粉色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXq2AFIs5AAgawLS1G5Y004.jpg"},{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOXrWAcIsOAAETwD7A1Is874.jpg"}]
                String itemImages = goodsVo.getGoodsDesc().getItemImages();
                List<Map> imageList = JSON.parseArray(itemImages, Map.class);
                //判断图片是否为空
                if (imageList != null && imageList.size() > 0){
                    //object类型可以强转成string类型
                    item.setImage((String) imageList.get(0).get("url"));

                }

                //获取商品分类中三级商品分类的id
                item.setCategoryid(goodsVo.getGoods().getCategory3Id());

                //获取三级分类的名称
                item.setCategory(itemCatDao.selectByPrimaryKey(goodsVo.getGoods().getCategory3Id()).getName());

                //添加时间(创建时间)
                item.setCreateTime(new Date());

                //更新时间
                item.setUpdateTime(new Date());

                //获取商品id
                item.setGoodsId(goodsVo.getGoods().getId());

                //获取商家id
                item.setSellerId(goodsVo.getGoods().getSellerId());

                //获取商家名称
                item.setSeller(sellerDao.selectByPrimaryKey(goodsVo.getGoods().getSellerId()).getNickName());

                //获取品牌名称
                item.setBrand(brandDao.selectByPrimaryKey(goodsVo.getGoods().getBrandId()).getName());

                //保存一条库存表数据
                itemDao.insertSelective(item);
            }
        }else {
            //不写
        }

    }


    @Autowired
    private JmsTemplate jmsTemplate;

    //注入目的地对象
    @Autowired
    private Destination topicPageAndSolrDestination;

    @Autowired
    private Destination queueSolrDeleteDestination;

    @Override
    public void updateStatus(Long[] ids, String status) {
        Goods goods = new Goods();
        goods.setAuditStatus(status);
        for (Long id : ids) {
            goods.setId(id);

            //1. 更新状态
            goodsDao.updateByPrimaryKeySelective(goods);

            //判断商品只有在运营商审核通过的情况下, 才能保存商品信息到索引库
            if ("1".equals(status)){
                //发消息  spring提供的jmstemplate
                //注: 匿名内部类使用外面的id, 使用final 修饰
                jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(String.valueOf(id));
                    }
                });

            }


        }
    }


    @Override
    public void delete(Long[] ids) {
        Goods goods = new Goods();
        goods.setIsDelete("1");
        for (Long id : ids) {
            goods.setId(id);
            //1. 不是真的删除, 而是更新商品的是否删除字段为1
            goodsDao.updateByPrimaryKeySelective(goods);

            //发消息
            jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage(String.valueOf(id));
                }
            });

        }
    }
}
