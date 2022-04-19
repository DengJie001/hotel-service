package com.easonhotel.dao.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 资源信息
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_resource")
public class Resource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空")
    private String name;

    /**
     * 资源图标
     */
    private String icon;

    /**
     * 资源路径
     */
    @NotBlank(message = "资源路径不能为空")
    private String path;

    /**
     * 父级资源ID
     */
    @TableField("parentId")
    private String parentId;

    /**
     * 资源描述
     */
    private String description;


}
