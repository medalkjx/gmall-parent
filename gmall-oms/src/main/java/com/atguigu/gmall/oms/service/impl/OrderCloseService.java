package com.atguigu.gmall.oms.service.impl;

import com.atguigu.gmall.constant.OrderStatusEnum;
import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.oms.mapper.OrderMapper;
import com.atguigu.gmall.to.OrderMQTo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * @author ：mei
 * @date ：Created in 2019/4/7 0007 下午 18:34
 * @description：关闭订单服务
 * @modified By：
 * @version: $
 */
@Service
public class OrderCloseService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Scheduled(cron = "0 0 3 * * ?")
    public void clearCount() {
        redisTemplate.delete("orderCountId");
    }

    @RabbitListener(queues = "deadQueue")
    public void closeOrder(OrderMQTo order, Channel channel, Message message) throws IOException {
        //TODO 未测试
        Order selectById = orderMapper.selectById(order.getOrder().getId());
        if (selectById.getStatus().equals(OrderStatusEnum.UNPAY.getCode())) {
            Order saveOrder = new Order();
            saveOrder.setId(selectById.getId());
            saveOrder.setStatus(OrderStatusEnum.CLOSED.getCode());
            orderMapper.updateById(saveOrder);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            rabbitTemplate.convertAndSend("deadExchange", "release.stock", order);
        } else {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}