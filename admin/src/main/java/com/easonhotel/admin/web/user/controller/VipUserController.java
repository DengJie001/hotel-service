package com.easonhotel.admin.web.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easonhotel.dao.web.dto.admin.UserInfoAdminQueryParam;
import com.easonhotel.dao.web.vo.admin.VipUserAdminVo;
import com.easonhotel.common.ResData;
import com.easonhotel.dao.web.entity.UserInfo;
import com.easonhotel.dao.web.service.IUserInfoService;
import com.easonhotel.dao.web.service.IUserPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员接口
 */
@RestController
@RequestMapping("/vipUser")
public class VipUserController {
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserPointService userPointService;

    /**
     * 分页查询会员信息
     * @param param
     * @return
     */
    @PostMapping("/pageList")
    public ResData<Object> pageList(@RequestBody UserInfoAdminQueryParam param) {
        Map<String, Object> resMap = userInfoService.pageList(param);
        return ResData.create(resMap);
    }
}
