package com.easonhotel.dao.web.service.impl;

import com.easonhotel.dao.web.entity.HotelComments;
import com.easonhotel.dao.web.mapper.HotelCommentsMapper;
import com.easonhotel.dao.web.service.IHotelCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 酒店评价表 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-03-12
 */
@Service
public class HotelCommentsServiceImpl extends ServiceImpl<HotelCommentsMapper, HotelComments> implements IHotelCommentsService {

}
