package com.easonhotel.dao.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.dao.sys.entity.Resource;
import com.easonhotel.dao.sys.entity.Role;
import com.easonhotel.dao.sys.entity.RoleResource;
import com.easonhotel.dao.sys.mapper.ResourceMapper;
import com.easonhotel.dao.sys.mapper.RoleResourceMapper;
import com.easonhotel.dao.sys.service.IRoleResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easonhotel.dao.sys.vo.RoleWithResource;
import com.easonhotel.dao.sys.vo.TreeItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色资源关联信息 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements IRoleResourceService {
    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    /**
     * 查询所有的资源信息并封装为树形结构
     * @return
     */
    @Override
    public List<TreeItem> getResources() {
        List<Resource> resources = resourceMapper.selectList(Wrappers.<Resource>query()
                .eq("deleted", 0)
                .orderByAsc("parentId", "createdAt"));
        List<TreeItem> data = new ArrayList<>();
        for (Resource resource : resources) {
            TreeItem treeItem = new TreeItem();
            treeItem.setId(resource.getId());
            treeItem.setTitle(resource.getName());
            treeItem.setKey(resource.getId());
            if (StringUtils.isNotBlank(resource.getParentId())) {
                for (TreeItem item : data) {
                    if (item.getId().equals(resource.getParentId())) {
                        if (item.getChildren() == null) {
                            item.setChildren(new ArrayList<>());
                        }
                        item.getChildren().add(treeItem);
                        break;
                    }
                }
            } else {
                data.add(treeItem);
            }
        }
        return data;
    }

    /**
     * 给角色分配资源
     */
    @Override
    public void allocate(RoleWithResource roleWithResource) {
        roleResourceMapper.delete(Wrappers.<RoleResource>query().eq("roleId", roleWithResource.getRoleId()));
        for (String resourceId : roleWithResource.getResourceIds()) {
            RoleResource roleResource = new RoleResource();
            roleResource.setRoleId(roleWithResource.getRoleId());
            roleResource.setResourceId(resourceId);
            roleResourceMapper.insert(roleResource);
        }
    }
}
