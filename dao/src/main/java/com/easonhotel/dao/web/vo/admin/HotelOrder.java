package com.easonhotel.dao.web.vo.admin;

import lombok.Data;

@Data
public class HotelOrder {
    private String id;

    private String hotelId;

    private String hotelName;

    private String roomId;

    private String roomName;

    private String roomNumberId;

    private String roomNumber;

    private String startDate;

    private String status;

    private Integer days;

    private Integer orderAmount;

    private String userName;

    private String telNumber;

    private Integer peopleNumber;

    private String remark;

    private String createdAt;
}
