package com.easonhotel.dao.sys.vo;

import lombok.Data;

import java.util.List;

/**
 * 后台管理系统路由权限实体
 */
@Data
public class Router {
    private String id;

    private String key;

    private String path;

    private String name;

    private String icon;

    private String parentId;

    private String description;

    private List<Router> children;
}
