package com.easonhotel.admin.web.workspace;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.common.utils.DateUtils;
import com.easonhotel.dao.sys.entity.Dict;
import com.easonhotel.dao.sys.entity.Role;
import com.easonhotel.dao.sys.entity.User;
import com.easonhotel.dao.sys.entity.UserRole;
import com.easonhotel.dao.sys.service.IDictService;
import com.easonhotel.dao.sys.service.IRoleService;
import com.easonhotel.dao.sys.service.IUserRoleService;
import com.easonhotel.dao.sys.service.IUserService;
import com.easonhotel.dao.web.entity.CommodityOrder;
import com.easonhotel.dao.web.entity.CommoditySonOrder;
import com.easonhotel.dao.web.entity.HotelCommodity;
import com.easonhotel.dao.web.entity.Order;
import com.easonhotel.dao.web.service.ICommodityOrderService;
import com.easonhotel.dao.web.service.ICommoditySonOrderService;
import com.easonhotel.dao.web.service.IHotelCommodityService;
import com.easonhotel.dao.web.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {
    @Autowired
    private ICommodityOrderService commodityOrderService;

    @Autowired
    private ICommoditySonOrderService sonOrderService;

    @Autowired
    private IOrderService hotelOrderService;

    @Autowired
    private IHotelCommodityService commodityService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IDictService dictService;

    private static final Integer DAYS = 30;

    private static final String HOTEL_ORDER_STATUS_REFUNDED = "HOTEL_ORDER_STATUS_REFUNDED";

    private static final String COMMODITY_ORDER_STATUS_REFUNDED = "COMMODITY_ORDER_STATUS_REFUNDED";

    private static final String COMMODITY_TYPE_PARENT_CODE = "COMMODITY_TYPE";

    /**
     * 销售数据饼状图数据
     * @return
     */
    @GetMapping("/pieData")
    public ResData<Object> pieData() {
        List<JSONObject> res = new ArrayList<>();
        // 先查询所有的商品code
        List<Dict> commodityTypes = dictService.list(Wrappers.<Dict>query().eq("parentCode", COMMODITY_TYPE_PARENT_CODE));
        for (Dict dict : commodityTypes) {
            JSONObject item = new JSONObject();
            int value = 0;
            List<HotelCommodity> commodityList = commodityService.list(Wrappers.<HotelCommodity>query().eq("type", dict.getCode()));
            Map<String, Integer> map = new HashMap<>();
            for (HotelCommodity commodity : commodityList) {
                map.put(commodity.getId(), commodity.getPrice());
            }
            List<String> ids = new ArrayList<>();
            for (HotelCommodity hotelCommodity : commodityList) {
                ids.add(hotelCommodity.getId());
            }
            List<CommoditySonOrder> sonOrderList = sonOrderService.list(Wrappers.<CommoditySonOrder>query().in("commodityId", ids));
            for (CommoditySonOrder sonOrder : sonOrderList) {
                value += (sonOrder.getNumber() * map.get(sonOrder.getCommodityId()));
            }
            item.put("type", dict.getValue());
            item.put("value", value);
            res.add(item);
        }
        return ResData.create(res);
    }

    /**
     * 当月每天的营业额数据 折线图数据
     * @author PengYiXing
     * @return
     */
    @GetMapping("/lineData")
    public ResData<Object> lineData() {
        List<JSONObject> res = new ArrayList<>();
        // 当天时间
        long today = System.currentTimeMillis();
        for (int i = 0; i < DAYS; ++i) {
            JSONObject item = new JSONObject();
            int turnover = 0;
            long now = today - ((long) i * 24 * 60 * 60 * 1000);
            long lastDay = today - ((long) (i + 1) * 24 * 60 * 60 * 1000);
            System.out.println(DateUtils.parseDate(now, "yyyy-MM-dd"));
            // 酒店营业额
            List<Order> hotelOrderList = hotelOrderService.list(Wrappers.<Order>query()
                    .eq("deleted", 0)
                    .ne("status", HOTEL_ORDER_STATUS_REFUNDED)
                    .le("createdAt", now)
                    .gt("createdAt", lastDay));
            // 商品营业额
            List<CommodityOrder> commodityOrderList = commodityOrderService.list(Wrappers.<CommodityOrder>query()
                    .eq("deleted", 0)
                    .ne("status", COMMODITY_ORDER_STATUS_REFUNDED)
                    .le("createdAt", now)
                    .gt("createdAt", lastDay));
            for (Order order : hotelOrderList) {
                turnover += order.getPrice();
            }
            for (CommodityOrder commodityOrder : commodityOrderList) {
                turnover += commodityOrder.getPrice();
            }
            item.put("date", DateUtils.parseDate(now, "yyyy-MM-dd"));
            item.put("value", turnover);
            res.add(item);
        }
        return ResData.create(res);
    }

    @GetMapping("/currentLoginUser")
    public ResData<Object> currentLoginUser() {
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getOne(Wrappers.<User>query().eq("deleted", 0).eq("loginName", loginName).last("LIMIT 0, 1"));
        UserRole userRole = userRoleService.getOne(Wrappers.<UserRole>query().eq("deleted", 0).eq("userId", user.getId()).last("LIMIT 0,1"));
        Role role = roleService.getOne(Wrappers.<Role>query().eq("deleted", 0).eq("id", userRole.getRoleId()).last("LIMIT 0,1"));
        JSONObject res = new JSONObject();
        res.put("name", user.getName());
        res.put("roleName", role.getName());
        int time = Integer.parseInt(DateUtils.parseDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss").split(":")[1]);
        if (time >= 0 && time <= 6) {
            res.put("time", "辛苦了");
        } else if (time > 6 && time <= 9) {
            res.put("time", "早上好");
        } else if (time > 9 && time <= 12) {
            res.put("time", "上午好");
        } else if (time > 12 && time <= 18) {
            res.put("time", "下午好");
        } else {
            res.put("time", "晚上好");
        }
        return ResData.create(res);
    }
}
