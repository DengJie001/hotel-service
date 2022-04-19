package com.easonhotel.admin.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.sys.entity.Dict;
import com.easonhotel.dao.sys.service.IDictService;
import com.easonhotel.dao.sys.vo.DictTreeItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {
    @Autowired
    private IDictService dictService;

    /**
     * 新增或修改字典项
     * @param dict
     * @author PengYiXing
     * @return
     */
    @PostMapping("/modify")
    public ResData<Object> modify(@RequestBody Dict dict) {
        // 验证同级下是否有同名字典项
        if (StringUtils.isBlank(dict.getParentCode())) {
            dict.setParentCode(null);
        }
        Dict existSameNameDict = dictService.getOne(Wrappers.<Dict>query()
                .eq("deleted", 0)
                .eq("parentCode", dict.getParentCode())
                .last("LIMIT 0, 1"));
        if (existSameNameDict != null && dict.getName().equals(existSameNameDict.getName()) && !dict.getId().equals(existSameNameDict.getId())) {
            return ResData.create(-1, "同级字典项下已有同名字典项");
        }
        // 新增或修改
        dictService.saveOrUpdate(dict);
        return ResData.create(0, "操作成功");
    }

    /**
     * 删除字典项
     * @param id 字典项ID
     * @author PengYiXing
     * @return
     */
    @GetMapping("/remove")
    public ResData<Object> remove(@RequestParam String id) {
        // 参数校验
        if (StringUtils.isBlank(id)) {
            return ResData.create(-1, "ID不能为空");
        }
        // 如果是父级字典项不允许直接删除
        Dict one = dictService.getOne(Wrappers.<Dict>query()
                .eq("deleted", 0)
                .eq("id", id)
                .last("LIMIT 0, 1"));
        if (one != null && StringUtils.isBlank(one.getParentCode())) {
            List<Dict> sonDictList = dictService.list(Wrappers.<Dict>query()
                    .eq("deleted", 0)
                    .eq("parentCode", one.getCode()));
            if (sonDictList != null && sonDictList.size() > 0) {
                return ResData.create(-1, "请先删除子字典项");
            }
        }
        // 删除字典项
        dictService.update(Wrappers.<Dict>update()
                .eq("id", id)
                .set("deleted", true));
        return ResData.create(0, "操作成功");
    }

    /**
     * 查询所有的字典项
     * @author PengYiXing
     * @return
     */
    @GetMapping("/pageList")
    public ResData<Object> pageList() {
        List<DictTreeItem> dictTreeItems = dictService.pageList();
        return ResData.create(dictTreeItems);
    }

    /**
     * 查询所有可以作为父级字典的字典项
     * @author PengYiXing
     * @return
     */
    @GetMapping("/listAllParentDict")
    public ResData<Object> listAllParentDict() {
        List<Dict> allParentDict = dictService.list(Wrappers.<Dict>query()
                .eq("deleted", 0)
                .isNull("parentCode"));
        return ResData.create(allParentDict);
    }

    /**
     * 根据父字典的code，查询该父字典下的其他字典项
     * @param code 父级字典的code
     * @author PengYiXing
     * @return
     */
    @GetMapping("/listByParentCode")
    public ResData<Object> listByParentCode(@RequestParam String code) {
        if (StringUtils.isBlank(code)) {
            return ResData.create(new ArrayList<>());
        }
        List<Dict> dictList = dictService.list(Wrappers.<Dict>query()
                .eq("deleted", 0)
                .eq("parentCode", code));
        return ResData.create(dictList);
    }
}
