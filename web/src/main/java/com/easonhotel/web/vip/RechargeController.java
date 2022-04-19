package com.easonhotel.web.vip;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.easonhotel.common.ResData;
import com.easonhotel.common.utils.DateUtils;
import com.easonhotel.dao.sys.entity.Dict;
import com.easonhotel.dao.sys.service.IDictService;
import com.easonhotel.dao.sys.service.impl.DictServiceImpl;
import com.easonhotel.dao.web.entity.Recharge;
import com.easonhotel.dao.web.service.IRechargeService;
import com.easonhotel.dao.web.service.IUserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eh/recharge")
public class RechargeController {
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IRechargeService rechargeService;

    @Autowired
    private IDictService dictService;

    private String 	RECHARGE_STATUS_TO_BE_CONFIRMED = "RECHARGE_STATUS_TO_BE_CONFIRMED";

    @PostMapping("/apply")
    @Transactional
    public ResData<Object> apply(@RequestBody Recharge recharge) {
        recharge.setStatus(RECHARGE_STATUS_TO_BE_CONFIRMED);
        rechargeService.save(recharge);
        return ResData.create(0, "申请成功");
    }

    @GetMapping("/listByUserId/{id}")
    public ResData<Object> listByUserId(@PathVariable String id) {
        List<Recharge> rechargeList = rechargeService.list(Wrappers.<Recharge>query()
                .eq("deleted", 0).eq("userId", id).orderByDesc("createdAt"));
        List<JSONObject> res = new ArrayList<>();
        for (Recharge recharge : rechargeList) {
            Dict dict = dictService.getOne(Wrappers.<Dict>query().eq("deleted", 0).eq("code", recharge.getStatus())
                    .last("LIMIT 0, 1"));
            recharge.setStatus(dict.getValue());
            JSONObject json = (JSONObject) JSONObject.toJSON(recharge);
            json.put("timestamp", DateUtils.parseDate(recharge.getCreatedAt(), "yyyy年MM月dd日 HH:mm:ss"));
            res.add(json);
        }
        return ResData.create(res);
    }
}
