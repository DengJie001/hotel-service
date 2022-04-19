package com.easonhotel.admin.web.order.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.common.utils.PasswordUtil;
import com.easonhotel.dao.sys.entity.User;
import com.easonhotel.dao.sys.service.IUserService;
import com.easonhotel.dao.web.dto.admin.HotelOrderQueryParam;
import com.easonhotel.dao.web.entity.HotelRoomNumber;
import com.easonhotel.dao.web.entity.Order;
import com.easonhotel.dao.web.entity.UserInfo;
import com.easonhotel.dao.web.service.IHotelRoomNumberService;
import com.easonhotel.dao.web.service.IOrderService;
import com.easonhotel.dao.web.service.IUserInfoService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 酒店订单接口
 */
@RestController
@RequestMapping("/finance/hotelOrder")
public class HotelOrderController {
    private String HOTEL_ORDER_STATUS_REFUNDED = "HOTEL_ORDER_STATUS_REFUNDED";

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IHotelRoomNumberService hotelRoomNumberService;

    @Autowired
    private IUserService userService;

    private static final String COMMODITY_ORDER_STATUS_FINISHED = "COMMODITY_ORDER_STATUS_FINISHED";

    private static final String ROOM_STATUS_FREE = "ROOM_STATUS_FREE";

    /**
     * 酒店订单的分页查询
     * @param param
     * @return
     */
    @PostMapping("/pageList")
    public ResData<Object> pageList(@RequestBody HotelOrderQueryParam param) {
        Map<String, Object> res = orderService.pageList(param);
        return ResData.create(res);
    }

    /**
     * 酒店订单退款
     * @param map
     * id: 订单ID
     * password：执行退款的操作员密码
     * @return
     */
    @PostMapping("/refund")
    @Transactional
    public ResData<Object> refund(@RequestBody Map<String, String> map) {
        // 参数校验
        if (!map.containsKey("id") || StringUtils.isBlank(map.get("id"))) {
            return ResData.create(-1, "缺少订单ID");
        }
        if (!map.containsKey("password") || StringUtils.isBlank(map.get("password"))) {
            return ResData.create(-1, "请操作员输入密码");
        }
        // 验证操作员身份
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        User sysUser = userService.getOne(Wrappers.<User>query().eq("deleted", 0).eq("loginName", loginName).last("LIMIT 0, 1"));
        if (!PasswordUtil.encrypt(map.get("password")).equals(sysUser.getPassword())) {
            return ResData.create(-1, "操作员身份校验失败：密码错误");
        }
        // 检查订单是否可退款
        Order hotelOrder = orderService.getOne(Wrappers.<Order>query()
                .eq("deleted", 0)
                .eq("id", map.get("id"))
                .last("LIMIT 0, 1"));
        if (HOTEL_ORDER_STATUS_REFUNDED.equals(hotelOrder.getStatus())) {
            return ResData.create(-1, "该订单已退款！");
        }
        // 修改订单状态
        hotelOrder.setStatus(HOTEL_ORDER_STATUS_REFUNDED);
        orderService.updateById(hotelOrder);
        // 退款给用户
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>query()
                .eq("deleted", 0)
                .eq("id", hotelOrder.getUserId())
                .last("LIMIT 0, 1"));
        userInfo.setBalance(userInfo.getBalance() + hotelOrder.getPrice());
        userInfoService.updateById(userInfo);
        // 将客房状态修改为空闲
        hotelRoomNumberService.update(Wrappers.<HotelRoomNumber>update().eq("id", hotelOrder.getRoomNumberId()).set("status", "ROOM_STATUS_FREE"));
        return ResData.create(0, "操作成功");
    }

    /**
     * 退房
     * @param id 订单ID
     * @author PengYiXing
     * @return
     */
    @GetMapping("/checkOut")
    public ResData<Object> checkOut(@RequestParam String id) {
        Order order = orderService.getOne(Wrappers.<Order>query()
                .eq("deleted", 0).eq("id", id).last("LIMIT 0, 1"));
        orderService.update(Wrappers.<Order>update()
                .eq("id", id).set("status", COMMODITY_ORDER_STATUS_FINISHED));
        hotelRoomNumberService.update(Wrappers.<HotelRoomNumber>update()
                .eq("id", order.getRoomNumberId()).set("status", ROOM_STATUS_FREE));
        return ResData.create(0, "操作成功");
    }
}
