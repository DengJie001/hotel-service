package com.easonhotel.dao;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BaseEntity {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField(value = "createdAt", fill = FieldFill.INSERT)
    private Long createdAt;

    @TableField(value = "updatedAt", fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;

    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private boolean deleted;

    @TableField(value = "sortedNum", fill = FieldFill.INSERT)
    private Integer sortedNum;
}
