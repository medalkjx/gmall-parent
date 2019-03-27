package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.FlashPromotionSession;
import com.atguigu.gmall.sms.service.FlashPromotionSessionService;
import com.atguigu.gmall.sms.vo.FlashPromotionSessionCountResult;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ：mei
 * @date ：Created in 2019/3/25 0025 下午 20:13
 * @description：限时购场次管理
 * @modified By：
 * @version: $
 */
@Api(description = "限时购场次管理")
@CrossOrigin
@RequestMapping("/flashSession")
@RestController
public class SmsFlashPromotionSessionController {
    @Reference
    FlashPromotionSessionService flashPromotionSessionService;

    @ApiOperation("获取全部场次")
    @GetMapping("/list")
    public Object list() {
        List<FlashPromotionSession> promotionSessionList = flashPromotionSessionService.selectList();
        return new CommonResult().success(promotionSessionList);
    }

    @ApiOperation("创建场次")
    @PostMapping("/create")
    public Object create(@RequestBody FlashPromotionSession flashPromotionSession) {
        boolean result = flashPromotionSessionService.save(flashPromotionSession);
        if (result) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("删除场次")
    @PostMapping("/delete/{id}")
    public Object delete(@PathVariable Long id) {
        boolean result = flashPromotionSessionService.removeById(id);
        if (result) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("修改场次")
    @PostMapping("/update/{id}")
    public Object update(@PathVariable Long id,
                         @RequestBody FlashPromotionSession flashPromotionSession) {
        flashPromotionSession.setId(id);
        boolean result = flashPromotionSessionService.updateById(flashPromotionSession);
        if (result) {
            return new CommonResult().success(null);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("修改启用状态")
    @PostMapping("/update/status/{id}")
    public Object updateStatus(@PathVariable Long id,
                               @RequestParam Integer status) {
        flashPromotionSessionService.updateStatus(id, status);
        return new CommonResult().success(null);
    }

    @ApiOperation("获取场次详情")
    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        FlashPromotionSession flashPromotionSession = flashPromotionSessionService.getById(id);
        return new CommonResult().success(flashPromotionSession);
    }

    @ApiOperation("获取全部可选场次及其数量")
    @GetMapping("/selectList")
    public Object selectList(@RequestParam Long flashPromotionId){
        List<FlashPromotionSessionCountResult> items = flashPromotionSessionService.getFlashPromotionSessionCount(flashPromotionId);
        return new CommonResult().success(items);
    }


}
