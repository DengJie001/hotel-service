package com.easonhotel.dao.sys.service;

import com.easonhotel.dao.sys.entity.RoleResource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easonhotel.dao.sys.vo.RoleWithResource;
import com.easonhotel.dao.sys.vo.TreeItem;

import java.util.List;

/**
 * <p>
 * 角色资源关联信息 服务类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
public interface IRoleResourceService extends IService<RoleResource> {
    public List<TreeItem> getResources();

    public void allocate(RoleWithResource roleWithResource);
}
