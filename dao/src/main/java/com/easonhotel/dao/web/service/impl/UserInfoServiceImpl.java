package com.easonhotel.dao.web.service.impl;

import com.easonhotel.common.utils.DateUtils;
import com.easonhotel.dao.web.dto.admin.UserInfoAdminQueryParam;
import com.easonhotel.dao.web.entity.UserInfo;
import com.easonhotel.dao.web.mapper.UserInfoMapper;
import com.easonhotel.dao.web.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easonhotel.dao.web.vo.admin.VipUserAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 分页查询会员信息
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> pageList(UserInfoAdminQueryParam param) {
        Map<String, Object> resMap = new HashMap<>();
        List<VipUserAdminVo> vipUserAdminVos = userInfoMapper.pageList(param);
        for (VipUserAdminVo vipUserAdminVo : vipUserAdminVos) {
            vipUserAdminVo.setRegisterTime(DateUtils.parseDate(vipUserAdminVo.getCreatedAt(), "yyyy-MM-dd HH:mm"));
        }
        resMap.put("data", vipUserAdminVos);
        resMap.put("count", userInfoMapper.pageListCount(param));
        return resMap;
    }
}
