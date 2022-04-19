package com.easonhotel.admin.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.sys.entity.User;
import com.easonhotel.dao.sys.entity.UserRole;
import com.easonhotel.dao.sys.service.IUserRoleService;
import com.easonhotel.dao.sys.service.IUserService;
import com.easonhotel.dao.sys.vo.UserWithRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    /**
     * 获取当前登录的用户信息
     * 目的：验证登录状态
     * @author PengYiXing
     */
    @PostMapping("/currentLoginUser")
    public ResData<Map<String, Object>> currentLoginUser() {
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResData.create(userService.currentLoginUser(loginName));
    }

    /**
     * 新增或修改管理员用户基本信息
     * @param userWithRole
     * @author PengYiXing
     * @return
     */
    @PostMapping("/modify")
//    @PreAuthorize("hasAnyAuthority('/sys/resource', '/sys/role', '/sys/user')")
    public ResData<Object> modify(@RequestBody UserWithRole userWithRole) {
        // 密码正则表达式
        String reg = "^[a-zA-Z0-9]{6,16}$";
        // 如果有密码的话需要对密码进行验证
        if (StringUtils.isNotBlank(userWithRole.getPassword()) && !userWithRole.getPassword().matches(reg)) {
            return ResData.create(-1, "密码格式错误");
        }
        // 登录名必须唯一
        User loginUser = userService.getOne(Wrappers.<User>query()
                .eq("deleted", 0)
                .eq("loginName", userWithRole.getLoginName())
                .last("LIMIT 0, 1"));
        if (loginUser != null && !loginUser.getId().equals(userWithRole.getId())) {
            return ResData.create(-1, "登录名已存在");
        }
        userService.modify(userWithRole);
        return ResData.create(0,"操作成功");
    }

    /**
     * 根据id删除用户信息
     * @param map
     * @return
     */
    @PostMapping("/remove")
//    @PreAuthorize("hasAnyAuthority('/sys/resource', '/sys/role', '/sys/user')")
    public ResData<Object> remove(@RequestBody Map<String, String> map) {
        // 参数合法性校验
        if (!map.containsKey("id") || StringUtils.isBlank(map.get("id"))) {
            return ResData.create(-1, "id不能为空");
        }
        // 删除用户信息
        userService.update(Wrappers.<User>update()
                .eq("deleted", 0)
                .eq("id", map.get("id"))
                .set("deleted", true));
        // 删除用户-角色关联信息
        userRoleService.update(Wrappers.<UserRole>update()
                .eq("deleted", 0)
                .eq("userId", map.get("id"))
                .set("deleted", true));
        return ResData.create(0, "操作成功");
    }

    /**
     * 分页查询管理员用户信息
     * @param map 其中包含当前页号（page），以及每页数据量（size）
     * @return
     */
    @PostMapping("/pageList")
//    @PreAuthorize("hasAnyAuthority('/sys/resource', '/sys/role', '/sys/user')")
    public ResData<Map<String, Object>> pageList(@RequestBody Map<String, String> map) {
        // 必选参数校验
        if (!map.containsKey("page") || StringUtils.isBlank(map.get("page")) || !map.containsKey("size") || StringUtils.isBlank(map.get("size"))) {
            return ResData.create(-1, "分页参数异常");
        }
        // 查询
        Map<String, Object> resMap = userService.pageList(map);
        return ResData.create(resMap);
    }
}
