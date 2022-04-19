package com.easonhotel.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.common.utils.AttachUtils;
import com.easonhotel.common.utils.DateUtils;
import com.easonhotel.dao.att.entity.Attach;
import com.easonhotel.dao.att.service.IAttachService;
import com.easonhotel.dao.web.entity.*;
import com.easonhotel.dao.web.service.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/mock")
public class MockController {
    @Autowired
    private IHotelInfoService hotelInfoService;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private ICityInfoService cityInfoService;

    @Autowired
    private IHotelRoomInfoService hotelRoomInfoService;

    @Autowired
    private IHotelRoomNumberService hotelRoomNumberService;

    @Autowired
    private IHotelRoomService hotelRoomService;

    @Autowired
    private IHotelFacilityService hotelFacilityService;

    @Autowired
    private IHotelPolicyService hotelPolicyService;

    @Autowired
    private IHotelCommodityService commodityService;

    private static final String ATTACH_PATH = "D:\\EasonHotelFile\\Images\\";

    private String[] hotelNameArray = { "缘梦缘酒店", "康辉酒店", "缘森主题酒店", "妙亨酒店", "远香湖酒店", "茂风酒店", "威盛酒店",
    "鼎然酒店", "园馨酒店", "悦居酒店", "悦己时尚酒店", "悦洱酒店", "月城酒店", "凯麟酒店", "祥亮酒店", "米可酒店", "梦雨露酒店", "琛聪酒店",
    "亦锋酒店", "儒衡酒店"};

    private int[] openYearArray = { 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022 };

    private String[] telNumberArray = { "15562566011", "15562566012", "15562566013", "15562566014", "15562566015", "15562566016", "15562566017", "15562566018" };

    private String[] tagArray = { "小而美", "网红打卡", "智能酒店", "连锁品牌", "免费停车", "儿童乐园", "情侣酒店", "中餐厅", "西餐厅" };

    private String[] roomName = { "大床房", "家庭房", "总统套房", "情侣套房", "豪华套房" };

    private String[] roomNumberArray1 = { "101", "102", "103", "104" };

    private String[] bedTypeArray = { "1张1.5米双人床", "2张1.5米双人床", "1张1.8米双人床", "两张1.8米双人床" };

//    @GetMapping("/mockAddHotelInfo")
    @Transactional
    public ResData<Object> mockAddHotelInfo() throws IOException {
        Map<String, Object> res = new HashMap<>();
        long start = System.currentTimeMillis();
        List<CityInfo> parentCityList = cityInfoService.list(Wrappers.<CityInfo>query().eq("deleted", 0).isNull("parentCityId"));

        File dirHotelCover = new File("E:\\彭一星毕设\\材料\\images\\酒店图片\\封面");
        File[] hotelCoverFiles = dirHotelCover.listFiles();
        AttachUtils.Attachment coverImageAttachment = AttachUtils.upload(hotelCoverFiles[new Random().nextInt(hotelCoverFiles.length)], ATTACH_PATH);
        Attach coverImageAttach = new Attach();
        coverImageAttach.setFileSize(Integer.parseInt(coverImageAttachment.fileSize.toString()));
        coverImageAttach.setFileType(coverImageAttachment.fileType);
        coverImageAttach.setRealName(coverImageAttachment.realName);
        coverImageAttach.setSaveName(coverImageAttachment.saveName);
        coverImageAttach.setSavePath(coverImageAttachment.savePath);
        attachService.save(coverImageAttach);

        String detailImages = "";
        for (int j = 0; j < 10; ++j) {
            File dirHotelDetail = new File("E:\\彭一星毕设\\材料\\images\\酒店图片\\轮播图");
            File[] hotelDetailFiles = dirHotelDetail.listFiles();
            int index = new Random().nextInt(hotelDetailFiles.length);
            AttachUtils.Attachment detailImageAttachment = AttachUtils.upload(hotelDetailFiles[index], ATTACH_PATH);
            Attach detailImageAttach = new Attach();
            detailImageAttach.setFileSize(Integer.parseInt(detailImageAttachment.fileSize.toString()));
            detailImageAttach.setFileType(detailImageAttachment.fileType);
            detailImageAttach.setRealName(detailImageAttachment.realName);
            detailImageAttach.setSaveName(detailImageAttachment.saveName);
            detailImageAttach.setSavePath(detailImageAttachment.savePath);
            attachService.save(detailImageAttach);
            detailImages += detailImageAttach.getId();
            if (j < 9) {
                detailImages += ",";
            }
        }

        File dirRoomCover = new File("E:\\彭一星毕设\\材料\\images\\客房图片\\封面");
        File[] roomCoverFiles = dirRoomCover.listFiles();
        AttachUtils.Attachment roomCoverImageAttachment = AttachUtils.upload(roomCoverFiles[new Random().nextInt(roomCoverFiles.length)], ATTACH_PATH);
        Attach roomCoverImageAttach = new Attach();
        roomCoverImageAttach.setFileSize(Integer.parseInt(roomCoverImageAttachment.fileSize.toString()));
        roomCoverImageAttach.setFileType(roomCoverImageAttachment.fileType);
        roomCoverImageAttach.setRealName(roomCoverImageAttachment.realName);
        roomCoverImageAttach.setSaveName(roomCoverImageAttachment.saveName);
        roomCoverImageAttach.setSavePath(roomCoverImageAttachment.savePath);
        attachService.save(roomCoverImageAttach);

        String roomDetailImages = "";
        for (int j = 0; j < 10; ++j) {
            File dirRoomDetail = new File("E:\\彭一星毕设\\材料\\images\\客房图片\\轮播图");
            File[] roomDetailFiles = dirRoomDetail.listFiles();
            int index = new Random().nextInt(roomDetailFiles.length);
            AttachUtils.Attachment detailImageAttachment = AttachUtils.upload(roomDetailFiles[index], ATTACH_PATH);
            Attach roomDetailImageAttach = new Attach();
            roomDetailImageAttach.setFileSize(Integer.parseInt(detailImageAttachment.fileSize.toString()));
            roomDetailImageAttach.setFileType(detailImageAttachment.fileType);
            roomDetailImageAttach.setRealName(detailImageAttachment.realName);
            roomDetailImageAttach.setSaveName(detailImageAttachment.saveName);
            roomDetailImageAttach.setSavePath(detailImageAttachment.savePath);
            attachService.save(roomDetailImageAttach);
            roomDetailImages += roomDetailImageAttach.getId();
            if (j < 9) {
                roomDetailImages += ",";
            }
        }
        res.put("addImageCost", (System.currentTimeMillis() - start) / 1000);
        long startAddHotel = System.currentTimeMillis();
        for (CityInfo cityInfo : parentCityList) {
            List<CityInfo> sonCityList = cityInfoService.list(Wrappers.<CityInfo>query().eq("deleted", 0).eq("parentCityId", cityInfo.getId()));
            for (CityInfo city : sonCityList) {
                for (int i = 1; i <= 20; ++i) {
                    HotelInfo hotelInfo = new HotelInfo();
                    hotelInfo.setName(hotelNameArray[new Random().nextInt(hotelNameArray.length)]);
                    hotelInfo.setAddress(cityInfo.getName() + city.getName() + "学府大道" + i + "号");
                    hotelInfo.setCoverImage(coverImageAttach.getId());
                    hotelInfo.setDetailImages(detailImages);
                    hotelInfo.setDescription("无锡艾迪花园酒店座落于无锡市惠山新城智慧路17号，是一座按五星级标准建造，集餐饮、客房、会议、娱乐、休闲等为一体的多功能商务型花园酒店。酒店总建筑面积45360㎡，于2010年5月28日正式开业。酒店交通便捷，位于惠山大道与政和大道交汇处，紧邻沪宁高速、锡澄高速、342省道等多条交通枢纽，距轻轨1号线始发站堰桥站仅5分钟、市中心商业区20分钟车程。");
                    hotelInfo.setOpenYear(openYearArray[new Random().nextInt(openYearArray.length)]);
                    hotelInfo.setTelNumber(telNumberArray[new Random().nextInt(telNumberArray.length)]);
                    hotelInfo.setMinimumPrice(199 + new Random().nextInt(300));
                    hotelInfo.setMaximumPrice(599 + new Random().nextInt(500));
                    hotelInfo.setTag(tagArray[new Random().nextInt(tagArray.length)] + "," + tagArray[new Random().nextInt(tagArray.length)] + "," + tagArray[new Random().nextInt(tagArray.length)]);
                    hotelInfo.setRecommend(new Random().nextInt(10) % 2 == 1);
                    hotelInfo.setTitle(hotelInfo.getName() + "-夜空下被灯光点亮的唯美小楼");
                    hotelInfo.setCityId(city.getId());
                    hotelInfo.setScore(10);
                    hotelInfoService.save(hotelInfo);

                    HotelFacility hotelFacility = new HotelFacility();
                    hotelFacility.setHotFacility("停车场，无充电车位，管家服务");
                    hotelFacility.setEntertainmentFacility("台球厅，免费KTV，免费按摩，免费SPA");
                    hotelFacility.setFrontDeskService("提供口罩，行李寄存，代客泊车，24小时大堂经理，叫醒服务，信用卡结算，邮政服务");
                    hotelFacility.setCleaningService("每日换毛巾，每日换被单，洗衣服务");
                    hotelFacility.setCateringService("中餐厅，西餐厅，自助餐");
                    hotelFacility.setOtherService("24小时安保人员");
                    hotelFacility.setPublicArea("公用区WIFI，非经营性客人休息区");
                    hotelFacility.setBusinessService("传真，复印");
                    hotelFacility.setGeneralFacility("暖气，电梯，公共音响系统，公共区域禁烟，公共区域24小时监控");
                    HotelPolicy hotelPolicy = new HotelPolicy();
                    hotelPolicy.setImportantNotice("必须提供7*24小时内的核酸阴性证明");
                    hotelPolicy.setHotelTips("如有特殊要求请致电前台");
                    hotelPolicy.setCityNotice("为贯彻落实《上海市生活垃圾管理条例》相关规定，推进生活垃圾源头减量，上海市文化和旅游局特别制定《关于本市旅游住宿业不提供客房一次性日用品的实施意见》，2019年7月1日" +
                            "起，上海旅游住宿业将不再主动提供牙刷，梳子，浴擦，剃须刀，指甲刀，鞋套这些一次性日用品。若需要可咨询酒店\n受疫情影响请您在下单前提前与酒店联系，以免影响您的出行。");
                    hotelPolicy.setChildrenPolicy("酒店允许携带儿童入住\n酒店不提供婴儿加床\n酒店不提供加床\n");
                    hotelPolicy.setPetPolicy("允许携带宠物，不额外收费");
                    hotelPolicy.setAgePolicy("不允许18岁以下的客户单独办理入住");
                    hotelPolicy.setBookingPolicy("订单需要酒店供应商确认后生效，确认结果以小程序显示为准");
                    hotelPolicy.setBreakfastPolicy("餐食类型：中餐；费用：66元/人\n餐食类型：西餐；费用：66元/人");
                    hotelFacility.setHotelId(hotelInfo.getId());
                    hotelFacilityService.save(hotelFacility);
                    hotelPolicy.setHotelId(hotelInfo.getId());
                    hotelPolicyService.save(hotelPolicy);
                    for (int a = 0; a < roomName.length; ++a) {
                        HotelRoomInfo hotelRoomInfo = new HotelRoomInfo();
                        hotelRoomInfo.setName(roomName[a]);
                        hotelRoomInfo.setBedType(bedTypeArray[new Random().nextInt(bedTypeArray.length)]);
                        hotelRoomInfo.setPeopleNumber(1 + new Random().nextInt(3));
                        hotelRoomInfo.setMinimumArea(18 + new Random().nextInt(20));
                        hotelRoomInfo.setMaximumArea(20 + new Random().nextInt(20));
                        hotelRoomInfo.setPrice(199 + new Random().nextInt(200));
                        hotelRoomInfo.setTag(tagArray[new Random().nextInt(tagArray.length)] + "," + tagArray[new Random().nextInt(tagArray.length)] + "," + tagArray[new Random().nextInt(tagArray.length)]);

                        hotelRoomInfo.setCoverImage(roomCoverImageAttach.getId());
                        hotelRoomInfo.setDetailImages(roomDetailImages);
                        hotelRoomInfoService.save(hotelRoomInfo);
                        HotelRoom hotelRoom = new HotelRoom();
                        hotelRoom.setHotelId(hotelInfo.getId());
                        hotelRoom.setRoomId(hotelRoomInfo.getId());
                        hotelRoomService.save(hotelRoom);
                        for (int b = 0; b < roomNumberArray1.length; ++b) {
                            HotelRoomNumber hotelRoomNumber = new HotelRoomNumber();
                            hotelRoomNumber.setRoomId(hotelRoomInfo.getId());
                            hotelRoomNumber.setRoomNumber((b + 1) + roomNumberArray1[b]);
                            hotelRoomNumber.setStatus("ROOM_STATUS_FREE");
                            hotelRoomNumber.setPrice(199 + new Random().nextInt(200));
                            hotelRoomNumber.setArea(18 + new Random().nextInt(50));
                            hotelRoomNumberService.save(hotelRoomNumber);
                        }
                    }
                }
            }
        }
        res.put("addHotelCost", (System.currentTimeMillis() - startAddHotel) / 1000);
        res.put("totalCost", (System.currentTimeMillis() - start) / 1000);
        return ResData.create(0, res, "操作成功");
    }

//    @GetMapping("/mockAddCommodity")
    @Transactional
    public ResData<Object> mockAddCommodity() throws Exception {
        long start = System.currentTimeMillis();
        List<HotelInfo> hotelInfoList = hotelInfoService.list(Wrappers.<HotelInfo>query().eq("deleted", 0));
        List<HotelCommodity> hotelCommodityList = commodityService.list(Wrappers.<HotelCommodity>query().eq("deleted", 0));
        for (HotelInfo hotelInfo : hotelInfoList) {
            if (hotelInfo.getId().equalsIgnoreCase(hotelCommodityList.get(1).getHotelId())) {
                continue;
            }
            for (int i = 0; i < hotelCommodityList.size(); ++i) {
                HotelCommodity hotelCommodity = hotelCommodityList.get(i);
                hotelCommodity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                hotelCommodity.setHotelId(hotelInfo.getId());
                commodityService.save(hotelCommodity);
            }
        }
        long end = System.currentTimeMillis();
        return ResData.create(0, (end - start) / 1000, "操作成功");
    }
}
