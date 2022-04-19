package com.easonhotel.dao.sys.service.impl;

import com.easonhotel.dao.sys.entity.UserRole;
import com.easonhotel.dao.sys.mapper.UserRoleMapper;
import com.easonhotel.dao.sys.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-角色关联表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
