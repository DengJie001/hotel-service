package com.easonhotel.dao.web.service;

import com.easonhotel.dao.web.dto.web.HotelQueryParam;
import com.easonhotel.dao.web.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easonhotel.dao.web.dto.admin.AddHotelInfoDto;
import com.easonhotel.dao.web.vo.admin.HotelInfoVo;
import com.easonhotel.dao.web.vo.web.HotelDetail;
import com.easonhotel.dao.web.vo.web.HotelWebVo;
import com.easonhotel.dao.web.vo.web.RoomDetail;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 酒店信息表 服务类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
public interface IHotelInfoService extends IService<HotelInfo> {
    public HotelInfo parseHotelInfoFromAddHotelInfoDto(AddHotelInfoDto hotelInfoDto);

    public HotelDetail info(String id);

    public Map<String, Object> pageList(Map<String, String> map);

    public List<HotelWebVo> pageList(HotelQueryParam param);

    public String parseImageIds(List<String> list);

    public List<String> parseImageIds(String ids);

    public String parseTag(List<String> tagList);

    public List<String> parseTag(String tags);

    public HotelInfoVo parseHotelInfoVo(HotelInfo hotelInfo);

    public RoomDetail parseRoomDetail(HotelRoomInfo hotelRoomInfo, List<HotelRoomNumber> hotelRoomNumbers);

    public HotelDetail parseHotelDetail(HotelInfo hotelInfo, List<RoomDetail> roomDetails, HotelPolicy hotelPolicy, HotelFacility hotelFacility);
}
