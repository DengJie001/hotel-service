package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 酒店朋友圈表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_hotel_zone")
public class HotelZone extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableField("userId")
    private String userId;

    /**
     * 朋友圈内容
     */
    private String content;

    /**
     * 图片
     */
    private String images;

    /**
     * 发送时间
     */
    private Long date;

    /**
     * 朋友圈话题
     */
    private String topic;

    /**
     * 点赞数量
     */
    @TableField("likeNumber")
    private Integer likeNumber;


}
