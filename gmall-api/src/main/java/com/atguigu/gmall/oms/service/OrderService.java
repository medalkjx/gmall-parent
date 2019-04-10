package com.atguigu.gmall.oms.service;

import com.atguigu.gmall.constant.OrderStatusEnum;
import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.oms.vo.OrderConfirmPageVo;
import com.atguigu.gmall.oms.vo.OrderResponseVo;
import com.atguigu.gmall.oms.vo.OrderSubmitVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author mei
 * @since 2019-04-03
 */
public interface OrderService extends IService<Order> {

    /**
     * 获取订单结算页信息
     *
     * @param token
     * @return
     */
    OrderConfirmPageVo createOrderInfo(String token);

    /**
     * 创建订单
     *
     * @param orderSubmitVo
     * @return
     */
    OrderResponseVo createOrder(OrderSubmitVo orderSubmitVo);

    /**
     * 跳转支付
     *
     * @param out_trade_no
     * @param total_amount
     * @param subject
     * @param body
     * @return
     */
    String payOrder(String out_trade_no, String total_amount, String subject, String body);

    /**
     * 修改订单状态
     *
     * @param out_trade_no
     * @param payed
     */
    void updateOrderStatus(String out_trade_no, OrderStatusEnum payed);
}
