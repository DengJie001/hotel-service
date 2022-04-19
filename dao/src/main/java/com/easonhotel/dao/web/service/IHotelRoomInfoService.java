package com.easonhotel.dao.web.service;

import com.easonhotel.dao.web.entity.HotelInfo;
import com.easonhotel.dao.web.entity.HotelRoom;
import com.easonhotel.dao.web.entity.HotelRoomInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easonhotel.dao.web.entity.HotelRoomNumber;
import com.easonhotel.dao.web.dto.admin.AddHotelRoomInfoDto;
import com.easonhotel.dao.web.vo.admin.HotelRoomInfoVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客房信息表 服务类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
public interface IHotelRoomInfoService extends IService<HotelRoomInfo> {
    public void add(AddHotelRoomInfoDto hotelRoomInfoDto);

    public void update(AddHotelRoomInfoDto hotelRoomInfoDto);

    public Map<String, Object> pageList(Map<String, String> map);

    public String parseListToString(List<String> list, String separator);

    public List<String> parseStringToList(String str, String separator);

    public HotelRoomInfo parseAddHotelRoomInfoDtoToHotelRoomInfo(AddHotelRoomInfoDto hotelRoomInfoDto);

    public HotelRoomInfoVo parseHotelRoomInfoToHotelRoomInfoVo(HotelRoomInfo hotelRoomInfo, HotelRoom hotelRoom, HotelInfo hotelInfo, List<HotelRoomNumber> hotelRoomNumbers);
}
