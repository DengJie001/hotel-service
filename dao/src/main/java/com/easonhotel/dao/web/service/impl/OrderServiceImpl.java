package com.easonhotel.dao.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easonhotel.common.utils.DateUtils;
import com.easonhotel.dao.sys.entity.Dict;
import com.easonhotel.dao.sys.mapper.DictMapper;
import com.easonhotel.dao.web.dto.admin.HotelOrderQueryParam;
import com.easonhotel.dao.web.entity.HotelInfo;
import com.easonhotel.dao.web.entity.HotelRoomInfo;
import com.easonhotel.dao.web.entity.HotelRoomNumber;
import com.easonhotel.dao.web.entity.Order;
import com.easonhotel.dao.web.mapper.*;
import com.easonhotel.dao.web.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easonhotel.dao.web.vo.admin.HotelOrder;
import com.easonhotel.dao.web.vo.web.HotelOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private HotelInfoMapper hotelInfoMapper;

    @Autowired
    private HotelRoomMapper hotelRoomMapper;

    @Autowired
    private HotelRoomNumberMapper hotelRoomNumberMapper;

    @Autowired
    private HotelRoomInfoMapper hotelRoomInfoMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<HotelOrderVo> queryHotelOrder(Map<String, String> map) {
        List<HotelOrderVo> hotelOrderVos = new ArrayList<>();
        // 先查询订单信息
        List<Order> orders = orderMapper.selectList(Wrappers.<Order>query()
                .eq("deleted", 0)
                .eq("userId", map.get("userId"))
                .orderByDesc("createdAt", "stayTime"));
        for (Order order : orders) {
            // 查询酒店信息
            HotelInfo hotelInfo = hotelInfoMapper.selectOne(Wrappers.<HotelInfo>query()
                    .eq("id", order.getHotelId()).last("LIMIT 0, 1"));
            // 查询客房信息
            HotelRoomInfo hotelRoomInfo = hotelRoomInfoMapper.selectOne(Wrappers.<HotelRoomInfo>query()
                    .eq("id", order.getRoomId()).last("LIMIT 0, 1"));
            // 查询客房状态字典项
            Dict dict = dictMapper.selectOne(Wrappers.<Dict>query()
                    .eq("deleted", 0)
                    .eq("code", order.getStatus())
                    .last("LIMIT 0, 1"));
            hotelOrderVos.add(this.parseHotelOrderVo(hotelInfo, hotelRoomInfo, order, dict));
        }
        return hotelOrderVos;
    }

    /**
     * 后台分页查询数据
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> pageList(HotelOrderQueryParam param) {
        Map<String, Object> resMap = new HashMap<>();
        List<HotelOrder> hotelOrders = new ArrayList<>();
        // 分页查询
        IPage<Order> iPage = new Page<>(param.getPage(), param.getSize());
        IPage<Order> paged = orderMapper.selectPage(iPage, Wrappers.<Order>query()
                .eq("deleted", 0)
                .eq(StringUtils.isNotBlank(param.getStatus()), "status", param.getStatus())
                .eq(StringUtils.isNotBlank(param.getId()), "id", param.getId())
                .eq(StringUtils.isNotBlank(param.getTelNumber()), "telNumber", param.getTelNumber())
                .orderByDesc("createdAt"));
        List<Order> orders = paged.getRecords();
        if (orders == null || orders.size() == 0) {
            resMap.put("count", 0);
            resMap.put("data", new ArrayList<>());
            return resMap;
        }
        for (Order order : orders) {
            // 酒店信息
            HotelInfo hotelInfo = hotelInfoMapper.selectOne(Wrappers.<HotelInfo>query()
                    .eq("id", order.getHotelId()).last("LIMIT 0, 1"));
            // 房型信息
            HotelRoomInfo hotelRoomInfo = hotelRoomInfoMapper.selectOne(Wrappers.<HotelRoomInfo>query()
                    .eq("id", order.getRoomId()).last("LIMIT 0, 1"));
            // 客房信息
            HotelRoomNumber hotelRoomNumber = hotelRoomNumberMapper.selectOne(Wrappers.<HotelRoomNumber>query()
                    .eq("id", order.getRoomNumberId()).last("LIMIT 0, 1"));
            Dict dict = dictMapper.selectOne(Wrappers.<Dict>query()
                    .eq("code", order.getStatus()).last("LIMIT 0, 1"));
            hotelOrders.add(this.parseHotelOrder(hotelInfo, hotelRoomInfo, hotelRoomNumber, dict, order));
        }
        resMap.put("count", paged.getTotal());
        resMap.put("data", hotelOrders);
        return resMap;
    }

    @Override
    public HotelOrder parseHotelOrder(HotelInfo hotelInfo, HotelRoomInfo hotelRoomInfo, HotelRoomNumber hotelRoomNumber, Dict dict, Order order) {
        HotelOrder hotelOrder = new HotelOrder();
        hotelOrder.setId(order.getId());
        hotelOrder.setHotelId(hotelInfo.getId());
        hotelOrder.setHotelName(hotelInfo.getName());
        hotelOrder.setRoomId(hotelRoomInfo.getId());
        hotelOrder.setRoomName(hotelRoomInfo.getName());
        hotelOrder.setRoomNumberId(hotelRoomNumber.getId());
        hotelOrder.setRoomNumber(hotelRoomNumber.getRoomNumber());
        hotelOrder.setStartDate(DateUtils.parseDate(order.getStayTime()));
        hotelOrder.setStatus(dict.getValue());
        hotelOrder.setDays(order.getDays());
        hotelOrder.setOrderAmount(order.getPrice());
        hotelOrder.setUserName(order.getUserName());
        hotelOrder.setTelNumber(order.getTelNumber());
        hotelOrder.setPeopleNumber(order.getPeopleNumber());
        hotelOrder.setCreatedAt(DateUtils.parseDate(order.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
        return hotelOrder;
    }

    @Override
    public HotelOrderVo parseHotelOrderVo(HotelInfo hotelInfo, HotelRoomInfo hotelRoomInfo, Order order, Dict dict) {
        HotelOrderVo vo = new HotelOrderVo();
        vo.setId(order.getId());
        vo.setHotelId(hotelInfo.getId());
        vo.setHotelName(hotelInfo.getName());
        vo.setRoomId(hotelRoomInfo.getId());
        vo.setRoomName(hotelRoomInfo.getName());
        vo.setStatus(dict.getValue());
        vo.setStartDate(DateUtils.parseDate(order.getStayTime()));
        vo.setEndDate(DateUtils.parseDate(order.getStayTime() + (order.getDays() * 24 * 60 * 60 * 1000)));
        vo.setStaying(System.currentTimeMillis() >= order.getStayTime());
        vo.setLeave(System.currentTimeMillis() >= (order.getStayTime() + (order.getDays() * 24 * 60 * 60 * 1000)));
        vo.setPrice(order.getPrice());
        vo.setDays(order.getDays());
        vo.setStartDateJson(this.parseDateJSONObject(vo.getStartDate()));
        vo.setEndDateJson(this.parseDateJSONObject(vo.getEndDate()));
        System.out.println("========================================================================");
        vo.setCreatedAt(DateUtils.parseDate(order.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
        return vo;
    }

    @Override
    public JSONObject parseDateJSONObject(String date) {
        JSONObject dateJson = new JSONObject();
        dateJson.put("year", Integer.parseInt(date.split("-")[0]));
        dateJson.put("month", Integer.parseInt(date.split("-")[1]));
        dateJson.put("date", date.split("-")[2]);
        return dateJson;
    }
}
