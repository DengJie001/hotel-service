package com.easonhotel.dao.web.dto.admin;

import com.easonhotel.dao.web.entity.HotelRoomInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AddHotelRoomInfoDto extends HotelRoomInfo {
    private String hotelId;

    private String roomNumber;

    private List<String> tagList = new ArrayList<>();

    private List<String> coverPicList = new ArrayList<>();

    private List<String> detailPicList = new ArrayList<>();
}
