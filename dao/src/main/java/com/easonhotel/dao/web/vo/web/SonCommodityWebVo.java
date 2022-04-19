package com.easonhotel.dao.web.vo.web;

import lombok.Data;

@Data
public class SonCommodityWebVo {
    private String id;

    private String name;

    private String coverImage;

    private Integer inventory;

    private Integer sales;

    private Integer price;
}
