package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 酒店房间门牌号表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_hotel_room_number")
public class HotelRoomNumber extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 房间ID
     */
    @TableField("roomId")
    private String roomId;

    /**
     * 门牌号
     */
    @TableField("roomNumber")
    private String roomNumber;

    /**
     * 客房状态
     */
    private String status;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 房间面积
     */
    private Integer area;
}
