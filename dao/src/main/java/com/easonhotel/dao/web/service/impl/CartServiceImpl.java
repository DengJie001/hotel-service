package com.easonhotel.dao.web.service.impl;

import com.easonhotel.dao.web.entity.Cart;
import com.easonhotel.dao.web.mapper.CartMapper;
import com.easonhotel.dao.web.service.ICartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品购物车 服务实现类
 * </p>
 *
 * @author PengYiXing
 * @since 2022-04-07
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

}
