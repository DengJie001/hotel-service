package com.easonhotel.dao.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.dao.sys.entity.Resource;
import com.easonhotel.dao.sys.entity.RoleResource;
import com.easonhotel.dao.sys.entity.UserRole;
import com.easonhotel.dao.sys.mapper.ResourceMapper;
import com.easonhotel.dao.sys.mapper.RoleResourceMapper;
import com.easonhotel.dao.sys.mapper.UserRoleMapper;
import com.easonhotel.dao.sys.service.IResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easonhotel.dao.sys.vo.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 资源信息 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Autowired
    private ResourceMapper resourceMapper;
    @Override
    public List<Resource> getAuthedResource(String userId) {
        UserRole userRole = userRoleMapper.selectOne(Wrappers.<UserRole>query()
                .eq("deleted", 0)
                .eq("userId", userId)
                .last("limit 0, 1"));
        List<RoleResource> roleResources = roleResourceMapper.selectList(Wrappers.<RoleResource>query()
                .eq("deleted", 0)
                .eq("roleId", userRole.getRoleId())
                .orderByAsc("sortedNum"));
        List<String> resourceIds = new ArrayList<>();
        for (RoleResource roleResource : roleResources) {
            resourceIds.add(roleResource.getResourceId());
        }
        List<Resource> resources = new ArrayList<>();
        if (resourceIds.size() == 0) {
            return resources;
        }
        resources = resourceMapper.selectList(Wrappers.<Resource>query()
                .eq("deleted", 0)
                .in("id", resourceIds)
                .orderByAsc("parentId", "sortedNum"));
        return resources;
    }

    @Override
    public List<Router> listRouter() {
        List<Resource> resources = resourceMapper.selectList(Wrappers.<Resource>query()
                .eq("deleted", 0)
                .orderByAsc("parentId", "sortedNum", "createdAt"));
        return parseRouter(resources);
    }

    @Override
    public List<Router> parseRouter(List<Resource> resources) {
        List<Router> routers = new ArrayList<>();
        for (Resource resource : resources) {
            Router router = new Router();
            router.setId(resource.getId());
            router.setName(resource.getName());
            router.setPath(resource.getPath());
            router.setIcon(resource.getIcon());
            router.setParentId(resource.getParentId());
            router.setDescription(resource.getDescription());
            router.setKey(resource.getId());
            if (StringUtils.isNotBlank(resource.getParentId())) {
                for (Router antdRouter : routers) {
                    if (antdRouter.getId().equals(resource.getParentId())) {
                        if (antdRouter.getChildren() == null) {
                            antdRouter.setChildren(new ArrayList<>());
                        }
                        antdRouter.getChildren().add(router);
                        break;
                    }
                }
            } else {
                routers.add(router);
            }
        }
        return routers;
    }
}
