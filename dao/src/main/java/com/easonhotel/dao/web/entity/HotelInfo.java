package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 酒店信息表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_hotel_info")
public class HotelInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 酒店名称
     */
    private String name;

    /**
     * 酒店地址
     */
    private String address;

    /**
     * 酒店封面图片
     */
    @TableField("coverImage")
    private String coverImage;

    /**
     * 轮播多图
     */
    @TableField("detailImages")
    private String detailImages;

    /**
     * 酒店介绍
     */
    private String description;

    /**
     * 开业时间
     */
    @TableField("openYear")
    private Integer openYear;

    /**
     * 联系电话
     */
    @TableField("telNumber")
    private String telNumber;

    /**
     * 最低价
     */
    @TableField("minimumPrice")
    private Integer minimumPrice;

    /**
     * 最高价
     */
    @TableField("maximumPrice")
    private Integer maximumPrice;

    /**
     * 酒店标签
     */
    private String tag;

    /**
     * 是否首页推荐
     */
    private boolean recommend;

    /**
     * 推荐标题
     */
    private String title;

    /**
     * 所在城市
     */
    @TableField("cityId")
    private String cityId;

    private Integer score;
}
