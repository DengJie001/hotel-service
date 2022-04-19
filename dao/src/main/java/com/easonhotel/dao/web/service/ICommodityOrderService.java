package com.easonhotel.dao.web.service;

import com.easonhotel.dao.web.dto.admin.CommodityOrderQueryParam;
import com.easonhotel.dao.web.entity.CommodityOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easonhotel.dao.web.vo.admin.CommodityOrderAdminVo;
import com.easonhotel.dao.web.vo.web.CommodityOrderWebVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 酒店商品父订单表 服务类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
public interface ICommodityOrderService extends IService<CommodityOrder> {
    public List<CommodityOrderWebVo> pageList(Map<String, String> map);

    public Map<String, Object> pageList(CommodityOrderQueryParam param);
}
