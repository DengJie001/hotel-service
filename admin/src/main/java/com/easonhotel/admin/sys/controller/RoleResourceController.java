package com.easonhotel.admin.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.sys.entity.RoleResource;
import com.easonhotel.dao.sys.service.IResourceService;
import com.easonhotel.dao.sys.service.IRoleResourceService;
import com.easonhotel.dao.sys.vo.RoleWithResource;
import com.easonhotel.dao.sys.vo.TreeItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色-资源对应接口
 */
@RestController
@RequestMapping("/sys/roleResource")
public class RoleResourceController {
    @Autowired
    private IRoleResourceService roleResourceService;

    @Autowired
    private IResourceService resourceService;

    /**
     * 给角色分配资源信息
     * @param roleWithResource
     * @author PengYiXing
     * @return
     */
    @PostMapping("/allocate")
    public ResData<Object> allocate(@RequestBody @Validated RoleWithResource roleWithResource) {
        roleResourceService.allocate(roleWithResource);
        return ResData.create(0, "操作成功");
    }

    @PostMapping("/resources")
    public ResData<List<TreeItem>> resources() {
        List<TreeItem> resources = roleResourceService.getResources();
        return ResData.create(resources);
    }

    @PostMapping("/listAllCheckedResource")
    public ResData<List<String>> listAllCheckedResource(@RequestBody Map<String, String> map) {
        List<RoleResource> roleResourceList = roleResourceService.list(Wrappers.<RoleResource>query().eq("deleted", 0).eq("roleId", map.get("id")));
        List<String> allCheckedResource = new ArrayList<>();
        for (RoleResource roleResource : roleResourceList) {
            allCheckedResource.add(roleResource.getResourceId());
        }
        return ResData.create(allCheckedResource);
    }
}
