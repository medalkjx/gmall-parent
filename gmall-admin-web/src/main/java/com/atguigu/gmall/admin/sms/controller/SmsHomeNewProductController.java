package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.HomeNewProduct;
import com.atguigu.gmall.sms.service.HomeNewProductService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/3/27 0027 下午 20:29
 * @description：首页新品管理
 * @modified By：
 * @version: $
 */
@Api(description = "首页新品管理")
@CrossOrigin
@RestController
@RequestMapping("/home/newProduct")
public class SmsHomeNewProductController {

    @Reference
    HomeNewProductService homeNewProductService;

    @ApiOperation("分页查询推荐")
    @GetMapping("/list")
    public Object list(@RequestParam(value = "productName", required = false) String productName,
                       @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Map<String, Object> pageInfo = homeNewProductService.pageHomeNewProduct(productName, recommendStatus, pageNum, pageSize);
        return new CommonResult().success(pageInfo);
    }

    @ApiOperation("添加首页推荐品牌")
    @PostMapping("/create")
    public Object create(@RequestBody List<HomeNewProduct> list) {
        boolean b = homeNewProductService.create(list);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("批量删除推荐")
    @PostMapping("/delete")
    public Object delete(@RequestParam List<Long> ids) {
        boolean b = homeNewProductService.deleteByIds(ids);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("批量修改推荐状态")
    @PostMapping("/update/recommendStatus")
    public Object updateRecommendStatus(@RequestParam List<Long> ids,
                                        @RequestParam Integer recommendStatus) {
        boolean b = homeNewProductService.updateRecommendStatus(ids, recommendStatus);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("修改推荐排序")
    @PostMapping("/update/sort/{id}")
    public Object updateSort(@RequestParam Integer sort, @PathVariable Long id) {
        boolean b = homeNewProductService.updateSortById(sort, id);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }
}
