package com.easonhotel.dao.web.vo.admin;

import com.easonhotel.dao.web.entity.HotelRoomInfo;
import com.easonhotel.dao.web.entity.HotelRoomNumber;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HotelRoomInfoVo extends HotelRoomInfo {
    private String hotelId;

    private String hotelName;

    private String roomNumber;

    private List<HotelRoomNumber> roomNumberList = new ArrayList<>();

    private List<String> tagList = new ArrayList<>();

    private List<String> coverPicList = new ArrayList<>();

    private List<String> detailPicList = new ArrayList<>();
}
