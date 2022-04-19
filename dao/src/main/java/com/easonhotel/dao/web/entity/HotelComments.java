package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 酒店评价表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_hotel_comments")
public class HotelComments extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 评价图片
     */
    private String images;

    @TableField("hotelId")
    private String hotelId;

    @TableField("orderId")
    private String orderId;

    @TableField("userId")
    private String userId;
}
