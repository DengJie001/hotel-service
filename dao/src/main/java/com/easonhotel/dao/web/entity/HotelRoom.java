package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 酒店客房关联信息
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_hotel_room")
public class HotelRoom extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 酒店ID
     */
    @TableField("hotelId")
    private String hotelId;

    /**
     * 客房ID
     */
    @TableField("roomId")
    private String roomId;


}
