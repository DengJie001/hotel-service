package com.easonhotel.dao.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.dao.web.entity.CityInfo;
import com.easonhotel.dao.web.mapper.CityInfoMapper;
import com.easonhotel.dao.web.service.ICityInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easonhotel.dao.web.vo.admin.CityTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 城市信息表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class CityInfoServiceImpl extends ServiceImpl<CityInfoMapper, CityInfo> implements ICityInfoService {
    @Autowired
    private CityInfoMapper cityInfoMapper;

    /**
     * 按照父子关系查询所有城市信息
     * @return
     */
    @Override
    public List<CityTree> listAllCities() {
        // 查询所有的一级城市
        List<CityInfo> parentCities = cityInfoMapper.selectList(Wrappers.<CityInfo>query()
                .eq("deleted", 0)
                .isNull("parentCityId"));
        if (parentCities.size() == 0) {
            return new ArrayList<>();
        }
        // 查询一级城市下的所有二级城市
        List<CityTree> cityTrees = new ArrayList<>();
        for (CityInfo cityInfo : parentCities) {
            CityTree cityTree = parseCityTree(cityInfo);
            if (cityTree.getChildren() == null) {
                cityTree.setChildren(new ArrayList<>());
            }
            List<CityInfo> sonCities = cityInfoMapper.selectList(Wrappers.<CityInfo>query()
                    .eq("deleted", 0)
                    .eq("parentCityId", cityInfo.getId()));
            for (CityInfo sonCityInfo : sonCities) {
                cityTree.getChildren().add(parseCityTree(sonCityInfo));
            }
            cityTrees.add(cityTree);
        }
        return cityTrees;
    }

    /**
     * 将CityInfo转换为CityTree类型
     * @param cityInfo
     * @return
     */
    @Override
    public CityTree parseCityTree(CityInfo cityInfo) {
        CityTree cityTree = new CityTree();
        cityTree.setId(cityInfo.getId());
        cityTree.setKey(cityInfo.getId());
        cityTree.setName(cityInfo.getName());
        cityTree.setPhonics(cityInfo.getPhonics());
        return cityTree;
    }
}
