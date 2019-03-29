package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.HomeRecommendProduct;
import com.atguigu.gmall.sms.service.HomeRecommendProductService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/3/28 0028 上午 10:32
 * @description：首页人气推荐管理
 * @modified By：
 * @version: $
 */
@Api(description = "首页人气推荐管理")
@CrossOrigin
@RestController
@RequestMapping("/home/recommendProduct")
public class SmsHomeRecommendProductController {
    @Reference
    HomeRecommendProductService homeRecommendProductService;

    @ApiOperation("分页查询推荐")
    @GetMapping("/list")
    public Object list(@RequestParam(value = "productName", required = false) String productName,
                       @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Map<String, Object> pageInfo = homeRecommendProductService.pageHomeRecommendProduct(productName, recommendStatus, pageNum, pageSize);
        return new CommonResult().success(pageInfo);
    }

    @ApiOperation("添加首页人气推荐")
    @PostMapping("/create")
    public Object create(@RequestBody List<HomeRecommendProduct> homeRecommendProductList) {
        boolean b = homeRecommendProductService.create(homeRecommendProductList);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("批量删除推荐")
    @PostMapping("/delete")
    public Object delete(@RequestParam List<Long> ids) {
        boolean b = homeRecommendProductService.delete(ids);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("批量修改推荐状态")
    @PostMapping("/update/recommendStatus")
    public Object updateRecommendStatus(@RequestParam Integer recommendStatus,
                                        @RequestParam List<Long> ids) {
        boolean b = homeRecommendProductService.updateRecommendStatus(recommendStatus, ids);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("修改推荐排序")
    @PostMapping("/update/sort/{id}")
    public Object updateSort(@PathVariable Long id,
                             @RequestParam Integer sort) {
        boolean b = homeRecommendProductService.updateSort(id, sort);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }
}
