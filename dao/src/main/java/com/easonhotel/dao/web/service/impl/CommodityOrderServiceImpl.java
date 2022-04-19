package com.easonhotel.dao.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easonhotel.common.utils.DateUtils;
import com.easonhotel.dao.sys.entity.Dict;
import com.easonhotel.dao.sys.mapper.DictMapper;
import com.easonhotel.dao.web.dto.admin.CommodityOrderQueryParam;
import com.easonhotel.dao.web.entity.*;
import com.easonhotel.dao.web.mapper.*;
import com.easonhotel.dao.web.service.ICommodityOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easonhotel.dao.web.vo.admin.CommodityOrderAdminVo;
import com.easonhotel.dao.web.vo.admin.CommoditySonOrderAdminVo;
import com.easonhotel.dao.web.vo.web.CommodityOrderWebVo;
import com.easonhotel.dao.web.vo.web.CommoditySonOrderWebVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 酒店商品父订单表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class CommodityOrderServiceImpl extends ServiceImpl<CommodityOrderMapper, CommodityOrder> implements ICommodityOrderService {
    @Autowired
    private CommodityOrderMapper orderMapper;

    @Autowired
    private CommoditySonOrderMapper sonOrderMapper;

    @Autowired
    private DictMapper dictMapper;

    @Autowired
    private HotelCommodityMapper commodityMapper;

    @Autowired
    private HotelRoomNumberMapper hotelRoomNumberMapper;

    @Autowired
    private HotelRoomMapper hotelRoomMapper;

    @Autowired
    private HotelInfoMapper hotelInfoMapper;

    @Override
    public List<CommodityOrderWebVo> pageList(Map<String, String> map) {
        Integer page = Integer.parseInt(map.get("page"));
        Integer size = Integer.parseInt(map.get("size"));
        List<CommodityOrderWebVo> vos = new ArrayList<>();
        Page<CommodityOrder> paged = orderMapper.selectPage(new Page<CommodityOrder>(page, size), Wrappers.<CommodityOrder>query()
                .eq("deleted", 0).eq("userId", map.get("userId")).orderByDesc("createdAt"));
        for (CommodityOrder order : paged.getRecords()) {
            Dict dict = dictMapper.selectOne(Wrappers.<Dict>query()
                    .eq("deleted", 0).eq("code", order.getStatus()).last("LIMIT 0, 1"));
            CommodityOrderWebVo vo = new CommodityOrderWebVo();
            vo.setId(order.getId());
            vo.setPrice(order.getPrice());
            vo.setStatus(dict.getValue());
            vo.setTimestamp(DateUtils.parseDate(order.getCreatedAt(), "yyyy年MM月dd日 HH:mm:ss"));
            if (vo.getSonOrderList() == null) {
                vo.setSonOrderList(new ArrayList<>());
            }
            List<CommoditySonOrder> sonOrders = sonOrderMapper.selectList(Wrappers.<CommoditySonOrder>query()
                    .eq("deleted", 0)
                    .eq("parentOrderId", order.getId()));
            for (CommoditySonOrder sonOrder : sonOrders) {
                CommoditySonOrderWebVo sonOrderWebVo = new CommoditySonOrderWebVo();
                HotelCommodity hotelCommodity = commodityMapper.selectOne(Wrappers.<HotelCommodity>query()
                        .eq("deleted", 0)
                        .eq("id", sonOrder.getCommodityId())
                        .last("LIMIT 0, 1"));
                sonOrderWebVo.setPrice(hotelCommodity.getPrice());
                sonOrderWebVo.setCount(sonOrder.getNumber());
                sonOrderWebVo.setCoverImage(hotelCommodity.getCoverImage());
                sonOrderWebVo.setName(hotelCommodity.getName());
                vo.getSonOrderList().add(sonOrderWebVo);
            }
            vos.add(vo);
        }
        return vos;
    }

    /**
     * 分页查询商品订单
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> pageList(CommodityOrderQueryParam param) {
        Map<String, Object> res = new HashMap<>();
        List<CommodityOrderAdminVo> orderVos = new ArrayList<>();
        // 查询商品订单信息
        Page<CommodityOrder> paged = orderMapper.selectPage(new Page<CommodityOrder>(param.getPage(), param.getSize()), Wrappers.<CommodityOrder>query()
                .eq("deleted", 0)
                .eq(StringUtils.isNotBlank(param.getId()), "id", param.getId())
                .eq(StringUtils.isNotBlank(param.getStatus()), "status", param.getStatus())
                .orderByDesc("createdAt"));
        for (CommodityOrder commodityOrder : paged.getRecords()) {
            CommodityOrderAdminVo vo = new CommodityOrderAdminVo();
            vo.setId(commodityOrder.getId());
            vo.setPrice(commodityOrder.getPrice());
            vo.setCreatedDate(DateUtils.parseDate(commodityOrder.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
            // 房间号信息
            HotelRoomNumber hotelRoomNumber = hotelRoomNumberMapper.selectOne(Wrappers.<HotelRoomNumber>query()
                    .eq("id", commodityOrder.getRoomNumber()).last("LIMIT 0, 1"));
            // 这个是房型信息
            HotelRoom hotelRoom = hotelRoomMapper.selectOne(Wrappers.<HotelRoom>query()
                    .eq("roomId", hotelRoomNumber.getRoomId()).last("LIMIT 0, 1"));
            // 酒店信息
            HotelInfo hotelInfo = hotelInfoMapper.selectOne(Wrappers.<HotelInfo>query()
                    .eq("id", hotelRoom.getHotelId()).last("LIMIT 0, 1"));
            Dict dict = dictMapper.selectOne(Wrappers.<Dict>query()
                    .eq("deleted", 0).eq("code", commodityOrder.getStatus())
                    .last("LIMIT 0, 1"));
            vo.setHotelName(hotelInfo.getName());
            vo.setRoomNumber(hotelRoomNumber.getRoomNumber());
            vo.setStatus(commodityOrder.getStatus());
            // 查询商品子订单
            List<CommoditySonOrder> sonOrders = sonOrderMapper.selectList(Wrappers.<CommoditySonOrder>query()
                    .eq("deleted", 0)
                    .eq("parentOrderId", commodityOrder.getId()));
            List<CommoditySonOrderAdminVo> sonOrderAdminVos = new ArrayList<>();
            // 封装数据
            for (CommoditySonOrder item : sonOrders) {
                CommoditySonOrderAdminVo sonOrderVo = new CommoditySonOrderAdminVo();
                HotelCommodity hotelCommodity = commodityMapper.selectOne(Wrappers.<HotelCommodity>query()
                        .eq("id", item.getCommodityId()).last("LIMIT 0, 1"));
                sonOrderVo.setCommodityName(hotelCommodity.getName());
                sonOrderVo.setCommodityPrice(hotelCommodity.getPrice());
                sonOrderVo.setCount(item.getNumber());
                sonOrderVo.setId(item.getId());
                sonOrderAdminVos.add(sonOrderVo);
            }
            vo.setSonOrderList(sonOrderAdminVos);
            orderVos.add(vo);
        }
        res.put("count", paged.getTotal());
        res.put("data", orderVos);
        return res;
    }
}
