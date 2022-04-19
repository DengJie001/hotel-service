package com.easonhotel.dao.sys.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DictTreeItem {
    private String id;

    private String key;

    private String parentCode;

    private String name;

    private String code;

    private String value;

    private String remarks;

    private List<DictTreeItem> children = null;
}
