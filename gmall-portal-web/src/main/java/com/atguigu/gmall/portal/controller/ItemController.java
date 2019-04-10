package com.atguigu.gmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.item.vo.ProductAllInfos;
import com.atguigu.gmall.item.vo.ItemService;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：mei
 * @date ：Created in 2019/3/29 0029 下午 19:02
 * @description：
 * @modified By：
 * @version: $
 */
@CrossOrigin
@RestController
@RequestMapping("/item")
public class ItemController {

    @Reference
    ItemService itemService;

    @GetMapping(value = "/{skuId}.html", produces = "application/json")
    public ProductAllInfos productInfo(@PathVariable("skuId") Long skuId) {
        ProductAllInfos allInfos = itemService.getInfo(skuId);
        return allInfos;
    }
}
