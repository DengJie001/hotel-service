package com.easonhotel.dao.web.vo.web;

import lombok.Data;

@Data
public class WxUserInfo {
    private String avatarUrl;

    private String city;

    private String province;

    private String country;

    private Integer gender;

    private String nickName;

    private String name;

    private String idCard;

    private String email;

    private String phoneNumber;
}
