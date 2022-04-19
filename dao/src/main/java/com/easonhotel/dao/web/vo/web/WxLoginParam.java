package com.easonhotel.dao.web.vo.web;

import lombok.Data;

@Data
public class WxLoginParam {
    private String code;

    private String cloudId;

    private String encryptedData;

    private String iv;

    private String rawData;

    private String signature;

    private WxUserInfo wxUserInfo;
}
