package com.easonhotel.dao.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 管理员用户信息
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 管理员用户姓名
     */
    private String name;

    /**
     * 登录名
     */
    @TableField("loginName")
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 管理员头像
     */
    private String avatar;

    /**
     * 管理员手机号
     */
    @TableField("phoneNum")
    private String phoneNum;

    /**
     * 管理员邮箱
     */
    private String email;


}
