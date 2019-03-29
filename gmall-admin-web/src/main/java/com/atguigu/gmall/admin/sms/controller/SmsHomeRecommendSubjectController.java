package com.atguigu.gmall.admin.sms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.sms.entity.HomeRecommendSubject;
import com.atguigu.gmall.sms.service.HomeRecommendSubjectService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：mei
 * @date ：Created in 2019/3/28 0028 下午 15:54
 * @description：首页专题推荐管理
 * @modified By：
 * @version: $
 */
@Api(description = "首页专题推荐管理")
@CrossOrigin
@RestController
@RequestMapping("/home/recommendSubject")
public class SmsHomeRecommendSubjectController {
    @Reference
    HomeRecommendSubjectService homeRecommendSubjectService;

    @ApiOperation("分页查询推荐")
    @GetMapping("/list")
    public Object list(@RequestParam(value = "subjectName", required = false) String subjectName,
                       @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Map<String, Object> pageInfo = homeRecommendSubjectService.pageHomeRecommendSubject(subjectName, recommendStatus, pageNum, pageSize);
        return new CommonResult().success(pageInfo);
    }

    @ApiOperation("添加首页推荐专题")
    @PostMapping("/create")
    public Object create(@RequestBody List<HomeRecommendSubject> homeRecommendSubjectList) {
        boolean b = homeRecommendSubjectService.create(homeRecommendSubjectList);
        return new CommonResult().success(null);
    }

    @ApiOperation("批量删除推荐")
    @PostMapping("/delete")
    public Object delete(@RequestParam List<Long> ids) {
        boolean b = homeRecommendSubjectService.delete(ids);
        return new CommonResult().success(null);
    }

    @ApiOperation("批量修改推荐状态")
    @PostMapping("/update/recommendStatus")
    public Object updateRecommendStatus(@RequestParam List<Long> ids,
                                        @RequestParam Integer recommendStatus) {
        boolean b = homeRecommendSubjectService.updateRecommendStatus(ids, recommendStatus);
        return new CommonResult().success(null);
    }

    @ApiOperation("修改推荐排序")
    @PostMapping("/update/sort/{id}")
    public Object updateSort(@RequestParam Integer sort,
                             @PathVariable Long id) {
        boolean b = homeRecommendSubjectService.updateSort(id, sort);
        return new CommonResult().success(null);
    }
}
