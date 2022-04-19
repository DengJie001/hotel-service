package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品子订单表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_commodity_son_order")
public class CommoditySonOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父订单ID
     */
    @TableField("parentOrderId")
    private String parentOrderId;

    /**
     * 商品ID
     */
    @TableField("commodityId")
    private String commodityId;

    /**
     * 商品数量
     */
    private Integer number;


}
