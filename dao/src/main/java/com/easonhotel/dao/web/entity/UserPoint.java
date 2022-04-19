package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会员积分表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_user_point")
public class UserPoint extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableField("userId")
    private String userId;

    /**
     * 总积分
     */
    private Long point;

    /**
     * 会员等级
     */
    private String level;


}
