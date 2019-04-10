package com.atguigu.gmall.oms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.oms.entity.OrderItem;
import com.atguigu.gmall.oms.mapper.OrderItemMapper;
import com.atguigu.gmall.oms.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单中所包含的商品 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-04-03
 */
@Service
@Component
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
