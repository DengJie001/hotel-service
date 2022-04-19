package com.easonhotel.dao.web.vo.admin;

import com.easonhotel.dao.web.entity.CityInfo;
import lombok.Data;

import java.util.List;

@Data
public class CityTree {
    private String id;

    private String phonics;

    private String name;

    private String key;

    private List<CityTree> children;
}
