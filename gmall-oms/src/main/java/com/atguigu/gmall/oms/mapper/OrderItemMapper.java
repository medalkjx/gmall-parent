package com.atguigu.gmall.oms.mapper;

import com.atguigu.gmall.oms.entity.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 订单中所包含的商品 Mapper 接口
 * </p>
 *
 * @author mei
 * @since 2019-04-03
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * TODO
     * 批量保存购物项
     */
    //void insertBatch(ArrayList<OrderItem> orderItems);
}
