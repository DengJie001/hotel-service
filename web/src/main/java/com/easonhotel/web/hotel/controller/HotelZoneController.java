package com.easonhotel.web.hotel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easonhotel.common.ResData;
import com.easonhotel.common.utils.DateUtils;
import com.easonhotel.common.utils.ParseUtils;
import com.easonhotel.dao.web.entity.HotelZone;
import com.easonhotel.dao.web.entity.UserInfo;
import com.easonhotel.dao.web.service.IHotelZoneService;
import com.easonhotel.dao.web.service.IUserInfoService;
import com.easonhotel.web.hotel.vo.HotelZoneVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eh/hotelZone")
public class HotelZoneController {
    @Autowired
    private IHotelZoneService hotelZoneService;

    @Autowired
    private IUserInfoService userInfoService;

    @PostMapping("/add")
    @Transactional
    public ResData<Object> add(@RequestBody HotelZone hotelZone) {
        hotelZone.setDate(System.currentTimeMillis());
        hotelZoneService.save(hotelZone);
        return ResData.create(hotelZone);
    }

    @PostMapping("/pageList")
    public ResData<Object> pageList(@RequestBody Map<String, String> map) {
        Integer page = Integer.parseInt(map.get("page"));
        Integer size = Integer.parseInt(map.get("size"));
        List<HotelZoneVo> hotelZoneVos = new ArrayList<>();
        IPage<HotelZone> iPage = new Page<>(page, size);
        IPage<HotelZone> paged = hotelZoneService.page(iPage, Wrappers.<HotelZone>query().eq("deleted", 0).orderByDesc("createdAt"));
        for (HotelZone hotelZone : paged.getRecords()) {
            HotelZoneVo hotelZoneVo = new HotelZoneVo();
            BeanUtils.copyProperties(hotelZone, hotelZoneVo);
            UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>query().eq("deleted", 0).eq("id", hotelZone.getUserId()).last("LIMIT 0, 1"));
            hotelZoneVo.setUserNickName(userInfo.getNickName());
            hotelZoneVo.setPicIds(ParseUtils.parseList(hotelZone.getImages(), ","));
            hotelZoneVo.setSubmitDate(DateUtils.parseDate(hotelZone.getDate(), "yyyy年MM月dd日 HH:mm"));
            hotelZoneVo.setUserAvatar(userInfo.getAvatar());
            hotelZoneVos.add(hotelZoneVo);
        }
        return ResData.create(hotelZoneVos);
    }
}
