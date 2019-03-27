package com.atguigu.gmall.admin.ums.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.to.CommonResult;
import com.atguigu.gmall.ums.entity.MemberLevel;
import com.atguigu.gmall.ums.service.MemberLevelService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：mei
 * @date ：Created in 2019/3/22 0022 下午 18:23
 * @description：会员管理
 * @modified By：
 * @version: $
 */
@CrossOrigin
@RestController
@Api(tags = "MemberController", description = "会员管理")
public class UmsMemberController {

    @Reference
    MemberLevelService memberLevelService;

    @GetMapping("/memberLevel/list")
    public Object memberLevel(@RequestParam(value = "defaultStatus",defaultValue = "0") Integer defaultStatus){
        List<MemberLevel> memberLevelList = memberLevelService.getMemberLevelByStatus(defaultStatus);
        return new CommonResult().success(memberLevelList);
    }
}
