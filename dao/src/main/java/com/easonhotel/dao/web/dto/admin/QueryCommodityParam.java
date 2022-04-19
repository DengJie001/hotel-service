package com.easonhotel.dao.web.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class QueryCommodityParam {
    @NotBlank(message = "缺少分页参数：Page")
    private Integer page;

    @NotBlank(message = "缺少分页参数：size")
    private Integer size;

    private String hotelName;

    private String type;

    private String name;
}
