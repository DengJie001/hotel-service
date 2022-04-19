package com.easonhotel.admin.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.sys.entity.Resource;
import com.easonhotel.dao.sys.entity.RoleResource;
import com.easonhotel.dao.sys.service.IResourceService;
import com.easonhotel.dao.sys.service.IRoleResourceService;
import com.easonhotel.dao.sys.vo.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 资源/权限管理接口
 */
@RestController
@RequestMapping("/sys/resource")
public class ResourceController {
    @Autowired
    private IResourceService resourceService;

    @Autowired
    private IRoleResourceService roleResourceService;

    /**
     * 新增或修改资源信息
     * @author PengYiXing
     * @return
     */
    @PostMapping("/modify")
    public ResData<Object> modify(@RequestBody @Validated Resource resource) {
        // 验证该资源的父级资源不是本身
        if (StringUtils.isNotBlank(resource.getId()) && resource.getId().equals(resource.getParentId())) {
            return ResData.create(-1, "父级资源不能是本身");
        }
        // 同一父级资源下不允许同名子资源
        Resource one = resourceService.getOne(Wrappers.<Resource>query()
                .eq("deleted", 0)
                .isNull(StringUtils.isBlank(resource.getParentId()), "parentId")
                .eq(StringUtils.isNotBlank(resource.getParentId()), "parentId", resource.getParentId())
                .eq("path", resource.getPath())
                .eq("name", resource.getName())
                .last("LIMIT 0, 1"));
        if (one != null && !one.getId().equals(resource.getId())) {
            return ResData.create(-1, "已存在相同资源");
        }
        resourceService.saveOrUpdate(resource);
        return ResData.create(0, "操作成功");
    }

    /**
     * 删除资源信息
     * @author PengYiXing
     */
    @PostMapping("/remove")
    public ResData<Object> remove(@RequestBody Map<String, String> map) {
        // 参数校验
        if (!map.containsKey("id") || StringUtils.isBlank(map.get("id"))) {
            return ResData.create(-1, "ID不能为空");
        }
        // 规则校验：被删除的资源下面是否还存在未删除的子资源
        List<Resource> existSonResourceList = resourceService.list(Wrappers.<Resource>query()
                .eq("deleted", 0)
                .eq("parentId", map.get("id")));
        if (existSonResourceList.size() > 0) {
            return ResData.create(-1, "请先删除该资源下的子资源");
        }
        // 规则校验：是否还有角色需要该资源
        List<RoleResource> existRoleNeedThisResource = roleResourceService.list(Wrappers.<RoleResource>query()
                .eq("deleted", 0)
                .eq("resourceId", map.get("id")));
        if (existRoleNeedThisResource.size() > 0) {
            return ResData.create(-1, "还有角色正在使用该资源");
        }
        // 执行删除操作
        resourceService.update(Wrappers.<Resource>update()
                .eq("deleted", 0)
                .eq("id", map.get("id"))
                .set("deleted", true));
        return ResData.create(0, "操作成功");
    }

    /**
     * 按照父子关系查询资源信息
     * @author PengYiXing
     * @return
     */
    @PostMapping("/list")
    public ResData<Object> list() {
        List<Router> routers = resourceService.listRouter();
        return ResData.create(routers);
    }

    /**
     * 查询所有的父级资源
     * @author PengYiXing
     * @return
     */
    @PostMapping("/listAllParent")
    public ResData<List<Resource>> listAllParent() {
        List<Resource> allParentResource = resourceService.list(Wrappers.<Resource>query()
                .eq("deleted", 0)
                .isNull("parentId")
                .or()
                .eq("parentId", "")
                .orderByDesc("sortedNum", "createdAt"));
        return ResData.create(allParentResource);
    }
}
