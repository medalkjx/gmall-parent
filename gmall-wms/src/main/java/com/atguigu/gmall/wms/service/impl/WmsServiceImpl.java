package com.atguigu.gmall.wms.service.impl;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.oms.entity.OrderItem;
import com.atguigu.gmall.pms.service.SkuStockService;
import com.atguigu.gmall.to.OrderMQTo;
import com.atguigu.gmall.wms.service.WmsService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
import java.util.List;

/**
 * @author ：mei
 * @date ：Created in 2019/4/7 0007 下午 18:49
 * @description：库存服务
 * @modified By：
 * @version: $
 */
@Service
public class WmsServiceImpl implements WmsService {

    @Reference
    SkuStockService skuStockService;

    @RabbitListener(queues = "stockQueue")
    public void lockStock(OrderMQTo orderMQTo, Channel channel, Message message) throws IOException {
        try {
            List<OrderItem> items = orderMQTo.getItems();
            items.forEach(item -> {
                Long skuId = item.getProductSkuId();
                Integer quantity = item.getProductQuantity();
                skuStockService.updateStock(skuId, quantity);
                try {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                } catch (IOException e) {
                }
            });
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);

        }
    }

    @RabbitListener(queues = "releaseStockQueue")
    public void releaseStock(OrderMQTo orderMQTo, Channel channel, Message message) throws IOException {
        try {
            List<OrderItem> items = orderMQTo.getItems();
            items.forEach(item -> {
                Long skuId = item.getProductSkuId();
                Integer quantity = item.getProductQuantity();
                skuStockService.releaseStock(skuId, quantity);
            });
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}
