package com.atguigu.gmall.oms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.oms.entity.OrderReturnApply;
import com.atguigu.gmall.oms.mapper.OrderReturnApplyMapper;
import com.atguigu.gmall.oms.service.OrderReturnApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单退货申请 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-04-03
 */
@Component
@Service
public class OrderReturnApplyServiceImpl extends ServiceImpl<OrderReturnApplyMapper, OrderReturnApply> implements OrderReturnApplyService {

}
