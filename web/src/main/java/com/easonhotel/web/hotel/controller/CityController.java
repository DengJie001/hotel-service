package com.easonhotel.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.CityInfo;
import com.easonhotel.dao.web.entity.HotelCity;
import com.easonhotel.dao.web.service.ICityInfoService;
import com.easonhotel.dao.web.service.IHotelCityService;
import com.easonhotel.dao.web.vo.admin.CityTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/eh/city")
public class CityController {
    @Autowired
    private ICityInfoService cityInfoService;

    @GetMapping("/list")
    public ResData<Object> list() {
        List<CityTree> cityTrees = new ArrayList<>();
        List<CityInfo> cityInfoList = cityInfoService.list(Wrappers.<CityInfo>query()
                .eq("deleted", 0)
                .isNull("parentCityId")
                .orderByAsc("phonics"));
        for (CityInfo cityInfo : cityInfoList) {
            CityTree cityTree = cityInfoService.parseCityTree(cityInfo);
            List<CityInfo> list = cityInfoService.list(Wrappers.<CityInfo>query()
                    .eq("deleted", 0)
                    .eq("parentCityId", cityInfo.getId())
                    .orderByAsc("phonics"));
            if (cityTree.getChildren() == null) {
                cityTree.setChildren(new ArrayList<>());
            }
            for (CityInfo item : list) {
                cityTree.getChildren().add(cityInfoService.parseCityTree(item));
            }
            cityTrees.add(cityTree);
        }
        return ResData.create(cityTrees);
    }
}
