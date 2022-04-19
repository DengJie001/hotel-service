package com.easonhotel.dao.web.dto.admin;

import com.easonhotel.dao.web.entity.HotelCommodity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString(callSuper = true)
public class AddCommodityDto extends HotelCommodity {
    private List<String> coverPicList = new ArrayList<>();

    private List<String> detailPicList = new ArrayList<>();
}
