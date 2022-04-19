package com.easonhotel.web.hotel.vo;

import com.easonhotel.dao.web.entity.HotelZone;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HotelZoneVo extends HotelZone {
    private List<String> picIds = new ArrayList<>();

    private String submitDate;

    private String userNickName;

    private String userAvatar;
}
