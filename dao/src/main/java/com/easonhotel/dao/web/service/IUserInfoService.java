package com.easonhotel.dao.web.service;

import com.easonhotel.dao.web.dto.admin.UserInfoAdminQueryParam;
import com.easonhotel.dao.web.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
public interface IUserInfoService extends IService<UserInfo> {
    public Map<String, Object> pageList(UserInfoAdminQueryParam param);
}
