package com.easonhotel.dao.sys.vo;

import lombok.Data;

import java.util.List;

@Data
public class TreeItem {
    private String id;

    private String title;

    private String key;

    private List<TreeItem> children;
}
