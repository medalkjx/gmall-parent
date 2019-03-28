package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.HomeAdvertise;
import com.atguigu.gmall.sms.service.HomeAdvertiseService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/3/26 0026 上午 8:48
 * @description：首页轮播广告管理
 * @modified By：
 * @version: $
 */

@Api(description = "首页轮播广告管理")
@CrossOrigin
@RestController
@RequestMapping("/home/advertise")
public class SmsHomeAdvertiseController {

    @Reference
    HomeAdvertiseService homeAdvertiseService;

    @ApiOperation("分页查询广告")
    @GetMapping("/list")
    public Object list(@RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "endTime", required = false) String endTime,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {

        Map<String, Object> pageInfo = homeAdvertiseService.pageHomeAdvertise(name, type, endTime, pageNum, pageSize);
        return new CommonResult().success(pageInfo);
    }

    @ApiOperation("查询广告详情")
    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        HomeAdvertise homeAdvertise = homeAdvertiseService.getById(id);
        return new CommonResult().success(homeAdvertise);
    }

    @ApiOperation("创建广告")
    @PostMapping("/create")
    public Object create(@RequestBody HomeAdvertise advertise) {
        boolean b = homeAdvertiseService.save(advertise);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("删除广告")
    @PostMapping("/delete/{id}")
    public Object delete(@PathVariable List<Long> ids) {
        boolean b = homeAdvertiseService.removeByIds(ids);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("修改广告")
    @PostMapping("/update/{id}")
    public Object update(@RequestBody HomeAdvertise advertise, @PathVariable Long id) {
        advertise.setId(id);
        boolean b = homeAdvertiseService.updateById(advertise);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("修改广告状态")
    @PostMapping("/update/status/{id}")
    public Object updateStatus(@RequestParam Integer status, @PathVariable Long id) {
        boolean b = homeAdvertiseService.updateStatus(status, id);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }
}
