package com.easonhotel.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.HotelFacility;
import com.easonhotel.dao.web.service.IHotelFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eh/hotelFacility")
public class HotelFacilityController {
    @Autowired
    private IHotelFacilityService hotelFacilityService;

    @GetMapping("/getByHotelId")
    public ResData<Object> getByHotelId(@RequestParam String hotelId) {
        HotelFacility hotelFacility = hotelFacilityService.getOne(Wrappers.<HotelFacility>query()
                .eq("deleted", 0)
                .eq("hotelId", hotelId)
                .last("LIMIT 0, 1"));
        return ResData.create(hotelFacility);
    }
}
