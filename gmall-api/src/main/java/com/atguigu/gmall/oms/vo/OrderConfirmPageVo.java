package com.atguigu.gmall.oms.vo;

import com.atguigu.gmall.cart.bean.CartItem;
import com.atguigu.gmall.ums.entity.MemberReceiveAddress;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：mei
 * @date ：Created in 2019/4/3 0003 下午 19:12
 * @description：订单提交页详情
 * @modified By：
 * @version: $
 */
@Data
public class OrderConfirmPageVo implements Serializable {
    private List<CartItem> cartItems;
    private List<MemberReceiveAddress> memberReceiveAddresses;
    //交易令牌
    private String tradeToken;
}
