package com.easonhotel.dao.sys.vo;

import com.easonhotel.dao.sys.entity.User;
import lombok.Data;

@Data
public class UserWithRole extends User {
    private String roleId;

    private String roleName;
}
