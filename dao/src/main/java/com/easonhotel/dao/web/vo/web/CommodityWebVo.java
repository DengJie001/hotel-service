package com.easonhotel.dao.web.vo.web;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommodityWebVo {
    private String id;

    private String name;

    private String commodityTypeCode;

    private List<SonCommodityWebVo> sonCommodityList = new ArrayList<>();
}
