package com.easonhotel.admin.web.city.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.CityInfo;
import com.easonhotel.dao.web.service.ICityInfoService;
import com.easonhotel.dao.web.vo.admin.CityTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 城市接口
 */
@RestController
@RequestMapping("/city")
public class CityController {
    @Autowired
    private ICityInfoService cityInfoService;

    /**
     * 新增或修改城市信息
     * @param cityInfo
     * @author PengYiXing
     * @return
     */
    @PostMapping("/modify")
    public ResData<Object> modify(@RequestBody @Validated CityInfo cityInfo) {
        // 验证是否有同名城市
        CityInfo one = cityInfoService.getOne(Wrappers.<CityInfo>query().eq("deleted", 0).eq("name", cityInfo.getName()).last("LIMIT 0, 1"));
        if (one != null) {
            return ResData.create(-1, "存在同名城市");
        }
        // 插入或修改数据
        cityInfoService.saveOrUpdate(cityInfo);
        return ResData.create(0, "操作成功");
    }

    /**
     * 删除城市信息
     * @param map
     * @author PengYiXing
     * @return
     */
    @PostMapping("/remove")
    public ResData<Object> remove(@RequestBody Map<String, String> map) {
        // 必选参数校验
        if (!map.containsKey("id") || StringUtils.isBlank(map.get("id"))) {
            return ResData.create(-1, "城市ID不能为空");
        }
        // 判断是否为省份或直辖市,如果是则要验证该城市下是否有下级地区
        List<CityInfo> sonCityList = cityInfoService.list(Wrappers.<CityInfo>query()
                .eq("deleted", 0)
                .eq("parentCityId", map.get("id")));
        if (sonCityList.size() > 0) {
            return ResData.create(-1, "请先删除二级地区");
        }
        // 执行删除
        cityInfoService.update(Wrappers.<CityInfo>update().eq("id", map.get("id")).set("deleted", true));
        return ResData.create(0, "操作成功");
    }

    /**
     * 按照父子级关系查询所有的城市信息
     * @author PengYiXing
     * @return
     */
    @PostMapping("/listAllCities")
    public ResData<Object> listAllCities() {
        List<CityTree> cityTrees = cityInfoService.listAllCities();
        return ResData.create(cityTrees);
    }

    /**
     * 查询所有父级城市
     * @author PengYiXing
     * @return
     */
    @PostMapping("/listAllParentCities")
    public ResData<List<CityInfo>> listAllParentCities() {
        List<CityInfo> allParentCities = cityInfoService.list(Wrappers.<CityInfo>query()
                .eq("deleted", 0)
                .isNull("parentCityId"));
        return ResData.create(allParentCities);
    }

    /**
     * 查询所有二级城市
     * @author PengYiXing
     * @return
     */
    @PostMapping("/listAllSonCities")
    public ResData<List<CityInfo>> listAllSonCities() {
        List<CityInfo> allSonCities = cityInfoService.list(Wrappers.<CityInfo>query()
                .eq("deleted", 0)
                .isNotNull("parentCityId"));
        return ResData.create(allSonCities);
    }

    /**
     * 查询城市列表
     * @param map
     * @return
     */
    @PostMapping("/queryByCityName")
    public ResData<List<CityInfo>> queryByCityName(@RequestBody Map<String, String> map) {
        List<CityInfo> cities = cityInfoService.list(Wrappers.<CityInfo>query()
                .eq("deleted", 0)
                .and(
                        queryWrapper -> queryWrapper.isNotNull("parentCityId")
                )
                .and(
                        map.containsKey("name") && StringUtils.isNotBlank(map.get("name")),
                        queryWrapper -> queryWrapper.like("name", map.get("name"))
                                .or(
                                map.containsKey("name") && StringUtils.isNotBlank(map.get("name")),
                                        queryWrapper1 -> queryWrapper1.like("phonics", map.get("name"))
                                )
                )

        );
        return ResData.create(cities);
    }
}
