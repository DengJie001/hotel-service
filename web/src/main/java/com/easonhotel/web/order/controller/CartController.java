package com.easonhotel.web.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.Cart;
import com.easonhotel.dao.web.entity.HotelCommodity;
import com.easonhotel.dao.web.service.ICartService;
import com.easonhotel.dao.web.service.IHotelCommodityService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/eh/cart")
public class CartController {
    @Autowired
    private ICartService cartService;

    @Autowired
    private IHotelCommodityService commodityService;

    /**
     * 新增商品到购物车
     * @param cart
     * @return
     */
    @PostMapping("/modify")
    public ResData<Object> modify(@RequestBody Cart cart) {
        Cart existThisCommodityInCart = cartService.getOne(Wrappers.<Cart>query()
                .eq("deleted", 0).eq("userId", cart.getUserId())
                .eq("commodityId", cart.getCommodityId()).last("LIMIT 0, 1"));
        // 如果数据库中该商品不在该用户的购物车列表中 则直接新增
        if (existThisCommodityInCart == null) {
            cartService.save(cart);
            return ResData.create(0, "操作成功");
        }
        // 如果该商品在该用户的购物车列表中 则更新数据
        existThisCommodityInCart.setCount(existThisCommodityInCart.getCount() + cart.getCount());
        cartService.updateById(existThisCommodityInCart);
        return ResData.create(0, "操作成功");
    }

    /**
     * 删除购物车中的某件商品
     * @param commodityId 商品ID
     * @param userId 用户ID
     * @return
     */
    @GetMapping("/delete")
    public ResData<Object> delete(@RequestParam String commodityId, @RequestParam String userId) {
        cartService.update(Wrappers.<Cart>update().eq("userId", userId).eq("commodityId", commodityId)
                .set("deleted", true));
        return ResData.create(0, "操作成功");
    }

    /**
     * 查询该用户的购物车中的所有商品
     * @param userId 用户ID
     * @return
     */
    @GetMapping("/listByUserId")
    public ResData<Object> listByUserId(@RequestParam String userId) {
        List<JSONObject> res = new ArrayList<>();
        List<Cart> cartList = cartService.list(Wrappers.<Cart>query()
                .eq("deleted", 0).eq("userId", userId));
        for (Cart cart : cartList) {
            JSONObject item = new JSONObject();
            item.put("id", cart.getCommodityId());
            item.put("count", cart.getCount());
            HotelCommodity commodity = commodityService.getOne(Wrappers.<HotelCommodity>query()
                    .eq("deleted", 0).eq("id", cart.getCommodityId())
                    .last("LIMIT 0, 1"));
            item.put("price", commodity.getPrice());
            item.put("coverImage", commodity.getCoverImage());
            item.put("name", commodity.getName());
            res.add(item);
        }
        return ResData.create(res);
    }

    @GetMapping("/calculateTotal")
    public ResData<Object> calculateTotal(@RequestParam String userId) {
        Integer total = 0;
        List<Cart> cartList = cartService.list(Wrappers.<Cart>query()
                .eq("deleted", 0).eq("userId", userId));
        for (Cart cart : cartList) {
            total += cart.getCount();
        }
        return ResData.create(total);
    }
}
