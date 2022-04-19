package com.easonhotel.dao.web.dto.admin;

import lombok.Data;

@Data
public class CommodityOrderQueryParam {
    private Integer page = 1;

    private Integer size = 10;

    private String id;

    private String status;
}
