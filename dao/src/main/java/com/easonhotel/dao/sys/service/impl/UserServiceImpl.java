package com.easonhotel.dao.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easonhotel.common.utils.PasswordUtil;
import com.easonhotel.dao.sys.entity.*;
import com.easonhotel.dao.sys.mapper.*;
import com.easonhotel.dao.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easonhotel.dao.sys.vo.UserWithRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员用户信息 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 获取当前登录的用户信息：基本信息，路由权限信息
     * 由用户-角色-权限的依赖关系需要先查用户信息，再查用户角色信息，再查角色资源信息
     * @param loginName 用户登录名
     * @return
     */
    @Override
    public Map<String, Object> currentLoginUser(String loginName) {
        Map<String, Object> resMap = new HashMap<>();
        // 用户信息
        User loginUser = userMapper.selectOne(Wrappers.<User>query()
                .eq("deleted", 0)
                .eq("loginName", loginName)
                .last("LIMIT 0, 1"));
        // 避免NullPointerException
        if (loginUser == null) {
            return resMap;
        }
        resMap.put("id", loginUser.getId());
        resMap.put("name", loginUser.getName());
        resMap.put("loginName", loginUser.getLoginName());
        resMap.put("avatar", loginUser.getAvatar());
        // 用户角色信息
        UserRole userRole = userRoleMapper.selectOne(Wrappers.<UserRole>query()
                .eq("deleted", 0)
                .eq("userId", loginUser.getId())
                .last("LIMIT 0, 1"));
        // 避免NullPointException
        if (userRole == null) {
            return new HashMap<>();
        }
        // 角色资源信息
        List<RoleResource> roleResources = roleResourceMapper.selectList(Wrappers.<RoleResource>query()
                .eq("deleted", 0)
                .eq("roleId", userRole.getRoleId()));
        // 避免NullPointerException
        if (roleResources == null || roleResources.size() == 0) {
            return new HashMap<>();
        }
        List<String> resourceIds = new ArrayList<>();
        for (RoleResource roleResource : roleResources) {
            resourceIds.add(roleResource.getResourceId());
        }
        List<Resource> resources = resourceMapper.selectList(Wrappers.<Resource>query()
                .eq("deleted", 0)
                .in("id", resourceIds));
        // 避免NullPointerException
        if (resources == null || resources.size() == 0) {
            return new HashMap<>();
        }
        // 封装用户可操作的资源路径
        List<String> canVisit = new ArrayList<>();
        for (Resource resource : resources) {
            canVisit.add(resource.getPath());
        }
        resMap.put("canVisit", canVisit);
        resMap.put("roleId", userRole.getRoleId());
        return resMap;
    }

    /**
     * 新增或修改管理员信息
     * @param userWithRole
     */
    @Override
    public void modify(UserWithRole userWithRole) {
        User user = userMapper.selectOne(Wrappers.<User>query()
                .eq("deleted", 0)
                .eq("loginName", userWithRole.getLoginName()));
        if (user == null) {
            user = new User();
        }
        if (StringUtils.isNotBlank(userWithRole.getName())) {
            user.setName(userWithRole.getName());
        }
        user.setLoginName(userWithRole.getLoginName());
        if (StringUtils.isNotBlank(userWithRole.getPassword())) {
            user.setPassword(PasswordUtil.encrypt(userWithRole.getPassword()));
        }
        if (StringUtils.isNotBlank(userWithRole.getAvatar())) {
            user.setAvatar(userWithRole.getAvatar());
        }
        if (StringUtils.isNotBlank(userWithRole.getPhoneNum())) {
            user.setPhoneNum(userWithRole.getPhoneNum());
        }
        if (StringUtils.isNotBlank(userWithRole.getEmail())) {
            user.setEmail(userWithRole.getEmail());
        }
        user.setSortedNum(userWithRole.getSortedNum());
        this.saveOrUpdate(user);
        userRoleMapper.delete(Wrappers.<UserRole>query().eq("userId", userWithRole.getId()));
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(userWithRole.getRoleId());
        userRoleMapper.insert(userRole);

    }

    /**
     * 分页查询用户信息
     * @param map
     * @return
     */
    @Override
    public Map<String, Object> pageList(Map<String, String> map) {
        Integer page = 1;
        Integer size = 10;
        Map<String, Object> resMap = new HashMap<>();
        try {
            page = Integer.parseInt(map.get("page"));
            size = Integer.parseInt(map.get("size"));
        } catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
            resMap.put("error", true);
            return resMap;
        }
        // 分页对象
        IPage<User> iPage = new Page<>(page, size);
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("deleted", 0).orderByDesc("createdAt", "sortedNum")
                .like(map.containsKey("name") && StringUtils.isNotBlank(map.get("name")), "name", map.get("name"))
                .like(map.containsKey("loginName") && StringUtils.isNotBlank(map.get("loginName")), "loginName", map.get("loginName"));
        IPage<User> paged = userMapper.selectPage(iPage, queryWrapper);
        List<User> users = paged.getRecords();
        List<UserWithRole> userWithRoles = new ArrayList<>();
        for (User user : users) {
            UserRole userRole = userRoleMapper.selectOne(Wrappers.<UserRole>query()
                    .eq("deleted", 0)
                    .eq("userId", user.getId())
                    .last("LIMIT 0, 1"));
            Role role = roleMapper.selectOne(Wrappers.<Role>query()
                    .eq("deleted", 0)
                    .eq("id", userRole.getRoleId())
                    .last("LIMIT 0, 1"));
            userWithRoles.add(parseUserWithRole(user, role));
        }
        resMap.put("data", userWithRoles);
        resMap.put("count", paged.getTotal());
        return resMap;
    }

    @Override
    public UserWithRole parseUserWithRole(User user, Role role) {
        UserWithRole userWithRole = new UserWithRole();
        userWithRole.setId(user.getId());
        userWithRole.setName(user.getName());
        userWithRole.setLoginName(user.getLoginName());
        userWithRole.setPassword(null);
        userWithRole.setAvatar(user.getAvatar());
        userWithRole.setDeleted(user.isDeleted());
        userWithRole.setCreatedAt(user.getCreatedAt());
        userWithRole.setUpdatedAt(user.getUpdatedAt());
        userWithRole.setSortedNum(user.getSortedNum());
        userWithRole.setRoleId(role.getId());
        userWithRole.setRoleName(role.getName());
        userWithRole.setPhoneNum(user.getPhoneNum());
        userWithRole.setEmail(user.getEmail());
        return userWithRole;
    }
}
