package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.CouponHistory;
import com.atguigu.gmall.sms.mapper.CouponHistoryMapper;
import com.atguigu.gmall.sms.service.CouponHistoryService;
import com.atguigu.gmall.utils.PageMapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <p>
 * 优惠券使用、领取历史表 服务实现类
 * </p>
 *
 * @author mei
 * @since 2019-03-25
 */
@Service
@Component
public class CouponHistoryServiceImpl extends ServiceImpl<CouponHistoryMapper, CouponHistory> implements CouponHistoryService {

    @Override
    public Map<String, Object> pageCouponHistory(Long couponId, Integer useStatus, String orderSn, Integer pageNum, Integer pageSize) {
        Page<CouponHistory> page = new Page<>(pageNum, pageSize);
        QueryWrapper<CouponHistory> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(couponId)){
            queryWrapper.eq("coupon_id",couponId);
        }
        if(!StringUtils.isEmpty(useStatus)){
            queryWrapper.eq("use_status",useStatus);
        }
        if (!StringUtils.isEmpty(orderSn)){
            queryWrapper.eq("order_sn",orderSn);
        }
        IPage<CouponHistory> pageInfo = baseMapper.selectPage(page, queryWrapper);
        Map<String, Object> map = PageMapUtils.getPageMap(pageInfo);
        return map;
    }
}
