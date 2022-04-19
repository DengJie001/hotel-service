package com.easonhotel.admin.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.HotelFacility;
import com.easonhotel.dao.web.service.IHotelFacilityService;
import com.easonhotel.dao.web.service.IHotelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 酒店设施接口
 */
@RestController
@RequestMapping("/web/hotelFacility")
public class HotelFacilityController {
    @Autowired
    private IHotelInfoService hotelInfoService;

    @Autowired
    private IHotelFacilityService hotelFacilityService;

    /**
     * 新增或修改酒店设施信息
     * @param hotelFacility
     * @author PengYiXing
     * @return
     */
    @PostMapping("/modify")
    private ResData<Object> modify(@RequestBody HotelFacility hotelFacility) {
        hotelFacilityService.saveOrUpdate(hotelFacility);
        return ResData.create(0, "操作成功");
    }

    /**
     * 根据酒店ID查询酒店设施信息
     * 如果存在设施信息。返回true，并同时返回酒店的设施信息
     * 否则返回false
     * @param hotelId 酒店ID
     * @author PengYiXing
     * @return
     */
    @GetMapping("/queryByHotelId/{hotelId}")
    public ResData<Map<String, Object>> queryByHotelId(@PathVariable String hotelId) {
        Map<String, Object> resMap = new HashMap<>();
        // 参数校验
        if (StringUtils.isBlank(hotelId)) {
            return ResData.create(-1, "酒店ID不能为空");
        }
        // 查询设施信息是否存在
        HotelFacility one = hotelFacilityService.getOne(Wrappers.<HotelFacility>query()
                .eq("deleted", 0)
                .eq("hotelId", hotelId)
                .last("LIMIT 0, 1"));
        if (one == null) {
            resMap.put("exist", false);
        } else {
            resMap.put("exist", true);
            resMap.put("data", one);
        }
        return ResData.create(resMap);
    }
}
