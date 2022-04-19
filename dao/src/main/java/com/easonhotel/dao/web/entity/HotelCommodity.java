package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 酒店商品表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_hotel_commodity")
public class HotelCommodity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 酒店ID
     */
    @TableField("hotelId")
    private String hotelId;

    /**
     * 商品类别
     */
    private String type;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品数量
     */
    private Integer inventory;

    /**
     * 商品价格
     */
    private Integer price;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品封面图片
     */
    @TableField("coverImage")
    private String coverImage;

    /**
     * 商品详情图片
     */
    @TableField("detailImages")
    private String detailImages;

    /**
     * 商品销量
     */
    private Integer sales;


}
