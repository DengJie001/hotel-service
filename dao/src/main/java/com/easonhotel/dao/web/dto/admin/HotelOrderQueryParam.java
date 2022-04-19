package com.easonhotel.dao.web.dto.admin;

import lombok.Data;

@Data
public class HotelOrderQueryParam {
    private String id;

    private String status;

    private String telNumber;

    private Integer page = 1;

    private Integer size = 10;
}
