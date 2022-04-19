package com.easonhotel.admin.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.*;
import com.easonhotel.dao.web.service.*;
import com.easonhotel.dao.web.dto.admin.AddHotelInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 酒店信息接口
 */
@RestController
@RequestMapping("/hotel/hotelInfo")
public class HotelInfoController {
    @Autowired
    private IHotelInfoService hotelInfoService;

    @Autowired
    private IHotelCityService hotelCityService;

    @Autowired
    private IHotelRoomInfoService hotelRoomInfoService;

    @Autowired
    private IHotelRoomService hotelRoomService;

    @Autowired
    private IHotelPolicyService hotelPolicyService;

    @Autowired
    private IHotelFacilityService hotelFacilityService;

    /**
     * 新增或修改酒店信息
     * @param hotelInfoDto
     * @author PengYiXing
     * @return
     */
    @PostMapping("/modify")
    public ResData<Object> modify(@RequestBody AddHotelInfoDto hotelInfoDto) {
        // 同名酒店验证
        HotelInfo one = hotelInfoService.getOne(Wrappers.<HotelInfo>query()
                .eq("deleted", 0)
                .eq("name", hotelInfoDto.getName())
                .last("LIMIT 0, 1"));
        if (one != null && StringUtils.isNotBlank(hotelInfoDto.getId()) && !hotelInfoDto.getId().equals(one.getId())) {
            return ResData.create(-1, "已有同名酒店");
        }
        // 新增或修改酒店基本信息
        HotelInfo hotelInfo = hotelInfoService.parseHotelInfoFromAddHotelInfoDto(hotelInfoDto);
        hotelInfoService.saveOrUpdate(hotelInfo);
        return ResData.create(0, "操作成功");
    }

    /**
     * 修改酒店是否推荐
     * @param id
     * @param recommend
     * @author PengYiXing
     * @return
     */
    @GetMapping("/modifyRecommend")
    public ResData<Object> modifyRecommend(@RequestParam String id, @RequestParam boolean recommend) {
        hotelInfoService.update(Wrappers.<HotelInfo>update()
                .eq("id", id)
                .set("recommend", recommend));
        return ResData.create(0, "操作成功");
    }

    /**
     * 删除酒店信息
     * @param map
     * @author PengYiXing
     * @return
     */
    @PostMapping("/remove")
    public ResData<Object> remove(@RequestBody Map<String, String> map) {
        // 必选参数校验
        if (!map.containsKey("id") || StringUtils.isBlank(map.get("id"))) {
            return ResData.create(-1, "酒店ID不能为空");
        }
        // 查看是否存在客房信息未删除 如果有的话要提示先删除客房信息
        List<HotelRoom> list = hotelRoomService.list(Wrappers.<HotelRoom>query()
                .eq("deleted", 0)
                .eq("hotelId", map.get("id")));
        if (list.size() > 0) {
            return ResData.create(-1, "请先删除对应的客房信息");
        }
        // 删除酒店政策信息和酒店设施信息
        hotelPolicyService.update(Wrappers.<HotelPolicy>update()
                .eq("hotelId", map.get("id"))
                .set("deleted", true));
        hotelFacilityService.update(Wrappers.<HotelFacility>update()
                .eq("hotelId", map.get("id"))
                .set("deleted", true));
        // 删除酒店信息
        hotelInfoService.update(Wrappers.<HotelInfo>update()
                .eq("id", map.get("id"))
                .set("deleted", true));
        return ResData.create(0, "操作成功");
    }

    /**
     * 分页查询酒店信息
     * @param map
     * 必选参数：page，size
     * @author PengYiXing
     * @return
     */
    @PostMapping("/pageList")
    public ResData<Object> pageList(@RequestBody Map<String, String> map) {
        // 参数校验
        if (!map.containsKey("page") || StringUtils.isBlank(map.get("page")) || !map.containsKey("size") || StringUtils.isBlank(map.get("size"))) {
            return ResData.create(-1, "分页参数不能为空");
        }
        Map<String, Object> resMap = hotelInfoService.pageList(map);
        return ResData.create(resMap);
    }
}
