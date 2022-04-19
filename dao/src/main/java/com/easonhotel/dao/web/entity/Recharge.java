package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 充值记录表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_recharge")
public class Recharge extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableField("userId")
    private String userId;

    /**
     * 充值金额
     */
    private Integer amount;

    private String status;
}
