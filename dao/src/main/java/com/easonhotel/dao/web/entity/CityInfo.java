package com.easonhotel.dao.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.easonhotel.dao.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 城市信息表
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("web_city_info")
public class CityInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 城市名称
     */
    @NotBlank(message = "城市名称不能为空")
    private String name;

    /**
     * 城市拼音
     */
    @NotBlank(message = "城市拼音不能为空")
    private String phonics;

    /**
     * 父级城市ID
     */
    @TableField("parentCityId")
    private String parentCityId;


}
