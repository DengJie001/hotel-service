package com.easonhotel.dao.web.vo.admin;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommodityOrderAdminVo {
    private String id;

    private String hotelName;

    private String roomNumber;

    private Integer price;

    private String status;

    private String createdDate;

    private List<CommoditySonOrderAdminVo> sonOrderList = new ArrayList<>();
}
