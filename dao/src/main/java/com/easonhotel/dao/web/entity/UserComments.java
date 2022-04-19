package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户-评价关联表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_user_comments")
public class UserComments extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableField("userId")
    private String userId;

    /**
     * 评价ID
     */
    @TableField("commentId")
    private String commentId;


}
