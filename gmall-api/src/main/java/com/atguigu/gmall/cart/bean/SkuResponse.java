package com.atguigu.gmall.cart.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ：mei
 * @date ：Created in 2019/4/2 0002 上午 0:26
 * @description：
 * @modified By：
 * @version: $
 */
@Data
public class SkuResponse implements Serializable {
    private CartItem item;
    private String cartKey;//购物车的key

}
