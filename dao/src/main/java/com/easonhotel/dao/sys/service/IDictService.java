package com.easonhotel.dao.sys.service;

import com.easonhotel.dao.sys.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.easonhotel.dao.sys.vo.DictTreeItem;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
public interface IDictService extends IService<Dict> {
    public List<DictTreeItem> pageList();

    public DictTreeItem parseDictToDictTreeItem(Dict dict);
}
