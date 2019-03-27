package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.service.CouponHistoryService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/3/25 0025 下午 18:53
 * @description：优惠券领取记录管理
 * @modified By：
 * @version: $
 */
@Api(description = "优惠券领取记录管理")
@CrossOrigin
@RestController
@RequestMapping("/couponHistory")
public class SmsCouponHistoryController {

    @Reference
    CouponHistoryService couponHistoryService;

    @ApiOperation("根据优惠券id，使用状态，订单编号分页获取领取记录")
    @GetMapping("/list")
    public Object list(@RequestParam(value = "couponId", required = false) Long couponId,
                       @RequestParam(value = "useStatus", required = false) Integer useStatus,
                       @RequestParam(value = "orderSn", required = false) String orderSn,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        Map<String, Object> pageInfo = couponHistoryService.pageCouponHistory(couponId, useStatus, orderSn, pageNum, pageSize);
        return new CommonResult().success(pageInfo);
    }

}
