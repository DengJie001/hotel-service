package com.easonhotel.dao.web.dto.admin;

import com.easonhotel.dao.web.entity.HotelInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AddHotelInfoDto extends HotelInfo {
    /**
     * 接受来自前端的封面图片ID数组
     */
    private List<String> coverPicList = new ArrayList<>();

    /**
     * 接受来自前端的轮播图片ID数组
     */
    private List<String> detailPicList = new ArrayList<>();

    /**
     * 接收来自前端的酒店标签数组
     */
    private List<String> tagList = new ArrayList<>();
}
