package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 酒店商品父订单表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_commodity_order")
public class CommodityOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableField("userId")
    private String userId;

    /**
     * 客房门牌号
     */
    @TableField("roomNumber")
    private String roomNumber;

    /**
     * 订单总价
     */
    private Integer price;

    /**
     * 订单状态
     */
    private String status;


}
