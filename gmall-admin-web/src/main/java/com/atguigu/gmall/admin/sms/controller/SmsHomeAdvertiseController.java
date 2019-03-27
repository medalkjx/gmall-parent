package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.service.HomeAdvertiseService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/3/26 0026 上午 8:48
 * @description：首页轮播广告管理
 * @modified By：
 * @version: $
 */

@Api("首页轮播广告管理")
@CrossOrigin
@RestController
@RequestMapping("/home/advertise")
public class SmsHomeAdvertiseController {

    @Reference
    HomeAdvertiseService homeAdvertiseService;

    @ApiOperation("分页查询广告")
    @GetMapping("/list")
    public Object list(@RequestParam(value = "广告名称",required = false) String name ,
                       @RequestParam(value = "广告类型",required = false)Integer type ,
                       @RequestParam(value = "广告结束时间",required = false)String endTime,
                       @RequestParam(value = "页码",defaultValue = "1")Integer pageNum ,
                       @RequestParam(value = "页面显示条数",defaultValue = "5") Integer pageSize){

        Map<String ,Object> pageInfo =homeAdvertiseService.pageHomeAdvertise(name,type,endTime,pageNum,pageSize);
        return new CommonResult().success(pageInfo);
    }

}
