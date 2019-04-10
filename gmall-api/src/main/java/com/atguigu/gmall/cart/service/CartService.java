package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.cart.bean.Cart;
import com.atguigu.gmall.cart.bean.CartItem;
import com.atguigu.gmall.cart.bean.SkuResponse;

import java.util.List;

/**
 * @author ：mei
 * @date ：Created in 2019/4/2 0002 上午 0:14
 * @description：购物车服务
 * @modified By：
 * @version: $
 */
public interface CartService {
    /**
     * 添加到购物车
     *
     * @param skuId
     * @param num
     * @param cartKey
     * @return
     */
    SkuResponse addToCart(Long skuId, Integer num, String cartKey);

    /**
     * 获取购物车列表
     *
     * @param cartKey
     * @return
     */
    Cart cartItemsList(String cartKey);

    /**
     * 选择购物车商品
     *
     * @param skuId
     * @param flag
     * @param cartKey
     * @return
     */
    boolean checkCart(Long skuId, Integer flag, String cartKey);

    /**
     * 删除购物车商品
     *
     * @param skuId
     * @param cartKey
     * @return
     */
    boolean deleteCart(Long skuId, String cartKey);

    /**
     * 修改购物车商品数量
     *
     * @param skuId
     * @param num
     * @param cartKey
     * @return
     */
    boolean updateCount(Long skuId, Integer num, String cartKey);


    /**
     * 根据用户id获取购物车项
     * @param id
     * @return
     */
    List<CartItem> cartItemsForAction(Long id);

}
