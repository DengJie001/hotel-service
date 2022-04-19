package com.easonhotel.dao.web.dto.admin;

import lombok.Data;

@Data
public class UserInfoAdminQueryParam {
    private Integer page = 1;

    private Integer size = 10;

    private String phoneNumber;

    private String realName;

    private String level;
}
