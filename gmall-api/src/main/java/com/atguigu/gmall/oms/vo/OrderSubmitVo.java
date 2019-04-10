package com.atguigu.gmall.oms.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author ：mei
 * @date ：Created in 2019/4/3 0003 下午 23:59
 * @description：订单提交项
 * @modified By：
 * @version: $
 */
@Data
public class OrderSubmitVo implements Serializable {
    //用户令牌
    private String token;
    //交易令牌
    private String tradeToken;
    //支付金额
    private BigDecimal price;
    //选择的地址id
    private Long addressId;
    //订单备注
    private String comment;

}
