package com.atguigu.gmall.oms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.oms.entity.OrderSetting;
import com.atguigu.gmall.oms.mapper.OrderSettingMapper;
import com.atguigu.gmall.oms.service.OrderSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单设置表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-04-03
 */
@Service
@Component
public class OrderSettingServiceImpl extends ServiceImpl<OrderSettingMapper, OrderSetting> implements OrderSettingService {

}
