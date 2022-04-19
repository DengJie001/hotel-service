package com.easonhotel.admin.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.HotelRoomNumber;
import com.easonhotel.dao.web.service.IHotelRoomNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客房房间信息接口
 */
@RestController
@RequestMapping("/web/hotelRoomNumber")
public class HotelRoomNumberController {
    @Autowired
    private IHotelRoomNumberService hotelRoomNumberService;

    /**
     * 根据客房ID查询该客房下所有的房间
     * @param roomId
     * @author PengYiXing
     * @return
     */
    @GetMapping("/queryByRoomId/{roomId}")
    public ResData<Object> queryByRoomId(@PathVariable String roomId) {
        List<HotelRoomNumber> hotelRoomNumberList = hotelRoomNumberService.list(Wrappers.<HotelRoomNumber>query()
                .eq("deleted", 0)
                .eq("roomId", roomId));
        return ResData.create(hotelRoomNumberList);
    }

    /**
     * 修改房间信息
     * @param hotelRoomNumber
     * @author PengYiXing
     * @return
     */
    @PostMapping("/modify")
    public ResData<Object> modify(@RequestBody HotelRoomNumber hotelRoomNumber) {
        hotelRoomNumberService.updateById(hotelRoomNumber);
        return ResData.create(0, "操作成功");
    }
    /**
     * 删除客房的门牌号信息
     * @param id
     * @author PengYiXing
     * @return
     */
    @GetMapping("/remove")
    public ResData<Object> remove(@RequestParam String id) {
        if (StringUtils.isBlank(id)) {
            return ResData.create(-1, "ID不能为空");
        }
        // 执行删除
        hotelRoomNumberService.update(Wrappers.<HotelRoomNumber>update()
                .eq("id", id)
                .set("deleted", true));
        return ResData.create(0, "操作成功");
    }
}
