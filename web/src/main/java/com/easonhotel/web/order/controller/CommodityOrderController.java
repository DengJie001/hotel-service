package com.easonhotel.web.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.*;
import com.easonhotel.dao.web.service.*;
import com.easonhotel.dao.web.vo.web.CommodityOrderWebVo;
import com.easonhotel.web.hotel.dto.AddCommodityOrderParam;
import com.easonhotel.web.security.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 前台商品订单接口
 * @author PengYiXing
 */
@RestController
@RequestMapping("/eh/commodityOrder")
public class CommodityOrderController {
    @Autowired
    private ICommodityOrderService commodityOrderService;

    @Autowired
    private ICommoditySonOrderService sonOrderService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private IHotelCommodityService commodityService;

    /**
     * 新增商品购物订单
     * @param map
     * @return
     */
    @PostMapping("/add")
    // 自动事务提交
    @Transactional
    // 验证登录状态
    @LoginRequired
    public ResData<Object> add(@RequestBody Map<String, String> map) {
        String userId = map.get("userId");
        Integer total = 0;
        // 是否允许执行插入操作
        boolean canAdd = true;
        List<CommoditySonOrder> sonOrders = new ArrayList<>();
        CommodityOrder commodityOrder = new CommodityOrder();
        commodityOrder.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        // 查询该用户的购物车商品
        List<Cart> cartList = cartService.list(Wrappers.<Cart>query()
                .eq("deleted", 0).eq("userId", userId));
        // 检查库存 计算商品总价 新增父订单 子订单
        for (int i = 0; i < cartList.size(); ++i) {
            Cart cart = cartList.get(i);
            HotelCommodity hotelCommodity = commodityService.getOne(Wrappers.<HotelCommodity>query()
                    .eq("deleted", 0).eq("id", cart.getCommodityId()).last("LIMIT 0, 1"));
            // 检查库存
            if (hotelCommodity.getInventory() < cart.getCount()) {
                if (hotelCommodity.getInventory() == 0) {
                    cartService.update(Wrappers.<Cart>update().eq("id", cart.getId()).set("deleted", true));
                    canAdd = false;
                    continue;
                }
                // 修改购物车商品数量
                cart.setCount(hotelCommodity.getInventory());
                cartService.updateById(cart);
                canAdd = false;
            } else {
                total += (cart.getCount() * hotelCommodity.getPrice());
                CommoditySonOrder sonOrder = new CommoditySonOrder();
                sonOrder.setCommodityId(hotelCommodity.getId());
                sonOrder.setParentOrderId(commodityOrder.getId());
                sonOrder.setNumber(cart.getCount());
                sonOrders.add(sonOrder);
            }
        }
        if (!canAdd) {
            return ResData.create(-1, "商品库存不足");
        }
        commodityOrder.setUserId(userId);
        commodityOrder.setPrice(total);
        commodityOrder.setStatus("COMMODITY_ORDER_STATUS_PAYED");
        commodityOrder.setRoomNumber(map.get("roomNumber"));
        // 保存父订单
        commodityOrderService.save(commodityOrder);
        // 保存子订单
        for (CommoditySonOrder order : sonOrders) {
            sonOrderService.save(order);
        }
        // 减去用户余额
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>query().eq("deleted", 0).eq("id", userId)
                .last("LIMIT 0, 1"));
        userInfo.setBalance((long) userInfo.getBalance() - total);
        userInfoService.updateById(userInfo);
        // 减去商品库存 修改商品销量 删除购物车数据
        cartService.update(Wrappers.<Cart>update()
                .eq("userId", userId).set("deleted", true));
        for (int i = 0; i < cartList.size(); ++i) {
            HotelCommodity commodity = commodityService.getOne(Wrappers.<HotelCommodity>query()
                    .eq("id", cartList.get(i).getCommodityId())
                    .eq("deleted", 0).last("LIMIT 0, 1"));
            // 减去库存
            commodity.setInventory(commodity.getInventory() - cartList.get(i).getCount());
            // 增加销量
            commodity.setSales(commodity.getSales() + cartList.get(i).getCount());
            commodityService.updateById(commodity);
        }
        return ResData.create(commodityOrder);
    }

    /**
     * 查询该用户的所有购物订单
     * @param map
     * @author PengYiXing
     * @return
     */
    @PostMapping("/pageList")
    public ResData<Object> pageList(@RequestBody Map<String, String> map) {
        List<CommodityOrderWebVo> commodityOrderWebVos = commodityOrderService.pageList(map);
        return ResData.create(commodityOrderWebVos);
    }
}
