package com.atguigu.gmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.atguigu.gmall.constant.OrderStatusEnum;
import com.atguigu.gmall.oms.service.OrderService;
import com.atguigu.gmall.oms.vo.OrderResponseVo;
import com.atguigu.gmall.portal.config.AlipayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/4/7 0007 下午 17:27
 * @description：支付服务
 * @modified By：
 * @version: $
 */
@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {

    @Reference
    OrderService orderService;

    @ResponseBody
    @RequestMapping("/order")
    public String payOrder(OrderResponseVo orderResponseVo) {
        String payHtml = orderService.payOrder(orderResponseVo.getOut_trade_no(), orderResponseVo.getTotal_amount(), orderResponseVo.getSubject(), orderResponseVo.getBody());
        return payHtml;
    }

    /**
     * 同步通知处理
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/async/success")
    public String paySucccess(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = true;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset,
                    AlipayConfig.sign_type);
            System.out.println("验签：" + signVerified);

        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
        }
        // 商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        // 支付宝流水号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
        // 交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

        if (trade_status.equals("TRADE_FINISHED")) {
            //改订单状态
            orderService.updateOrderStatus(out_trade_no, OrderStatusEnum.FINISHED);
            //TODO 保存流水日志


        } else if (trade_status.equals("TRADE_SUCCESS")) {
            orderService.updateOrderStatus(out_trade_no, OrderStatusEnum.PAYED);
        }

        return "success";
    }

}
