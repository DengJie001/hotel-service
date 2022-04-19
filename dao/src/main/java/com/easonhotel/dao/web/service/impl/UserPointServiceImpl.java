package com.easonhotel.dao.web.service.impl;

import com.easonhotel.dao.web.entity.UserPoint;
import com.easonhotel.dao.web.mapper.UserPointMapper;
import com.easonhotel.dao.web.service.IUserPointService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员积分表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-28
 */
@Service
public class UserPointServiceImpl extends ServiceImpl<UserPointMapper, UserPoint> implements IUserPointService {

}
