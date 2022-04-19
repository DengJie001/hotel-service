package com.easonhotel.dao.sys.service.impl;

import com.easonhotel.dao.sys.entity.Role;
import com.easonhotel.dao.sys.mapper.RoleMapper;
import com.easonhotel.dao.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色信息 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
