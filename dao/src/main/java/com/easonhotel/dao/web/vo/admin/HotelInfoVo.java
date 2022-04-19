package com.easonhotel.dao.web.vo.admin;

import lombok.Data;

import java.util.List;

@Data
public class HotelInfoVo {
    private String id;

    private String name;

    private String address;

    private String coverImage;

    private List<String> coverPicList;

    private String detailImages;

    private List<String> detailPicList;

    private String description;

    private Integer openYear;

    private String telNumber;

    private String cityId;

    private Integer minimumPrice;

    private Integer maximumPrice;

    private List<String> tagList;

    private boolean recommend;

    private String title;
}
