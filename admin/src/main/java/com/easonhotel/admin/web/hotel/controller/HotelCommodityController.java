package com.easonhotel.admin.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.dto.admin.QueryCommodityParam;
import com.easonhotel.dao.web.entity.HotelCommodity;
import com.easonhotel.dao.web.service.IHotelCommodityService;
import com.easonhotel.dao.web.dto.admin.AddCommodityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 酒店商品接口
 */
@RestController
@RequestMapping("/hotelCommodity")
public class HotelCommodityController {
    @Autowired
    private IHotelCommodityService hotelCommodityService;

    /**
     * 新增或修改商品信息
     * @param commodityDto
     * @author PengYiXing
     * @return
     */
    @PostMapping("/modify")
    public ResData<Object> modify(@RequestBody AddCommodityDto commodityDto) {
        hotelCommodityService.saveOrUpdate(hotelCommodityService.parseCommodityDtoToCommodity(commodityDto));
        return ResData.create(0, "操作成功");
    }

    /**
     * 删除商品信息
     * @param id 商品ID
     * @author PengYiXing
     * @return
     */
    @GetMapping("/remove")
    public ResData<Object> remove(@RequestParam String id) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
            return ResData.create(-1, "ID不能为空");
        }
        hotelCommodityService.update(Wrappers.<HotelCommodity>update()
                .eq("id", id)
                .set("deleted", true));
        return ResData.create(0, "操作成功");
    }

    /**
     * 分页查找商品数据
     * @param commodityParam
     * @author PengYiXing
     * @return
     */
    @PostMapping("/pageList")
    public ResData<Object> pageList(@RequestBody QueryCommodityParam commodityParam) {
        Map<String, Object> resMap = hotelCommodityService.pageList(commodityParam);
        return ResData.create(resMap);
    }
}
