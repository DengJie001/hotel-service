package com.easonhotel.dao.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easonhotel.dao.web.entity.HotelInfo;
import com.easonhotel.dao.web.entity.HotelRoom;
import com.easonhotel.dao.web.entity.HotelRoomInfo;
import com.easonhotel.dao.web.entity.HotelRoomNumber;
import com.easonhotel.dao.web.mapper.HotelInfoMapper;
import com.easonhotel.dao.web.mapper.HotelRoomInfoMapper;
import com.easonhotel.dao.web.mapper.HotelRoomMapper;
import com.easonhotel.dao.web.mapper.HotelRoomNumberMapper;
import com.easonhotel.dao.web.service.IHotelRoomInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easonhotel.dao.web.dto.admin.AddHotelRoomInfoDto;
import com.easonhotel.dao.web.vo.admin.HotelRoomInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 客房信息表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class HotelRoomInfoServiceImpl extends ServiceImpl<HotelRoomInfoMapper, HotelRoomInfo> implements IHotelRoomInfoService {
    @Autowired
    private HotelRoomInfoMapper hotelRoomInfoMapper;

    @Autowired
    private HotelRoomMapper hotelRoomMapper;

    @Autowired
    private HotelRoomNumberMapper hotelRoomNumberMapper;

    @Autowired
    private HotelInfoMapper hotelInfoMapper;

    private static final String ROOM_STATUS_IN_USE = "ROOM_STATUS_IN_USE";

    private static final String ROOM_STATUS_FREE = "ROOM_STATUS_FREE";

    /**
     * 新增
     * @param hotelRoomInfoDto
     */
    @Override
    public void add(AddHotelRoomInfoDto hotelRoomInfoDto) {
        // 新增或修改客房信息
        HotelRoomInfo hotelRoomInfo = this.parseAddHotelRoomInfoDtoToHotelRoomInfo(hotelRoomInfoDto);
        hotelRoomInfoMapper.insert(hotelRoomInfo);
        // 新增酒店-客房对应信息
        HotelRoom hotelRoom = new HotelRoom();
        hotelRoom.setHotelId(hotelRoomInfoDto.getHotelId());
        hotelRoom.setRoomId(hotelRoomInfo.getId());
        hotelRoomMapper.insert(hotelRoom);
        // 新增对应的门牌号信息
        List<String> roomNumbers = this.parseStringToList(hotelRoomInfoDto.getRoomNumber(), ",");
        for (String number : roomNumbers) {
            HotelRoomNumber hotelRoomNumber = new HotelRoomNumber();
            hotelRoomNumber.setRoomId(hotelRoomInfo.getId());
            hotelRoomNumber.setRoomNumber(number);
            hotelRoomNumber.setStatus(ROOM_STATUS_FREE);
            hotelRoomNumberMapper.insert(hotelRoomNumber);
        }
    }

    /**
     * 修改
     * @param hotelRoomInfoDto
     */
    @Override
    public void update(AddHotelRoomInfoDto hotelRoomInfoDto) {
        HotelRoomInfo hotelRoomInfo = this.parseAddHotelRoomInfoDtoToHotelRoomInfo(hotelRoomInfoDto);
        hotelRoomInfoMapper.updateById(hotelRoomInfo);
    }

    @Override
    public Map<String, Object> pageList(Map<String, String> map) {
        List<HotelRoomInfoVo> hotelRoomInfoVos = new ArrayList<>();
        Map<String, Object> resMap = new HashMap<>();
        Integer page = Integer.parseInt(map.get("page"));
        Integer size = Integer.parseInt(map.get("size"));
        List<String> hotelIds = new ArrayList<>();
        // 查询对应酒店下的客房
        if (map.containsKey("hotelName") && StringUtils.isNotBlank(map.get("hotelName"))) {
            List<HotelInfo> hotelInfos = hotelInfoMapper.selectList(Wrappers.<HotelInfo>query()
                    .eq("deleted", 0)
                    .like("name", map.get("hotelName")));
            for (HotelInfo hotelInfo : hotelInfos) {
                hotelIds.add(hotelInfo.getId());
            }
            if (hotelInfos == null || hotelInfos.size() == 0) {
                return resMap;
            }
        }
        IPage<HotelRoom> iPage = new Page<>(page, size);
        IPage<HotelRoom> hotelRoomPaged = hotelRoomMapper.selectPage(iPage, Wrappers.<HotelRoom>query()
                .eq("deleted", 0)
                .in(hotelIds.size() > 0, "hotelId", hotelIds));
        for (HotelRoom hotelRoom : hotelRoomPaged.getRecords()) {
            // 查询客房信息
            HotelRoomInfo hotelRoomInfo = hotelRoomInfoMapper.selectOne(Wrappers.<HotelRoomInfo>query()
                    .eq("deleted", 0)
                    .eq("id", hotelRoom.getRoomId())
                    .last("LIMIT 0, 1"));
            // 查询客房属于哪个酒店
            HotelInfo hotelInfo = hotelInfoMapper.selectOne(Wrappers.<HotelInfo>query()
                    .eq("deleted", 0)
                    .eq("id", hotelRoom.getHotelId())
                    .last("LIMIT 0, 1"));
            // 查询房间信息
            List<HotelRoomNumber> hotelRoomNumbers = hotelRoomNumberMapper.selectList(Wrappers.<HotelRoomNumber>query()
                    .eq("deleted", 0)
                    .eq("roomId", hotelRoom.getRoomId()));
            // 数据封装
            hotelRoomInfoVos.add(this.parseHotelRoomInfoToHotelRoomInfoVo(hotelRoomInfo, hotelRoom, hotelInfo, hotelRoomNumbers));
        }
        resMap.put("count", hotelRoomPaged.getTotal());
        resMap.put("data", hotelRoomInfoVos);
        return resMap;
    }

    /**
     * 将list数组转换为字符串，每两个元素使用separator分隔
     * @param list
     * @param separator
     * @return
     */
    @Override
    public String parseListToString(List<String> list, String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        if (list == null || list.size() == 0) {
            return null;
        }
        for (int i = 0; i < list.size(); ++i) {
            stringBuilder.append(list.get(i));
            if (i < list.size() - 1) {
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 将字符串分割后转换为字符串数组
     * @param str 源字符串
     * @param separator 分隔符
     * @return
     */
    @Override
    public List<String> parseStringToList(String str, String separator) {
        if (str == null) {
            return new ArrayList<>();
        }
        String[] splitRes = str.split(separator);
        List<String> list = new ArrayList<>(Arrays.asList(splitRes));
        return list;
    }

    @Override
    public HotelRoomInfo parseAddHotelRoomInfoDtoToHotelRoomInfo(AddHotelRoomInfoDto hotelRoomInfoDto) {
        HotelRoomInfo hotelRoomInfo = new HotelRoomInfo();
        hotelRoomInfo.setId(hotelRoomInfoDto.getId());
        hotelRoomInfo.setBedType(hotelRoomInfoDto.getBedType());
        hotelRoomInfo.setName(hotelRoomInfoDto.getName());
        hotelRoomInfo.setPeopleNumber(hotelRoomInfoDto.getPeopleNumber());
        hotelRoomInfo.setMinimumArea(hotelRoomInfoDto.getMinimumArea());
        hotelRoomInfo.setMaximumArea(hotelRoomInfoDto.getMaximumArea());
        hotelRoomInfo.setPrice(hotelRoomInfoDto.getPrice());
        hotelRoomInfo.setTag(this.parseListToString(hotelRoomInfoDto.getTagList(), ","));
        hotelRoomInfo.setExtraPrice(hotelRoomInfoDto.getExtraPrice());
        hotelRoomInfo.setCoverImage(this.parseListToString(hotelRoomInfoDto.getCoverPicList(), ","));
        hotelRoomInfo.setDetailImages(this.parseListToString(hotelRoomInfoDto.getDetailPicList(), ","));
        return hotelRoomInfo;
    }

    @Override
    public HotelRoomInfoVo parseHotelRoomInfoToHotelRoomInfoVo(HotelRoomInfo hotelRoomInfo, HotelRoom hotelRoom, HotelInfo hotelInfo, List<HotelRoomNumber> hotelRoomNumbers) {
        HotelRoomInfoVo hotelRoomInfoVo = new HotelRoomInfoVo();
        hotelRoomInfoVo.setId(hotelRoomInfo.getId());
        hotelRoomInfoVo.setName(hotelRoomInfo.getName());
        hotelRoomInfoVo.setBedType(hotelRoomInfo.getBedType());
        hotelRoomInfoVo.setPeopleNumber(hotelRoomInfo.getPeopleNumber());
        hotelRoomInfoVo.setMinimumArea(hotelRoomInfo.getMinimumArea());
        hotelRoomInfoVo.setMaximumArea(hotelRoomInfo.getMaximumArea());
        hotelRoomInfoVo.setPrice(hotelRoomInfo.getPrice());
        hotelRoomInfoVo.setTag(hotelRoomInfo.getTag());
        hotelRoomInfoVo.setTagList(this.parseStringToList(hotelRoomInfo.getTag(), ","));
        hotelRoomInfoVo.setCoverPicList(this.parseStringToList(hotelRoomInfo.getCoverImage(), ","));
        hotelRoomInfoVo.setDetailPicList(this.parseStringToList(hotelRoomInfo.getDetailImages(), ","));
        hotelRoomInfoVo.setExtraPrice(hotelRoomInfo.getExtraPrice());
        hotelRoomInfoVo.setHotelId(hotelRoom.getHotelId());
        hotelRoomInfoVo.setHotelName(hotelInfo.getName());
        hotelRoomInfoVo.setRoomNumberList(hotelRoomNumbers);
        StringBuilder roomNumber = new StringBuilder();
        for (int i = 0; i < hotelRoomNumbers.size(); ++i) {
            roomNumber.append(hotelRoomNumbers.get(i).getRoomNumber());
            if (i < hotelRoomNumbers.size() - 1) {
                roomNumber.append(",");
            }
        }
        hotelRoomInfoVo.setRoomNumber(roomNumber.toString());
        return hotelRoomInfoVo;
    }
}
