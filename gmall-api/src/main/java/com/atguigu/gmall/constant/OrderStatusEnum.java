package com.atguigu.gmall.constant;

/**
 * @author mei
 * 订单状态
 */
public enum OrderStatusEnum {
    UNPAY(0, "待付款"), PAYED(1, "已支付，待发货"), SENDED(2, "已发货"),
    CLOSED(4, "已关闭"), FINISHED(3, "已完成"), UNVAILED(5, "无效订单");

    private Integer code;
    private String msg;


    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
