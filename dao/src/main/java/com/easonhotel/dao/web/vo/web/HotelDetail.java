package com.easonhotel.dao.web.vo.web;

import com.alibaba.fastjson.JSONObject;
import com.easonhotel.dao.web.entity.HotelFacility;
import com.easonhotel.dao.web.entity.HotelPolicy;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HotelDetail {
    private String id;

    private String name;

    private String address;

    private List<String> coverPicList = new ArrayList<>();

    private List<String> detailPicList = new ArrayList<>();

    private String description;

    private Integer openYear;

    private String telNumber;

    private String cityName;

    private Double minimumPrice;

    private Double MaximumPrice;

    private HotelPolicy hotelPolicy;

    private HotelFacility hotelFacility;

    private boolean recommend;

    private Integer score;

    private List<String> tagList = new ArrayList<>();

    private List<RoomDetail> roomDetailList = new ArrayList<>();

    private List<JSONObject> hotelCommentList = new ArrayList<>();
}
