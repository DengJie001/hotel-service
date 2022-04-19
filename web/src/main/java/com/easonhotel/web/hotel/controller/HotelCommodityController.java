package com.easonhotel.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.sys.service.IDictService;
import com.easonhotel.dao.web.entity.HotelCommodity;
import com.easonhotel.dao.web.service.IHotelCommodityService;
import com.easonhotel.dao.web.vo.web.CommodityWebVo;
import com.easonhotel.web.hotel.vo.HotelCommodityVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eh/commodity")
public class HotelCommodityController {
    @Autowired
    private IHotelCommodityService commodityService;

    @Autowired
    private IDictService dictService;

    @GetMapping("/list")
    public ResData<Object> list(@RequestParam String hotelId) {
        List<CommodityWebVo> commodityWebVos = commodityService.listAll(hotelId);
        return ResData.create(commodityWebVos);
    }

    @GetMapping("/detail/{id}")
    public ResData<Object> detail(@PathVariable String id) {
        HotelCommodity hotelCommodity = commodityService.getOne(Wrappers.<HotelCommodity>query()
                .eq("deleted", 0).eq("id", id).last("LIMIT 0, 1"));
        HotelCommodityVo hotelCommodityVo = new HotelCommodityVo();
        BeanUtils.copyProperties(hotelCommodity, hotelCommodityVo);
        hotelCommodityVo.setDetailPicList(commodityService.parseImageId(hotelCommodity.getDetailImages()));
        return ResData.create(hotelCommodityVo);
    }

    @PostMapping("/detailList")
    public ResData<Object> detailList(@RequestBody List<String> ids) {
        if (ids == null || ids.size() == 0) {
            return ResData.create(new ArrayList<>());
        }
        List<HotelCommodity> list = commodityService.list(Wrappers.<HotelCommodity>query()
                .eq("deleted", 0)
                .in("id", ids));
        return ResData.create(list);
    }
}
