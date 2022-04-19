package com.easonhotel.dao.web.dto.web;

import lombok.Data;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;

@Data
public class HotelQueryParam {
    private String hotelName;

    private String cityId;

    private Integer page = 1;

    private Integer size = 10;
}
