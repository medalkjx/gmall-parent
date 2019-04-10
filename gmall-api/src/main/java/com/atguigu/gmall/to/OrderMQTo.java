package com.atguigu.gmall.to;

import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.oms.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：mei
 * @date ：Created in 2019/4/7 0007 下午 14:55
 * @description：订单消息发布
 * @modified By：
 * @version: $
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderMQTo implements Serializable {
    private Order order;
    private List<OrderItem> items;
}
