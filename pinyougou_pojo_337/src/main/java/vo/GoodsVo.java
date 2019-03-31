package vo;

import com.itheima.core.pojo.good.Goods;
import com.itheima.core.pojo.good.GoodsDesc;
import com.itheima.core.pojo.item.Item;

import java.io.Serializable;
import java.util.List;

/**
 * @描述: 商品包装对象
 * @Auther: yanlong
 * @Date: 2019/3/8 20:23:33
 * @Version: 1.0
 */
public class GoodsVo implements Serializable {
    //商品对象 SPU
    private Goods goods;

    //商品详情对象
    private GoodsDesc goodsDesc;

    //库存对象  SKU
    private List<Item> itemList;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(GoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
