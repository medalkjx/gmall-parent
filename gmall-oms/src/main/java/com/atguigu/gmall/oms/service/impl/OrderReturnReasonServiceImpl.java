package com.atguigu.gmall.oms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.oms.entity.OrderReturnReason;
import com.atguigu.gmall.oms.mapper.OrderReturnReasonMapper;
import com.atguigu.gmall.oms.service.OrderReturnReasonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 退货原因表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-04-03
 */
@Component
@Service
public class OrderReturnReasonServiceImpl extends ServiceImpl<OrderReturnReasonMapper, OrderReturnReason> implements OrderReturnReasonService {

}
