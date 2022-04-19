package com.easonhotel.admin.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.sys.entity.Role;
import com.easonhotel.dao.sys.entity.RoleResource;
import com.easonhotel.dao.sys.entity.UserRole;
import com.easonhotel.dao.sys.service.IRoleResourceService;
import com.easonhotel.dao.sys.service.IRoleService;
import com.easonhotel.dao.sys.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 角色接口
 */
@RestController
@RequestMapping("/sys/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleResourceService roleResourceService;

    /**
     * 新建或修改角色信息
     * @author PengYiXing
     * @param role
     */
    @PostMapping("/modify")
    public ResData<Object> modify(@RequestBody @Validated Role role) {
        Role one = roleService.getOne(Wrappers.<Role>query().eq("deleted", 0).eq("name", role.getName()).last("LIMIT 0, 1"));
        if (one != null && !one.getId().equals(role.getId())) {
            return ResData.create(-1, "存在同名角色");
        }
        roleService.saveOrUpdate(role);
        return ResData.create(0, "操作成功");
    }

    /**
     * 删除角色信息
     * @param map
     * @author PengYiXing
     * @return
     */
    @PostMapping("/remove")
    public ResData<Object> remove(@RequestBody Map<String, String> map) {
        // 参数校验
        if (!map.containsKey("id") || StringUtils.isBlank(map.get("id"))) {
            return ResData.create(-1, "ID不能为空");
        }
        // 删除角色信息之前要确保没有管理员属于该角色
        List<UserRole> userRoleList = userRoleService.list(Wrappers.<UserRole>query()
                .eq("deleted", 0)
                .eq("roleId", map.get("id")));
        if (userRoleList.size() > 0) {
            return ResData.create(-1, "请先删除该角色对应的管理员");
        }
        // 删除角色信息
        roleService.update(Wrappers.<Role>update().eq("id", map.get("id")).set("deleted", true));
        // 删除角色-资源对应信息
        roleResourceService.update(Wrappers.<RoleResource>update().eq("roleId", map.get("id")).set("deleted", true));
        return ResData.create(0, "操作成功");
    }

    /**
     * 分页查询角色信息
     * @param map
     * @author PengYiXing
     * @return
     */
    @PostMapping("/pageList")
    public ResData<List<Role>> pageList(@RequestBody Map<String, String> map) {
        List<Role> roles = roleService.list(Wrappers.<Role>query()
                .eq("deleted", 0)
                .like(map.containsKey("name") && StringUtils.isNotBlank(map.get("name")), "name", map.get("name"))
                .orderByDesc("sortedNum", "createdAt", "name"));
        return ResData.create(roles);
    }

    /**
     * 查询所有角色信息，下拉列表使用
     * @author PengYiXing
     * @return
     */
    @PostMapping("/listAllRoles")
    public ResData<List<Role>> listAllRoles() {
        List<Role> roles = roleService.list(Wrappers.<Role>query().eq("deleted", 0).orderByDesc("sortedNum", "createdAt"));
        return ResData.create(roles);
    }
}
