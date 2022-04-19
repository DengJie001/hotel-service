package com.easonhotel.admin.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.HotelRoom;
import com.easonhotel.dao.web.entity.HotelRoomInfo;
import com.easonhotel.dao.web.entity.HotelRoomNumber;
import com.easonhotel.dao.web.service.IHotelInfoService;
import com.easonhotel.dao.web.service.IHotelRoomInfoService;
import com.easonhotel.dao.web.service.IHotelRoomNumberService;
import com.easonhotel.dao.web.service.IHotelRoomService;
import com.easonhotel.dao.web.dto.admin.AddHotelRoomInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 客房信息
 */
@RestController
@RequestMapping("/hotel/hotelRoom")
public class HotelRoomInfoController {
    @Autowired
    private IHotelInfoService hotelInfoService;

    @Autowired
    private IHotelRoomInfoService hotelRoomInfoService;

    @Autowired
    private IHotelRoomNumberService hotelRoomNumberService;

    @Autowired
    private IHotelRoomService hotelRoomService;

    private static final String ROOM_STATUS_IN_USE = "ROOM_STATUS_IN_USE";

    /**
     * 新增酒店客房数据
     * 其中，新增时前端传过来的客房门牌号是用-分隔的字符串，需要将他们分割后分配给各个客房
     * @param hotelRoomInfoDto
     * @return
     */
    @PostMapping("/add")
    public ResData<Object> add(@RequestBody AddHotelRoomInfoDto hotelRoomInfoDto) {
        hotelRoomInfoService.add(hotelRoomInfoDto);
        return ResData.create(0, "操作成功");
    }

    /**
     * 更新酒店客房门牌号信息
     * @param hotelRoomInfoDto
     * @author PengYiXing
     * @return
     */
    @PostMapping("/update")
    public ResData<Object> update(@RequestBody AddHotelRoomInfoDto hotelRoomInfoDto) {
        hotelRoomInfoService.update(hotelRoomInfoDto);
        return ResData.create(0, "操作成功");
    }

    /**
     * 删除客房信息
     * @param id 客房ID
     * @author PengYiXing
     * @return
     */
    @GetMapping("/remove")
    public ResData<Object> remove(@RequestParam String id) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
            return ResData.create(-1, "ID不能为空");
        }
        // 验证该客房是否还在使用中 如果在使用中 则不允许删除
        List<HotelRoomNumber> list = hotelRoomNumberService.list(Wrappers.<HotelRoomNumber>query()
                .eq("deleted", 0)
                .eq("status", ROOM_STATUS_IN_USE)
                .eq("roomId", id));
        if (list != null && list.size() > 0) {
            return ResData.create(-1, "该客房仍在使用中，请等待退房后再进行操作");
        }
        // 删除酒店客房对应关系
        hotelRoomService.update(Wrappers.<HotelRoom>update()
                .eq("roomId", id)
                .set("deleted", true));
        // 删除客房-门牌号对应关系
        hotelRoomNumberService.update(Wrappers.<HotelRoomNumber>update()
                .eq("roomId", id)
                .set("deleted", true));
        // 删除客房信息
        hotelRoomInfoService.update(Wrappers.<HotelRoomInfo>update()
                .eq("id", id)
                .set("deleted", true));
        return ResData.create(0, "操作成功");
    }

    /**
     * 分页查询酒店信息
     * @param map
     * @author PengYiXing
     * @return
     */
    @PostMapping("/pageList")
    public ResData<Object> pageList(@RequestBody Map<String, String> map) {
        // 必选参数校验
        if (!map.containsKey("page") || StringUtils.isBlank(map.get("page")) || !map.containsKey("size") || StringUtils.isBlank(map.get("size"))) {
            return ResData.create(-1, "分页参数异常");
        }
        Map<String, Object> resMap = hotelRoomInfoService.pageList(map);
        return ResData.create(resMap);
    }
}
