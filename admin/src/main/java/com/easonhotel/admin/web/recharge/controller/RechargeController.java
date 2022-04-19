package com.easonhotel.admin.web.recharge.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easonhotel.admin.web.recharge.vo.RechargeVo;
import com.easonhotel.common.ResData;
import com.easonhotel.common.utils.DateUtils;
import com.easonhotel.dao.web.entity.Recharge;
import com.easonhotel.dao.web.entity.UserInfo;
import com.easonhotel.dao.web.service.IRechargeService;
import com.easonhotel.dao.web.service.IUserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 充值记录接口
 */
@RestController
@RequestMapping("/finance/recharge")
public class RechargeController {
    @Autowired
    private IRechargeService rechargeService;

    @Autowired
    private IUserInfoService userInfoService;

    private static final String RECHARGE_STATUS_FINISHED = "RECHARGE_STATUS_FINISHED";

    /**
     * 确认用户充值
     * @param map
     * @author PengYiXing
     * @return
     */
    @Transactional
    @PostMapping("/confirm")
    public ResData<Object> confirm(@RequestBody Map<String, String> map) {
        Recharge recharge = rechargeService.getOne(Wrappers.<Recharge>query().eq("id", map.get("id")));
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>query().eq("id", recharge.getUserId()).last("LIMIT 0, 1"));
        recharge.setStatus(RECHARGE_STATUS_FINISHED);
        rechargeService.saveOrUpdate(recharge);
        userInfo.setBalance(userInfo.getBalance() + recharge.getAmount());
        userInfoService.saveOrUpdate(userInfo);
        return ResData.create(0, "操作成功");
    }

    /**
     * 在后台为用户进行充值
     * @param recharge
     * @author PengYiXing
     * @return
     */
    @PostMapping("/recharge")
    public ResData<Object> recharge(@RequestBody Recharge recharge) {
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>query()
                .eq("deleted", 0)
                .eq("id", recharge.getUserId())
                .last("LIMIT 0, 1"));
        userInfo.setBalance(userInfo.getBalance() + recharge.getAmount());
        userInfoService.updateById(userInfo);
        recharge.setStatus("RECHARGE_STATUS_FINISHED");
        rechargeService.saveOrUpdate(recharge);
        return ResData.create(0, "操作成功");
    }

    /**
     * 充值记录分页查询
     * @param map
     * @author PengYiXing
     * @return
     */
    @PostMapping("/pageList")
    public ResData<Object> pageList(@RequestBody Map<String, String> map) {
        // 参数校验
        if (!map.containsKey("page") || StringUtils.isBlank(map.get("page")) || !map.containsKey("size") || StringUtils.isBlank(map.get("size"))) {
            return ResData.create(-1, "分页参数异常");
        }
        // 分页查询
        IPage<Recharge> iPage = new Page<>(Integer.parseInt(map.get("page")), Integer.parseInt(map.get("size")));
        IPage<Recharge> paged = rechargeService.page(iPage, Wrappers.<Recharge>query()
                .eq("deleted", 0)
                .eq(map.containsKey("status") && StringUtils.isNotBlank(map.get("status")), "status", map.get("status"))
                .eq(map.containsKey("userId") && StringUtils.isNotBlank(map.get("userId")), "userId", map.get("userId"))
                .orderByDesc("createdAt", "amount"));
        // 封装数据
        List<RechargeVo> rechargeVos = new ArrayList<>();
        for (Recharge recharge : paged.getRecords()) {
            RechargeVo rechargeVo = new RechargeVo();
            BeanUtils.copyProperties(recharge, rechargeVo);
            // 格式化充值时间
            rechargeVo.setRechargeTime(DateUtils.parseDate(recharge.getCreatedAt(), "yyyy-MM-dd HH:mm"));
            rechargeVos.add(rechargeVo);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("count", paged.getTotal());
        res.put("data", rechargeVos);
        return ResData.create(res);
    }
}
