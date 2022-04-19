package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 酒店政策表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_hotel_policy")
public class HotelPolicy extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 酒店ID
     */
    @TableField("hotelId")
    private String hotelId;

    /**
     * 重要通知
     */
    @TableField("importantNotice")
    private String importantNotice;

    /**
     * 酒店提示
     */
    @TableField("hotelTips")
    private String hotelTips;

    /**
     * 城市通知
     */
    @TableField("cityNotice")
    private String cityNotice;

    /**
     * 儿童政策
     */
    @TableField("childrenPolicy")
    private String childrenPolicy;

    /**
     * 宠物政策
     */
    @TableField("petPolicy")
    private String petPolicy;

    /**
     * 年龄政策
     */
    @TableField("agePolicy")
    private String agePolicy;

    /**
     * 预订政策
     */
    @TableField("bookingPolicy")
    private String bookingPolicy;

    /**
     * 早餐政策
     */
    @TableField("breakfastPolicy")
    private String breakfastPolicy;


}
