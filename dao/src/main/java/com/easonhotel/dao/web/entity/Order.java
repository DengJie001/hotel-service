package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_order")
public class Order extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 入住酒店ID
     */
    @TableField("hotelId")
    private String hotelId;

    /**
     * 客房ID
     */
    @TableField("roomId")
    private String roomId;

    @TableField("roomNumberId")
    private String roomNumberId;

    /**
     * 用户ID
     */
    @TableField("userId")
    private String userId;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 入住时间
     */
    @TableField("stayTime")
    private Long stayTime;

    /**
     * 入住天数
     */
    private Integer days;

    /**
     * 总价
     */
    private Integer price;

    @TableField("userName")
    private String userName;

    @TableField("telNumber")
    private String telNumber;

    private String remark;

    @TableField("peopleNumber")
    private Integer peopleNumber;
}
