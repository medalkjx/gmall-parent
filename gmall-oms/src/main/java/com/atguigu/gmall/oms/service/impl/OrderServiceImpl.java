package com.atguigu.gmall.oms.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gmall.cart.bean.Cart;
import com.atguigu.gmall.cart.bean.CartItem;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.constant.OrderStatusEnum;
import com.atguigu.gmall.constant.RedisCacheConstant;
import com.atguigu.gmall.oms.config.AlipayConfig;
import com.atguigu.gmall.oms.entity.Order;
import com.atguigu.gmall.oms.entity.OrderItem;
import com.atguigu.gmall.oms.mapper.OrderItemMapper;
import com.atguigu.gmall.oms.mapper.OrderMapper;
import com.atguigu.gmall.oms.service.OrderService;
import com.atguigu.gmall.oms.vo.OrderConfirmPageVo;
import com.atguigu.gmall.oms.vo.OrderResponseVo;
import com.atguigu.gmall.oms.vo.OrderSubmitVo;
import com.atguigu.gmall.pms.entity.SkuStock;
import com.atguigu.gmall.pms.service.SkuStockService;
import com.atguigu.gmall.to.OrderMQTo;
import com.atguigu.gmall.ums.entity.Member;
import com.atguigu.gmall.ums.entity.MemberReceiveAddress;
import com.atguigu.gmall.ums.service.MemberReceiveAddressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-04-03
 */
@Component
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    JedisPool jedisPool;
    @Reference
    SkuStockService skuStockService;
    @Reference
    MemberReceiveAddressService memberReceiveAddressService;
    @Reference
    CartService cartService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public OrderConfirmPageVo createOrderInfo(String token) {
        OrderConfirmPageVo orderConfirmPageVo = new OrderConfirmPageVo();
        String memberJson = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + token);
        Member member = JSON.parseObject(memberJson, Member.class);
        Long id = member.getId();
        String cartKey = RedisCacheConstant.USER_CART + id;
        //结算的商品信息
        RMap<String, String> map = redissonClient.getMap(cartKey);
        String checked = map.get("checked");
        Set<String> checkItems = JSON.parseObject(checked, new TypeReference<Set<String>>() {
        });
        List<CartItem> cartItems = new ArrayList<>();
        if (checkItems != null && !checkItems.isEmpty()) {
            checkItems.forEach(checkedItem -> {
                String s = map.get(checkedItem);
                CartItem cartItem = JSON.parseObject(s, CartItem.class);
                Long skuId = cartItem.getProductSkuId();
                SkuStock skuStock = skuStockService.getById(id);
                BigDecimal price = skuStock.getPrice();
                if (cartItem.getNewPrice() != null) {
                    cartItem.setPrice(cartItem.getNewPrice());
                }
                cartItem.setNewPrice(price);
                cartItems.add(cartItem);
            });
        }
        orderConfirmPageVo.setCartItems(cartItems);
        //用户可选的地址列表
        List<MemberReceiveAddress> memberReceiveAddresses = memberReceiveAddressService.selectMemberReceiveAddressByMemberId(id);
        orderConfirmPageVo.setMemberReceiveAddresses(memberReceiveAddresses);
        //临时令牌防重
        String replace = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(RedisCacheConstant.TRADE_TOKEN + token, replace, RedisCacheConstant.TRADE_TOKEN_TIMEOUT, TimeUnit.MINUTES);
        orderConfirmPageVo.setTradeToken(replace);
        return orderConfirmPageVo;
    }

    @Transactional
    @Override
    public OrderResponseVo createOrder(OrderSubmitVo orderSubmitVo) {
        //TODO 创建订单
        String tradeToken = orderSubmitVo.getTradeToken();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Jedis jedis = jedisPool.getResource();
        Long eval = (Long) jedis.eval(script, Collections.singletonList(RedisCacheConstant.TRADE_TOKEN + orderSubmitVo.getToken()),
                Collections.singletonList(tradeToken));
        if (eval == 1) {
            Order order = new Order();
            String s = redisTemplate.opsForValue().get(RedisCacheConstant.USER_INFO_CACHE_KEY + orderSubmitVo.getToken());
            Member member = JSON.parseObject(s, Member.class);
            order.setMemberId(member.getId());
            order.setMemberUsername(member.getUsername());
            //查出用户收货地址
            Long addressId = orderSubmitVo.getAddressId();
            MemberReceiveAddress address = memberReceiveAddressService.getById(addressId);
            order.setReceiverCity(address.getCity());
            order.setReceiverDetailAddress(address.getDetailAddress());
            order.setReceiverName(address.getName());
            order.setReceiverPhone(address.getPhoneNumber());
            order.setReceiverProvince(address.getProvince());
            order.setReceiverPostCode(address.getPostCode());
            order.setReceiverRegion(address.getRegion());
            //计算订单总额
            List<CartItem> cartItems = cartService.cartItemsForAction(member.getId());
            Cart cart = new Cart();
            cart.setItems(cartItems);
            BigDecimal totalPrice = cart.getTotalPrice();
            order.setTotalAmount(totalPrice);
            //订单状态
            order.setStatus(OrderStatusEnum.UNPAY.getCode());
            //订单号拼接
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String prefix = format.format(new Date());
            Long countId = redisTemplate.opsForValue().increment("orderCountId");
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumIntegerDigits(9);
            numberFormat.setMinimumIntegerDigits(9);
            String suffix = numberFormat.format(countId);
            String orderSn = prefix + suffix.replace(",", "");
            order.setOrderSn(orderSn);
            //保存订单
            baseMapper.insert(order);
            //保存订单项信息
            ArrayList<OrderItem> orderItems = new ArrayList<>();
            cartItems.forEach(cartItem -> {
                OrderItem orderItem = new OrderItem();
                BeanUtils.copyProperties(cartItem, orderItem);
                orderItem.setProductQuantity(cartItem.getNum());
                orderItem.setRealAmount(cartItem.getNewPrice());
                orderItem.setOrderSn(orderSn);
                orderItem.setOrderId(order.getId());
                orderItems.add(orderItem);
                orderItemMapper.insert(orderItem);
            });
            //批量保存订单信息
            //orderItemMapper.insertBatch(orderItems);
            //发布订单创建完成消息
            OrderMQTo orderMQTo = new OrderMQTo(order, orderItems);
            rabbitTemplate.convertAndSend("orderExchange", "order", orderMQTo);

            return new OrderResponseVo("订单创建成功!", 0, orderSn, order.getTotalAmount().toString(),
                    "订单提交成功,请尽快付款:" + orderSn, orderItems.get(0).getProductName());

        } else {
            //TODO 自定义异常替代
            throw new RuntimeException("令牌过期..,请重新结算");

        }
    }

    @Override
    public String payOrder(String out_trade_no, String total_amount, String subject, String body) {
        //验价
        BigDecimal bigDecimal = new BigDecimal(total_amount);
        Order orderSn = baseMapper.selectOne(new QueryWrapper<Order>().eq("order_sn", out_trade_no));
        System.out.println(orderSn.getTotalAmount());
        System.out.println(bigDecimal);
        if (!orderSn.getTotalAmount().equals(bigDecimal)) {
            //TODO 自定义异常
            throw new RuntimeException("前端非法提交请求");
        }
        // 1、创建支付宝客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
                AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);

        // 2、创建一次支付请求
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        // 付款金额，必填
        // 订单名称，必填
        // 商品描述，可空

        // 3、构造支付请求数据
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\"" + total_amount
                + "\"," + "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = "";
        try {
            // 4、请求
            result = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;// 支付跳转页的代码

    }

    @Override
    public void updateOrderStatus(String out_trade_no, OrderStatusEnum status) {
        Order order = new Order();
        order.setStatus(status.getCode());
        baseMapper.update(order, new QueryWrapper<Order>().eq("order_sn", out_trade_no));
    }

}
