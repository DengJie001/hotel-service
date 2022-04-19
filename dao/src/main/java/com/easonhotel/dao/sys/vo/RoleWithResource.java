package com.easonhotel.dao.sys.vo;

import javafx.beans.NamedArg;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RoleWithResource {
    @NotBlank(message = "角色ID不能为空")
    private String roleId;

    private String[] resourceIds;
}
