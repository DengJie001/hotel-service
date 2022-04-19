package com.easonhotel.web.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.common.utils.DateUtils;
import com.easonhotel.dao.web.entity.*;
import com.easonhotel.dao.web.service.*;
import com.easonhotel.dao.web.vo.web.HotelOrderVo;
import com.easonhotel.web.security.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

@RestController
@RequestMapping("/eh/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IHotelRoomNumberService hotelRoomNumberService;

    @Autowired
    private IHotelInfoService hotelInfoService;

    @Autowired
    private IHotelRoomInfoService roomInfoService;

    private static final String ROOM_STATUS_IN_USE = "ROOM_STATUS_IN_USE";

    private static final String ROOM_STATUS_FREE = "ROOM_STATUS_FREE";

    private static final String HOTEL_ORDER_STATUS_REFUNDED = "HOTEL_ORDER_STATUS_REFUNDED";

    @PostMapping("/addHotelOrder")
    @LoginRequired
    public ResData<Object> addHotelOrder(@RequestBody Order order) {
        // 先减去用户余额
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>query()
                .eq("deleted", 0)
                .eq("id", order.getUserId())
                .last("LIMIT 0, 1"));
        userInfo.setBalance((long) (userInfo.getBalance() - order.getPrice()));
        userInfoService.saveOrUpdate(userInfo);
        // 将订单数据存入数据库
        orderService.save(order);
        // 修改客房状态
        hotelRoomNumberService.update(Wrappers.<HotelRoomNumber>update()
                .eq("id", order.getRoomNumberId()).set("status", ROOM_STATUS_IN_USE));
        return ResData.create(order);
    }

    /**
     * 根据用户ID查询用户酒店订单
     * @return
     */
    @PostMapping("/queryHotelOrder")
    @LoginRequired
    public ResData<Object> queryHotelOrder(@RequestBody Map<String, String> map) {
        List<HotelOrderVo> hotelOrderVos = orderService.queryHotelOrder(map);
        return ResData.create(hotelOrderVos);
    }

    /**
     * 获取该用户所有的可以购物的订单
     * @param userId 用户ID
     * @return
     */
    @GetMapping("/listValidOrder")
    public ResData<Object> listValidOrder(@RequestParam String userId) {
        List<JSONObject> res = new ArrayList<>();
        List<Order> orderList = orderService.list(Wrappers.<Order>query()
                .eq("deleted", 0).eq("userId", userId)
                .in("status", "HOTEL_ORDER_STATUS_PAYED_NOT_STAY", "HOTEL_ORDER_STATUS_STAYING"));
        for (Order order : orderList) {
            JSONObject item = new JSONObject();
            HotelInfo hotelInfo = hotelInfoService.getOne(Wrappers.<HotelInfo>query()
                    .eq("deleted", 0).eq("id", order.getHotelId()).last("LIMIT 0, 1"));
            item.put("id", order.getId());
            item.put("hotelId", hotelInfo.getId());
            item.put("hotelName", hotelInfo.getName());
            item.put("hotelAddress", hotelInfo.getAddress());
            HotelRoomInfo roomInfo = roomInfoService.getOne(Wrappers.<HotelRoomInfo>query()
                    .eq("deleted", 0).eq("id", order.getRoomId()).last("LIMIT 0, 1"));
            item.put("roomName", roomInfo.getName());
            HotelRoomNumber roomNumber = hotelRoomNumberService.getOne(Wrappers.<HotelRoomNumber>query()
                    .eq("deleted", 0).eq("id", order.getRoomNumberId()).last("LIMIT 0, 1"));
            item.put("roomNumber", roomNumber.getRoomNumber());
            item.put("roomNumberId", roomNumber.getId());
            item.put("stayTime", DateUtils.parseDate(order.getStayTime(), "yyyy-MM-dd"));
            item.put("createdAt", DateUtils.parseDate(order.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
            res.add(item);
        }
        return ResData.create(res);
    }

    @GetMapping("/refund")
    @LoginRequired
    @Transactional
    public ResData<Object> refund(@RequestParam String orderId) {
        Order order = orderService.getOne(Wrappers.<Order>query()
                .eq("deleted", 0).eq("id", orderId).last("LIMIT 0, 1"));
        hotelRoomNumberService.update(Wrappers.<HotelRoomNumber>update()
                .eq("id", order.getRoomNumberId())
                .set("status", ROOM_STATUS_FREE));
        orderService.update(Wrappers.<Order>update().eq("id", orderId).set("status", HOTEL_ORDER_STATUS_REFUNDED));
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>query().eq("deleted", 0).eq("id", order.getUserId()));
        userInfo.setBalance(userInfo.getBalance() + order.getPrice());
        userInfoService.saveOrUpdate(userInfo);
        return ResData.create(0, "操作成功");
    }
}
