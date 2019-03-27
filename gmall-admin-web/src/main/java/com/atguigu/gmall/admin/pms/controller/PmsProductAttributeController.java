package com.atguigu.gmall.admin.pms.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.pms.entity.ProductAttribute;
import com.atguigu.gmall.pms.service.ProductAttributeService;
import com.atguigu.gmall.pms.vo.PmsProductAttributeParam;
import com.atguigu.gmall.pms.vo.ProductAttrInfo;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品属性管理Controller
 */
@CrossOrigin
@RestController
@Api(tags = "PmsProductAttributeController", description = "商品属性管理")
@RequestMapping("/productAttribute")
public class PmsProductAttributeController {
    @Reference
    private ProductAttributeService productAttributeService;

    @ApiOperation("根据分类查询属性列表或参数列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "0表示属性，1表示参数", required = true, paramType = "query", dataType = "Integer")})
    @GetMapping(value = "/list/{cid}")
    public Object getList(@PathVariable Long cid,
                          @RequestParam(value = "type") Integer type,
                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        // 根据分类查询属性列表或参数列表
        Map<String,Object> pageProductAttribute=productAttributeService.pageProductAttribute(cid,type,pageNum,pageSize);
        return new CommonResult().success(pageProductAttribute);
    }

    @ApiOperation("添加商品属性信息")
    @PostMapping(value = "/create")
    public Object create(@Validated @RequestBody PmsProductAttributeParam productAttributeParam, BindingResult bindingResult) {
        // 添加商品属性信息
        boolean b = productAttributeService.createProductAttribute(productAttributeParam);
        return new CommonResult().success(null);
    }

    @ApiOperation("修改商品属性信息")
    @PostMapping(value = "/update/{id}")
    public Object update(@PathVariable Long id, @Validated @RequestBody PmsProductAttributeParam productAttributeParam, BindingResult bindingResult){
        // 修改商品属性信息
        boolean b = productAttributeService.updateProductAttributeById(id,productAttributeParam);
        return new CommonResult().success(null);
    }

    @ApiOperation("查询单个商品属性")
    @GetMapping(value = "/{id}")
    public Object getItem(@PathVariable Long id){
        // 查询单个商品属性
        ProductAttribute productAttribute = productAttributeService.getById(id);
        return new CommonResult().success(productAttribute);
    }

    @ApiOperation("批量删除商品属性")
    @PostMapping(value = "/delete")
    public Object delete(@RequestParam("ids") List<Long> ids){
        // 批量删除商品属性
        boolean b = productAttributeService.removeByIds(ids);
        return new CommonResult().success(null);
    }

    @ApiOperation("根据商品分类的id获取商品属性及属性分类")
    @GetMapping(value = "/attrInfo/{productCategoryId}")
    public Object getAttrInfo(@PathVariable Long productCategoryId){
        //TODO 根据分类查询属性列表或参数列表
         List<ProductAttrInfo> productAttrInfoList = productAttributeService.getAttrInfoList(productCategoryId);
        return new CommonResult().success(productAttrInfoList);
    }
}
