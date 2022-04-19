package com.easonhotel.web.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.sys.entity.Dict;
import com.easonhotel.dao.sys.service.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/eh/sys/dict")
public class DictController {
    @Autowired
    private IDictService dictService;

    @GetMapping("/listSonDictByParentCode")
    public ResData<Object> listSonDictByParentCode(@RequestParam String parentCode) {
        List<Dict> list = dictService.list(Wrappers.<Dict>query()
                .eq("deleted", 0)
                .eq("parentCode", parentCode));
        return ResData.create(list);
    }
}
