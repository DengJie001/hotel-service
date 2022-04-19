package com.easonhotel.dao.sys.service;

import com.easonhotel.dao.sys.entity.Resource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easonhotel.dao.sys.vo.Router;

import java.util.List;

/**
 * <p>
 * 资源信息 服务类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
public interface IResourceService extends IService<Resource> {
    public List<Resource> getAuthedResource(String userId);

    public List<Router> listRouter();

    public List<Router> parseRouter(List<Resource> resources);
}
