package com.easonhotel.dao.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict")
public class Dict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父级字典ID
     */
    @TableField("parentCode")
    private String parentCode;

    /**
     * 字典项名称
     */
    private String name;

    /**
     * 字典项编码
     */
    private String code;

    /**
     * 字典值
     */
    private String value;

    /**
     * 备注
     */
    private String remarks;


}
