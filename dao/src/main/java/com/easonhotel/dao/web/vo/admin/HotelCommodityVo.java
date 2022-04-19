package com.easonhotel.dao.web.vo.admin;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HotelCommodityVo {
    private String id;

    private String hotelId;

    private String hotelName;

    private String type;

    private String name;

    private Integer inventory;

    private Integer price;

    private String description;

    private String coverImage;

    private List<String> coverPicList = new ArrayList<>();

    private String detailImages;

    private List<String> detailPicList = new ArrayList<>();

    private Integer sales;
}
