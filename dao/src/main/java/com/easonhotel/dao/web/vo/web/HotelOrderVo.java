package com.easonhotel.dao.web.vo.web;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class HotelOrderVo {
    private String id;

    private String hotelId;

    private String hotelName;

    private String roomId;

    private String roomName;

    private String roomNumberId;

    private String status;

    private String startDate;

    private String endDate;

    private boolean staying;

    private boolean leave;

    private Integer price;

    private Integer days;

    private JSONObject startDateJson;

    private String createdAt;

    private JSONObject endDateJson;
}
