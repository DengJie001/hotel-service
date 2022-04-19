package com.easonhotel.dao.web.vo.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommodityOrderWebVo {
    private String id;

    private String status;

    private String timestamp;

    private Integer price;

    private List<CommoditySonOrderWebVo> sonOrderList = new ArrayList<>();
}
