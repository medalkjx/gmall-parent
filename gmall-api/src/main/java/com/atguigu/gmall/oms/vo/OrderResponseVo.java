package com.atguigu.gmall.oms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ：mei
 * @date ：Created in 2019/4/4 0004 上午 0:06
 * @description：订单完成返回项
 * @modified By：
 * @version: $
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponseVo implements Serializable {

    private String tips;
    private Integer code;
    //订单号交给前端
    private String out_trade_no;
    //总金额
    private String total_amount;
    //主题
    private String subject;
    private String body;
}
