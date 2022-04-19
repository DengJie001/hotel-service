package com.easonhotel.dao.web.vo.web;

import lombok.Data;

import java.util.List;

@Data
public class HotelWebVo {
    private String id;

    private String name;

    private Double score;

    private Integer commentNumber;

    private String address;

    private List<String> tags;

    private Integer minimumPrice;

    private String coverImage;
}
