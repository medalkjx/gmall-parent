package com.atguigu.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.gmall.cart.bean.Cart;
import com.atguigu.gmall.cart.bean.CartItem;
import com.atguigu.gmall.cart.bean.SkuResponse;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.pms.service.ProductService;
import com.atguigu.gmall.pms.service.SkuStockService;
import com.atguigu.gmall.sms.entity.Coupon;
import com.atguigu.gmall.sms.service.CouponService;
import com.atguigu.gmall.ums.entity.Member;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author ：mei
 * @date ：Created in 2019/4/2 0002 上午 0:30
 * @description：
 * @modified By：
 * @version: $
 */
@Component
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Reference
    SkuStockService skuStockService;
    @Reference
    ProductService productService;
    @Reference
    CouponService couponService;
    @Autowired
    RedissonClient redissonClient;

    @Override
    public SkuResponse addToCart(Long skuId, Integer num, String cartKey) {
        SkuResponse skuResponse = new SkuResponse();
        String token = RpcContext.getContext().getAttachment("gmallusertoken");
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);
        Long memberId = member == null ? 0L : member.getId();
        String memberName = member == null ? "" : member.getNickname();
        SkuStock skuStock = skuStockService.getById(skuId);
        Product product = productService.getProductByIdFromCache(skuStock.getProductId());
        Coupon coupon = couponService.getCouponByProductId(product.getId());
        String couponNote = "";
        if (coupon != null) {
            couponNote = coupon.getNote();
        }
        CartItem item = new CartItem(product.getId(),
                skuStock.getId(),
                memberId,
                num,
                skuStock.getPrice(),
                skuStock.getPrice(),
                num,
                skuStock.getSp1(), skuStock.getSp2(), skuStock.getSp3(),
                product.getPic(),
                product.getName(),
                memberName,
                product.getProductCategoryId(),
                product.getBrandName(),
                false,
                couponNote);
        if (StringUtils.isEmpty(memberJson)) {
            if (!StringUtils.isEmpty(cartKey)) {
                skuResponse.setCartKey(cartKey);
                cartKey = RedisCacheConstant.CART_TEMP + cartKey;
                addItemToCart(item, num, cartKey);
            } else {
                String replace = UUID.randomUUID().toString().replace("-", "");
                String newCartKey = RedisCacheConstant.CART_TEMP + replace;
                skuResponse.setCartKey(replace);
                addItemToCart(item, num, newCartKey);
            }
        } else {
            String loginCartKey = RedisCacheConstant.USER_CART + member.getId();
            mergeCart(RedisCacheConstant.CART_TEMP + cartKey, loginCartKey);
            addItemToCart(item, num, loginCartKey);
        }
        skuResponse.setItem(item);
        return skuResponse;
    }

    private void mergeCart(String oldCartKey, String loginCartKey) {
        RMap<String, String> map = redissonClient.getMap(oldCartKey);
        if (map != null && map.entrySet() != null) {
            map.entrySet().forEach(entry -> {
                String key = entry.getKey();
                if (!"checked".equals(key)) {
                    String value = entry.getValue();
                    CartItem item = JSON.parseObject(value, CartItem.class);
                    addItemToCart(item, item.getNum(), loginCartKey);
                    map.remove(item.getProductSkuId() + "");
                }
            });
        }
    }

    private void addItemToCart(CartItem item, Integer num, String cartKey) {
        RMap<String, String> map = redissonClient.getMap(cartKey);
        boolean b = map.containsKey(item.getProductSkuId() + "");
        if (b) {
            String json = map.get(item.getProductSkuId() + "");
            CartItem cartItem = JSON.parseObject(json, CartItem.class);
            cartItem.setNum(cartItem.getNum() + num);
            cartItem.setTotal(cartItem.getTotal() + num);
            String string = JSON.toJSONString(cartItem);
            map.put(item.getProductSkuId() + "", string);
        } else {
            String string = JSON.toJSONString(item);
            map.put(item.getProductSkuId() + "", string);
        }
    }

    @Override
    public Cart cartItemsList(String cartKey) {
        String token = RpcContext.getContext().getAttachment("gmallusertoken");
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);
        RMap<String, String> map = null;
        if (member == null) {
            map = redissonClient.getMap(RedisCacheConstant.CART_TEMP + cartKey);
        } else {
            mergeCart(RedisCacheConstant.CART_TEMP + cartKey, RedisCacheConstant.USER_CART + member.getId());
            map = redissonClient.getMap(RedisCacheConstant.USER_CART + member.getId());
        }
        if (map != null) {
            Cart cart = new Cart();
            cart.setItems(new ArrayList<CartItem>());
            map.entrySet().forEach(o -> {
                if (!"checked".equals(o.getKey())) {
                    String json = o.getValue();
                    CartItem cartItem = JSON.parseObject(json, CartItem.class);
                    cart.getItems().add(cartItem);
                }
            });
            return cart;
        } else {
            return new Cart();
        }
    }

    @Override
    public boolean checkCart(Long skuId, Integer flag, String cartKey) {
        String token = RpcContext.getContext().getAttachment("gmallusertoken");
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);
        RMap<String, String> map = null;
        if (member == null) {
            map = redissonClient.getMap(RedisCacheConstant.CART_TEMP + cartKey);
        } else {
            map = redissonClient.getMap(RedisCacheConstant.USER_CART + member.getId());
        }
        String s = map.get(skuId + "");
        CartItem item = JSON.parseObject(s, CartItem.class);
        item.setChecked(flag == 0 ? false : true);
        String json = JSON.toJSONString(item);
        map.put(skuId + "", json);
        String checked = map.get("checked");
        Set<String> checkedSkuIds = new HashSet<>();
        if (!StringUtils.isEmpty(checked)) {
            Set<String> strings = JSON.parseObject(checked, new TypeReference<Set<String>>() {
            });
            if (flag == 0) {
                strings.remove(skuId + "");
            } else {
                strings.add(skuId + "");
            }
            String s1 = JSON.toJSONString(strings);
            map.put("checked", s1);
        } else {
            checkedSkuIds.add(skuId + "");
            String s1 = JSON.toJSONString(checkedSkuIds);
            map.put("checked", s1);
        }
        return true;
    }

    @Override
    public boolean deleteCart(Long skuId, String cartKey) {
        String token = RpcContext.getContext().getAttachment("gmallusertoken");
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);
        RMap<String, String> map = null;
        if (member == null) {
            map = redissonClient.getMap(RedisCacheConstant.CART_TEMP + cartKey);
        } else {
            map = redissonClient.getMap(RedisCacheConstant.USER_CART + member.getId());
        }
        map.remove(skuId + "");
        return true;
    }

    @Override
    public boolean updateCount(Long skuId, Integer num, String cartKey) {
        String token = RpcContext.getContext().getAttachment("gmallusertoken");
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);
        RMap<String, String> map = null;
        if (member == null) {
            map = redissonClient.getMap(RedisCacheConstant.CART_TEMP + cartKey);
        } else {
            map = redissonClient.getMap(RedisCacheConstant.USER_CART + member.getId());
        }
        String s = map.get(skuId + "");
        CartItem item = JSON.parseObject(s, CartItem.class);
        item.setNum(num);
        item.setTotal(num);
        String json = JSON.toJSONString(item);
        map.put(skuId + "", json);
        return true;
    }

    @Override
    public List<CartItem> cartItemsForAction(Long id) {
        String cartKey = RedisCacheConstant.USER_CART + id;
        RMap<String, String> map = redissonClient.getMap(cartKey);
        String checked = map.get("checked");
        Set<String> checkedItems = JSON.parseObject(checked, new TypeReference<Set<String>>() {
        });
        ArrayList<CartItem> cartItems = new ArrayList<>();
        if (checkedItems != null && !checkedItems.isEmpty()) {
            checkedItems.forEach(item -> {
                String s = map.get(item);
                CartItem cartItem = JSON.parseObject(s, CartItem.class);
                Long productSkuId = cartItem.getProductSkuId();
                BigDecimal price = skuStockService.getSkuPriceById(productSkuId);
                if (cartItem.getNewPrice() != null) {
                    cartItem.setPrice(cartItem.getNewPrice());
                }
                cartItem.setNewPrice(price);
                cartItems.add(cartItem);
            });
        }
        return cartItems;
    }


}
