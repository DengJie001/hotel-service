package com.easonhotel.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.HotelComments;
import com.easonhotel.dao.web.entity.HotelInfo;
import com.easonhotel.dao.web.service.IHotelCommentsService;
import com.easonhotel.dao.web.service.IHotelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eh/hotelComment")
public class HotelCommentController {
    @Autowired
    private IHotelCommentsService hotelCommentsService;

    @Autowired
    private IHotelInfoService hotelInfoService;

    @PostMapping("/addHotelComment")
    public ResData<Object> addHotelComment(@RequestBody HotelComments hotelComments) {
        // 计算酒店平均评分
        HotelInfo hotelInfo = hotelInfoService.getOne(Wrappers.<HotelInfo>query()
                .eq("id", hotelComments.getHotelId())
                .last("LIMIT 0, 1"));
        // 查询该酒店的总评分然后计算平均分
        List<HotelComments> hotelCommentList = hotelCommentsService.list(Wrappers.<HotelComments>query()
                .eq("deleted", 0)
                .eq("hotelId", hotelComments.getHotelId()));
        Integer score = 0;
        for (HotelComments item : hotelCommentList) {
            score += item.getScore();
        }
        Integer currentScore = (score + hotelComments.getScore()) / (hotelCommentList.size() + 1);
        hotelInfo.setScore(currentScore);
        hotelInfoService.saveOrUpdate(hotelInfo);
        // 插入酒店评价
        hotelCommentsService.save(hotelComments);
        return ResData.create(0, "操作成功");
    }
}
