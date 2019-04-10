package com.atguigu.gmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.atguigu.gmall.cart.bean.Cart;
import com.atguigu.gmall.cart.bean.SkuResponse;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.to.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：mei
 * @date ：Created in 2019/4/2 0002 上午 0:19
 * @description：购物车服务
 * @modified By：
 * @version: $
 */
@CrossOrigin
@Api(description = "购物车")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    CartService cartService;

    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    public Object addToCart(@RequestParam Long skuId,
                            @RequestParam Integer num,
                            @RequestParam String token,
                            @RequestParam String cartKey) {

        RpcContext.getContext().setAttachment("gmallusertoken", token);
        SkuResponse skuResponse = cartService.addToCart(skuId, num, cartKey);
        return new CommonResult().success(skuResponse);
    }

    @ApiOperation("修改商品数量")
    @PostMapping("/update")
    public Object updateCart(@RequestParam Long skuId,
                             @RequestParam Integer num,
                             @RequestParam String token,
                             @RequestParam String cartKey) {

        RpcContext.getContext().setAttachment("gmallusertoken", token);
        boolean update = cartService.updateCount(skuId, num, cartKey);
        return new CommonResult().success(null);
    }

    @ApiOperation("从购物车删除商品")
    @PostMapping("/delete")
    public Object removeToCart(@RequestParam Long skuId,
                               @RequestParam String token,
                               @RequestParam String cartKey) {

        RpcContext.getContext().setAttachment("gmallusertoken", token);
        boolean delete = cartService.deleteCart(skuId, cartKey);
        return new CommonResult().success(null);
    }

    @ApiOperation("选择购物车商品")
    @PostMapping("/check")
    public Object checkToCart(@RequestParam Long skuId,
                              @RequestParam Integer flag,
                              @RequestParam String token,
                              @RequestParam String cartKey) {

        RpcContext.getContext().setAttachment("gmallusertoken", token);
        boolean check = cartService.checkCart(skuId, flag, cartKey);
        return new CommonResult().success(null);
    }

    @ApiOperation("查看购物车列表")
    @GetMapping("/list")
    public Object list(@RequestParam String token,
                       @RequestParam String cartKey) {
        RpcContext.getContext().setAttachment("gmallusertoken", token);
        Cart cart = cartService.cartItemsList(cartKey);
        return new CommonResult().success(cart);
    }
}
