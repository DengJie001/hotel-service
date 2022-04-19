package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品购物车
 * </p>
 *
 * @author PengYiXing
 * @since 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_cart")
public class Cart extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableField("userId")
    private String userId;

    /**
     * 商品ID
     */
    @TableField("commodityId")
    private String commodityId;

    /**
     * 商品数量
     */
    private Integer count;


}
