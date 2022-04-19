package com.easonhotel.dao.web.mapper;

import com.easonhotel.dao.web.dto.admin.UserInfoAdminQueryParam;
import com.easonhotel.dao.web.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easonhotel.dao.web.vo.admin.VipUserAdminVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    public List<VipUserAdminVo> pageList(@Param("param") UserInfoAdminQueryParam param);

    public Integer pageListCount(@Param("param") UserInfoAdminQueryParam param);
}
