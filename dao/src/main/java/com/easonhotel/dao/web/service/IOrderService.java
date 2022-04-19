package com.easonhotel.dao.web.service;

import com.alibaba.fastjson.JSONObject;
import com.easonhotel.dao.sys.entity.Dict;
import com.easonhotel.dao.web.dto.admin.HotelOrderQueryParam;
import com.easonhotel.dao.web.entity.HotelInfo;
import com.easonhotel.dao.web.entity.HotelRoomInfo;
import com.easonhotel.dao.web.entity.HotelRoomNumber;
import com.easonhotel.dao.web.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easonhotel.dao.web.vo.admin.HotelOrder;
import com.easonhotel.dao.web.vo.web.HotelOrderVo;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单信息表 服务类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
public interface IOrderService extends IService<Order> {
    public List<HotelOrderVo> queryHotelOrder(Map<String, String> map);

    public Map<String, Object> pageList(HotelOrderQueryParam param);

    public HotelOrder parseHotelOrder(HotelInfo hotelInfo, HotelRoomInfo hotelRoomInfo, HotelRoomNumber hotelRoomNumber, Dict dict, Order order);

    public HotelOrderVo parseHotelOrderVo(HotelInfo hotelInfo, HotelRoomInfo hotelRoomInfo, Order order, Dict dict);

    public JSONObject parseDateJSONObject(String date);
}
