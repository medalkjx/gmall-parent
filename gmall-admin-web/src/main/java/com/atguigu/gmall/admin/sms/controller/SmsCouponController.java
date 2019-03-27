package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.service.CouponService;
import com.atguigu.gmall.sms.vo.CouponRelationParam;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/3/25 0025 下午 15:17
 * @description：
 * @modified By：
 * @version: $
 */
@Api(description = "优惠管理")
@CrossOrigin
@RestController
@RequestMapping("/coupon")
public class SmsCouponController {
    @Reference
    CouponService couponService;

    @ApiOperation("根据优惠券名称和类型分页获取优惠券列表")
    @GetMapping("/list")
    public Object list(@RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Map<String, Object> pageCoupon = couponService.pageCoupon(name, type, pageNum, pageSize);
        return new CommonResult().success(pageCoupon);
    }

    @ApiOperation("获取单个优惠券的详细信息")
    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        CouponRelationParam couponRelationParam = couponService.getCouponRelationParam(id);
        return new CommonResult().success(couponRelationParam);
    }

    @ApiOperation("创建优惠券")
    @PostMapping("/create")
    public Object create(@RequestBody CouponRelationParam couponRelationParam) {
        couponService.createCouponRelationParam(couponRelationParam);
        return new CommonResult().success(null);
    }

    @ApiOperation("删除优惠券")
    @PostMapping("/delete/{id}")
    public Object delete(@PathVariable Long id) {
        couponService.deleteById(id);
        return new CommonResult().success(null);
    }

    @ApiOperation("更新优惠券")
    @PostMapping("/update/{id}")
    public Object update(@RequestBody CouponRelationParam couponRelationParam, @PathVariable Long id) {
        couponService.updateCouponInfoById(couponRelationParam, id);
        return new CommonResult().success(null);
    }
}
