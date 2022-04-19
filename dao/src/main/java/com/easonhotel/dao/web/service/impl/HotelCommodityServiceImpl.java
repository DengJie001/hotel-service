package com.easonhotel.dao.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easonhotel.dao.sys.entity.Dict;
import com.easonhotel.dao.sys.mapper.DictMapper;
import com.easonhotel.dao.web.dto.admin.QueryCommodityParam;
import com.easonhotel.dao.web.entity.HotelCommodity;
import com.easonhotel.dao.web.entity.HotelInfo;
import com.easonhotel.dao.web.mapper.HotelCommodityMapper;
import com.easonhotel.dao.web.mapper.HotelInfoMapper;
import com.easonhotel.dao.web.service.IHotelCommodityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easonhotel.dao.web.dto.admin.AddCommodityDto;
import com.easonhotel.dao.web.vo.admin.HotelCommodityVo;
import com.easonhotel.dao.web.vo.web.CommodityWebVo;
import com.easonhotel.dao.web.vo.web.SonCommodityWebVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 酒店商品表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class HotelCommodityServiceImpl extends ServiceImpl<HotelCommodityMapper, HotelCommodity> implements IHotelCommodityService {
    @Autowired
    private HotelInfoMapper hotelInfoMapper;

    @Autowired
    private HotelCommodityMapper hotelCommodityMapper;

    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<String> parseImageId(String ids) {
        List<String> idList = new ArrayList<>();
        if (StringUtils.isBlank(ids)) {
            return idList;
        }
        idList.addAll(Arrays.asList(ids.split(",")));
        return idList;
    }

    /**
     * 商品信息分页查找
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> pageList(QueryCommodityParam param) {
        List<HotelInfo> hotelInfos = null;
        List<String> hotelIds = new ArrayList<>();
        List<HotelCommodityVo> hotelCommodityVos = new ArrayList<>();
        Map<String, Object> resMap = new HashMap<>();
        // 查询酒店信息如果酒店名称不为空的情况下表明需要查询该酒店名称下的所有商品
        if (StringUtils.isNotBlank(param.getHotelName())) {
            hotelInfos = hotelInfoMapper.selectList(Wrappers.<HotelInfo>query()
                    .eq("deleted", 0)
                    .like("name", param.getHotelName()));
            if (hotelInfos != null && hotelInfos.size() > 0) {
                for (HotelInfo hotelInfo : hotelInfos) {
                    hotelIds.add(hotelInfo.getId());
                }
            }
        }
        // 商品信息分页查询
        IPage<HotelCommodity> iPage = new Page<>(param.getPage(), param.getSize());
        IPage<HotelCommodity> paged = hotelCommodityMapper.selectPage(iPage, Wrappers.<HotelCommodity>query()
                .eq("deleted", 0)
                .like(StringUtils.isNotBlank(param.getName()), "name", param.getName())
                .in(StringUtils.isNotBlank(param.getType()), "type", param.getType())
                .in(hotelIds.size() > 0, "hotelId", hotelIds));
        List<HotelCommodity> hotelCommodityList = paged.getRecords();
        // 数据封装
        for (HotelCommodity hotelCommodity : hotelCommodityList) {
            HotelInfo hotelInfo = hotelInfoMapper.selectOne(Wrappers.<HotelInfo>query()
                    .eq("deleted", 0)
                    .eq("id", hotelCommodity.getHotelId())
                    .last("LIMIT 0, 1"));
            hotelCommodityVos.add(this.parseHotelCommodityToHotelCommodityVo(hotelCommodity, hotelInfo));
        }
        resMap.put("count", paged.getTotal());
        resMap.put("data", hotelCommodityVos);
        return resMap;
    }

    @Override
    public List<CommodityWebVo> listAll(String hotelId) {
        List<CommodityWebVo> commodityWebVoList = new ArrayList<>();
        List<Dict> dictList = dictMapper.selectList(Wrappers.<Dict>query()
                .eq("deleted", 0)
                .eq("parentCode", "COMMODITY_TYPE"));
        for (Dict dict : dictList) {
            CommodityWebVo commodityWebVo = new CommodityWebVo();
            commodityWebVo.setId(dict.getId());
            commodityWebVo.setName(dict.getValue());
            commodityWebVo.setCommodityTypeCode(dict.getCode());
            if (commodityWebVo.getSonCommodityList() == null) {
                commodityWebVo.setSonCommodityList(new ArrayList<>());
            }
            List<HotelCommodity> hotelCommodityList = hotelCommodityMapper.selectList(Wrappers.<HotelCommodity>query()
                    .eq("deleted", 0).eq("type", dict.getCode()).eq("hotelId", hotelId));
            for (HotelCommodity hotelCommodity : hotelCommodityList) {
                SonCommodityWebVo son = new SonCommodityWebVo();
                son.setId(hotelCommodity.getId());
                son.setName(hotelCommodity.getName());
                son.setCoverImage(hotelCommodity.getCoverImage());
                son.setInventory(hotelCommodity.getInventory());
                son.setSales(hotelCommodity.getSales());
                son.setPrice(hotelCommodity.getPrice());
                commodityWebVo.getSonCommodityList().add(son);
            }
            commodityWebVoList.add(commodityWebVo);
        }
        return commodityWebVoList;
    }

    @Override
    public String parseImageId(List<String> ids) {
        StringBuilder idBuilder = new StringBuilder();
        if (ids == null || ids.size() == 0) {
            return null;
        }
        for (int i = 0; i < ids.size(); ++i) {
            idBuilder.append(ids.get(i));
            if (i < ids.size() - 1) {
                idBuilder.append(",");
            }
        }
        return idBuilder.toString();
    }

    @Override
    public HotelCommodity parseCommodityDtoToCommodity(AddCommodityDto commodityDto) {
        HotelCommodity hotelCommodity = new HotelCommodity();
        hotelCommodity.setId(commodityDto.getId());
        hotelCommodity.setHotelId(commodityDto.getHotelId());
        hotelCommodity.setType(commodityDto.getType());
        hotelCommodity.setName(commodityDto.getName());
        hotelCommodity.setInventory(commodityDto.getInventory());
        hotelCommodity.setPrice(commodityDto.getPrice());
        hotelCommodity.setCoverImage(this.parseImageId(commodityDto.getCoverPicList()));
        hotelCommodity.setDetailImages(this.parseImageId(commodityDto.getDetailPicList()));
        hotelCommodity.setDescription(commodityDto.getDescription());
        hotelCommodity.setSales(commodityDto.getSales() == null || commodityDto.getSales() == 0 ? 0 : commodityDto.getSales());
        return hotelCommodity;
    }

    @Override
    public HotelCommodityVo parseHotelCommodityToHotelCommodityVo(HotelCommodity hotelCommodity, HotelInfo hotelInfo) {
        HotelCommodityVo vo = new HotelCommodityVo();
        vo.setId(hotelCommodity.getId());
        vo.setHotelId(hotelCommodity.getHotelId());
        vo.setHotelName(hotelInfo.getName());
        vo.setType(hotelCommodity.getType());
        vo.setName(hotelCommodity.getName());
        vo.setInventory(hotelCommodity.getInventory());
        vo.setPrice(hotelCommodity.getPrice());
        vo.setDescription(hotelCommodity.getDescription());
        vo.setCoverImage(hotelCommodity.getCoverImage());
        vo.setCoverPicList(this.parseImageId(hotelCommodity.getCoverImage()));
        vo.setDetailImages(hotelCommodity.getDetailImages());
        vo.setDetailPicList(this.parseImageId(hotelCommodity.getDetailImages()));
        vo.setSales(hotelCommodity.getSales());
        return vo;
    }
}
