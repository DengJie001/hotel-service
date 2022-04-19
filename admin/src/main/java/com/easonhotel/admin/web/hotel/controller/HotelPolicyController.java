package com.easonhotel.admin.web.hotel.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.HotelPolicy;
import com.easonhotel.dao.web.service.IHotelInfoService;
import com.easonhotel.dao.web.service.IHotelPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 酒店政策接口
 */
@RestController
@RequestMapping("/web/hotelPolicy")
public class HotelPolicyController {
    @Autowired
    private IHotelInfoService hotelInfoService;

    @Autowired
    private IHotelPolicyService hotelPolicyService;

    /**
     * 新增或修改酒店政策信息
     * @author PengYiXing
     * @return
     */
    @PostMapping("/modify")
    public ResData<Object> modify(@RequestBody HotelPolicy hotelPolicy) {
        hotelPolicyService.saveOrUpdate(hotelPolicy);
        return ResData.create(0, "操作成功");
    }

    /**
     * 根据酒店ID查询酒店政策信息
     * 如果该酒店不存在政策信息，则要返回false
     * 如果存在政策信息，则返回true，并且同时返回该酒店的政策信息
     * @param id 酒店ID
     * @author PengYiXing
     * @return
     */
    @GetMapping("/queryByHotelId/{id}")
    public ResData<Map<String, Object>> queryByHotelId(@PathVariable String id) {
        Map<String, Object> resMap = new HashMap<>();
        // 验证参数是否为空，如果酒店ID为空，则无法正常进行查询
        if (StringUtils.isBlank(id)) {
            return ResData.create(-1, null, "酒店ID不能为空");
        }
        // 查询是否有对应的政策信息
        HotelPolicy one = hotelPolicyService.getOne(Wrappers.<HotelPolicy>query()
                .eq("deleted", 0)
                .eq("hotelId", id)
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
