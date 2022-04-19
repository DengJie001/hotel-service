package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 酒店-城市关联信息
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_hotel_city")
public class HotelCity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 城市ID
     */
    @TableField("cityId")
    private String cityId;

    /**
     * 酒店ID
     */
    @TableField("hotelId")
    private String hotelId;


}
