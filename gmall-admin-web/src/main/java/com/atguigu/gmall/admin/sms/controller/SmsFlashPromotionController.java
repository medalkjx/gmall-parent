package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.FlashPromotion;
import com.atguigu.gmall.sms.service.FlashPromotionService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author ：mei
 * @date ：Created in 2019/3/25 0025 下午 19:16
 * @description：限时购活动管理
 * @modified By：
 * @version: $
 */

@Api(description = "限时购活动管理")
@CrossOrigin
@RequestMapping("/flash")
@RestController
public class SmsFlashPromotionController {
    @Reference
    FlashPromotionService flashPromotionService;

    @ApiOperation("根据活动名称分页查询")
    @GetMapping("/list")
    public Object list(@RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        Map<String, Object> pageInfo = flashPromotionService.pageFlashPromotion(keyword, pageNum, pageSize);
        return new CommonResult().success(pageInfo);
    }

    @ApiOperation("获取活动详情")
    @GetMapping("/{id}")
    public Object getByid(@PathVariable Long id) {
        FlashPromotion flashPromotion = flashPromotionService.getById(id);
        return new CommonResult().success(flashPromotion);
    }

    @ApiOperation("创建活动")
    @PostMapping("/create")
    public Object create(@RequestBody FlashPromotion flashPromotion) {
        boolean result = flashPromotionService.save(flashPromotion);
        if (result) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("删除活动")
    @PostMapping("/delete/{id}")
    public Object delete(@PathVariable Long id) {
        boolean result = flashPromotionService.removeById(id);
        if (result) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("修改上下线状态")
    @PostMapping("/update/status/{id}")
    public Object updateStatus(@PathVariable Long id,
                               @RequestParam Integer status) {
        flashPromotionService.updateStatusById(id, status);
        return new CommonResult().success(null);
    }

    @ApiOperation("修改活动信息")
    @PostMapping("/update/{id}")
    public Object update(@PathVariable Long id,
                         @RequestBody FlashPromotion flashPromotion) {
        flashPromotion.setId(id);
        flashPromotionService.updateById(flashPromotion);
        return new CommonResult().success(null);
    }
}
