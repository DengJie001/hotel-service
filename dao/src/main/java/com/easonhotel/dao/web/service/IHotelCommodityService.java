package com.easonhotel.dao.web.service;

import com.easonhotel.dao.web.dto.admin.QueryCommodityParam;
import com.easonhotel.dao.web.entity.HotelCommodity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easonhotel.dao.web.dto.admin.AddCommodityDto;
import com.easonhotel.dao.web.entity.HotelInfo;
import com.easonhotel.dao.web.vo.admin.HotelCommodityVo;
import com.easonhotel.dao.web.vo.web.CommodityWebVo;

import javax.management.Query;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 酒店商品表 服务类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
public interface IHotelCommodityService extends IService<HotelCommodity> {
    public List<String> parseImageId(String ids);

    public Map<String, Object> pageList(QueryCommodityParam param);

    public List<CommodityWebVo> listAll(String hotelId);

    public String parseImageId(List<String> ids);

    public HotelCommodity parseCommodityDtoToCommodity(AddCommodityDto commodityDto);

    public HotelCommodityVo parseHotelCommodityToHotelCommodityVo(HotelCommodity hotelCommodity, HotelInfo hotelInfo);
}
