package com.easonhotel.dao.web.service;

import com.easonhotel.dao.web.entity.CityInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easonhotel.dao.web.vo.admin.CityTree;

import java.util.List;

/**
 * <p>
 * 城市信息表 服务类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
public interface ICityInfoService extends IService<CityInfo> {
    public List<CityTree> listAllCities();

    public CityTree parseCityTree(CityInfo cityInfo);
}
