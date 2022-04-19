package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_user_info")
public class UserInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 微信小程序唯一对应用户的openid
     */
    @TableField("openId")
    private String openId;

    /**
     * 用户手机号
     */
    @TableField("phoneNumber")
    private String phoneNumber;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户昵称
     */
    @TableField("nickName")
    private String nickName;

    /**
     * 真实姓名
     */
    @TableField("realName")
    private String realName;

    /**
     * 用户性别，0-男，1-女
     */
    private Integer gender;

    /**
     * 用户生日
     */
    private String birthday;

    /**
     * 账户余额
     */
    private Long balance;

    /**
     * 身份证号
     */
    @TableField("idCard")
    private String idCard;

    private String token;

    private String avatar;
}
