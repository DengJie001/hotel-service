package com.easonhotel.web.hotel.vo;

import com.easonhotel.dao.web.entity.HotelCommodity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HotelCommodityVo extends HotelCommodity {
    private List<String> detailPicList = new ArrayList<>();
}
