package com.easonhotel.dao.sys.service;

import com.easonhotel.dao.sys.entity.Role;
import com.easonhotel.dao.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easonhotel.dao.sys.vo.UserWithRole;

import java.util.Map;

/**
 * <p>
 * 管理员用户信息 服务类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
public interface IUserService extends IService<User> {
    public Map<String, Object> currentLoginUser(String loginName);

    public void modify(UserWithRole userWithRole);

    public Map<String, Object> pageList(Map<String, String> map);

    public UserWithRole parseUserWithRole(User user, Role role);
}
