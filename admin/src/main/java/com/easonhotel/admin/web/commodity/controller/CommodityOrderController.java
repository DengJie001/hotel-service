package com.easonhotel.admin.web.commodity.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.dto.admin.CommodityOrderQueryParam;
import com.easonhotel.dao.web.entity.CommodityOrder;
import com.easonhotel.dao.web.entity.UserInfo;
import com.easonhotel.dao.web.service.ICommodityOrderService;
import com.easonhotel.dao.web.service.ICommoditySonOrderService;
import com.easonhotel.dao.web.service.IUserInfoService;
import com.easonhotel.dao.web.vo.admin.CommodityOrderAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 商品订单接口
 */
@RestController
@RequestMapping("/finance/commodityOrder")
public class CommodityOrderController {
    @Autowired
    private ICommodityOrderService commodityOrderService;

    @Autowired
    private ICommoditySonOrderService commoditySonOrderService;

    @Autowired
    private IUserInfoService userInfoService;

    private static final String COMMODITY_ORDER_STATUS_REFUNDED = "COMMODITY_ORDER_STATUS_REFUNDED";

    private static final String COMMODITY_ORDER_STATUS_FINISHED = "COMMODITY_ORDER_STATUS_FINISHED";

    /**
     * 分页查询商品订单信息
     * @param param
     * @author PengYiXing
     * @return
     */
    @PostMapping("/pageList")
    public ResData<Object> pageList(@RequestBody CommodityOrderQueryParam param) {
        Map<String, Object> res = commodityOrderService.pageList(param);
        return ResData.create(res);
    }

    /**
     * 商品订单退款
     * @param map
     * @author PengYiXing
     * @return
     */
    @Transactional
    @PostMapping("refund")
    public ResData<Object> refund(@RequestBody Map<String, String> map) {
        CommodityOrder order = commodityOrderService.getOne(Wrappers.<CommodityOrder>query()
                .eq("deleted", 0).eq("id", map.get("id")));
        // 检查订单状态 不允许多次退款
        if (COMMODITY_ORDER_STATUS_REFUNDED.equalsIgnoreCase(order.getStatus())) {
            return ResData.create(-1, "已退款，不能多次退款");
        } else if (COMMODITY_ORDER_STATUS_FINISHED.equalsIgnoreCase(order.getStatus())) {
            return ResData.create(-1, "订单已结束，无法退款");
        }
        // 设置状态状态为退款
        order.setStatus(COMMODITY_ORDER_STATUS_REFUNDED);
        commodityOrderService.saveOrUpdate(order);
        // 退款给用户
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>query().eq("id", order.getUserId()).last("LIMIT 0, 1"));
        userInfo.setBalance(userInfo.getBalance() + order.getPrice());
        userInfoService.updateById(userInfo);
        return ResData.create(0, "操作成功");
    }

    /**
     * 标志订单结束
     * @param map
     * @author PengYiXing
     * @return
     */
    @Transactional
    @PostMapping("finish")
    public ResData<Object> finish(@RequestBody Map<String, String> map) {
        // 更新订单状态为已结束
        commodityOrderService.update(Wrappers.<CommodityOrder>update().eq("id", map.get("id")).set("status", COMMODITY_ORDER_STATUS_FINISHED));
        return ResData.create(0, "操作成功");
    }
}
