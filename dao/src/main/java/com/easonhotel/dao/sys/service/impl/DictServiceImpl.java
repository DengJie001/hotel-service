package com.easonhotel.dao.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.dao.sys.entity.Dict;
import com.easonhotel.dao.sys.mapper.DictMapper;
import com.easonhotel.dao.sys.service.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easonhotel.dao.sys.vo.DictTreeItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {
    @Autowired
    private DictMapper dictMapper;

    /**
     * 查询所有的字典项
     * @return
     */
    @Override
    public List<DictTreeItem> pageList() {
        List<DictTreeItem> dictTreeItems = new ArrayList<>();
        // 查询所有的父级字典项
        List<Dict> parentDictList = dictMapper.selectList(Wrappers.<Dict>query()
                .eq("deleted", 0)
                .isNull("parentCode"));
        // 封装为树形结构
        if (parentDictList != null && parentDictList.size() > 0) {
            for (Dict dict : parentDictList) {
                DictTreeItem dictTreeItem = this.parseDictToDictTreeItem(dict);
                List<Dict> currentSonDictList = dictMapper.selectList(Wrappers.<Dict>query()
                        .eq("deleted", 0)
                        .eq("parentCode", dict.getCode()));
                for (Dict sonDict : currentSonDictList) {
                    if (dictTreeItem.getChildren() == null) {
                        dictTreeItem.setChildren(new ArrayList<>());
                    }
                    dictTreeItem.getChildren().add(this.parseDictToDictTreeItem(sonDict));
                }
                dictTreeItems.add(dictTreeItem);
            }
        }
        return dictTreeItems;
    }

    @Override
    public DictTreeItem parseDictToDictTreeItem(Dict dict) {
        DictTreeItem dictTreeItem = new DictTreeItem();
        dictTreeItem.setId(dict.getId());
        dictTreeItem.setKey(dict.getId());
        dictTreeItem.setName(dict.getName());
        dictTreeItem.setParentCode(dict.getParentCode());
        dictTreeItem.setCode(dict.getCode());
        dictTreeItem.setValue(dict.getValue());
        dictTreeItem.setRemarks(dict.getRemarks());
        return dictTreeItem;
    }
}
