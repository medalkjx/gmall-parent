package com.atguigu.gmall.oms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.oms.entity.OrderOperateHistory;
import com.atguigu.gmall.oms.mapper.OrderOperateHistoryMapper;
import com.atguigu.gmall.oms.service.OrderOperateHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单操作历史记录 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-04-03
 */
@Service
@Component
public class OrderOperateHistoryServiceImpl extends ServiceImpl<OrderOperateHistoryMapper, OrderOperateHistory> implements OrderOperateHistoryService {

}
