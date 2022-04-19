package com.easonhotel.dao.att.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 附件信息表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("att_attach")
public class Attach extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 附件原名称
     */
    @TableField("realName")
    private String realName;

    /**
     * 附件保存在服务器的名称
     */
    @TableField("saveName")
    private String saveName;

    /**
     * 附件保存路径
     */
    @TableField("savePath")
    private String savePath;

    /**
     * 附件类型
     */
    @TableField("fileType")
    private String fileType;

    /**
     * 附件大小
     */
    @TableField("fileSize")
    private Integer fileSize;


}
