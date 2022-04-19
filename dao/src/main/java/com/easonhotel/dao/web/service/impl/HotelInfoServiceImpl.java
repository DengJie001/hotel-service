package com.easonhotel.dao.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easonhotel.common.utils.DateUtils;
import com.easonhotel.dao.web.dto.web.HotelQueryParam;
import com.easonhotel.dao.web.entity.*;
import com.easonhotel.dao.web.mapper.*;
import com.easonhotel.dao.web.service.IHotelInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easonhotel.dao.web.dto.admin.AddHotelInfoDto;
import com.easonhotel.dao.web.vo.admin.HotelInfoVo;
import com.easonhotel.dao.web.vo.web.HotelDetail;
import com.easonhotel.dao.web.vo.web.HotelWebVo;
import com.easonhotel.dao.web.vo.web.RoomDetail;
import com.easonhotel.dao.web.vo.web.RoomNumberDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 酒店信息表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class HotelInfoServiceImpl extends ServiceImpl<HotelInfoMapper, HotelInfo> implements IHotelInfoService {
    @Autowired
    private HotelInfoMapper hotelInfoMapper;

    @Autowired
    private HotelRoomNumberMapper hotelRoomNumberMapper;

    @Autowired
    private HotelRoomInfoMapper hotelRoomInfoMapper;

    @Autowired
    private HotelRoomMapper hotelRoomMapper;

    @Autowired
    private HotelFacilityMapper hotelFacilityMapper;

    @Autowired
    private HotelPolicyMapper hotelPolicyMapper;

    @Autowired
    private CityInfoMapper cityInfoMapper;

    @Autowired
    private HotelCommentsMapper hotelCommentsMapper;

    @Override
    public HotelInfo parseHotelInfoFromAddHotelInfoDto(AddHotelInfoDto hotelInfoDto) {
        HotelInfo hotelInfo = new HotelInfo();
        hotelInfo.setId(hotelInfoDto.getId());
        hotelInfo.setName(hotelInfoDto.getName());
        hotelInfo.setAddress(hotelInfoDto.getAddress());
        hotelInfo.setCoverImage(hotelInfoDto.getCoverPicList().get(0));
        hotelInfo.setDetailImages(this.parseImageIds(hotelInfoDto.getDetailPicList()));
        hotelInfo.setDescription(hotelInfoDto.getDescription());
        hotelInfo.setOpenYear(hotelInfoDto.getOpenYear());
        hotelInfo.setTelNumber(hotelInfoDto.getTelNumber());
        hotelInfo.setMinimumPrice(hotelInfoDto.getMinimumPrice());
        hotelInfo.setMaximumPrice(hotelInfoDto.getMaximumPrice());
        hotelInfo.setTag(this.parseTag(hotelInfoDto.getTagList()));
        hotelInfo.setCityId(hotelInfoDto.getCityId());
        hotelInfo.setRecommend(hotelInfoDto.isRecommend());
        hotelInfo.setTitle(hotelInfoDto.getTitle());
        return hotelInfo;
    }

    @Override
    public HotelDetail info(String id) {
        // 查询酒店信息
        HotelInfo hotelInfo = hotelInfoMapper.selectOne(Wrappers.<HotelInfo>query()
                .eq("deleted", 0)
                .eq("id", id)
                .last("LIMIT 0, 1"));
        if (hotelInfo == null) {
            return null;
        }
        // 查询酒店政策和设施信息
        HotelPolicy hotelPolicy = hotelPolicyMapper.selectOne(Wrappers.<HotelPolicy>query()
                .eq("deleted", 0)
                .eq("hotelId", hotelInfo.getId())
                .last("LIMIT 0, 1"));
        HotelFacility hotelFacility = hotelFacilityMapper.selectOne(Wrappers.<HotelFacility>query()
                .eq("deleted", 0)
                .eq("hotelId", hotelInfo.getId())
                .last("LIMIT 0, 1"));
        // 查询酒店-客房信息
        List<HotelRoom> hotelRooms = hotelRoomMapper.selectList(Wrappers.<HotelRoom>query()
                .eq("deleted", 0)
                .eq("hotelId", hotelInfo.getId()));
        if (hotelRooms == null || hotelRooms.size() == 0) {
            return null;
        }
        List<String> roomIds = new ArrayList<>();
        for (HotelRoom hotelRoom : hotelRooms) {
            roomIds.add(hotelRoom.getRoomId());
        }
        List<HotelRoomInfo> hotelRoomInfos = hotelRoomInfoMapper.selectList(Wrappers.<HotelRoomInfo>query()
                .eq("deleted", 0)
                .in("id", roomIds));
        if (hotelRoomInfos == null || hotelRoomInfos.size() == 0) {
            return null;
        }
        // 查询客房-门牌号-价格信息
        List<RoomDetail> roomDetails = new ArrayList<>();
        for (HotelRoomInfo hotelRoomInfo : hotelRoomInfos) {
            List<HotelRoomNumber> hotelRoomNumbers = hotelRoomNumberMapper.selectList(Wrappers.<HotelRoomNumber>query()
                    .eq("deleted", 0)
                    .eq("roomId", hotelRoomInfo.getId())
                    .eq("status", "ROOM_STATUS_FREE"));
            roomDetails.add(this.parseRoomDetail(hotelRoomInfo, hotelRoomNumbers));
        }
        // 查询评论信息
        List<HotelComments> hotelCommentsList = hotelCommentsMapper.selectList(Wrappers.<HotelComments>query()
                .eq("deleted", 0).eq("hotelId", id));
        HotelDetail hotelDetail = this.parseHotelDetail(hotelInfo, roomDetails, hotelPolicy, hotelFacility);
        List<JSONObject> hotelCommentJsonList = new ArrayList<>();
        for (HotelComments comment : hotelCommentsList) {
            JSONObject json = new JSONObject();
            json.put("date", DateUtils.parseDate(comment.getCreatedAt(), "yyyy-MM-dd"));
            json.put("content", comment.getContent());
            hotelCommentJsonList.add(json);
        }
        hotelDetail.setHotelCommentList(hotelCommentJsonList);
        return hotelDetail;
    }

    /**
     * 分页查询酒店信息
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> pageList(Map<String, String> map) {
        Map<String, Object> resMap = new HashMap<>();
        Integer page = Integer.parseInt(map.get("page"));
        Integer size = Integer.parseInt(map.get("size"));
        IPage<HotelInfo> iPage = new Page<>(page, size);
        // 执行分页查询
        QueryWrapper<HotelInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("deleted", 0)
                .like(map.containsKey("name") && StringUtils.isNotBlank(map.get("name")), "name", map.get("name"));
        IPage<HotelInfo> paged = hotelInfoMapper.selectPage(iPage, queryWrapper);
        List<HotelInfo> records = paged.getRecords();
        List<HotelInfoVo> hotelInfoVos = new ArrayList<>();
        resMap.put("count", paged.getTotal());
        // 封装数据
        for (HotelInfo hotelInfo : records) {
            hotelInfoVos.add(this.parseHotelInfoVo(hotelInfo));
        }
        resMap.put("data", hotelInfoVos);
        return resMap;
    }

    @Override
    public List<HotelWebVo> pageList(HotelQueryParam param) {
        List<HotelWebVo> hotelWebVos = new ArrayList<>();
        Page<HotelInfo> hotelInfoPage = hotelInfoMapper.selectPage(new Page<HotelInfo>(param.getPage(), param.getSize()), Wrappers.<HotelInfo>query()
                .eq("deleted", 0)
                .eq(StringUtils.isNotBlank(param.getCityId()), "cityId", param.getCityId())
                .like(StringUtils.isNotBlank(param.getHotelName()), "name", param.getHotelName())
                .orderByDesc("minimumPrice", "maximumPrice"));
        for (HotelInfo hotelInfo : hotelInfoPage.getRecords()) {
            HotelWebVo hotelWebVo = new HotelWebVo();
            System.out.println("============================================================");
            System.out.println(hotelInfo);
            System.out.println(hotelInfo.getId());
            hotelWebVo.setId(hotelInfo.getId());
            hotelWebVo.setName(hotelInfo.getName());
            hotelWebVo.setScore((double) hotelInfo.getScore());
            hotelWebVo.setCoverImage(hotelInfo.getCoverImage());
            hotelWebVo.setCommentNumber(hotelCommentsMapper.selectCount(Wrappers.<HotelComments>query()
                    .eq("deleted", 0)
                    .eq("hotelId", hotelInfo.getId())));
            hotelWebVo.setAddress(hotelInfo.getAddress());
            hotelWebVo.setTags(this.parseTag(hotelInfo.getTag()));
            hotelWebVo.setMinimumPrice(hotelInfo.getMinimumPrice());
            hotelWebVos.add(hotelWebVo);
        }
        return hotelWebVos;
    }

    @Override
    public String parseImageIds(List<String> list) {
        StringBuilder idStringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            idStringBuilder.append(list.get(i));
            if (i != list.size() - 1) {
                idStringBuilder.append(",");
            }
        }
        return idStringBuilder.toString();
    }

    @Override
    public List<String> parseImageIds(String ids) {
        List<String> idList = new ArrayList<>();
        if (StringUtils.isBlank(ids)) {
            return idList;
        }
        String[] idArray = ids.split(",");
        idList.addAll(Arrays.asList(idArray));
        return idList;
    }

    @Override
    public String parseTag(List<String> tagList) {
        if (tagList == null) {
            return null;
        }
        StringBuilder tagStringBuilder = new StringBuilder();
        for (int i = 0; i < tagList.size(); ++i) {
            tagStringBuilder.append(tagList.get(i));
            if (i != tagList.size()) {
                tagStringBuilder.append(",");
            }
        }
        return tagStringBuilder.toString();
    }

    @Override
    public List<String> parseTag(String tags) {
        List<String> tagList = new ArrayList<>();
        if (StringUtils.isBlank(tags)) {
            return tagList;
        }
        tagList.addAll(Arrays.asList(tags.split(",")));
        return tagList;
    }

    @Override
    public HotelInfoVo parseHotelInfoVo(HotelInfo hotelInfo) {
        HotelInfoVo hotelInfoVo = new HotelInfoVo();
        hotelInfoVo.setId(hotelInfo.getId());
        hotelInfoVo.setName(hotelInfo.getName());
        hotelInfoVo.setAddress(hotelInfo.getAddress());
        hotelInfoVo.setCoverImage(hotelInfo.getCoverImage());
        hotelInfoVo.setCoverPicList(this.parseImageIds(hotelInfo.getCoverImage()));
        hotelInfoVo.setDetailImages(hotelInfo.getDetailImages());
        hotelInfoVo.setDetailPicList(this.parseImageIds(hotelInfo.getDetailImages()));
        hotelInfoVo.setDescription(hotelInfo.getDescription());
        hotelInfoVo.setOpenYear(hotelInfo.getOpenYear());
        hotelInfoVo.setTelNumber(hotelInfo.getTelNumber());
        hotelInfoVo.setCityId(hotelInfo.getCityId());
        hotelInfoVo.setMinimumPrice(hotelInfo.getMinimumPrice());
        hotelInfoVo.setMaximumPrice(hotelInfo.getMaximumPrice());
        hotelInfoVo.setTagList(this.parseTag(hotelInfo.getTag()));
        hotelInfoVo.setRecommend(hotelInfo.isRecommend());
        hotelInfoVo.setTitle(hotelInfo.getTitle());
        return hotelInfoVo;
    }

    @Override
    public RoomDetail parseRoomDetail(HotelRoomInfo hotelRoomInfo, List<HotelRoomNumber> hotelRoomNumbers) {
        RoomDetail roomDetail = new RoomDetail();
        roomDetail.setId(hotelRoomInfo.getId());
        roomDetail.setBedType(hotelRoomInfo.getBedType());
        roomDetail.setName(hotelRoomInfo.getName());
        roomDetail.setPrice((double) hotelRoomInfo.getPrice());
        roomDetail.setPeopleNumber(hotelRoomInfo.getPeopleNumber());
        roomDetail.setMinimumArea((double) hotelRoomInfo.getMinimumArea());
        roomDetail.setMaximumArea((double) hotelRoomInfo.getMaximumArea());
        roomDetail.setTagList(this.parseTag(hotelRoomInfo.getTag()));
        roomDetail.setCoverPicList(this.parseImageIds(hotelRoomInfo.getCoverImage()));
        roomDetail.setDetailPicList(this.parseImageIds(hotelRoomInfo.getDetailImages()));
        roomDetail.setExtraPrice(hotelRoomInfo.getExtraPrice());
        for (HotelRoomNumber hotelRoomNumber : hotelRoomNumbers) {
            RoomNumberDetail roomNumberDetail = new RoomNumberDetail();
            roomNumberDetail.setId(hotelRoomNumber.getId());
            roomNumberDetail.setArea((double) hotelRoomNumber.getArea());
            roomNumberDetail.setPrice((double) hotelRoomNumber.getPrice());
            roomNumberDetail.setStatus(hotelRoomNumber.getStatus());
            roomNumberDetail.setRoomNumber(hotelRoomNumber.getRoomNumber());
            roomDetail.getRoomNumberDetailList().add(roomNumberDetail);
        }
        return roomDetail;
    }

    @Override
    public HotelDetail parseHotelDetail(HotelInfo hotelInfo, List<RoomDetail> roomDetails, HotelPolicy hotelPolicy, HotelFacility hotelFacility) {
        HotelDetail hotelDetail = new HotelDetail();
        hotelDetail.setId(hotelInfo.getId());
        hotelDetail.setName(hotelInfo.getName());
        hotelDetail.setAddress(hotelInfo.getAddress());
        hotelDetail.setCoverPicList(this.parseImageIds(hotelInfo.getCoverImage()));
        hotelDetail.setDetailPicList(this.parseImageIds(hotelInfo.getDetailImages()));
        hotelDetail.setDescription(hotelInfo.getDescription());
        hotelDetail.setOpenYear(hotelInfo.getOpenYear());
        hotelDetail.setTelNumber(hotelInfo.getTelNumber());
        hotelDetail.setCityName(cityInfoMapper.selectOne(Wrappers.<CityInfo>query().eq("deleted", 0).eq("id", hotelInfo.getCityId())).getName());
        hotelDetail.setMinimumPrice((double) hotelInfo.getMinimumPrice());
        hotelDetail.setMaximumPrice((double) hotelInfo.getMaximumPrice());
        hotelDetail.setHotelPolicy(hotelPolicy);
        hotelDetail.setHotelFacility(hotelFacility);
        hotelDetail.setTagList(this.parseTag(hotelInfo.getTag()));
        hotelDetail.setRoomDetailList(roomDetails);
        hotelDetail.setRecommend(hotelInfo.isRecommend());
        hotelDetail.setScore(hotelInfo.getScore());
        return hotelDetail;
    }
}
