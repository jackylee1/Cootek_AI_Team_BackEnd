package com.qulongjun.team.controller;

import com.jfinal.core.Controller;
import com.qulongjun.team.config.error.OtherException;
import com.qulongjun.team.domain.Food;
import com.qulongjun.team.domain.FoodCategory;
import com.qulongjun.team.domain.Shop;
import com.qulongjun.team.utils.DateUtils;
import com.qulongjun.team.utils.RenderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qulongjun on 2018/1/25.
 */
public class FoodController extends Controller {
    /**
     * 获取商店信息
     */
    public void shop() {
        List<Shop> shopList = Shop.shopDao.find("SELECT * FROM `db_shop` WHERE state=1");
        Map history = new HashMap();
        history.put("id", 0);
        history.put("state", 1);
        history.put("shop", "常点餐品");
        history.put("create_time", DateUtils.getCurrentDate());
        List<Food> foodList = Food.foodDao.find("SELECT DISTINCT f.* FROM `db_order` o,`db_shop_food` f,`db_shop_category` c,`db_shop` s WHERE o.user_id=" + getPara("id") + " AND o.food_id = f.id AND f.category_id=c.id AND c.shop_id =s.id AND s.state=1");
        history.put("food", Food._toListJson(foodList));
        List result = new ArrayList();
        result.add(history);
        result.addAll(Shop._toListJson(shopList));
        renderJson(result);
    }


    /**
     * 新增菜品
     */
    public void addFood() {
        Food food = new Food();
        Boolean result = food
                .set("food", getPara("food"))
                .set("category_id", getPara("category_id"))
                .set("state", 1)
                .set("create_time", DateUtils.getCurrentDate())
                .set("price", 30)
                .set("desp", getPara("desp")).save();
        if (!result) throw new OtherException("服务器异常");
        renderJson(RenderUtils.CODE_SUCCESS);
    }


    /**
     * 获取餐厅列表
     */
    public void restaurant() {
        List<Shop> shopList = Shop.shopDao.find("SELECT * FROM `db_shop` WHERE state=1");
        renderJson(Shop._toListSimpleJson(shopList));
    }

    /**
     * 获取餐厅菜品
     */
    public void cookbook() {
        int restaurant = 0;
        if (getPara("id") != null) {
            restaurant = getParaToInt("id");
        }
        List<Shop> shopList = Shop.shopDao.find("SELECT * FROM `db_shop` WHERE state=1 " + (restaurant != 0 ? " AND id=" + restaurant : ""));
        renderJson(Shop._toListJson(shopList));
    }
}
