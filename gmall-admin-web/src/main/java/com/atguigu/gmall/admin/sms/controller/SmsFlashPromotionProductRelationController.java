package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.FlashPromotionProductRelation;
import com.atguigu.gmall.sms.service.FlashPromotionProductRelationService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/3/29 0029 上午 8:37
 * @description：限时购和商品关系管理
 * @modified By：
 * @version: $
 */
@Api(description = "限时购和商品关系管理")
@CrossOrigin
@RestController
@RequestMapping("/flashProductRelation")
public class SmsFlashPromotionProductRelationController {
    @Reference
    FlashPromotionProductRelationService flashPromotionProductRelationService;

    @ApiOperation("分页查询不同场次关联及商品信息")
    @GetMapping("/list")
    public Object list(@RequestParam Long flashPromotionId,
                       @RequestParam Long flashPromotionSessionId,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Map<String, Object> pageInfo = flashPromotionProductRelationService.pageFlashPromotionProductRelation(flashPromotionId, flashPromotionSessionId, pageNum, pageSize);
        return new CommonResult().success(pageInfo);
    }

    @ApiOperation("批量选择商品添加关联")
    @PostMapping("/create")
    public Object create(@RequestBody List<FlashPromotionProductRelation> flashPromotionProductRelationList) {
        boolean b = flashPromotionProductRelationService.create(flashPromotionProductRelationList);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("删除关联")
    @PostMapping("/delete/{id}")
    public Object delete(@PathVariable Long id) {
        boolean b = flashPromotionProductRelationService.removeById(id);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("修改关联相关信息")
    @PostMapping("/update/{id}")
    public Object update(@PathVariable Long id,
                         @RequestBody FlashPromotionProductRelation flashPromotionProductRelation) {
        flashPromotionProductRelation.setId(id);
        boolean b = flashPromotionProductRelationService.updateById(flashPromotionProductRelation);
        if (b) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("获取管理商品促销信息")
    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        FlashPromotionProductRelation flashPromotionProductRelation = flashPromotionProductRelationService.getById(id);
        return new CommonResult().success(flashPromotionProductRelation);
    }
}
