package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 酒店设施表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_hotel_facility")
public class HotelFacility extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 酒店ID
     */
    @TableField("hotelId")
    private String hotelId;

    /**
     * 热门设施
     */
    @TableField("hotFacility")
    private String hotFacility;

    /**
     * 娱乐设施
     */
    @TableField("entertainmentFacility")
    private String entertainmentFacility;

    /**
     * 前台服务
     */
    @TableField("frontDeskService")
    private String frontDeskService;

    /**
     * 清洁服务
     */
    @TableField("cleaningService")
    private String cleaningService;

    /**
     * 餐饮服务
     */
    @TableField("cateringService")
    private String cateringService;

    /**
     * 其他服务
     */
    @TableField("otherService")
    private String otherService;

    /**
     * 公共区域
     */
    @TableField("publicArea")
    private String publicArea;

    /**
     * 商务服务
     */
    @TableField("businessService")
    private String businessService;

    /**
     * 通用设施
     */
    @TableField("generalFacility")
    private String generalFacility;


}
