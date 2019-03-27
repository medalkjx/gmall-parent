package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.HomeBrand;
import com.atguigu.gmall.sms.service.HomeBrandService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/3/27 0027 上午 11:52
 * @description：首页品牌管理
 * @modified By：
 * @version: $
 */
@Api(description = "首页品牌管理")
@CrossOrigin
@RequestMapping("/home/brand")
@RestController
public class SmsHomeBrandController {
    @Reference
    HomeBrandService homeBrandService;

    @ApiOperation("分页查询推荐品牌")
    @GetMapping("/list")
    public Object list(@RequestParam(value = "品牌名称", required = false) String brandName,
                       @RequestParam(value = "推荐状态", required = false) Integer recommendStatus,
                       @RequestParam(value = "页码", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "当前页面显示条数", defaultValue = "5") Integer pageSize) {
        Map<String, Object> pageInfo = homeBrandService.pageHomeBrand(brandName, recommendStatus, pageNum, pageSize);
        return new CommonResult().success(pageInfo);
    }

    @ApiOperation("添加首页推荐品牌")
    @PostMapping("/create")
    public Object create(@RequestBody List<HomeBrand> homeBrandList) {
        boolean b = homeBrandService.createHomeBrands(homeBrandList);
        return new CommonResult().success(null);
    }

    @ApiOperation("批量删除推荐商品")
    @PostMapping("/delete")
    public Object delete(@RequestParam List<Long> ids) {
        boolean b = homeBrandService.deleteByIds(ids);
        return new CommonResult().success(null);
    }

    @ApiOperation("批量修改推荐状态")
    @PostMapping("/update/recommendStatus")
    public Object updateRecommendStatus(@RequestParam List<Long> ids,
                                        @RequestParam Integer recommendStatus) {
        boolean b = homeBrandService.updateRecommendStatus(ids, recommendStatus);
        return new CommonResult().success(null);
    }

    @ApiOperation("修改品牌排序")
    @PostMapping("/update/sort/{id}")
    public Object updateSortById(@PathVariable Long id,
                                 @RequestParam Integer sort) {
        boolean b = homeBrandService.updateSortById(id, sort);
        return new CommonResult().success(null);
    }
}
