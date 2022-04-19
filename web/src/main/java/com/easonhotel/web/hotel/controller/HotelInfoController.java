package com.easonhotel.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.dto.web.HotelQueryParam;
import com.easonhotel.dao.web.entity.HotelInfo;
import com.easonhotel.dao.web.service.IHotelInfoService;
import com.easonhotel.dao.web.vo.web.HotelWebVo;
import com.easonhotel.web.security.LoginRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eh/hotel")
public class HotelInfoController {
    @Autowired
    private IHotelInfoService hotelInfoService;

    @GetMapping("/getRecommend")
    public ResData<Object> getRecommend() {
        List<HotelInfo> hotelInfos = hotelInfoService.list(Wrappers.<HotelInfo>query()
                .eq("deleted", 0)
                .eq("recommend", true)
                .last("LIMIT 0, 10"));
        return ResData.create(hotelInfos);
    }

    @GetMapping("/info")
    public ResData<Object> info(@RequestParam String id) {
        return ResData.create(hotelInfoService.info(id));
    }

    @GetMapping("/getHotelCoverImageId")
    public ResData<Object> getHotelCoverImageId(@RequestParam String hotelId) {
        HotelInfo hotelInfo = hotelInfoService.getOne(Wrappers.<HotelInfo>query().eq("id", hotelId));
        return ResData.create(hotelInfo.getCoverImage());
    }

    /**
     * 前台查询酒店信息列表
     * @param param
     * @return
     */
    @PostMapping("/pageList")
    public ResData<Object> pageList(@RequestBody HotelQueryParam param) {
        List<HotelWebVo> hotelWebVos = hotelInfoService.pageList(param);
        return ResData.create(hotelWebVos);
    }
}
