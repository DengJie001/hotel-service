package com.easonhotel.dao.web.vo.admin;

import com.easonhotel.dao.web.entity.UserInfo;
import lombok.Data;

@Data
public class VipUserAdminVo extends UserInfo {
    private String level;

    private Long point;

    private String registerTime;
}
