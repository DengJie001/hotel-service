package com.easonhotel.dao.web.service.impl;

import com.easonhotel.dao.web.entity.UserComments;
import com.easonhotel.dao.web.mapper.UserCommentsMapper;
import com.easonhotel.dao.web.service.IUserCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-评价关联表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class UserCommentsServiceImpl extends ServiceImpl<UserCommentsMapper, UserComments> implements IUserCommentsService {

}
