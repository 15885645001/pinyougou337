package com.itheima.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.itheima.core.pojo.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.*;


/**
 * @描述:  spring管理实务: 真正是mysql数据库有事务
 *                       solr索引库(可以手动控制事务)
 * @Auther: yanlong
 * @Date: 2019/3/16 09:34:29
 * @Version: 1.0
 */
@SuppressWarnings("all")
@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    //从索引库查询  不是从mysql数据库查询
    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *@描述: 搜索
     *@Param: [searchMap]
     *@Return: java.util.Map<java.lang.String,java.lang.Object>
    */
    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {

        //map的合并
        Map<String,Object> resultMap = new HashMap<>();

        //关键词空格处理
        String keywords = searchMap.get("keywords");
        searchMap.put("keywords",keywords.replaceAll(" ",""));

        //1.调用方法获取商品分类结果集
        List<String> categoryList = searchCategoryListByKeywords(searchMap);
        resultMap.put("categoryList", categoryList);
        //2.品牌结果集
        //3.规格结果集
        if(null != categoryList && categoryList.size() > 0){
            resultMap.putAll(searchBrandListAndSpecListByCategory(categoryList.get(0)));
        }

        //调用方法 4. search1 查询结果集  总条数  总页数
        //resultMap.putAll(search1(searchMap));
        //调用方法 4. search2 查询高亮结果集  总条数  总页数
        resultMap.putAll(search2(searchMap));

        return resultMap;


    }

    //2:品牌结果集
    //3:规格结果集
    public Map<String,Object> searchBrandListAndSpecListByCategory(String category){
        Map<String,Object> resultMap = new HashMap<>();


        //1:通过分类名称查询模板ID
        Object typeId = redisTemplate.boundHashOps("itemCat").get(category);
        //2:通过模板ID查询品牌结果集
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);
        //3:通过模板ID查询规格结果集
        List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeId);

        resultMap.put("brandList",brandList);
        resultMap.put("specList",specList);
        return resultMap;

    }

    //查询商品分类结果集
    public List<String> searchCategoryListByKeywords(Map<String,String> searchMap){
        //关键词
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        Query query = new SimpleQuery(criteria);

        //创建分组项对象
        GroupOptions groupOptions = new GroupOptions();
        //设置分组域
        groupOptions.addGroupByField("item_category");

        query.setGroupOptions(groupOptions);

        //商品分类是去重了的,这里是通过分组去重查询  分组分页对象
        GroupPage<Item> page = solrTemplate.queryForGroupPage(query, Item.class);

        List<String> categoryList = new ArrayList<>();
        //获取page的分组信息
        GroupResult<Item> categorys = page.getGroupResult("item_category");
        Page<GroupEntry<Item>> groupEntries = categorys.getGroupEntries();
        List<GroupEntry<Item>> content = groupEntries.getContent();

        //看到集合等要进行判断
        if (content != null && content.size() > 0){

            for (GroupEntry<Item> itemGroupEntry : content) {
                String groupValue = itemGroupEntry.getGroupValue();
                categoryList.add(groupValue);
            }

        }

        return categoryList;
    }


    //查询普通结果集  总条数  总页数
    public Map<String, Object> search1(Map<String, String> searchMap){
        Map<String,Object> resultMap = new HashMap<>();

        //定义搜索对象的结构  category:商品分类
        //$scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':20,'sort':'','sortField':''};


        //设置搜索查询条件
        //关键词    is相当于等号(=),精准查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        Query query = new SimpleQuery(criteria);

        //分页
        String pageNo = searchMap.get("pageNo");
        String pageSize = searchMap.get("pageSize");
        //转换为integer类型
        //设置开始行 或 当前页
        query.setOffset((Integer.parseInt(pageNo)-1)*Integer.parseInt(pageSize));
        //设置每页条数
        query.setRows(Integer.parseInt(pageSize));



        //查询普通分页结果集对象
        ScoredPage<Item> pageItem = solrTemplate.queryForPage(query, Item.class);

        //分页后的结果集   (分页前的结果集是默认10条)
        resultMap.put("rows", pageItem.getContent());
        //总条数
        resultMap.put("total",pageItem.getTotalElements());
        //总页数
        resultMap.put("totalPages",pageItem.getTotalPages());

        return resultMap;
    }


    //查询高亮结果集  总条数  总页数
    public Map<String, Object> search2(Map<String, String> searchMap){
        Map<String,Object> resultMap = new HashMap<>();

        //定义搜索对象的结构  category:商品分类
        //$scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':20,'sort':'','sortField':''};


        //设置搜索查询条件
        //关键词    is相当于等号(=),精准查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        //Query query = new SimpleQuery(criteria);

        //创建高亮的条件对象  (暗示着高亮开关已开启)
        HighlightQuery highlightQuery = new SimpleHighlightQuery(criteria);
        //设置高亮的条件
        //高亮的域
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");
        //前 缀
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        //后 缀
        highlightOptions.setSimplePostfix("</em>");

        highlightQuery.setHighlightOptions(highlightOptions);


        //分页
        String pageNo = searchMap.get("pageNo");
        String pageSize = searchMap.get("pageSize");
        //转换为integer类型
        //设置开始行 或 当前页
        highlightQuery.setOffset((Integer.parseInt(pageNo)-1)*Integer.parseInt(pageSize));
        //设置每页条数
        highlightQuery.setRows(Integer.parseInt(pageSize));


        //设置过滤条件
        //商品分类
        if (searchMap.get("category") != null && !"".equals(searchMap.get("category"))){
            FilterQuery filterQuery = new SimpleFilterQuery();
            filterQuery.addCriteria(new Criteria("item_category").is(searchMap.get("category")));
            highlightQuery.addFilterQuery(filterQuery);

        }
        //品牌
        if (searchMap.get("brand") != null && !"".equals(searchMap.get("brand"))){
            FilterQuery filterQuery = new SimpleFilterQuery();
            filterQuery.addCriteria(new Criteria("item_brand").is(searchMap.get("brand")));
            highlightQuery.addFilterQuery(filterQuery);

        }
        //规格
        if (searchMap.get("spec") != null && searchMap.get("spec").length() > 0){
            //searchMap.get("spec")获取的是:{"机身内存":"16G","网络":"联通3G"}----->转为对象 使用JSON.parseObject()
            Map<String,String> specMap = JSON.parseObject(searchMap.get("spec"), Map.class);
            Set<Map.Entry<String, String>> entries = specMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                FilterQuery filterQuery = new SimpleFilterQuery();
                //is相当于是冒号( : )  "item_spec_网络": "联通3G"
                filterQuery.addCriteria(new Criteria("item_spec_"+entry.getKey()).is(entry.getValue()));
                highlightQuery.addFilterQuery(filterQuery);
            }
        }
        //价格区间
        if (searchMap.get("price") != null && !"".equals(searchMap.get("price"))){
            //string转成数组
            String[] prices = searchMap.get("price").split("-");
            FilterQuery filterQuery = new SimpleFilterQuery();
            if (searchMap.get("price").contains("*")){
                //包含*
                filterQuery.addCriteria(new Criteria("item_price").greaterThan(prices[0]));

            }else {
                //不包含*
                filterQuery.addCriteria(new Criteria("item_price").between(prices[0],prices[1],true,true));

            }
            highlightQuery.addFilterQuery(filterQuery);

        }
        //排序
        if (searchMap.get("sort") != null && !"".equals(searchMap.get("sort"))){
            //判断升序还是降序
            if ("ASC".equals(searchMap.get("sort"))){
                highlightQuery.addSort(new Sort(Sort.Direction.ASC,"item_"+searchMap.get("sortField")));

            }else {

                highlightQuery.addSort(new Sort(Sort.Direction.DESC,"item_"+searchMap.get("sortField")));
            }
        }



        //查询高亮分页结果集对象
        HighlightPage<Item> page = solrTemplate.queryForHighlightPage(highlightQuery, Item.class);

        //获取分页后的高亮结果集
        List<HighlightEntry<Item>> highlighted = page.getHighlighted();
        for (HighlightEntry<Item> itemHighlightEntry : highlighted) {
            //获取entity  普通
            Item entity = itemHighlightEntry.getEntity();
            //获取highlights 高亮
            List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();
            if(null != highlights && highlights.size() > 0){
                entity.setTitle(highlights.get(0).getSnipplets().get(0));
            }
        }

        //分页后的结果集   (分页前的结果集是默认10条)
        resultMap.put("rows", page.getContent());
        //总条数
        resultMap.put("total",page.getTotalElements());
        //总页数
        resultMap.put("totalPages",page.getTotalPages());

        return resultMap;
    }
}
