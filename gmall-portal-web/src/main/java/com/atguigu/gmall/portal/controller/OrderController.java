package com.atguigu.gmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.oms.service.OrderService;
import com.atguigu.gmall.oms.vo.OrderConfirmPageVo;
import com.atguigu.gmall.oms.vo.OrderResponseVo;
import com.atguigu.gmall.oms.vo.OrderSubmitVo;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ：mei
 * @date ：Created in 2019/4/3 0003 上午 11:48
 * @description：订单服务
 * @modified By：
 * @version: $
 */
@Api(description = "订单服务")
@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference
    OrderService orderService;

    @ResponseBody
    @PostMapping("/action")
    public Object cartAction(@RequestParam String token) {
        //去结算确认页
        OrderConfirmPageVo orderConfirmPageVo = orderService.createOrderInfo(token);
        return new CommonResult().success(orderConfirmPageVo);
    }

    @ResponseBody
    @PostMapping("/submit")
    public Object payOrder(OrderSubmitVo orderSubmitVo) {
        //创建订单
        OrderResponseVo orderResponseVo = orderService.createOrder(orderSubmitVo);
        //交易token
        return new CommonResult().success(orderResponseVo);
    }

}
