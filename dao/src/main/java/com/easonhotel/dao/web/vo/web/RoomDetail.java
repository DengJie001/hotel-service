package com.easonhotel.dao.web.vo.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomDetail {
    private String id;

    private String name;

    private String bedType;

    private Integer peopleNumber;

    private Double minimumArea;

    private Double maximumArea;

    private Double price;

    private List<String> tagList = new ArrayList<>();

    private List<String> coverPicList = new ArrayList<>();

    private List<String> detailPicList = new ArrayList<>();

    private String extraPrice;

    private List<RoomNumberDetail> roomNumberDetailList = new ArrayList<>();
}
