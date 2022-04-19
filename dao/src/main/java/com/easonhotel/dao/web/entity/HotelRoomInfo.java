package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客房信息表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_hotel_room_info")
public class HotelRoomInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 房间名称(类型)
     */
    private String name;

    /**
     * 床型(例如：双人床一张，单人床两张)
     */
    @TableField("bedType")
    private String bedType;

    /**
     * 可入住人数
     */
    @TableField("peopleNumber")
    private Integer peopleNumber;

    /**
     * 最小面积
     */
    @TableField("minimumArea")
    private Integer minimumArea;

    /**
     * 最大面积
     */
    @TableField("maximumArea")
    private Integer maximumArea;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 标签
     */
    private String tag;

    /**
     * 封面图片
     */
    @TableField("coverImage")
    private String coverImage;

    /**
     * 轮播图片
     */
    @TableField("detailImages")
    private String detailImages;

    /**
     * 额外付费及付费详情
     */
    @TableField("extraPrice")
    private String extraPrice;


}
