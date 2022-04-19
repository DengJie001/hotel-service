package com.easonhotel.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.HotelFacility;
import com.easonhotel.dao.web.entity.HotelPolicy;
import com.easonhotel.dao.web.service.IHotelPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eh/hotelPolicy")
public class HotelPolicyController {
    @Autowired
    private IHotelPolicyService hotelPolicyService;

    @GetMapping("/getByHotelId")
    public ResData<Object> getByHotelId(@RequestParam String hotelId) {
        HotelPolicy hotelPolicy = hotelPolicyService.getOne(Wrappers.<HotelPolicy>query()
                .eq("deleted", 0)
                .eq("hotelId", hotelId)
                .last("LIMIT 0, 1"));
        return ResData.create(hotelPolicy);
    }
}
